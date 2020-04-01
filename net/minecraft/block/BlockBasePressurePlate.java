package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate extends Block {
  protected static final AxisAlignedBB PRESSED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);
  
  protected static final AxisAlignedBB UNPRESSED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
  
  protected static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D);
  
  protected BlockBasePressurePlate(Material materialIn) {
    this(materialIn, materialIn.getMaterialMapColor());
  }
  
  protected BlockBasePressurePlate(Material materialIn, MapColor mapColorIn) {
    super(materialIn, mapColorIn);
    setCreativeTab(CreativeTabs.REDSTONE);
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    boolean flag = (getRedstoneStrength(state) > 0);
    return flag ? PRESSED_AABB : UNPRESSED_AABB;
  }
  
  public int tickRate(World worldIn) {
    return 20;
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  
  public boolean canSpawnInBlock() {
    return true;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return canBePlacedOn(worldIn, pos.down());
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!canBePlacedOn(worldIn, pos.down())) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  private boolean canBePlacedOn(World worldIn, BlockPos pos) {
    return !(!worldIn.getBlockState(pos).isFullyOpaque() && !(worldIn.getBlockState(pos).getBlock() instanceof BlockFence));
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote) {
      int i = getRedstoneStrength(state);
      if (i > 0)
        updateState(worldIn, pos, state, i); 
    } 
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote) {
      int i = getRedstoneStrength(state);
      if (i == 0)
        updateState(worldIn, pos, state, i); 
    } 
  }
  
  protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength) {
    int i = computeRedstoneStrength(worldIn, pos);
    boolean flag = (oldRedstoneStrength > 0);
    boolean flag1 = (i > 0);
    if (oldRedstoneStrength != i) {
      state = setRedstoneStrength(state, i);
      worldIn.setBlockState(pos, state, 2);
      updateNeighbors(worldIn, pos);
      worldIn.markBlockRangeForRenderUpdate(pos, pos);
    } 
    if (!flag1 && flag) {
      playClickOffSound(worldIn, pos);
    } else if (flag1 && !flag) {
      playClickOnSound(worldIn, pos);
    } 
    if (flag1)
      worldIn.scheduleUpdate(new BlockPos((Vec3i)pos), this, tickRate(worldIn)); 
  }
  
  protected abstract void playClickOnSound(World paramWorld, BlockPos paramBlockPos);
  
  protected abstract void playClickOffSound(World paramWorld, BlockPos paramBlockPos);
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (getRedstoneStrength(state) > 0)
      updateNeighbors(worldIn, pos); 
    super.breakBlock(worldIn, pos, state);
  }
  
  protected void updateNeighbors(World worldIn, BlockPos pos) {
    worldIn.notifyNeighborsOfStateChange(pos, this, false);
    worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
  }
  
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return getRedstoneStrength(blockState);
  }
  
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return (side == EnumFacing.UP) ? getRedstoneStrength(blockState) : 0;
  }
  
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  
  public EnumPushReaction getMobilityFlag(IBlockState state) {
    return EnumPushReaction.DESTROY;
  }
  
  protected abstract int computeRedstoneStrength(World paramWorld, BlockPos paramBlockPos);
  
  protected abstract int getRedstoneStrength(IBlockState paramIBlockState);
  
  protected abstract IBlockState setRedstoneStrength(IBlockState paramIBlockState, int paramInt);
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockBasePressurePlate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */