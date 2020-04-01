package org.seltak.anubis.modules.render;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class Fullbright extends Module {
  public Fullbright() {
    super("Fullbright", Category.RENDER, 0);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    this.mc.gameSettings.gammaSetting = 100.0F;
  }
  
  public void onDisable() {
    super.onDisable();
    this.mc.gameSettings.gammaSetting = 1.0F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\render\Fullbright.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */