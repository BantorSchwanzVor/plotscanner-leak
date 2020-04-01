package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase {
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return !(!server.isSinglePlayer() && !super.checkPermission(server, sender));
  }
  
  public String getCommandName() {
    return "seed";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.seed.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    World world = (sender instanceof EntityPlayer) ? ((EntityPlayer)sender).world : (World)server.worldServerForDimension(0);
    sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.seed.success", new Object[] { Long.valueOf(world.getSeed()) }));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandShowSeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */