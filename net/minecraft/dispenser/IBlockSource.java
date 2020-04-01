package net.minecraft.dispenser;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IBlockSource extends ILocatableSource {
  double getX();
  
  double getY();
  
  double getZ();
  
  BlockPos getBlockPos();
  
  IBlockState getBlockState();
  
  <T extends net.minecraft.tileentity.TileEntity> T getBlockTileEntity();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\dispenser\IBlockSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */