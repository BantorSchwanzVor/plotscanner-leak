package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemChorusFruit extends ItemFood {
  public ItemChorusFruit(int amount, float saturation) {
    super(amount, saturation, false);
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
    ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
    if (!worldIn.isRemote) {
      double d0 = entityLiving.posX;
      double d1 = entityLiving.posY;
      double d2 = entityLiving.posZ;
      for (int i = 0; i < 16; i++) {
        double d3 = entityLiving.posX + (entityLiving.getRNG().nextDouble() - 0.5D) * 16.0D;
        double d4 = MathHelper.clamp(entityLiving.posY + (entityLiving.getRNG().nextInt(16) - 8), 0.0D, (worldIn.getActualHeight() - 1));
        double d5 = entityLiving.posZ + (entityLiving.getRNG().nextDouble() - 0.5D) * 16.0D;
        if (entityLiving.isRiding())
          entityLiving.dismountRidingEntity(); 
        if (entityLiving.attemptTeleport(d3, d4, d5)) {
          worldIn.playSound(null, d0, d1, d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
          entityLiving.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
          break;
        } 
      } 
      if (entityLiving instanceof EntityPlayer)
        ((EntityPlayer)entityLiving).getCooldownTracker().setCooldown(this, 20); 
    } 
    return itemstack;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemChorusFruit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */