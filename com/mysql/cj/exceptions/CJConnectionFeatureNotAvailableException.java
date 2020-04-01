package com.mysql.cj.exceptions;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.protocol.PacketSentTimeHolder;
import com.mysql.cj.protocol.ServerSession;

public class CJConnectionFeatureNotAvailableException extends CJCommunicationsException {
  private static final long serialVersionUID = -4129847384681995107L;
  
  public CJConnectionFeatureNotAvailableException() {}
  
  public CJConnectionFeatureNotAvailableException(PropertySet propertySet, ServerSession serverSession, PacketSentTimeHolder packetSentTimeHolder, Exception underlyingException) {
    super(underlyingException);
    init(propertySet, serverSession, packetSentTimeHolder, null);
  }
  
  public String getMessage() {
    return Messages.getString("ConnectionFeatureNotAvailableException.0");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\CJConnectionFeatureNotAvailableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */