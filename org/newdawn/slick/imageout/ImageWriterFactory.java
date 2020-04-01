package org.newdawn.slick.imageout;

import java.util.HashMap;
import javax.imageio.ImageIO;
import org.newdawn.slick.SlickException;

public class ImageWriterFactory {
  private static HashMap writers = new HashMap<>();
  
  static {
    String[] formats = ImageIO.getWriterFormatNames();
    ImageIOWriter writer = new ImageIOWriter();
    for (int i = 0; i < formats.length; i++)
      registerWriter(formats[i], writer); 
    TGAWriter tga = new TGAWriter();
    registerWriter("tga", tga);
  }
  
  public static void registerWriter(String format, ImageWriter writer) {
    writers.put(format, writer);
  }
  
  public static String[] getSupportedFormats() {
    return (String[])writers.keySet().toArray((Object[])new String[0]);
  }
  
  public static ImageWriter getWriterForFormat(String format) throws SlickException {
    ImageWriter writer = (ImageWriter)writers.get(format);
    if (writer != null)
      return writer; 
    writer = (ImageWriter)writers.get(format.toLowerCase());
    if (writer != null)
      return writer; 
    writer = (ImageWriter)writers.get(format.toUpperCase());
    if (writer != null)
      return writer; 
    throw new SlickException("No image writer available for: " + format);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\imageout\ImageWriterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */