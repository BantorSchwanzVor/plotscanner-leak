package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public final class TFTPReadRequestPacket extends TFTPRequestPacket {
  public TFTPReadRequestPacket(InetAddress destination, int port, String filename, int mode) {
    super(destination, port, 1, filename, mode);
  }
  
  TFTPReadRequestPacket(DatagramPacket datagram) throws TFTPPacketException {
    super(1, datagram);
  }
  
  public String toString() {
    return String.valueOf(super.toString()) + " RRQ " + getFilename() + " " + TFTP.getModeName(getMode());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\tftp\TFTPReadRequestPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */