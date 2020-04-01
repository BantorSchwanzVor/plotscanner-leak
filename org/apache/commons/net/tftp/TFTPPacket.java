package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public abstract class TFTPPacket {
  static final int MIN_PACKET_SIZE = 4;
  
  public static final int READ_REQUEST = 1;
  
  public static final int WRITE_REQUEST = 2;
  
  public static final int DATA = 3;
  
  public static final int ACKNOWLEDGEMENT = 4;
  
  public static final int ERROR = 5;
  
  public static final int SEGMENT_SIZE = 512;
  
  int _type;
  
  int _port;
  
  InetAddress _address;
  
  public static final TFTPPacket newTFTPPacket(DatagramPacket datagram) throws TFTPPacketException {
    TFTPPacket packet = null;
    if (datagram.getLength() < 4)
      throw new TFTPPacketException(
          "Bad packet. Datagram data length is too short."); 
    byte[] data = datagram.getData();
    switch (data[1]) {
      case 1:
        packet = new TFTPReadRequestPacket(datagram);
        return packet;
      case 2:
        packet = new TFTPWriteRequestPacket(datagram);
        return packet;
      case 3:
        packet = new TFTPDataPacket(datagram);
        return packet;
      case 4:
        packet = new TFTPAckPacket(datagram);
        return packet;
      case 5:
        packet = new TFTPErrorPacket(datagram);
        return packet;
    } 
    throw new TFTPPacketException("Bad packet.  Invalid TFTP operator code.");
  }
  
  TFTPPacket(int type, InetAddress address, int port) {
    this._type = type;
    this._address = address;
    this._port = port;
  }
  
  abstract DatagramPacket _newDatagram(DatagramPacket paramDatagramPacket, byte[] paramArrayOfbyte);
  
  public abstract DatagramPacket newDatagram();
  
  public final int getType() {
    return this._type;
  }
  
  public final InetAddress getAddress() {
    return this._address;
  }
  
  public final int getPort() {
    return this._port;
  }
  
  public final void setPort(int port) {
    this._port = port;
  }
  
  public final void setAddress(InetAddress address) {
    this._address = address;
  }
  
  public String toString() {
    return this._address + " " + this._port + " " + this._type;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\tftp\TFTPPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */