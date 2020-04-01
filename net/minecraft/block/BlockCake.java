package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block {
  public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
  
  protected static final AxisAlignedBB[] CAKE_AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D) };
  
  protected BlockCake() {
    super(Material.CAKE);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)BITES, Integer.valueOf(0)));
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return CAKE_AABB[((Integer)state.getValue((IProperty)BITES)).intValue()];
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (!worldIn.isRemote)
      return eatCake(worldIn, pos, state, playerIn); 
    ItemStack itemstack = playerIn.getHeldItem(hand);
    return !(!eatCake(worldIn, pos, state, playerIn) && !itemstack.func_190926_b());
  }
  
  private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    if (!player.canEat(false))
      return false; 
    player.addStat(StatList.CAKE_SLICES_EATEN);
    player.getFoodStats().addStats(2, 0.1F);
    int i = ((Integer)state.getValue((IProperty)BITES)).intValue();
    if (i < 6) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)BITES, Integer.valueOf(i + 1)), 3);
    } else {
      worldIn.setBlockToAir(pos);
    } 
    return true;
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return super.canPlaceBlockAt(worldIn, pos) ? canBlockStay(worldIn, pos) : false;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!canBlockStay(worldIn, pos))
      worldIn.setBlockToAir(pos); 
  }
  
  private boolean canBlockStay(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.field_190931_a;
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Items.CAKE);
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)BITES, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)BITES)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)BITES });
  }
  
  public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
    return (7 - ((Integer)blockState.getValue((IProperty)BITES)).intValue()) * 2;
  }
  
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockCake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */