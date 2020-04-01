package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall extends CommandBase {
  public String getCommandName() {
    return "toggledownfall";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.downfall.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    toggleRainfall(server);
    notifyCommandListener(sender, this, "commands.downfall.success", new Object[0]);
  }
  
  protected void toggleRainfall(MinecraftServer server) {
    WorldInfo worldinfo = server.worldServers[0].getWorldInfo();
    worldinfo.setRaining(!worldinfo.isRaining());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandToggleDownfall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */