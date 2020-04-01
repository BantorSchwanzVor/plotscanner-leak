package org.newdawn.slick.openal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class MODSound extends AudioImpl {
  private SoundStore store;
  
  public MODSound(SoundStore store, InputStream in) throws IOException {
    this.store = store;
  }
  
  public int playAsMusic(float pitch, float gain, boolean loop) {
    cleanUpSource();
    this.store.setCurrentMusicVolume(gain);
    this.store.setMOD(this);
    return this.store.getSource(0);
  }
  
  private void cleanUpSource() {
    AL10.alSourceStop(this.store.getSource(0));
    IntBuffer buffer = BufferUtils.createIntBuffer(1);
    int queued = AL10.alGetSourcei(this.store.getSource(0), 4117);
    while (queued > 0) {
      AL10.alSourceUnqueueBuffers(this.store.getSource(0), buffer);
      queued--;
    } 
    AL10.alSourcei(this.store.getSource(0), 4105, 0);
  }
  
  public void poll() {}
  
  public int playAsSoundEffect(float pitch, float gain, boolean loop) {
    return -1;
  }
  
  public void stop() {
    this.store.setMOD(null);
  }
  
  public float getPosition() {
    throw new RuntimeException("Positioning on modules is not currently supported");
  }
  
  public boolean setPosition(float position) {
    throw new RuntimeException("Positioning on modules is not currently supported");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\openal\MODSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */