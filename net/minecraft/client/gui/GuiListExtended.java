package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot {
  public GuiListExtended(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
    super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
  }
  
  protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}
  
  protected boolean isSelected(int slotIndex) {
    return false;
  }
  
  protected void drawBackground() {}
  
  protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
    getListEntry(p_192637_1_).func_192634_a(p_192637_1_, p_192637_2_, p_192637_3_, getListWidth(), p_192637_4_, p_192637_5_, p_192637_6_, (isMouseYWithinSlotBounds(p_192637_6_) && getSlotIndexFromScreenCoords(p_192637_5_, p_192637_6_) == p_192637_1_), p_192637_7_);
  }
  
  protected void func_192639_a(int p_192639_1_, int p_192639_2_, int p_192639_3_, float p_192639_4_) {
    getListEntry(p_192639_1_).func_192633_a(p_192639_1_, p_192639_2_, p_192639_3_, p_192639_4_);
  }
  
  public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
    if (isMouseYWithinSlotBounds(mouseY)) {
      int i = getSlotIndexFromScreenCoords(mouseX, mouseY);
      if (i >= 0) {
        int j = this.left + this.width / 2 - getListWidth() / 2 + 2;
        int k = this.top + 4 - getAmountScrolled() + i * this.slotHeight + this.headerPadding;
        int l = mouseX - j;
        int i1 = mouseY - k;
        if (getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i1)) {
          setEnabled(false);
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public boolean mouseReleased(int p_148181_1_, int p_148181_2_, int p_148181_3_) {
    for (int i = 0; i < getSize(); i++) {
      int j = this.left + this.width / 2 - getListWidth() / 2 + 2;
      int k = this.top + 4 - getAmountScrolled() + i * this.slotHeight + this.headerPadding;
      int l = p_148181_1_ - j;
      int i1 = p_148181_2_ - k;
      getListEntry(i).mouseReleased(i, p_148181_1_, p_148181_2_, p_148181_3_, l, i1);
    } 
    setEnabled(true);
    return false;
  }
  
  public abstract IGuiListEntry getListEntry(int paramInt);
  
  public static interface IGuiListEntry {
    void func_192633_a(int param1Int1, int param1Int2, int param1Int3, float param1Float);
    
    void func_192634_a(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, boolean param1Boolean, float param1Float);
    
    boolean mousePressed(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
    
    void mouseReleased(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiListExtended.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */