package com.mysql.cj.jdbc;

import com.mysql.cj.exceptions.ExceptionInterceptor;
import java.sql.NClob;

public class NClob extends Clob implements NClob {
  NClob(ExceptionInterceptor exceptionInterceptor) {
    super(exceptionInterceptor);
  }
  
  public NClob(String charDataInit, ExceptionInterceptor exceptionInterceptor) {
    super(charDataInit, exceptionInterceptor);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\NClob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */