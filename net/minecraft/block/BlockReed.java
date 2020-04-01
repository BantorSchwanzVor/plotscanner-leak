package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReed extends Block {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
  
  protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
  
  protected BlockReed() {
    super(Material.PLANTS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)));
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return REED_AABB;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (worldIn.getBlockState(pos.down()).getBlock() == Blocks.REEDS || checkForDrop(worldIn, pos, state))
      if (worldIn.isAirBlock(pos.up())) {
        int i;
        for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; i++);
        if (i < 3) {
          int j = ((Integer)state.getValue((IProperty)AGE)).intValue();
          if (j == 15) {
            worldIn.setBlockState(pos.up(), getDefaultState());
            worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(0)), 4);
          } else {
            worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(j + 1)), 4);
          } 
        } 
      }  
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    Block block = worldIn.getBlockState(pos.down()).getBlock();
    if (block == this)
      return true; 
    if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.SAND)
      return false; 
    BlockPos blockpos = pos.down();
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      IBlockState iblockstate = worldIn.getBlockState(blockpos.offset(enumfacing));
      if (iblockstate.getMaterial() == Material.WATER || iblockstate.getBlock() == Blocks.FROSTED_ICE)
        return true; 
    } 
    return false;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    checkForDrop(worldIn, pos, state);
  }
  
  protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
    if (canBlockStay(worldIn, pos))
      return true; 
    dropBlockAsItem(worldIn, pos, state, 0);
    worldIn.setBlockToAir(pos);
    return false;
  }
  
  public boolean canBlockStay(World worldIn, BlockPos pos) {
    return canPlaceBlockAt(worldIn, pos);
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.REEDS;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Items.REEDS);
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)AGE)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)AGE });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockReed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */