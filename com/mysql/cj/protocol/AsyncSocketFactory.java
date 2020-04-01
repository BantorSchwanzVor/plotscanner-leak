package com.mysql.cj.protocol;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.CJCommunicationsException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class AsyncSocketFactory implements SocketFactory {
  AsynchronousSocketChannel channel;
  
  public <T extends java.io.Closeable> T connect(String host, int port, PropertySet props, int loginTimeout) throws IOException {
    try {
      this.channel = AsynchronousSocketChannel.open();
      this.channel.setOption(StandardSocketOptions.SO_SNDBUF, Integer.valueOf(131072));
      this.channel.setOption(StandardSocketOptions.SO_RCVBUF, Integer.valueOf(131072));
      Future<Void> connectPromise = this.channel.connect(new InetSocketAddress(host, port));
      connectPromise.get();
    } catch (CJCommunicationsException e) {
      throw e;
    } catch (IOException|InterruptedException|java.util.concurrent.ExecutionException|RuntimeException ex) {
      throw new CJCommunicationsException(ex);
    } 
    return (T)this.channel;
  }
  
  public <T extends java.io.Closeable> T performTlsHandshake(SocketConnection socketConnection, ServerSession serverSession) throws IOException {
    this.channel = ExportControlled.startTlsOnAsynchronousChannel(this.channel, socketConnection);
    return (T)this.channel;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\AsyncSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */