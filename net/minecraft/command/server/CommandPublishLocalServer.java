package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;

public class CommandPublishLocalServer extends CommandBase {
  public String getCommandName() {
    return "publish";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.publish.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    String s = server.shareToLAN(GameType.SURVIVAL, false);
    if (s != null) {
      notifyCommandListener(sender, (ICommand)this, "commands.publish.started", new Object[] { s });
    } else {
      notifyCommandListener(sender, (ICommand)this, "commands.publish.failed", new Object[0]);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandPublishLocalServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */