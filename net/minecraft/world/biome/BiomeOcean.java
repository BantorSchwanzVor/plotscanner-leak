package net.minecraft.world.biome;

public class BiomeOcean extends Biome {
  public BiomeOcean(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableCreatureList.clear();
  }
  
  public Biome.TempCategory getTempCategory() {
    return Biome.TempCategory.OCEAN;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */