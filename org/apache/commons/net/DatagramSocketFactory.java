package org.apache.commons.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public interface DatagramSocketFactory {
  DatagramSocket createDatagramSocket() throws SocketException;
  
  DatagramSocket createDatagramSocket(int paramInt) throws SocketException;
  
  DatagramSocket createDatagramSocket(int paramInt, InetAddress paramInetAddress) throws SocketException;
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\DatagramSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */