package net.minecraft.potion;

public class PotionHealth extends Potion {
  public PotionHealth(boolean isBadEffectIn, int liquidColorIn) {
    super(isBadEffectIn, liquidColorIn);
  }
  
  public boolean isInstant() {
    return true;
  }
  
  public boolean isReady(int duration, int amplifier) {
    return (duration >= 1);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\potion\PotionHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */