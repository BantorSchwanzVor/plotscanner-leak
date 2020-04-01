package com.mysql.cj.protocol;

import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ExceptionFactory;

public interface MessageListener<M extends Message> {
  default boolean processMessage(M message) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not allowed");
  }
  
  void error(Throwable paramThrowable);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\MessageListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */