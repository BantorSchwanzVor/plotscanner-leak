package javax.vecmath;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class VecMathI18N {
  static String getString(String key) {
    String s;
    try {
      s = ResourceBundle.getBundle("javax.vecmath.ExceptionStrings").getString(key);
    } catch (MissingResourceException e) {
      System.err.println("VecMathI18N: Error looking up: " + key);
      s = key;
    } 
    return s;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\javax\vecmath\VecMathI18N.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */