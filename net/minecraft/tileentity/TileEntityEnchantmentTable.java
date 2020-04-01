package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject {
  public int tickCount;
  
  public float pageFlip;
  
  public float pageFlipPrev;
  
  public float flipT;
  
  public float flipA;
  
  public float bookSpread;
  
  public float bookSpreadPrev;
  
  public float bookRotation;
  
  public float bookRotationPrev;
  
  public float tRot;
  
  private static final Random rand = new Random();
  
  private String customName;
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (hasCustomName())
      compound.setString("CustomName", this.customName); 
    return compound;
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey("CustomName", 8))
      this.customName = compound.getString("CustomName"); 
  }
  
  public void update() {
    this.bookSpreadPrev = this.bookSpread;
    this.bookRotationPrev = this.bookRotation;
    EntityPlayer entityplayer = this.world.getClosestPlayer((this.pos.getX() + 0.5F), (this.pos.getY() + 0.5F), (this.pos.getZ() + 0.5F), 3.0D, false);
    if (entityplayer != null) {
      double d0 = entityplayer.posX - (this.pos.getX() + 0.5F);
      double d1 = entityplayer.posZ - (this.pos.getZ() + 0.5F);
      this.tRot = (float)MathHelper.atan2(d1, d0);
      this.bookSpread += 0.1F;
      if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
        float f1 = this.flipT;
        do {
          this.flipT += (rand.nextInt(4) - rand.nextInt(4));
        } while (f1 == this.flipT);
      } 
    } else {
      this.tRot += 0.02F;
      this.bookSpread -= 0.1F;
    } 
    while (this.bookRotation >= 3.1415927F)
      this.bookRotation -= 6.2831855F; 
    while (this.bookRotation < -3.1415927F)
      this.bookRotation += 6.2831855F; 
    while (this.tRot >= 3.1415927F)
      this.tRot -= 6.2831855F; 
    while (this.tRot < -3.1415927F)
      this.tRot += 6.2831855F; 
    float f2;
    for (f2 = this.tRot - this.bookRotation; f2 >= 3.1415927F; f2 -= 6.2831855F);
    while (f2 < -3.1415927F)
      f2 += 6.2831855F; 
    this.bookRotation += f2 * 0.4F;
    this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
    this.tickCount++;
    this.pageFlipPrev = this.pageFlip;
    float f = (this.flipT - this.pageFlip) * 0.4F;
    float f3 = 0.2F;
    f = MathHelper.clamp(f, -0.2F, 0.2F);
    this.flipA += (f - this.flipA) * 0.9F;
    this.pageFlip += this.flipA;
  }
  
  public String getName() {
    return hasCustomName() ? this.customName : "container.enchant";
  }
  
  public boolean hasCustomName() {
    return (this.customName != null && !this.customName.isEmpty());
  }
  
  public void setCustomName(String customNameIn) {
    this.customName = customNameIn;
  }
  
  public ITextComponent getDisplayName() {
    return hasCustomName() ? (ITextComponent)new TextComponentString(getName()) : (ITextComponent)new TextComponentTranslation(getName(), new Object[0]);
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    return (Container)new ContainerEnchantment(playerInventory, this.world, this.pos);
  }
  
  public String getGuiID() {
    return "minecraft:enchanting_table";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityEnchantmentTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */