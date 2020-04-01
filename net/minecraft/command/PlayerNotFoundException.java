package net.minecraft.command;

public class PlayerNotFoundException extends CommandException {
  public PlayerNotFoundException(String p_i47330_1_) {
    super(p_i47330_1_, new Object[0]);
  }
  
  public PlayerNotFoundException(String message, Object... replacements) {
    super(message, replacements);
  }
  
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\PlayerNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */