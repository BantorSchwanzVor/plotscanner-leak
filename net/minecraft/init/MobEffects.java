package net.minecraft.init;

import javax.annotation.Nullable;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class MobEffects {
  @Nullable
  private static Potion getRegisteredMobEffect(String id) {
    Potion potion = (Potion)Potion.REGISTRY.getObject(new ResourceLocation(id));
    if (potion == null)
      throw new IllegalStateException("Invalid MobEffect requested: " + id); 
    return potion;
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed MobEffects before Bootstrap!"); 
  }
  
  public static final Potion SPEED = getRegisteredMobEffect("speed");
  
  public static final Potion SLOWNESS = getRegisteredMobEffect("slowness");
  
  public static final Potion HASTE = getRegisteredMobEffect("haste");
  
  public static final Potion MINING_FATIGUE = getRegisteredMobEffect("mining_fatigue");
  
  public static final Potion STRENGTH = getRegisteredMobEffect("strength");
  
  public static final Potion INSTANT_HEALTH = getRegisteredMobEffect("instant_health");
  
  public static final Potion INSTANT_DAMAGE = getRegisteredMobEffect("instant_damage");
  
  public static final Potion JUMP_BOOST = getRegisteredMobEffect("jump_boost");
  
  public static final Potion NAUSEA = getRegisteredMobEffect("nausea");
  
  public static final Potion REGENERATION = getRegisteredMobEffect("regeneration");
  
  public static final Potion RESISTANCE = getRegisteredMobEffect("resistance");
  
  public static final Potion FIRE_RESISTANCE = getRegisteredMobEffect("fire_resistance");
  
  public static final Potion WATER_BREATHING = getRegisteredMobEffect("water_breathing");
  
  public static final Potion INVISIBILITY = getRegisteredMobEffect("invisibility");
  
  public static final Potion BLINDNESS = getRegisteredMobEffect("blindness");
  
  public static final Potion NIGHT_VISION = getRegisteredMobEffect("night_vision");
  
  public static final Potion HUNGER = getRegisteredMobEffect("hunger");
  
  public static final Potion WEAKNESS = getRegisteredMobEffect("weakness");
  
  public static final Potion POISON = getRegisteredMobEffect("poison");
  
  public static final Potion WITHER = getRegisteredMobEffect("wither");
  
  public static final Potion HEALTH_BOOST = getRegisteredMobEffect("health_boost");
  
  public static final Potion ABSORPTION = getRegisteredMobEffect("absorption");
  
  public static final Potion SATURATION = getRegisteredMobEffect("saturation");
  
  public static final Potion GLOWING = getRegisteredMobEffect("glowing");
  
  public static final Potion LEVITATION = getRegisteredMobEffect("levitation");
  
  public static final Potion LUCK = getRegisteredMobEffect("luck");
  
  public static final Potion UNLUCK = getRegisteredMobEffect("unluck");
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\init\MobEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */