package net.minecraft.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;

public class SlotFurnaceOutput extends Slot {
  private final EntityPlayer thePlayer;
  
  private int removeCount;
  
  public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
    super(inventoryIn, slotIndex, xPosition, yPosition);
    this.thePlayer = player;
  }
  
  public boolean isItemValid(ItemStack stack) {
    return false;
  }
  
  public ItemStack decrStackSize(int amount) {
    if (getHasStack())
      this.removeCount += Math.min(amount, getStack().func_190916_E()); 
    return super.decrStackSize(amount);
  }
  
  public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_) {
    onCrafting(p_190901_2_);
    super.func_190901_a(p_190901_1_, p_190901_2_);
    return p_190901_2_;
  }
  
  protected void onCrafting(ItemStack stack, int amount) {
    this.removeCount += amount;
    onCrafting(stack);
  }
  
  protected void onCrafting(ItemStack stack) {
    stack.onCrafting(this.thePlayer.world, this.thePlayer, this.removeCount);
    if (!this.thePlayer.world.isRemote) {
      int i = this.removeCount;
      float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
      if (f == 0.0F) {
        i = 0;
      } else if (f < 1.0F) {
        int j = MathHelper.floor(i * f);
        if (j < MathHelper.ceil(i * f) && Math.random() < (i * f - j))
          j++; 
        i = j;
      } 
      while (i > 0) {
        int k = EntityXPOrb.getXPSplit(i);
        i -= k;
        this.thePlayer.world.spawnEntityInWorld((Entity)new EntityXPOrb(this.thePlayer.world, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, k));
      } 
    } 
    this.removeCount = 0;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\SlotFurnaceOutput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */