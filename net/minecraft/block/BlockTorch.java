package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorch extends Block {
  public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
        public boolean apply(@Nullable EnumFacing p_apply_1_) {
          return (p_apply_1_ != EnumFacing.DOWN);
        }
      });
  
  protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
  
  protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
  
  protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
  
  protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
  
  protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);
  
  protected BlockTorch() {
    super(Material.CIRCUITS);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.UP));
    setTickRandomly(true);
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    switch ((EnumFacing)state.getValue((IProperty)FACING)) {
      case EAST:
        return TORCH_EAST_AABB;
      case WEST:
        return TORCH_WEST_AABB;
      case SOUTH:
        return TORCH_SOUTH_AABB;
      case NORTH:
        return TORCH_NORTH_AABB;
    } 
    return STANDING_AABB;
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
  
  private boolean canPlaceOn(World worldIn, BlockPos pos) {
    Block block = worldIn.getBlockState(pos).getBlock();
    boolean flag = !(block != Blocks.END_GATEWAY && block != Blocks.LIT_PUMPKIN);
    if (worldIn.getBlockState(pos).isFullyOpaque())
      return !flag; 
    boolean flag1 = !(!(block instanceof BlockFence) && block != Blocks.GLASS && block != Blocks.COBBLESTONE_WALL && block != Blocks.STAINED_GLASS);
    return (flag1 && !flag);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    for (EnumFacing enumfacing : FACING.getAllowedValues()) {
      if (canPlaceAt(worldIn, pos, enumfacing))
        return true; 
    } 
    return false;
  }
  
  private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
    BlockPos blockpos = pos.offset(facing.getOpposite());
    IBlockState iblockstate = worldIn.getBlockState(blockpos);
    Block block = iblockstate.getBlock();
    BlockFaceShape blockfaceshape = iblockstate.func_193401_d((IBlockAccess)worldIn, blockpos, facing);
    if (facing.equals(EnumFacing.UP) && canPlaceOn(worldIn, blockpos))
      return true; 
    if (facing != EnumFacing.UP && facing != EnumFacing.DOWN)
      return (!func_193382_c(block) && blockfaceshape == BlockFaceShape.SOLID); 
    return false;
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    if (canPlaceAt(worldIn, pos, facing))
      return getDefaultState().withProperty((IProperty)FACING, (Comparable)facing); 
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      if (canPlaceAt(worldIn, pos, enumfacing))
        return getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing); 
    } 
    return getDefaultState();
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    checkForDrop(worldIn, pos, state);
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    onNeighborChangeInternal(worldIn, pos, state);
  }
  
  protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
    if (!checkForDrop(worldIn, pos, state))
      return true; 
    EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
    EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
    EnumFacing enumfacing1 = enumfacing.getOpposite();
    BlockPos blockpos = pos.offset(enumfacing1);
    boolean flag = false;
    if (enumfacing$axis.isHorizontal() && worldIn.getBlockState(blockpos).func_193401_d((IBlockAccess)worldIn, blockpos, enumfacing) != BlockFaceShape.SOLID) {
      flag = true;
    } else if (enumfacing$axis.isVertical() && !canPlaceOn(worldIn, blockpos)) {
      flag = true;
    } 
    if (flag) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
      return true;
    } 
    return false;
  }
  
  protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
    if (state.getBlock() == this && canPlaceAt(worldIn, pos, (EnumFacing)state.getValue((IProperty)FACING)))
      return true; 
    if (worldIn.getBlockState(pos).getBlock() == this) {
      dropBlockAsItem(worldIn, pos, state, 0);
      worldIn.setBlockToAir(pos);
    } 
    return false;
  }
  
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    EnumFacing enumfacing = (EnumFacing)stateIn.getValue((IProperty)FACING);
    double d0 = pos.getX() + 0.5D;
    double d1 = pos.getY() + 0.7D;
    double d2 = pos.getZ() + 0.5D;
    double d3 = 0.22D;
    double d4 = 0.27D;
    if (enumfacing.getAxis().isHorizontal()) {
      EnumFacing enumfacing1 = enumfacing.getOpposite();
      worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
      worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.27D * enumfacing1.getFrontOffsetX(), d1 + 0.22D, d2 + 0.27D * enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
    } else {
      worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
      worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = getDefaultState();
    switch (meta) {
      case 1:
        iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.EAST);
        return iblockstate;
      case 2:
        iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.WEST);
        return iblockstate;
      case 3:
        iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.SOUTH);
        return iblockstate;
      case 4:
        iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH);
        return iblockstate;
    } 
    iblockstate = iblockstate.withProperty((IProperty)FACING, (Comparable)EnumFacing.UP);
    return iblockstate;
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    switch ((EnumFacing)state.getValue((IProperty)FACING)) {
      case EAST:
        i |= 0x1;
        return i;
      case WEST:
        i |= 0x2;
        return i;
      case SOUTH:
        i |= 0x3;
        return i;
      case NORTH:
        i |= 0x4;
        return i;
    } 
    i |= 0x5;
    return i;
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockTorch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */