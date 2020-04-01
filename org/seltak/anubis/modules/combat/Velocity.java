package org.seltak.anubis.modules.combat;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class Velocity extends Module {
  public Velocity() {
    super("Velocity", Category.COMBAT, 0);
  }
  
  public void setup() {
    ArrayList<String> options = new ArrayList<>();
    options.add("Vanilla");
    options.add("NCP");
    options.add("AAC");
    options.add("Hypixel");
    Anubis.setmgr.rSetting(new Setting("Velocity Mode", this, "NCP", options));
    super.setup();
  }
  
  public void onPreUpdate() {
    String mode = Anubis.setmgr.getSettingByName("Velocity Mode").getValString();
    if (mode.equalsIgnoreCase("Hypixel") && 
      this.mc.player.hurtTime > 0) {
      this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY - 0.26D, this.mc.player.posZ);
      this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + 0.3D, this.mc.player.posZ);
    } 
    super.onPreUpdate();
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\combat\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */