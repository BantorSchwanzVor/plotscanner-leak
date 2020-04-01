package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream {
  protected static final Logger LOGGER = LogManager.getLogger();
  
  protected final String domain;
  
  public LoggingPrintStream(String domainIn, OutputStream outStream) {
    super(outStream);
    this.domain = domainIn;
  }
  
  public void println(String p_println_1_) {
    logString(p_println_1_);
  }
  
  public void println(Object p_println_1_) {
    logString(String.valueOf(p_println_1_));
  }
  
  protected void logString(String string) {
    LOGGER.info("[{}]: {}", this.domain, string);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\LoggingPrintStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */