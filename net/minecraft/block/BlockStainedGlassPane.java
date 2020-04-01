package net.minecraft.block;

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
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStainedGlassPane extends BlockPane {
  public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
  
  public BlockStainedGlassPane() {
    super(Material.GLASS, false);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)).withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.WHITE));
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    for (int i = 0; i < (EnumDyeColor.values()).length; i++)
      tab.add(new ItemStack(this, 1, i)); 
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.func_193558_a((EnumDyeColor)state.getValue((IProperty)COLOR));
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case null:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)SOUTH)).withProperty((IProperty)EAST, state.getValue((IProperty)WEST)).withProperty((IProperty)SOUTH, state.getValue((IProperty)NORTH)).withProperty((IProperty)WEST, state.getValue((IProperty)EAST));
      case COUNTERCLOCKWISE_90:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)EAST)).withProperty((IProperty)EAST, state.getValue((IProperty)SOUTH)).withProperty((IProperty)SOUTH, state.getValue((IProperty)WEST)).withProperty((IProperty)WEST, state.getValue((IProperty)NORTH));
      case CLOCKWISE_90:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)WEST)).withProperty((IProperty)EAST, state.getValue((IProperty)NORTH)).withProperty((IProperty)SOUTH, state.getValue((IProperty)EAST)).withProperty((IProperty)WEST, state.getValue((IProperty)SOUTH));
    } 
    return state;
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    switch (mirrorIn) {
      case LEFT_RIGHT:
        return state.withProperty((IProperty)NORTH, state.getValue((IProperty)SOUTH)).withProperty((IProperty)SOUTH, state.getValue((IProperty)NORTH));
      case null:
        return state.withProperty((IProperty)EAST, state.getValue((IProperty)WEST)).withProperty((IProperty)WEST, state.getValue((IProperty)EAST));
    } 
    return super.withMirror(state, mirrorIn);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)NORTH, (IProperty)EAST, (IProperty)WEST, (IProperty)SOUTH, (IProperty)COLOR });
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote)
      BlockBeacon.updateColorAsync(worldIn, pos); 
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (!worldIn.isRemote)
      BlockBeacon.updateColorAsync(worldIn, pos); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStainedGlassPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */