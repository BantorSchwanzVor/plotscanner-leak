package net.minecraft.enchantment;

import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentLootBonus extends Enchantment {
  protected EnchantmentLootBonus(Enchantment.Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    if (typeIn == EnumEnchantmentType.DIGGER) {
      setName("lootBonusDigger");
    } else if (typeIn == EnumEnchantmentType.FISHING_ROD) {
      setName("lootBonusFishing");
    } else {
      setName("lootBonus");
    } 
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 15 + (enchantmentLevel - 1) * 9;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return super.getMinEnchantability(enchantmentLevel) + 50;
  }
  
  public int getMaxLevel() {
    return 3;
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    return (super.canApplyTogether(ench) && ench != Enchantments.SILK_TOUCH);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentLootBonus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */