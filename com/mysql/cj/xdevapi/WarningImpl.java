package com.mysql.cj.xdevapi;

import com.mysql.cj.protocol.Warning;

public class WarningImpl implements Warning {
  private Warning message;
  
  public WarningImpl(Warning message) {
    this.message = message;
  }
  
  public int getLevel() {
    return this.message.getLevel();
  }
  
  public long getCode() {
    return this.message.getCode();
  }
  
  public String getMessage() {
    return this.message.getMessage();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\xdevapi\WarningImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */