package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException {
  public WrongUsageException(String message, Object... replacements) {
    super(message, replacements);
  }
  
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\WrongUsageException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */