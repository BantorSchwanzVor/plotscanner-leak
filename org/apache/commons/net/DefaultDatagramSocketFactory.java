package org.apache.commons.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DefaultDatagramSocketFactory implements DatagramSocketFactory {
  public DatagramSocket createDatagramSocket() throws SocketException {
    return new DatagramSocket();
  }
  
  public DatagramSocket createDatagramSocket(int port) throws SocketException {
    return new DatagramSocket(port);
  }
  
  public DatagramSocket createDatagramSocket(int port, InetAddress laddr) throws SocketException {
    return new DatagramSocket(port, laddr);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\DefaultDatagramSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */