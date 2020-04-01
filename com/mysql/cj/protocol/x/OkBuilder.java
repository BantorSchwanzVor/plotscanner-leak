package com.mysql.cj.protocol.x;

import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import com.mysql.cj.protocol.ProtocolEntity;
import com.mysql.cj.protocol.ResultBuilder;

public class OkBuilder implements ResultBuilder<Ok> {
  public boolean addProtocolEntity(ProtocolEntity entity) {
    if (entity instanceof Notice)
      return false; 
    if (entity instanceof Ok)
      return true; 
    throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unexpected protocol entity " + entity);
  }
  
  public Ok build() {
    return new Ok();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\protocol\x\OkBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */