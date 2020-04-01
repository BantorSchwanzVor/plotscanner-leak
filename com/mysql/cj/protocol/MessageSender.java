package com.mysql.cj.protocol;

import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface MessageSender<M extends Message> {
  default void send(byte[] message, int messageLen, byte messageSequence) throws IOException {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  default void send(M message) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  default CompletableFuture<?> send(M message, CompletableFuture<?> future, Runnable callback) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  default void setMaxAllowedPacket(int maxAllowedPacket) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  default MessageSender<M> undecorateAll() {
    return this;
  }
  
  default MessageSender<M> undecorate() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\MessageSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */