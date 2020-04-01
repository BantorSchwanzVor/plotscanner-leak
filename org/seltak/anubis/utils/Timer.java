package org.seltak.anubis.utils;

public class Timer {
  private static long lastDelay;
  
  private static long lastMs;
  
  public static long average = 1L;
  
  private static long delay = 1L;
  
  public static void reset() {
    lastMs = getCurrentTime();
  }
  
  public static boolean delayOver(long l) {
    return (getCurrentTime() - lastMs > l);
  }
  
  private static long getCurrentTime() {
    return System.currentTimeMillis();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubi\\utils\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */