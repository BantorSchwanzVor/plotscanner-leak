package org.seltak.anubis.modules.movement;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class AutoJump extends Module {
  public AutoJump() {
    super("AutoJump", Category.MOVEMENT, 0);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    this.mc.gameSettings.keyBindJump.setPressed(true);
  }
  
  public void onDisable() {
    super.onDisable();
    this.mc.gameSettings.keyBindJump.setPressed(false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\AutoJump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */