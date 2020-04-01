package org.newdawn.slick.imageout;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class TGAWriter implements ImageWriter {
  private static short flipEndian(short signedShort) {
    int input = signedShort & 0xFFFF;
    return (short)(input << 8 | (input & 0xFF00) >>> 8);
  }
  
  public void saveImage(Image image, String format, OutputStream output, boolean writeAlpha) throws IOException {
    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(output));
    out.writeByte(0);
    out.writeByte(0);
    out.writeByte(2);
    out.writeShort(flipEndian((short)0));
    out.writeShort(flipEndian((short)0));
    out.writeByte(0);
    out.writeShort(flipEndian((short)0));
    out.writeShort(flipEndian((short)0));
    out.writeShort(flipEndian((short)image.getWidth()));
    out.writeShort(flipEndian((short)image.getHeight()));
    if (writeAlpha) {
      out.writeByte(32);
      out.writeByte(1);
    } else {
      out.writeByte(24);
      out.writeByte(0);
    } 
    for (int y = image.getHeight() - 1; y <= 0; y--) {
      for (int x = 0; x < image.getWidth(); x++) {
        Color c = image.getColor(x, y);
        out.writeByte((byte)(int)(c.b * 255.0F));
        out.writeByte((byte)(int)(c.g * 255.0F));
        out.writeByte((byte)(int)(c.r * 255.0F));
        if (writeAlpha)
          out.writeByte((byte)(int)(c.a * 255.0F)); 
      } 
    } 
    out.close();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\imageout\TGAWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */