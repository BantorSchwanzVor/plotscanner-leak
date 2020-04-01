package net.minecraft.client.resources.data;

public class AnimationFrame {
  private final int frameIndex;
  
  private final int frameTime;
  
  public AnimationFrame(int frameIndexIn) {
    this(frameIndexIn, -1);
  }
  
  public AnimationFrame(int frameIndexIn, int frameTimeIn) {
    this.frameIndex = frameIndexIn;
    this.frameTime = frameTimeIn;
  }
  
  public boolean hasNoTime() {
    return (this.frameTime == -1);
  }
  
  public int getFrameTime() {
    return this.frameTime;
  }
  
  public int getFrameIndex() {
    return this.frameIndex;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\data\AnimationFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */