package com.mysql.cj.jdbc.ha;

import java.util.Properties;

public interface LoadBalanceExceptionChecker {
  void init(Properties paramProperties);
  
  void destroy();
  
  boolean shouldExceptionTriggerFailover(Throwable paramThrowable);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\LoadBalanceExceptionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */