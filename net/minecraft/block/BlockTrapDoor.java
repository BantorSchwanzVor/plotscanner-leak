package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public static final PropertyBool OPEN = PropertyBool.create("open");
  
  public static final PropertyEnum<DoorHalf> HALF = PropertyEnum.create("half", DoorHalf.class);
  
  protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
  
  protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
  
  protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected BlockTrapDoor(Material materialIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)HALF, DoorHalf.BOTTOM));
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    AxisAlignedBB axisalignedbb;
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
      switch ((EnumFacing)state.getValue((IProperty)FACING)) {
        default:
          axisalignedbb = NORTH_OPEN_AABB;
          return axisalignedbb;
        case SOUTH:
          axisalignedbb = SOUTH_OPEN_AABB;
          return axisalignedbb;
        case WEST:
          axisalignedbb = WEST_OPEN_AABB;
          return axisalignedbb;
        case EAST:
          break;
      } 
      axisalignedbb = EAST_OPEN_AABB;
    } else if (state.getValue((IProperty)HALF) == DoorHalf.TOP) {
      axisalignedbb = TOP_AABB;
    } else {
      axisalignedbb = BOTTOM_AABB;
    } 
    return axisalignedbb;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return !((Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN)).booleanValue();
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (this.blockMaterial == Material.IRON)
      return false; 
    state = state.cycleProperty((IProperty)OPEN);
    worldIn.setBlockState(pos, state, 2);
    playSound(playerIn, worldIn, pos, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue());
    return true;
  }
  
  protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_) {
    if (p_185731_4_) {
      int i = (this.blockMaterial == Material.IRON) ? 1037 : 1007;
      worldIn.playEvent(player, i, pos, 0);
    } else {
      int j = (this.blockMaterial == Material.IRON) ? 1036 : 1013;
      worldIn.playEvent(player, j, pos, 0);
    } 
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.isRemote) {
      boolean flag = worldIn.isBlockPowered(pos);
      if (flag || blockIn.getDefaultState().canProvidePower()) {
        boolean flag1 = ((Boolean)state.getValue((IProperty)OPEN)).booleanValue();
        if (flag1 != flag) {
          worldIn.setBlockState(pos, state.withProperty((IProperty)OPEN, Boolean.valueOf(flag)), 2);
          playSound((EntityPlayer)null, worldIn, pos, flag);
        } 
      } 
    } 
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState();
    if (facing.getAxis().isHorizontal()) {
      iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)OPEN, Boolean.valueOf(false));
      iblockstate = iblockstate.withProperty((IProperty)HALF, (hitY > 0.5F) ? DoorHalf.TOP : DoorHalf.BOTTOM);
    } else {
      iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite()).withProperty((IProperty)OPEN, Boolean.valueOf(false));
      iblockstate = iblockstate.withProperty((IProperty)HALF, (facing == EnumFacing.UP) ? DoorHalf.BOTTOM : DoorHalf.TOP);
    } 
    if (worldIn.isBlockPowered(pos))
      iblockstate = iblockstate.withProperty((IProperty)OPEN, Boolean.valueOf(true)); 
    return iblockstate;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return true;
  }
  
  protected static EnumFacing getFacing(int meta) {
    switch (meta & 0x3) {
      case 0:
        return EnumFacing.NORTH;
      case 1:
        return EnumFacing.SOUTH;
      case 2:
        return EnumFacing.WEST;
    } 
    return EnumFacing.EAST;
  }
  
  protected static int getMetaForFacing(EnumFacing facing) {
    switch (facing) {
      case NORTH:
        return 0;
      case SOUTH:
        return 1;
      case WEST:
        return 2;
    } 
    return 3;
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)getFacing(meta)).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) != 0))).withProperty((IProperty)HALF, ((meta & 0x8) == 0) ? DoorHalf.BOTTOM : DoorHalf.TOP);
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= getMetaForFacing((EnumFacing)state.getValue((IProperty)FACING));
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
      i |= 0x4; 
    if (state.getValue((IProperty)HALF) == DoorHalf.TOP)
      i |= 0x8; 
    return i;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)OPEN, (IProperty)HALF });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (((p_193383_4_ == EnumFacing.UP && p_193383_2_.getValue((IProperty)HALF) == DoorHalf.TOP) || (p_193383_4_ == EnumFacing.DOWN && p_193383_2_.getValue((IProperty)HALF) == DoorHalf.BOTTOM)) && !((Boolean)p_193383_2_.getValue((IProperty)OPEN)).booleanValue()) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
  
  public enum DoorHalf implements IStringSerializable {
    TOP("top"),
    BOTTOM("bottom");
    
    private final String name;
    
    DoorHalf(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockTrapDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */