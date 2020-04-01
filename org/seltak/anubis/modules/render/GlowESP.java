package org.seltak.anubis.modules.render;

import net.minecraft.entity.Entity;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class GlowESP extends Module {
  public GlowESP() {
    super("GlowESP", Category.RENDER, 0);
  }
  
  public void onPreUpdate() {
    for (Entity entity : this.mc.world.loadedEntityList) {
      if (!entity.isInvisible())
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\render\GlowESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */