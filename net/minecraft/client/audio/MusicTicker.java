package net.minecraft.client.audio;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

public class MusicTicker implements ITickable {
  private final Random rand = new Random();
  
  private final Minecraft mc;
  
  private ISound currentMusic;
  
  private int timeUntilNextMusic = 100;
  
  public MusicTicker(Minecraft mcIn) {
    this.mc = mcIn;
  }
  
  public void update() {
    MusicType musicticker$musictype = this.mc.getAmbientMusicType();
    if (this.currentMusic != null) {
      if (!musicticker$musictype.getMusicLocation().getSoundName().equals(this.currentMusic.getSoundLocation())) {
        this.mc.getSoundHandler().stopSound(this.currentMusic);
        this.timeUntilNextMusic = MathHelper.getInt(this.rand, 0, musicticker$musictype.getMinDelay() / 2);
      } 
      if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
        this.currentMusic = null;
        this.timeUntilNextMusic = Math.min(MathHelper.getInt(this.rand, musicticker$musictype.getMinDelay(), musicticker$musictype.getMaxDelay()), this.timeUntilNextMusic);
      } 
    } 
    this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, musicticker$musictype.getMaxDelay());
    if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0)
      playMusic(musicticker$musictype); 
  }
  
  public void playMusic(MusicType requestedMusicType) {
    this.currentMusic = PositionedSoundRecord.getMusicRecord(requestedMusicType.getMusicLocation());
    this.mc.getSoundHandler().playSound(this.currentMusic);
    this.timeUntilNextMusic = Integer.MAX_VALUE;
  }
  
  public enum MusicType {
    MENU((String)SoundEvents.MUSIC_MENU, 20, 600),
    GAME((String)SoundEvents.MUSIC_GAME, 12000, 24000),
    CREATIVE((String)SoundEvents.MUSIC_CREATIVE, 1200, 3600),
    CREDITS((String)SoundEvents.MUSIC_CREDITS, 0, 0),
    NETHER((String)SoundEvents.MUSIC_NETHER, 1200, 3600),
    END_BOSS((String)SoundEvents.MUSIC_DRAGON, 0, 0),
    END((String)SoundEvents.MUSIC_END, 6000, 24000);
    
    private final SoundEvent musicLocation;
    
    private final int minDelay;
    
    private final int maxDelay;
    
    MusicType(SoundEvent musicLocationIn, int minDelayIn, int maxDelayIn) {
      this.musicLocation = musicLocationIn;
      this.minDelay = minDelayIn;
      this.maxDelay = maxDelayIn;
    }
    
    public SoundEvent getMusicLocation() {
      return this.musicLocation;
    }
    
    public int getMinDelay() {
      return this.minDelay;
    }
    
    public int getMaxDelay() {
      return this.maxDelay;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\audio\MusicTicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */