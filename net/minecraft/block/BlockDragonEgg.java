package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg extends Block {
  protected static final AxisAlignedBB DRAGON_EGG_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);
  
  public BlockDragonEgg() {
    super(Material.DRAGON_EGG, MapColor.BLACK);
  }
  
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return DRAGON_EGG_AABB;
  }
  
  public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
    worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    checkFall(worldIn, pos);
  }
  
  private void checkFall(World worldIn, BlockPos pos) {
    if (BlockFalling.canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
      int i = 32;
      if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
        worldIn.spawnEntityInWorld((Entity)new EntityFallingBlock(worldIn, (pos.getX() + 0.5F), pos.getY(), (pos.getZ() + 0.5F), getDefaultState()));
      } else {
        worldIn.setBlockToAir(pos);
        BlockPos blockpos;
        for (blockpos = pos; BlockFalling.canFallThrough(worldIn.getBlockState(blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down());
        if (blockpos.getY() > 0)
          worldIn.setBlockState(blockpos, getDefaultState(), 2); 
      } 
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    teleport(worldIn, pos);
    return true;
  }
  
  public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    teleport(worldIn, pos);
  }
  
  private void teleport(World worldIn, BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (iblockstate.getBlock() == this)
      for (int i = 0; i < 1000; i++) {
        BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
        if ((worldIn.getBlockState(blockpos).getBlock()).blockMaterial == Material.AIR) {
          if (worldIn.isRemote) {
            for (int j = 0; j < 128; j++) {
              double d0 = worldIn.rand.nextDouble();
              float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
              float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
              float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
              double d1 = blockpos.getX() + (pos.getX() - blockpos.getX()) * d0 + worldIn.rand.nextDouble() - 0.5D + 0.5D;
              double d2 = blockpos.getY() + (pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() - 0.5D;
              double d3 = blockpos.getZ() + (pos.getZ() - blockpos.getZ()) * d0 + worldIn.rand.nextDouble() - 0.5D + 0.5D;
              worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, f, f1, f2, new int[0]);
            } 
          } else {
            worldIn.setBlockState(blockpos, iblockstate, 2);
            worldIn.setBlockToAir(pos);
          } 
          return;
        } 
      }  
  }
  
  public int tickRate(World worldIn) {
    return 5;
  }
  
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return true;
  }
  
  public BlockFaceShape func_193383_a(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
    return BlockFaceShape.UNDEFINED;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockDragonEgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */