package net.minecraft.util.text;

public enum ChatType {
  CHAT((byte)0),
  SYSTEM((byte)1),
  GAME_INFO((byte)2);
  
  private final byte field_192588_d;
  
  ChatType(byte p_i47429_3_) {
    this.field_192588_d = p_i47429_3_;
  }
  
  public byte func_192583_a() {
    return this.field_192588_d;
  }
  
  public static ChatType func_192582_a(byte p_192582_0_) {
    byte b;
    int i;
    ChatType[] arrayOfChatType;
    for (i = (arrayOfChatType = values()).length, b = 0; b < i; ) {
      ChatType chattype = arrayOfChatType[b];
      if (p_192582_0_ == chattype.field_192588_d)
        return chattype; 
      b++;
    } 
    return CHAT;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\ChatType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */