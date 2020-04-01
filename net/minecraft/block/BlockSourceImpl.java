package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource {
  private final World worldObj;
  
  private final BlockPos pos;
  
  public BlockSourceImpl(World worldIn, BlockPos posIn) {
    this.worldObj = worldIn;
    this.pos = posIn;
  }
  
  public World getWorld() {
    return this.worldObj;
  }
  
  public double getX() {
    return this.pos.getX() + 0.5D;
  }
  
  public double getY() {
    return this.pos.getY() + 0.5D;
  }
  
  public double getZ() {
    return this.pos.getZ() + 0.5D;
  }
  
  public BlockPos getBlockPos() {
    return this.pos;
  }
  
  public IBlockState getBlockState() {
    return this.worldObj.getBlockState(this.pos);
  }
  
  public <T extends net.minecraft.tileentity.TileEntity> T getBlockTileEntity() {
    return (T)this.worldObj.getTileEntity(this.pos);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSourceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */