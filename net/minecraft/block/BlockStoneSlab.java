package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab {
  public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
  
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockStoneSlab() {
    super(Material.ROCK);
    IBlockState iblockstate = this.blockState.getBaseState();
    if (isDouble()) {
      iblockstate = iblockstate.withProperty((IProperty)SEAMLESS, Boolean.valueOf(false));
    } else {
      iblockstate = iblockstate.withProperty((IProperty)HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    } 
    setDefaultState(iblockstate.withProperty((IProperty)VARIANT, EnumType.STONE));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(Blocks.STONE_SLAB);
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(Blocks.STONE_SLAB, 1, ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata());
  }
  
  public String getUnlocalizedName(int meta) {
    return String.valueOf(getUnlocalizedName()) + "." + EnumType.byMetadata(meta).getUnlocalizedName();
  }
  
  public IProperty<?> getVariantProperty() {
    return (IProperty<?>)VARIANT;
  }
  
  public Comparable<?> getTypeForItem(ItemStack stack) {
    return EnumType.byMetadata(stack.getMetadata() & 0x7);
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blockstoneslab$enumtype = arrayOfEnumType[b];
      if (blockstoneslab$enumtype != EnumType.WOOD)
        tab.add(new ItemStack(this, 1, blockstoneslab$enumtype.getMetadata())); 
      b++;
    } 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta & 0x7));
    if (isDouble()) {
      iblockstate = iblockstate.withProperty((IProperty)SEAMLESS, Boolean.valueOf(((meta & 0x8) != 0)));
    } else {
      iblockstate = iblockstate.withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    } 
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
    if (isDouble()) {
      if (((Boolean)state.getValue((IProperty)SEAMLESS)).booleanValue())
        i |= 0x8; 
    } else if (state.getValue((IProperty)HALF) == BlockSlab.EnumBlockHalf.TOP) {
      i |= 0x8;
    } 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return isDouble() ? new BlockStateContainer(this, new IProperty[] { (IProperty)SEAMLESS, (IProperty)VARIANT }) : new BlockStateContainer(this, new IProperty[] { (IProperty)HALF, (IProperty)VARIANT });
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMapColor();
  }
  
  public enum EnumType implements IStringSerializable {
    STONE(0, MapColor.STONE, "stone"),
    SAND(1, MapColor.SAND, "sandstone", (MapColor)"sand"),
    WOOD(2, MapColor.WOOD, "wood_old", (MapColor)"wood"),
    COBBLESTONE(3, MapColor.STONE, "cobblestone", (MapColor)"cobble"),
    BRICK(4, MapColor.RED, "brick"),
    SMOOTHBRICK(5, MapColor.STONE, "stone_brick", (MapColor)"smoothStoneBrick"),
    NETHERBRICK(6, MapColor.NETHERRACK, "nether_brick", (MapColor)"netherBrick"),
    QUARTZ(7, MapColor.QUARTZ, "quartz");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final MapColor mapColor;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockstoneslab$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockstoneslab$enumtype.getMetadata()] = blockstoneslab$enumtype;
        b++;
      } 
    }
    
    EnumType(int p_i46382_3_, MapColor p_i46382_4_, String p_i46382_5_, String p_i46382_6_) {
      this.meta = p_i46382_3_;
      this.mapColor = p_i46382_4_;
      this.name = p_i46382_5_;
      this.unlocalizedName = p_i46382_6_;
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStoneSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */