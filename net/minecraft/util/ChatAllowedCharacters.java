package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;

public class ChatAllowedCharacters {
  public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
  
  public static final char[] ILLEGAL_STRUCTURE_CHARACTERS = new char[] { 
      '.', '\n', '\r', '\t', '\f', '`', '?', '*', '\\', '<', 
      '>', '|', '"' };
  
  public static final char[] ILLEGAL_FILE_CHARACTERS = new char[] { 
      '/', '\n', '\r', '\t', '\f', '`', '?', '*', '\\', '<', 
      '>', '|', '"', ':' };
  
  public static boolean isAllowedCharacter(char character) {
    return (character != '§' && character >= ' ' && character != '');
  }
  
  public static String filterAllowedCharacters(String input) {
    StringBuilder stringbuilder = new StringBuilder();
    byte b;
    int i;
    char[] arrayOfChar;
    for (i = (arrayOfChar = input.toCharArray()).length, b = 0; b < i; ) {
      char c0 = arrayOfChar[b];
      if (isAllowedCharacter(c0))
        stringbuilder.append(c0); 
      b++;
    } 
    return stringbuilder.toString();
  }
  
  static {
    ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\ChatAllowedCharacters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */