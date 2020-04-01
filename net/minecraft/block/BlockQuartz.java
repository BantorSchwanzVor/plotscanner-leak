package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuartz extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockQuartz() {
    super(Material.ROCK);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.DEFAULT));
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    if (meta == EnumType.LINES_Y.getMetadata())
      switch (facing.getAxis()) {
        case Z:
          return getDefaultState().withProperty((IProperty)VARIANT, EnumType.LINES_Z);
        case null:
          return getDefaultState().withProperty((IProperty)VARIANT, EnumType.LINES_X);
        case Y:
          return getDefaultState().withProperty((IProperty)VARIANT, EnumType.LINES_Y);
      }  
    return (meta == EnumType.CHISELED.getMetadata()) ? getDefaultState().withProperty((IProperty)VARIANT, EnumType.CHISELED) : getDefaultState().withProperty((IProperty)VARIANT, EnumType.DEFAULT);
  }
  
  public int damageDropped(IBlockState state) {
    EnumType blockquartz$enumtype = (EnumType)state.getValue((IProperty)VARIANT);
    return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? blockquartz$enumtype.getMetadata() : EnumType.LINES_Y.getMetadata();
  }
  
  protected ItemStack getSilkTouchDrop(IBlockState state) {
    EnumType blockquartz$enumtype = (EnumType)state.getValue((IProperty)VARIANT);
    return (blockquartz$enumtype != EnumType.LINES_X && blockquartz$enumtype != EnumType.LINES_Z) ? super.getSilkTouchDrop(state) : new ItemStack(Item.getItemFromBlock(this), 1, EnumType.LINES_Y.getMetadata());
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    tab.add(new ItemStack(this, 1, EnumType.DEFAULT.getMetadata()));
    tab.add(new ItemStack(this, 1, EnumType.CHISELED.getMetadata()));
    tab.add(new ItemStack(this, 1, EnumType.LINES_Y.getMetadata()));
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.QUARTZ;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_90:
      case COUNTERCLOCKWISE_90:
        switch ((EnumType)state.getValue((IProperty)VARIANT)) {
          case LINES_X:
            return state.withProperty((IProperty)VARIANT, EnumType.LINES_Z);
          case LINES_Z:
            return state.withProperty((IProperty)VARIANT, EnumType.LINES_X);
        } 
        return state;
    } 
    return state;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public enum EnumType implements IStringSerializable {
    DEFAULT(0, "default", "default"),
    CHISELED(1, "chiseled", "chiseled"),
    LINES_Y(2, "lines_y", "lines"),
    LINES_X(3, "lines_x", "lines"),
    LINES_Z(4, "lines_z", "lines");
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String serializedName;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockquartz$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockquartz$enumtype.getMetadata()] = blockquartz$enumtype;
        b++;
      } 
    }
    
    EnumType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.serializedName = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public String toString() {
      return this.unlocalizedName;
    }
    
    public static EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public String getName() {
      return this.serializedName;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockQuartz.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */