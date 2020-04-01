package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandServerKick extends CommandBase {
  public String getCommandName() {
    return "kick";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.kick.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length > 0 && args[0].length() > 1) {
      EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
      if (entityplayermp == null)
        throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { args[0] }); 
      if (args.length >= 2) {
        ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 1);
        entityplayermp.connection.func_194028_b(itextcomponent);
        notifyCommandListener(sender, this, "commands.kick.success.reason", new Object[] { entityplayermp.getName(), itextcomponent.getUnformattedText() });
      } else {
        entityplayermp.connection.func_194028_b((ITextComponent)new TextComponentTranslation("multiplayer.disconnect.kicked", new Object[0]));
        notifyCommandListener(sender, this, "commands.kick.success", new Object[] { entityplayermp.getName() });
      } 
    } else {
      throw new WrongUsageException("commands.kick.usage", new Object[0]);
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length >= 1) ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandServerKick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */