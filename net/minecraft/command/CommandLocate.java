package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandLocate extends CommandBase {
  public String getCommandName() {
    return "locate";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.locate.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 1)
      throw new WrongUsageException("commands.locate.usage", new Object[0]); 
    String s = args[0];
    BlockPos blockpos = sender.getEntityWorld().func_190528_a(s, sender.getPosition(), false);
    if (blockpos != null) {
      sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.locate.success", new Object[] { s, Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getZ()) }));
    } else {
      throw new CommandException("commands.locate.failure", new Object[] { s });
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "Stronghold", "Monument", "Village", "Mansion", "EndCity", "Fortress", "Temple", "Mineshaft" }) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandLocate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */