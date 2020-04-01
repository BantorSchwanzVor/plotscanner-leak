package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;

public class BlockPrismarine extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public static final int ROUGH_META = EnumType.ROUGH.getMetadata();
  
  public static final int BRICKS_META = EnumType.BRICKS.getMetadata();
  
  public static final int DARK_META = EnumType.DARK.getMetadata();
  
  public BlockPrismarine() {
    super(Material.ROCK);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.ROUGH));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public String getLocalizedName() {
    return I18n.translateToLocal(String.valueOf(getUnlocalizedName()) + "." + EnumType.ROUGH.getUnlocalizedName() + ".name");
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return (state.getValue((IProperty)VARIANT) == EnumType.ROUGH) ? MapColor.CYAN : MapColor.DIAMOND;
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    tab.add(new ItemStack(this, 1, ROUGH_META));
    tab.add(new ItemStack(this, 1, BRICKS_META));
    tab.add(new ItemStack(this, 1, DARK_META));
  }
  
  public enum EnumType implements IStringSerializable {
    ROUGH(0, "prismarine", "rough"),
    BRICKS(1, "prismarine_bricks", "bricks"),
    DARK(2, "dark_prismarine", "dark");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockprismarine$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockprismarine$enumtype.getMetadata()] = blockprismarine$enumtype;
        b++;
      } 
    }
    
    EnumType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getUnlocalizedName() {
      return this.unlocalizedName;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPrismarine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */