package net.minecraft.command.server;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSetDefaultSpawnpoint extends CommandBase {
  public String getCommandName() {
    return "setworldspawn";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.setworldspawn.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    BlockPos blockpos;
    if (args.length == 0) {
      blockpos = getCommandSenderAsPlayer(sender).getPosition();
    } else {
      if (args.length != 3 || sender.getEntityWorld() == null)
        throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]); 
      blockpos = parseBlockPos(sender, args, 0, true);
    } 
    sender.getEntityWorld().setSpawnPoint(blockpos);
    server.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketSpawnPosition(blockpos));
    notifyCommandListener(sender, (ICommand)this, "commands.setworldspawn.success", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length > 0 && args.length <= 3) ? getTabCompletionCoordinate(args, 0, pos) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandSetDefaultSpawnpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */