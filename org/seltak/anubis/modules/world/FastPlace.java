package org.seltak.anubis.modules.world;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class FastPlace extends Module {
  public FastPlace() {
    super("FastPlace", Category.WORLD, 0);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    this.mc.setRightClickDelayTimer(0);
  }
  
  public void onDisable() {
    super.onDisable();
    this.mc.setRightClickDelayTimer(4);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\world\FastPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */