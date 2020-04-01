package net.minecraft.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public abstract class Biomes {
  private static Biome getRegisteredBiome(String id) {
    Biome biome = (Biome)Biome.REGISTRY.getObject(new ResourceLocation(id));
    if (biome == null)
      throw new IllegalStateException("Invalid Biome requested: " + id); 
    return biome;
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed Biomes before Bootstrap!"); 
  }
  
  public static final Biome OCEAN = getRegisteredBiome("ocean");
  
  public static final Biome DEFAULT = OCEAN;
  
  public static final Biome PLAINS = getRegisteredBiome("plains");
  
  public static final Biome DESERT = getRegisteredBiome("desert");
  
  public static final Biome EXTREME_HILLS = getRegisteredBiome("extreme_hills");
  
  public static final Biome FOREST = getRegisteredBiome("forest");
  
  public static final Biome TAIGA = getRegisteredBiome("taiga");
  
  public static final Biome SWAMPLAND = getRegisteredBiome("swampland");
  
  public static final Biome RIVER = getRegisteredBiome("river");
  
  public static final Biome HELL = getRegisteredBiome("hell");
  
  public static final Biome SKY = getRegisteredBiome("sky");
  
  public static final Biome FROZEN_OCEAN = getRegisteredBiome("frozen_ocean");
  
  public static final Biome FROZEN_RIVER = getRegisteredBiome("frozen_river");
  
  public static final Biome ICE_PLAINS = getRegisteredBiome("ice_flats");
  
  public static final Biome ICE_MOUNTAINS = getRegisteredBiome("ice_mountains");
  
  public static final Biome MUSHROOM_ISLAND = getRegisteredBiome("mushroom_island");
  
  public static final Biome MUSHROOM_ISLAND_SHORE = getRegisteredBiome("mushroom_island_shore");
  
  public static final Biome BEACH = getRegisteredBiome("beaches");
  
  public static final Biome DESERT_HILLS = getRegisteredBiome("desert_hills");
  
  public static final Biome FOREST_HILLS = getRegisteredBiome("forest_hills");
  
  public static final Biome TAIGA_HILLS = getRegisteredBiome("taiga_hills");
  
  public static final Biome EXTREME_HILLS_EDGE = getRegisteredBiome("smaller_extreme_hills");
  
  public static final Biome JUNGLE = getRegisteredBiome("jungle");
  
  public static final Biome JUNGLE_HILLS = getRegisteredBiome("jungle_hills");
  
  public static final Biome JUNGLE_EDGE = getRegisteredBiome("jungle_edge");
  
  public static final Biome DEEP_OCEAN = getRegisteredBiome("deep_ocean");
  
  public static final Biome STONE_BEACH = getRegisteredBiome("stone_beach");
  
  public static final Biome COLD_BEACH = getRegisteredBiome("cold_beach");
  
  public static final Biome BIRCH_FOREST = getRegisteredBiome("birch_forest");
  
  public static final Biome BIRCH_FOREST_HILLS = getRegisteredBiome("birch_forest_hills");
  
  public static final Biome ROOFED_FOREST = getRegisteredBiome("roofed_forest");
  
  public static final Biome COLD_TAIGA = getRegisteredBiome("taiga_cold");
  
  public static final Biome COLD_TAIGA_HILLS = getRegisteredBiome("taiga_cold_hills");
  
  public static final Biome REDWOOD_TAIGA = getRegisteredBiome("redwood_taiga");
  
  public static final Biome REDWOOD_TAIGA_HILLS = getRegisteredBiome("redwood_taiga_hills");
  
  public static final Biome EXTREME_HILLS_WITH_TREES = getRegisteredBiome("extreme_hills_with_trees");
  
  public static final Biome SAVANNA = getRegisteredBiome("savanna");
  
  public static final Biome SAVANNA_PLATEAU = getRegisteredBiome("savanna_rock");
  
  public static final Biome MESA = getRegisteredBiome("mesa");
  
  public static final Biome MESA_ROCK = getRegisteredBiome("mesa_rock");
  
  public static final Biome MESA_CLEAR_ROCK = getRegisteredBiome("mesa_clear_rock");
  
  public static final Biome VOID = getRegisteredBiome("void");
  
  public static final Biome MUTATED_PLAINS = getRegisteredBiome("mutated_plains");
  
  public static final Biome MUTATED_DESERT = getRegisteredBiome("mutated_desert");
  
  public static final Biome MUTATED_EXTREME_HILLS = getRegisteredBiome("mutated_extreme_hills");
  
  public static final Biome MUTATED_FOREST = getRegisteredBiome("mutated_forest");
  
  public static final Biome MUTATED_TAIGA = getRegisteredBiome("mutated_taiga");
  
  public static final Biome MUTATED_SWAMPLAND = getRegisteredBiome("mutated_swampland");
  
  public static final Biome MUTATED_ICE_FLATS = getRegisteredBiome("mutated_ice_flats");
  
  public static final Biome MUTATED_JUNGLE = getRegisteredBiome("mutated_jungle");
  
  public static final Biome MUTATED_JUNGLE_EDGE = getRegisteredBiome("mutated_jungle_edge");
  
  public static final Biome MUTATED_BIRCH_FOREST = getRegisteredBiome("mutated_birch_forest");
  
  public static final Biome MUTATED_BIRCH_FOREST_HILLS = getRegisteredBiome("mutated_birch_forest_hills");
  
  public static final Biome MUTATED_ROOFED_FOREST = getRegisteredBiome("mutated_roofed_forest");
  
  public static final Biome MUTATED_TAIGA_COLD = getRegisteredBiome("mutated_taiga_cold");
  
  public static final Biome MUTATED_REDWOOD_TAIGA = getRegisteredBiome("mutated_redwood_taiga");
  
  public static final Biome MUTATED_REDWOOD_TAIGA_HILLS = getRegisteredBiome("mutated_redwood_taiga_hills");
  
  public static final Biome MUTATED_EXTREME_HILLS_WITH_TREES = getRegisteredBiome("mutated_extreme_hills_with_trees");
  
  public static final Biome MUTATED_SAVANNA = getRegisteredBiome("mutated_savanna");
  
  public static final Biome MUTATED_SAVANNA_ROCK = getRegisteredBiome("mutated_savanna_rock");
  
  public static final Biome MUTATED_MESA = getRegisteredBiome("mutated_mesa");
  
  public static final Biome MUTATED_MESA_ROCK = getRegisteredBiome("mutated_mesa_rock");
  
  public static final Biome MUTATED_MESA_CLEAR_ROCK = getRegisteredBiome("mutated_mesa_clear_rock");
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\init\Biomes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */