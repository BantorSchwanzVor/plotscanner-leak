package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEmptyMap extends ItemMapBase {
  protected ItemEmptyMap() {
    setCreativeTab(CreativeTabs.MISC);
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = ItemMap.func_190906_a(itemStackIn, worldIn.posX, worldIn.posZ, (byte)0, true, false);
    ItemStack itemstack1 = worldIn.getHeldItem(playerIn);
    itemstack1.func_190918_g(1);
    if (itemstack1.func_190926_b())
      return new ActionResult(EnumActionResult.SUCCESS, itemstack); 
    if (!worldIn.inventory.addItemStackToInventory(itemstack.copy()))
      worldIn.dropItem(itemstack, false); 
    worldIn.addStat(StatList.getObjectUseStats(this));
    return new ActionResult(EnumActionResult.SUCCESS, itemstack1);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemEmptyMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */