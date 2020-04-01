package com.mysql.cj.protocol.a.result;

import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.protocol.a.NativePacketPayload;
import com.mysql.cj.protocol.result.AbstractResultsetRow;

public abstract class AbstractBufferRow extends AbstractResultsetRow {
  protected NativePacketPayload rowFromServer;
  
  protected int homePosition = 0;
  
  protected int lastRequestedIndex = -1;
  
  protected int lastRequestedPos;
  
  protected AbstractBufferRow(ExceptionInterceptor exceptionInterceptor) {
    super(exceptionInterceptor);
  }
  
  abstract int findAndSeekToOffset(int paramInt);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\a\result\AbstractBufferRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */