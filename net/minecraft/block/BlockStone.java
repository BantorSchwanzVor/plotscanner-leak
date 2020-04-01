package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;

public class BlockStone extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockStone() {
    super(Material.ROCK);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.STONE));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public String getLocalizedName() {
    return I18n.translateToLocal(String.valueOf(getUnlocalizedName()) + "." + EnumType.STONE.getUnlocalizedName() + ".name");
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return (state.getValue((IProperty)VARIANT) == EnumType.STONE) ? Item.getItemFromBlock(Blocks.COBBLESTONE) : Item.getItemFromBlock(Blocks.STONE);
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blockstone$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blockstone$enumtype.getMetadata()));
      b++;
    } 
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
  
  public enum EnumType implements IStringSerializable {
    STONE(0, MapColor.STONE, "stone", true),
    GRANITE(1, MapColor.DIRT, "granite", true),
    GRANITE_SMOOTH(2, MapColor.DIRT, "smooth_granite", (MapColor)"graniteSmooth", false),
    DIORITE(3, MapColor.QUARTZ, "diorite", true),
    DIORITE_SMOOTH(4, MapColor.QUARTZ, "smooth_diorite", (MapColor)"dioriteSmooth", false),
    ANDESITE(5, MapColor.STONE, "andesite", true),
    ANDESITE_SMOOTH(6, MapColor.STONE, "smooth_andesite", (MapColor)"andesiteSmooth", false);
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    private final MapColor mapColor;
    
    private final boolean field_190913_m;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockstone$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
        b++;
      } 
    }
    
    EnumType(int p_i46384_3_, MapColor p_i46384_4_, String p_i46384_5_, String p_i46384_6_, boolean p_i46384_7_) {
      this.meta = p_i46384_3_;
      this.name = p_i46384_5_;
      this.unlocalizedName = p_i46384_6_;
      this.mapColor = p_i46384_4_;
      this.field_190913_m = p_i46384_7_;
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
    
    public boolean func_190912_e() {
      return this.field_190913_m;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */