package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class ItemNameTag extends Item {
  public ItemNameTag() {
    setCreativeTab(CreativeTabs.TOOLS);
  }
  
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
    if (stack.hasDisplayName() && !(target instanceof EntityPlayer)) {
      target.setCustomNameTag(stack.getDisplayName());
      if (target instanceof EntityLiving)
        ((EntityLiving)target).enablePersistence(); 
      stack.func_190918_g(1);
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemNameTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */