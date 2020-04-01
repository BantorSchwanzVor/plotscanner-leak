package net.minecraft.tileentity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntityLockableLoot implements IHopper, ITickable {
  private NonNullList<ItemStack> inventory = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
  
  private int transferCooldown = -1;
  
  private long field_190578_g;
  
  public static void registerFixesHopper(DataFixer fixer) {
    fixer.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker)new ItemStackDataLists(TileEntityHopper.class, new String[] { "Items" }));
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.inventory = NonNullList.func_191197_a(getSizeInventory(), ItemStack.field_190927_a);
    if (!checkLootAndRead(compound))
      ItemStackHelper.func_191283_b(compound, this.inventory); 
    if (compound.hasKey("CustomName", 8))
      this.field_190577_o = compound.getString("CustomName"); 
    this.transferCooldown = compound.getInteger("TransferCooldown");
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (!checkLootAndWrite(compound))
      ItemStackHelper.func_191282_a(compound, this.inventory); 
    compound.setInteger("TransferCooldown", this.transferCooldown);
    if (hasCustomName())
      compound.setString("CustomName", this.field_190577_o); 
    return compound;
  }
  
  public int getSizeInventory() {
    return this.inventory.size();
  }
  
  public ItemStack decrStackSize(int index, int count) {
    fillWithLoot((EntityPlayer)null);
    ItemStack itemstack = ItemStackHelper.getAndSplit((List)func_190576_q(), index, count);
    return itemstack;
  }
  
  public void setInventorySlotContents(int index, ItemStack stack) {
    fillWithLoot((EntityPlayer)null);
    func_190576_q().set(index, stack);
    if (stack.func_190916_E() > getInventoryStackLimit())
      stack.func_190920_e(getInventoryStackLimit()); 
  }
  
  public String getName() {
    return hasCustomName() ? this.field_190577_o : "container.hopper";
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public void update() {
    if (this.world != null && !this.world.isRemote) {
      this.transferCooldown--;
      this.field_190578_g = this.world.getTotalWorldTime();
      if (!isOnTransferCooldown()) {
        setTransferCooldown(0);
        updateHopper();
      } 
    } 
  }
  
  private boolean updateHopper() {
    if (this.world != null && !this.world.isRemote) {
      if (!isOnTransferCooldown() && BlockHopper.isEnabled(getBlockMetadata())) {
        boolean flag = false;
        if (!isEmpty())
          flag = transferItemsOut(); 
        if (!isFull())
          flag = !(!captureDroppedItems(this) && !flag); 
        if (flag) {
          setTransferCooldown(8);
          markDirty();
          return true;
        } 
      } 
      return false;
    } 
    return false;
  }
  
  private boolean isEmpty() {
    for (ItemStack itemstack : this.inventory) {
      if (!itemstack.func_190926_b())
        return false; 
    } 
    return true;
  }
  
  public boolean func_191420_l() {
    return isEmpty();
  }
  
  private boolean isFull() {
    for (ItemStack itemstack : this.inventory) {
      if (itemstack.func_190926_b() || itemstack.func_190916_E() != itemstack.getMaxStackSize())
        return false; 
    } 
    return true;
  }
  
  private boolean transferItemsOut() {
    IInventory iinventory = getInventoryForHopperTransfer();
    if (iinventory == null)
      return false; 
    EnumFacing enumfacing = BlockHopper.getFacing(getBlockMetadata()).getOpposite();
    if (isInventoryFull(iinventory, enumfacing))
      return false; 
    for (int i = 0; i < getSizeInventory(); i++) {
      if (!getStackInSlot(i).func_190926_b()) {
        ItemStack itemstack = getStackInSlot(i).copy();
        ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, decrStackSize(i, 1), enumfacing);
        if (itemstack1.func_190926_b()) {
          iinventory.markDirty();
          return true;
        } 
        setInventorySlotContents(i, itemstack);
      } 
    } 
    return false;
  }
  
  private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
    if (inventoryIn instanceof ISidedInventory) {
      ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
      int[] aint = isidedinventory.getSlotsForFace(side);
      byte b;
      int i, arrayOfInt1[];
      for (i = (arrayOfInt1 = aint).length, b = 0; b < i; ) {
        int k = arrayOfInt1[b];
        ItemStack itemstack1 = isidedinventory.getStackInSlot(k);
        if (itemstack1.func_190926_b() || itemstack1.func_190916_E() != itemstack1.getMaxStackSize())
          return false; 
        b++;
      } 
    } else {
      int i = inventoryIn.getSizeInventory();
      for (int j = 0; j < i; j++) {
        ItemStack itemstack = inventoryIn.getStackInSlot(j);
        if (itemstack.func_190926_b() || itemstack.func_190916_E() != itemstack.getMaxStackSize())
          return false; 
      } 
    } 
    return true;
  }
  
  private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side) {
    if (inventoryIn instanceof ISidedInventory) {
      ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
      int[] aint = isidedinventory.getSlotsForFace(side);
      byte b;
      int i, arrayOfInt1[];
      for (i = (arrayOfInt1 = aint).length, b = 0; b < i; ) {
        int j = arrayOfInt1[b];
        if (!isidedinventory.getStackInSlot(j).func_190926_b())
          return false; 
        b++;
      } 
    } else {
      int j = inventoryIn.getSizeInventory();
      for (int k = 0; k < j; k++) {
        if (!inventoryIn.getStackInSlot(k).func_190926_b())
          return false; 
      } 
    } 
    return true;
  }
  
  public static boolean captureDroppedItems(IHopper hopper) {
    IInventory iinventory = getHopperInventory(hopper);
    if (iinventory != null) {
      EnumFacing enumfacing = EnumFacing.DOWN;
      if (isInventoryEmpty(iinventory, enumfacing))
        return false; 
      if (iinventory instanceof ISidedInventory) {
        ISidedInventory isidedinventory = (ISidedInventory)iinventory;
        int[] aint = isidedinventory.getSlotsForFace(enumfacing);
        byte b;
        int i, arrayOfInt1[];
        for (i = (arrayOfInt1 = aint).length, b = 0; b < i; ) {
          int j = arrayOfInt1[b];
          if (pullItemFromSlot(hopper, iinventory, j, enumfacing))
            return true; 
          b++;
        } 
      } else {
        int j = iinventory.getSizeInventory();
        for (int k = 0; k < j; k++) {
          if (pullItemFromSlot(hopper, iinventory, k, enumfacing))
            return true; 
        } 
      } 
    } else {
      for (EntityItem entityitem : getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos())) {
        if (putDropInInventoryAllSlots((IInventory)null, hopper, entityitem))
          return true; 
      } 
    } 
    return false;
  }
  
  private static boolean pullItemFromSlot(IHopper hopper, IInventory inventoryIn, int index, EnumFacing direction) {
    ItemStack itemstack = inventoryIn.getStackInSlot(index);
    if (!itemstack.func_190926_b() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
      ItemStack itemstack1 = itemstack.copy();
      ItemStack itemstack2 = putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), (EnumFacing)null);
      if (itemstack2.func_190926_b()) {
        inventoryIn.markDirty();
        return true;
      } 
      inventoryIn.setInventorySlotContents(index, itemstack1);
    } 
    return false;
  }
  
  public static boolean putDropInInventoryAllSlots(IInventory p_145898_0_, IInventory itemIn, EntityItem p_145898_2_) {
    boolean flag = false;
    if (p_145898_2_ == null)
      return false; 
    ItemStack itemstack = p_145898_2_.getEntityItem().copy();
    ItemStack itemstack1 = putStackInInventoryAllSlots(p_145898_0_, itemIn, itemstack, (EnumFacing)null);
    if (itemstack1.func_190926_b()) {
      flag = true;
      p_145898_2_.setDead();
    } else {
      p_145898_2_.setEntityItemStack(itemstack1);
    } 
    return flag;
  }
  
  public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, IInventory stack, ItemStack side, @Nullable EnumFacing p_174918_3_) {
    if (stack instanceof ISidedInventory && p_174918_3_ != null) {
      ISidedInventory isidedinventory = (ISidedInventory)stack;
      int[] aint = isidedinventory.getSlotsForFace(p_174918_3_);
      for (int k = 0; k < aint.length && !side.func_190926_b(); k++)
        side = insertStack(inventoryIn, stack, side, aint[k], p_174918_3_); 
    } else {
      int i = stack.getSizeInventory();
      for (int j = 0; j < i && !side.func_190926_b(); j++)
        side = insertStack(inventoryIn, stack, side, j, p_174918_3_); 
    } 
    return side;
  }
  
  private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
    if (!inventoryIn.isItemValidForSlot(index, stack))
      return false; 
    return !(inventoryIn instanceof ISidedInventory && !((ISidedInventory)inventoryIn).canInsertItem(index, stack, side));
  }
  
  private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
    return !(inventoryIn instanceof ISidedInventory && !((ISidedInventory)inventoryIn).canExtractItem(index, stack, side));
  }
  
  private static ItemStack insertStack(IInventory inventoryIn, IInventory stack, ItemStack index, int side, EnumFacing p_174916_4_) {
    ItemStack itemstack = stack.getStackInSlot(side);
    if (canInsertItemInSlot(stack, index, side, p_174916_4_)) {
      boolean flag = false;
      boolean flag1 = stack.func_191420_l();
      if (itemstack.func_190926_b()) {
        stack.setInventorySlotContents(side, index);
        index = ItemStack.field_190927_a;
        flag = true;
      } else if (canCombine(itemstack, index)) {
        int i = index.getMaxStackSize() - itemstack.func_190916_E();
        int j = Math.min(index.func_190916_E(), i);
        index.func_190918_g(j);
        itemstack.func_190917_f(j);
        flag = (j > 0);
      } 
      if (flag) {
        if (flag1 && stack instanceof TileEntityHopper) {
          TileEntityHopper tileentityhopper1 = (TileEntityHopper)stack;
          if (!tileentityhopper1.mayTransfer()) {
            int k = 0;
            if (inventoryIn != null && inventoryIn instanceof TileEntityHopper) {
              TileEntityHopper tileentityhopper = (TileEntityHopper)inventoryIn;
              if (tileentityhopper1.field_190578_g >= tileentityhopper.field_190578_g)
                k = 1; 
            } 
            tileentityhopper1.setTransferCooldown(8 - k);
          } 
        } 
        stack.markDirty();
      } 
    } 
    return index;
  }
  
  private IInventory getInventoryForHopperTransfer() {
    EnumFacing enumfacing = BlockHopper.getFacing(getBlockMetadata());
    return getInventoryAtPosition(getWorld(), getXPos() + enumfacing.getFrontOffsetX(), getYPos() + enumfacing.getFrontOffsetY(), getZPos() + enumfacing.getFrontOffsetZ());
  }
  
  public static IInventory getHopperInventory(IHopper hopper) {
    return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
  }
  
  public static List<EntityItem> getCaptureItems(World worldIn, double p_184292_1_, double p_184292_3_, double p_184292_5_) {
    return worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p_184292_1_ - 0.5D, p_184292_3_, p_184292_5_ - 0.5D, p_184292_1_ + 0.5D, p_184292_3_ + 1.5D, p_184292_5_ + 0.5D), EntitySelectors.IS_ALIVE);
  }
  
  public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
    ILockableContainer iLockableContainer;
    IInventory iInventory1, iinventory = null;
    int i = MathHelper.floor(x);
    int j = MathHelper.floor(y);
    int k = MathHelper.floor(z);
    BlockPos blockpos = new BlockPos(i, j, k);
    Block block = worldIn.getBlockState(blockpos).getBlock();
    if (block.hasTileEntity()) {
      TileEntity tileentity = worldIn.getTileEntity(blockpos);
      if (tileentity instanceof IInventory) {
        iinventory = (IInventory)tileentity;
        if (iinventory instanceof TileEntityChest && block instanceof BlockChest)
          iLockableContainer = ((BlockChest)block).getContainer(worldIn, blockpos, true); 
      } 
    } 
    if (iLockableContainer == null) {
      List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelectors.HAS_INVENTORY);
      if (!list.isEmpty())
        iInventory1 = (IInventory)list.get(worldIn.rand.nextInt(list.size())); 
    } 
    return iInventory1;
  }
  
  private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
    if (stack1.getItem() != stack2.getItem())
      return false; 
    if (stack1.getMetadata() != stack2.getMetadata())
      return false; 
    if (stack1.func_190916_E() > stack1.getMaxStackSize())
      return false; 
    return ItemStack.areItemStackTagsEqual(stack1, stack2);
  }
  
  public double getXPos() {
    return this.pos.getX() + 0.5D;
  }
  
  public double getYPos() {
    return this.pos.getY() + 0.5D;
  }
  
  public double getZPos() {
    return this.pos.getZ() + 0.5D;
  }
  
  private void setTransferCooldown(int ticks) {
    this.transferCooldown = ticks;
  }
  
  private boolean isOnTransferCooldown() {
    return (this.transferCooldown > 0);
  }
  
  private boolean mayTransfer() {
    return (this.transferCooldown > 8);
  }
  
  public String getGuiID() {
    return "minecraft:hopper";
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    fillWithLoot(playerIn);
    return (Container)new ContainerHopper(playerInventory, this, playerIn);
  }
  
  protected NonNullList<ItemStack> func_190576_q() {
    return this.inventory;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */