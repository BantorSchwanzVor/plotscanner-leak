package org.seltak.anubis.modules.gui;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class ClickGUI extends Module {
  public ClickGUI() {
    super("ClickGUI", Category.GUI, 54);
  }
  
  public void setup() {
    ArrayList<String> options = new ArrayList<>();
    options.add("JellyLike");
    options.add("New");
    Anubis.setmgr.rSetting(new Setting("Design", this, "New", options));
    Anubis.setmgr.rSetting(new Setting("Sound", this, false));
    Anubis.setmgr.rSetting(new Setting("GuiRed", this, 255.0D, 0.0D, 255.0D, true));
    Anubis.setmgr.rSetting(new Setting("GuiGreen", this, 26.0D, 0.0D, 255.0D, true));
    Anubis.setmgr.rSetting(new Setting("GuiBlue", this, 42.0D, 0.0D, 255.0D, true));
  }
  
  public void onEnable() {
    super.onEnable();
    this.mc.displayGuiScreen((GuiScreen)Anubis.clickgui);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    setEnabled(false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\gui\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */