package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandReload extends CommandBase {
  public String getCommandName() {
    return "reload";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.reload.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length > 0)
      throw new WrongUsageException("commands.reload.usage", new Object[0]); 
    server.func_193031_aM();
    notifyCommandListener(sender, this, "commands.reload.success", new Object[0]);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandReload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */