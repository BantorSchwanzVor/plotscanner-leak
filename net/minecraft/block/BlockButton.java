package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton extends BlockDirectional {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  protected static final AxisAlignedBB AABB_DOWN_OFF = new AxisAlignedBB(0.3125D, 0.875D, 0.375D, 0.6875D, 1.0D, 0.625D);
  
  protected static final AxisAlignedBB AABB_UP_OFF = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.125D, 0.625D);
  
  protected static final AxisAlignedBB AABB_NORTH_OFF = new AxisAlignedBB(0.3125D, 0.375D, 0.875D, 0.6875D, 0.625D, 1.0D);
  
  protected static final AxisAlignedBB AABB_SOUTH_OFF = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
  
  protected static final AxisAlignedBB AABB_WEST_OFF = new AxisAlignedBB(0.875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
  
  protected static final AxisAlignedBB AABB_EAST_OFF = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.125D, 0.625D, 0.6875D);
  
  protected static final AxisAlignedBB AABB_DOWN_ON = new AxisAlignedBB(0.3125D, 0.9375D, 0.375D, 0.6875D, 1.0D, 0.625D);
  
  protected static final AxisAlignedBB AABB_UP_ON = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.0625D, 0.625D);
  
  protected static final AxisAlignedBB AABB_NORTH_ON = new AxisAlignedBB(0.3125D, 0.375D, 0.9375D, 0.6875D, 0.625D, 1.0D);
  
  protected static final AxisAlignedBB AABB_SOUTH_ON = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.0625D);
  
  protected static final AxisAlignedBB AABB_WEST_ON = new AxisAlignedBB(0.9375D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
  
  protected static final AxisAlignedBB AABB_EAST_ON = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.0625D, 0.625D, 0.6875D);
  
  private final boolean wooden;
  
  protected BlockButton(boolean wooden) {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.REDSTONE);
    this.wooden = wooden;
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  public int tickRate(World worldIn) {
    return this.wooden ? 30 : 20;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
    return canPlaceBlock(worldIn, pos, side);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (canPlaceBlock(worldIn, pos, enumfacing))
        return true; 
      b++;
    } 
    return false;
  }
  
  protected static boolean canPlaceBlock(World worldIn, BlockPos pos, EnumFacing direction) {
    BlockPos blockpos = pos.offset(direction.getOpposite());
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    boolean flag = (iblockstate.func_193401_d((IBlockAccess)worldIn, blockpos, direction) == BlockFaceShape.SOLID);
    Block block = iblockstate.getBlock();
    if (direction == EnumFacing.UP)
      return !(block != Blocks.HOPPER && (func_193384_b(block) || !flag)); 
    return (!func_193382_c(block) && flag);
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return canPlaceBlock(worldIn, pos, facing) ? getDefaultState().withProperty((IProperty)FACING, (Comparable)facing).withProperty((IProperty)POWERED, Boolean.valueOf(false)) : getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.DOWN).withProperty((IProperty)POWERED, Boolean.valueOf(false));
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (checkForDrop(worldIn, pos, state) && !canPlaceBlock(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING))) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
  }
  
  private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
    if (canPlaceBlockAt(worldIn, pos))
      return true; 
    dropBlockAsItem(worldIn, pos, state, 0);
    worldIn.setBlockToAir(pos);
    return false;
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    boolean flag = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
    switch (enumfacing) {
      case EAST:
        return flag ? AABB_EAST_ON : AABB_EAST_OFF;
      case WEST:
        return flag ? AABB_WEST_ON : AABB_WEST_OFF;
      case SOUTH:
        return flag ? AABB_SOUTH_ON : AABB_SOUTH_OFF;
      default:
        return flag ? AABB_NORTH_ON : AABB_NORTH_OFF;
      case UP:
        return flag ? AABB_UP_ON : AABB_UP_OFF;
      case null:
        break;
    } 
    return flag ? AABB_DOWN_ON : AABB_DOWN_OFF;
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      return true; 
    worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(true)), 3);
    worldIn.markBlockRangeForRenderUpdate(pos, pos);
    playClickSound(playerIn, worldIn, pos);
    notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
    worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    return true;
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING)); 
    super.breakBlock(worldIn, pos, state);
  }
  
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return ((Boolean)blockState.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
  }
  
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    if (!((Boolean)blockState.getValue((IProperty)POWERED)).booleanValue())
      return 0; 
    return (blockState.getValue((IProperty)FACING) == side) ? 15 : 0;
  }
  
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (!worldIn.isRemote)
      if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
        if (this.wooden) {
          checkPressed(state, worldIn, pos);
        } else {
          worldIn.setBlockState(pos, state.withProperty((IProperty)POWERED, Boolean.valueOf(false)));
          notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING));
          playReleaseSound(worldIn, pos);
          worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }   
  }
  
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    if (!worldIn.isRemote)
      if (this.wooden)
        if (!((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
          checkPressed(state, worldIn, pos);   
  }
  
  private void checkPressed(IBlockState p_185616_1_, World p_185616_2_, BlockPos p_185616_3_) {
    List<? extends Entity> list = p_185616_2_.getEntitiesWithinAABB(EntityArrow.class, p_185616_1_.getBoundingBox((IBlockAccess)p_185616_2_, p_185616_3_).offset(p_185616_3_));
    boolean flag = !list.isEmpty();
    boolean flag1 = ((Boolean)p_185616_1_.getValue((IProperty)POWERED)).booleanValue();
    if (flag && !flag1) {
      p_185616_2_.setBlockState(p_185616_3_, p_185616_1_.withProperty((IProperty)POWERED, Boolean.valueOf(true)));
      notifyNeighbors(p_185616_2_, p_185616_3_, (EnumFacing)p_185616_1_.getValue((IProperty)FACING));
      p_185616_2_.markBlockRangeForRenderUpdate(p_185616_3_, p_185616_3_);
      playClickSound((EntityPlayer)null, p_185616_2_, p_185616_3_);
    } 
    if (!flag && flag1) {
      p_185616_2_.setBlockState(p_185616_3_, p_185616_1_.withProperty((IProperty)POWERED, Boolean.valueOf(false)));
      notifyNeighbors(p_185616_2_, p_185616_3_, (EnumFacing)p_185616_1_.getValue((IProperty)FACING));
      p_185616_2_.markBlockRangeForRenderUpdate(p_185616_3_, p_185616_3_);
      playReleaseSound(p_185616_2_, p_185616_3_);
    } 
    if (flag)
      p_185616_2_.scheduleUpdate(new BlockPos((Vec3i)p_185616_3_), this, tickRate(p_185616_2_)); 
  }
  
  private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing) {
    worldIn.notifyNeighborsOfStateChange(pos, this, false);
    worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this, false);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing enumfacing;
    switch (meta & 0x7) {
      case 0:
        enumfacing = EnumFacing.DOWN;
        break;
      case 1:
        enumfacing = EnumFacing.EAST;
        break;
      case 2:
        enumfacing = EnumFacing.WEST;
        break;
      case 3:
        enumfacing = EnumFacing.SOUTH;
        break;
      case 4:
        enumfacing = EnumFacing.NORTH;
        break;
      default:
        enumfacing = EnumFacing.UP;
        break;
    } 
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i;
    switch ((EnumFacing)state.getValue((IProperty)FACING)) {
      case EAST:
        i = 1;
        break;
      case WEST:
        i = 2;
        break;
      case SOUTH:
        i = 3;
        break;
      case NORTH:
        i = 4;
        break;
      default:
        i = 5;
        break;
      case null:
        i = 0;
        break;
    } 
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
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
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
  
  protected abstract void playClickSound(EntityPlayer paramEntityPlayer, World paramWorld, BlockPos paramBlockPos);
  
  protected abstract void playReleaseSound(World paramWorld, BlockPos paramBlockPos);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */