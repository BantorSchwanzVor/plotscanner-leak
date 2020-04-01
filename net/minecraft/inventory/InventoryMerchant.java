package net.minecraft.inventory;

import java.util.List;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
  private final IMerchant theMerchant;
  
  private final NonNullList<ItemStack> theInventory = NonNullList.func_191197_a(3, ItemStack.field_190927_a);
  
  private final EntityPlayer thePlayer;
  
  private MerchantRecipe currentRecipe;
  
  private int currentRecipeIndex;
  
  public InventoryMerchant(EntityPlayer thePlayerIn, IMerchant theMerchantIn) {
    this.thePlayer = thePlayerIn;
    this.theMerchant = theMerchantIn;
  }
  
  public int getSizeInventory() {
    return this.theInventory.size();
  }
  
  public boolean func_191420_l() {
    for (ItemStack itemstack : this.theInventory) {
      if (!itemstack.func_190926_b())
        return false; 
    } 
    return true;
  }
  
  public ItemStack getStackInSlot(int index) {
    return (ItemStack)this.theInventory.get(index);
  }
  
  public ItemStack decrStackSize(int index, int count) {
    ItemStack itemstack = (ItemStack)this.theInventory.get(index);
    if (index == 2 && !itemstack.func_190926_b())
      return ItemStackHelper.getAndSplit((List<ItemStack>)this.theInventory, index, itemstack.func_190916_E()); 
    ItemStack itemstack1 = ItemStackHelper.getAndSplit((List<ItemStack>)this.theInventory, index, count);
    if (!itemstack1.func_190926_b() && inventoryResetNeededOnSlotChange(index))
      resetRecipeAndSlots(); 
    return itemstack1;
  }
  
  private boolean inventoryResetNeededOnSlotChange(int slotIn) {
    return !(slotIn != 0 && slotIn != 1);
  }
  
  public ItemStack removeStackFromSlot(int index) {
    return ItemStackHelper.getAndRemove((List<ItemStack>)this.theInventory, index);
  }
  
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.theInventory.set(index, stack);
    if (!stack.func_190926_b() && stack.func_190916_E() > getInventoryStackLimit())
      stack.func_190920_e(getInventoryStackLimit()); 
    if (inventoryResetNeededOnSlotChange(index))
      resetRecipeAndSlots(); 
  }
  
  public String getName() {
    return "mob.villager";
  }
  
  public boolean hasCustomName() {
    return false;
  }
  
  public ITextComponent getDisplayName() {
    return hasCustomName() ? (ITextComponent)new TextComponentString(getName()) : (ITextComponent)new TextComponentTranslation(getName(), new Object[0]);
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUsableByPlayer(EntityPlayer player) {
    return (this.theMerchant.getCustomer() == player);
  }
  
  public void openInventory(EntityPlayer player) {}
  
  public void closeInventory(EntityPlayer player) {}
  
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }
  
  public void markDirty() {
    resetRecipeAndSlots();
  }
  
  public void resetRecipeAndSlots() {
    this.currentRecipe = null;
    ItemStack itemstack = (ItemStack)this.theInventory.get(0);
    ItemStack itemstack1 = (ItemStack)this.theInventory.get(1);
    if (itemstack.func_190926_b()) {
      itemstack = itemstack1;
      itemstack1 = ItemStack.field_190927_a;
    } 
    if (itemstack.func_190926_b()) {
      setInventorySlotContents(2, ItemStack.field_190927_a);
    } else {
      MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
      if (merchantrecipelist != null) {
        MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack1, this.currentRecipeIndex);
        if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
          this.currentRecipe = merchantrecipe;
          setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
        } else if (!itemstack1.func_190926_b()) {
          merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack1, itemstack, this.currentRecipeIndex);
          if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
            this.currentRecipe = merchantrecipe;
            setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
          } else {
            setInventorySlotContents(2, ItemStack.field_190927_a);
          } 
        } else {
          setInventorySlotContents(2, ItemStack.field_190927_a);
        } 
      } 
      this.theMerchant.verifySellingItem(getStackInSlot(2));
    } 
  }
  
  public MerchantRecipe getCurrentRecipe() {
    return this.currentRecipe;
  }
  
  public void setCurrentRecipeIndex(int currentRecipeIndexIn) {
    this.currentRecipeIndex = currentRecipeIndexIn;
    resetRecipeAndSlots();
  }
  
  public int getField(int id) {
    return 0;
  }
  
  public void setField(int id, int value) {}
  
  public int getFieldCount() {
    return 0;
  }
  
  public void clear() {
    this.theInventory.clear();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\InventoryMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */