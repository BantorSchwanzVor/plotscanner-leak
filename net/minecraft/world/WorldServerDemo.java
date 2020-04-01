package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerDemo extends WorldServer {
  private static final long DEMO_WORLD_SEED = "North Carolina".hashCode();
  
  public static final WorldSettings DEMO_WORLD_SETTINGS = (new WorldSettings(DEMO_WORLD_SEED, GameType.SURVIVAL, true, false, WorldType.DEFAULT)).enableBonusChest();
  
  public WorldServerDemo(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo worldInfoIn, int dimensionId, Profiler profilerIn) {
    super(server, saveHandlerIn, worldInfoIn, dimensionId, profilerIn);
    this.worldInfo.populateFromWorldSettings(DEMO_WORLD_SETTINGS);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\WorldServerDemo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */