package org.seltak.anubis.modules.player;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class AutoRespawn extends Module {
  public AutoRespawn() {
    super("AutoRespawn", Category.PLAYER, 0);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    if (this.mc.player.isDead) {
      this.mc.player.respawnPlayer();
      this.mc.displayGuiScreen(null);
    } 
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\player\AutoRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */