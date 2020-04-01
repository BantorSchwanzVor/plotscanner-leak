package com.mysql.cj.exceptions;

public class FeatureNotAvailableException extends CJException {
  private static final long serialVersionUID = -6649508222074639690L;
  
  public FeatureNotAvailableException() {}
  
  public FeatureNotAvailableException(String message) {
    super(message);
  }
  
  public FeatureNotAvailableException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public FeatureNotAvailableException(Throwable cause) {
    super(cause);
  }
  
  public FeatureNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\FeatureNotAvailableException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */