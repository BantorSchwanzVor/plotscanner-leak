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

public class BlockPlanks extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockPlanks() {
    super(Material.WOOD);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.OAK));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blockplanks$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blockplanks$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public enum EnumType implements IStringSerializable {
    OAK(0, "oak", MapColor.WOOD),
    SPRUCE(1, "spruce", MapColor.OBSIDIAN),
    BIRCH(2, "birch", MapColor.SAND),
    JUNGLE(3, "jungle", MapColor.DIRT),
    ACACIA(4, "acacia", MapColor.ADOBE),
    DARK_OAK(5, "dark_oak", "big_oak", (String)MapColor.BROWN);
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    private final MapColor mapColor;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockplanks$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
        b++;
      } 
    }
    
    EnumType(int metaIn, String nameIn, String unlocalizedNameIn, MapColor mapColorIn) {
      this.meta = metaIn;
      this.name = nameIn;
      this.unlocalizedName = unlocalizedNameIn;
      this.mapColor = mapColorIn;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public MapColor getMapColor() {
      return this.mapColor;
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPlanks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */