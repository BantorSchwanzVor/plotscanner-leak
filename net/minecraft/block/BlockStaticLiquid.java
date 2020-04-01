package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStaticLiquid extends BlockLiquid {
  protected BlockStaticLiquid(Material materialIn) {
    super(materialIn);
    setTickRandomly(false);
    if (materialIn == Material.LAVA)
      setTickRandomly(true); 
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    if (!checkForMixing(worldIn, pos, state))
      updateLiquid(worldIn, pos, state); 
  }
  
  private void updateLiquid(World worldIn, BlockPos pos, IBlockState state) {
    BlockDynamicLiquid blockdynamicliquid = getFlowingBlock(this.blockMaterial);
    worldIn.setBlockState(pos, blockdynamicliquid.getDefaultState().withProperty((IProperty)LEVEL, state.getValue((IProperty)LEVEL)), 2);
    worldIn.scheduleUpdate(pos, blockdynamicliquid, tickRate(worldIn));
  }
  
  public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    if (this.blockMaterial == Material.LAVA)
      if (worldIn.getGameRules().getBoolean("doFireTick")) {
        int i = rand.nextInt(3);
        if (i > 0) {
          BlockPos blockpos = pos;
          for (int j = 0; j < i; j++) {
            blockpos = blockpos.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);
            if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos))
              return; 
            Block block = worldIn.getBlockState(blockpos).getBlock();
            if (block.blockMaterial == Material.AIR) {
              if (isSurroundingBlockFlammable(worldIn, blockpos)) {
                worldIn.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                return;
              } 
            } else if (block.blockMaterial.blocksMovement()) {
              return;
            } 
          } 
        } else {
          for (int k = 0; k < 3; k++) {
            BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
            if (blockpos1.getY() >= 0 && blockpos1.getY() < 256 && !worldIn.isBlockLoaded(blockpos1))
              return; 
            if (worldIn.isAirBlock(blockpos1.up()) && getCanBlockBurn(worldIn, blockpos1))
              worldIn.setBlockState(blockpos1.up(), Blocks.FIRE.getDefaultState()); 
          } 
        } 
      }  
  }
  
  protected boolean isSurroundingBlockFlammable(World worldIn, BlockPos pos) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (getCanBlockBurn(worldIn, pos.offset(enumfacing)))
        return true; 
      b++;
    } 
    return false;
  }
  
  private boolean getCanBlockBurn(World worldIn, BlockPos pos) {
    return (pos.getY() >= 0 && pos.getY() < 256 && !worldIn.isBlockLoaded(pos)) ? false : worldIn.getBlockState(pos).getMaterial().getCanBurn();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockStaticLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */