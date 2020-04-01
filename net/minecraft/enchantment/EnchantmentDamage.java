package net.minecraft.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class EnchantmentDamage extends Enchantment {
  private static final String[] PROTECTION_NAME = new String[] { "all", "undead", "arthropods" };
  
  private static final int[] BASE_ENCHANTABILITY = new int[] { 1, 5, 5 };
  
  private static final int[] LEVEL_ENCHANTABILITY = new int[] { 11, 8, 8 };
  
  private static final int[] THRESHOLD_ENCHANTABILITY = new int[] { 20, 20, 20 };
  
  public final int damageType;
  
  public EnchantmentDamage(Enchantment.Rarity rarityIn, int damageTypeIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.WEAPON, slots);
    this.damageType = damageTypeIn;
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return BASE_ENCHANTABILITY[this.damageType] + (enchantmentLevel - 1) * LEVEL_ENCHANTABILITY[this.damageType];
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + THRESHOLD_ENCHANTABILITY[this.damageType];
  }
  
  public int getMaxLevel() {
    return 5;
  }
  
  public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
    if (this.damageType == 0)
      return 1.0F + Math.max(0, level - 1) * 0.5F; 
    if (this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD)
      return level * 2.5F; 
    return (this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD) ? (level * 2.5F) : 0.0F;
  }
  
  public String getName() {
    return "enchantment.damage." + PROTECTION_NAME[this.damageType];
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    return !(ench instanceof EnchantmentDamage);
  }
  
  public boolean canApply(ItemStack stack) {
    return (stack.getItem() instanceof net.minecraft.item.ItemAxe) ? true : super.canApply(stack);
  }
  
  public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
    if (target instanceof EntityLivingBase) {
      EntityLivingBase entitylivingbase = (EntityLivingBase)target;
      if (this.damageType == 2 && entitylivingbase.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
        int i = 20 + user.getRNG().nextInt(10 * level);
        entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, i, 3));
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */