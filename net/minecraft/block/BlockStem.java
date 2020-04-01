package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem extends BlockBush implements IGrowable {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
  
  public static final PropertyDirection FACING = BlockTorch.FACING;
  
  private final Block crop;
  
  protected static final AxisAlignedBB[] STEM_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D) };
  
  protected BlockStem(Block crop) {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)).withProperty((IProperty)FACING, (Comparable)EnumFacing.UP));
    this.crop = crop;
    setTickRandomly(true);
    setCreativeTab(null);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return STEM_AABB[((Integer)state.getValue((IProperty)AGE)).intValue()];
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
    state = state.withProperty((IProperty)FACING, (Comparable)EnumFacing.UP);
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop && i == 7) {
        state = state.withProperty((IProperty)FACING, (Comparable)enumfacing);
        break;
      } 
    } 
    return state;
  }
  
  protected boolean canSustainBush(IBlockState state) {
    return (state.getBlock() == Blocks.FARMLAND);
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    super.updateTick(worldIn, pos, state, rand);
    if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
      float f = BlockCrops.getGrowthChance(this, worldIn, pos);
      if (rand.nextInt((int)(25.0F / f) + 1) == 0) {
        int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
        if (i < 7) {
          state = state.withProperty((IProperty)AGE, Integer.valueOf(i + 1));
          worldIn.setBlockState(pos, state, 2);
        } else {
          for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop)
              return; 
          } 
          pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
          Block block = worldIn.getBlockState(pos.down()).getBlock();
          if ((worldIn.getBlockState(pos).getBlock()).blockMaterial == Material.AIR && (block == Blocks.FARMLAND || block == Blocks.DIRT || block == Blocks.GRASS))
            worldIn.setBlockState(pos, this.crop.getDefaultState()); 
        } 
      } 
    } 
  }
  
  public void growStem(World worldIn, BlockPos pos, IBlockState state) {
    int i = ((Integer)state.getValue((IProperty)AGE)).intValue() + MathHelper.getInt(worldIn.rand, 2, 5);
    worldIn.setBlockState(pos, state.withProperty((IProperty)AGE, Integer.valueOf(Math.min(7, i))), 2);
  }
  
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    if (!worldIn.isRemote) {
      Item item = getSeedItem();
      if (item != null) {
        int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
        for (int j = 0; j < 3; j++) {
          if (worldIn.rand.nextInt(15) <= i)
            spawnAsEntity(worldIn, pos, new ItemStack(item)); 
        } 
      } 
    } 
  }
  
  @Nullable
  protected Item getSeedItem() {
    if (this.crop == Blocks.PUMPKIN)
      return Items.PUMPKIN_SEEDS; 
    return (this.crop == Blocks.MELON_BLOCK) ? Items.MELON_SEEDS : null;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.field_190931_a;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    Item item = getSeedItem();
    return (item == null) ? ItemStack.field_190927_a : new ItemStack(item);
  }
  
  public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
    return (((Integer)state.getValue((IProperty)AGE)).intValue() != 7);
  }
  
  public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    return true;
  }
  
  public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
    growStem(worldIn, pos, state);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)AGE)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)AGE, (IProperty)FACING });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */