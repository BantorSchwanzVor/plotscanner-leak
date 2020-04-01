package net.minecraft.command;

public class CommandNotFoundException extends CommandException {
  public CommandNotFoundException() {
    this("commands.generic.notFound", new Object[0]);
  }
  
  public CommandNotFoundException(String message, Object... args) {
    super(message, args);
  }
  
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */