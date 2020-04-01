package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalEventLoopGroup;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.network.NetHandlerHandshakeMemory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkSystem {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public static final LazyLoadBase<NioEventLoopGroup> SERVER_NIO_EVENTLOOP = new LazyLoadBase<NioEventLoopGroup>() {
      protected NioEventLoopGroup load() {
        return new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Server IO #%d").setDaemon(true).build());
      }
    };
  
  public static final LazyLoadBase<EpollEventLoopGroup> SERVER_EPOLL_EVENTLOOP = new LazyLoadBase<EpollEventLoopGroup>() {
      protected EpollEventLoopGroup load() {
        return new EpollEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build());
      }
    };
  
  public static final LazyLoadBase<LocalEventLoopGroup> SERVER_LOCAL_EVENTLOOP = new LazyLoadBase<LocalEventLoopGroup>() {
      protected LocalEventLoopGroup load() {
        return new LocalEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
      }
    };
  
  private final MinecraftServer mcServer;
  
  public volatile boolean isAlive;
  
  private final List<ChannelFuture> endpoints = Collections.synchronizedList(Lists.newArrayList());
  
  private final List<NetworkManager> networkManagers = Collections.synchronizedList(Lists.newArrayList());
  
  public NetworkSystem(MinecraftServer server) {
    this.mcServer = server;
    this.isAlive = true;
  }
  
  public void addLanEndpoint(InetAddress address, int port) throws IOException {
    synchronized (this.endpoints) {
      Class<NioServerSocketChannel> clazz;
      LazyLoadBase<NioEventLoopGroup> lazyLoadBase;
      if (Epoll.isAvailable() && this.mcServer.shouldUseNativeTransport()) {
        Class<EpollServerSocketChannel> clazz1 = EpollServerSocketChannel.class;
        LazyLoadBase<EpollEventLoopGroup> lazyLoadBase1 = SERVER_EPOLL_EVENTLOOP;
        LOGGER.info("Using epoll channel type");
      } else {
        clazz = NioServerSocketChannel.class;
        lazyLoadBase = SERVER_NIO_EVENTLOOP;
        LOGGER.info("Using default channel type");
      } 
      this.endpoints.add(((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(clazz)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
              protected void initChannel(Channel p_initChannel_1_) throws Exception {
                try {
                  p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
                } catch (ChannelException channelException) {}
                p_initChannel_1_.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new LegacyPingHandler(NetworkSystem.this)).addLast("splitter", (ChannelHandler)new NettyVarint21FrameDecoder()).addLast("decoder", (ChannelHandler)new NettyPacketDecoder(EnumPacketDirection.SERVERBOUND)).addLast("prepender", (ChannelHandler)new NettyVarint21FrameEncoder()).addLast("encoder", (ChannelHandler)new NettyPacketEncoder(EnumPacketDirection.CLIENTBOUND));
                NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
                NetworkSystem.this.networkManagers.add(networkmanager);
                p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
                networkmanager.setNetHandler((INetHandler)new NetHandlerHandshakeTCP(NetworkSystem.this.mcServer, networkmanager));
              }
            }).group((EventLoopGroup)lazyLoadBase.getValue()).localAddress(address, port)).bind().syncUninterruptibly());
    } 
  }
  
  public SocketAddress addLocalEndpoint() {
    ChannelFuture channelfuture;
    synchronized (this.endpoints) {
      channelfuture = ((ServerBootstrap)((ServerBootstrap)(new ServerBootstrap()).channel(LocalServerChannel.class)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
            protected void initChannel(Channel p_initChannel_1_) throws Exception {
              NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.SERVERBOUND);
              networkmanager.setNetHandler((INetHandler)new NetHandlerHandshakeMemory(NetworkSystem.this.mcServer, networkmanager));
              NetworkSystem.this.networkManagers.add(networkmanager);
              p_initChannel_1_.pipeline().addLast("packet_handler", (ChannelHandler)networkmanager);
            }
          }).group((EventLoopGroup)SERVER_NIO_EVENTLOOP.getValue()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
      this.endpoints.add(channelfuture);
    } 
    return channelfuture.channel().localAddress();
  }
  
  public void terminateEndpoints() {
    this.isAlive = false;
    for (ChannelFuture channelfuture : this.endpoints) {
      try {
        channelfuture.channel().close().sync();
      } catch (InterruptedException var4) {
        LOGGER.error("Interrupted whilst closing channel");
      } 
    } 
  }
  
  public void networkTick() {
    synchronized (this.networkManagers) {
      Iterator<NetworkManager> iterator = this.networkManagers.iterator();
      while (iterator.hasNext()) {
        final NetworkManager networkmanager = iterator.next();
        if (!networkmanager.hasNoChannel()) {
          if (networkmanager.isChannelOpen()) {
            try {
              networkmanager.processReceivedPackets();
            } catch (Exception exception) {
              if (networkmanager.isLocalChannel()) {
                CrashReport crashreport = CrashReport.makeCrashReport(exception, "Ticking memory connection");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Ticking connection");
                crashreportcategory.setDetail("Connection", new ICrashReportDetail<String>() {
                      public String call() throws Exception {
                        return networkmanager.toString();
                      }
                    });
                throw new ReportedException(crashreport);
              } 
              LOGGER.warn("Failed to handle packet for {}", networkmanager.getRemoteAddress(), exception);
              final TextComponentString textcomponentstring = new TextComponentString("Internal server error");
              networkmanager.sendPacket((Packet<?>)new SPacketDisconnect((ITextComponent)textcomponentstring), new GenericFutureListener<Future<? super Void>>() {
                    public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception {
                      networkmanager.closeChannel((ITextComponent)textcomponentstring);
                    }
                  },  (GenericFutureListener<? extends Future<? super Void>>[])new GenericFutureListener[0]);
              networkmanager.disableAutoRead();
            } 
            continue;
          } 
          iterator.remove();
          networkmanager.checkDisconnected();
        } 
      } 
    } 
  }
  
  public MinecraftServer getServer() {
    return this.mcServer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\NetworkSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */