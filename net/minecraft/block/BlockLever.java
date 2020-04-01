package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
  public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", EnumOrientation.class);
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  protected static final AxisAlignedBB LEVER_NORTH_AABB = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.625D, 0.6875D, 0.800000011920929D, 1.0D);
  
  protected static final AxisAlignedBB LEVER_SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.0D, 0.6875D, 0.800000011920929D, 0.375D);
  
  protected static final AxisAlignedBB LEVER_WEST_AABB = new AxisAlignedBB(0.625D, 0.20000000298023224D, 0.3125D, 1.0D, 0.800000011920929D, 0.6875D);
  
  protected static final AxisAlignedBB LEVER_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3125D, 0.375D, 0.800000011920929D, 0.6875D);
  
  protected static final AxisAlignedBB LEVER_UP_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.6000000238418579D, 0.75D);
  
  protected static final AxisAlignedBB LEVER_DOWN_AABB = new AxisAlignedBB(0.25D, 0.4000000059604645D, 0.25D, 0.75D, 1.0D, 0.75D);
  
  protected BlockLever() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, EnumOrientation.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return canAttachTo(worldIn, pos, side);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (canAttachTo(worldIn, pos, enumfacing))
        return true; 
      b++;
    } 
    return false;
  }
  
  protected static boolean canAttachTo(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
    return BlockButton.canPlaceBlock(p_181090_0_, p_181090_1_, p_181090_2_);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(false));
    if (canAttachTo(worldIn, pos, facing))
      return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing())); 
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (enumfacing != facing && canAttachTo(worldIn, pos, enumfacing))
        return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing())); 
    } 
    if (worldIn.getBlockState(pos.down()).isFullyOpaque())
      return iblockstate.withProperty((IProperty)FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing())); 
    return iblockstate;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (checkCanSurvive(worldIn, pos, state) && !canAttachTo(worldIn, pos, ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing())) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  private boolean checkCanSurvive(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
    if (canPlaceBlockAt(p_181091_1_, p_181091_2_))
      return true; 
    dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
    p_181091_1_.setBlockToAir(p_181091_2_);
    return false;
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch ((EnumOrientation)state.getValue((IProperty)FACING)) {
      default:
        return LEVER_EAST_AABB;
      case WEST:
        return LEVER_WEST_AABB;
      case SOUTH:
        return LEVER_SOUTH_AABB;
      case NORTH:
        return LEVER_NORTH_AABB;
      case UP_Z:
      case UP_X:
        return LEVER_UP_AABB;
      case null:
      case DOWN_Z:
        break;
    } 
    return LEVER_DOWN_AABB;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (worldIn.isRemote)
      return true; 
    state = state.cycleProperty((IProperty)POWERED);
    worldIn.setBlockState(pos, state, 3);
    float f = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 0.6F : 0.5F;
    worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
    worldIn.notifyNeighborsOfStateChange(pos, this, false);
    EnumFacing enumfacing = ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing();
    worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
    return true;
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue()) {
      worldIn.notifyNeighborsOfStateChange(pos, this, false);
      EnumFacing enumfacing = ((EnumOrientation)state.getValue((IProperty)FACING)).getFacing();
      worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
    } 
    super.breakBlock(worldIn, pos, state);
  }
  
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return ((Boolean)blockState.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    if (!((Boolean)blockState.getValue((IProperty)POWERED)).booleanValue())
      return 0; 
    return (((EnumOrientation)blockState.getValue((IProperty)FACING)).getFacing() == side) ? 15 : 0;
  }
  
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, EnumOrientation.byMetadata(meta & 0x7)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumOrientation)state.getValue((IProperty)FACING)).getMetadata();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    return i;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case null:
        switch ((EnumOrientation)state.getValue((IProperty)FACING)) {
          case EAST:
            return state.withProperty((IProperty)FACING, EnumOrientation.WEST);
          case WEST:
            return state.withProperty((IProperty)FACING, EnumOrientation.EAST);
          case SOUTH:
            return state.withProperty((IProperty)FACING, EnumOrientation.NORTH);
          case NORTH:
            return state.withProperty((IProperty)FACING, EnumOrientation.SOUTH);
        } 
        return state;
      case COUNTERCLOCKWISE_90:
        switch ((EnumOrientation)state.getValue((IProperty)FACING)) {
          case EAST:
            return state.withProperty((IProperty)FACING, EnumOrientation.NORTH);
          case WEST:
            return state.withProperty((IProperty)FACING, EnumOrientation.SOUTH);
          case SOUTH:
            return state.withProperty((IProperty)FACING, EnumOrientation.EAST);
          case NORTH:
            return state.withProperty((IProperty)FACING, EnumOrientation.WEST);
          case UP_Z:
            return state.withProperty((IProperty)FACING, EnumOrientation.UP_X);
          case UP_X:
            return state.withProperty((IProperty)FACING, EnumOrientation.UP_Z);
          case null:
            return state.withProperty((IProperty)FACING, EnumOrientation.DOWN_Z);
          case DOWN_Z:
            return state.withProperty((IProperty)FACING, EnumOrientation.DOWN_X);
        } 
      case CLOCKWISE_90:
        switch ((EnumOrientation)state.getValue((IProperty)FACING)) {
          case EAST:
            return state.withProperty((IProperty)FACING, EnumOrientation.SOUTH);
          case WEST:
            return state.withProperty((IProperty)FACING, EnumOrientation.NORTH);
          case SOUTH:
            return state.withProperty((IProperty)FACING, EnumOrientation.WEST);
          case NORTH:
            return state.withProperty((IProperty)FACING, EnumOrientation.EAST);
          case UP_Z:
            return state.withProperty((IProperty)FACING, EnumOrientation.UP_X);
          case UP_X:
            return state.withProperty((IProperty)FACING, EnumOrientation.UP_Z);
          case null:
            return state.withProperty((IProperty)FACING, EnumOrientation.DOWN_Z);
          case DOWN_Z:
            return state.withProperty((IProperty)FACING, EnumOrientation.DOWN_X);
        } 
        break;
    } 
    return state;
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation(((EnumOrientation)state.getValue((IProperty)FACING)).getFacing()));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
  
  public enum EnumOrientation implements IStringSerializable {
    DOWN_X(0, "down_x", EnumFacing.DOWN),
    EAST(1, "east", EnumFacing.EAST),
    WEST(2, "west", EnumFacing.WEST),
    SOUTH(3, "south", EnumFacing.SOUTH),
    NORTH(4, "north", EnumFacing.NORTH),
    UP_Z(5, "up_z", EnumFacing.UP),
    UP_X(6, "up_x", EnumFacing.UP),
    DOWN_Z(7, "down_z", EnumFacing.DOWN);
    
    private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final EnumFacing facing;
    
    static {
      byte b;
      int i;
      EnumOrientation[] arrayOfEnumOrientation;
      for (i = (arrayOfEnumOrientation = values()).length, b = 0; b < i; ) {
        EnumOrientation blocklever$enumorientation = arrayOfEnumOrientation[b];
        META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
        b++;
      } 
    }
    
    EnumOrientation(int meta, String name, EnumFacing facing) {
      this.meta = meta;
      this.name = name;
      this.facing = facing;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public EnumFacing getFacing() {
      return this.facing;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumOrientation byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
      switch (clickedSide) {
        case null:
          switch (entityFacing.getAxis()) {
            case null:
              return DOWN_X;
            case Z:
              return DOWN_Z;
          } 
          throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
        case UP:
          switch (entityFacing.getAxis()) {
            case null:
              return UP_X;
            case Z:
              return UP_Z;
          } 
          throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
        case NORTH:
          return NORTH;
        case SOUTH:
          return SOUTH;
        case WEST:
          return WEST;
        case EAST:
          return EAST;
      } 
      throw new IllegalArgumentException("Invalid facing: " + clickedSide);
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockLever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */