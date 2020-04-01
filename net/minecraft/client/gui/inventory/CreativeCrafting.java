package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CreativeCrafting implements IContainerListener {
  private final Minecraft mc;
  
  public CreativeCrafting(Minecraft mc) {
    this.mc = mc;
  }
  
  public void updateCraftingInventory(Container containerToSend, NonNullList<ItemStack> itemsList) {}
  
  public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
    this.mc.playerController.sendSlotPacket(stack, slotInd);
  }
  
  public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {}
  
  public void sendAllWindowProperties(Container containerIn, IInventory inventory) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\inventory\CreativeCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */