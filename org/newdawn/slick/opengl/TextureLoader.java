package org.newdawn.slick.opengl;

import java.io.IOException;
import java.io.InputStream;

public class TextureLoader {
  public static Texture getTexture(String format, InputStream in) throws IOException {
    return getTexture(format, in, false, 9729);
  }
  
  public static Texture getTexture(String format, InputStream in, boolean flipped) throws IOException {
    return getTexture(format, in, flipped, 9729);
  }
  
  public static Texture getTexture(String format, InputStream in, int filter) throws IOException {
    return getTexture(format, in, false, filter);
  }
  
  public static Texture getTexture(String format, InputStream in, boolean flipped, int filter) throws IOException {
    return InternalTextureLoader.get().getTexture(in, String.valueOf(in.toString()) + "." + format, flipped, filter);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\TextureLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */