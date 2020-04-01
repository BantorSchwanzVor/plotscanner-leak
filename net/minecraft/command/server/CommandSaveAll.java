package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase {
  public String getCommandName() {
    return "save-all";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.save.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.save.start", new Object[0]));
    if (server.getPlayerList() != null)
      server.getPlayerList().saveAllPlayerData(); 
    try {
      for (int i = 0; i < server.worldServers.length; i++) {
        if (server.worldServers[i] != null) {
          WorldServer worldserver = server.worldServers[i];
          boolean flag = worldserver.disableLevelSaving;
          worldserver.disableLevelSaving = false;
          worldserver.saveAllChunks(true, null);
          worldserver.disableLevelSaving = flag;
        } 
      } 
      if (args.length > 0 && "flush".equals(args[0])) {
        sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.save.flushStart", new Object[0]));
        for (int j = 0; j < server.worldServers.length; j++) {
          if (server.worldServers[j] != null) {
            WorldServer worldserver1 = server.worldServers[j];
            boolean flag1 = worldserver1.disableLevelSaving;
            worldserver1.disableLevelSaving = false;
            worldserver1.saveChunkData();
            worldserver1.disableLevelSaving = flag1;
          } 
        } 
        sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.save.flushEnd", new Object[0]));
      } 
    } catch (MinecraftException minecraftexception) {
      notifyCommandListener(sender, (ICommand)this, "commands.save.failed", new Object[] { minecraftexception.getMessage() });
      return;
    } 
    notifyCommandListener(sender, (ICommand)this, "commands.save.success", new Object[0]);
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "flush" }) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandSaveAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */