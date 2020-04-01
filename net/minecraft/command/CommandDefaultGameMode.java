package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class CommandDefaultGameMode extends CommandGameMode {
  public String getCommandName() {
    return "defaultgamemode";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.defaultgamemode.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length <= 0)
      throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]); 
    GameType gametype = getGameModeFromCommand(sender, args[0]);
    setDefaultGameType(gametype, server);
    notifyCommandListener(sender, this, "commands.defaultgamemode.success", new Object[] { new TextComponentTranslation("gameMode." + gametype.getName(), new Object[0]) });
  }
  
  protected void setDefaultGameType(GameType gameType, MinecraftServer server) {
    server.setGameType(gameType);
    if (server.getForceGamemode())
      for (EntityPlayerMP entityplayermp : server.getPlayerList().getPlayerList())
        entityplayermp.setGameType(gameType);  
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandDefaultGameMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */