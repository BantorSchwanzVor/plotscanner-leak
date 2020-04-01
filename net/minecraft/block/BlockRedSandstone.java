package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class BlockRedSandstone extends Block {
  public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
  
  public BlockRedSandstone() {
    super(Material.ROCK, BlockSand.EnumType.RED_SAND.getMapColor());
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
      EnumType blockredsandstone$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blockredsandstone$enumtype.getMetadata()));
      b++;
    } 
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
    DEFAULT(0, "red_sandstone", "default"),
    CHISELED(1, "chiseled_red_sandstone", "chiseled"),
    SMOOTH(2, "smooth_red_sandstone", "smooth");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockredsandstone$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype;
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockRedSandstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */