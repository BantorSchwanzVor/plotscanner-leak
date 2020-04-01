package shadersmod.client;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonShaderOption extends GuiButton {
  private ShaderOption shaderOption = null;
  
  public GuiButtonShaderOption(int buttonId, int x, int y, int widthIn, int heightIn, ShaderOption shaderOption, String text) {
    super(buttonId, x, y, widthIn, heightIn, text);
    this.shaderOption = shaderOption;
  }
  
  public ShaderOption getShaderOption() {
    return this.shaderOption;
  }
  
  public void valueChanged() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\GuiButtonShaderOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */