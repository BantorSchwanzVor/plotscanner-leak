package net.minecraft.block;

import com.google.common.base.MoreObjects;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTripWireHook extends Block {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  public static final PropertyBool ATTACHED = PropertyBool.create("attached");
  
  protected static final AxisAlignedBB HOOK_NORTH_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.625D, 0.6875D, 0.625D, 1.0D);
  
  protected static final AxisAlignedBB HOOK_SOUTH_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.625D, 0.375D);
  
  protected static final AxisAlignedBB HOOK_WEST_AABB = new AxisAlignedBB(0.625D, 0.0D, 0.3125D, 1.0D, 0.625D, 0.6875D);
  
  protected static final AxisAlignedBB HOOK_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 0.375D, 0.625D, 0.6875D);
  
  public BlockTripWireHook() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH).withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false)));
    setCreativeTab(CreativeTabs.REDSTONE);
    setTickRandomly(true);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch ((EnumFacing)state.getValue((IProperty)FACING)) {
      default:
        return HOOK_EAST_AABB;
      case WEST:
        return HOOK_WEST_AABB;
      case SOUTH:
        return HOOK_SOUTH_AABB;
      case NORTH:
        break;
    } 
    return HOOK_NORTH_AABB;
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
    EnumFacing enumfacing = side.getOpposite();
    BlockPos blockpos = pos.offset(enumfacing);
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    boolean flag = func_193382_c(iblockstate.getBlock());
    return (!flag && side.getAxis().isHorizontal() && iblockstate.func_193401_d((IBlockAccess)worldIn, blockpos, side) == BlockFaceShape.SOLID && !iblockstate.canProvidePower());
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (canPlaceBlockOnSide(worldIn, pos, enumfacing))
        return true; 
    } 
    return false;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    IBlockState iblockstate = getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf(false)).withProperty((IProperty)ATTACHED, Boolean.valueOf(false));
    if (facing.getAxis().isHorizontal())
      iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)facing); 
    return iblockstate;
  }
  
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    calculateState(worldIn, pos, state, false, false, -1, (IBlockState)null);
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (blockIn != this)
      if (checkForDrop(worldIn, pos, state)) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        if (!canPlaceBlockOnSide(worldIn, pos, enumfacing)) {
          dropBlockAsItem(worldIn, pos, state, 0);
          worldIn.setBlockToAir(pos);
        } 
      }  
  }
  
  public void calculateState(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, @Nullable IBlockState p_176260_7_) {
    int m;
    EnumFacing enumfacing = (EnumFacing)hookState.getValue((IProperty)FACING);
    int flag = ((Boolean)hookState.getValue((IProperty)ATTACHED)).booleanValue();
    boolean flag1 = ((Boolean)hookState.getValue((IProperty)POWERED)).booleanValue();
    boolean flag2 = !p_176260_4_;
    boolean flag3 = false;
    int i = 0;
    IBlockState[] aiblockstate = new IBlockState[42];
    for (int j = 1; j < 42; j++) {
      BlockPos blockpos = pos.offset(enumfacing, j);
      IBlockState iblockstate = worldIn.getBlockState(blockpos);
      if (iblockstate.getBlock() == Blocks.TRIPWIRE_HOOK) {
        if (iblockstate.getValue((IProperty)FACING) == enumfacing.getOpposite())
          i = j; 
        break;
      } 
      if (iblockstate.getBlock() != Blocks.TRIPWIRE && j != p_176260_6_) {
        aiblockstate[j] = null;
        flag2 = false;
      } else {
        if (j == p_176260_6_)
          iblockstate = (IBlockState)MoreObjects.firstNonNull(p_176260_7_, iblockstate); 
        boolean flag4 = !((Boolean)iblockstate.getValue((IProperty)BlockTripWire.DISARMED)).booleanValue();
        boolean flag5 = ((Boolean)iblockstate.getValue((IProperty)BlockTripWire.POWERED)).booleanValue();
        m = flag3 | ((flag4 && flag5) ? 1 : 0);
        aiblockstate[j] = iblockstate;
        if (j == p_176260_6_) {
          worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
          flag2 &= flag4;
        } 
      } 
    } 
    int k = flag2 & ((i > 1) ? 1 : 0);
    m &= k;
    IBlockState iblockstate1 = getDefaultState().withProperty((IProperty)ATTACHED, Boolean.valueOf(k)).withProperty((IProperty)POWERED, Boolean.valueOf(m));
    if (i > 0) {
      BlockPos blockpos1 = pos.offset(enumfacing, i);
      EnumFacing enumfacing1 = enumfacing.getOpposite();
      worldIn.setBlockState(blockpos1, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing1), 3);
      notifyNeighbors(worldIn, blockpos1, enumfacing1);
      playSound(worldIn, blockpos1, k, m, flag, flag1);
    } 
    playSound(worldIn, pos, k, m, flag, flag1);
    if (!p_176260_4_) {
      worldIn.setBlockState(pos, iblockstate1.withProperty((IProperty)FACING, (Comparable)enumfacing), 3);
      if (p_176260_5_)
        notifyNeighbors(worldIn, pos, enumfacing); 
    } 
    if (flag != k)
      for (int n = 1; n < i; n++) {
        BlockPos blockpos2 = pos.offset(enumfacing, n);
        IBlockState iblockstate2 = aiblockstate[n];
        if (iblockstate2 != null && worldIn.getBlockState(blockpos2).getMaterial() != Material.AIR)
          worldIn.setBlockState(blockpos2, iblockstate2.withProperty((IProperty)ATTACHED, Boolean.valueOf(k)), 3); 
      }  
  }
  
  public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    calculateState(worldIn, pos, state, false, true, -1, (IBlockState)null);
  }
  
  private void playSound(World worldIn, BlockPos pos, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_) {
    if (p_180694_4_ && !p_180694_6_) {
      worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_ON, SoundCategory.BLOCKS, 0.4F, 0.6F);
    } else if (!p_180694_4_ && p_180694_6_) {
      worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, SoundCategory.BLOCKS, 0.4F, 0.5F);
    } else if (p_180694_3_ && !p_180694_5_) {
      worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_ATTACH, SoundCategory.BLOCKS, 0.4F, 0.7F);
    } else if (!p_180694_3_ && p_180694_5_) {
      worldIn.playSound(null, pos, SoundEvents.BLOCK_TRIPWIRE_DETACH, SoundCategory.BLOCKS, 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
    } 
  }
  
  private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing side) {
    worldIn.notifyNeighborsOfStateChange(pos, this, false);
    worldIn.notifyNeighborsOfStateChange(pos.offset(side.getOpposite()), this, false);
  }
  
  private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
    if (!canPlaceBlockAt(worldIn, pos)) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
      return false;
    } 
    return true;
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    boolean flag = ((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue();
    boolean flag1 = ((Boolean)state.getValue((IProperty)POWERED)).booleanValue();
    if (flag || flag1)
      calculateState(worldIn, pos, state, true, false, -1, (IBlockState)null); 
    if (flag1) {
      worldIn.notifyNeighborsOfStateChange(pos, this, false);
      worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue((IProperty)FACING)).getOpposite()), this, false);
    } 
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
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT_MIPPED;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta & 0x3)).withProperty((IProperty)POWERED, Boolean.valueOf(((meta & 0x8) > 0))).withProperty((IProperty)ATTACHED, Boolean.valueOf(((meta & 0x4) > 0)));
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    if (((Boolean)state.getValue((IProperty)POWERED)).booleanValue())
      i |= 0x8; 
    if (((Boolean)state.getValue((IProperty)ATTACHED)).booleanValue())
      i |= 0x4; 
    return i;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING, (IProperty)POWERED, (IProperty)ATTACHED });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockTripWireHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */