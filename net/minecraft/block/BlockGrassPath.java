package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGrassPath extends Block {
  protected static final AxisAlignedBB GRASS_PATH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
  
  protected BlockGrassPath() {
    super(Material.GROUND);
    setLightOpacity(255);
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    IBlockState iblockstate;
    Block block;
    switch (side) {
      case UP:
        return true;
      case NORTH:
      case SOUTH:
      case WEST:
      case EAST:
        iblockstate = blockAccess.getBlockState(pos.offset(side));
        block = iblockstate.getBlock();
        return (!iblockstate.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH);
    } 
    return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
    func_190971_b(worldIn, pos);
  }
  
  private void func_190971_b(World p_190971_1_, BlockPos p_190971_2_) {
    if (p_190971_1_.getBlockState(p_190971_2_.up()).getMaterial().isSolid())
      BlockFarmland.func_190970_b(p_190971_1_, p_190971_2_); 
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return GRASS_PATH_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this);
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
    func_190971_b(worldIn, pos);
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (p_193383_4_ == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockGrassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */