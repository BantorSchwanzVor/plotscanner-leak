package org.seltak.anubis.modules.player;

import de.Hero.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class BCrasher extends Module {
  int x = 0;
  
  public BCrasher() {
    super("BCrasher", Category.PLAYER, 0);
  }
  
  public void setup() {
    Anubis.setmgr.rSetting(new Setting("CPT", this, 5.0D, 0.0D, 20.0D, true));
    Anubis.setmgr.rSetting(new Setting("Slot", this, 45.0D, 0.0D, 100.0D, true));
    Anubis.setmgr.rSetting(new Setting("CWAir", this, true));
    Anubis.setmgr.rSetting(new Setting("Ignore Item", this, true));
    Anubis.setmgr.rSetting(new Setting("Compare I", this, 100.0D, 0.0D, 100.0D, true));
    Anubis.setmgr.rSetting(new Setting("Compare II", this, 59.0D, 0.0D, 100.0D, true));
    Anubis.setmgr.rSetting(new Setting("Compare III", this, 0.0D, 0.0D, 100.0D, true));
    Anubis.setmgr.rSetting(new Setting("Compare IV", this, 0.0D, 0.0D, 100.0D, true));
    Anubis.setmgr.rSetting(new Setting("Compare V", this, 0.0D, 0.0D, 100.0D, true));
  }
  
  public void onPreUpdate() {
    int compare = (int)Anubis.setmgr.getSettingByName("Compare I").getValDouble() + (int)Anubis.setmgr.getSettingByName("Compare II").getValDouble() + (int)Anubis.setmgr.getSettingByName("Compare III").getValDouble() + (int)Anubis.setmgr.getSettingByName("Compare IV").getValDouble() + (int)Anubis.setmgr.getSettingByName("Compare V").getValDouble();
    for (int i = 0; i < (int)Anubis.setmgr.getSettingByName("CPT").getValDouble(); i++) {
      if (this.mc.player.openContainer != null && this.mc.player.openContainer.getSlot((int)Anubis.setmgr.getSettingByName("Slot").getValDouble()).getStack().getItem() != null && (
        Anubis.setmgr.getSettingByName("Ignore Item").getValBoolean() || this.mc.player.openContainer.getSlot((int)Anubis.setmgr.getSettingByName("Slot").getValDouble()).getStack().getItem().equals(Item.getItemById(compare)) || (Anubis.setmgr.getSettingByName("CWAir").getValBoolean() && this.mc.player.openContainer.getSlot((int)Anubis.setmgr.getSettingByName("Slot").getValDouble()).getStack().getItem().getUnlocalizedName().equals("tile.air"))))
        this.mc.playerController.windowClick(this.mc.player.openContainer.windowId, (int)Anubis.setmgr.getSettingByName("Slot").getValDouble(), 0, ClickType.PICKUP, (EntityPlayer)this.mc.player); 
    } 
    super.onPreUpdate();
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\player\BCrasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */