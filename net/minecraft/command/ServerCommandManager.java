package net.minecraft.command;

import net.minecraft.command.server.CommandBanIp;
import net.minecraft.command.server.CommandBanPlayer;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandDeOp;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandListBans;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.command.server.CommandOp;
import net.minecraft.command.server.CommandPardonIp;
import net.minecraft.command.server.CommandPardonPlayer;
import net.minecraft.command.server.CommandPublishLocalServer;
import net.minecraft.command.server.CommandSaveAll;
import net.minecraft.command.server.CommandSaveOff;
import net.minecraft.command.server.CommandSaveOn;
import net.minecraft.command.server.CommandScoreboard;
import net.minecraft.command.server.CommandSetBlock;
import net.minecraft.command.server.CommandSetDefaultSpawnpoint;
import net.minecraft.command.server.CommandStop;
import net.minecraft.command.server.CommandSummon;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.command.server.CommandTestFor;
import net.minecraft.command.server.CommandTestForBlock;
import net.minecraft.command.server.CommandWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ServerCommandManager extends CommandHandler implements ICommandListener {
  private final MinecraftServer server;
  
  public ServerCommandManager(MinecraftServer serverIn) {
    this.server = serverIn;
    registerCommand(new CommandTime());
    registerCommand(new CommandGameMode());
    registerCommand(new CommandDifficulty());
    registerCommand(new CommandDefaultGameMode());
    registerCommand(new CommandKill());
    registerCommand(new CommandToggleDownfall());
    registerCommand(new CommandWeather());
    registerCommand(new CommandXP());
    registerCommand(new CommandTP());
    registerCommand((ICommand)new CommandTeleport());
    registerCommand(new CommandGive());
    registerCommand(new CommandReplaceItem());
    registerCommand(new CommandStats());
    registerCommand(new CommandEffect());
    registerCommand(new CommandEnchant());
    registerCommand(new CommandParticle());
    registerCommand((ICommand)new CommandEmote());
    registerCommand(new CommandShowSeed());
    registerCommand(new CommandHelp());
    registerCommand(new CommandDebug());
    registerCommand((ICommand)new CommandMessage());
    registerCommand((ICommand)new CommandBroadcast());
    registerCommand(new CommandSetSpawnpoint());
    registerCommand((ICommand)new CommandSetDefaultSpawnpoint());
    registerCommand(new CommandGameRule());
    registerCommand(new CommandClearInventory());
    registerCommand((ICommand)new CommandTestFor());
    registerCommand(new CommandSpreadPlayers());
    registerCommand(new CommandPlaySound());
    registerCommand((ICommand)new CommandScoreboard());
    registerCommand(new CommandExecuteAt());
    registerCommand(new CommandTrigger());
    registerCommand(new AdvancementCommand());
    registerCommand(new RecipeCommand());
    registerCommand((ICommand)new CommandSummon());
    registerCommand((ICommand)new CommandSetBlock());
    registerCommand(new CommandFill());
    registerCommand(new CommandClone());
    registerCommand(new CommandCompare());
    registerCommand(new CommandBlockData());
    registerCommand((ICommand)new CommandTestForBlock());
    registerCommand((ICommand)new CommandMessageRaw());
    registerCommand(new CommandWorldBorder());
    registerCommand(new CommandTitle());
    registerCommand(new CommandEntityData());
    registerCommand(new CommandStopSound());
    registerCommand(new CommandLocate());
    registerCommand(new CommandReload());
    registerCommand(new CommandFunction());
    if (serverIn.isDedicatedServer()) {
      registerCommand((ICommand)new CommandOp());
      registerCommand((ICommand)new CommandDeOp());
      registerCommand((ICommand)new CommandStop());
      registerCommand((ICommand)new CommandSaveAll());
      registerCommand((ICommand)new CommandSaveOff());
      registerCommand((ICommand)new CommandSaveOn());
      registerCommand((ICommand)new CommandBanIp());
      registerCommand((ICommand)new CommandPardonIp());
      registerCommand((ICommand)new CommandBanPlayer());
      registerCommand((ICommand)new CommandListBans());
      registerCommand((ICommand)new CommandPardonPlayer());
      registerCommand(new CommandServerKick());
      registerCommand((ICommand)new CommandListPlayers());
      registerCommand((ICommand)new CommandWhitelist());
      registerCommand(new CommandSetPlayerTimeout());
    } else {
      registerCommand((ICommand)new CommandPublishLocalServer());
    } 
    CommandBase.setCommandListener(this);
  }
  
  public void notifyListener(ICommandSender sender, ICommand command, int flags, String translationKey, Object... translationArgs) {
    boolean flag = true;
    MinecraftServer minecraftserver = this.server;
    if (!sender.sendCommandFeedback())
      flag = false; 
    TextComponentTranslation textComponentTranslation = new TextComponentTranslation("chat.type.admin", new Object[] { sender.getName(), new TextComponentTranslation(translationKey, translationArgs) });
    textComponentTranslation.getStyle().setColor(TextFormatting.GRAY);
    textComponentTranslation.getStyle().setItalic(Boolean.valueOf(true));
    if (flag)
      for (EntityPlayer entityplayer : minecraftserver.getPlayerList().getPlayerList()) {
        if (entityplayer != sender && minecraftserver.getPlayerList().canSendCommands(entityplayer.getGameProfile()) && command.checkPermission(this.server, sender)) {
          boolean flag1 = (sender instanceof MinecraftServer && this.server.shouldBroadcastConsoleToOps());
          boolean flag2 = (sender instanceof net.minecraft.network.rcon.RConConsoleSource && this.server.shouldBroadcastRconToOps());
          if (flag1 || flag2 || (!(sender instanceof net.minecraft.network.rcon.RConConsoleSource) && !(sender instanceof MinecraftServer)))
            entityplayer.addChatMessage((ITextComponent)textComponentTranslation); 
        } 
      }  
    if (sender != minecraftserver && minecraftserver.worldServers[0].getGameRules().getBoolean("logAdminCommands"))
      minecraftserver.addChatMessage((ITextComponent)textComponentTranslation); 
    boolean flag3 = minecraftserver.worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    if (sender instanceof CommandBlockBaseLogic)
      flag3 = ((CommandBlockBaseLogic)sender).shouldTrackOutput(); 
    if (((flags & 0x1) != 1 && flag3) || sender instanceof MinecraftServer)
      sender.addChatMessage((ITextComponent)new TextComponentTranslation(translationKey, translationArgs)); 
  }
  
  protected MinecraftServer getServer() {
    return this.server;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\ServerCommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */