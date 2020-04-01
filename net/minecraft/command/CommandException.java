package net.minecraft.command;

public class CommandException extends Exception {
  private final Object[] errorObjects;
  
  public CommandException(String message, Object... objects) {
    super(message);
    this.errorObjects = objects;
  }
  
  public Object[] getErrorObjects() {
    return this.errorObjects;
  }
  
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */