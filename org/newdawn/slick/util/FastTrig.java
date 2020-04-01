package org.newdawn.slick.util;

public class FastTrig {
  private static double reduceSinAngle(double radians) {
    double orig = radians;
    radians %= 6.283185307179586D;
    if (Math.abs(radians) > Math.PI)
      radians -= 6.283185307179586D; 
    if (Math.abs(radians) > 1.5707963267948966D)
      radians = Math.PI - radians; 
    return radians;
  }
  
  public static double sin(double radians) {
    radians = reduceSinAngle(radians);
    if (Math.abs(radians) <= 0.7853981633974483D)
      return Math.sin(radians); 
    return Math.cos(1.5707963267948966D - radians);
  }
  
  public static double cos(double radians) {
    return sin(radians + 1.5707963267948966D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\FastTrig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */