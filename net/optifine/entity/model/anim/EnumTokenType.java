package net.optifine.entity.model.anim;

public enum EnumTokenType {
  IDENTIFIER("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "0123456789_:."),
  CONSTANT("0123456789", "."),
  OPERATOR("+-*/%", 1),
  COMMA(",", 1),
  BRACKET_OPEN("(", 1),
  BRACKET_CLOSE(")", 1);
  
  private String charsFirst;
  
  private String charsExt;
  
  private int maxLen;
  
  public static final EnumTokenType[] VALUES;
  
  static {
    VALUES = values();
  }
  
  EnumTokenType(String charsFirst) {
    this.charsFirst = charsFirst;
    this.charsExt = "";
  }
  
  EnumTokenType(String charsFirst, int maxLen) {
    this.charsFirst = charsFirst;
    this.charsExt = "";
    this.maxLen = maxLen;
  }
  
  EnumTokenType(String charsFirst, String charsExt) {
    this.charsFirst = charsFirst;
    this.charsExt = charsExt;
  }
  
  public String getCharsFirst() {
    return this.charsFirst;
  }
  
  public String getCharsExt() {
    return this.charsExt;
  }
  
  public static EnumTokenType getTypeByFirstChar(char ch) {
    for (int i = 0; i < VALUES.length; i++) {
      EnumTokenType enumtokentype = VALUES[i];
      if (enumtokentype.getCharsFirst().indexOf(ch) >= 0)
        return enumtokentype; 
    } 
    return null;
  }
  
  public boolean hasChar(char ch) {
    if (getCharsFirst().indexOf(ch) >= 0)
      return true; 
    return (getCharsExt().indexOf(ch) >= 0);
  }
  
  public int getMaxLen() {
    return this.maxLen;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\EnumTokenType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */