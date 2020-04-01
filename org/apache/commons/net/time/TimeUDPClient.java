package org.apache.commons.net.time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.net.DatagramSocketClient;

public final class TimeUDPClient extends DatagramSocketClient {
  public static final int DEFAULT_PORT = 37;
  
  public static final long SECONDS_1900_TO_1970 = 2208988800L;
  
  private final byte[] __dummyData = new byte[1];
  
  private final byte[] __timeData = new byte[4];
  
  public long getTime(InetAddress host, int port) throws IOException {
    DatagramPacket sendPacket = 
      new DatagramPacket(this.__dummyData, this.__dummyData.length, host, port);
    DatagramPacket receivePacket = new DatagramPacket(this.__timeData, this.__timeData.length);
    this._socket_.send(sendPacket);
    this._socket_.receive(receivePacket);
    long time = 0L;
    time |= ((this.__timeData[0] & 0xFF) << 24) & 0xFFFFFFFFL;
    time |= ((this.__timeData[1] & 0xFF) << 16) & 0xFFFFFFFFL;
    time |= ((this.__timeData[2] & 0xFF) << 8) & 0xFFFFFFFFL;
    time |= (this.__timeData[3] & 0xFF) & 0xFFFFFFFFL;
    return time;
  }
  
  public long getTime(InetAddress host) throws IOException {
    return getTime(host, 37);
  }
  
  public Date getDate(InetAddress host, int port) throws IOException {
    return new Date((getTime(host, port) - 2208988800L) * 1000L);
  }
  
  public Date getDate(InetAddress host) throws IOException {
    return new Date((getTime(host, 37) - 
        2208988800L) * 1000L);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\time\TimeUDPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */