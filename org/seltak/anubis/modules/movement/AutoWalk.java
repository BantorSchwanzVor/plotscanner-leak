package org.seltak.anubis.modules.movement;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class AutoWalk extends Module {
  int x = 1;
  
  public AutoWalk() {
    super("AutoWalk", Category.MOVEMENT, 0);
  }
  
  public void onPreUpdate() {
    this.mc.gameSettings.keyBindForward.setPressed(true);
  }
  
  public void onDisable() {
    super.onDisable();
    this.mc.gameSettings.keyBindForward.setPressed(false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\AutoWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */