package com.mysql.cj.exceptions;

import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.protocol.PacketReceivedTimeHolder;
import com.mysql.cj.protocol.PacketSentTimeHolder;
import com.mysql.cj.protocol.ServerSession;

public class CJCommunicationsException extends CJException {
  private static final long serialVersionUID = 344035358493554245L;
  
  public CJCommunicationsException() {}
  
  public CJCommunicationsException(String message) {
    super(message);
  }
  
  public CJCommunicationsException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public CJCommunicationsException(Throwable cause) {
    super(cause);
  }
  
  protected CJCommunicationsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
  public void init(PropertySet propertySet, ServerSession serverSession, PacketSentTimeHolder packetSentTimeHolder, PacketReceivedTimeHolder packetReceivedTimeHolder) {
    this.exceptionMessage = ExceptionFactory.createLinkFailureMessageBasedOnHeuristics(propertySet, serverSession, packetSentTimeHolder, packetReceivedTimeHolder, 
        getCause());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\CJCommunicationsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */