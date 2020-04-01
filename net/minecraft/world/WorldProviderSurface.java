package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider {
  public DimensionType getDimensionType() {
    return DimensionType.OVERWORLD;
  }
  
  public boolean canDropChunk(int x, int z) {
    return !this.worldObj.isSpawnChunk(x, z);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\WorldProviderSurface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */