package net.optifine.entity.model.anim;

public class Token {
  private EnumTokenType type;
  
  private String text;
  
  public Token(EnumTokenType type, String text) {
    this.type = type;
    this.text = text;
  }
  
  public EnumTokenType getType() {
    return this.type;
  }
  
  public String getText() {
    return this.text;
  }
  
  public String toString() {
    return this.text;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */