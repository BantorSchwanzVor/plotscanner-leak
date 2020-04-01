package org.seltak.anubis.modules.gui;

import net.minecraft.entity.player.EntityPlayer;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class ShowClosestPlayer extends Module {
  public ShowClosestPlayer() {
    super("ClosestPlayer", Category.GUI, 0);
  }
  
  public void onGui() {
    this.mc.fontRendererObj.drawStringWithShadow("§6[§eAnubis§6] §aClosest Player: §c" + getClosestPlayerToPlayer(1000.0F), Anubis.xGUI, Anubis.yGUI, -1);
    super.onGui();
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  public String getClosestPlayerToPlayer(float distance) {
    EntityPlayer player = this.mc.world.getClosestPlayerExceptYou(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, distance, false);
    if (player != null)
      return player.getName(); 
    return "";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\gui\ShowClosestPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */