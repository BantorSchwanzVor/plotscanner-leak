package org.apache.commons.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

public class DefaultSocketFactory extends SocketFactory {
  private final Proxy connProxy;
  
  public DefaultSocketFactory() {
    this(null);
  }
  
  public DefaultSocketFactory(Proxy proxy) {
    this.connProxy = proxy;
  }
  
  public Socket createSocket() throws IOException {
    if (this.connProxy != null)
      return new Socket(this.connProxy); 
    return new Socket();
  }
  
  public Socket createSocket(String host, int port) throws UnknownHostException, IOException {
    if (this.connProxy != null) {
      Socket s = new Socket(this.connProxy);
      s.connect(new InetSocketAddress(host, port));
      return s;
    } 
    return new Socket(host, port);
  }
  
  public Socket createSocket(InetAddress address, int port) throws IOException {
    if (this.connProxy != null) {
      Socket s = new Socket(this.connProxy);
      s.connect(new InetSocketAddress(address, port));
      return s;
    } 
    return new Socket(address, port);
  }
  
  public Socket createSocket(String host, int port, InetAddress localAddr, int localPort) throws UnknownHostException, IOException {
    if (this.connProxy != null) {
      Socket s = new Socket(this.connProxy);
      s.bind(new InetSocketAddress(localAddr, localPort));
      s.connect(new InetSocketAddress(host, port));
      return s;
    } 
    return new Socket(host, port, localAddr, localPort);
  }
  
  public Socket createSocket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
    if (this.connProxy != null) {
      Socket s = new Socket(this.connProxy);
      s.bind(new InetSocketAddress(localAddr, localPort));
      s.connect(new InetSocketAddress(address, port));
      return s;
    } 
    return new Socket(address, port, localAddr, localPort);
  }
  
  public ServerSocket createServerSocket(int port) throws IOException {
    return new ServerSocket(port);
  }
  
  public ServerSocket createServerSocket(int port, int backlog) throws IOException {
    return new ServerSocket(port, backlog);
  }
  
  public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
    return new ServerSocket(port, backlog, bindAddr);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\DefaultSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */