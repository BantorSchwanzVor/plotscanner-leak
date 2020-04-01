package org.seltak.anubis.modules.movement;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class Fly extends Module {
  private int state = 0;
  
  public Fly() {
    super("Fly", Category.MOVEMENT, 0);
  }
  
  public void setup() {
    ArrayList<String> options = new ArrayList<>();
    options.add("Vanilla");
    options.add("Hypixel");
    Anubis.setmgr.rSetting(new Setting("Fly Mode", this, "Hypixel", options));
    super.setup();
  }
  
  public void onPreUpdate() {
    String mode = Anubis.setmgr.getSettingByName("Fly Mode").getValString();
    if (mode.equalsIgnoreCase("Hypixel")) {
      this.mc.player.motionY = 0.0D;
      if (this.mc.player.ticksExisted % 3 == 0) {
        double y = this.mc.player.posY - 1.0E-10D;
        this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, y, this.mc.player.posZ, true));
      } 
      double y1 = this.mc.player.posY + 1.0E-10D;
      this.mc.player.setPosition(this.mc.player.posX, y1, this.mc.player.posZ);
    } 
    if (mode.equalsIgnoreCase("Vanilla"))
      this.mc.player.capabilities.isFlying = true; 
    super.onPreUpdate();
  }
  
  public void onDisable() {
    String mode = Anubis.setmgr.getSettingByName("Fly Mode").getValString();
    if (mode.equalsIgnoreCase("Vanilla"))
      this.mc.player.capabilities.isFlying = false; 
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\Fly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */