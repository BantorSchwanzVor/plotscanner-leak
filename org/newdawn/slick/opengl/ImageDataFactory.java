package org.newdawn.slick.opengl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.newdawn.slick.util.Log;

public class ImageDataFactory {
  private static boolean usePngLoader = true;
  
  private static boolean pngLoaderPropertyChecked = false;
  
  private static final String PNG_LOADER = "org.newdawn.slick.pngloader";
  
  private static void checkProperty() {
    if (!pngLoaderPropertyChecked) {
      pngLoaderPropertyChecked = true;
      try {
        AccessController.doPrivileged(new PrivilegedAction() {
              public Object run() {
                String val = System.getProperty("org.newdawn.slick.pngloader");
                if ("false".equalsIgnoreCase(val))
                  ImageDataFactory.usePngLoader = false; 
                Log.info("Use Java PNG Loader = " + ImageDataFactory.usePngLoader);
                return null;
              }
            });
      } catch (Throwable throwable) {}
    } 
  }
  
  public static LoadableImageData getImageDataFor(String ref) {
    checkProperty();
    ref = ref.toLowerCase();
    if (ref.endsWith(".tga"))
      return new TGAImageData(); 
    if (ref.endsWith(".png")) {
      CompositeImageData data = new CompositeImageData();
      if (usePngLoader)
        data.add(new PNGImageData()); 
      data.add(new ImageIOImageData());
      return data;
    } 
    return new ImageIOImageData();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\opengl\ImageDataFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */