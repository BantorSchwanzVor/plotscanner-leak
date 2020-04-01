package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator {
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    for (; position.getY() < 128; position = position.up()) {
      if (worldIn.isAirBlock(position)) {
        byte b;
        int i;
        EnumFacing[] arrayOfEnumFacing;
        for (i = (arrayOfEnumFacing = EnumFacing.Plane.HORIZONTAL.facings()).length, b = 0; b < i; ) {
          EnumFacing enumfacing = arrayOfEnumFacing[b];
          if (Blocks.VINE.canPlaceBlockOnSide(worldIn, position, enumfacing)) {
            IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty((IProperty)BlockVine.NORTH, Boolean.valueOf((enumfacing == EnumFacing.NORTH))).withProperty((IProperty)BlockVine.EAST, Boolean.valueOf((enumfacing == EnumFacing.EAST))).withProperty((IProperty)BlockVine.SOUTH, Boolean.valueOf((enumfacing == EnumFacing.SOUTH))).withProperty((IProperty)BlockVine.WEST, Boolean.valueOf((enumfacing == EnumFacing.WEST)));
            worldIn.setBlockState(position, iblockstate, 2);
            break;
          } 
          b++;
        } 
      } else {
        position = position.add(rand.nextInt(4) - rand.nextInt(4), 0, rand.nextInt(4) - rand.nextInt(4));
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\feature\WorldGenVines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */