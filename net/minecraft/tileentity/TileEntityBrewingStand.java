package net.minecraft.tileentity;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;

public class TileEntityBrewingStand extends TileEntityLockable implements ITickable, ISidedInventory {
  private static final int[] SLOTS_FOR_UP = new int[] { 3 };
  
  private static final int[] SLOTS_FOR_DOWN = new int[] { 0, 1, 2, 3 };
  
  private static final int[] OUTPUT_SLOTS = new int[] { 0, 1, 2, 4 };
  
  private NonNullList<ItemStack> brewingItemStacks = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
  
  private int brewTime;
  
  private boolean[] filledSlots;
  
  private Item ingredientID;
  
  private String customName;
  
  private int fuel;
  
  public String getName() {
    return hasCustomName() ? this.customName : "container.brewing";
  }
  
  public boolean hasCustomName() {
    return (this.customName != null && !this.customName.isEmpty());
  }
  
  public void setName(String name) {
    this.customName = name;
  }
  
  public int getSizeInventory() {
    return this.brewingItemStacks.size();
  }
  
  public boolean func_191420_l() {
    for (ItemStack itemstack : this.brewingItemStacks) {
      if (!itemstack.func_190926_b())
        return false; 
    } 
    return true;
  }
  
  public void update() {
    ItemStack itemstack = (ItemStack)this.brewingItemStacks.get(4);
    if (this.fuel <= 0 && itemstack.getItem() == Items.BLAZE_POWDER) {
      this.fuel = 20;
      itemstack.func_190918_g(1);
      markDirty();
    } 
    boolean flag = canBrew();
    boolean flag1 = (this.brewTime > 0);
    ItemStack itemstack1 = (ItemStack)this.brewingItemStacks.get(3);
    if (flag1) {
      this.brewTime--;
      boolean flag2 = (this.brewTime == 0);
      if (flag2 && flag) {
        brewPotions();
        markDirty();
      } else if (!flag) {
        this.brewTime = 0;
        markDirty();
      } else if (this.ingredientID != itemstack1.getItem()) {
        this.brewTime = 0;
        markDirty();
      } 
    } else if (flag && this.fuel > 0) {
      this.fuel--;
      this.brewTime = 400;
      this.ingredientID = itemstack1.getItem();
      markDirty();
    } 
    if (!this.world.isRemote) {
      boolean[] aboolean = createFilledSlotsArray();
      if (!Arrays.equals(aboolean, this.filledSlots)) {
        this.filledSlots = aboolean;
        IBlockState iblockstate = this.world.getBlockState(getPos());
        if (!(iblockstate.getBlock() instanceof BlockBrewingStand))
          return; 
        for (int i = 0; i < BlockBrewingStand.HAS_BOTTLE.length; i++)
          iblockstate = iblockstate.withProperty((IProperty)BlockBrewingStand.HAS_BOTTLE[i], Boolean.valueOf(aboolean[i])); 
        this.world.setBlockState(this.pos, iblockstate, 2);
      } 
    } 
  }
  
  public boolean[] createFilledSlotsArray() {
    boolean[] aboolean = new boolean[3];
    for (int i = 0; i < 3; i++) {
      if (!((ItemStack)this.brewingItemStacks.get(i)).func_190926_b())
        aboolean[i] = true; 
    } 
    return aboolean;
  }
  
  private boolean canBrew() {
    ItemStack itemstack = (ItemStack)this.brewingItemStacks.get(3);
    if (itemstack.func_190926_b())
      return false; 
    if (!PotionHelper.isReagent(itemstack))
      return false; 
    for (int i = 0; i < 3; i++) {
      ItemStack itemstack1 = (ItemStack)this.brewingItemStacks.get(i);
      if (!itemstack1.func_190926_b() && PotionHelper.hasConversions(itemstack1, itemstack))
        return true; 
    } 
    return false;
  }
  
  private void brewPotions() {
    ItemStack itemstack = (ItemStack)this.brewingItemStacks.get(3);
    for (int i = 0; i < 3; i++)
      this.brewingItemStacks.set(i, PotionHelper.doReaction(itemstack, (ItemStack)this.brewingItemStacks.get(i))); 
    itemstack.func_190918_g(1);
    BlockPos blockpos = getPos();
    if (itemstack.getItem().hasContainerItem()) {
      ItemStack itemstack1 = new ItemStack(itemstack.getItem().getContainerItem());
      if (itemstack.func_190926_b()) {
        itemstack = itemstack1;
      } else {
        InventoryHelper.spawnItemStack(this.world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), itemstack1);
      } 
    } 
    this.brewingItemStacks.set(3, itemstack);
    this.world.playEvent(1035, blockpos, 0);
  }
  
  public static void registerFixesBrewingStand(DataFixer fixer) {
    fixer.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker)new ItemStackDataLists(TileEntityBrewingStand.class, new String[] { "Items" }));
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.brewingItemStacks = NonNullList.func_191197_a(getSizeInventory(), ItemStack.field_190927_a);
    ItemStackHelper.func_191283_b(compound, this.brewingItemStacks);
    this.brewTime = compound.getShort("BrewTime");
    if (compound.hasKey("CustomName", 8))
      this.customName = compound.getString("CustomName"); 
    this.fuel = compound.getByte("Fuel");
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setShort("BrewTime", (short)this.brewTime);
    ItemStackHelper.func_191282_a(compound, this.brewingItemStacks);
    if (hasCustomName())
      compound.setString("CustomName", this.customName); 
    compound.setByte("Fuel", (byte)this.fuel);
    return compound;
  }
  
  public ItemStack getStackInSlot(int index) {
    return (index >= 0 && index < this.brewingItemStacks.size()) ? (ItemStack)this.brewingItemStacks.get(index) : ItemStack.field_190927_a;
  }
  
  public ItemStack decrStackSize(int index, int count) {
    return ItemStackHelper.getAndSplit((List)this.brewingItemStacks, index, count);
  }
  
  public ItemStack removeStackFromSlot(int index) {
    return ItemStackHelper.getAndRemove((List)this.brewingItemStacks, index);
  }
  
  public void setInventorySlotContents(int index, ItemStack stack) {
    if (index >= 0 && index < this.brewingItemStacks.size())
      this.brewingItemStacks.set(index, stack); 
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public boolean isUsableByPlayer(EntityPlayer player) {
    if (this.world.getTileEntity(this.pos) != this)
      return false; 
    return (player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D);
  }
  
  public void openInventory(EntityPlayer player) {}
  
  public void closeInventory(EntityPlayer player) {}
  
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index == 3)
      return PotionHelper.isReagent(stack); 
    Item item = stack.getItem();
    if (index == 4)
      return (item == Items.BLAZE_POWDER); 
    return ((item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE) && getStackInSlot(index).func_190926_b());
  }
  
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP)
      return SLOTS_FOR_UP; 
    return (side == EnumFacing.DOWN) ? SLOTS_FOR_DOWN : OUTPUT_SLOTS;
  }
  
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return isItemValidForSlot(index, itemStackIn);
  }
  
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    if (index == 3)
      return (stack.getItem() == Items.GLASS_BOTTLE); 
    return true;
  }
  
  public String getGuiID() {
    return "minecraft:brewing_stand";
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    return (Container)new ContainerBrewingStand(playerInventory, (IInventory)this);
  }
  
  public int getField(int id) {
    switch (id) {
      case 0:
        return this.brewTime;
      case 1:
        return this.fuel;
    } 
    return 0;
  }
  
  public void setField(int id, int value) {
    switch (id) {
      case 0:
        this.brewTime = value;
        break;
      case 1:
        this.fuel = value;
        break;
    } 
  }
  
  public int getFieldCount() {
    return 2;
  }
  
  public void clear() {
    this.brewingItemStacks.clear();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityBrewingStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */