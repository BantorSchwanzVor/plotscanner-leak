package org.newdawn.slick.opengl;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class EmptyImageData implements ImageData {
  private int width;
  
  private int height;
  
  public EmptyImageData(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  public int getDepth() {
    return 32;
  }
  
  public int getHeight() {
    return this.height;
  }
  
  public ByteBuffer getImageBufferData() {
    return BufferUtils.createByteBuffer(getTexWidth() * getTexHeight() * 4);
  }
  
  public int getTexHeight() {
    return InternalTextureLoader.get2Fold(this.height);
  }
  
  public int getTexWidth() {
    return InternalTextureLoader.get2Fold(this.width);
  }
  
  public int getWidth() {
    return this.width;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\EmptyImageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */