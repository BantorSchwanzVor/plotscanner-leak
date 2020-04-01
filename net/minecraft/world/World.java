package net.minecraft.world;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.pathfinding.PathWorldListener;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.loot.LootTableManager;

public abstract class World implements IBlockAccess {
  private int seaLevel = 63;
  
  protected boolean scheduledUpdatesAreImmediate;
  
  public final List<Entity> loadedEntityList = Lists.newArrayList();
  
  protected final List<Entity> unloadedEntityList = Lists.newArrayList();
  
  public final List<TileEntity> loadedTileEntityList = Lists.newArrayList();
  
  public final List<TileEntity> tickableTileEntities = Lists.newArrayList();
  
  private final List<TileEntity> addedTileEntityList = Lists.newArrayList();
  
  private final List<TileEntity> tileEntitiesToBeRemoved = Lists.newArrayList();
  
  public final List<EntityPlayer> playerEntities = Lists.newArrayList();
  
  public final List<Entity> weatherEffects = Lists.newArrayList();
  
  protected final IntHashMap<Entity> entitiesById = new IntHashMap();
  
  private final long cloudColour = 16777215L;
  
  private int skylightSubtracted;
  
  protected int updateLCG = (new Random()).nextInt();
  
  protected final int DIST_HASH_MAGIC = 1013904223;
  
  protected float prevRainingStrength;
  
  protected float rainingStrength;
  
  protected float prevThunderingStrength;
  
  protected float thunderingStrength;
  
  private int lastLightningBolt;
  
  public final Random rand = new Random();
  
  public final WorldProvider provider;
  
  protected PathWorldListener pathListener = new PathWorldListener();
  
  protected List<IWorldEventListener> eventListeners;
  
  protected IChunkProvider chunkProvider;
  
  protected final ISaveHandler saveHandler;
  
  protected WorldInfo worldInfo;
  
  protected boolean findingSpawnPoint;
  
  protected MapStorage mapStorage;
  
  protected VillageCollection villageCollectionObj;
  
  protected LootTableManager lootTable;
  
  protected AdvancementManager field_191951_C;
  
  protected FunctionManager field_193036_D;
  
  public final Profiler theProfiler;
  
  private final Calendar theCalendar;
  
  protected Scoreboard worldScoreboard;
  
  public final boolean isRemote;
  
  protected boolean spawnHostileMobs;
  
  protected boolean spawnPeacefulMobs;
  
  private boolean processingLoadedTiles;
  
  private final WorldBorder worldBorder;
  
  int[] lightUpdateBlockList;
  
  protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
    this.eventListeners = Lists.newArrayList((Object[])new IWorldEventListener[] { (IWorldEventListener)this.pathListener });
    this.theCalendar = Calendar.getInstance();
    this.worldScoreboard = new Scoreboard();
    this.spawnHostileMobs = true;
    this.spawnPeacefulMobs = true;
    this.lightUpdateBlockList = new int[32768];
    this.saveHandler = saveHandlerIn;
    this.theProfiler = profilerIn;
    this.worldInfo = info;
    this.provider = providerIn;
    this.isRemote = client;
    this.worldBorder = providerIn.createWorldBorder();
  }
  
  public World init() {
    return this;
  }
  
  public Biome getBiome(final BlockPos pos) {
    if (isBlockLoaded(pos)) {
      Chunk chunk = getChunkFromBlockCoords(pos);
      try {
        return chunk.getBiome(pos, this.provider.getBiomeProvider());
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
        crashreportcategory.setDetail("Location", new ICrashReportDetail<String>() {
              public String call() throws Exception {
                return CrashReportCategory.getCoordinateInfo(pos);
              }
            });
        throw new ReportedException(crashreport);
      } 
    } 
    return this.provider.getBiomeProvider().getBiome(pos, Biomes.PLAINS);
  }
  
  public BiomeProvider getBiomeProvider() {
    return this.provider.getBiomeProvider();
  }
  
  public void initialize(WorldSettings settings) {
    this.worldInfo.setServerInitialized(true);
  }
  
  @Nullable
  public MinecraftServer getMinecraftServer() {
    return null;
  }
  
  public void setInitialSpawnLocation() {
    setSpawnPoint(new BlockPos(8, 64, 8));
  }
  
  public IBlockState getGroundAboveSeaLevel(BlockPos pos) {
    BlockPos blockpos;
    for (blockpos = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ()); !isAirBlock(blockpos.up()); blockpos = blockpos.up());
    return getBlockState(blockpos);
  }
  
  private boolean isValid(BlockPos pos) {
    return (!isOutsideBuildHeight(pos) && pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000);
  }
  
  private boolean isOutsideBuildHeight(BlockPos pos) {
    return !(pos.getY() >= 0 && pos.getY() < 256);
  }
  
  public boolean isAirBlock(BlockPos pos) {
    return (getBlockState(pos).getMaterial() == Material.AIR);
  }
  
  public boolean isBlockLoaded(BlockPos pos) {
    return isBlockLoaded(pos, true);
  }
  
  public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty) {
    return isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
  }
  
  public boolean isAreaLoaded(BlockPos center, int radius) {
    return isAreaLoaded(center, radius, true);
  }
  
  public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty) {
    return isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
  }
  
  public boolean isAreaLoaded(BlockPos from, BlockPos to) {
    return isAreaLoaded(from, to, true);
  }
  
  public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty) {
    return isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
  }
  
  public boolean isAreaLoaded(StructureBoundingBox box) {
    return isAreaLoaded(box, true);
  }
  
  public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty) {
    return isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
  }
  
  private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty) {
    if (yEnd >= 0 && yStart < 256) {
      xStart >>= 4;
      zStart >>= 4;
      xEnd >>= 4;
      zEnd >>= 4;
      for (int i = xStart; i <= xEnd; i++) {
        for (int j = zStart; j <= zEnd; j++) {
          if (!isChunkLoaded(i, j, allowEmpty))
            return false; 
        } 
      } 
      return true;
    } 
    return false;
  }
  
  public Chunk getChunkFromBlockCoords(BlockPos pos) {
    return getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
  }
  
  public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
    return this.chunkProvider.provideChunk(chunkX, chunkZ);
  }
  
  public boolean func_190526_b(int p_190526_1_, int p_190526_2_) {
    return isChunkLoaded(p_190526_1_, p_190526_2_, false) ? true : this.chunkProvider.func_191062_e(p_190526_1_, p_190526_2_);
  }
  
  public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
    if (isOutsideBuildHeight(pos))
      return false; 
    if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
      return false; 
    Chunk chunk = getChunkFromBlockCoords(pos);
    Block block = newState.getBlock();
    IBlockState iblockstate = chunk.setBlockState(pos, newState);
    if (iblockstate == null)
      return false; 
    if (newState.getLightOpacity() != iblockstate.getLightOpacity() || newState.getLightValue() != iblockstate.getLightValue()) {
      this.theProfiler.startSection("checkLight");
      checkLight(pos);
      this.theProfiler.endSection();
    } 
    if ((flags & 0x2) != 0 && (!this.isRemote || (flags & 0x4) == 0) && chunk.isPopulated())
      notifyBlockUpdate(pos, iblockstate, newState, flags); 
    if (!this.isRemote && (flags & 0x1) != 0) {
      notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);
      if (newState.hasComparatorInputOverride())
        updateComparatorOutputLevel(pos, block); 
    } else if (!this.isRemote && (flags & 0x10) == 0) {
      func_190522_c(pos, block);
    } 
    return true;
  }
  
  public boolean setBlockToAir(BlockPos pos) {
    return setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
  }
  
  public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
    IBlockState iblockstate = getBlockState(pos);
    Block block = iblockstate.getBlock();
    if (iblockstate.getMaterial() == Material.AIR)
      return false; 
    playEvent(2001, pos, Block.getStateId(iblockstate));
    if (dropBlock)
      block.dropBlockAsItem(this, pos, iblockstate, 0); 
    return setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
  }
  
  public boolean setBlockState(BlockPos pos, IBlockState state) {
    return setBlockState(pos, state, 3);
  }
  
  public void notifyBlockUpdate(BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).notifyBlockUpdate(this, pos, oldState, newState, flags); 
  }
  
  public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType, boolean p_175722_3_) {
    if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
      notifyNeighborsOfStateChange(pos, blockType, p_175722_3_); 
  }
  
  public void markBlocksDirtyVertical(int x1, int z1, int x2, int z2) {
    if (x2 > z2) {
      int i = z2;
      z2 = x2;
      x2 = i;
    } 
    if (this.provider.func_191066_m())
      for (int j = x2; j <= z2; j++)
        checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));  
    markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
  }
  
  public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax) {
    markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
  }
  
  public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2); 
  }
  
  public void func_190522_c(BlockPos p_190522_1_, Block p_190522_2_) {
    func_190529_b(p_190522_1_.west(), p_190522_2_, p_190522_1_);
    func_190529_b(p_190522_1_.east(), p_190522_2_, p_190522_1_);
    func_190529_b(p_190522_1_.down(), p_190522_2_, p_190522_1_);
    func_190529_b(p_190522_1_.up(), p_190522_2_, p_190522_1_);
    func_190529_b(p_190522_1_.north(), p_190522_2_, p_190522_1_);
    func_190529_b(p_190522_1_.south(), p_190522_2_, p_190522_1_);
  }
  
  public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType, boolean p_175685_3_) {
    func_190524_a(pos.west(), blockType, pos);
    func_190524_a(pos.east(), blockType, pos);
    func_190524_a(pos.down(), blockType, pos);
    func_190524_a(pos.up(), blockType, pos);
    func_190524_a(pos.north(), blockType, pos);
    func_190524_a(pos.south(), blockType, pos);
    if (p_175685_3_)
      func_190522_c(pos, blockType); 
  }
  
  public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide) {
    if (skipSide != EnumFacing.WEST)
      func_190524_a(pos.west(), blockType, pos); 
    if (skipSide != EnumFacing.EAST)
      func_190524_a(pos.east(), blockType, pos); 
    if (skipSide != EnumFacing.DOWN)
      func_190524_a(pos.down(), blockType, pos); 
    if (skipSide != EnumFacing.UP)
      func_190524_a(pos.up(), blockType, pos); 
    if (skipSide != EnumFacing.NORTH)
      func_190524_a(pos.north(), blockType, pos); 
    if (skipSide != EnumFacing.SOUTH)
      func_190524_a(pos.south(), blockType, pos); 
  }
  
  public void func_190524_a(BlockPos p_190524_1_, final Block p_190524_2_, BlockPos p_190524_3_) {
    if (!this.isRemote) {
      IBlockState iblockstate = getBlockState(p_190524_1_);
      try {
        iblockstate.neighborChanged(this, p_190524_1_, p_190524_2_, p_190524_3_);
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
        crashreportcategory.setDetail("Source block type", new ICrashReportDetail<String>() {
              public String call() throws Exception {
                try {
                  return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getIdFromBlock(this.val$p_190524_2_)), this.val$p_190524_2_.getUnlocalizedName(), this.val$p_190524_2_.getClass().getCanonicalName() });
                } catch (Throwable var2) {
                  return "ID #" + Block.getIdFromBlock(p_190524_2_);
                } 
              }
            });
        CrashReportCategory.addBlockInfo(crashreportcategory, p_190524_1_, iblockstate);
        throw new ReportedException(crashreport);
      } 
    } 
  }
  
  public void func_190529_b(BlockPos p_190529_1_, final Block p_190529_2_, BlockPos p_190529_3_) {
    if (!this.isRemote) {
      IBlockState iblockstate = getBlockState(p_190529_1_);
      if (iblockstate.getBlock() == Blocks.field_190976_dk)
        try {
          ((BlockObserver)iblockstate.getBlock()).func_190962_b(iblockstate, this, p_190529_1_, p_190529_2_, p_190529_3_);
        } catch (Throwable throwable) {
          CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
          CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
          crashreportcategory.setDetail("Source block type", new ICrashReportDetail<String>() {
                public String call() throws Exception {
                  try {
                    return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(Block.getIdFromBlock(this.val$p_190529_2_)), this.val$p_190529_2_.getUnlocalizedName(), this.val$p_190529_2_.getClass().getCanonicalName() });
                  } catch (Throwable var2) {
                    return "ID #" + Block.getIdFromBlock(p_190529_2_);
                  } 
                }
              });
          CrashReportCategory.addBlockInfo(crashreportcategory, p_190529_1_, iblockstate);
          throw new ReportedException(crashreport);
        }  
    } 
  }
  
  public boolean isBlockTickPending(BlockPos pos, Block blockType) {
    return false;
  }
  
  public boolean canSeeSky(BlockPos pos) {
    return getChunkFromBlockCoords(pos).canSeeSky(pos);
  }
  
  public boolean canBlockSeeSky(BlockPos pos) {
    if (pos.getY() >= getSeaLevel())
      return canSeeSky(pos); 
    BlockPos blockpos = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ());
    if (!canSeeSky(blockpos))
      return false; 
    for (BlockPos blockpos1 = blockpos.down(); blockpos1.getY() > pos.getY(); blockpos1 = blockpos1.down()) {
      IBlockState iblockstate = getBlockState(blockpos1);
      if (iblockstate.getLightOpacity() > 0 && !iblockstate.getMaterial().isLiquid())
        return false; 
    } 
    return true;
  }
  
  public int getLight(BlockPos pos) {
    if (pos.getY() < 0)
      return 0; 
    if (pos.getY() >= 256)
      pos = new BlockPos(pos.getX(), 255, pos.getZ()); 
    return getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
  }
  
  public int getLightFromNeighbors(BlockPos pos) {
    return getLight(pos, true);
  }
  
  public int getLight(BlockPos pos, boolean checkNeighbors) {
    if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
      if (checkNeighbors && getBlockState(pos).useNeighborBrightness()) {
        int i1 = getLight(pos.up(), false);
        int i = getLight(pos.east(), false);
        int j = getLight(pos.west(), false);
        int k = getLight(pos.south(), false);
        int l = getLight(pos.north(), false);
        if (i > i1)
          i1 = i; 
        if (j > i1)
          i1 = j; 
        if (k > i1)
          i1 = k; 
        if (l > i1)
          i1 = l; 
        return i1;
      } 
      if (pos.getY() < 0)
        return 0; 
      if (pos.getY() >= 256)
        pos = new BlockPos(pos.getX(), 255, pos.getZ()); 
      Chunk chunk = getChunkFromBlockCoords(pos);
      return chunk.getLightSubtracted(pos, this.skylightSubtracted);
    } 
    return 15;
  }
  
  public BlockPos getHeight(BlockPos pos) {
    return new BlockPos(pos.getX(), getHeight(pos.getX(), pos.getZ()), pos.getZ());
  }
  
  public int getHeight(int x, int z) {
    int i;
    if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
      if (isChunkLoaded(x >> 4, z >> 4, true)) {
        i = getChunkFromChunkCoords(x >> 4, z >> 4).getHeightValue(x & 0xF, z & 0xF);
      } else {
        i = 0;
      } 
    } else {
      i = getSeaLevel() + 1;
    } 
    return i;
  }
  
  @Deprecated
  public int getChunksLowestHorizon(int x, int z) {
    if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
      if (!isChunkLoaded(x >> 4, z >> 4, true))
        return 0; 
      Chunk chunk = getChunkFromChunkCoords(x >> 4, z >> 4);
      return chunk.getLowestHeight();
    } 
    return getSeaLevel() + 1;
  }
  
  public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos) {
    if (!this.provider.func_191066_m() && type == EnumSkyBlock.SKY)
      return 0; 
    if (pos.getY() < 0)
      pos = new BlockPos(pos.getX(), 0, pos.getZ()); 
    if (!isValid(pos))
      return type.defaultLightValue; 
    if (!isBlockLoaded(pos))
      return type.defaultLightValue; 
    if (getBlockState(pos).useNeighborBrightness()) {
      int i1 = getLightFor(type, pos.up());
      int i = getLightFor(type, pos.east());
      int j = getLightFor(type, pos.west());
      int k = getLightFor(type, pos.south());
      int l = getLightFor(type, pos.north());
      if (i > i1)
        i1 = i; 
      if (j > i1)
        i1 = j; 
      if (k > i1)
        i1 = k; 
      if (l > i1)
        i1 = l; 
      return i1;
    } 
    Chunk chunk = getChunkFromBlockCoords(pos);
    return chunk.getLightFor(type, pos);
  }
  
  public int getLightFor(EnumSkyBlock type, BlockPos pos) {
    if (pos.getY() < 0)
      pos = new BlockPos(pos.getX(), 0, pos.getZ()); 
    if (!isValid(pos))
      return type.defaultLightValue; 
    if (!isBlockLoaded(pos))
      return type.defaultLightValue; 
    Chunk chunk = getChunkFromBlockCoords(pos);
    return chunk.getLightFor(type, pos);
  }
  
  public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue) {
    if (isValid(pos))
      if (isBlockLoaded(pos)) {
        Chunk chunk = getChunkFromBlockCoords(pos);
        chunk.setLightFor(type, pos, lightValue);
        notifyLightSet(pos);
      }  
  }
  
  public void notifyLightSet(BlockPos pos) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).notifyLightSet(pos); 
  }
  
  public int getCombinedLight(BlockPos pos, int lightValue) {
    int i = getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
    int j = getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
    if (j < lightValue)
      j = lightValue; 
    return i << 20 | j << 4;
  }
  
  public float getLightBrightness(BlockPos pos) {
    return this.provider.getLightBrightnessTable()[getLightFromNeighbors(pos)];
  }
  
  public IBlockState getBlockState(BlockPos pos) {
    if (isOutsideBuildHeight(pos))
      return Blocks.AIR.getDefaultState(); 
    Chunk chunk = getChunkFromBlockCoords(pos);
    return chunk.getBlockState(pos);
  }
  
  public boolean isDaytime() {
    return (this.skylightSubtracted < 4);
  }
  
  @Nullable
  public RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end) {
    return rayTraceBlocks(start, end, false, false, false);
  }
  
  @Nullable
  public RayTraceResult rayTraceBlocks(Vec3d start, Vec3d end, boolean stopOnLiquid) {
    return rayTraceBlocks(start, end, stopOnLiquid, false, false);
  }
  
  @Nullable
  public RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
    if (!Double.isNaN(vec31.xCoord) && !Double.isNaN(vec31.yCoord) && !Double.isNaN(vec31.zCoord)) {
      if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord)) {
        int i = MathHelper.floor(vec32.xCoord);
        int j = MathHelper.floor(vec32.yCoord);
        int k = MathHelper.floor(vec32.zCoord);
        int l = MathHelper.floor(vec31.xCoord);
        int i1 = MathHelper.floor(vec31.yCoord);
        int j1 = MathHelper.floor(vec31.zCoord);
        BlockPos blockpos = new BlockPos(l, i1, j1);
        IBlockState iblockstate = getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
          RayTraceResult raytraceresult = iblockstate.collisionRayTrace(this, blockpos, vec31, vec32);
          if (raytraceresult != null)
            return raytraceresult; 
        } 
        RayTraceResult raytraceresult2 = null;
        int k1 = 200;
        while (k1-- >= 0) {
          EnumFacing enumfacing;
          if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord))
            return null; 
          if (l == i && i1 == j && j1 == k)
            return returnLastUncollidableBlock ? raytraceresult2 : null; 
          boolean flag2 = true;
          boolean flag = true;
          boolean flag1 = true;
          double d0 = 999.0D;
          double d1 = 999.0D;
          double d2 = 999.0D;
          if (i > l) {
            d0 = l + 1.0D;
          } else if (i < l) {
            d0 = l + 0.0D;
          } else {
            flag2 = false;
          } 
          if (j > i1) {
            d1 = i1 + 1.0D;
          } else if (j < i1) {
            d1 = i1 + 0.0D;
          } else {
            flag = false;
          } 
          if (k > j1) {
            d2 = j1 + 1.0D;
          } else if (k < j1) {
            d2 = j1 + 0.0D;
          } else {
            flag1 = false;
          } 
          double d3 = 999.0D;
          double d4 = 999.0D;
          double d5 = 999.0D;
          double d6 = vec32.xCoord - vec31.xCoord;
          double d7 = vec32.yCoord - vec31.yCoord;
          double d8 = vec32.zCoord - vec31.zCoord;
          if (flag2)
            d3 = (d0 - vec31.xCoord) / d6; 
          if (flag)
            d4 = (d1 - vec31.yCoord) / d7; 
          if (flag1)
            d5 = (d2 - vec31.zCoord) / d8; 
          if (d3 == -0.0D)
            d3 = -1.0E-4D; 
          if (d4 == -0.0D)
            d4 = -1.0E-4D; 
          if (d5 == -0.0D)
            d5 = -1.0E-4D; 
          if (d3 < d4 && d3 < d5) {
            enumfacing = (i > l) ? EnumFacing.WEST : EnumFacing.EAST;
            vec31 = new Vec3d(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
          } else if (d4 < d5) {
            enumfacing = (j > i1) ? EnumFacing.DOWN : EnumFacing.UP;
            vec31 = new Vec3d(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
          } else {
            enumfacing = (k > j1) ? EnumFacing.NORTH : EnumFacing.SOUTH;
            vec31 = new Vec3d(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
          } 
          l = MathHelper.floor(vec31.xCoord) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
          i1 = MathHelper.floor(vec31.yCoord) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
          j1 = MathHelper.floor(vec31.zCoord) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
          blockpos = new BlockPos(l, i1, j1);
          IBlockState iblockstate1 = getBlockState(blockpos);
          Block block1 = iblockstate1.getBlock();
          if (!ignoreBlockWithoutBoundingBox || iblockstate1.getMaterial() == Material.PORTAL || iblockstate1.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) {
            if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
              RayTraceResult raytraceresult1 = iblockstate1.collisionRayTrace(this, blockpos, vec31, vec32);
              if (raytraceresult1 != null)
                return raytraceresult1; 
              continue;
            } 
            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
          } 
        } 
        return returnLastUncollidableBlock ? raytraceresult2 : null;
      } 
      return null;
    } 
    return null;
  }
  
  public void playSound(@Nullable EntityPlayer player, BlockPos pos, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
    playSound(player, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundIn, category, volume, pitch);
  }
  
  public void playSound(@Nullable EntityPlayer player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).playSoundToAllNearExcept(player, soundIn, category, x, y, z, volume, pitch); 
  }
  
  public void playSound(double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch, boolean distanceDelay) {}
  
  public void playRecord(BlockPos blockPositionIn, @Nullable SoundEvent soundEventIn) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).playRecord(soundEventIn, blockPositionIn); 
  }
  
  public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  
  public void func_190523_a(int p_190523_1_, double p_190523_2_, double p_190523_4_, double p_190523_6_, double p_190523_8_, double p_190523_10_, double p_190523_12_, int... p_190523_14_) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).func_190570_a(p_190523_1_, false, true, p_190523_2_, p_190523_4_, p_190523_6_, p_190523_8_, p_190523_10_, p_190523_12_, p_190523_14_); 
  }
  
  public void spawnParticle(EnumParticleTypes particleType, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    spawnParticle(particleType.getParticleID(), !(!particleType.getShouldIgnoreRange() && !ignoreRange), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  
  private void spawnParticle(int particleID, boolean ignoreRange, double xCood, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).spawnParticle(particleID, ignoreRange, xCood, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters); 
  }
  
  public boolean addWeatherEffect(Entity entityIn) {
    this.weatherEffects.add(entityIn);
    return true;
  }
  
  public boolean spawnEntityInWorld(Entity entityIn) {
    int i = MathHelper.floor(entityIn.posX / 16.0D);
    int j = MathHelper.floor(entityIn.posZ / 16.0D);
    boolean flag = entityIn.forceSpawn;
    if (entityIn instanceof EntityPlayer)
      flag = true; 
    if (!flag && !isChunkLoaded(i, j, false))
      return false; 
    if (entityIn instanceof EntityPlayer) {
      EntityPlayer entityplayer = (EntityPlayer)entityIn;
      this.playerEntities.add(entityplayer);
      updateAllPlayersSleepingFlag();
    } 
    getChunkFromChunkCoords(i, j).addEntity(entityIn);
    this.loadedEntityList.add(entityIn);
    onEntityAdded(entityIn);
    return true;
  }
  
  protected void onEntityAdded(Entity entityIn) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).onEntityAdded(entityIn); 
  }
  
  protected void onEntityRemoved(Entity entityIn) {
    for (int i = 0; i < this.eventListeners.size(); i++)
      ((IWorldEventListener)this.eventListeners.get(i)).onEntityRemoved(entityIn); 
  }
  
  public void removeEntity(Entity entityIn) {
    if (entityIn.isBeingRidden())
      entityIn.removePassengers(); 
    if (entityIn.isRiding())
      entityIn.dismountRidingEntity(); 
    entityIn.setDead();
    if (entityIn instanceof EntityPlayer) {
      this.playerEntities.remove(entityIn);
      updateAllPlayersSleepingFlag();
      onEntityRemoved(entityIn);
    } 
  }
  
  public void removeEntityDangerously(Entity entityIn) {
    entityIn.setDropItemsWhenDead(false);
    entityIn.setDead();
    if (entityIn instanceof EntityPlayer) {
      this.playerEntities.remove(entityIn);
      updateAllPlayersSleepingFlag();
    } 
    int i = entityIn.chunkCoordX;
    int j = entityIn.chunkCoordZ;
    if (entityIn.addedToChunk && isChunkLoaded(i, j, true))
      getChunkFromChunkCoords(i, j).removeEntity(entityIn); 
    this.loadedEntityList.remove(entityIn);
    onEntityRemoved(entityIn);
  }
  
  public void addEventListener(IWorldEventListener listener) {
    this.eventListeners.add(listener);
  }
  
  public void removeEventListener(IWorldEventListener listener) {
    this.eventListeners.remove(listener);
  }
  
  private boolean func_191504_a(@Nullable Entity p_191504_1_, AxisAlignedBB p_191504_2_, boolean p_191504_3_, @Nullable List<AxisAlignedBB> p_191504_4_) {
    int i = MathHelper.floor(p_191504_2_.minX) - 1;
    int j = MathHelper.ceil(p_191504_2_.maxX) + 1;
    int k = MathHelper.floor(p_191504_2_.minY) - 1;
    int l = MathHelper.ceil(p_191504_2_.maxY) + 1;
    int i1 = MathHelper.floor(p_191504_2_.minZ) - 1;
    int j1 = MathHelper.ceil(p_191504_2_.maxZ) + 1;
    WorldBorder worldborder = getWorldBorder();
    boolean flag = (p_191504_1_ != null && p_191504_1_.isOutsideBorder());
    boolean flag1 = (p_191504_1_ != null && func_191503_g(p_191504_1_));
    IBlockState iblockstate = Blocks.STONE.getDefaultState();
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    try {
      for (int k1 = i; k1 < j; k1++) {
        for (int l1 = i1; l1 < j1; l1++) {
          boolean flag2 = !(k1 != i && k1 != j - 1);
          boolean flag3 = !(l1 != i1 && l1 != j1 - 1);
          if ((!flag2 || !flag3) && isBlockLoaded((BlockPos)blockpos$pooledmutableblockpos.setPos(k1, 64, l1)))
            for (int i2 = k; i2 < l; i2++) {
              if ((!flag2 && !flag3) || i2 != l - 1) {
                IBlockState iblockstate1;
                if (p_191504_3_) {
                  if (k1 < -30000000 || k1 >= 30000000 || l1 < -30000000 || l1 >= 30000000) {
                    boolean lvt_21_2_ = true;
                    return lvt_21_2_;
                  } 
                } else if (p_191504_1_ != null && flag == flag1) {
                  p_191504_1_.setOutsideBorder(!flag1);
                } 
                blockpos$pooledmutableblockpos.setPos(k1, i2, l1);
                if (!p_191504_3_ && !worldborder.contains((BlockPos)blockpos$pooledmutableblockpos) && flag1) {
                  iblockstate1 = iblockstate;
                } else {
                  iblockstate1 = getBlockState((BlockPos)blockpos$pooledmutableblockpos);
                } 
                iblockstate1.addCollisionBoxToList(this, (BlockPos)blockpos$pooledmutableblockpos, p_191504_2_, p_191504_4_, p_191504_1_, false);
                if (p_191504_3_ && !p_191504_4_.isEmpty()) {
                  boolean flag5 = true;
                  return flag5;
                } 
              } 
            }  
        } 
      } 
    } finally {
      blockpos$pooledmutableblockpos.release();
    } 
    return !p_191504_4_.isEmpty();
  }
  
  public List<AxisAlignedBB> getCollisionBoxes(@Nullable Entity entityIn, AxisAlignedBB aabb) {
    List<AxisAlignedBB> list = Lists.newArrayList();
    func_191504_a(entityIn, aabb, false, list);
    if (entityIn != null) {
      List<Entity> list1 = getEntitiesWithinAABBExcludingEntity(entityIn, aabb.expandXyz(0.25D));
      for (int i = 0; i < list1.size(); i++) {
        Entity entity = list1.get(i);
        if (!entityIn.isRidingSameEntity(entity)) {
          AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();
          if (axisalignedbb != null && axisalignedbb.intersectsWith(aabb))
            list.add(axisalignedbb); 
          axisalignedbb = entityIn.getCollisionBox(entity);
          if (axisalignedbb != null && axisalignedbb.intersectsWith(aabb))
            list.add(axisalignedbb); 
        } 
      } 
    } 
    return list;
  }
  
  public boolean func_191503_g(Entity p_191503_1_) {
    double d0 = this.worldBorder.minX();
    double d1 = this.worldBorder.minZ();
    double d2 = this.worldBorder.maxX();
    double d3 = this.worldBorder.maxZ();
    if (p_191503_1_.isOutsideBorder()) {
      d0++;
      d1++;
      d2--;
      d3--;
    } else {
      d0--;
      d1--;
      d2++;
      d3++;
    } 
    return (p_191503_1_.posX > d0 && p_191503_1_.posX < d2 && p_191503_1_.posZ > d1 && p_191503_1_.posZ < d3);
  }
  
  public boolean collidesWithAnyBlock(AxisAlignedBB bbox) {
    return func_191504_a(null, bbox, true, Lists.newArrayList());
  }
  
  public int calculateSkylightSubtracted(float partialTicks) {
    float f = getCelestialAngle(partialTicks);
    float f1 = 1.0F - MathHelper.cos(f * 6.2831855F) * 2.0F + 0.5F;
    f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
    f1 = 1.0F - f1;
    f1 = (float)(f1 * (1.0D - (getRainStrength(partialTicks) * 5.0F) / 16.0D));
    f1 = (float)(f1 * (1.0D - (getThunderStrength(partialTicks) * 5.0F) / 16.0D));
    f1 = 1.0F - f1;
    return (int)(f1 * 11.0F);
  }
  
  public float getSunBrightness(float p_72971_1_) {
    float f = getCelestialAngle(p_72971_1_);
    float f1 = 1.0F - MathHelper.cos(f * 6.2831855F) * 2.0F + 0.2F;
    f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
    f1 = 1.0F - f1;
    f1 = (float)(f1 * (1.0D - (getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
    f1 = (float)(f1 * (1.0D - (getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
    return f1 * 0.8F + 0.2F;
  }
  
  public Vec3d getSkyColor(Entity entityIn, float partialTicks) {
    float f = getCelestialAngle(partialTicks);
    float f1 = MathHelper.cos(f * 6.2831855F) * 2.0F + 0.5F;
    f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
    int i = MathHelper.floor(entityIn.posX);
    int j = MathHelper.floor(entityIn.posY);
    int k = MathHelper.floor(entityIn.posZ);
    BlockPos blockpos = new BlockPos(i, j, k);
    Biome biome = getBiome(blockpos);
    float f2 = biome.getFloatTemperature(blockpos);
    int l = biome.getSkyColorByTemp(f2);
    float f3 = (l >> 16 & 0xFF) / 255.0F;
    float f4 = (l >> 8 & 0xFF) / 255.0F;
    float f5 = (l & 0xFF) / 255.0F;
    f3 *= f1;
    f4 *= f1;
    f5 *= f1;
    float f6 = getRainStrength(partialTicks);
    if (f6 > 0.0F) {
      float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
      float f8 = 1.0F - f6 * 0.75F;
      f3 = f3 * f8 + f7 * (1.0F - f8);
      f4 = f4 * f8 + f7 * (1.0F - f8);
      f5 = f5 * f8 + f7 * (1.0F - f8);
    } 
    float f10 = getThunderStrength(partialTicks);
    if (f10 > 0.0F) {
      float f11 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
      float f9 = 1.0F - f10 * 0.75F;
      f3 = f3 * f9 + f11 * (1.0F - f9);
      f4 = f4 * f9 + f11 * (1.0F - f9);
      f5 = f5 * f9 + f11 * (1.0F - f9);
    } 
    if (this.lastLightningBolt > 0) {
      float f12 = this.lastLightningBolt - partialTicks;
      if (f12 > 1.0F)
        f12 = 1.0F; 
      f12 *= 0.45F;
      f3 = f3 * (1.0F - f12) + 0.8F * f12;
      f4 = f4 * (1.0F - f12) + 0.8F * f12;
      f5 = f5 * (1.0F - f12) + 1.0F * f12;
    } 
    return new Vec3d(f3, f4, f5);
  }
  
  public float getCelestialAngle(float partialTicks) {
    return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
  }
  
  public int getMoonPhase() {
    return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
  }
  
  public float getCurrentMoonPhaseFactor() {
    return WorldProvider.MOON_PHASE_FACTORS[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
  }
  
  public float getCelestialAngleRadians(float partialTicks) {
    float f = getCelestialAngle(partialTicks);
    return f * 6.2831855F;
  }
  
  public Vec3d getCloudColour(float partialTicks) {
    float f = getCelestialAngle(partialTicks);
    float f1 = MathHelper.cos(f * 6.2831855F) * 2.0F + 0.5F;
    f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
    float f2 = 1.0F;
    float f3 = 1.0F;
    float f4 = 1.0F;
    float f5 = getRainStrength(partialTicks);
    if (f5 > 0.0F) {
      float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
      float f7 = 1.0F - f5 * 0.95F;
      f2 = f2 * f7 + f6 * (1.0F - f7);
      f3 = f3 * f7 + f6 * (1.0F - f7);
      f4 = f4 * f7 + f6 * (1.0F - f7);
    } 
    f2 *= f1 * 0.9F + 0.1F;
    f3 *= f1 * 0.9F + 0.1F;
    f4 *= f1 * 0.85F + 0.15F;
    float f9 = getThunderStrength(partialTicks);
    if (f9 > 0.0F) {
      float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
      float f8 = 1.0F - f9 * 0.95F;
      f2 = f2 * f8 + f10 * (1.0F - f8);
      f3 = f3 * f8 + f10 * (1.0F - f8);
      f4 = f4 * f8 + f10 * (1.0F - f8);
    } 
    return new Vec3d(f2, f3, f4);
  }
  
  public Vec3d getFogColor(float partialTicks) {
    float f = getCelestialAngle(partialTicks);
    return this.provider.getFogColor(f, partialTicks);
  }
  
  public BlockPos getPrecipitationHeight(BlockPos pos) {
    return getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
  }
  
  public BlockPos getTopSolidOrLiquidBlock(BlockPos pos) {
    Chunk chunk = getChunkFromBlockCoords(pos);
    for (BlockPos blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1) {
      BlockPos blockpos1 = blockpos.down();
      Material material = chunk.getBlockState(blockpos1).getMaterial();
      if (material.blocksMovement() && material != Material.LEAVES)
        break; 
    } 
    return blockpos;
  }
  
  public float getStarBrightness(float partialTicks) {
    float f = getCelestialAngle(partialTicks);
    float f1 = 1.0F - MathHelper.cos(f * 6.2831855F) * 2.0F + 0.25F;
    f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
    return f1 * f1 * 0.5F;
  }
  
  public boolean isUpdateScheduled(BlockPos pos, Block blk) {
    return true;
  }
  
  public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {}
  
  public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {}
  
  public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {}
  
  public void updateEntities() {
    this.theProfiler.startSection("entities");
    this.theProfiler.startSection("global");
    for (int i = 0; i < this.weatherEffects.size(); i++) {
      Entity entity = this.weatherEffects.get(i);
      try {
        entity.ticksExisted++;
        entity.onUpdate();
      } catch (Throwable throwable2) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
        if (entity == null) {
          crashreportcategory.addCrashSection("Entity", "~~NULL~~");
        } else {
          entity.addEntityCrashInfo(crashreportcategory);
        } 
        throw new ReportedException(crashreport);
      } 
      if (entity.isDead)
        this.weatherEffects.remove(i--); 
    } 
    this.theProfiler.endStartSection("remove");
    this.loadedEntityList.removeAll(this.unloadedEntityList);
    for (int k = 0; k < this.unloadedEntityList.size(); k++) {
      Entity entity1 = this.unloadedEntityList.get(k);
      int j = entity1.chunkCoordX;
      int k1 = entity1.chunkCoordZ;
      if (entity1.addedToChunk && isChunkLoaded(j, k1, true))
        getChunkFromChunkCoords(j, k1).removeEntity(entity1); 
    } 
    for (int l = 0; l < this.unloadedEntityList.size(); l++)
      onEntityRemoved(this.unloadedEntityList.get(l)); 
    this.unloadedEntityList.clear();
    tickPlayers();
    this.theProfiler.endStartSection("regular");
    for (int i1 = 0; i1 < this.loadedEntityList.size(); i1++) {
      Entity entity2 = this.loadedEntityList.get(i1);
      Entity entity3 = entity2.getRidingEntity();
      if (entity3 != null) {
        if (!entity3.isDead && entity3.isPassenger(entity2))
          continue; 
        entity2.dismountRidingEntity();
      } 
      this.theProfiler.startSection("tick");
      if (!entity2.isDead && !(entity2 instanceof net.minecraft.entity.player.EntityPlayerMP))
        try {
          updateEntity(entity2);
        } catch (Throwable throwable1) {
          CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
          CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Entity being ticked");
          entity2.addEntityCrashInfo(crashreportcategory1);
          throw new ReportedException(crashreport1);
        }  
      this.theProfiler.endSection();
      this.theProfiler.startSection("remove");
      if (entity2.isDead) {
        int l1 = entity2.chunkCoordX;
        int i2 = entity2.chunkCoordZ;
        if (entity2.addedToChunk && isChunkLoaded(l1, i2, true))
          getChunkFromChunkCoords(l1, i2).removeEntity(entity2); 
        this.loadedEntityList.remove(i1--);
        onEntityRemoved(entity2);
      } 
      this.theProfiler.endSection();
      continue;
    } 
    this.theProfiler.endStartSection("blockEntities");
    if (!this.tileEntitiesToBeRemoved.isEmpty()) {
      this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
      this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
      this.tileEntitiesToBeRemoved.clear();
    } 
    this.processingLoadedTiles = true;
    Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();
    while (iterator.hasNext()) {
      TileEntity tileentity = iterator.next();
      if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
        BlockPos blockpos = tileentity.getPos();
        if (isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos))
          try {
            this.theProfiler.func_194340_a(() -> String.valueOf(TileEntity.func_190559_a(paramTileEntity.getClass())));
            ((ITickable)tileentity).update();
            this.theProfiler.endSection();
          } catch (Throwable throwable) {
            CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
            CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Block entity being ticked");
            tileentity.addInfoToCrashReport(crashreportcategory2);
            throw new ReportedException(crashreport2);
          }  
      } 
      if (tileentity.isInvalid()) {
        iterator.remove();
        this.loadedTileEntityList.remove(tileentity);
        if (isBlockLoaded(tileentity.getPos()))
          getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos()); 
      } 
    } 
    this.processingLoadedTiles = false;
    this.theProfiler.endStartSection("pendingBlockEntities");
    if (!this.addedTileEntityList.isEmpty()) {
      for (int j1 = 0; j1 < this.addedTileEntityList.size(); j1++) {
        TileEntity tileentity1 = this.addedTileEntityList.get(j1);
        if (!tileentity1.isInvalid()) {
          if (!this.loadedTileEntityList.contains(tileentity1))
            addTileEntity(tileentity1); 
          if (isBlockLoaded(tileentity1.getPos())) {
            Chunk chunk = getChunkFromBlockCoords(tileentity1.getPos());
            IBlockState iblockstate = chunk.getBlockState(tileentity1.getPos());
            chunk.addTileEntity(tileentity1.getPos(), tileentity1);
            notifyBlockUpdate(tileentity1.getPos(), iblockstate, iblockstate, 3);
          } 
        } 
      } 
      this.addedTileEntityList.clear();
    } 
    this.theProfiler.endSection();
    this.theProfiler.endSection();
  }
  
  protected void tickPlayers() {}
  
  public boolean addTileEntity(TileEntity tile) {
    boolean flag = this.loadedTileEntityList.add(tile);
    if (flag && tile instanceof ITickable)
      this.tickableTileEntities.add(tile); 
    if (this.isRemote) {
      BlockPos blockpos1 = tile.getPos();
      IBlockState iblockstate1 = getBlockState(blockpos1);
      notifyBlockUpdate(blockpos1, iblockstate1, iblockstate1, 2);
    } 
    return flag;
  }
  
  public void addTileEntities(Collection<TileEntity> tileEntityCollection) {
    if (this.processingLoadedTiles) {
      this.addedTileEntityList.addAll(tileEntityCollection);
    } else {
      for (TileEntity tileentity2 : tileEntityCollection)
        addTileEntity(tileentity2); 
    } 
  }
  
  public void updateEntity(Entity ent) {
    updateEntityWithOptionalForce(ent, true);
  }
  
  public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
    if (!(entityIn instanceof EntityPlayer)) {
      int j2 = MathHelper.floor(entityIn.posX);
      int k2 = MathHelper.floor(entityIn.posZ);
      int l2 = 32;
      if (forceUpdate && !isAreaLoaded(j2 - 32, 0, k2 - 32, j2 + 32, 0, k2 + 32, true))
        return; 
    } 
    entityIn.lastTickPosX = entityIn.posX;
    entityIn.lastTickPosY = entityIn.posY;
    entityIn.lastTickPosZ = entityIn.posZ;
    entityIn.prevRotationYaw = entityIn.rotationYaw;
    entityIn.prevRotationPitch = entityIn.rotationPitch;
    if (forceUpdate && entityIn.addedToChunk) {
      entityIn.ticksExisted++;
      if (entityIn.isRiding()) {
        entityIn.updateRidden();
      } else {
        entityIn.onUpdate();
      } 
    } 
    this.theProfiler.startSection("chunkCheck");
    if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX))
      entityIn.posX = entityIn.lastTickPosX; 
    if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY))
      entityIn.posY = entityIn.lastTickPosY; 
    if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ))
      entityIn.posZ = entityIn.lastTickPosZ; 
    if (Double.isNaN(entityIn.rotationPitch) || Double.isInfinite(entityIn.rotationPitch))
      entityIn.rotationPitch = entityIn.prevRotationPitch; 
    if (Double.isNaN(entityIn.rotationYaw) || Double.isInfinite(entityIn.rotationYaw))
      entityIn.rotationYaw = entityIn.prevRotationYaw; 
    int i3 = MathHelper.floor(entityIn.posX / 16.0D);
    int j3 = MathHelper.floor(entityIn.posY / 16.0D);
    int k3 = MathHelper.floor(entityIn.posZ / 16.0D);
    if (!entityIn.addedToChunk || entityIn.chunkCoordX != i3 || entityIn.chunkCoordY != j3 || entityIn.chunkCoordZ != k3) {
      if (entityIn.addedToChunk && isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true))
        getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY); 
      if (!entityIn.setPositionNonDirty() && !isChunkLoaded(i3, k3, true)) {
        entityIn.addedToChunk = false;
      } else {
        getChunkFromChunkCoords(i3, k3).addEntity(entityIn);
      } 
    } 
    this.theProfiler.endSection();
    if (forceUpdate && entityIn.addedToChunk)
      for (Entity entity4 : entityIn.getPassengers()) {
        if (!entity4.isDead && entity4.getRidingEntity() == entityIn) {
          updateEntity(entity4);
          continue;
        } 
        entity4.dismountRidingEntity();
      }  
  }
  
  public boolean checkNoEntityCollision(AxisAlignedBB bb) {
    return checkNoEntityCollision(bb, null);
  }
  
  public boolean checkNoEntityCollision(AxisAlignedBB bb, @Nullable Entity entityIn) {
    List<Entity> list = getEntitiesWithinAABBExcludingEntity(null, bb);
    for (int j2 = 0; j2 < list.size(); j2++) {
      Entity entity4 = list.get(j2);
      if (!entity4.isDead && entity4.preventEntitySpawning && entity4 != entityIn && (entityIn == null || entity4.isRidingSameEntity(entityIn)))
        return false; 
    } 
    return true;
  }
  
  public boolean checkBlockCollision(AxisAlignedBB bb) {
    int j2 = MathHelper.floor(bb.minX);
    int k2 = MathHelper.ceil(bb.maxX);
    int l2 = MathHelper.floor(bb.minY);
    int i3 = MathHelper.ceil(bb.maxY);
    int j3 = MathHelper.floor(bb.minZ);
    int k3 = MathHelper.ceil(bb.maxZ);
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    for (int l3 = j2; l3 < k2; l3++) {
      for (int i4 = l2; i4 < i3; i4++) {
        for (int j4 = j3; j4 < k3; j4++) {
          IBlockState iblockstate1 = getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
          if (iblockstate1.getMaterial() != Material.AIR) {
            blockpos$pooledmutableblockpos.release();
            return true;
          } 
        } 
      } 
    } 
    blockpos$pooledmutableblockpos.release();
    return false;
  }
  
  public boolean containsAnyLiquid(AxisAlignedBB bb) {
    int j2 = MathHelper.floor(bb.minX);
    int k2 = MathHelper.ceil(bb.maxX);
    int l2 = MathHelper.floor(bb.minY);
    int i3 = MathHelper.ceil(bb.maxY);
    int j3 = MathHelper.floor(bb.minZ);
    int k3 = MathHelper.ceil(bb.maxZ);
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    for (int l3 = j2; l3 < k2; l3++) {
      for (int i4 = l2; i4 < i3; i4++) {
        for (int j4 = j3; j4 < k3; j4++) {
          IBlockState iblockstate1 = getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
          if (iblockstate1.getMaterial().isLiquid()) {
            blockpos$pooledmutableblockpos.release();
            return true;
          } 
        } 
      } 
    } 
    blockpos$pooledmutableblockpos.release();
    return false;
  }
  
  public boolean isFlammableWithin(AxisAlignedBB bb) {
    int j2 = MathHelper.floor(bb.minX);
    int k2 = MathHelper.ceil(bb.maxX);
    int l2 = MathHelper.floor(bb.minY);
    int i3 = MathHelper.ceil(bb.maxY);
    int j3 = MathHelper.floor(bb.minZ);
    int k3 = MathHelper.ceil(bb.maxZ);
    if (isAreaLoaded(j2, l2, j3, k2, i3, k3, true)) {
      BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
      for (int l3 = j2; l3 < k2; l3++) {
        for (int i4 = l2; i4 < i3; i4++) {
          for (int j4 = j3; j4 < k3; j4++) {
            Block block = getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getBlock();
            if (block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
              blockpos$pooledmutableblockpos.release();
              return true;
            } 
          } 
        } 
      } 
      blockpos$pooledmutableblockpos.release();
    } 
    return false;
  }
  
  public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn) {
    int j2 = MathHelper.floor(bb.minX);
    int k2 = MathHelper.ceil(bb.maxX);
    int l2 = MathHelper.floor(bb.minY);
    int i3 = MathHelper.ceil(bb.maxY);
    int j3 = MathHelper.floor(bb.minZ);
    int k3 = MathHelper.ceil(bb.maxZ);
    if (!isAreaLoaded(j2, l2, j3, k2, i3, k3, true))
      return false; 
    boolean flag = false;
    Vec3d vec3d = Vec3d.ZERO;
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    for (int l3 = j2; l3 < k2; l3++) {
      for (int i4 = l2; i4 < i3; i4++) {
        for (int j4 = j3; j4 < k3; j4++) {
          blockpos$pooledmutableblockpos.setPos(l3, i4, j4);
          IBlockState iblockstate1 = getBlockState((BlockPos)blockpos$pooledmutableblockpos);
          Block block = iblockstate1.getBlock();
          if (iblockstate1.getMaterial() == materialIn) {
            double d0 = ((i4 + 1) - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate1.getValue((IProperty)BlockLiquid.LEVEL)).intValue()));
            if (i3 >= d0) {
              flag = true;
              vec3d = block.modifyAcceleration(this, (BlockPos)blockpos$pooledmutableblockpos, entityIn, vec3d);
            } 
          } 
        } 
      } 
    } 
    blockpos$pooledmutableblockpos.release();
    if (vec3d.lengthVector() > 0.0D && entityIn.isPushedByWater()) {
      vec3d = vec3d.normalize();
      double d1 = 0.014D;
      entityIn.motionX += vec3d.xCoord * 0.014D;
      entityIn.motionY += vec3d.yCoord * 0.014D;
      entityIn.motionZ += vec3d.zCoord * 0.014D;
    } 
    return flag;
  }
  
  public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn) {
    int j2 = MathHelper.floor(bb.minX);
    int k2 = MathHelper.ceil(bb.maxX);
    int l2 = MathHelper.floor(bb.minY);
    int i3 = MathHelper.ceil(bb.maxY);
    int j3 = MathHelper.floor(bb.minZ);
    int k3 = MathHelper.ceil(bb.maxZ);
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    for (int l3 = j2; l3 < k2; l3++) {
      for (int i4 = l2; i4 < i3; i4++) {
        for (int j4 = j3; j4 < k3; j4++) {
          if (getBlockState((BlockPos)blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getMaterial() == materialIn) {
            blockpos$pooledmutableblockpos.release();
            return true;
          } 
        } 
      } 
    } 
    blockpos$pooledmutableblockpos.release();
    return false;
  }
  
  public Explosion createExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
    return newExplosion(entityIn, x, y, z, strength, false, isSmoking);
  }
  
  public Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
    Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
    explosion.doExplosionA();
    explosion.doExplosionB(true);
    return explosion;
  }
  
  public float getBlockDensity(Vec3d vec, AxisAlignedBB bb) {
    double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
    double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
    double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
    double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
    double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
    if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
      int j2 = 0;
      int k2 = 0;
      for (float f = 0.0F; f <= 1.0F; f = (float)(f + d0)) {
        for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)(f1 + d1)) {
          for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)(f2 + d2)) {
            double d5 = bb.minX + (bb.maxX - bb.minX) * f;
            double d6 = bb.minY + (bb.maxY - bb.minY) * f1;
            double d7 = bb.minZ + (bb.maxZ - bb.minZ) * f2;
            if (rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec) == null)
              j2++; 
            k2++;
          } 
        } 
      } 
      return j2 / k2;
    } 
    return 0.0F;
  }
  
  public boolean extinguishFire(@Nullable EntityPlayer player, BlockPos pos, EnumFacing side) {
    pos = pos.offset(side);
    if (getBlockState(pos).getBlock() == Blocks.FIRE) {
      playEvent(player, 1009, pos, 0);
      setBlockToAir(pos);
      return true;
    } 
    return false;
  }
  
  public String getDebugLoadedEntities() {
    return "All: " + this.loadedEntityList.size();
  }
  
  public String getProviderName() {
    return this.chunkProvider.makeString();
  }
  
  @Nullable
  public TileEntity getTileEntity(BlockPos pos) {
    if (isOutsideBuildHeight(pos))
      return null; 
    TileEntity tileentity2 = null;
    if (this.processingLoadedTiles)
      tileentity2 = getPendingTileEntityAt(pos); 
    if (tileentity2 == null)
      tileentity2 = getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE); 
    if (tileentity2 == null)
      tileentity2 = getPendingTileEntityAt(pos); 
    return tileentity2;
  }
  
  @Nullable
  private TileEntity getPendingTileEntityAt(BlockPos p_189508_1_) {
    for (int j2 = 0; j2 < this.addedTileEntityList.size(); j2++) {
      TileEntity tileentity2 = this.addedTileEntityList.get(j2);
      if (!tileentity2.isInvalid() && tileentity2.getPos().equals(p_189508_1_))
        return tileentity2; 
    } 
    return null;
  }
  
  public void setTileEntity(BlockPos pos, @Nullable TileEntity tileEntityIn) {
    if (!isOutsideBuildHeight(pos))
      if (tileEntityIn != null && !tileEntityIn.isInvalid())
        if (this.processingLoadedTiles) {
          tileEntityIn.setPos(pos);
          Iterator<TileEntity> iterator1 = this.addedTileEntityList.iterator();
          while (iterator1.hasNext()) {
            TileEntity tileentity2 = iterator1.next();
            if (tileentity2.getPos().equals(pos)) {
              tileentity2.invalidate();
              iterator1.remove();
            } 
          } 
          this.addedTileEntityList.add(tileEntityIn);
        } else {
          getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
          addTileEntity(tileEntityIn);
        }   
  }
  
  public void removeTileEntity(BlockPos pos) {
    TileEntity tileentity2 = getTileEntity(pos);
    if (tileentity2 != null && this.processingLoadedTiles) {
      tileentity2.invalidate();
      this.addedTileEntityList.remove(tileentity2);
    } else {
      if (tileentity2 != null) {
        this.addedTileEntityList.remove(tileentity2);
        this.loadedTileEntityList.remove(tileentity2);
        this.tickableTileEntities.remove(tileentity2);
      } 
      getChunkFromBlockCoords(pos).removeTileEntity(pos);
    } 
  }
  
  public void markTileEntityForRemoval(TileEntity tileEntityIn) {
    this.tileEntitiesToBeRemoved.add(tileEntityIn);
  }
  
  public boolean isBlockFullCube(BlockPos pos) {
    AxisAlignedBB axisalignedbb = getBlockState(pos).getCollisionBoundingBox(this, pos);
    return (axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0D);
  }
  
  public boolean isBlockNormalCube(BlockPos pos, boolean _default) {
    if (isOutsideBuildHeight(pos))
      return false; 
    Chunk chunk1 = this.chunkProvider.getLoadedChunk(pos.getX() >> 4, pos.getZ() >> 4);
    if (chunk1 != null && !chunk1.isEmpty()) {
      IBlockState iblockstate1 = getBlockState(pos);
      return (iblockstate1.getMaterial().isOpaque() && iblockstate1.isFullCube());
    } 
    return _default;
  }
  
  public void calculateInitialSkylight() {
    int j2 = calculateSkylightSubtracted(1.0F);
    if (j2 != this.skylightSubtracted)
      this.skylightSubtracted = j2; 
  }
  
  public void setAllowedSpawnTypes(boolean hostile, boolean peaceful) {
    this.spawnHostileMobs = hostile;
    this.spawnPeacefulMobs = peaceful;
  }
  
  public void tick() {
    updateWeather();
  }
  
  protected void calculateInitialWeather() {
    if (this.worldInfo.isRaining()) {
      this.rainingStrength = 1.0F;
      if (this.worldInfo.isThundering())
        this.thunderingStrength = 1.0F; 
    } 
  }
  
  protected void updateWeather() {
    if (this.provider.func_191066_m())
      if (!this.isRemote) {
        boolean flag = getGameRules().getBoolean("doWeatherCycle");
        if (flag) {
          int j2 = this.worldInfo.getCleanWeatherTime();
          if (j2 > 0) {
            j2--;
            this.worldInfo.setCleanWeatherTime(j2);
            this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
            this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
          } 
          int k2 = this.worldInfo.getThunderTime();
          if (k2 <= 0) {
            if (this.worldInfo.isThundering()) {
              this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
            } else {
              this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
            } 
          } else {
            k2--;
            this.worldInfo.setThunderTime(k2);
            if (k2 <= 0)
              this.worldInfo.setThundering(!this.worldInfo.isThundering()); 
          } 
          int l2 = this.worldInfo.getRainTime();
          if (l2 <= 0) {
            if (this.worldInfo.isRaining()) {
              this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
            } else {
              this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
            } 
          } else {
            l2--;
            this.worldInfo.setRainTime(l2);
            if (l2 <= 0)
              this.worldInfo.setRaining(!this.worldInfo.isRaining()); 
          } 
        } 
        this.prevThunderingStrength = this.thunderingStrength;
        if (this.worldInfo.isThundering()) {
          this.thunderingStrength = (float)(this.thunderingStrength + 0.01D);
        } else {
          this.thunderingStrength = (float)(this.thunderingStrength - 0.01D);
        } 
        this.thunderingStrength = MathHelper.clamp(this.thunderingStrength, 0.0F, 1.0F);
        this.prevRainingStrength = this.rainingStrength;
        if (this.worldInfo.isRaining()) {
          this.rainingStrength = (float)(this.rainingStrength + 0.01D);
        } else {
          this.rainingStrength = (float)(this.rainingStrength - 0.01D);
        } 
        this.rainingStrength = MathHelper.clamp(this.rainingStrength, 0.0F, 1.0F);
      }  
  }
  
  protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn) {
    chunkIn.enqueueRelightChecks();
  }
  
  protected void updateBlocks() {}
  
  public void immediateBlockTick(BlockPos pos, IBlockState state, Random random) {
    this.scheduledUpdatesAreImmediate = true;
    state.getBlock().updateTick(this, pos, state, random);
    this.scheduledUpdatesAreImmediate = false;
  }
  
  public boolean canBlockFreezeWater(BlockPos pos) {
    return canBlockFreeze(pos, false);
  }
  
  public boolean canBlockFreezeNoWater(BlockPos pos) {
    return canBlockFreeze(pos, true);
  }
  
  public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj) {
    Biome biome = getBiome(pos);
    float f = biome.getFloatTemperature(pos);
    if (f >= 0.15F)
      return false; 
    if (pos.getY() >= 0 && pos.getY() < 256 && getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
      IBlockState iblockstate1 = getBlockState(pos);
      Block block = iblockstate1.getBlock();
      if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && ((Integer)iblockstate1.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0) {
        if (!noWaterAdj)
          return true; 
        boolean flag = (isWater(pos.west()) && isWater(pos.east()) && isWater(pos.north()) && isWater(pos.south()));
        if (!flag)
          return true; 
      } 
    } 
    return false;
  }
  
  private boolean isWater(BlockPos pos) {
    return (getBlockState(pos).getMaterial() == Material.WATER);
  }
  
  public boolean canSnowAt(BlockPos pos, boolean checkLight) {
    Biome biome = getBiome(pos);
    float f = biome.getFloatTemperature(pos);
    if (f >= 0.15F)
      return false; 
    if (!checkLight)
      return true; 
    if (pos.getY() >= 0 && pos.getY() < 256 && getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
      IBlockState iblockstate1 = getBlockState(pos);
      if (iblockstate1.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(this, pos))
        return true; 
    } 
    return false;
  }
  
  public boolean checkLight(BlockPos pos) {
    boolean flag = false;
    if (this.provider.func_191066_m())
      flag |= checkLightFor(EnumSkyBlock.SKY, pos); 
    flag |= checkLightFor(EnumSkyBlock.BLOCK, pos);
    return flag;
  }
  
  private int getRawLight(BlockPos pos, EnumSkyBlock lightType) {
    if (lightType == EnumSkyBlock.SKY && canSeeSky(pos))
      return 15; 
    IBlockState iblockstate1 = getBlockState(pos);
    int j2 = (lightType == EnumSkyBlock.SKY) ? 0 : iblockstate1.getLightValue();
    int k2 = iblockstate1.getLightOpacity();
    if (k2 >= 15 && iblockstate1.getLightValue() > 0)
      k2 = 1; 
    if (k2 < 1)
      k2 = 1; 
    if (k2 >= 15)
      return 0; 
    if (j2 >= 14)
      return j2; 
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    try {
      byte b;
      int i;
      EnumFacing[] arrayOfEnumFacing;
      for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
        EnumFacing enumfacing = arrayOfEnumFacing[b];
        blockpos$pooledmutableblockpos.setPos((Vec3i)pos).move(enumfacing);
        int l2 = getLightFor(lightType, (BlockPos)blockpos$pooledmutableblockpos) - k2;
        if (l2 > j2)
          j2 = l2; 
        if (j2 >= 14) {
          int i3 = j2;
          return i3;
        } 
        b++;
      } 
      return j2;
    } finally {
      blockpos$pooledmutableblockpos.release();
    } 
  }
  
  public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos) {
    if (!isAreaLoaded(pos, 17, false))
      return false; 
    int j2 = 0;
    int k2 = 0;
    this.theProfiler.startSection("getBrightness");
    int l2 = getLightFor(lightType, pos);
    int i3 = getRawLight(pos, lightType);
    int j3 = pos.getX();
    int k3 = pos.getY();
    int l3 = pos.getZ();
    if (i3 > l2) {
      this.lightUpdateBlockList[k2++] = 133152;
    } else if (i3 < l2) {
      this.lightUpdateBlockList[k2++] = 0x20820 | l2 << 18;
      while (j2 < k2) {
        int i4 = this.lightUpdateBlockList[j2++];
        int j4 = (i4 & 0x3F) - 32 + j3;
        int k4 = (i4 >> 6 & 0x3F) - 32 + k3;
        int l4 = (i4 >> 12 & 0x3F) - 32 + l3;
        int i5 = i4 >> 18 & 0xF;
        BlockPos blockpos1 = new BlockPos(j4, k4, l4);
        int j5 = getLightFor(lightType, blockpos1);
        if (j5 == i5) {
          setLightFor(lightType, blockpos1, 0);
          if (i5 > 0) {
            int k5 = MathHelper.abs(j4 - j3);
            int l5 = MathHelper.abs(k4 - k3);
            int i6 = MathHelper.abs(l4 - l3);
            if (k5 + l5 + i6 < 17) {
              BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
              byte b;
              int i;
              EnumFacing[] arrayOfEnumFacing;
              for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
                EnumFacing enumfacing = arrayOfEnumFacing[b];
                int j6 = j4 + enumfacing.getFrontOffsetX();
                int k6 = k4 + enumfacing.getFrontOffsetY();
                int l6 = l4 + enumfacing.getFrontOffsetZ();
                blockpos$pooledmutableblockpos.setPos(j6, k6, l6);
                int i7 = Math.max(1, getBlockState((BlockPos)blockpos$pooledmutableblockpos).getLightOpacity());
                j5 = getLightFor(lightType, (BlockPos)blockpos$pooledmutableblockpos);
                if (j5 == i5 - i7 && k2 < this.lightUpdateBlockList.length)
                  this.lightUpdateBlockList[k2++] = j6 - j3 + 32 | k6 - k3 + 32 << 6 | l6 - l3 + 32 << 12 | i5 - i7 << 18; 
                b++;
              } 
              blockpos$pooledmutableblockpos.release();
            } 
          } 
        } 
      } 
      j2 = 0;
    } 
    this.theProfiler.endSection();
    this.theProfiler.startSection("checkedPosition < toCheckCount");
    while (j2 < k2) {
      int j7 = this.lightUpdateBlockList[j2++];
      int k7 = (j7 & 0x3F) - 32 + j3;
      int l7 = (j7 >> 6 & 0x3F) - 32 + k3;
      int i8 = (j7 >> 12 & 0x3F) - 32 + l3;
      BlockPos blockpos2 = new BlockPos(k7, l7, i8);
      int j8 = getLightFor(lightType, blockpos2);
      int k8 = getRawLight(blockpos2, lightType);
      if (k8 != j8) {
        setLightFor(lightType, blockpos2, k8);
        if (k8 > j8) {
          int l8 = Math.abs(k7 - j3);
          int i9 = Math.abs(l7 - k3);
          int j9 = Math.abs(i8 - l3);
          boolean flag = (k2 < this.lightUpdateBlockList.length - 6);
          if (l8 + i9 + j9 < 17 && flag) {
            if (getLightFor(lightType, blockpos2.west()) < k8)
              this.lightUpdateBlockList[k2++] = k7 - 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12); 
            if (getLightFor(lightType, blockpos2.east()) < k8)
              this.lightUpdateBlockList[k2++] = k7 + 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12); 
            if (getLightFor(lightType, blockpos2.down()) < k8)
              this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12); 
            if (getLightFor(lightType, blockpos2.up()) < k8)
              this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 + 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12); 
            if (getLightFor(lightType, blockpos2.north()) < k8)
              this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - 1 - l3 + 32 << 12); 
            if (getLightFor(lightType, blockpos2.south()) < k8)
              this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 + 1 - l3 + 32 << 12); 
          } 
        } 
      } 
    } 
    this.theProfiler.endSection();
    return true;
  }
  
  public boolean tickUpdates(boolean p_72955_1_) {
    return false;
  }
  
  @Nullable
  public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_) {
    return null;
  }
  
  @Nullable
  public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean p_175712_2_) {
    return null;
  }
  
  public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable Entity entityIn, AxisAlignedBB bb) {
    return getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
  }
  
  public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
    List<Entity> list = Lists.newArrayList();
    int j2 = MathHelper.floor((boundingBox.minX - 2.0D) / 16.0D);
    int k2 = MathHelper.floor((boundingBox.maxX + 2.0D) / 16.0D);
    int l2 = MathHelper.floor((boundingBox.minZ - 2.0D) / 16.0D);
    int i3 = MathHelper.floor((boundingBox.maxZ + 2.0D) / 16.0D);
    for (int j3 = j2; j3 <= k2; j3++) {
      for (int k3 = l2; k3 <= i3; k3++) {
        if (isChunkLoaded(j3, k3, true))
          getChunkFromChunkCoords(j3, k3).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate); 
      } 
    } 
    return list;
  }
  
  public <T extends Entity> List<T> getEntities(Class<? extends T> entityType, Predicate<? super T> filter) {
    List<T> list = Lists.newArrayList();
    for (Entity entity4 : this.loadedEntityList) {
      if (entityType.isAssignableFrom(entity4.getClass()) && filter.apply(entity4))
        list.add((T)entity4); 
    } 
    return list;
  }
  
  public <T extends Entity> List<T> getPlayers(Class<? extends T> playerType, Predicate<? super T> filter) {
    List<T> list = Lists.newArrayList();
    for (Entity entity4 : this.playerEntities) {
      if (playerType.isAssignableFrom(entity4.getClass()) && filter.apply(entity4))
        list.add((T)entity4); 
    } 
    return list;
  }
  
  public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> classEntity, AxisAlignedBB bb) {
    return getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
  }
  
  public <T extends Entity> List<T> getEntitiesWithinAABB(Class<? extends T> clazz, AxisAlignedBB aabb, @Nullable Predicate<? super T> filter) {
    int j2 = MathHelper.floor((aabb.minX - 2.0D) / 16.0D);
    int k2 = MathHelper.ceil((aabb.maxX + 2.0D) / 16.0D);
    int l2 = MathHelper.floor((aabb.minZ - 2.0D) / 16.0D);
    int i3 = MathHelper.ceil((aabb.maxZ + 2.0D) / 16.0D);
    List<T> list = Lists.newArrayList();
    for (int j3 = j2; j3 < k2; j3++) {
      for (int k3 = l2; k3 < i3; k3++) {
        if (isChunkLoaded(j3, k3, true))
          getChunkFromChunkCoords(j3, k3).getEntitiesOfTypeWithinAAAB(clazz, aabb, list, filter); 
      } 
    } 
    return list;
  }
  
  @Nullable
  public <T extends Entity> T findNearestEntityWithinAABB(Class<? extends T> entityType, AxisAlignedBB aabb, T closestTo) {
    Entity entity;
    List<T> list = getEntitiesWithinAABB(entityType, aabb);
    T t = null;
    double d0 = Double.MAX_VALUE;
    for (int j2 = 0; j2 < list.size(); j2++) {
      Entity entity1 = (Entity)list.get(j2);
      if (entity1 != closestTo && EntitySelectors.NOT_SPECTATING.apply(entity1)) {
        double d1 = closestTo.getDistanceSqToEntity(entity1);
        if (d1 <= d0) {
          entity = entity1;
          d0 = d1;
        } 
      } 
    } 
    return (T)entity;
  }
  
  @Nullable
  public Entity getEntityByID(int id) {
    return (Entity)this.entitiesById.lookup(id);
  }
  
  public List<Entity> getLoadedEntityList() {
    return this.loadedEntityList;
  }
  
  public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {
    if (isBlockLoaded(pos))
      getChunkFromBlockCoords(pos).setChunkModified(); 
  }
  
  public int countEntities(Class<?> entityType) {
    int j2 = 0;
    for (Entity entity4 : this.loadedEntityList) {
      if ((!(entity4 instanceof EntityLiving) || !((EntityLiving)entity4).isNoDespawnRequired()) && entityType.isAssignableFrom(entity4.getClass()))
        j2++; 
    } 
    return j2;
  }
  
  public void loadEntities(Collection<Entity> entityCollection) {
    this.loadedEntityList.addAll(entityCollection);
    for (Entity entity4 : entityCollection)
      onEntityAdded(entity4); 
  }
  
  public void unloadEntities(Collection<Entity> entityCollection) {
    this.unloadedEntityList.addAll(entityCollection);
  }
  
  public boolean func_190527_a(Block p_190527_1_, BlockPos p_190527_2_, boolean p_190527_3_, EnumFacing p_190527_4_, @Nullable Entity p_190527_5_) {
    IBlockState iblockstate1 = getBlockState(p_190527_2_);
    AxisAlignedBB axisalignedbb = p_190527_3_ ? null : p_190527_1_.getDefaultState().getCollisionBoundingBox(this, p_190527_2_);
    if (axisalignedbb != Block.NULL_AABB && !checkNoEntityCollision(axisalignedbb.offset(p_190527_2_), p_190527_5_))
      return false; 
    if (iblockstate1.getMaterial() == Material.CIRCUITS && p_190527_1_ == Blocks.ANVIL)
      return true; 
    return (iblockstate1.getMaterial().isReplaceable() && p_190527_1_.canPlaceBlockOnSide(this, p_190527_2_, p_190527_4_));
  }
  
  public int getSeaLevel() {
    return this.seaLevel;
  }
  
  public void setSeaLevel(int seaLevelIn) {
    this.seaLevel = seaLevelIn;
  }
  
  public int getStrongPower(BlockPos pos, EnumFacing direction) {
    return getBlockState(pos).getStrongPower(this, pos, direction);
  }
  
  public WorldType getWorldType() {
    return this.worldInfo.getTerrainType();
  }
  
  public int getStrongPower(BlockPos pos) {
    int j2 = 0;
    j2 = Math.max(j2, getStrongPower(pos.down(), EnumFacing.DOWN));
    if (j2 >= 15)
      return j2; 
    j2 = Math.max(j2, getStrongPower(pos.up(), EnumFacing.UP));
    if (j2 >= 15)
      return j2; 
    j2 = Math.max(j2, getStrongPower(pos.north(), EnumFacing.NORTH));
    if (j2 >= 15)
      return j2; 
    j2 = Math.max(j2, getStrongPower(pos.south(), EnumFacing.SOUTH));
    if (j2 >= 15)
      return j2; 
    j2 = Math.max(j2, getStrongPower(pos.west(), EnumFacing.WEST));
    if (j2 >= 15)
      return j2; 
    j2 = Math.max(j2, getStrongPower(pos.east(), EnumFacing.EAST));
    return (j2 >= 15) ? j2 : j2;
  }
  
  public boolean isSidePowered(BlockPos pos, EnumFacing side) {
    return (getRedstonePower(pos, side) > 0);
  }
  
  public int getRedstonePower(BlockPos pos, EnumFacing facing) {
    IBlockState iblockstate1 = getBlockState(pos);
    return iblockstate1.isNormalCube() ? getStrongPower(pos) : iblockstate1.getWeakPower(this, pos, facing);
  }
  
  public boolean isBlockPowered(BlockPos pos) {
    if (getRedstonePower(pos.down(), EnumFacing.DOWN) > 0)
      return true; 
    if (getRedstonePower(pos.up(), EnumFacing.UP) > 0)
      return true; 
    if (getRedstonePower(pos.north(), EnumFacing.NORTH) > 0)
      return true; 
    if (getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0)
      return true; 
    if (getRedstonePower(pos.west(), EnumFacing.WEST) > 0)
      return true; 
    return (getRedstonePower(pos.east(), EnumFacing.EAST) > 0);
  }
  
  public int isBlockIndirectlyGettingPowered(BlockPos pos) {
    int j2 = 0;
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      int k2 = getRedstonePower(pos.offset(enumfacing), enumfacing);
      if (k2 >= 15)
        return 15; 
      if (k2 > j2)
        j2 = k2; 
      b++;
    } 
    return j2;
  }
  
  @Nullable
  public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance) {
    return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, false);
  }
  
  @Nullable
  public EntityPlayer getNearestPlayerNotCreative(Entity entityIn, double distance) {
    return getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, true);
  }
  
  @Nullable
  public EntityPlayer getClosestPlayer(double posX, double posY, double posZ, double distance, boolean spectator) {
    Predicate<Entity> predicate = spectator ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
    return func_190525_a(posX, posY, posZ, distance, predicate);
  }
  
  @Nullable
  public EntityPlayer getClosestPlayerExceptYou(double posX, double posY, double posZ, double distance, boolean spectator) {
    Predicate<Entity> predicate = spectator ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
    return func_190525_b(posX, posY, posZ, distance, predicate);
  }
  
  @Nullable
  public EntityPlayer func_190525_b(double p_190525_1_, double p_190525_3_, double p_190525_5_, double p_190525_7_, Predicate<Entity> p_190525_9_) {
    double d0 = -1.0D;
    EntityPlayer entityplayer = null;
    List<EntityPlayer> playerEntities = this.playerEntities;
    playerEntities.remove((Minecraft.getMinecraft()).player);
    for (int i = 0; i < playerEntities.size(); i++) {
      EntityPlayer entityplayer1 = playerEntities.get(i);
      if (p_190525_9_.apply(entityplayer1)) {
        double d1 = entityplayer1.getDistanceSq(p_190525_1_, p_190525_3_, p_190525_5_);
        if ((p_190525_7_ < 0.0D || d1 < p_190525_7_ * p_190525_7_) && (d0 == -1.0D || d1 < d0)) {
          d0 = d1;
          entityplayer = entityplayer1;
        } 
      } 
    } 
    return entityplayer;
  }
  
  @Nullable
  public EntityPlayer func_190525_a(double p_190525_1_, double p_190525_3_, double p_190525_5_, double p_190525_7_, Predicate<Entity> p_190525_9_) {
    double d0 = -1.0D;
    EntityPlayer entityplayer = null;
    for (int j2 = 0; j2 < this.playerEntities.size(); j2++) {
      EntityPlayer entityplayer1 = this.playerEntities.get(j2);
      if (p_190525_9_.apply(entityplayer1)) {
        double d1 = entityplayer1.getDistanceSq(p_190525_1_, p_190525_3_, p_190525_5_);
        if ((p_190525_7_ < 0.0D || d1 < p_190525_7_ * p_190525_7_) && (d0 == -1.0D || d1 < d0)) {
          d0 = d1;
          entityplayer = entityplayer1;
        } 
      } 
    } 
    return entityplayer;
  }
  
  public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range) {
    for (int j2 = 0; j2 < this.playerEntities.size(); j2++) {
      EntityPlayer entityplayer = this.playerEntities.get(j2);
      if (EntitySelectors.NOT_SPECTATING.apply(entityplayer)) {
        double d0 = entityplayer.getDistanceSq(x, y, z);
        if (range < 0.0D || d0 < range * range)
          return true; 
      } 
    } 
    return false;
  }
  
  @Nullable
  public EntityPlayer getNearestAttackablePlayer(Entity entityIn, double maxXZDistance, double maxYDistance) {
    return getNearestAttackablePlayer(entityIn.posX, entityIn.posY, entityIn.posZ, maxXZDistance, maxYDistance, null, null);
  }
  
  @Nullable
  public EntityPlayer getNearestAttackablePlayer(BlockPos pos, double maxXZDistance, double maxYDistance) {
    return getNearestAttackablePlayer((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), maxXZDistance, maxYDistance, null, null);
  }
  
  @Nullable
  public EntityPlayer getNearestAttackablePlayer(double posX, double posY, double posZ, double maxXZDistance, double maxYDistance, @Nullable Function<EntityPlayer, Double> playerToDouble, @Nullable Predicate<EntityPlayer> p_184150_12_) {
    double d0 = -1.0D;
    EntityPlayer entityplayer = null;
    for (int j2 = 0; j2 < this.playerEntities.size(); j2++) {
      EntityPlayer entityplayer1 = this.playerEntities.get(j2);
      if (!entityplayer1.capabilities.disableDamage && entityplayer1.isEntityAlive() && !entityplayer1.isSpectator() && (p_184150_12_ == null || p_184150_12_.apply(entityplayer1))) {
        double d1 = entityplayer1.getDistanceSq(posX, entityplayer1.posY, posZ);
        double d2 = maxXZDistance;
        if (entityplayer1.isSneaking())
          d2 = maxXZDistance * 0.800000011920929D; 
        if (entityplayer1.isInvisible()) {
          float f = entityplayer1.getArmorVisibility();
          if (f < 0.1F)
            f = 0.1F; 
          d2 *= (0.7F * f);
        } 
        if (playerToDouble != null)
          d2 *= ((Double)MoreObjects.firstNonNull(playerToDouble.apply(entityplayer1), Double.valueOf(1.0D))).doubleValue(); 
        if ((maxYDistance < 0.0D || Math.abs(entityplayer1.posY - posY) < maxYDistance * maxYDistance) && (maxXZDistance < 0.0D || d1 < d2 * d2) && (d0 == -1.0D || d1 < d0)) {
          d0 = d1;
          entityplayer = entityplayer1;
        } 
      } 
    } 
    return entityplayer;
  }
  
  @Nullable
  public EntityPlayer getPlayerEntityByName(String name) {
    for (int j2 = 0; j2 < this.playerEntities.size(); j2++) {
      EntityPlayer entityplayer = this.playerEntities.get(j2);
      if (name.equals(entityplayer.getName()))
        return entityplayer; 
    } 
    return null;
  }
  
  @Nullable
  public EntityPlayer getPlayerEntityByUUID(UUID uuid) {
    for (int j2 = 0; j2 < this.playerEntities.size(); j2++) {
      EntityPlayer entityplayer = this.playerEntities.get(j2);
      if (uuid.equals(entityplayer.getUniqueID()))
        return entityplayer; 
    } 
    return null;
  }
  
  public void sendQuittingDisconnectingPacket() {}
  
  public void checkSessionLock() throws MinecraftException {
    this.saveHandler.checkSessionLock();
  }
  
  public void setTotalWorldTime(long worldTime) {
    this.worldInfo.setWorldTotalTime(worldTime);
  }
  
  public long getSeed() {
    return this.worldInfo.getSeed();
  }
  
  public long getTotalWorldTime() {
    return this.worldInfo.getWorldTotalTime();
  }
  
  public long getWorldTime() {
    return this.worldInfo.getWorldTime();
  }
  
  public void setWorldTime(long time) {
    this.worldInfo.setWorldTime(time);
  }
  
  public BlockPos getSpawnPoint() {
    BlockPos blockpos1 = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
    if (!getWorldBorder().contains(blockpos1))
      blockpos1 = getHeight(new BlockPos(getWorldBorder().getCenterX(), 0.0D, getWorldBorder().getCenterZ())); 
    return blockpos1;
  }
  
  public void setSpawnPoint(BlockPos pos) {
    this.worldInfo.setSpawn(pos);
  }
  
  public void joinEntityInSurroundings(Entity entityIn) {
    int j2 = MathHelper.floor(entityIn.posX / 16.0D);
    int k2 = MathHelper.floor(entityIn.posZ / 16.0D);
    int l2 = 2;
    for (int i3 = -2; i3 <= 2; i3++) {
      for (int j3 = -2; j3 <= 2; j3++)
        getChunkFromChunkCoords(j2 + i3, k2 + j3); 
    } 
    if (!this.loadedEntityList.contains(entityIn))
      this.loadedEntityList.add(entityIn); 
  }
  
  public boolean isBlockModifiable(EntityPlayer player, BlockPos pos) {
    return true;
  }
  
  public void setEntityState(Entity entityIn, byte state) {}
  
  public IChunkProvider getChunkProvider() {
    return this.chunkProvider;
  }
  
  public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
    getBlockState(pos).onBlockEventReceived(this, pos, eventID, eventParam);
  }
  
  public ISaveHandler getSaveHandler() {
    return this.saveHandler;
  }
  
  public WorldInfo getWorldInfo() {
    return this.worldInfo;
  }
  
  public GameRules getGameRules() {
    return this.worldInfo.getGameRulesInstance();
  }
  
  public void updateAllPlayersSleepingFlag() {}
  
  public float getThunderStrength(float delta) {
    return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * getRainStrength(delta);
  }
  
  public void setThunderStrength(float strength) {
    this.prevThunderingStrength = strength;
    this.thunderingStrength = strength;
  }
  
  public float getRainStrength(float delta) {
    return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
  }
  
  public void setRainStrength(float strength) {
    this.prevRainingStrength = strength;
    this.rainingStrength = strength;
  }
  
  public boolean isThundering() {
    return (getThunderStrength(1.0F) > 0.9D);
  }
  
  public boolean isRaining() {
    return (getRainStrength(1.0F) > 0.2D);
  }
  
  public boolean isRainingAt(BlockPos strikePosition) {
    if (!isRaining())
      return false; 
    if (!canSeeSky(strikePosition))
      return false; 
    if (getPrecipitationHeight(strikePosition).getY() > strikePosition.getY())
      return false; 
    Biome biome = getBiome(strikePosition);
    if (biome.getEnableSnow())
      return false; 
    return canSnowAt(strikePosition, false) ? false : biome.canRain();
  }
  
  public boolean isBlockinHighHumidity(BlockPos pos) {
    Biome biome = getBiome(pos);
    return biome.isHighHumidity();
  }
  
  @Nullable
  public MapStorage getMapStorage() {
    return this.mapStorage;
  }
  
  public void setItemData(String dataID, WorldSavedData worldSavedDataIn) {
    this.mapStorage.setData(dataID, worldSavedDataIn);
  }
  
  @Nullable
  public WorldSavedData loadItemData(Class<? extends WorldSavedData> clazz, String dataID) {
    return this.mapStorage.getOrLoadData(clazz, dataID);
  }
  
  public int getUniqueDataId(String key) {
    return this.mapStorage.getUniqueDataId(key);
  }
  
  public void playBroadcastSound(int id, BlockPos pos, int data) {
    for (int j2 = 0; j2 < this.eventListeners.size(); j2++)
      ((IWorldEventListener)this.eventListeners.get(j2)).broadcastSound(id, pos, data); 
  }
  
  public void playEvent(int type, BlockPos pos, int data) {
    playEvent(null, type, pos, data);
  }
  
  public void playEvent(@Nullable EntityPlayer player, int type, BlockPos pos, int data) {
    try {
      for (int j2 = 0; j2 < this.eventListeners.size(); j2++)
        ((IWorldEventListener)this.eventListeners.get(j2)).playEvent(player, type, pos, data); 
    } catch (Throwable throwable3) {
      CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Playing level event");
      CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Level event being played");
      crashreportcategory3.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
      crashreportcategory3.addCrashSection("Event source", player);
      crashreportcategory3.addCrashSection("Event type", Integer.valueOf(type));
      crashreportcategory3.addCrashSection("Event data", Integer.valueOf(data));
      throw new ReportedException(crashreport3);
    } 
  }
  
  public int getHeight() {
    return 256;
  }
  
  public int getActualHeight() {
    return this.provider.getHasNoSky() ? 128 : 256;
  }
  
  public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_) {
    long j2 = p_72843_1_ * 341873128712L + p_72843_2_ * 132897987541L + getWorldInfo().getSeed() + p_72843_3_;
    this.rand.setSeed(j2);
    return this.rand;
  }
  
  public double getHorizon() {
    return (this.worldInfo.getTerrainType() == WorldType.FLAT) ? 0.0D : 63.0D;
  }
  
  public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
    CrashReportCategory crashreportcategory3 = report.makeCategoryDepth("Affected level", 1);
    crashreportcategory3.addCrashSection("Level name", (this.worldInfo == null) ? "????" : this.worldInfo.getWorldName());
    crashreportcategory3.setDetail("All players", new ICrashReportDetail<String>() {
          public String call() {
            return String.valueOf(World.this.playerEntities.size()) + " total; " + World.this.playerEntities;
          }
        });
    crashreportcategory3.setDetail("Chunk stats", new ICrashReportDetail<String>() {
          public String call() {
            return World.this.chunkProvider.makeString();
          }
        });
    try {
      this.worldInfo.addToCrashReport(crashreportcategory3);
    } catch (Throwable throwable3) {
      crashreportcategory3.addCrashSectionThrowable("Level Data Unobtainable", throwable3);
    } 
    return crashreportcategory3;
  }
  
  public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
    for (int j2 = 0; j2 < this.eventListeners.size(); j2++) {
      IWorldEventListener iworldeventlistener = this.eventListeners.get(j2);
      iworldeventlistener.sendBlockBreakProgress(breakerId, pos, progress);
    } 
  }
  
  public Calendar getCurrentDate() {
    if (getTotalWorldTime() % 600L == 0L)
      this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis()); 
    return this.theCalendar;
  }
  
  public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, @Nullable NBTTagCompound compund) {}
  
  public Scoreboard getScoreboard() {
    return this.worldScoreboard;
  }
  
  public void updateComparatorOutputLevel(BlockPos pos, Block blockIn) {
    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
      BlockPos blockpos1 = pos.offset(enumfacing);
      if (isBlockLoaded(blockpos1)) {
        IBlockState iblockstate1 = getBlockState(blockpos1);
        if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate1)) {
          iblockstate1.neighborChanged(this, blockpos1, blockIn, pos);
          continue;
        } 
        if (iblockstate1.isNormalCube()) {
          blockpos1 = blockpos1.offset(enumfacing);
          iblockstate1 = getBlockState(blockpos1);
          if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate1))
            iblockstate1.neighborChanged(this, blockpos1, blockIn, pos); 
        } 
      } 
    } 
  }
  
  public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
    long j2 = 0L;
    float f = 0.0F;
    if (isBlockLoaded(pos)) {
      f = getCurrentMoonPhaseFactor();
      j2 = getChunkFromBlockCoords(pos).getInhabitedTime();
    } 
    return new DifficultyInstance(getDifficulty(), getWorldTime(), j2, f);
  }
  
  public EnumDifficulty getDifficulty() {
    return getWorldInfo().getDifficulty();
  }
  
  public int getSkylightSubtracted() {
    return this.skylightSubtracted;
  }
  
  public void setSkylightSubtracted(int newSkylightSubtracted) {
    this.skylightSubtracted = newSkylightSubtracted;
  }
  
  public int getLastLightningBolt() {
    return this.lastLightningBolt;
  }
  
  public void setLastLightningBolt(int lastLightningBoltIn) {
    this.lastLightningBolt = lastLightningBoltIn;
  }
  
  public VillageCollection getVillageCollection() {
    return this.villageCollectionObj;
  }
  
  public WorldBorder getWorldBorder() {
    return this.worldBorder;
  }
  
  public boolean isSpawnChunk(int x, int z) {
    BlockPos blockpos1 = getSpawnPoint();
    int j2 = x * 16 + 8 - blockpos1.getX();
    int k2 = z * 16 + 8 - blockpos1.getZ();
    int l2 = 128;
    return (j2 >= -128 && j2 <= 128 && k2 >= -128 && k2 <= 128);
  }
  
  public void sendPacketToServer(Packet<?> packetIn) {
    throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
  }
  
  public LootTableManager getLootTableManager() {
    return this.lootTable;
  }
  
  @Nullable
  public BlockPos func_190528_a(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_) {
    return null;
  }
  
  protected abstract IChunkProvider createChunkProvider();
  
  protected abstract boolean isChunkLoaded(int paramInt1, int paramInt2, boolean paramBoolean);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\World.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */