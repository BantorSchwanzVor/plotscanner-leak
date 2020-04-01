package net.minecraft.entity;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPart {
  World getWorld();
  
  boolean attackEntityFromPart(MultiPartEntityPart paramMultiPartEntityPart, DamageSource paramDamageSource, float paramFloat);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\IEntityMultiPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */