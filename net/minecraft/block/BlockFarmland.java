package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmland extends Block {
  public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
  
  protected static final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);
  
  protected static final AxisAlignedBB field_194405_c = new AxisAlignedBB(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
  
  protected BlockFarmland() {
    super(Material.GROUND);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)MOISTURE, Integer.valueOf(0)));
    setTickRandomly(true);
    setLightOpacity(255);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return FARMLAND_AABB;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    int i = ((Integer)state.getValue((IProperty)MOISTURE)).intValue();
    if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
      if (i > 0) {
        worldIn.setBlockState(pos, state.withProperty((IProperty)MOISTURE, Integer.valueOf(i - 1)), 2);
      } else if (!hasCrops(worldIn, pos)) {
        func_190970_b(worldIn, pos);
      } 
    } else if (i < 7) {
      worldIn.setBlockState(pos, state.withProperty((IProperty)MOISTURE, Integer.valueOf(7)), 2);
    } 
  }
  
  public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
    if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F && entityIn instanceof net.minecraft.entity.EntityLivingBase && (entityIn instanceof net.minecraft.entity.player.EntityPlayer || worldIn.getGameRules().getBoolean("mobGriefing")) && entityIn.width * entityIn.width * entityIn.height > 0.512F)
      func_190970_b(worldIn, pos); 
    super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
  }
  
  protected static void func_190970_b(World p_190970_0_, BlockPos p_190970_1_) {
    p_190970_0_.setBlockState(p_190970_1_, Blocks.DIRT.getDefaultState());
    AxisAlignedBB axisalignedbb = field_194405_c.offset(p_190970_1_);
    for (Entity entity : p_190970_0_.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb)) {
      double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - (entity.getEntityBoundingBox()).minY);
      entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001D, entity.posZ);
    } 
  }
  
  private boolean hasCrops(World worldIn, BlockPos pos) {
    Block block = worldIn.getBlockState(pos.up()).getBlock();
    return !(!(block instanceof BlockCrops) && !(block instanceof BlockStem));
  }
  
  private boolean hasWater(World worldIn, BlockPos pos) {
    for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
      if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos).getMaterial() == Material.WATER)
        return true; 
    } 
    return false;
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
    if (worldIn.getBlockState(pos.up()).getMaterial().isSolid())
      func_190970_b(worldIn, pos); 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    super.onBlockAdded(worldIn, pos, state);
    if (worldIn.getBlockState(pos.up()).getMaterial().isSolid())
      func_190970_b(worldIn, pos); 
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    IBlockState iblockstate;
    Block block;
    switch (side) {
      case UP:
        return true;
      case NORTH:
      case SOUTH:
      case WEST:
      case EAST:
        iblockstate = blockAccess.getBlockState(pos.offset(side));
        block = iblockstate.getBlock();
        return (!iblockstate.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH);
    } 
    return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }
  
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Blocks.DIRT.getItemDropped(Blocks.DIRT.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)MOISTURE, Integer.valueOf(meta & 0x7));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)MOISTURE)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)MOISTURE });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return (p_193383_4_ == EnumFacing.DOWN) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockFarmland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */