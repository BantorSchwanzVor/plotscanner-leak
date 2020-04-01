package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStainedGlass extends BlockBreakable {
  public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
  
  public BlockStainedGlass(Material materialIn) {
    super(materialIn, false);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.WHITE));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumDyeColor[] arrayOfEnumDyeColor;
    for (i = (arrayOfEnumDyeColor = EnumDyeColor.values()).length, b = 0; b < i; ) {
      EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[b];
      tab.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
      b++;
    } 
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.func_193558_a((EnumDyeColor)state.getValue((IProperty)COLOR));
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  protected boolean canSilkHarvest() {
    return true;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.byMetadata(meta));
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote)
      BlockBeacon.updateColorAsync(worldIn, pos); 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote)
      BlockBeacon.updateColorAsync(worldIn, pos); 
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)COLOR });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStainedGlass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */