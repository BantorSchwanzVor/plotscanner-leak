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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.server.management.UserListEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandBanPlayer extends CommandBase {
  public String getCommandName() {
    return "ban";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.ban.usage";
  }
  
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return (server.getPlayerList().getBannedPlayers().isLanServer() && super.checkPermission(server, sender));
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length >= 1 && args[0].length() > 0) {
      GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
      if (gameprofile == null)
        throw new CommandException("commands.ban.failed", new Object[] { args[0] }); 
      String s = null;
      if (args.length >= 2)
        s = getChatComponentFromNthArg(sender, args, 1).getUnformattedText(); 
      UserListBansEntry userlistbansentry = new UserListBansEntry(gameprofile, null, sender.getName(), null, s);
      server.getPlayerList().getBannedPlayers().addEntry((UserListEntry)userlistbansentry);
      EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);
      if (entityplayermp != null)
        entityplayermp.connection.func_194028_b((ITextComponent)new TextComponentTranslation("multiplayer.disconnect.banned", new Object[0])); 
      notifyCommandListener(sender, (ICommand)this, "commands.ban.success", new Object[] { args[0] });
    } else {
      throw new WrongUsageException("commands.ban.usage", new Object[0]);
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length >= 1) ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandBanPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */