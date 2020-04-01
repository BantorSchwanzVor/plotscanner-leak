package net.minecraft.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFire extends Block {
  public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
  
  public static final PropertyBool NORTH = PropertyBool.create("north");
  
  public static final PropertyBool EAST = PropertyBool.create("east");
  
  public static final PropertyBool SOUTH = PropertyBool.create("south");
  
  public static final PropertyBool WEST = PropertyBool.create("west");
  
  public static final PropertyBool UPPER = PropertyBool.create("up");
  
  private final Map<Block, Integer> encouragements = Maps.newIdentityHashMap();
  
  private final Map<Block, Integer> flammabilities = Maps.newIdentityHashMap();
  
  public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !Blocks.FIRE.canCatchFire(worldIn, pos.down())) ? state.withProperty((IProperty)NORTH, Boolean.valueOf(canCatchFire(worldIn, pos.north()))).withProperty((IProperty)EAST, Boolean.valueOf(canCatchFire(worldIn, pos.east()))).withProperty((IProperty)SOUTH, Boolean.valueOf(canCatchFire(worldIn, pos.south()))).withProperty((IProperty)WEST, Boolean.valueOf(canCatchFire(worldIn, pos.west()))).withProperty((IProperty)UPPER, Boolean.valueOf(canCatchFire(worldIn, pos.up()))) : getDefaultState();
  }
  
  protected BlockFire() {
    super(Material.FIRE);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AGE, Integer.valueOf(0)).withProperty((IProperty)NORTH, Boolean.valueOf(false)).withProperty((IProperty)EAST, Boolean.valueOf(false)).withProperty((IProperty)SOUTH, Boolean.valueOf(false)).withProperty((IProperty)WEST, Boolean.valueOf(false)).withProperty((IProperty)UPPER, Boolean.valueOf(false)));
    setTickRandomly(true);
  }
  
  public static void init() {
    Blocks.FIRE.setFireInfo(Blocks.PLANKS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.DOUBLE_WOODEN_SLAB, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.WOODEN_SLAB, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE_GATE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE_GATE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE_GATE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE_GATE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE_GATE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.OAK_STAIRS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.BIRCH_STAIRS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.SPRUCE_STAIRS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.JUNGLE_STAIRS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.ACACIA_STAIRS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_STAIRS, 5, 20);
    Blocks.FIRE.setFireInfo(Blocks.LOG, 5, 5);
    Blocks.FIRE.setFireInfo(Blocks.LOG2, 5, 5);
    Blocks.FIRE.setFireInfo(Blocks.LEAVES, 30, 60);
    Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 30, 60);
    Blocks.FIRE.setFireInfo(Blocks.BOOKSHELF, 30, 20);
    Blocks.FIRE.setFireInfo(Blocks.TNT, 15, 100);
    Blocks.FIRE.setFireInfo(Blocks.TALLGRASS, 60, 100);
    Blocks.FIRE.setFireInfo(Blocks.DOUBLE_PLANT, 60, 100);
    Blocks.FIRE.setFireInfo(Blocks.YELLOW_FLOWER, 60, 100);
    Blocks.FIRE.setFireInfo(Blocks.RED_FLOWER, 60, 100);
    Blocks.FIRE.setFireInfo(Blocks.DEADBUSH, 60, 100);
    Blocks.FIRE.setFireInfo(Blocks.WOOL, 30, 60);
    Blocks.FIRE.setFireInfo(Blocks.VINE, 15, 100);
    Blocks.FIRE.setFireInfo(Blocks.COAL_BLOCK, 5, 5);
    Blocks.FIRE.setFireInfo(Blocks.HAY_BLOCK, 60, 20);
    Blocks.FIRE.setFireInfo(Blocks.CARPET, 60, 20);
  }
  
  public void setFireInfo(Block blockIn, int encouragement, int flammability) {
    this.encouragements.put(blockIn, Integer.valueOf(encouragement));
    this.flammabilities.put(blockIn, Integer.valueOf(flammability));
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
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public int tickRate(World worldIn) {
    return 30;
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (worldIn.getGameRules().getBoolean("doFireTick")) {
      if (!canPlaceBlockAt(worldIn, pos))
        worldIn.setBlockToAir(pos); 
      Block block = worldIn.getBlockState(pos.down()).getBlock();
      boolean flag = !(block != Blocks.NETHERRACK && block != Blocks.MAGMA);
      if (worldIn.provider instanceof net.minecraft.world.WorldProviderEnd && block == Blocks.BEDROCK)
        flag = true; 
      int i = ((Integer)state.getValue((IProperty)AGE)).intValue();
      if (!flag && worldIn.isRaining() && canDie(worldIn, pos) && rand.nextFloat() < 0.2F + i * 0.03F) {
        worldIn.setBlockToAir(pos);
      } else {
        if (i < 15) {
          state = state.withProperty((IProperty)AGE, Integer.valueOf(i + rand.nextInt(3) / 2));
          worldIn.setBlockState(pos, state, 4);
        } 
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + rand.nextInt(10));
        if (!flag) {
          if (!canNeighborCatchFire(worldIn, pos)) {
            if (!worldIn.getBlockState(pos.down()).isFullyOpaque() || i > 3)
              worldIn.setBlockToAir(pos); 
            return;
          } 
          if (!canCatchFire((IBlockAccess)worldIn, pos.down()) && i == 15 && rand.nextInt(4) == 0) {
            worldIn.setBlockToAir(pos);
            return;
          } 
        } 
        boolean flag1 = worldIn.isBlockinHighHumidity(pos);
        int j = 0;
        if (flag1)
          j = -50; 
        catchOnFire(worldIn, pos.east(), 300 + j, rand, i);
        catchOnFire(worldIn, pos.west(), 300 + j, rand, i);
        catchOnFire(worldIn, pos.down(), 250 + j, rand, i);
        catchOnFire(worldIn, pos.up(), 250 + j, rand, i);
        catchOnFire(worldIn, pos.north(), 300 + j, rand, i);
        catchOnFire(worldIn, pos.south(), 300 + j, rand, i);
        for (int k = -1; k <= 1; k++) {
          for (int l = -1; l <= 1; l++) {
            for (int i1 = -1; i1 <= 4; i1++) {
              if (k != 0 || i1 != 0 || l != 0) {
                int j1 = 100;
                if (i1 > 1)
                  j1 += (i1 - 1) * 100; 
                BlockPos blockpos = pos.add(k, i1, l);
                int k1 = getNeighborEncouragement(worldIn, blockpos);
                if (k1 > 0) {
                  int l1 = (k1 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (i + 30);
                  if (flag1)
                    l1 /= 2; 
                  if (l1 > 0 && rand.nextInt(j1) <= l1 && (!worldIn.isRaining() || !canDie(worldIn, blockpos))) {
                    int i2 = i + rand.nextInt(5) / 4;
                    if (i2 > 15)
                      i2 = 15; 
                    worldIn.setBlockState(blockpos, state.withProperty((IProperty)AGE, Integer.valueOf(i2)), 3);
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  protected boolean canDie(World worldIn, BlockPos pos) {
    return !(!worldIn.isRainingAt(pos) && !worldIn.isRainingAt(pos.west()) && !worldIn.isRainingAt(pos.east()) && !worldIn.isRainingAt(pos.north()) && !worldIn.isRainingAt(pos.south()));
  }
  
  public boolean requiresUpdates() {
    return false;
  }
  
  private int getFlammability(Block blockIn) {
    Integer integer = this.flammabilities.get(blockIn);
    return (integer == null) ? 0 : integer.intValue();
  }
  
  private int getEncouragement(Block blockIn) {
    Integer integer = this.encouragements.get(blockIn);
    return (integer == null) ? 0 : integer.intValue();
  }
  
  private void catchOnFire(World worldIn, BlockPos pos, int chance, Random random, int age) {
    int i = getFlammability(worldIn.getBlockState(pos).getBlock());
    if (random.nextInt(chance) < i) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
        int j = age + random.nextInt(5) / 4;
        if (j > 15)
          j = 15; 
        worldIn.setBlockState(pos, getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(j)), 3);
      } else {
        worldIn.setBlockToAir(pos);
      } 
      if (iblockstate.getBlock() == Blocks.TNT)
        Blocks.TNT.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty((IProperty)BlockTNT.EXPLODE, Boolean.valueOf(true))); 
    } 
  }
  
  private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (canCatchFire((IBlockAccess)worldIn, pos.offset(enumfacing)))
        return true; 
      b++;
    } 
    return false;
  }
  
  private int getNeighborEncouragement(World worldIn, BlockPos pos) {
    if (!worldIn.isAirBlock(pos))
      return 0; 
    int i = 0;
    byte b;
    int j;
    EnumFacing[] arrayOfEnumFacing;
    for (j = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < j; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      i = Math.max(getEncouragement(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
      b++;
    } 
    return i;
  }
  
  public boolean isCollidable() {
    return false;
  }
  
  public boolean canCatchFire(IBlockAccess worldIn, BlockPos pos) {
    return (getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0);
  }
  
  public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    return !(!worldIn.getBlockState(pos.down()).isFullyOpaque() && !canNeighborCatchFire(worldIn, pos));
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !canNeighborCatchFire(worldIn, pos))
      worldIn.setBlockToAir(pos); 
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos))
      if (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !canNeighborCatchFire(worldIn, pos)) {
        worldIn.setBlockToAir(pos);
      } else {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + worldIn.rand.nextInt(10));
      }  
  }
  
  public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (rand.nextInt(24) == 0)
      worldIn.playSound((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false); 
    if (!worldIn.getBlockState(pos.down()).isFullyOpaque() && !Blocks.FIRE.canCatchFire((IBlockAccess)worldIn, pos.down())) {
      if (Blocks.FIRE.canCatchFire((IBlockAccess)worldIn, pos.west()))
        for (int j = 0; j < 2; j++) {
          double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612D;
          double d8 = pos.getY() + rand.nextDouble();
          double d13 = pos.getZ() + rand.nextDouble();
          worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D, new int[0]);
        }  
      if (Blocks.FIRE.canCatchFire((IBlockAccess)worldIn, pos.east()))
        for (int k = 0; k < 2; k++) {
          double d4 = (pos.getX() + 1) - rand.nextDouble() * 0.10000000149011612D;
          double d9 = pos.getY() + rand.nextDouble();
          double d14 = pos.getZ() + rand.nextDouble();
          worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D, new int[0]);
        }  
      if (Blocks.FIRE.canCatchFire((IBlockAccess)worldIn, pos.north()))
        for (int l = 0; l < 2; l++) {
          double d5 = pos.getX() + rand.nextDouble();
          double d10 = pos.getY() + rand.nextDouble();
          double d15 = pos.getZ() + rand.nextDouble() * 0.10000000149011612D;
          worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D, new int[0]);
        }  
      if (Blocks.FIRE.canCatchFire((IBlockAccess)worldIn, pos.south()))
        for (int i1 = 0; i1 < 2; i1++) {
          double d6 = pos.getX() + rand.nextDouble();
          double d11 = pos.getY() + rand.nextDouble();
          double d16 = (pos.getZ() + 1) - rand.nextDouble() * 0.10000000149011612D;
          worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D, new int[0]);
        }  
      if (Blocks.FIRE.canCatchFire((IBlockAccess)worldIn, pos.up()))
        for (int j1 = 0; j1 < 2; j1++) {
          double d7 = pos.getX() + rand.nextDouble();
          double d12 = (pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
          double d17 = pos.getZ() + rand.nextDouble();
          worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
        }  
    } else {
      for (int i = 0; i < 3; i++) {
        double d0 = pos.getX() + rand.nextDouble();
        double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
        double d2 = pos.getZ() + rand.nextDouble();
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
      } 
    } 
  }
  
  public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_) {
    return MapColor.TNT;
  }
  
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)AGE, Integer.valueOf(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Integer)state.getValue((IProperty)AGE)).intValue();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)AGE, (IProperty)NORTH, (IProperty)EAST, (IProperty)SOUTH, (IProperty)WEST, (IProperty)UPPER });
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */