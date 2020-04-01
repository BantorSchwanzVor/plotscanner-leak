package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class GuiLockIconButton extends GuiButton {
  private boolean locked;
  
  public GuiLockIconButton(int p_i45538_1_, int p_i45538_2_, int p_i45538_3_) {
    super(p_i45538_1_, p_i45538_2_, p_i45538_3_, 20, 20, "");
  }
  
  public boolean isLocked() {
    return this.locked;
  }
  
  public void setLocked(boolean lockedIn) {
    this.locked = lockedIn;
  }
  
  public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
    if (this.visible) {
      Icon guilockiconbutton$icon;
      p_191745_1_.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      boolean flag = (p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height);
      if (this.locked) {
        if (!this.enabled) {
          guilockiconbutton$icon = Icon.LOCKED_DISABLED;
        } else if (flag) {
          guilockiconbutton$icon = Icon.LOCKED_HOVER;
        } else {
          guilockiconbutton$icon = Icon.LOCKED;
        } 
      } else if (!this.enabled) {
        guilockiconbutton$icon = Icon.UNLOCKED_DISABLED;
      } else if (flag) {
        guilockiconbutton$icon = Icon.UNLOCKED_HOVER;
      } else {
        guilockiconbutton$icon = Icon.UNLOCKED;
      } 
      drawTexturedModalRect(this.xPosition, this.yPosition, guilockiconbutton$icon.getX(), guilockiconbutton$icon.getY(), this.width, this.height);
    } 
  }
  
  enum Icon {
    LOCKED(0, 146),
    LOCKED_HOVER(0, 166),
    LOCKED_DISABLED(0, 186),
    UNLOCKED(20, 146),
    UNLOCKED_HOVER(20, 166),
    UNLOCKED_DISABLED(20, 186);
    
    private final int x;
    
    private final int y;
    
    Icon(int p_i45537_3_, int p_i45537_4_) {
      this.x = p_i45537_3_;
      this.y = p_i45537_4_;
    }
    
    public int getX() {
      return this.x;
    }
    
    public int getY() {
      return this.y;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiLockIconButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */