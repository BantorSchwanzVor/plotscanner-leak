package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSweepingEdge extends Enchantment {
  public EnchantmentSweepingEdge(Enchantment.Rarity p_i47366_1_, EntityEquipmentSlot... p_i47366_2_) {
    super(p_i47366_1_, EnumEnchantmentType.WEAPON, p_i47366_2_);
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return 5 + (enchantmentLevel - 1) * 9;
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + 15;
  }
  
  public int getMaxLevel() {
    return 3;
  }
  
  public static float func_191526_e(int p_191526_0_) {
    return 1.0F - 1.0F / (p_191526_0_ + 1);
  }
  
  public String getName() {
    return "enchantment.sweeping";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentSweepingEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */