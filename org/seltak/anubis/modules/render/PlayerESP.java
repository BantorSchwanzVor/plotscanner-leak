package org.seltak.anubis.modules.render;

import net.minecraft.entity.Entity;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class PlayerESP extends Module {
  public PlayerESP() {
    super("PlayerESP", Category.RENDER, 49);
  }
  
  public void onPreUpdate() {
    for (Entity entity : this.mc.world.loadedEntityList) {
      if (entity instanceof net.minecraft.entity.player.EntityPlayer)
        entity.setGlowing(true); 
    } 
    super.onPreUpdate();
  }
  
  public void onDisable() {
    for (Entity entity : this.mc.world.loadedEntityList)
      entity.setGlowing(false); 
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\render\PlayerESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */