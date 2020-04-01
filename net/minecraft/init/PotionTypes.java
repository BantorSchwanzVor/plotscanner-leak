package net.minecraft.init;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;

public class PotionTypes {
  private static PotionType getRegisteredPotionType(String id) {
    PotionType potiontype = (PotionType)PotionType.REGISTRY.getObject(new ResourceLocation(id));
    if (!CACHE.add(potiontype))
      throw new IllegalStateException("Invalid Potion requested: " + id); 
    return potiontype;
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed Potions before Bootstrap!"); 
  }
  
  private static final Set<PotionType> CACHE = Sets.newHashSet();
  
  public static final PotionType EMPTY = getRegisteredPotionType("empty");
  
  public static final PotionType WATER = getRegisteredPotionType("water");
  
  public static final PotionType MUNDANE = getRegisteredPotionType("mundane");
  
  public static final PotionType THICK = getRegisteredPotionType("thick");
  
  public static final PotionType AWKWARD = getRegisteredPotionType("awkward");
  
  public static final PotionType NIGHT_VISION = getRegisteredPotionType("night_vision");
  
  public static final PotionType LONG_NIGHT_VISION = getRegisteredPotionType("long_night_vision");
  
  public static final PotionType INVISIBILITY = getRegisteredPotionType("invisibility");
  
  public static final PotionType LONG_INVISIBILITY = getRegisteredPotionType("long_invisibility");
  
  public static final PotionType LEAPING = getRegisteredPotionType("leaping");
  
  public static final PotionType LONG_LEAPING = getRegisteredPotionType("long_leaping");
  
  public static final PotionType STRONG_LEAPING = getRegisteredPotionType("strong_leaping");
  
  public static final PotionType FIRE_RESISTANCE = getRegisteredPotionType("fire_resistance");
  
  public static final PotionType LONG_FIRE_RESISTANCE = getRegisteredPotionType("long_fire_resistance");
  
  public static final PotionType SWIFTNESS = getRegisteredPotionType("swiftness");
  
  public static final PotionType LONG_SWIFTNESS = getRegisteredPotionType("long_swiftness");
  
  public static final PotionType STRONG_SWIFTNESS = getRegisteredPotionType("strong_swiftness");
  
  public static final PotionType SLOWNESS = getRegisteredPotionType("slowness");
  
  public static final PotionType LONG_SLOWNESS = getRegisteredPotionType("long_slowness");
  
  public static final PotionType WATER_BREATHING = getRegisteredPotionType("water_breathing");
  
  public static final PotionType LONG_WATER_BREATHING = getRegisteredPotionType("long_water_breathing");
  
  public static final PotionType HEALING = getRegisteredPotionType("healing");
  
  public static final PotionType STRONG_HEALING = getRegisteredPotionType("strong_healing");
  
  public static final PotionType HARMING = getRegisteredPotionType("harming");
  
  public static final PotionType STRONG_HARMING = getRegisteredPotionType("strong_harming");
  
  public static final PotionType POISON = getRegisteredPotionType("poison");
  
  public static final PotionType LONG_POISON = getRegisteredPotionType("long_poison");
  
  public static final PotionType STRONG_POISON = getRegisteredPotionType("strong_poison");
  
  public static final PotionType REGENERATION = getRegisteredPotionType("regeneration");
  
  public static final PotionType LONG_REGENERATION = getRegisteredPotionType("long_regeneration");
  
  public static final PotionType STRONG_REGENERATION = getRegisteredPotionType("strong_regeneration");
  
  public static final PotionType STRENGTH = getRegisteredPotionType("strength");
  
  public static final PotionType LONG_STRENGTH = getRegisteredPotionType("long_strength");
  
  public static final PotionType STRONG_STRENGTH = getRegisteredPotionType("strong_strength");
  
  public static final PotionType WEAKNESS = getRegisteredPotionType("weakness");
  
  public static final PotionType LONG_WEAKNESS = getRegisteredPotionType("long_weakness");
  
  static {
    CACHE.clear();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\init\PotionTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */