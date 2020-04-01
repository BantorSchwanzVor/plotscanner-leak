package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator {
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    if (!worldIn.isAirBlock(position))
      return false; 
    if (worldIn.getBlockState(position.up()).getBlock() != Blocks.NETHERRACK)
      return false; 
    worldIn.setBlockState(position, Blocks.GLOWSTONE.getDefaultState(), 2);
    for (int i = 0; i < 1500; i++) {
      BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
      if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR) {
        int j = 0;
        byte b;
        int k;
        EnumFacing[] arrayOfEnumFacing;
        for (k = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < k; ) {
          EnumFacing enumfacing = arrayOfEnumFacing[b];
          if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.GLOWSTONE)
            j++; 
          if (j > 1)
            break; 
          b++;
        } 
        if (j == 1)
          worldIn.setBlockState(blockpos, Blocks.GLOWSTONE.getDefaultState(), 2); 
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenGlowStone2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */