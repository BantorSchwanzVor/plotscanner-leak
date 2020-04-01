package net.minecraft.server;

import java.io.OutputStream;
import net.minecraft.util.LoggingPrintStream;

public class DebugLoggingPrintStream extends LoggingPrintStream {
  public DebugLoggingPrintStream(String p_i47315_1_, OutputStream p_i47315_2_) {
    super(p_i47315_1_, p_i47315_2_);
  }
  
  protected void logString(String string) {
    StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
    StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];
    LOGGER.info("[{}]@.({}:{}): {}", this.domain, stacktraceelement.getFileName(), Integer.valueOf(stacktraceelement.getLineNumber()), string);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\DebugLoggingPrintStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */