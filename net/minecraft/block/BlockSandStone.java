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
import net.minecraft.world.IBlockAccess;

public class BlockSandStone extends Block {
  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
  
  public BlockSandStone() {
    super(Material.ROCK);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)TYPE, EnumType.DEFAULT));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)TYPE)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blocksandstone$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blocksandstone$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.SAND;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)TYPE, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)TYPE)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)TYPE });
  }
  
  public enum EnumType implements IStringSerializable {
    DEFAULT(0, "sandstone", "default"),
    CHISELED(1, "chiseled_sandstone", "chiseled"),
    SMOOTH(2, "smooth_sandstone", "smooth");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int metadata;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blocksandstone$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blocksandstone$enumtype.getMetadata()] = blocksandstone$enumtype;
        b++;
      } 
    }
    
    EnumType(int meta, String name, String unlocalizedName) {
      this.metadata = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMetadata() {
      return this.metadata;
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSandStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */