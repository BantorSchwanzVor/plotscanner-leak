package org.newdawn.slick.openal;

public class NullAudio implements Audio {
  public int getBufferID() {
    return 0;
  }
  
  public float getPosition() {
    return 0.0F;
  }
  
  public boolean isPlaying() {
    return false;
  }
  
  public int playAsMusic(float pitch, float gain, boolean loop) {
    return 0;
  }
  
  public int playAsSoundEffect(float pitch, float gain, boolean loop) {
    return 0;
  }
  
  public int playAsSoundEffect(float pitch, float gain, boolean loop, float x, float y, float z) {
    return 0;
  }
  
  public boolean setPosition(float position) {
    return false;
  }
  
  public void stop() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\openal\NullAudio.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */