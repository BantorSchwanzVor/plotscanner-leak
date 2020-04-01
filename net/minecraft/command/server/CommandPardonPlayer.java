package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPardonPlayer extends CommandBase {
  public String getCommandName() {
    return "pardon";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.unban.usage";
  }
  
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return (server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender));
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 1 && args[0].length() > 0) {
      GameProfile gameprofile = server.getPlayerList().getBannedPlayers().getBannedProfile(args[0]);
      if (gameprofile == null)
        throw new CommandException("commands.unban.failed", new Object[] { args[0] }); 
      server.getPlayerList().getBannedPlayers().removeEntry(gameprofile);
      notifyCommandListener(sender, (ICommand)this, "commands.unban.success", new Object[] { args[0] });
    } else {
      throw new WrongUsageException("commands.unban.usage", new Object[0]);
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, server.getPlayerList().getBannedPlayers().getKeys()) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandPardonPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */