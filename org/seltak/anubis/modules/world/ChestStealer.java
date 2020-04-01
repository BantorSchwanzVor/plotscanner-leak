package org.seltak.anubis.modules.world;

import de.Hero.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.utils.Timer;

public class ChestStealer extends Module {
  Timer timer = new Timer();
  
  int i = 0;
  
  public ChestStealer() {
    super("ChestStealer", Category.WORLD, 0);
  }
  
  public void setup() {
    Anubis.setmgr.rSetting(new Setting("Delay", this, true));
    Anubis.setmgr.rSetting(new Setting("Milliseconds", this, 50.0D, 0.0D, 2000.0D, true));
  }
  
  public void onPreUpdate() {
    if (this.mc.player.openContainer != null && this.mc.player.openContainer instanceof ContainerChest) {
      ContainerChest chest = (ContainerChest)this.mc.player.openContainer;
      if (this.i < chest.getLowerChestInventory().getSizeInventory()) {
        if ((chest.getLowerChestInventory().getStackInSlot(this.i) != null && Timer.delayOver((int)Anubis.setmgr.getSettingByName("Milliseconds").getValDouble())) || (chest.getLowerChestInventory().getStackInSlot(this.i) != null && !Anubis.setmgr.getSettingByName("Delay").getValBoolean())) {
          this.mc.playerController.windowClick(chest.windowId, this.i, 0, ClickType.QUICK_MOVE, (EntityPlayer)this.mc.player);
          this.i++;
          Timer.reset();
        } else if (chest.getLowerChestInventory().getStackInSlot(this.i).getItem().getUnlocalizedName().equals("tile.air")) {
          this.i++;
          Timer.reset();
        } 
        chest.getInventory().isEmpty();
      } 
    } else {
      this.i = 0;
    } 
    super.onPreUpdate();
  }
  
  public void onDisable() {
    super.onDisable();
    this.mc.gameSettings.gammaSetting = 1.0F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\world\ChestStealer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */