package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusPlant extends Block {
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public static final PropertyBool UP = PropertyBool.create("up");
  
  public static final PropertyBool DOWN = PropertyBool.create("down");
  
  protected BlockChorusPlant() {
    super(Material.PLANTS, MapColor.PURPLE);
    setCreativeTab(CreativeTabs.DECORATIONS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)).withProperty((IProperty)UP, Boolean.valueOf(false)).withProperty((IProperty)DOWN, Boolean.valueOf(false)));
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    Block block = worldIn.getBlockState(pos.down()).getBlock();
    Block block1 = worldIn.getBlockState(pos.up()).getBlock();
    Block block2 = worldIn.getBlockState(pos.north()).getBlock();
    Block block3 = worldIn.getBlockState(pos.east()).getBlock();
    Block block4 = worldIn.getBlockState(pos.south()).getBlock();
    Block block5 = worldIn.getBlockState(pos.west()).getBlock();
    return state.withProperty((IProperty)DOWN, Boolean.valueOf(!(block != this && block != Blocks.CHORUS_FLOWER && block != Blocks.END_STONE))).withProperty((IProperty)UP, Boolean.valueOf(!(block1 != this && block1 != Blocks.CHORUS_FLOWER))).withProperty((IProperty)NORTH, Boolean.valueOf(!(block2 != this && block2 != Blocks.CHORUS_FLOWER))).withProperty((IProperty)EAST, Boolean.valueOf(!(block3 != this && block3 != Blocks.CHORUS_FLOWER))).withProperty((IProperty)SOUTH, Boolean.valueOf(!(block4 != this && block4 != Blocks.CHORUS_FLOWER))).withProperty((IProperty)WEST, Boolean.valueOf(!(block5 != this && block5 != Blocks.CHORUS_FLOWER)));
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    state = state.getActualState(source, pos);
    float f = 0.1875F;
    float f1 = ((Boolean)state.getValue((IProperty)WEST)).booleanValue() ? 0.0F : 0.1875F;
    float f2 = ((Boolean)state.getValue((IProperty)DOWN)).booleanValue() ? 0.0F : 0.1875F;
    float f3 = ((Boolean)state.getValue((IProperty)NORTH)).booleanValue() ? 0.0F : 0.1875F;
    float f4 = ((Boolean)state.getValue((IProperty)EAST)).booleanValue() ? 1.0F : 0.8125F;
    float f5 = ((Boolean)state.getValue((IProperty)UP)).booleanValue() ? 1.0F : 0.8125F;
    float f6 = ((Boolean)state.getValue((IProperty)SOUTH)).booleanValue() ? 1.0F : 0.8125F;
    return new AxisAlignedBB(f1, f2, f3, f4, f5, f6);
  }
  
  public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    if (!p_185477_7_)
      state = state.getActualState((IBlockAccess)worldIn, pos); 
    float f = 0.1875F;
    float f1 = 0.8125F;
    addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D));
    if (((Boolean)state.getValue((IProperty)WEST)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D)); 
    if (((Boolean)state.getValue((IProperty)EAST)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.8125D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D)); 
    if (((Boolean)state.getValue((IProperty)UP)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.8125D, 0.1875D, 0.8125D, 1.0D, 0.8125D)); 
    if (((Boolean)state.getValue((IProperty)DOWN)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.1875D, 0.8125D)); 
    if (((Boolean)state.getValue((IProperty)NORTH)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D)); 
    if (((Boolean)state.getValue((IProperty)SOUTH)).booleanValue())
      addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D)); 
  }
  
  public int getMetaFromState(IBlockState state) {
    return 0;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!canSurviveAt(worldIn, pos))
      worldIn.destroyBlock(pos, true); 
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.CHORUS_FRUIT;
  }
  
  public int quantityDropped(Random random) {
    return random.nextInt(2);
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return super.canPlaceBlockAt(worldIn, pos) ? canSurviveAt(worldIn, pos) : false;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!canSurviveAt(worldIn, pos))
      worldIn.scheduleUpdate(pos, this, 1); 
  }
  
  public boolean canSurviveAt(World wordIn, BlockPos pos) {
    boolean flag = wordIn.isAirBlock(pos.up());
    boolean flag1 = wordIn.isAirBlock(pos.down());
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      BlockPos blockpos = pos.offset(enumfacing);
      Block block = wordIn.getBlockState(blockpos).getBlock();
      if (block == this) {
        if (!flag && !flag1)
          return false; 
        Block block1 = wordIn.getBlockState(blockpos.down()).getBlock();
        if (block1 == this || block1 == Blocks.END_STONE)
          return true; 
      } 
    } 
    Block block2 = wordIn.getBlockState(pos.down()).getBlock();
    return !(block2 != this && block2 != Blocks.END_STONE);
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    Block block = blockAccess.getBlockState(pos.offset(side)).getBlock();
    return (block != this && block != Blocks.CHORUS_FLOWER && (side != EnumFacing.DOWN || block != Blocks.END_STONE));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST, (IProperty)UP, (IProperty)DOWN });
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return false;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockChorusPlant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */