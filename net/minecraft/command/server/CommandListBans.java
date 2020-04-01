package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListBans extends CommandBase {
  public String getCommandName() {
    return "banlist";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return ((server.getPlayerList().getBannedIPs().isLanServer() || server.getPlayerList().getBannedPlayers().isLanServer()) && super.checkPermission(server, sender));
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.banlist.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length >= 1 && "ips".equalsIgnoreCase(args[0])) {
      sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.banlist.ips", new Object[] { Integer.valueOf((server.getPlayerList().getBannedIPs().getKeys()).length) }));
      sender.addChatMessage((ITextComponent)new TextComponentString(joinNiceString((Object[])server.getPlayerList().getBannedIPs().getKeys())));
    } else {
      sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.banlist.players", new Object[] { Integer.valueOf((server.getPlayerList().getBannedPlayers().getKeys()).length) }));
      sender.addChatMessage((ITextComponent)new TextComponentString(joinNiceString((Object[])server.getPlayerList().getBannedPlayers().getKeys())));
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "players", "ips" }) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandListBans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */