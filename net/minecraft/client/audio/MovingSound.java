package net.minecraft.client.audio;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public abstract class MovingSound extends PositionedSound implements ITickableSound {
  protected boolean donePlaying;
  
  protected MovingSound(SoundEvent soundIn, SoundCategory categoryIn) {
    super(soundIn, categoryIn);
  }
  
  public boolean isDonePlaying() {
    return this.donePlaying;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\audio\MovingSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */