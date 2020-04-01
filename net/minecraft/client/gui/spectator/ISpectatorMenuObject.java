package net.minecraft.client.gui.spectator;

import net.minecraft.util.text.ITextComponent;

public interface ISpectatorMenuObject {
  void selectItem(SpectatorMenu paramSpectatorMenu);
  
  ITextComponent getSpectatorName();
  
  void renderIcon(float paramFloat, int paramInt);
  
  boolean isEnabled();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\spectator\ISpectatorMenuObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */