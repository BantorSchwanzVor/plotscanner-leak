package net.minecraft.util;

import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class StringUtils {
  private static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
  
  public static String ticksToElapsedTime(int ticks) {
    int i = ticks / 20;
    int j = i / 60;
    i %= 60;
    return (i < 10) ? (String.valueOf(j) + ":0" + i) : (String.valueOf(j) + ":" + i);
  }
  
  public static String stripControlCodes(String text) {
    return PATTERN_CONTROL_CODE.matcher(text).replaceAll("");
  }
  
  public static boolean isNullOrEmpty(@Nullable String string) {
    return org.apache.commons.lang3.StringUtils.isEmpty(string);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */