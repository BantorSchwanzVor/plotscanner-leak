package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class MovingSoundMinecartRiding extends MovingSound {
  private final EntityPlayer player;
  
  private final EntityMinecart minecart;
  
  public MovingSoundMinecartRiding(EntityPlayer playerRiding, EntityMinecart minecart) {
    super(SoundEvents.ENTITY_MINECART_INSIDE, SoundCategory.NEUTRAL);
    this.player = playerRiding;
    this.minecart = minecart;
    this.attenuationType = ISound.AttenuationType.NONE;
    this.repeat = true;
    this.repeatDelay = 0;
  }
  
  public void update() {
    if (!this.minecart.isDead && this.player.isRiding() && this.player.getRidingEntity() == this.minecart) {
      float f = MathHelper.sqrt(this.minecart.motionX * this.minecart.motionX + this.minecart.motionZ * this.minecart.motionZ);
      if (f >= 0.01D) {
        this.volume = 0.0F + MathHelper.clamp(f, 0.0F, 1.0F) * 0.75F;
      } else {
        this.volume = 0.0F;
      } 
    } else {
      this.donePlaying = true;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\audio\MovingSoundMinecartRiding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */