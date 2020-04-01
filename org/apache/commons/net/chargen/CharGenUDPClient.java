package org.apache.commons.net.chargen;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import org.apache.commons.net.DatagramSocketClient;

public final class CharGenUDPClient extends DatagramSocketClient {
  public static final int SYSTAT_PORT = 11;
  
  public static final int NETSTAT_PORT = 15;
  
  public static final int QUOTE_OF_DAY_PORT = 17;
  
  public static final int CHARGEN_PORT = 19;
  
  public static final int DEFAULT_PORT = 19;
  
  private final byte[] __receiveData = new byte[512];
  
  private final DatagramPacket __receivePacket = new DatagramPacket(this.__receiveData, this.__receiveData.length);
  
  private final DatagramPacket __sendPacket = new DatagramPacket(new byte[0], 0);
  
  public void send(InetAddress host, int port) throws IOException {
    this.__sendPacket.setAddress(host);
    this.__sendPacket.setPort(port);
    this._socket_.send(this.__sendPacket);
  }
  
  public void send(InetAddress host) throws IOException {
    send(host, 19);
  }
  
  public byte[] receive() throws IOException {
    this._socket_.receive(this.__receivePacket);
    int length;
    byte[] result = new byte[length = this.__receivePacket.getLength()];
    System.arraycopy(this.__receiveData, 0, result, 0, length);
    return result;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\chargen\CharGenUDPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */