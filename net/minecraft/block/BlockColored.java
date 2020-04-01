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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColored extends Block {
  public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
  
  public BlockColored(Material materialIn) {
    super(materialIn);
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
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)COLOR, (Comparable)EnumDyeColor.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumDyeColor)state.getValue((IProperty)COLOR)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)COLOR });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */