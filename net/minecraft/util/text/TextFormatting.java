package net.minecraft.util.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public enum TextFormatting {
  BLACK("BLACK", '0', 0),
  DARK_BLUE("DARK_BLUE", '1', 1),
  DARK_GREEN("DARK_GREEN", '2', 2),
  DARK_AQUA("DARK_AQUA", '3', 3),
  DARK_RED("DARK_RED", '4', 4),
  DARK_PURPLE("DARK_PURPLE", '5', 5),
  GOLD("GOLD", '6', 6),
  GRAY("GRAY", '7', 7),
  DARK_GRAY("DARK_GRAY", '8', 8),
  BLUE("BLUE", '9', 9),
  GREEN("GREEN", 'a', 10),
  AQUA("AQUA", 'b', 11),
  RED("RED", 'c', 12),
  LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
  YELLOW("YELLOW", 'e', 14),
  WHITE("WHITE", 'f', 15),
  OBFUSCATED("OBFUSCATED", 'k', true),
  BOLD("BOLD", 'l', true),
  STRIKETHROUGH("STRIKETHROUGH", 'm', true),
  UNDERLINE("UNDERLINE", 'n', true),
  ITALIC("ITALIC", 'o', true),
  RESET("RESET", 'r', -1);
  
  private static final Map<String, TextFormatting> NAME_MAPPING;
  
  private static final Pattern FORMATTING_CODE_PATTERN;
  
  private final String name;
  
  private final char formattingCode;
  
  private final boolean fancyStyling;
  
  private final String controlString;
  
  private final int colorIndex;
  
  static {
    NAME_MAPPING = Maps.newHashMap();
    FORMATTING_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    byte b;
    int i;
    TextFormatting[] arrayOfTextFormatting;
    for (i = (arrayOfTextFormatting = values()).length, b = 0; b < i; ) {
      TextFormatting textformatting = arrayOfTextFormatting[b];
      NAME_MAPPING.put(lowercaseAlpha(textformatting.name), textformatting);
      b++;
    } 
  }
  
  private static String lowercaseAlpha(String p_175745_0_) {
    return p_175745_0_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
  }
  
  TextFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex) {
    this.name = formattingName;
    this.formattingCode = formattingCodeIn;
    this.fancyStyling = fancyStylingIn;
    this.colorIndex = colorIndex;
    this.controlString = "§" + formattingCodeIn;
  }
  
  public int getColorIndex() {
    return this.colorIndex;
  }
  
  public boolean isFancyStyling() {
    return this.fancyStyling;
  }
  
  public boolean isColor() {
    return (!this.fancyStyling && this != RESET);
  }
  
  public String getFriendlyName() {
    return name().toLowerCase(Locale.ROOT);
  }
  
  public String toString() {
    return this.controlString;
  }
  
  @Nullable
  public static String getTextWithoutFormattingCodes(@Nullable String text) {
    return (text == null) ? null : FORMATTING_CODE_PATTERN.matcher(text).replaceAll("");
  }
  
  @Nullable
  public static TextFormatting getValueByName(@Nullable String friendlyName) {
    return (friendlyName == null) ? null : NAME_MAPPING.get(lowercaseAlpha(friendlyName));
  }
  
  @Nullable
  public static TextFormatting fromColorIndex(int index) {
    if (index < 0)
      return RESET; 
    byte b;
    int i;
    TextFormatting[] arrayOfTextFormatting;
    for (i = (arrayOfTextFormatting = values()).length, b = 0; b < i; ) {
      TextFormatting textformatting = arrayOfTextFormatting[b];
      if (textformatting.getColorIndex() == index)
        return textformatting; 
      b++;
    } 
    return null;
  }
  
  public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_) {
    List<String> list = Lists.newArrayList();
    byte b;
    int i;
    TextFormatting[] arrayOfTextFormatting;
    for (i = (arrayOfTextFormatting = values()).length, b = 0; b < i; ) {
      TextFormatting textformatting = arrayOfTextFormatting[b];
      if ((!textformatting.isColor() || p_96296_0_) && (!textformatting.isFancyStyling() || p_96296_1_))
        list.add(textformatting.getFriendlyName()); 
      b++;
    } 
    return list;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\TextFormatting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */