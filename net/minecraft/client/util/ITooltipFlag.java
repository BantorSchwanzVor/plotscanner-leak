package net.minecraft.client.util;

public interface ITooltipFlag {
  boolean func_194127_a();
  
  public enum TooltipFlags implements ITooltipFlag {
    NORMAL(false),
    ADVANCED(true);
    
    final boolean field_194131_c;
    
    TooltipFlags(boolean p_i47611_3_) {
      this.field_194131_c = p_i47611_3_;
    }
    
    public boolean func_194127_a() {
      return this.field_194131_c;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\clien\\util\ITooltipFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */