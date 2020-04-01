package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;

public class ItemArrow extends Item {
  public ItemArrow() {
    setCreativeTab(CreativeTabs.COMBAT);
  }
  
  public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
    EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
    entitytippedarrow.setPotionEffect(stack);
    return (EntityArrow)entitytippedarrow;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */