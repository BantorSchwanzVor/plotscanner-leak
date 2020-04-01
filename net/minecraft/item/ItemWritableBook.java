package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWritableBook extends Item {
  public ItemWritableBook() {
    setMaxStackSize(1);
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = worldIn.getHeldItem(playerIn);
    worldIn.openBook(itemstack, playerIn);
    worldIn.addStat(StatList.getObjectUseStats(this));
    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
  }
  
  public static boolean isNBTValid(NBTTagCompound nbt) {
    if (nbt == null)
      return false; 
    if (!nbt.hasKey("pages", 9))
      return false; 
    NBTTagList nbttaglist = nbt.getTagList("pages", 8);
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      String s = nbttaglist.getStringTagAt(i);
      if (s.length() > 32767)
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemWritableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */