package org.seltak.anubis.modules.movement;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class AutoSprint extends Module {
  int counter = 0;
  
  public AutoSprint() {
    super("AutoSprint", Category.MOVEMENT, 47);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    if (this.mc.player.movementInput.field_192832_b > 0.0F)
      this.mc.player.setSprinting(true); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\AutoSprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */