package net.minecraft.command;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandEntityData extends CommandBase {
  public String getCommandName() {
    return "entitydata";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.entitydata.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    NBTTagCompound nbttagcompound2;
    if (args.length < 2)
      throw new WrongUsageException("commands.entitydata.usage", new Object[0]); 
    Entity entity = getEntity(server, sender, args[0]);
    if (entity instanceof net.minecraft.entity.player.EntityPlayer)
      throw new CommandException("commands.entitydata.noPlayers", new Object[] { entity.getDisplayName() }); 
    NBTTagCompound nbttagcompound = entityToNBT(entity);
    NBTTagCompound nbttagcompound1 = nbttagcompound.copy();
    try {
      nbttagcompound2 = JsonToNBT.getTagFromJson(buildString(args, 1));
    } catch (NBTException nbtexception) {
      throw new CommandException("commands.entitydata.tagError", new Object[] { nbtexception.getMessage() });
    } 
    UUID uuid = entity.getUniqueID();
    nbttagcompound.merge(nbttagcompound2);
    entity.setUniqueId(uuid);
    if (nbttagcompound.equals(nbttagcompound1))
      throw new CommandException("commands.entitydata.failed", new Object[] { nbttagcompound.toString() }); 
    entity.readFromNBT(nbttagcompound);
    notifyCommandListener(sender, this, "commands.entitydata.success", new Object[] { nbttagcompound.toString() });
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (index == 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandEntityData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */