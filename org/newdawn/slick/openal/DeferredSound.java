package org.newdawn.slick.openal;

import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.util.Log;

public class DeferredSound extends AudioImpl implements DeferredResource {
  public static final int OGG = 1;
  
  public static final int WAV = 2;
  
  public static final int MOD = 3;
  
  public static final int AIF = 4;
  
  private int type;
  
  private String ref;
  
  private Audio target;
  
  private InputStream in;
  
  public DeferredSound(String ref, InputStream in, int type) {
    this.ref = ref;
    this.type = type;
    if (ref.equals(in.toString()))
      this.in = in; 
    LoadingList.get().add(this);
  }
  
  private void checkTarget() {
    if (this.target == null)
      throw new RuntimeException("Attempt to use deferred sound before loading"); 
  }
  
  public void load() throws IOException {
    boolean before = SoundStore.get().isDeferredLoading();
    SoundStore.get().setDeferredLoading(false);
    if (this.in != null) {
      switch (this.type) {
        case 1:
          this.target = SoundStore.get().getOgg(this.in);
          break;
        case 2:
          this.target = SoundStore.get().getWAV(this.in);
          break;
        case 3:
          this.target = SoundStore.get().getMOD(this.in);
          break;
        case 4:
          this.target = SoundStore.get().getAIF(this.in);
          break;
        default:
          Log.error("Unrecognised sound type: " + this.type);
          break;
      } 
    } else {
      switch (this.type) {
        case 1:
          this.target = SoundStore.get().getOgg(this.ref);
          break;
        case 2:
          this.target = SoundStore.get().getWAV(this.ref);
          break;
        case 3:
          this.target = SoundStore.get().getMOD(this.ref);
          break;
        case 4:
          this.target = SoundStore.get().getAIF(this.ref);
          break;
        default:
          Log.error("Unrecognised sound type: " + this.type);
          break;
      } 
    } 
    SoundStore.get().setDeferredLoading(before);
  }
  
  public boolean isPlaying() {
    checkTarget();
    return this.target.isPlaying();
  }
  
  public int playAsMusic(float pitch, float gain, boolean loop) {
    checkTarget();
    return this.target.playAsMusic(pitch, gain, loop);
  }
  
  public int playAsSoundEffect(float pitch, float gain, boolean loop) {
    checkTarget();
    return this.target.playAsSoundEffect(pitch, gain, loop);
  }
  
  public int playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z) {
    checkTarget();
    return this.target.playAsSoundEffect(pitch, gain, loop, x, y, z);
  }
  
  public void stop() {
    checkTarget();
    this.target.stop();
  }
  
  public String getDescription() {
    return this.ref;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\openal\DeferredSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */