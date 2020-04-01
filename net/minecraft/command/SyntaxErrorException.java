package net.minecraft.command;

public class SyntaxErrorException extends CommandException {
  public SyntaxErrorException() {
    this("commands.generic.snytax", new Object[0]);
  }
  
  public SyntaxErrorException(String message, Object... replacements) {
    super(message, replacements);
  }
  
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\SyntaxErrorException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */