package org.seltak.anubis.modules.combat;

import net.minecraft.entity.Entity;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class AntiBot extends Module {
  public AntiBot() {
    super("AntiBot", Category.COMBAT, 0);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    for (Object entity : this.mc.world.loadedEntityList) {
      if (((Entity)entity).isInvisible() && entity != this.mc.player)
        this.mc.world.removeEntity((Entity)entity); 
    } 
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\combat\AntiBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */