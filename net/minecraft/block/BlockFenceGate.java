package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockHorizontal {
  public static final PropertyBool OPEN = PropertyBool.create("open");
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
  
  protected static final AxisAlignedBB AABB_COLLIDE_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
  
  protected static final AxisAlignedBB AABB_COLLIDE_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
  
  protected static final AxisAlignedBB AABB_COLLIDE_ZAXIS_INWALL = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
  
  protected static final AxisAlignedBB AABB_COLLIDE_XAXIS_INWALL = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);
  
  protected static final AxisAlignedBB AABB_CLOSED_SELECTED_ZAXIS = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 1.0D, 1.5D, 0.625D);
  
  protected static final AxisAlignedBB AABB_CLOSED_SELECTED_XAXIS = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.5D, 1.0D);
  
  public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_) {
    super(Material.WOOD, p_i46394_1_.getMapColor());
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)OPEN, Boolean.valueOf(false)).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)IN_WALL, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    state = getActualState(state, source, pos);
    if (((Boolean)state.getValue((IProperty)IN_WALL)).booleanValue())
      return (((EnumFacing)state.getValue((IProperty)FACING)).getAxis() == EnumFacing.Axis.X) ? AABB_COLLIDE_XAXIS_INWALL : AABB_COLLIDE_ZAXIS_INWALL; 
    return (((EnumFacing)state.getValue((IProperty)FACING)).getAxis() == EnumFacing.Axis.X) ? AABB_COLLIDE_XAXIS : AABB_COLLIDE_ZAXIS;
  }
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue((IProperty)FACING)).getAxis();
    if ((enumfacing$axis == EnumFacing.Axis.Z && (worldIn.getBlockState(pos.west()).getBlock() == Blocks.COBBLESTONE_WALL || worldIn.getBlockState(pos.east()).getBlock() == Blocks.COBBLESTONE_WALL)) || (enumfacing$axis == EnumFacing.Axis.X && (worldIn.getBlockState(pos.north()).getBlock() == Blocks.COBBLESTONE_WALL || worldIn.getBlockState(pos.south()).getBlock() == Blocks.COBBLESTONE_WALL)))
      state = state.withProperty((IProperty)IN_WALL, Boolean.valueOf(true)); 
    return state;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return worldIn.getBlockState(pos.down()).getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    if (((Boolean)blockState.getValue((IProperty)OPEN)).booleanValue())
      return NULL_AABB; 
    return (((EnumFacing)blockState.getValue((IProperty)FACING)).getAxis() == EnumFacing.Axis.Z) ? AABB_CLOSED_SELECTED_ZAXIS : AABB_CLOSED_SELECTED_XAXIS;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return ((Boolean)worldIn.getBlockState(pos).getValue((IProperty)OPEN)).booleanValue();
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    boolean flag = worldIn.isBlockPowered(pos);
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing()).withProperty((IProperty)OPEN, Boolean.valueOf(flag)).withProperty((IProperty)POWERED, Boolean.valueOf(flag)).withProperty((IProperty)IN_WALL, Boolean.valueOf(false));
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue()) {
      state = state.withProperty((IProperty)OPEN, Boolean.valueOf(false));
      worldIn.setBlockState(pos, state, 10);
    } else {
      EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
      if (state.getValue((IProperty)FACING) == enumfacing.getOpposite())
        state = state.withProperty((IProperty)FACING, (Comparable)enumfacing); 
      state = state.withProperty((IProperty)OPEN, Boolean.valueOf(true));
      worldIn.setBlockState(pos, state, 10);
    } 
    worldIn.playEvent(playerIn, ((Boolean)state.getValue((IProperty)OPEN)).booleanValue() ? 1008 : 1014, pos, 0);
    return true;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.isRemote) {
      boolean flag = worldIn.isBlockPowered(pos);
      if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue() != flag) {
        worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(flag)).withProperty((IProperty)OPEN, Boolean.valueOf(flag)), 2);
        if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue() != flag)
          worldIn.playEvent(null, flag ? 1008 : 1014, pos, 0); 
      } 
    } 
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return true;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta)).withProperty((IProperty)OPEN, Boolean.valueOf(((meta & 0x4) != 0))).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) != 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    if (((Boolean)state.getValue((IProperty)OPEN)).booleanValue())
      i |= 0x4; 
    return i;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)OPEN, (IProperty)POWERED, (IProperty)IN_WALL });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    if (p_193383_4_ != EnumFacing.UP && p_193383_4_ != EnumFacing.DOWN)
      return (((EnumFacing)p_193383_2_.getValue((IProperty)FACING)).getAxis() == p_193383_4_.rotateY().getAxis()) ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.UNDEFINED; 
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockFenceGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */