package org.apache.commons.net.ftp.parser;

public class ParserInitializationException extends RuntimeException {
  private static final long serialVersionUID = 5563335279583210658L;
  
  public ParserInitializationException(String message) {
    super(message);
  }
  
  public ParserInitializationException(String message, Throwable rootCause) {
    super(message, rootCause);
  }
  
  @Deprecated
  public Throwable getRootCause() {
    return getCause();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\ParserInitializationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */