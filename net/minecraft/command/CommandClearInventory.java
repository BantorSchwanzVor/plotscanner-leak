package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClearInventory extends CommandBase {
  public String getCommandName() {
    return "clear";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.clear.usage";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    EntityPlayerMP entityplayermp = (args.length == 0) ? getCommandSenderAsPlayer(sender) : getPlayer(server, sender, args[0]);
    Item item = (args.length >= 2) ? getItemByText(sender, args[1]) : null;
    int i = (args.length >= 3) ? parseInt(args[2], -1) : -1;
    int j = (args.length >= 4) ? parseInt(args[3], -1) : -1;
    NBTTagCompound nbttagcompound = null;
    if (args.length >= 5)
      try {
        nbttagcompound = JsonToNBT.getTagFromJson(buildString(args, 4));
      } catch (NBTException nbtexception) {
        throw new CommandException("commands.clear.tagError", new Object[] { nbtexception.getMessage() });
      }  
    if (args.length >= 2 && item == null)
      throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() }); 
    int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
    entityplayermp.inventoryContainer.detectAndSendChanges();
    if (!entityplayermp.capabilities.isCreativeMode)
      entityplayermp.updateHeldItem(); 
    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
    if (k == 0)
      throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() }); 
    if (j == 0) {
      sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.clear.testing", new Object[] { entityplayermp.getName(), Integer.valueOf(k) }));
    } else {
      notifyCommandListener(sender, this, "commands.clear.success", new Object[] { entityplayermp.getName(), Integer.valueOf(k) });
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, server.getAllUsernames()); 
    return (args.length == 2) ? getListOfStringsMatchingLastWord(args, Item.REGISTRY.getKeys()) : Collections.<String>emptyList();
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (index == 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandClearInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */