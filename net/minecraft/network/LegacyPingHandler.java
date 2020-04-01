package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LegacyPingHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final NetworkSystem networkSystem;
  
  public LegacyPingHandler(NetworkSystem networkSystemIn) {
    this.networkSystem = networkSystemIn;
  }
  
  public void channelRead(ChannelHandlerContext p_channelRead_1_, Object p_channelRead_2_) throws Exception {
    ByteBuf bytebuf = (ByteBuf)p_channelRead_2_;
    bytebuf.markReaderIndex();
    boolean flag = true;
    try {
      if (bytebuf.readUnsignedByte() == 254) {
        String s2, s;
        boolean flag1;
        int m;
        boolean bool1;
        int k, j;
        String s1;
        ByteBuf bytebuf1;
        InetSocketAddress inetsocketaddress = (InetSocketAddress)p_channelRead_1_.channel().remoteAddress();
        MinecraftServer minecraftserver = this.networkSystem.getServer();
        int i = bytebuf.readableBytes();
        switch (i) {
          case 0:
            LOGGER.debug("Ping: (<1.3.x) from {}:{}", inetsocketaddress.getAddress(), Integer.valueOf(inetsocketaddress.getPort()));
            s2 = String.format("%s§%d§%d", new Object[] { minecraftserver.getMOTD(), Integer.valueOf(minecraftserver.getCurrentPlayerCount()), Integer.valueOf(minecraftserver.getMaxPlayers()) });
            writeAndFlush(p_channelRead_1_, getStringBuffer(s2));
            break;
          case 1:
            if (bytebuf.readUnsignedByte() != 1)
              return; 
            LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", inetsocketaddress.getAddress(), Integer.valueOf(inetsocketaddress.getPort()));
            s = String.format("§1\000%d\000%s\000%s\000%d\000%d", new Object[] { Integer.valueOf(127), minecraftserver.getMinecraftVersion(), minecraftserver.getMOTD(), Integer.valueOf(minecraftserver.getCurrentPlayerCount()), Integer.valueOf(minecraftserver.getMaxPlayers()) });
            writeAndFlush(p_channelRead_1_, getStringBuffer(s));
            break;
          default:
            flag1 = (bytebuf.readUnsignedByte() == 1);
            m = flag1 & ((bytebuf.readUnsignedByte() == 250) ? 1 : 0);
            bool1 = m & "MC|PingHost".equals(new String(bytebuf.readBytes(bytebuf.readShort() * 2).array(), StandardCharsets.UTF_16BE));
            j = bytebuf.readUnsignedShort();
            k = bool1 & ((bytebuf.readUnsignedByte() >= 73) ? 1 : 0);
            k &= (3 + (bytebuf.readBytes(bytebuf.readShort() * 2).array()).length + 4 == j) ? 1 : 0;
            k &= (bytebuf.readInt() <= 65535) ? 1 : 0;
            k &= (bytebuf.readableBytes() == 0) ? 1 : 0;
            if (k == 0)
              return; 
            LOGGER.debug("Ping: (1.6) from {}:{}", inetsocketaddress.getAddress(), Integer.valueOf(inetsocketaddress.getPort()));
            s1 = String.format("§1\000%d\000%s\000%s\000%d\000%d", new Object[] { Integer.valueOf(127), minecraftserver.getMinecraftVersion(), minecraftserver.getMOTD(), Integer.valueOf(minecraftserver.getCurrentPlayerCount()), Integer.valueOf(minecraftserver.getMaxPlayers()) });
            bytebuf1 = getStringBuffer(s1);
            try {
              writeAndFlush(p_channelRead_1_, bytebuf1);
            } finally {
              bytebuf1.release();
            } 
            break;
        } 
        bytebuf.release();
        flag = false;
        return;
      } 
    } catch (RuntimeException var21) {
      return;
    } finally {
      if (flag) {
        bytebuf.resetReaderIndex();
        p_channelRead_1_.channel().pipeline().remove("legacy_query");
        p_channelRead_1_.fireChannelRead(p_channelRead_2_);
      } 
    } 
  }
  
  private void writeAndFlush(ChannelHandlerContext ctx, ByteBuf data) {
    ctx.pipeline().firstContext().writeAndFlush(data).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
  }
  
  private ByteBuf getStringBuffer(String string) {
    ByteBuf bytebuf = Unpooled.buffer();
    bytebuf.writeByte(255);
    char[] achar = string.toCharArray();
    bytebuf.writeShort(achar.length);
    byte b;
    int i;
    char[] arrayOfChar1;
    for (i = (arrayOfChar1 = achar).length, b = 0; b < i; ) {
      char c0 = arrayOfChar1[b];
      bytebuf.writeChar(c0);
      b++;
    } 
    return bytebuf;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\LegacyPingHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */