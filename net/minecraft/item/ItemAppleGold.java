package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemAppleGold extends ItemFood {
  public ItemAppleGold(int amount, float saturation, boolean isWolfFood) {
    super(amount, saturation, isWolfFood);
    setHasSubtypes(true);
  }
  
  public boolean hasEffect(ItemStack stack) {
    return !(!super.hasEffect(stack) && stack.getMetadata() <= 0);
  }
  
  public EnumRarity getRarity(ItemStack stack) {
    return (stack.getMetadata() == 0) ? EnumRarity.RARE : EnumRarity.EPIC;
  }
  
  protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
    if (!worldIn.isRemote)
      if (stack.getMetadata() > 0) {
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
        player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
      } else {
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
        player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
      }  
  }
  
  public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    if (func_194125_a(itemIn)) {
      tab.add(new ItemStack(this));
      tab.add(new ItemStack(this, 1, 1));
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemAppleGold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */