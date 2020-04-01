package org.seltak.anubis.modules.player;

import de.Hero.settings.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.utils.Timer;

public class AutoArmor extends Module {
  Timer timer = new Timer();
  
  ItemArmor helmet;
  
  ItemArmor chestplate;
  
  ItemArmor leggings;
  
  ItemArmor boots;
  
  int[] armorSlotID = new int[4];
  
  int[] armorSlotIDBackup = new int[4];
  
  public AutoArmor() {
    super("AutoArmor", Category.PLAYER, 0);
  }
  
  public void setup() {
    Anubis.setmgr.rSetting(new Setting("AutoArmor_Instant", this, false));
    Anubis.setmgr.rSetting(new Setting("AutoArmor_OnlyInInv", this, true));
    Anubis.setmgr.rSetting(new Setting("AutoArmor_Delay", this, 60.0D, 10.0D, 1000.0D, true));
  }
  
  public void onPreUpdate() {
    if (this.mc.currentScreen instanceof GuiInventory) {
      searchBestArmor();
      useArmor();
    } 
  }
  
  public void openInventory() {
    if (!(this.mc.currentScreen instanceof GuiInventory) && this.armorSlotIDBackup != this.armorSlotID)
      this.mc.displayGuiScreen((GuiScreen)new GuiInventory((EntityPlayer)this.mc.player)); 
  }
  
  private int[] searchBestArmor() {
    if (this.mc.currentScreen instanceof GuiInventory)
      for (int i = 0; i < 36; i++) {
        if (this.mc.player.inventory.getStackInSlot(i).getItem() != null) {
          Item item = this.mc.player.inventory.getStackInSlot(i).getItem();
          if (item instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)item;
            if (armor.armorType == EntityEquipmentSlot.HEAD) {
              if (this.helmet == null || armor.getMaxDamage() > this.helmet.getMaxDamage() || (armor.getMaxDamage() >= this.helmet.getMaxDamage() && i != this.armorSlotID[0]) || this.mc.player.inventoryContainer.getSlot(5).getStack().getItem().getUnlocalizedName().equals("tile.air")) {
                this.helmet = armor;
                this.armorSlotID[0] = i;
                if (this.armorSlotID[0] < 9)
                  this.armorSlotID[0] = this.armorSlotID[0] + 36; 
              } 
            } else if (armor.armorType == EntityEquipmentSlot.CHEST) {
              if (this.chestplate == null || armor.getMaxDamage() > this.chestplate.getMaxDamage() || (armor.getMaxDamage() >= this.chestplate.getMaxDamage() && i != this.armorSlotID[1]) || this.mc.player.inventoryContainer.getSlot(6).getStack().getItem().getUnlocalizedName().equals("tile.air")) {
                this.chestplate = armor;
                this.armorSlotID[1] = i;
                if (this.armorSlotID[1] < 9)
                  this.armorSlotID[1] = this.armorSlotID[1] + 36; 
              } 
            } else if (armor.armorType == EntityEquipmentSlot.LEGS) {
              if (this.leggings == null || armor.getMaxDamage() > this.leggings.getMaxDamage() || (armor.getMaxDamage() >= this.leggings.getMaxDamage() && i != this.armorSlotID[2]) || this.mc.player.inventoryContainer.getSlot(7).getStack().getItem().getUnlocalizedName().equals("tile.air") || armor.getMaxDamage() >= this.leggings.getMaxDamage()) {
                this.leggings = armor;
                this.armorSlotID[2] = i;
                if (this.armorSlotID[2] < 9)
                  this.armorSlotID[2] = this.armorSlotID[2] + 36; 
              } 
            } else if (armor.armorType == EntityEquipmentSlot.FEET && (
              this.boots == null || armor.getMaxDamage() > this.boots.getMaxDamage() || (armor.getMaxDamage() >= this.boots.getMaxDamage() && i != this.armorSlotID[3]) || this.mc.player.inventoryContainer.getSlot(8).getStack().getItem().getUnlocalizedName().equals("tile.air"))) {
              this.boots = armor;
              this.armorSlotID[3] = i;
              if (this.armorSlotID[3] < 9)
                this.armorSlotID[3] = this.armorSlotID[3] + 36; 
            } 
          } 
        } 
      }  
    return this.armorSlotID;
  }
  
  private void useArmor() {
    if (this.mc.player.inventoryContainer.getSlot(this.armorSlotID[0]).getStack().getItem() != null && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[0]).getStack().getItem() instanceof ItemArmor && Timer.delayOver((int)Anubis.setmgr.getSettingByName("AutoArmor_Delay").getValDouble()) && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[0]).getStack().getItem() == this.helmet) {
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, this.armorSlotID[0], 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, 5, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      Timer.reset();
    } 
    if (this.mc.player.inventoryContainer.getSlot(this.armorSlotID[1]).getStack().getItem() != null && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[1]).getStack().getItem() instanceof ItemArmor && Timer.delayOver((int)Anubis.setmgr.getSettingByName("AutoArmor_Delay").getValDouble()) && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[1]).getStack().getItem() == this.chestplate) {
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, this.armorSlotID[1], 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      Timer.reset();
    } 
    if (this.mc.player.inventoryContainer.getSlot(this.armorSlotID[2]).getStack().getItem() != null && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[2]).getStack().getItem() instanceof ItemArmor && Timer.delayOver((int)Anubis.setmgr.getSettingByName("AutoArmor_Delay").getValDouble()) && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[2]).getStack().getItem() == this.leggings) {
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, this.armorSlotID[2], 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, 7, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      Timer.reset();
    } 
    if (this.mc.player.inventoryContainer.getSlot(this.armorSlotID[3]).getStack().getItem() != null && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[3]).getStack().getItem() instanceof ItemArmor && Timer.delayOver((int)Anubis.setmgr.getSettingByName("AutoArmor_Delay").getValDouble()) && this.mc.player.inventoryContainer.getSlot(this.armorSlotID[3]).getStack().getItem() == this.boots) {
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, this.armorSlotID[3], 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, 8, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
      Timer.reset();
    } 
    this.armorSlotIDBackup = this.armorSlotID;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\player\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */