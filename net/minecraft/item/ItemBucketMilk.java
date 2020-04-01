package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBucketMilk extends Item {
  public ItemBucketMilk() {
    setMaxStackSize(1);
    setCreativeTab(CreativeTabs.MISC);
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
    if (entityLiving instanceof EntityPlayerMP) {
      EntityPlayerMP entityplayermp = (EntityPlayerMP)entityLiving;
      CriteriaTriggers.field_193138_y.func_193148_a(entityplayermp, stack);
      entityplayermp.addStat(StatList.getObjectUseStats(this));
    } 
    if (entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).capabilities.isCreativeMode)
      stack.func_190918_g(1); 
    if (!worldIn.isRemote)
      entityLiving.clearActivePotions(); 
    return stack.func_190926_b() ? new ItemStack(Items.BUCKET) : stack;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 32;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.DRINK;
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    worldIn.setActiveHand(playerIn);
    return new ActionResult(EnumActionResult.SUCCESS, worldIn.getHeldItem(playerIn));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemBucketMilk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */