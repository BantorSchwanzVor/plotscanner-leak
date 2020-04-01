package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block {
  public static final PropertyEnum<DirtType> VARIANT = PropertyEnum.create("variant", DirtType.class);
  
  public static final PropertyBool SNOWY = PropertyBool.create("snowy");
  
  protected BlockDirt() {
    super(Material.GROUND);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, DirtType.DIRT).withProperty((IProperty)SNOWY, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return ((DirtType)state.getValue((IProperty)VARIANT)).getColor();
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    if (state.getValue((IProperty)VARIANT) == DirtType.PODZOL) {
      Block block = worldIn.getBlockState(pos.up()).getBlock();
      state = state.withProperty((IProperty)SNOWY, Boolean.valueOf(!(block != Blocks.SNOW && block != Blocks.SNOW_LAYER)));
    } 
    return state;
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    tab.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
    tab.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
    tab.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this, 1, ((DirtType)state.getValue((IProperty)VARIANT)).getMetadata());
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, DirtType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((DirtType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT, (IProperty)SNOWY });
  }
  
  public int damageDropped(IBlockState state) {
    DirtType blockdirt$dirttype = (DirtType)state.getValue((IProperty)VARIANT);
    if (blockdirt$dirttype == DirtType.PODZOL)
      blockdirt$dirttype = DirtType.DIRT; 
    return blockdirt$dirttype.getMetadata();
  }
  
  public enum DirtType implements IStringSerializable {
    DIRT(0, "dirt", "default", (String)MapColor.DIRT),
    COARSE_DIRT(1, "coarse_dirt", "coarse", (String)MapColor.DIRT),
    PODZOL(2, "podzol", MapColor.OBSIDIAN);
    
    private static final DirtType[] METADATA_LOOKUP = new DirtType[(values()).length];
    
    private final int metadata;
    
    private final String name;
    
    private final String unlocalizedName;
    
    private final MapColor color;
    
    static {
      byte b;
      int i;
      DirtType[] arrayOfDirtType;
      for (i = (arrayOfDirtType = values()).length, b = 0; b < i; ) {
        DirtType blockdirt$dirttype = arrayOfDirtType[b];
        METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
        b++;
      } 
    }
    
    DirtType(int metadataIn, String nameIn, String unlocalizedNameIn, MapColor color) {
      this.metadata = metadataIn;
      this.name = nameIn;
      this.unlocalizedName = unlocalizedNameIn;
      this.color = color;
    }
    
    public int getMetadata() {
      return this.metadata;
    }
    
    public String getUnlocalizedName() {
      return this.unlocalizedName;
    }
    
    public MapColor getColor() {
      return this.color;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static DirtType byMetadata(int metadata) {
      if (metadata < 0 || metadata >= METADATA_LOOKUP.length)
        metadata = 0; 
      return METADATA_LOOKUP[metadata];
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockDirt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */