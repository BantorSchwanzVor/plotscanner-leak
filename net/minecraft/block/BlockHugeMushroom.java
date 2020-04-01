package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  private final Block smallBlock;
  
  public BlockHugeMushroom(Material materialIn, MapColor color, Block smallBlockIn) {
    super(materialIn, color);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.ALL_OUTSIDE));
    this.smallBlock = smallBlockIn;
  }
  
  public int quantityDropped(Random random) {
    return Math.max(0, random.nextInt(10) - 7);
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    switch ((EnumType)state.getValue((IProperty)VARIANT)) {
      case ALL_STEM:
        return MapColor.CLOTH;
      case null:
        return MapColor.SAND;
      case STEM:
        return MapColor.SAND;
    } 
    return super.getMapColor(state, p_180659_2_, p_180659_3_);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(this.smallBlock);
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this.smallBlock);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState();
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case null:
        switch ((EnumType)state.getValue((IProperty)VARIANT)) {
          case STEM:
            break;
          case NORTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_EAST);
          case NORTH:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH);
          case NORTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_WEST);
          case WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.EAST);
          case EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.WEST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_EAST);
          case SOUTH:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH);
          case SOUTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_WEST);
          default:
            return state;
        } 
      case COUNTERCLOCKWISE_90:
        switch ((EnumType)state.getValue((IProperty)VARIANT)) {
          case STEM:
            break;
          case NORTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_WEST);
          case NORTH:
            return state.withProperty((IProperty)VARIANT, EnumType.WEST);
          case NORTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_WEST);
          case WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH);
          case EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH);
          case SOUTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_EAST);
          case SOUTH:
            return state.withProperty((IProperty)VARIANT, EnumType.EAST);
          case SOUTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_EAST);
          default:
            return state;
        } 
      case CLOCKWISE_90:
        switch ((EnumType)state.getValue((IProperty)VARIANT)) {
          case STEM:
            break;
          case NORTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_EAST);
          case NORTH:
            return state.withProperty((IProperty)VARIANT, EnumType.EAST);
          case NORTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_EAST);
          case WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH);
          case EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH);
          case SOUTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_WEST);
          case SOUTH:
            return state.withProperty((IProperty)VARIANT, EnumType.WEST);
          case SOUTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_WEST);
        } 
        return state;
    } 
    return state;
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    EnumType blockhugemushroom$enumtype = (EnumType)state.getValue((IProperty)VARIANT);
    switch (mirrorIn) {
      case LEFT_RIGHT:
        switch (blockhugemushroom$enumtype) {
          case NORTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_WEST);
          case NORTH:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH);
          case NORTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_EAST);
          default:
            return super.withMirror(state, mirrorIn);
          case SOUTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_WEST);
          case SOUTH:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH);
          case SOUTH_EAST:
            break;
        } 
        return state.withProperty((IProperty)VARIANT, EnumType.NORTH_EAST);
      case null:
        switch (blockhugemushroom$enumtype) {
          case NORTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_EAST);
          default:
            break;
          case NORTH_EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.NORTH_WEST);
          case WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.EAST);
          case EAST:
            return state.withProperty((IProperty)VARIANT, EnumType.WEST);
          case SOUTH_WEST:
            return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_EAST);
          case SOUTH_EAST:
            break;
        } 
        return state.withProperty((IProperty)VARIANT, EnumType.SOUTH_WEST);
    } 
    return super.withMirror(state, mirrorIn);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public enum EnumType implements IStringSerializable {
    NORTH_WEST(1, "north_west"),
    NORTH(2, "north"),
    NORTH_EAST(3, "north_east"),
    WEST(4, "west"),
    CENTER(5, "center"),
    EAST(6, "east"),
    SOUTH_WEST(7, "south_west"),
    SOUTH(8, "south"),
    SOUTH_EAST(9, "south_east"),
    STEM(10, "stem"),
    ALL_INSIDE(0, "all_inside"),
    ALL_OUTSIDE(14, "all_outside"),
    ALL_STEM(15, "all_stem");
    
    private static final EnumType[] META_LOOKUP = new EnumType[16];
    
    private final int meta;
    
    private final String name;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blockhugemushroom$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype;
        b++;
      } 
    }
    
    EnumType(int meta, String name) {
      this.meta = meta;
      this.name = name;
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
      EnumType blockhugemushroom$enumtype = META_LOOKUP[meta];
      return (blockhugemushroom$enumtype == null) ? META_LOOKUP[0] : blockhugemushroom$enumtype;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockHugeMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */