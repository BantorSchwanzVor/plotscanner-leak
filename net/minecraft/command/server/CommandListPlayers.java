package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListPlayers extends CommandBase {
  public String getCommandName() {
    return "list";
  }
  
  public int getRequiredPermissionLevel() {
    return 0;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.players.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    int i = server.getCurrentPlayerCount();
    sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.players.list", new Object[] { Integer.valueOf(i), Integer.valueOf(server.getMaxPlayers()) }));
    sender.addChatMessage((ITextComponent)new TextComponentString(server.getPlayerList().getFormattedListOfPlayers((args.length > 0 && "uuids".equalsIgnoreCase(args[0])))));
    sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandListPlayers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */