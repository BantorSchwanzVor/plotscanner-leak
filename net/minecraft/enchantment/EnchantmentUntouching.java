package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentUntouching extends Enchantment {
  protected EnchantmentUntouching(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.DIGGER, slots);
    setName("untouching");
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 15;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 1;
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    return (super.canApplyTogether(ench) && ench != Enchantments.FORTUNE);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentUntouching.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */