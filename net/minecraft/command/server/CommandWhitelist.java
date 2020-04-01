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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandWhitelist extends CommandBase {
  public String getCommandName() {
    return "whitelist";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.whitelist.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length < 1)
      throw new WrongUsageException("commands.whitelist.usage", new Object[0]); 
    if ("on".equals(args[0])) {
      server.getPlayerList().setWhiteListEnabled(true);
      notifyCommandListener(sender, (ICommand)this, "commands.whitelist.enabled", new Object[0]);
    } else if ("off".equals(args[0])) {
      server.getPlayerList().setWhiteListEnabled(false);
      notifyCommandListener(sender, (ICommand)this, "commands.whitelist.disabled", new Object[0]);
    } else if ("list".equals(args[0])) {
      sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.whitelist.list", new Object[] { Integer.valueOf((server.getPlayerList().getWhitelistedPlayerNames()).length), Integer.valueOf((server.getPlayerList().getAvailablePlayerDat()).length) }));
      String[] astring = server.getPlayerList().getWhitelistedPlayerNames();
      sender.addChatMessage((ITextComponent)new TextComponentString(joinNiceString((Object[])astring)));
    } else if ("add".equals(args[0])) {
      if (args.length < 2)
        throw new WrongUsageException("commands.whitelist.add.usage", new Object[0]); 
      GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[1]);
      if (gameprofile == null)
        throw new CommandException("commands.whitelist.add.failed", new Object[] { args[1] }); 
      server.getPlayerList().addWhitelistedPlayer(gameprofile);
      notifyCommandListener(sender, (ICommand)this, "commands.whitelist.add.success", new Object[] { args[1] });
    } else if ("remove".equals(args[0])) {
      if (args.length < 2)
        throw new WrongUsageException("commands.whitelist.remove.usage", new Object[0]); 
      GameProfile gameprofile1 = server.getPlayerList().getWhitelistedPlayers().getByName(args[1]);
      if (gameprofile1 == null)
        throw new CommandException("commands.whitelist.remove.failed", new Object[] { args[1] }); 
      server.getPlayerList().removePlayerFromWhitelist(gameprofile1);
      notifyCommandListener(sender, (ICommand)this, "commands.whitelist.remove.success", new Object[] { args[1] });
    } else if ("reload".equals(args[0])) {
      server.getPlayerList().reloadWhitelist();
      notifyCommandListener(sender, (ICommand)this, "commands.whitelist.reloaded", new Object[0]);
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, new String[] { "on", "off", "list", "add", "remove", "reload" }); 
    if (args.length == 2) {
      if ("remove".equals(args[0]))
        return getListOfStringsMatchingLastWord(args, server.getPlayerList().getWhitelistedPlayerNames()); 
      if ("add".equals(args[0]))
        return getListOfStringsMatchingLastWord(args, server.getPlayerProfileCache().getUsernames()); 
    } 
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandWhitelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */