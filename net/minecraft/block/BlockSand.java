package net.minecraft.block;

import net.minecraft.block.material.MapColor;
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

public class BlockSand extends BlockFalling {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockSand() {
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.SAND));
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blocksand$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blocksand$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public int getDustColor(IBlockState p_189876_1_) {
    EnumType blocksand$enumtype = (EnumType)p_189876_1_.getValue((IProperty)VARIANT);
    return blocksand$enumtype.getDustColor();
  }
  
  public enum EnumType implements IStringSerializable {
    SAND(0, "sand", "default", (String)MapColor.SAND, -2370656),
    RED_SAND(1, "red_sand", "red", (String)MapColor.ADOBE, -5679071);
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final MapColor mapColor;
    
    private final String unlocalizedName;
    
    private final int dustColor;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blocksand$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
        b++;
      } 
    }
    
    EnumType(int p_i47157_3_, String p_i47157_4_, String p_i47157_5_, MapColor p_i47157_6_, int p_i47157_7_) {
      this.meta = p_i47157_3_;
      this.name = p_i47157_4_;
      this.mapColor = p_i47157_6_;
      this.unlocalizedName = p_i47157_5_;
      this.dustColor = p_i47157_7_;
    }
    
    public int getDustColor() {
      return this.dustColor;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public String toString() {
      return this.name;
    }
    
    public MapColor getMapColor() {
      return this.mapColor;
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */