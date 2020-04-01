package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public final class TFTPWriteRequestPacket extends TFTPRequestPacket {
  public TFTPWriteRequestPacket(InetAddress destination, int port, String filename, int mode) {
    super(destination, port, 2, filename, mode);
  }
  
  TFTPWriteRequestPacket(DatagramPacket datagram) throws TFTPPacketException {
    super(2, datagram);
  }
  
  public String toString() {
    return String.valueOf(super.toString()) + " WRQ " + getFilename() + " " + TFTP.getModeName(getMode());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\tftp\TFTPWriteRequestPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */