package org.seltak.anubis.modules.movement;

import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.utils.BlockHelper;

public class Jesus extends Module {
  private boolean wasInWater;
  
  public Jesus() {
    super("Jesus", Category.MOVEMENT, 0);
  }
  
  public void onPostUpdate() {
    if (this.mc.player.onGround) {
      this.wasInWater = false;
      return;
    } 
    if (this.mc.player.fallDistance > 0.01D && BlockHelper.isInLiquid()) {
      this.mc.player.motionY += 1.121D;
      this.mc.player.motionY = -0.001D;
      this.mc.player.motionX += this.mc.player.motionX * 0.089D;
      this.mc.player.motionZ += this.mc.player.motionZ * 0.089D;
      this.mc.player.fallDistance = 0.0F;
      this.mc.player.motionY = 0.121D;
    } 
    this.wasInWater = (this.mc.player.fallDistance > 1.0F && BlockHelper.isInLiquid());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\Jesus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */