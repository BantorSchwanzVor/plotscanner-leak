package com.mysql.cj.jdbc.ha;

public class NdbLoadBalanceExceptionChecker extends StandardLoadBalanceExceptionChecker {
  public boolean shouldExceptionTriggerFailover(Throwable ex) {
    return (super.shouldExceptionTriggerFailover(ex) || checkNdbException(ex));
  }
  
  private boolean checkNdbException(Throwable ex) {
    return (ex.getMessage().startsWith("Lock wait timeout exceeded") || (ex
      .getMessage().startsWith("Got temporary error") && ex.getMessage().endsWith("from NDB")));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\NdbLoadBalanceExceptionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */