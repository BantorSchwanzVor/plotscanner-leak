package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandXP extends CommandBase {
  public String getCommandName() {
    return "xp";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.xp.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length <= 0)
      throw new WrongUsageException("commands.xp.usage", new Object[0]); 
    String s = args[0];
    boolean flag = !(!s.endsWith("l") && !s.endsWith("L"));
    if (flag && s.length() > 1)
      s = s.substring(0, s.length() - 1); 
    int i = parseInt(s);
    boolean flag1 = (i < 0);
    if (flag1)
      i *= -1; 
    EntityPlayerMP entityPlayerMP = (args.length > 1) ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);
    if (flag) {
      sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ((EntityPlayer)entityPlayerMP).experienceLevel);
      if (flag1) {
        entityPlayerMP.addExperienceLevel(-i);
        notifyCommandListener(sender, this, "commands.xp.success.negative.levels", new Object[] { Integer.valueOf(i), entityPlayerMP.getName() });
      } else {
        entityPlayerMP.addExperienceLevel(i);
        notifyCommandListener(sender, this, "commands.xp.success.levels", new Object[] { Integer.valueOf(i), entityPlayerMP.getName() });
      } 
    } else {
      sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, ((EntityPlayer)entityPlayerMP).experienceTotal);
      if (flag1)
        throw new CommandException("commands.xp.failure.widthdrawXp", new Object[0]); 
      entityPlayerMP.addExperience(i);
      notifyCommandListener(sender, this, "commands.xp.success", new Object[] { Integer.valueOf(i), entityPlayerMP.getName() });
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 2) ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.<String>emptyList();
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (index == 1);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandXP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */