package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBush extends Block {
  protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);
  
  protected BlockBush() {
    this(Material.PLANTS);
  }
  
  protected BlockBush(Material materialIn) {
    this(materialIn, materialIn.getMaterialMapColor());
  }
  
  protected BlockBush(Material materialIn, MapColor mapColorIn) {
    super(materialIn, mapColorIn);
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return (super.canPlaceBlockAt(worldIn, pos) && canSustainBush(worldIn.getBlockState(pos.down())));
  }
  
  protected boolean canSustainBush(IBlockState state) {
    return !(state.getBlock() != Blocks.GRASS && state.getBlock() != Blocks.DIRT && state.getBlock() != Blocks.FARMLAND);
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
    checkAndDropBlock(worldIn, pos, state);
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    checkAndDropBlock(worldIn, pos, state);
  }
  
  protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!canBlockStay(worldIn, pos, state)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    } 
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
    return canSustainBush(worldIn.getBlockState(pos.down()));
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return BUSH_AABB;
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
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockBush.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */