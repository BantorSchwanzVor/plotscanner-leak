package org.seltak.anubis.modules.player;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class Bot extends Module {
  public Bot() {
    super("Bot", Category.PLAYER, 0);
  }
  
  public void onPreUpdate() {
    respawnWhenDead();
    super.onPreUpdate();
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  private void respawnWhenDead() {
    if (this.mc.player.isDead) {
      this.mc.player.respawnPlayer();
      this.mc.displayGuiScreen(null);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\player\Bot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */