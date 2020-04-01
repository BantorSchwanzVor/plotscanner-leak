package com.mysql.cj.exceptions;

import com.mysql.cj.log.Log;
import java.util.Properties;

public interface ExceptionInterceptor {
  ExceptionInterceptor init(Properties paramProperties, Log paramLog);
  
  void destroy();
  
  Exception interceptException(Exception paramException);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\ExceptionInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */