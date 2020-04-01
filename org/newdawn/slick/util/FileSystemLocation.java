package org.newdawn.slick.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileSystemLocation implements ResourceLocation {
  private File root;
  
  public FileSystemLocation(File root) {
    this.root = root;
  }
  
  public URL getResource(String ref) {
    try {
      File file = new File(this.root, ref);
      if (!file.exists())
        file = new File(ref); 
      if (!file.exists())
        return null; 
      return file.toURI().toURL();
    } catch (IOException e) {
      return null;
    } 
  }
  
  public InputStream getResourceAsStream(String ref) {
    try {
      File file = new File(this.root, ref);
      if (!file.exists())
        file = new File(ref); 
      return new FileInputStream(file);
    } catch (IOException e) {
      return null;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\FileSystemLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */