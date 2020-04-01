package org.apache.commons.net.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Locale;

public abstract class TFTPRequestPacket extends TFTPPacket {
  static final String[] _modeStrings = new String[] { "netascii", "octet" };
  
  private static final byte[][] _modeBytes = new byte[][] { { 110, 101, 116, 97, 115, 99, 
        105, 105 }, { 111, 99, 116, 101, 116 } };
  
  private final int _mode;
  
  private final String _filename;
  
  TFTPRequestPacket(InetAddress destination, int port, int type, String filename, int mode) {
    super(type, destination, port);
    this._filename = filename;
    this._mode = mode;
  }
  
  TFTPRequestPacket(int type, DatagramPacket datagram) throws TFTPPacketException {
    super(type, datagram.getAddress(), datagram.getPort());
    byte[] data = datagram.getData();
    if (getType() != data[1])
      throw new TFTPPacketException("TFTP operator code does not match type."); 
    StringBuilder buffer = new StringBuilder();
    int index = 2;
    int length = datagram.getLength();
    while (index < length && data[index] != 0) {
      buffer.append((char)data[index]);
      index++;
    } 
    this._filename = buffer.toString();
    if (index >= length)
      throw new TFTPPacketException("Bad filename and mode format."); 
    buffer.setLength(0);
    index++;
    while (index < length && data[index] != 0) {
      buffer.append((char)data[index]);
      index++;
    } 
    String modeString = buffer.toString().toLowerCase(Locale.ENGLISH);
    length = _modeStrings.length;
    int mode = 0;
    for (index = 0; index < length; index++) {
      if (modeString.equals(_modeStrings[index])) {
        mode = index;
        break;
      } 
    } 
    this._mode = mode;
    if (index >= length)
      throw new TFTPPacketException("Unrecognized TFTP transfer mode: " + modeString); 
  }
  
  final DatagramPacket _newDatagram(DatagramPacket datagram, byte[] data) {
    int fileLength = this._filename.length();
    int modeLength = (_modeBytes[this._mode]).length;
    data[0] = 0;
    data[1] = (byte)this._type;
    System.arraycopy(this._filename.getBytes(), 0, data, 2, fileLength);
    data[fileLength + 2] = 0;
    System.arraycopy(_modeBytes[this._mode], 0, data, fileLength + 3, 
        modeLength);
    datagram.setAddress(this._address);
    datagram.setPort(this._port);
    datagram.setData(data);
    datagram.setLength(fileLength + modeLength + 3);
    return datagram;
  }
  
  public final DatagramPacket newDatagram() {
    int fileLength = this._filename.length();
    int modeLength = (_modeBytes[this._mode]).length;
    byte[] data = new byte[fileLength + modeLength + 4];
    data[0] = 0;
    data[1] = (byte)this._type;
    System.arraycopy(this._filename.getBytes(), 0, data, 2, fileLength);
    data[fileLength + 2] = 0;
    System.arraycopy(_modeBytes[this._mode], 0, data, fileLength + 3, 
        modeLength);
    return new DatagramPacket(data, data.length, this._address, this._port);
  }
  
  public final int getMode() {
    return this._mode;
  }
  
  public final String getFilename() {
    return this._filename;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\tftp\TFTPRequestPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */