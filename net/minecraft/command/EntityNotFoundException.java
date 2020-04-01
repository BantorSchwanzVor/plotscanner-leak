package net.minecraft.command;

public class EntityNotFoundException extends CommandException {
  public EntityNotFoundException(String p_i47332_1_) {
    this("commands.generic.entity.notFound", new Object[] { p_i47332_1_ });
  }
  
  public EntityNotFoundException(String message, Object... args) {
    super(message, args);
  }
  
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\EntityNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */