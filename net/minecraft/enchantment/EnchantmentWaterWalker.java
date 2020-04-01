package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentWaterWalker extends Enchantment {
  public EnchantmentWaterWalker(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.ARMOR_FEET, slots);
    setName("waterWalker");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return enchantmentLevel * 10;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 15;
  }
  
  public int getMaxLevel() {
    return 3;
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    return (super.canApplyTogether(ench) && ench != Enchantments.FROST_WALKER);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentWaterWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */