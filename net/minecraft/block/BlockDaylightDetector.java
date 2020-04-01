package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer {
  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
  
  protected static final AxisAlignedBB DAYLIGHT_DETECTOR_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);
  
  private final boolean inverted;
  
  public BlockDaylightDetector(boolean inverted) {
    super(Material.WOOD);
    this.inverted = inverted;
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWER, Integer.valueOf(0)));
    setCreativeTab(CreativeTabs.REDSTONE);
    setHardness(0.2F);
    setSoundType(SoundType.WOOD);
    setUnlocalizedName("daylightDetector");
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return DAYLIGHT_DETECTOR_AABB;
  }
  
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return ((Integer)blockState.getValue((IProperty)POWER)).intValue();
  }
  
  public void updatePower(World worldIn, BlockPos pos) {
    if (worldIn.provider.func_191066_m()) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      int i = worldIn.getLightFor(EnumSkyBlock.SKY, pos) - worldIn.getSkylightSubtracted();
      float f = worldIn.getCelestialAngleRadians(1.0F);
      if (this.inverted)
        i = 15 - i; 
      if (i > 0 && !this.inverted) {
        float f1 = (f < 3.1415927F) ? 0.0F : 6.2831855F;
        f += (f1 - f) * 0.2F;
        i = Math.round(i * MathHelper.cos(f));
      } 
      i = MathHelper.clamp(i, 0, 15);
      if (((Integer)iblockstate.getValue((IProperty)POWER)).intValue() != i)
        worldIn.setBlockState(pos, iblockstate.withProperty((IProperty)POWER, Integer.valueOf(i)), 3); 
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (playerIn.isAllowEdit()) {
      if (worldIn.isRemote)
        return true; 
      if (this.inverted) {
        worldIn.setBlockState(pos, Blocks.DAYLIGHT_DETECTOR.getDefaultState().withProperty((IProperty)POWER, state.getValue((IProperty)POWER)), 4);
        Blocks.DAYLIGHT_DETECTOR.updatePower(worldIn, pos);
      } else {
        worldIn.setBlockState(pos, Blocks.DAYLIGHT_DETECTOR_INVERTED.getDefaultState().withProperty((IProperty)POWER, state.getValue((IProperty)POWER)), 4);
        Blocks.DAYLIGHT_DETECTOR_INVERTED.updatePower(worldIn, pos);
      } 
      return true;
    } 
    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(Blocks.DAYLIGHT_DETECTOR);
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Blocks.DAYLIGHT_DETECTOR);
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }
  
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return (TileEntity)new TileEntityDaylightDetector();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)POWER, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)POWER)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)POWER });
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    if (!this.inverted)
      super.getSubBlocks(itemIn, tab); 
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (p_193383_4_ == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockDaylightDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */