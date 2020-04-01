package net.minecraft.util;

public class IntegerCache {
  private static final Integer[] CACHE = new Integer[65535];
  
  public static Integer getInteger(int value) {
    return Integer.valueOf((value > 0 && value < CACHE.length) ? CACHE[value].intValue() : value);
  }
  
  static {
    int i = 0;
    for (int j = CACHE.length; i < j; i++)
      CACHE[i] = Integer.valueOf(i); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\IntegerCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */