package optifine;

import java.util.Comparator;
import org.lwjgl.opengl.DisplayMode;

public class DisplayModeComparator implements Comparator {
  public int compare(Object p_compare_1_, Object p_compare_2_) {
    DisplayMode displaymode = (DisplayMode)p_compare_1_;
    DisplayMode displaymode1 = (DisplayMode)p_compare_2_;
    if (displaymode.getWidth() != displaymode1.getWidth())
      return displaymode.getWidth() - displaymode1.getWidth(); 
    if (displaymode.getHeight() != displaymode1.getHeight())
      return displaymode.getHeight() - displaymode1.getHeight(); 
    if (displaymode.getBitsPerPixel() != displaymode1.getBitsPerPixel())
      return displaymode.getBitsPerPixel() - displaymode1.getBitsPerPixel(); 
    return (displaymode.getFrequency() != displaymode1.getFrequency()) ? (displaymode.getFrequency() - displaymode1.getFrequency()) : 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\DisplayModeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */