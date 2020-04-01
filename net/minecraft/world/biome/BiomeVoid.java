package net.minecraft.world.biome;

public class BiomeVoid extends Biome {
  public BiomeVoid(Biome.BiomeProperties properties) {
    super(properties);
    this.spawnableMonsterList.clear();
    this.spawnableCreatureList.clear();
    this.spawnableWaterCreatureList.clear();
    this.spawnableCaveCreatureList.clear();
    this.theBiomeDecorator = new BiomeVoidDecorator();
  }
  
  public boolean ignorePlayerSpawnSuitability() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\biome\BiomeVoid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */