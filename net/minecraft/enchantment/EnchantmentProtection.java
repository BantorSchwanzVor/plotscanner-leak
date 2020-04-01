package net.minecraft.enchantment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class EnchantmentProtection extends Enchantment {
  public final Type protectionType;
  
  public EnchantmentProtection(Enchantment.Rarity rarityIn, Type protectionTypeIn, EntityEquipmentSlot... slots) {
    super(rarityIn, EnumEnchantmentType.ARMOR, slots);
    this.protectionType = protectionTypeIn;
    if (protectionTypeIn == Type.FALL)
      this.type = EnumEnchantmentType.ARMOR_FEET; 
  }
  
  public int getMinEnchantability(int enchantmentLevel) {
    return this.protectionType.getMinimalEnchantability() + (enchantmentLevel - 1) * this.protectionType.getEnchantIncreasePerLevel();
  }
  
  public int getMaxEnchantability(int enchantmentLevel) {
    return getMinEnchantability(enchantmentLevel) + this.protectionType.getEnchantIncreasePerLevel();
  }
  
  public int getMaxLevel() {
    return 4;
  }
  
  public int calcModifierDamage(int level, DamageSource source) {
    if (source.canHarmInCreative())
      return 0; 
    if (this.protectionType == Type.ALL)
      return level; 
    if (this.protectionType == Type.FIRE && source.isFireDamage())
      return level * 2; 
    if (this.protectionType == Type.FALL && source == DamageSource.fall)
      return level * 3; 
    if (this.protectionType == Type.EXPLOSION && source.isExplosion())
      return level * 2; 
    return (this.protectionType == Type.PROJECTILE && source.isProjectile()) ? (level * 2) : 0;
  }
  
  public String getName() {
    return "enchantment.protect." + this.protectionType.getTypeName();
  }
  
  public boolean canApplyTogether(Enchantment ench) {
    if (ench instanceof EnchantmentProtection) {
      EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;
      if (this.protectionType == enchantmentprotection.protectionType)
        return false; 
      return !(this.protectionType != Type.FALL && enchantmentprotection.protectionType != Type.FALL);
    } 
    return super.canApplyTogether(ench);
  }
  
  public static int getFireTimeForEntity(EntityLivingBase p_92093_0_, int p_92093_1_) {
    int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, p_92093_0_);
    if (i > 0)
      p_92093_1_ -= MathHelper.floor(p_92093_1_ * i * 0.15F); 
    return p_92093_1_;
  }
  
  public static double getBlastDamageReduction(EntityLivingBase entityLivingBaseIn, double damage) {
    int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, entityLivingBaseIn);
    if (i > 0)
      damage -= MathHelper.floor(damage * (i * 0.15F)); 
    return damage;
  }
  
  public enum Type {
    ALL("all", 1, 11, 20),
    FIRE("fire", 10, 8, 12),
    FALL("fall", 5, 6, 10),
    EXPLOSION("explosion", 5, 8, 12),
    PROJECTILE("projectile", 3, 6, 15);
    
    private final String typeName;
    
    private final int minEnchantability;
    
    private final int levelCost;
    
    private final int levelCostSpan;
    
    Type(String name, int minimal, int perLevelEnchantability, int p_i47051_6_) {
      this.typeName = name;
      this.minEnchantability = minimal;
      this.levelCost = perLevelEnchantability;
      this.levelCostSpan = p_i47051_6_;
    }
    
    public String getTypeName() {
      return this.typeName;
    }
    
    public int getMinimalEnchantability() {
      return this.minEnchantability;
    }
    
    public int getEnchantIncreasePerLevel() {
      return this.levelCost;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnchantmentProtection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */