package org.newdawn.slick.imageout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageOut {
  private static final boolean DEFAULT_ALPHA_WRITE = false;
  
  public static String TGA = "tga";
  
  public static String PNG = "png";
  
  public static String JPG = "jpg";
  
  public static String[] getSupportedFormats() {
    return ImageWriterFactory.getSupportedFormats();
  }
  
  public static void write(Image image, String format, OutputStream out) throws SlickException {
    write(image, format, out, false);
  }
  
  public static void write(Image image, String format, OutputStream out, boolean writeAlpha) throws SlickException {
    try {
      ImageWriter writer = ImageWriterFactory.getWriterForFormat(format);
      writer.saveImage(image, format, out, writeAlpha);
    } catch (IOException e) {
      throw new SlickException("Unable to write out the image in format: " + format, e);
    } 
  }
  
  public static void write(Image image, String dest) throws SlickException {
    write(image, dest, false);
  }
  
  public static void write(Image image, String dest, boolean writeAlpha) throws SlickException {
    try {
      int ext = dest.lastIndexOf('.');
      if (ext < 0)
        throw new SlickException("Unable to determine format from: " + dest); 
      String format = dest.substring(ext + 1);
      write(image, format, new FileOutputStream(dest), writeAlpha);
    } catch (IOException e) {
      throw new SlickException("Unable to write to the destination: " + dest, e);
    } 
  }
  
  public static void write(Image image, String format, String dest) throws SlickException {
    write(image, format, dest, false);
  }
  
  public static void write(Image image, String format, String dest, boolean writeAlpha) throws SlickException {
    try {
      write(image, format, new FileOutputStream(dest), writeAlpha);
    } catch (IOException e) {
      throw new SlickException("Unable to write to the destination: " + dest, e);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\imageout\ImageOut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */