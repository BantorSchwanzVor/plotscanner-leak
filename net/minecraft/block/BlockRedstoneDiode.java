package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockHorizontal {
  protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
  
  protected final boolean isRepeaterPowered;
  
  protected BlockRedstoneDiode(boolean powered) {
    super(Material.CIRCUITS);
    this.isRepeaterPowered = powered;
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return REDSTONE_DIODE_AABB;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.down()).isFullyOpaque() ? super.canPlaceBlockAt(worldIn, pos) : false;
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.down()).isFullyOpaque();
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!isLocked((IBlockAccess)worldIn, pos, state)) {
      boolean flag = shouldBePowered(worldIn, pos, state);
      if (this.isRepeaterPowered && !flag) {
        worldIn.setBlockState(pos, getUnpoweredState(state), 2);
      } else if (!this.isRepeaterPowered) {
        worldIn.setBlockState(pos, getPoweredState(state), 2);
        if (!flag)
          worldIn.updateBlockTick(pos, getPoweredState(state).getBlock(), getTickDelay(state), -1); 
      } 
    } 
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return (side.getAxis() != EnumFacing.Axis.Y);
  }
  
  protected boolean isPowered(IBlockState state) {
    return this.isRepeaterPowered;
  }
  
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getWeakPower(blockAccess, pos, side);
  }
  
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    if (!isPowered(blockState))
      return 0; 
    return (blockState.getValue((IProperty)FACING) == side) ? getActiveSignal(blockAccess, pos, blockState) : 0;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (canBlockStay(worldIn, pos)) {
      updateState(worldIn, pos, state);
    } else {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
      byte b;
      int i;
      EnumFacing[] arrayOfEnumFacing;
      for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
        EnumFacing enumfacing = arrayOfEnumFacing[b];
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
        b++;
      } 
    } 
  }
  
  protected void updateState(World worldIn, BlockPos pos, IBlockState state) {
    if (!isLocked((IBlockAccess)worldIn, pos, state)) {
      boolean flag = shouldBePowered(worldIn, pos, state);
      if (this.isRepeaterPowered != flag && !worldIn.isBlockTickPending(pos, this)) {
        int i = -1;
        if (isFacingTowardsRepeater(worldIn, pos, state)) {
          i = -3;
        } else if (this.isRepeaterPowered) {
          i = -2;
        } 
        worldIn.updateBlockTick(pos, this, getDelay(state), i);
      } 
    } 
  }
  
  public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
    return false;
  }
  
  protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state) {
    return (calculateInputStrength(worldIn, pos, state) > 0);
  }
  
  protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    BlockPos blockpos = pos.offset(enumfacing);
    int i = worldIn.getRedstonePower(blockpos, enumfacing);
    if (i >= 15)
      return i; 
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    return Math.max(i, (iblockstate.getBlock() == Blocks.REDSTONE_WIRE) ? ((Integer)iblockstate.getValue((IProperty)BlockRedstoneWire.POWER)).intValue() : 0);
  }
  
  protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    EnumFacing enumfacing1 = enumfacing.rotateY();
    EnumFacing enumfacing2 = enumfacing.rotateYCCW();
    return Math.max(getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
  }
  
  protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (isAlternateInput(iblockstate)) {
      if (block == Blocks.REDSTONE_BLOCK)
        return 15; 
      return (block == Blocks.REDSTONE_WIRE) ? ((Integer)iblockstate.getValue((IProperty)BlockRedstoneWire.POWER)).intValue() : worldIn.getStrongPower(pos, side);
    } 
    return 0;
  }
  
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite());
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (shouldBePowered(worldIn, pos, state))
      worldIn.scheduleUpdate(pos, this, 1); 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    notifyNeighbors(worldIn, pos, state);
  }
  
  protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    BlockPos blockpos = pos.offset(enumfacing.getOpposite());
    worldIn.func_190524_a(blockpos, this, pos);
    worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
  }
  
  public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    if (this.isRepeaterPowered) {
      byte b;
      int i;
      EnumFacing[] arrayOfEnumFacing;
      for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
        EnumFacing enumfacing = arrayOfEnumFacing[b];
        worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
        b++;
      } 
    } 
    super.onBlockDestroyedByPlayer(worldIn, pos, state);
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  protected boolean isAlternateInput(IBlockState state) {
    return state.canProvidePower();
  }
  
  protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
    return 15;
  }
  
  public static boolean isDiode(IBlockState state) {
    return !(!Blocks.UNPOWERED_REPEATER.isSameDiode(state) && !Blocks.UNPOWERED_COMPARATOR.isSameDiode(state));
  }
  
  public boolean isSameDiode(IBlockState state) {
    Block block = state.getBlock();
    return !(block != getPoweredState(getDefaultState()).getBlock() && block != getUnpoweredState(getDefaultState()).getBlock());
  }
  
  public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state) {
    EnumFacing enumfacing = ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite();
    BlockPos blockpos = pos.offset(enumfacing);
    if (isDiode(worldIn.getBlockState(blockpos)))
      return (worldIn.getBlockState(blockpos).getValue((IProperty)FACING) != enumfacing); 
    return false;
  }
  
  protected int getTickDelay(IBlockState state) {
    return getDelay(state);
  }
  
  protected abstract int getDelay(IBlockState paramIBlockState);
  
  protected abstract IBlockState getPoweredState(IBlockState paramIBlockState);
  
  protected abstract IBlockState getUnpoweredState(IBlockState paramIBlockState);
  
  public boolean isAssociatedBlock(Block other) {
    return isSameDiode(other.getDefaultState());
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (p_193383_4_ == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockRedstoneDiode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */