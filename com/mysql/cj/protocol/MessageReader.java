package com.mysql.cj.protocol;

import com.mysql.cj.exceptions.CJOperationNotSupportedException;
import com.mysql.cj.exceptions.ExceptionFactory;
import java.io.IOException;
import java.util.Optional;

public interface MessageReader<H extends MessageHeader, M extends Message> {
  H readHeader() throws IOException;
  
  M readMessage(Optional<M> paramOptional, H paramH) throws IOException;
  
  default M readMessage(Optional<M> reuse, int expectedType) throws IOException {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  default void pushMessageListener(MessageListener<M> l) {
    throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
  }
  
  default byte getMessageSequence() {
    return 0;
  }
  
  default void resetMessageSequence() {}
  
  default MessageReader<H, M> undecorateAll() {
    return this;
  }
  
  default MessageReader<H, M> undecorate() {
    return this;
  }
  
  default void start() {}
  
  default void stopAfterNextMessage() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\MessageReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */