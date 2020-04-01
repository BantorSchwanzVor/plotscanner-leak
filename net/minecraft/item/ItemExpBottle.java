package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemExpBottle extends Item {
  public ItemExpBottle() {
    setCreativeTab(CreativeTabs.MISC);
  }
  
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = worldIn.getHeldItem(playerIn);
    if (!worldIn.capabilities.isCreativeMode)
      itemstack.func_190918_g(1); 
    itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    if (!itemStackIn.isRemote) {
      EntityExpBottle entityexpbottle = new EntityExpBottle(itemStackIn, (EntityLivingBase)worldIn);
      entityexpbottle.setHeadingFromThrower((Entity)worldIn, worldIn.rotationPitch, worldIn.rotationYaw, -20.0F, 0.7F, 1.0F);
      itemStackIn.spawnEntityInWorld((Entity)entityexpbottle);
    } 
    worldIn.addStat(StatList.getObjectUseStats(this));
    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemExpBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */