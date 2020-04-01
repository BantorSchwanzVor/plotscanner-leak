package net.minecraft.command.server;

import com.google.common.collect.Lists;
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

public class CommandOp extends CommandBase {
  public String getCommandName() {
    return "op";
  }
  
  public int getRequiredPermissionLevel() {
    return 3;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.op.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 1 && args[0].length() > 0) {
      GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
      if (gameprofile == null)
        throw new CommandException("commands.op.failed", new Object[] { args[0] }); 
      server.getPlayerList().addOp(gameprofile);
      notifyCommandListener(sender, (ICommand)this, "commands.op.success", new Object[] { args[0] });
    } else {
      throw new WrongUsageException("commands.op.usage", new Object[0]);
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1) {
      String s = args[args.length - 1];
      List<String> list = Lists.newArrayList();
      byte b;
      int i;
      GameProfile[] arrayOfGameProfile;
      for (i = (arrayOfGameProfile = server.getGameProfiles()).length, b = 0; b < i; ) {
        GameProfile gameprofile = arrayOfGameProfile[b];
        if (!server.getPlayerList().canSendCommands(gameprofile) && doesStringStartWith(s, gameprofile.getName()))
          list.add(gameprofile.getName()); 
        b++;
      } 
      return list;
    } 
    return Collections.emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandOp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */