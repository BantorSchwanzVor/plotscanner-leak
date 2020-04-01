package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder extends Block {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
  
  protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
  
  protected BlockLadder() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch ((EnumFacing)state.getValue((IProperty)FACING)) {
      case NORTH:
        return LADDER_NORTH_AABB;
      case SOUTH:
        return LADDER_SOUTH_AABB;
      case WEST:
        return LADDER_WEST_AABB;
    } 
    return LADDER_EAST_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    if (func_193392_c(worldIn, pos.west(), side))
      return true; 
    if (func_193392_c(worldIn, pos.east(), side))
      return true; 
    if (func_193392_c(worldIn, pos.north(), side))
      return true; 
    return func_193392_c(worldIn, pos.south(), side);
  }
  
  private boolean func_193392_c(World p_193392_1_, BlockPos p_193392_2_, EnumFacing p_193392_3_) {
    IBlockState iblockstate = p_193392_1_.getBlockState(p_193392_2_);
    boolean flag = func_193382_c(iblockstate.getBlock());
    return (!flag && iblockstate.func_193401_d((IBlockAccess)p_193392_1_, p_193392_2_, p_193392_3_) == BlockFaceShape.SOLID && !iblockstate.canProvidePower());
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    if (facing.getAxis().isHorizontal() && func_193392_c(worldIn, pos.offset(facing.getOpposite()), facing))
      return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing); 
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (func_193392_c(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing))
        return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing); 
    } 
    return getDefaultState();
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    if (!func_193392_c(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
    super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing enumfacing = EnumFacing.getFront(meta);
    if (enumfacing.getAxis() == EnumFacing.Axis.Y)
      enumfacing = EnumFacing.NORTH; 
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockLadder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */