package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import net.minecraft.util.CryptManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler<Packet<?>> {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public static final Marker NETWORK_MARKER = MarkerManager.getMarker("NETWORK");
  
  public static final Marker NETWORK_PACKETS_MARKER = MarkerManager.getMarker("NETWORK_PACKETS", NETWORK_MARKER);
  
  public static final AttributeKey<EnumConnectionState> PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("protocol");
  
  public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
      protected NioEventLoopGroup load() {
        return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
      }
    };
  
  public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
      protected EpollEventLoopGroup load() {
        return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build());
      }
    };
  
  public static final LazyLoadBase<LocalEventLoopGroup> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
      protected LocalEventLoopGroup load() {
        return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build());
      }
    };
  
  private final EnumPacketDirection direction;
  
  private final Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
  
  private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  
  private Channel channel;
  
  private SocketAddress socketAddress;
  
  private INetHandler packetListener;
  
  private ITextComponent terminationReason;
  
  private boolean isEncrypted;
  
  private boolean disconnected;
  
  public NetworkManager(EnumPacketDirection packetDirection) {
    this.direction = packetDirection;
  }
  
  public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
    super.channelActive(p_channelActive_1_);
    this.channel = p_channelActive_1_.channel();
    this.socketAddress = this.channel.remoteAddress();
    try {
      setConnectionState(EnumConnectionState.HANDSHAKING);
    } catch (Throwable throwable) {
      LOGGER.fatal(throwable);
    } 
  }
  
  public void setConnectionState(EnumConnectionState newState) {
    this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).set(newState);
    this.channel.config().setAutoRead(true);
    LOGGER.debug("Enabled auto read");
  }
  
  public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {
    closeChannel((ITextComponent)new TextComponentTranslation("disconnect.endOfStream", new Object[0]));
  }
  
  public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
    TextComponentTranslation textcomponenttranslation;
    if (p_exceptionCaught_2_ instanceof io.netty.handler.timeout.TimeoutException) {
      textcomponenttranslation = new TextComponentTranslation("disconnect.timeout", new Object[0]);
    } else {
      textcomponenttranslation = new TextComponentTranslation("disconnect.genericReason", new Object[] { "Internal Exception: " + p_exceptionCaught_2_ });
    } 
    LOGGER.debug(textcomponenttranslation.getUnformattedText(), p_exceptionCaught_2_);
    closeChannel((ITextComponent)textcomponenttranslation);
  }
  
  protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_) throws Exception {
    if (this.channel.isOpen())
      try {
        p_channelRead0_2_.processPacket(this.packetListener);
      } catch (ThreadQuickExitException threadQuickExitException) {} 
  }
  
  public void setNetHandler(INetHandler handler) {
    Validate.notNull(handler, "packetListener", new Object[0]);
    LOGGER.debug("Set listener of {} to {}", this, handler);
    this.packetListener = handler;
  }
  
  public void sendPacket(Packet<?> packetIn) {
    if (isChannelOpen()) {
      flushOutboundQueue();
      dispatchPacket(packetIn, null);
    } else {
      this.readWriteLock.writeLock().lock();
      try {
        this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]));
      } finally {
        this.readWriteLock.writeLock().unlock();
      } 
    } 
  }
  
  public void sendPacket(Packet<?> packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener... listeners) {
    if (isChannelOpen()) {
      flushOutboundQueue();
      dispatchPacket(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add((Object[])listeners, 0, listener));
    } else {
      this.readWriteLock.writeLock().lock();
      try {
        this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener<? extends Future<? super Void>>[])ArrayUtils.add((Object[])listeners, 0, listener)));
      } finally {
        this.readWriteLock.writeLock().unlock();
      } 
    } 
  }
  
  private void dispatchPacket(final Packet<?> inPacket, @Nullable final GenericFutureListener[] futureListeners) {
    final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
    final EnumConnectionState enumconnectionstate1 = (EnumConnectionState)this.channel.attr(PROTOCOL_ATTRIBUTE_KEY).get();
    if (enumconnectionstate1 != enumconnectionstate) {
      LOGGER.debug("Disabled auto read");
      this.channel.config().setAutoRead(false);
    } 
    if (this.channel.eventLoop().inEventLoop()) {
      if (enumconnectionstate != enumconnectionstate1)
        setConnectionState(enumconnectionstate); 
      ChannelFuture channelfuture = this.channel.writeAndFlush(inPacket);
      if (futureListeners != null)
        channelfuture.addListeners(futureListeners); 
      channelfuture.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    } else {
      this.channel.eventLoop().execute(new Runnable() {
            public void run() {
              if (enumconnectionstate != enumconnectionstate1)
                NetworkManager.this.setConnectionState(enumconnectionstate); 
              ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush(inPacket);
              if (futureListeners != null)
                channelfuture1.addListeners(futureListeners); 
              channelfuture1.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
          });
    } 
  }
  
  private void flushOutboundQueue() {
    if (this.channel != null && this.channel.isOpen()) {
      this.readWriteLock.readLock().lock();
      try {
        while (!this.outboundPacketsQueue.isEmpty()) {
          InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = this.outboundPacketsQueue.poll();
          dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
        } 
      } finally {
        this.readWriteLock.readLock().unlock();
      } 
    } 
  }
  
  public void processReceivedPackets() {
    flushOutboundQueue();
    if (this.packetListener instanceof ITickable)
      ((ITickable)this.packetListener).update(); 
    if (this.channel != null)
      this.channel.flush(); 
  }
  
  public SocketAddress getRemoteAddress() {
    return this.socketAddress;
  }
  
  public void closeChannel(ITextComponent message) {
    if (this.channel.isOpen()) {
      this.channel.close().awaitUninterruptibly();
      this.terminationReason = message;
    } 
  }
  
  public boolean isLocalChannel() {
    return !(!(this.channel instanceof LocalChannel) && !(this.channel instanceof io.netty.channel.local.LocalServerChannel));
  }
  
  public static NetworkManager createNetworkManagerAndConnect(InetAddress address, int serverPort, boolean useNativeTransport) {
    Class<NioSocketChannel> clazz;
    LazyLoadBase<NioEventLoopGroup> lazyLoadBase;
    final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
    if (Epoll.isAvailable() && useNativeTransport) {
      Class<EpollSocketChannel> clazz1 = EpollSocketChannel.class;
      LazyLoadBase<EpollEventLoopGroup> lazyLoadBase1 = CLIENT_EPOLL_EVENTLOOP;
    } else {
      clazz = NioSocketChannel.class;
      lazyLoadBase = CLIENT_NIO_EVENTLOOP;
    } 
    ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)lazyLoadBase.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
          protected void initChannel(Channel p_initChannel_1_) throws Exception {
            try {
              p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
            } catch (ChannelException channelException) {}
            p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("splitter", (ChannelHandler)new NettyVarint21FrameDecoder()).addLast("decoder", (ChannelHandler)new NettyPacketDecoder(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", (ChannelHandler)new NettyVarint21FrameEncoder()).addLast("encoder", (ChannelHandler)new NettyPacketEncoder(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", (ChannelHandler)networkmanager);
          }
        })).channel(clazz)).connect(address, serverPort).syncUninterruptibly();
    return networkmanager;
  }
  
  public static NetworkManager provideLocalClient(SocketAddress address) {
    final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
    ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group((EventLoopGroup)CLIENT_LOCAL_EVENTLOOP.getValue())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
          protected void initChannel(Channel p_initChannel_1_) throws Exception {
            p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
          }
        })).channel(LocalChannel.class)).connect(address).syncUninterruptibly();
    return networkmanager;
  }
  
  public void enableEncryption(SecretKey key) {
    this.isEncrypted = true;
    this.channel.pipeline().addBefore("splitter", "decrypt", (ChannelHandler)new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
    this.channel.pipeline().addBefore("prepender", "encrypt", (ChannelHandler)new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
  }
  
  public boolean isEncrypted() {
    return this.isEncrypted;
  }
  
  public boolean isChannelOpen() {
    return (this.channel != null && this.channel.isOpen());
  }
  
  public boolean hasNoChannel() {
    return (this.channel == null);
  }
  
  public INetHandler getNetHandler() {
    return this.packetListener;
  }
  
  public ITextComponent getExitMessage() {
    return this.terminationReason;
  }
  
  public void disableAutoRead() {
    this.channel.config().setAutoRead(false);
  }
  
  public void setCompressionThreshold(int threshold) {
    if (threshold >= 0) {
      if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
        ((NettyCompressionDecoder)this.channel.pipeline().get("decompress")).setCompressionThreshold(threshold);
      } else {
        this.channel.pipeline().addBefore("decoder", "decompress", (ChannelHandler)new NettyCompressionDecoder(threshold));
      } 
      if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
        ((NettyCompressionEncoder)this.channel.pipeline().get("compress")).setCompressionThreshold(threshold);
      } else {
        this.channel.pipeline().addBefore("encoder", "compress", (ChannelHandler)new NettyCompressionEncoder(threshold));
      } 
    } else {
      if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder)
        this.channel.pipeline().remove("decompress"); 
      if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder)
        this.channel.pipeline().remove("compress"); 
    } 
  }
  
  public void checkDisconnected() {
    if (this.channel != null && !this.channel.isOpen())
      if (this.disconnected) {
        LOGGER.warn("handleDisconnection() called twice");
      } else {
        this.disconnected = true;
        if (getExitMessage() != null) {
          getNetHandler().onDisconnect(getExitMessage());
        } else if (getNetHandler() != null) {
          getNetHandler().onDisconnect((ITextComponent)new TextComponentTranslation("multiplayer.disconnect.generic", new Object[0]));
        } 
      }  
  }
  
  static class InboundHandlerTuplePacketListener {
    private final Packet<?> packet;
    
    private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
    
    public InboundHandlerTuplePacketListener(Packet<?> inPacket, GenericFutureListener... inFutureListeners) {
      this.packet = inPacket;
      this.futureListeners = (GenericFutureListener<? extends Future<? super Void>>[])inFutureListeners;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\NetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */