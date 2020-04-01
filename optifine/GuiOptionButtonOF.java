package optifine;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButtonOF extends GuiOptionButton implements IOptionControl {
  private GameSettings.Options option = null;
  
  public GuiOptionButtonOF(int p_i49_1_, int p_i49_2_, int p_i49_3_, GameSettings.Options p_i49_4_, String p_i49_5_) {
    super(p_i49_1_, p_i49_2_, p_i49_3_, p_i49_4_, p_i49_5_);
    this.option = p_i49_4_;
  }
  
  public GameSettings.Options getOption() {
    return this.option;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\GuiOptionButtonOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */