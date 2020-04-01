package net.minecraft.client.gui;

import net.minecraft.util.text.ITextComponent;

public class ChatLine {
  private final int updateCounterCreated;
  
  private final ITextComponent lineString;
  
  private final int chatLineID;
  
  public ChatLine(int p_i45000_1_, ITextComponent p_i45000_2_, int p_i45000_3_) {
    this.lineString = p_i45000_2_;
    this.updateCounterCreated = p_i45000_1_;
    this.chatLineID = p_i45000_3_;
  }
  
  public ITextComponent getChatComponent() {
    return this.lineString;
  }
  
  public int getUpdatedCounter() {
    return this.updateCounterCreated;
  }
  
  public int getChatLineID() {
    return this.chatLineID;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\ChatLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */