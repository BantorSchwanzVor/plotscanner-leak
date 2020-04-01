package org.newdawn.slick.muffin;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.newdawn.slick.util.Log;

public class FileMuffin implements Muffin {
  public void saveFile(HashMap scoreMap, String fileName) throws IOException {
    String userHome = System.getProperty("user.home");
    File file = new File(userHome);
    file = new File(file, ".java");
    if (!file.exists())
      file.mkdir(); 
    file = new File(file, fileName);
    FileOutputStream fos = new FileOutputStream(file);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(scoreMap);
    oos.close();
  }
  
  public HashMap loadFile(String fileName) throws IOException {
    HashMap<Object, Object> hashMap = new HashMap<>();
    String userHome = System.getProperty("user.home");
    File file = new File(userHome);
    file = new File(file, ".java");
    file = new File(file, fileName);
    if (file.exists())
      try {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        hashMap = (HashMap<Object, Object>)ois.readObject();
        ois.close();
      } catch (EOFException eOFException) {
      
      } catch (ClassNotFoundException e) {
        Log.error(e);
        throw new IOException("Failed to pull state from store - class not found");
      }  
    return hashMap;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\muffin\FileMuffin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */