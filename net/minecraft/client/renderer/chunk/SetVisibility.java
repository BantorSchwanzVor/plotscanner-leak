package net.minecraft.client.renderer.chunk;

import java.util.Set;
import net.minecraft.util.EnumFacing;

public class SetVisibility {
  private static final int COUNT_FACES = (EnumFacing.values()).length;
  
  private long bits;
  
  public void setManyVisible(Set<EnumFacing> facing) {
    for (EnumFacing enumfacing : facing) {
      for (EnumFacing enumfacing1 : facing)
        setVisible(enumfacing, enumfacing1, true); 
    } 
  }
  
  public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_) {
    setBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
    setBit(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
  }
  
  public void setAllVisible(boolean visible) {
    if (visible) {
      this.bits = -1L;
    } else {
      this.bits = 0L;
    } 
  }
  
  public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
    return getBit(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
  }
  
  public String toString() {
    StringBuilder stringbuilder = new StringBuilder();
    stringbuilder.append(' ');
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      stringbuilder.append(' ').append(enumfacing.toString().toUpperCase().charAt(0));
      b++;
    } 
    stringbuilder.append('\n');
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing2 = arrayOfEnumFacing[b];
      stringbuilder.append(enumfacing2.toString().toUpperCase().charAt(0));
      byte b1;
      int j;
      EnumFacing[] arrayOfEnumFacing1;
      for (j = (arrayOfEnumFacing1 = EnumFacing.values()).length, b1 = 0; b1 < j; ) {
        EnumFacing enumfacing1 = arrayOfEnumFacing1[b1];
        if (enumfacing2 == enumfacing1) {
          stringbuilder.append("  ");
        } else {
          boolean flag = isVisible(enumfacing2, enumfacing1);
          stringbuilder.append(' ').append(flag ? 89 : 110);
        } 
        b1++;
      } 
      stringbuilder.append('\n');
      b++;
    } 
    return stringbuilder.toString();
  }
  
  private boolean getBit(int p_getBit_1_) {
    return ((this.bits & (1 << p_getBit_1_)) != 0L);
  }
  
  private void setBit(int p_setBit_1_, boolean p_setBit_2_) {
    if (p_setBit_2_) {
      setBit(p_setBit_1_);
    } else {
      clearBit(p_setBit_1_);
    } 
  }
  
  private void setBit(int p_setBit_1_) {
    this.bits |= (1 << p_setBit_1_);
  }
  
  private void clearBit(int p_clearBit_1_) {
    this.bits &= (1 << p_clearBit_1_ ^ 0xFFFFFFFF);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\chunk\SetVisibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */