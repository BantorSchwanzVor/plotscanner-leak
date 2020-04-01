package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDispenser extends TileEntityLockableLoot {
  private static final Random RNG = new Random();
  
  private NonNullList<ItemStack> stacks = NonNullList.func_191197_a(9, ItemStack.field_190927_a);
  
  public int getSizeInventory() {
    return 9;
  }
  
  public boolean func_191420_l() {
    for (ItemStack itemstack : this.stacks) {
      if (!itemstack.func_190926_b())
        return false; 
    } 
    return true;
  }
  
  public int getDispenseSlot() {
    fillWithLoot((EntityPlayer)null);
    int i = -1;
    int j = 1;
    for (int k = 0; k < this.stacks.size(); k++) {
      if (!((ItemStack)this.stacks.get(k)).func_190926_b() && RNG.nextInt(j++) == 0)
        i = k; 
    } 
    return i;
  }
  
  public int addItemStack(ItemStack stack) {
    for (int i = 0; i < this.stacks.size(); i++) {
      if (((ItemStack)this.stacks.get(i)).func_190926_b()) {
        setInventorySlotContents(i, stack);
        return i;
      } 
    } 
    return -1;
  }
  
  public String getName() {
    return hasCustomName() ? this.field_190577_o : "container.dispenser";
  }
  
  public static void registerFixes(DataFixer fixer) {
    fixer.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker)new ItemStackDataLists(TileEntityDispenser.class, new String[] { "Items" }));
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.stacks = NonNullList.func_191197_a(getSizeInventory(), ItemStack.field_190927_a);
    if (!checkLootAndRead(compound))
      ItemStackHelper.func_191283_b(compound, this.stacks); 
    if (compound.hasKey("CustomName", 8))
      this.field_190577_o = compound.getString("CustomName"); 
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (!checkLootAndWrite(compound))
      ItemStackHelper.func_191282_a(compound, this.stacks); 
    if (hasCustomName())
      compound.setString("CustomName", this.field_190577_o); 
    return compound;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public String getGuiID() {
    return "minecraft:dispenser";
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    fillWithLoot(playerIn);
    return (Container)new ContainerDispenser((IInventory)playerInventory, (IInventory)this);
  }
  
  protected NonNullList<ItemStack> func_190576_q() {
    return this.stacks;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */