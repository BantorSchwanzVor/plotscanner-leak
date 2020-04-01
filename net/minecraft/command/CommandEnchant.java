package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandEnchant extends CommandBase {
  public String getCommandName() {
    return "enchant";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.enchant.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    Enchantment enchantment;
    if (args.length < 2)
      throw new WrongUsageException("commands.enchant.usage", new Object[0]); 
    EntityLivingBase entitylivingbase = getEntity(server, sender, args[0], EntityLivingBase.class);
    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
    try {
      enchantment = Enchantment.getEnchantmentByID(parseInt(args[1], 0));
    } catch (NumberInvalidException var12) {
      enchantment = Enchantment.getEnchantmentByLocation(args[1]);
    } 
    if (enchantment == null)
      throw new NumberInvalidException("commands.enchant.notFound", new Object[] { args[1] }); 
    int i = 1;
    ItemStack itemstack = entitylivingbase.getHeldItemMainhand();
    if (itemstack.func_190926_b())
      throw new CommandException("commands.enchant.noItem", new Object[0]); 
    if (!enchantment.canApply(itemstack))
      throw new CommandException("commands.enchant.cantEnchant", new Object[0]); 
    if (args.length >= 3)
      i = parseInt(args[2], enchantment.getMinLevel(), enchantment.getMaxLevel()); 
    if (itemstack.hasTagCompound()) {
      NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
      for (int j = 0; j < nbttaglist.tagCount(); j++) {
        int k = nbttaglist.getCompoundTagAt(j).getShort("id");
        if (Enchantment.getEnchantmentByID(k) != null) {
          Enchantment enchantment1 = Enchantment.getEnchantmentByID(k);
          if (!enchantment.func_191560_c(enchantment1))
            throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment.getTranslatedName(i), enchantment1.getTranslatedName(nbttaglist.getCompoundTagAt(j).getShort("lvl")) }); 
        } 
      } 
    } 
    itemstack.addEnchantment(enchantment, i);
    notifyCommandListener(sender, this, "commands.enchant.success", new Object[0]);
    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, server.getAllUsernames()); 
    return (args.length == 2) ? getListOfStringsMatchingLastWord(args, Enchantment.REGISTRY.getKeys()) : Collections.<String>emptyList();
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (index == 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandEnchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */