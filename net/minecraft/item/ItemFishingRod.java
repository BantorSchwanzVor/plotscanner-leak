package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFishingRod extends Item {
  public ItemFishingRod() {
    setMaxDamage(64);
    setMaxStackSize(1);
    setCreativeTab(CreativeTabs.TOOLS);
    addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
          public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            if (entityIn == null)
              return 0.0F; 
            boolean flag = (entityIn.getHeldItemMainhand() == stack);
            boolean flag1 = (entityIn.getHeldItemOffhand() == stack);
            if (entityIn.getHeldItemMainhand().getItem() instanceof ItemFishingRod)
              flag1 = false; 
            return ((flag || flag1) && entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).fishEntity != null) ? 1.0F : 0.0F;
          }
        });
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public boolean shouldRotateAroundWhenRendering() {
    return true;
  }
  
  public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
    ItemStack itemstack = worldIn.getHeldItem(playerIn);
    if (worldIn.fishEntity != null) {
      int i = worldIn.fishEntity.handleHookRetraction();
      itemstack.damageItem(i, (EntityLivingBase)worldIn);
      worldIn.swingArm(playerIn);
      itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.field_193780_J, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    } else {
      itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!itemStackIn.isRemote) {
        EntityFishHook entityfishhook = new EntityFishHook(itemStackIn, worldIn);
        int j = EnchantmentHelper.func_191528_c(itemstack);
        if (j > 0)
          entityfishhook.func_191516_a(j); 
        int k = EnchantmentHelper.func_191529_b(itemstack);
        if (k > 0)
          entityfishhook.func_191517_b(k); 
        itemStackIn.spawnEntityInWorld((Entity)entityfishhook);
      } 
      worldIn.swingArm(playerIn);
      worldIn.addStat(StatList.getObjectUseStats(this));
    } 
    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
  }
  
  public int getItemEnchantability() {
    return 1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemFishingRod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */