package org.seltak.anubis.modules.movement;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class Speed extends Module {
  private boolean boosted;
  
  public Speed() {
    super("Speed", Category.MOVEMENT, 0);
  }
  
  public void setup() {
    ArrayList<String> options = new ArrayList<>();
    options.add("Hypixel-Bhop-1");
    options.add("Hypixel-Bhop-2");
    Anubis.setmgr.rSetting(new Setting("Speed Mode", this, "Hypixel-Bhop-1", options));
  }
  
  public void onPreUpdate() {
    String mode = Anubis.setmgr.getSettingByName("Speed Mode").getValString();
    if (mode.equalsIgnoreCase("Hypixel-Bhop-1") && 
      this.mc.player != null && this.mc.world != null && 
      this.mc.gameSettings.keyBindForward.pressed && !this.mc.player.isCollidedHorizontally) {
      this.mc.gameSettings.keyBindJump.pressed = false;
      if (this.mc.player.onGround) {
        this.mc.player.jump();
        this.mc.timer.elapsedTicks = 1;
        this.mc.player.motionX *= 1.1008000373840332D;
        this.mc.player.motionZ *= 1.1008000373840332D;
        this.mc.player.moveStrafing *= 2.0F;
      } else {
        this.mc.player.jumpMovementFactor = 0.0265F;
      } 
    } 
    mode.equalsIgnoreCase("Hypixel-Bhop-2");
    super.onPreUpdate();
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */