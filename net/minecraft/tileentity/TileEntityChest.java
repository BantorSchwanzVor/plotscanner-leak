package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityChest extends TileEntityLockableLoot implements ITickable {
  private NonNullList<ItemStack> chestContents = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
  
  public boolean adjacentChestChecked;
  
  public TileEntityChest adjacentChestZNeg;
  
  public TileEntityChest adjacentChestXPos;
  
  public TileEntityChest adjacentChestXNeg;
  
  public TileEntityChest adjacentChestZPos;
  
  public float lidAngle;
  
  public float prevLidAngle;
  
  public int numPlayersUsing;
  
  private int ticksSinceSync;
  
  private BlockChest.Type cachedChestType;
  
  public TileEntityChest(BlockChest.Type typeIn) {
    this.cachedChestType = typeIn;
  }
  
  public int getSizeInventory() {
    return 27;
  }
  
  public boolean func_191420_l() {
    for (ItemStack itemstack : this.chestContents) {
      if (!itemstack.func_190926_b())
        return false; 
    } 
    return true;
  }
  
  public String getName() {
    return hasCustomName() ? this.field_190577_o : "container.chest";
  }
  
  public static void registerFixesChest(DataFixer fixer) {
    fixer.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker)new ItemStackDataLists(TileEntityChest.class, new String[] { "Items" }));
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.chestContents = NonNullList.func_191197_a(getSizeInventory(), ItemStack.field_190927_a);
    if (!checkLootAndRead(compound))
      ItemStackHelper.func_191283_b(compound, this.chestContents); 
    if (compound.hasKey("CustomName", 8))
      this.field_190577_o = compound.getString("CustomName"); 
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (!checkLootAndWrite(compound))
      ItemStackHelper.func_191282_a(compound, this.chestContents); 
    if (hasCustomName())
      compound.setString("CustomName", this.field_190577_o); 
    return compound;
  }
  
  public int getInventoryStackLimit() {
    return 64;
  }
  
  public void updateContainingBlockInfo() {
    super.updateContainingBlockInfo();
    this.adjacentChestChecked = false;
  }
  
  private void setNeighbor(TileEntityChest chestTe, EnumFacing side) {
    if (chestTe.isInvalid()) {
      this.adjacentChestChecked = false;
    } else if (this.adjacentChestChecked) {
      switch (side) {
        case NORTH:
          if (this.adjacentChestZNeg != chestTe)
            this.adjacentChestChecked = false; 
          break;
        case SOUTH:
          if (this.adjacentChestZPos != chestTe)
            this.adjacentChestChecked = false; 
          break;
        case EAST:
          if (this.adjacentChestXPos != chestTe)
            this.adjacentChestChecked = false; 
          break;
        case WEST:
          if (this.adjacentChestXNeg != chestTe)
            this.adjacentChestChecked = false; 
          break;
      } 
    } 
  }
  
  public void checkForAdjacentChests() {
    if (!this.adjacentChestChecked) {
      this.adjacentChestChecked = true;
      this.adjacentChestXNeg = getAdjacentChest(EnumFacing.WEST);
      this.adjacentChestXPos = getAdjacentChest(EnumFacing.EAST);
      this.adjacentChestZNeg = getAdjacentChest(EnumFacing.NORTH);
      this.adjacentChestZPos = getAdjacentChest(EnumFacing.SOUTH);
    } 
  }
  
  @Nullable
  protected TileEntityChest getAdjacentChest(EnumFacing side) {
    BlockPos blockpos = this.pos.offset(side);
    if (isChestAt(blockpos)) {
      TileEntity tileentity = this.world.getTileEntity(blockpos);
      if (tileentity instanceof TileEntityChest) {
        TileEntityChest tileentitychest = (TileEntityChest)tileentity;
        tileentitychest.setNeighbor(this, side.getOpposite());
        return tileentitychest;
      } 
    } 
    return null;
  }
  
  private boolean isChestAt(BlockPos posIn) {
    if (this.world == null)
      return false; 
    Block block = this.world.getBlockState(posIn).getBlock();
    return (block instanceof BlockChest && ((BlockChest)block).chestType == getChestType());
  }
  
  public void update() {
    checkForAdjacentChests();
    int i = this.pos.getX();
    int j = this.pos.getY();
    int k = this.pos.getZ();
    this.ticksSinceSync++;
    if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
      this.numPlayersUsing = 0;
      float f = 5.0F;
      for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((i - 5.0F), (j - 5.0F), (k - 5.0F), ((i + 1) + 5.0F), ((j + 1) + 5.0F), ((k + 1) + 5.0F)))) {
        if (entityplayer.openContainer instanceof ContainerChest) {
          IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();
          if (iinventory == this || (iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest((IInventory)this)))
            this.numPlayersUsing++; 
        } 
      } 
    } 
    this.prevLidAngle = this.lidAngle;
    float f1 = 0.1F;
    if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
      double d1 = i + 0.5D;
      double d2 = k + 0.5D;
      if (this.adjacentChestZPos != null)
        d2 += 0.5D; 
      if (this.adjacentChestXPos != null)
        d1 += 0.5D; 
      this.world.playSound(null, d1, j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
    } 
    if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0F) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0F)) {
      float f2 = this.lidAngle;
      if (this.numPlayersUsing > 0) {
        this.lidAngle += 0.1F;
      } else {
        this.lidAngle -= 0.1F;
      } 
      if (this.lidAngle > 1.0F)
        this.lidAngle = 1.0F; 
      float f3 = 0.5F;
      if (this.lidAngle < 0.5F && f2 >= 0.5F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
        double d3 = i + 0.5D;
        double d0 = k + 0.5D;
        if (this.adjacentChestZPos != null)
          d0 += 0.5D; 
        if (this.adjacentChestXPos != null)
          d3 += 0.5D; 
        this.world.playSound(null, d3, j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
      } 
      if (this.lidAngle < 0.0F)
        this.lidAngle = 0.0F; 
    } 
  }
  
  public boolean receiveClientEvent(int id, int type) {
    if (id == 1) {
      this.numPlayersUsing = type;
      return true;
    } 
    return super.receiveClientEvent(id, type);
  }
  
  public void openInventory(EntityPlayer player) {
    if (!player.isSpectator()) {
      if (this.numPlayersUsing < 0)
        this.numPlayersUsing = 0; 
      this.numPlayersUsing++;
      this.world.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
      this.world.notifyNeighborsOfStateChange(this.pos, getBlockType(), false);
      if (getChestType() == BlockChest.Type.TRAP)
        this.world.notifyNeighborsOfStateChange(this.pos.down(), getBlockType(), false); 
    } 
  }
  
  public void closeInventory(EntityPlayer player) {
    if (!player.isSpectator() && getBlockType() instanceof BlockChest) {
      this.numPlayersUsing--;
      this.world.addBlockEvent(this.pos, getBlockType(), 1, this.numPlayersUsing);
      this.world.notifyNeighborsOfStateChange(this.pos, getBlockType(), false);
      if (getChestType() == BlockChest.Type.TRAP)
        this.world.notifyNeighborsOfStateChange(this.pos.down(), getBlockType(), false); 
    } 
  }
  
  public void invalidate() {
    super.invalidate();
    updateContainingBlockInfo();
    checkForAdjacentChests();
  }
  
  public BlockChest.Type getChestType() {
    if (this.cachedChestType == null) {
      if (this.world == null || !(getBlockType() instanceof BlockChest))
        return BlockChest.Type.BASIC; 
      this.cachedChestType = ((BlockChest)getBlockType()).chestType;
    } 
    return this.cachedChestType;
  }
  
  public String getGuiID() {
    return "minecraft:chest";
  }
  
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    fillWithLoot(playerIn);
    return (Container)new ContainerChest((IInventory)playerInventory, (IInventory)this, playerIn);
  }
  
  protected NonNullList<ItemStack> func_190576_q() {
    return this.chestContents;
  }
  
  public TileEntityChest() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */