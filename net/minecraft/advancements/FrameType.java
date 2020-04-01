package net.minecraft.advancements;

import net.minecraft.util.text.TextFormatting;

public enum FrameType {
  TASK("task", 0, TextFormatting.GREEN),
  CHALLENGE("challenge", 26, TextFormatting.DARK_PURPLE),
  GOAL("goal", 52, TextFormatting.GREEN);
  
  private final String field_192313_d;
  
  private final int field_192314_e;
  
  private final TextFormatting field_193230_f;
  
  FrameType(String p_i47585_3_, int p_i47585_4_, TextFormatting p_i47585_5_) {
    this.field_192313_d = p_i47585_3_;
    this.field_192314_e = p_i47585_4_;
    this.field_193230_f = p_i47585_5_;
  }
  
  public String func_192307_a() {
    return this.field_192313_d;
  }
  
  public int func_192309_b() {
    return this.field_192314_e;
  }
  
  public static FrameType func_192308_a(String p_192308_0_) {
    byte b;
    int i;
    FrameType[] arrayOfFrameType;
    for (i = (arrayOfFrameType = values()).length, b = 0; b < i; ) {
      FrameType frametype = arrayOfFrameType[b];
      if (frametype.field_192313_d.equals(p_192308_0_))
        return frametype; 
      b++;
    } 
    throw new IllegalArgumentException("Unknown frame type '" + p_192308_0_ + "'");
  }
  
  public TextFormatting func_193229_c() {
    return this.field_193230_f;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\advancements\FrameType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */