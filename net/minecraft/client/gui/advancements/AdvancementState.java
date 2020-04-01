package net.minecraft.client.gui.advancements;

public enum AdvancementState {
  OBTAINED(0),
  UNOBTAINED(1);
  
  private final int field_192671_d;
  
  AdvancementState(int p_i47384_3_) {
    this.field_192671_d = p_i47384_3_;
  }
  
  public int func_192667_a() {
    return this.field_192671_d;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\advancements\AdvancementState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */