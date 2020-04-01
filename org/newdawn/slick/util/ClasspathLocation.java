package org.newdawn.slick.util;

import java.io.InputStream;
import java.net.URL;

public class ClasspathLocation implements ResourceLocation {
  public URL getResource(String ref) {
    String cpRef = ref.replace('\\', '/');
    return ResourceLoader.class.getClassLoader().getResource(cpRef);
  }
  
  public InputStream getResourceAsStream(String ref) {
    String cpRef = ref.replace('\\', '/');
    return ResourceLoader.class.getClassLoader().getResourceAsStream(cpRef);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\ClasspathLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */