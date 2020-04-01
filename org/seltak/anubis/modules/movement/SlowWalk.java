package org.seltak.anubis.modules.movement;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class SlowWalk extends Module {
  public SlowWalk() {
    super("SlowWalk", Category.MOVEMENT, 0);
  }
  
  public void onPreUpdate() {
    this.mc.player.motionX *= 0.0D;
    this.mc.player.motionZ *= 0.0D;
    super.onPreUpdate();
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\SlowWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */