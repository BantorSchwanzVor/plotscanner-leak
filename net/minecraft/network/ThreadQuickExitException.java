package net.minecraft.network;

public final class ThreadQuickExitException extends RuntimeException {
  public static final ThreadQuickExitException INSTANCE = new ThreadQuickExitException();
  
  private ThreadQuickExitException() {
    setStackTrace(new StackTraceElement[0]);
  }
  
  public synchronized Throwable fillInStackTrace() {
    setStackTrace(new StackTraceElement[0]);
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\ThreadQuickExitException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */