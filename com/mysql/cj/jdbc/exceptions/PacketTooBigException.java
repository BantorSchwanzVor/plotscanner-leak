package com.mysql.cj.jdbc.exceptions;

import com.mysql.cj.Messages;
import java.sql.SQLException;

public class PacketTooBigException extends SQLException {
  static final long serialVersionUID = 7248633977685452174L;
  
  public PacketTooBigException(long packetSize, long maximumPacketSize) {
    super(Messages.getString("PacketTooBigException.0", new Object[] { Long.valueOf(packetSize), Long.valueOf(maximumPacketSize) }), "S1000");
  }
  
  public PacketTooBigException(String message) {
    super(message, "S1000");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\exceptions\PacketTooBigException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */