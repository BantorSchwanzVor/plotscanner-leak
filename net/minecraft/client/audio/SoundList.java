package net.minecraft.client.audio;

import java.util.List;
import javax.annotation.Nullable;

public class SoundList {
  private final List<Sound> sounds;
  
  private final boolean replaceExisting;
  
  private final String subtitle;
  
  public SoundList(List<Sound> soundsIn, boolean replceIn, String subtitleIn) {
    this.sounds = soundsIn;
    this.replaceExisting = replceIn;
    this.subtitle = subtitleIn;
  }
  
  public List<Sound> getSounds() {
    return this.sounds;
  }
  
  public boolean canReplaceExisting() {
    return this.replaceExisting;
  }
  
  @Nullable
  public String getSubtitle() {
    return this.subtitle;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\audio\SoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */