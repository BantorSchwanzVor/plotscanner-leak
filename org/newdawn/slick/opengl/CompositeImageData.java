package org.newdawn.slick.opengl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.newdawn.slick.util.Log;

public class CompositeImageData implements LoadableImageData {
  private ArrayList sources = new ArrayList();
  
  private LoadableImageData picked;
  
  public void add(LoadableImageData data) {
    this.sources.add(data);
  }
  
  public ByteBuffer loadImage(InputStream fis) throws IOException {
    return loadImage(fis, false, null);
  }
  
  public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
    return loadImage(fis, flipped, false, transparent);
  }
  
  public ByteBuffer loadImage(InputStream is, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
    CompositeIOException exception = new CompositeIOException();
    ByteBuffer buffer = null;
    BufferedInputStream in = new BufferedInputStream(is, is.available());
    in.mark(is.available());
    for (int i = 0; i < this.sources.size(); i++) {
      in.reset();
      try {
        LoadableImageData data = this.sources.get(i);
        buffer = data.loadImage(in, flipped, forceAlpha, transparent);
        this.picked = data;
        break;
      } catch (Exception e) {
        Log.warn(this.sources.get(i).getClass() + " failed to read the data", e);
        exception.addException(e);
      } 
    } 
    if (this.picked == null)
      throw exception; 
    return buffer;
  }
  
  private void checkPicked() {
    if (this.picked == null)
      throw new RuntimeException("Attempt to make use of uninitialised or invalid composite image data"); 
  }
  
  public int getDepth() {
    checkPicked();
    return this.picked.getDepth();
  }
  
  public int getHeight() {
    checkPicked();
    return this.picked.getHeight();
  }
  
  public ByteBuffer getImageBufferData() {
    checkPicked();
    return this.picked.getImageBufferData();
  }
  
  public int getTexHeight() {
    checkPicked();
    return this.picked.getTexHeight();
  }
  
  public int getTexWidth() {
    checkPicked();
    return this.picked.getTexWidth();
  }
  
  public int getWidth() {
    checkPicked();
    return this.picked.getWidth();
  }
  
  public void configureEdging(boolean edging) {
    for (int i = 0; i < this.sources.size(); i++)
      ((LoadableImageData)this.sources.get(i)).configureEdging(edging); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\CompositeImageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */