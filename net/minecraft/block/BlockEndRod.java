package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndRod extends BlockDirectional {
  protected static final AxisAlignedBB END_ROD_VERTICAL_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
  
  protected static final AxisAlignedBB END_ROD_NS_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 1.0D);
  
  protected static final AxisAlignedBB END_ROD_EW_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);
  
  protected BlockEndRod() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.UP));
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withProperty((IProperty)FACING, (Comparable)mirrorIn.mirror((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch (((EnumFacing)state.getValue((IProperty)FACING)).getAxis()) {
      default:
        return END_ROD_EW_AABB;
      case Z:
        return END_ROD_NS_AABB;
      case Y:
        break;
    } 
    return END_ROD_VERTICAL_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return true;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = worldIn.getBlockState(pos.offset(facing.getOpposite()));
    if (iblockstate.getBlock() == Blocks.END_ROD) {
      EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)FACING);
      if (enumfacing == facing)
        return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing.getOpposite()); 
    } 
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing);
  }
  
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    EnumFacing enumfacing = (EnumFacing)stateIn.getValue((IProperty)FACING);
    double d0 = pos.getX() + 0.55D - (rand.nextFloat() * 0.1F);
    double d1 = pos.getY() + 0.55D - (rand.nextFloat() * 0.1F);
    double d2 = pos.getZ() + 0.55D - (rand.nextFloat() * 0.1F);
    double d3 = (0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F);
    if (rand.nextInt(5) == 0)
      worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0 + enumfacing.getFrontOffsetX() * d3, d1 + enumfacing.getFrontOffsetY() * d3, d2 + enumfacing.getFrontOffsetZ() * d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, new int[0]); 
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState();
    iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.getFront(meta));
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING });
  }
  
  public EnumPushReaction getMobilityFlag(IBlockState state) {
    return EnumPushReaction.NORMAL;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockEndRod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */