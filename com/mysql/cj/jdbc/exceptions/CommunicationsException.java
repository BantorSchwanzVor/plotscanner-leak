package com.mysql.cj.jdbc.exceptions;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.StreamingNotifiable;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.protocol.PacketReceivedTimeHolder;
import com.mysql.cj.protocol.PacketSentTimeHolder;
import java.sql.SQLRecoverableException;

public class CommunicationsException extends SQLRecoverableException implements StreamingNotifiable {
  private static final long serialVersionUID = 4317904269000988676L;
  
  private String exceptionMessage;
  
  public CommunicationsException(JdbcConnection conn, PacketSentTimeHolder packetSentTimeHolder, PacketReceivedTimeHolder packetReceivedTimeHolder, Exception underlyingException) {
    this(ExceptionFactory.createLinkFailureMessageBasedOnHeuristics((PropertySet)conn.getPropertySet(), conn.getSession().getServerSession(), packetSentTimeHolder, packetReceivedTimeHolder, underlyingException), underlyingException);
  }
  
  public CommunicationsException(String message, Throwable underlyingException) {
    this.exceptionMessage = message;
    if (underlyingException != null)
      initCause(underlyingException); 
  }
  
  public String getMessage() {
    return this.exceptionMessage;
  }
  
  public String getSQLState() {
    return "08S01";
  }
  
  public void setWasStreamingResults() {
    this.exceptionMessage = Messages.getString("CommunicationsException.ClientWasStreaming");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\exceptions\CommunicationsException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */