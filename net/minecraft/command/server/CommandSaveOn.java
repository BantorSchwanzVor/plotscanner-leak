package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn extends CommandBase {
  public String getCommandName() {
    return "save-on";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.save-on.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    boolean flag = false;
    for (int i = 0; i < server.worldServers.length; i++) {
      if (server.worldServers[i] != null) {
        WorldServer worldserver = server.worldServers[i];
        if (worldserver.disableLevelSaving) {
          worldserver.disableLevelSaving = false;
          flag = true;
        } 
      } 
    } 
    if (flag) {
      notifyCommandListener(sender, (ICommand)this, "commands.save.enabled", new Object[0]);
    } else {
      throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandSaveOn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */