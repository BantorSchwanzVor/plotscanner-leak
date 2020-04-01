package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.util.ITickable;

public class TileEntityDaylightDetector extends TileEntity implements ITickable {
  public void update() {
    if (this.world != null && !this.world.isRemote && this.world.getTotalWorldTime() % 20L == 0L) {
      this.blockType = getBlockType();
      if (this.blockType instanceof BlockDaylightDetector)
        ((BlockDaylightDetector)this.blockType).updatePower(this.world, this.pos); 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityDaylightDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */