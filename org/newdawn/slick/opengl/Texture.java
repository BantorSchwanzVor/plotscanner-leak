package org.newdawn.slick.opengl;

public interface Texture {
  boolean hasAlpha();
  
  String getTextureRef();
  
  void bind();
  
  int getImageHeight();
  
  int getImageWidth();
  
  float getHeight();
  
  float getWidth();
  
  int getTextureHeight();
  
  int getTextureWidth();
  
  void release();
  
  int getTextureID();
  
  byte[] getTextureData();
  
  void setTextureFilter(int paramInt);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\Texture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */