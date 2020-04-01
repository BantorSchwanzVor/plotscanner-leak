package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKill extends CommandBase {
  public String getCommandName() {
    return "kill";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.kill.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) {
      EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
      entityPlayerMP.onKillCommand();
      notifyCommandListener(sender, this, "commands.kill.successful", new Object[] { entityPlayerMP.getDisplayName() });
    } else {
      Entity entity = getEntity(server, sender, args[0]);
      entity.onKillCommand();
      notifyCommandListener(sender, this, "commands.kill.successful", new Object[] { entity.getDisplayName() });
    } 
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (index == 0);
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandKill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */