package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraft.world.storage.loot.LootTableManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IThreadListener {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final MinecraftServer mcServer;
  
  private final EntityTracker theEntityTracker;
  
  private final PlayerChunkMap thePlayerManager;
  
  private final Set<NextTickListEntry> pendingTickListEntriesHashSet = Sets.newHashSet();
  
  private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet = new TreeSet<>();
  
  private final Map<UUID, Entity> entitiesByUuid = Maps.newHashMap();
  
  public boolean disableLevelSaving;
  
  private boolean allPlayersSleeping;
  
  private int updateEntityTick;
  
  private final Teleporter worldTeleporter;
  
  private final WorldEntitySpawner entitySpawner = new WorldEntitySpawner();
  
  protected final VillageSiege villageSiege = new VillageSiege(this);
  
  private final ServerBlockEventList[] blockEventQueue = new ServerBlockEventList[] { new ServerBlockEventList(null), new ServerBlockEventList(null) };
  
  private int blockEventCacheIndex;
  
  private final List<NextTickListEntry> pendingTickListEntriesThisTick = Lists.newArrayList();
  
  public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId, Profiler profilerIn) {
    super(saveHandlerIn, info, DimensionType.getById(dimensionId).createDimension(), profilerIn, false);
    this.mcServer = server;
    this.theEntityTracker = new EntityTracker(this);
    this.thePlayerManager = new PlayerChunkMap(this);
    this.provider.registerWorld(this);
    this.chunkProvider = createChunkProvider();
    this.worldTeleporter = new Teleporter(this);
    calculateInitialSkylight();
    calculateInitialWeather();
    getWorldBorder().setSize(server.getMaxWorldSize());
  }
  
  public World init() {
    this.mapStorage = new MapStorage(this.saveHandler);
    String s = VillageCollection.fileNameForProvider(this.provider);
    VillageCollection villagecollection = (VillageCollection)this.mapStorage.getOrLoadData(VillageCollection.class, s);
    if (villagecollection == null) {
      this.villageCollectionObj = new VillageCollection(this);
      this.mapStorage.setData(s, (WorldSavedData)this.villageCollectionObj);
    } else {
      this.villageCollectionObj = villagecollection;
      this.villageCollectionObj.setWorldsForAll(this);
    } 
    this.worldScoreboard = (Scoreboard)new ServerScoreboard(this.mcServer);
    ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)this.mapStorage.getOrLoadData(ScoreboardSaveData.class, "scoreboard");
    if (scoreboardsavedata == null) {
      scoreboardsavedata = new ScoreboardSaveData();
      this.mapStorage.setData("scoreboard", (WorldSavedData)scoreboardsavedata);
    } 
    scoreboardsavedata.setScoreboard(this.worldScoreboard);
    ((ServerScoreboard)this.worldScoreboard).addDirtyRunnable((Runnable)new WorldSavedDataCallableSave((WorldSavedData)scoreboardsavedata));
    this.lootTable = new LootTableManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "loot_tables"));
    this.field_191951_C = new AdvancementManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "advancements"));
    this.field_193036_D = new FunctionManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "functions"), this.mcServer);
    getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
    getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
    getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
    getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
    getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
    if (this.worldInfo.getBorderLerpTime() > 0L) {
      getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
    } else {
      getWorldBorder().setTransition(this.worldInfo.getBorderSize());
    } 
    return this;
  }
  
  public void tick() {
    super.tick();
    if (getWorldInfo().isHardcoreModeEnabled() && getDifficulty() != EnumDifficulty.HARD)
      getWorldInfo().setDifficulty(EnumDifficulty.HARD); 
    this.provider.getBiomeProvider().cleanupCache();
    if (areAllPlayersAsleep()) {
      if (getGameRules().getBoolean("doDaylightCycle")) {
        long i = this.worldInfo.getWorldTime() + 24000L;
        this.worldInfo.setWorldTime(i - i % 24000L);
      } 
      wakeAllPlayers();
    } 
    this.theProfiler.startSection("mobSpawner");
    if (getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD)
      this.entitySpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, (this.worldInfo.getWorldTotalTime() % 400L == 0L)); 
    this.theProfiler.endStartSection("chunkSource");
    this.chunkProvider.unloadQueuedChunks();
    int j = calculateSkylightSubtracted(1.0F);
    if (j != getSkylightSubtracted())
      setSkylightSubtracted(j); 
    this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
    if (getGameRules().getBoolean("doDaylightCycle"))
      this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L); 
    this.theProfiler.endStartSection("tickPending");
    tickUpdates(false);
    this.theProfiler.endStartSection("tickBlocks");
    updateBlocks();
    this.theProfiler.endStartSection("chunkMap");
    this.thePlayerManager.tick();
    this.theProfiler.endStartSection("village");
    this.villageCollectionObj.tick();
    this.villageSiege.tick();
    this.theProfiler.endStartSection("portalForcer");
    this.worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
    this.theProfiler.endSection();
    sendQueuedBlockEvents();
  }
  
  @Nullable
  public Biome.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType creatureType, BlockPos pos) {
    List<Biome.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
    return (list != null && !list.isEmpty()) ? (Biome.SpawnListEntry)WeightedRandom.getRandomItem(this.rand, list) : null;
  }
  
  public boolean canCreatureTypeSpawnHere(EnumCreatureType creatureType, Biome.SpawnListEntry spawnListEntry, BlockPos pos) {
    List<Biome.SpawnListEntry> list = getChunkProvider().getPossibleCreatures(creatureType, pos);
    return (list != null && !list.isEmpty()) ? list.contains(spawnListEntry) : false;
  }
  
  public void updateAllPlayersSleepingFlag() {
    this.allPlayersSleeping = false;
    if (!this.playerEntities.isEmpty()) {
      int i = 0;
      int j = 0;
      for (EntityPlayer entityplayer : this.playerEntities) {
        if (entityplayer.isSpectator()) {
          i++;
          continue;
        } 
        if (entityplayer.isPlayerSleeping())
          j++; 
      } 
      this.allPlayersSleeping = (j > 0 && j >= this.playerEntities.size() - i);
    } 
  }
  
  protected void wakeAllPlayers() {
    this.allPlayersSleeping = false;
    for (EntityPlayer entityplayer : this.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).collect(Collectors.toList()))
      entityplayer.wakeUpPlayer(false, false, true); 
    if (getGameRules().getBoolean("doWeatherCycle"))
      resetRainAndThunder(); 
  }
  
  private void resetRainAndThunder() {
    this.worldInfo.setRainTime(0);
    this.worldInfo.setRaining(false);
    this.worldInfo.setThunderTime(0);
    this.worldInfo.setThundering(false);
  }
  
  public boolean areAllPlayersAsleep() {
    if (this.allPlayersSleeping && !this.isRemote) {
      for (EntityPlayer entityplayer : this.playerEntities) {
        if (!entityplayer.isSpectator() && !entityplayer.isPlayerFullyAsleep())
          return false; 
      } 
      return true;
    } 
    return false;
  }
  
  public void setInitialSpawnLocation() {
    if (this.worldInfo.getSpawnY() <= 0)
      this.worldInfo.setSpawnY(getSeaLevel() + 1); 
    int i = this.worldInfo.getSpawnX();
    int j = this.worldInfo.getSpawnZ();
    int k = 0;
    while (getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.AIR) {
      i += this.rand.nextInt(8) - this.rand.nextInt(8);
      j += this.rand.nextInt(8) - this.rand.nextInt(8);
      k++;
      if (k == 10000)
        break; 
    } 
    this.worldInfo.setSpawnX(i);
    this.worldInfo.setSpawnZ(j);
  }
  
  protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
    return getChunkProvider().chunkExists(x, z);
  }
  
  protected void playerCheckLight() {
    this.theProfiler.startSection("playerCheckLight");
    if (!this.playerEntities.isEmpty()) {
      int i = this.rand.nextInt(this.playerEntities.size());
      EntityPlayer entityplayer = this.playerEntities.get(i);
      int j = MathHelper.floor(entityplayer.posX) + this.rand.nextInt(11) - 5;
      int k = MathHelper.floor(entityplayer.posY) + this.rand.nextInt(11) - 5;
      int l = MathHelper.floor(entityplayer.posZ) + this.rand.nextInt(11) - 5;
      checkLight(new BlockPos(j, k, l));
    } 
    this.theProfiler.endSection();
  }
  
  protected void updateBlocks() {
    playerCheckLight();
    if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
      Iterator<Chunk> iterator1 = this.thePlayerManager.getChunkIterator();
      while (iterator1.hasNext())
        ((Chunk)iterator1.next()).onTick(false); 
    } else {
      int i = getGameRules().getInt("randomTickSpeed");
      boolean flag = isRaining();
      boolean flag1 = isThundering();
      this.theProfiler.startSection("pollingChunks");
      for (Iterator<Chunk> iterator = this.thePlayerManager.getChunkIterator(); iterator.hasNext(); this.theProfiler.endSection()) {
        this.theProfiler.startSection("getChunk");
        Chunk chunk = iterator.next();
        int j = chunk.xPosition * 16;
        int k = chunk.zPosition * 16;
        this.theProfiler.endStartSection("checkNextLight");
        chunk.enqueueRelightChecks();
        this.theProfiler.endStartSection("tickChunk");
        chunk.onTick(false);
        this.theProfiler.endStartSection("thunder");
        if (flag && flag1 && this.rand.nextInt(100000) == 0) {
          this.updateLCG = this.updateLCG * 3 + 1013904223;
          int l = this.updateLCG >> 2;
          BlockPos blockpos = adjustPosToNearbyEntity(new BlockPos(j + (l & 0xF), 0, k + (l >> 8 & 0xF)));
          if (isRainingAt(blockpos)) {
            DifficultyInstance difficultyinstance = getDifficultyForLocation(blockpos);
            if (getGameRules().getBoolean("doMobSpawning") && this.rand.nextDouble() < difficultyinstance.getAdditionalDifficulty() * 0.01D) {
              EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this);
              entityskeletonhorse.func_190691_p(true);
              entityskeletonhorse.setGrowingAge(0);
              entityskeletonhorse.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
              spawnEntityInWorld((Entity)entityskeletonhorse);
              addWeatherEffect((Entity)new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), true));
            } else {
              addWeatherEffect((Entity)new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), false));
            } 
          } 
        } 
        this.theProfiler.endStartSection("iceandsnow");
        if (this.rand.nextInt(16) == 0) {
          this.updateLCG = this.updateLCG * 3 + 1013904223;
          int j2 = this.updateLCG >> 2;
          BlockPos blockpos1 = getPrecipitationHeight(new BlockPos(j + (j2 & 0xF), 0, k + (j2 >> 8 & 0xF)));
          BlockPos blockpos2 = blockpos1.down();
          if (canBlockFreezeNoWater(blockpos2))
            setBlockState(blockpos2, Blocks.ICE.getDefaultState()); 
          if (flag && canSnowAt(blockpos1, true))
            setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState()); 
          if (flag && getBiome(blockpos2).canRain())
            getBlockState(blockpos2).getBlock().fillWithRain(this, blockpos2); 
        } 
        this.theProfiler.endStartSection("tickBlocks");
        if (i > 0) {
          byte b;
          int m;
          ExtendedBlockStorage[] arrayOfExtendedBlockStorage;
          for (m = (arrayOfExtendedBlockStorage = chunk.getBlockStorageArray()).length, b = 0; b < m; ) {
            ExtendedBlockStorage extendedblockstorage = arrayOfExtendedBlockStorage[b];
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && extendedblockstorage.getNeedsRandomTick())
              for (int i1 = 0; i1 < i; i1++) {
                this.updateLCG = this.updateLCG * 3 + 1013904223;
                int j1 = this.updateLCG >> 2;
                int k1 = j1 & 0xF;
                int l1 = j1 >> 8 & 0xF;
                int i2 = j1 >> 16 & 0xF;
                IBlockState iblockstate = extendedblockstorage.get(k1, i2, l1);
                Block block = iblockstate.getBlock();
                this.theProfiler.startSection("randomTick");
                if (block.getTickRandomly())
                  block.randomTick(this, new BlockPos(k1 + j, i2 + extendedblockstorage.getYLocation(), l1 + k), iblockstate, this.rand); 
                this.theProfiler.endSection();
              }  
            b++;
          } 
        } 
      } 
      this.theProfiler.endSection();
    } 
  }
  
  protected BlockPos adjustPosToNearbyEntity(BlockPos pos) {
    BlockPos blockpos = getPrecipitationHeight(pos);
    AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), getHeight(), blockpos.getZ()))).expandXyz(3.0D);
    List<EntityLivingBase> list = getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new Predicate<EntityLivingBase>() {
          public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
            return (p_apply_1_ != null && p_apply_1_.isEntityAlive() && WorldServer.this.canSeeSky(p_apply_1_.getPosition()));
          }
        });
    if (!list.isEmpty())
      return ((EntityLivingBase)list.get(this.rand.nextInt(list.size()))).getPosition(); 
    if (blockpos.getY() == -1)
      blockpos = blockpos.up(2); 
    return blockpos;
  }
  
  public boolean isBlockTickPending(BlockPos pos, Block blockType) {
    NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockType);
    return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
  }
  
  public boolean isUpdateScheduled(BlockPos pos, Block blk) {
    NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blk);
    return this.pendingTickListEntriesHashSet.contains(nextticklistentry);
  }
  
  public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {
    updateBlockTick(pos, blockIn, delay, 0);
  }
  
  public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {
    Material material = blockIn.getDefaultState().getMaterial();
    if (this.scheduledUpdatesAreImmediate && material != Material.AIR) {
      if (blockIn.requiresUpdates()) {
        if (isAreaLoaded(pos.add(-8, -8, -8), pos.add(8, 8, 8))) {
          IBlockState iblockstate = getBlockState(pos);
          if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() == blockIn)
            iblockstate.getBlock().updateTick(this, pos, iblockstate, this.rand); 
        } 
        return;
      } 
      delay = 1;
    } 
    NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
    if (isBlockLoaded(pos)) {
      if (material != Material.AIR) {
        nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
        nextticklistentry.setPriority(priority);
      } 
      if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
        this.pendingTickListEntriesHashSet.add(nextticklistentry);
        this.pendingTickListEntriesTreeSet.add(nextticklistentry);
      } 
    } 
  }
  
  public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {
    NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
    nextticklistentry.setPriority(priority);
    Material material = blockIn.getDefaultState().getMaterial();
    if (material != Material.AIR)
      nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime()); 
    if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
      this.pendingTickListEntriesHashSet.add(nextticklistentry);
      this.pendingTickListEntriesTreeSet.add(nextticklistentry);
    } 
  }
  
  public void updateEntities() {
    if (this.playerEntities.isEmpty()) {
      if (this.updateEntityTick++ >= 300)
        return; 
    } else {
      resetUpdateEntityTick();
    } 
    this.provider.onWorldUpdateEntities();
    super.updateEntities();
  }
  
  protected void tickPlayers() {
    super.tickPlayers();
    this.theProfiler.endStartSection("players");
    for (int i = 0; i < this.playerEntities.size(); i++) {
      Entity entity = (Entity)this.playerEntities.get(i);
      Entity entity1 = entity.getRidingEntity();
      if (entity1 != null) {
        if (!entity1.isDead && entity1.isPassenger(entity))
          continue; 
        entity.dismountRidingEntity();
      } 
      this.theProfiler.startSection("tick");
      if (!entity.isDead)
        try {
          updateEntity(entity);
        } catch (Throwable throwable) {
          CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
          CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
          entity.addEntityCrashInfo(crashreportcategory);
          throw new ReportedException(crashreport);
        }  
      this.theProfiler.endSection();
      this.theProfiler.startSection("remove");
      if (entity.isDead) {
        int j = entity.chunkCoordX;
        int k = entity.chunkCoordZ;
        if (entity.addedToChunk && isChunkLoaded(j, k, true))
          getChunkFromChunkCoords(j, k).removeEntity(entity); 
        this.loadedEntityList.remove(entity);
        onEntityRemoved(entity);
      } 
      this.theProfiler.endSection();
      continue;
    } 
  }
  
  public void resetUpdateEntityTick() {
    this.updateEntityTick = 0;
  }
  
  public boolean tickUpdates(boolean p_72955_1_) {
    if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
      return false; 
    int i = this.pendingTickListEntriesTreeSet.size();
    if (i != this.pendingTickListEntriesHashSet.size())
      throw new IllegalStateException("TickNextTick list out of synch"); 
    if (i > 65536)
      i = 65536; 
    this.theProfiler.startSection("cleaning");
    for (int j = 0; j < i; j++) {
      NextTickListEntry nextticklistentry = this.pendingTickListEntriesTreeSet.first();
      if (!p_72955_1_ && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime())
        break; 
      this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
      this.pendingTickListEntriesHashSet.remove(nextticklistentry);
      this.pendingTickListEntriesThisTick.add(nextticklistentry);
    } 
    this.theProfiler.endSection();
    this.theProfiler.startSection("ticking");
    Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();
    while (iterator.hasNext()) {
      NextTickListEntry nextticklistentry1 = iterator.next();
      iterator.remove();
      int k = 0;
      if (isAreaLoaded(nextticklistentry1.position.add(0, 0, 0), nextticklistentry1.position.add(0, 0, 0))) {
        IBlockState iblockstate = getBlockState(nextticklistentry1.position);
        if (iblockstate.getMaterial() != Material.AIR && Block.isEqualTo(iblockstate.getBlock(), nextticklistentry1.getBlock()))
          try {
            iblockstate.getBlock().updateTick(this, nextticklistentry1.position, iblockstate, this.rand);
          } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
            CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry1.position, iblockstate);
            throw new ReportedException(crashreport);
          }  
        continue;
      } 
      scheduleUpdate(nextticklistentry1.position, nextticklistentry1.getBlock(), 0);
    } 
    this.theProfiler.endSection();
    this.pendingTickListEntriesThisTick.clear();
    return !this.pendingTickListEntriesTreeSet.isEmpty();
  }
  
  @Nullable
  public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_) {
    ChunkPos chunkpos = chunkIn.getChunkCoordIntPair();
    int i = (chunkpos.chunkXPos << 4) - 2;
    int j = i + 16 + 2;
    int k = (chunkpos.chunkZPos << 4) - 2;
    int l = k + 16 + 2;
    return getPendingBlockUpdates(new StructureBoundingBox(i, 0, k, j, 256, l), p_72920_2_);
  }
  
  @Nullable
  public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean p_175712_2_) {
    List<NextTickListEntry> list = null;
    for (int i = 0; i < 2; i++) {
      Iterator<NextTickListEntry> iterator;
      if (i == 0) {
        iterator = this.pendingTickListEntriesTreeSet.iterator();
      } else {
        iterator = this.pendingTickListEntriesThisTick.iterator();
      } 
      while (iterator.hasNext()) {
        NextTickListEntry nextticklistentry = iterator.next();
        BlockPos blockpos = nextticklistentry.position;
        if (blockpos.getX() >= structureBB.minX && blockpos.getX() < structureBB.maxX && blockpos.getZ() >= structureBB.minZ && blockpos.getZ() < structureBB.maxZ) {
          if (p_175712_2_) {
            if (i == 0)
              this.pendingTickListEntriesHashSet.remove(nextticklistentry); 
            iterator.remove();
          } 
          if (list == null)
            list = Lists.newArrayList(); 
          list.add(nextticklistentry);
        } 
      } 
    } 
    return list;
  }
  
  public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
    if (!canSpawnAnimals() && (entityIn instanceof net.minecraft.entity.passive.EntityAnimal || entityIn instanceof net.minecraft.entity.passive.EntityWaterMob))
      entityIn.setDead(); 
    if (!canSpawnNPCs() && entityIn instanceof net.minecraft.entity.INpc)
      entityIn.setDead(); 
    super.updateEntityWithOptionalForce(entityIn, forceUpdate);
  }
  
  private boolean canSpawnNPCs() {
    return this.mcServer.getCanSpawnNPCs();
  }
  
  private boolean canSpawnAnimals() {
    return this.mcServer.getCanSpawnAnimals();
  }
  
  protected IChunkProvider createChunkProvider() {
    IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);
    return (IChunkProvider)new ChunkProviderServer(this, ichunkloader, this.provider.createChunkGenerator());
  }
  
  public boolean isBlockModifiable(EntityPlayer player, BlockPos pos) {
    return (!this.mcServer.isBlockProtected(this, pos, player) && getWorldBorder().contains(pos));
  }
  
  public void initialize(WorldSettings settings) {
    if (!this.worldInfo.isInitialized()) {
      try {
        createSpawnPosition(settings);
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD)
          setDebugWorldSettings(); 
        super.initialize(settings);
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");
        try {
          addWorldInfoToCrashReport(crashreport);
        } catch (Throwable throwable1) {}
        throw new ReportedException(crashreport);
      } 
      this.worldInfo.setServerInitialized(true);
    } 
  }
  
  private void setDebugWorldSettings() {
    this.worldInfo.setMapFeaturesEnabled(false);
    this.worldInfo.setAllowCommands(true);
    this.worldInfo.setRaining(false);
    this.worldInfo.setThundering(false);
    this.worldInfo.setCleanWeatherTime(1000000000);
    this.worldInfo.setWorldTime(6000L);
    this.worldInfo.setGameType(GameType.SPECTATOR);
    this.worldInfo.setHardcore(false);
    this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
    this.worldInfo.setDifficultyLocked(true);
    getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
  }
  
  private void createSpawnPosition(WorldSettings settings) {
    if (!this.provider.canRespawnHere()) {
      this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
    } else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
      this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
    } else {
      this.findingSpawnPoint = true;
      BiomeProvider biomeprovider = this.provider.getBiomeProvider();
      List<Biome> list = biomeprovider.getBiomesToSpawnIn();
      Random random = new Random(getSeed());
      BlockPos blockpos = biomeprovider.findBiomePosition(0, 0, 256, list, random);
      int i = 8;
      int j = this.provider.getAverageGroundLevel();
      int k = 8;
      if (blockpos != null) {
        i = blockpos.getX();
        k = blockpos.getZ();
      } else {
        LOGGER.warn("Unable to find spawn biome");
      } 
      int l = 0;
      while (!this.provider.canCoordinateBeSpawn(i, k)) {
        i += random.nextInt(64) - random.nextInt(64);
        k += random.nextInt(64) - random.nextInt(64);
        l++;
        if (l == 1000)
          break; 
      } 
      this.worldInfo.setSpawn(new BlockPos(i, j, k));
      this.findingSpawnPoint = false;
      if (settings.isBonusChestEnabled())
        createBonusChest(); 
    } 
  }
  
  protected void createBonusChest() {
    WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest();
    for (int i = 0; i < 10; i++) {
      int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
      int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
      BlockPos blockpos = getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();
      if (worldgeneratorbonuschest.generate(this, this.rand, blockpos))
        break; 
    } 
  }
  
  @Nullable
  public BlockPos getSpawnCoordinate() {
    return this.provider.getSpawnCoordinate();
  }
  
  public void saveAllChunks(boolean p_73044_1_, @Nullable IProgressUpdate progressCallback) throws MinecraftException {
    ChunkProviderServer chunkproviderserver = getChunkProvider();
    if (chunkproviderserver.canSave()) {
      if (progressCallback != null)
        progressCallback.displaySavingString("Saving level"); 
      saveLevel();
      if (progressCallback != null)
        progressCallback.displayLoadingString("Saving chunks"); 
      chunkproviderserver.saveChunks(p_73044_1_);
      for (Chunk chunk : Lists.newArrayList(chunkproviderserver.getLoadedChunks())) {
        if (chunk != null && !this.thePlayerManager.contains(chunk.xPosition, chunk.zPosition))
          chunkproviderserver.unload(chunk); 
      } 
    } 
  }
  
  public void saveChunkData() {
    ChunkProviderServer chunkproviderserver = getChunkProvider();
    if (chunkproviderserver.canSave())
      chunkproviderserver.saveExtraData(); 
  }
  
  protected void saveLevel() throws MinecraftException {
    checkSessionLock();
    byte b;
    int i;
    WorldServer[] arrayOfWorldServer;
    for (i = (arrayOfWorldServer = this.mcServer.worldServers).length, b = 0; b < i; ) {
      WorldServer worldserver = arrayOfWorldServer[b];
      if (worldserver instanceof WorldServerMulti)
        ((WorldServerMulti)worldserver).saveAdditionalData(); 
      b++;
    } 
    this.worldInfo.setBorderSize(getWorldBorder().getDiameter());
    this.worldInfo.getBorderCenterX(getWorldBorder().getCenterX());
    this.worldInfo.getBorderCenterZ(getWorldBorder().getCenterZ());
    this.worldInfo.setBorderSafeZone(getWorldBorder().getDamageBuffer());
    this.worldInfo.setBorderDamagePerBlock(getWorldBorder().getDamageAmount());
    this.worldInfo.setBorderWarningDistance(getWorldBorder().getWarningDistance());
    this.worldInfo.setBorderWarningTime(getWorldBorder().getWarningTime());
    this.worldInfo.setBorderLerpTarget(getWorldBorder().getTargetSize());
    this.worldInfo.setBorderLerpTime(getWorldBorder().getTimeUntilTarget());
    this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getPlayerList().getHostPlayerData());
    this.mapStorage.saveAllData();
  }
  
  public boolean spawnEntityInWorld(Entity entityIn) {
    return canAddEntity(entityIn) ? super.spawnEntityInWorld(entityIn) : false;
  }
  
  public void loadEntities(Collection<Entity> entityCollection) {
    for (Entity entity : Lists.newArrayList(entityCollection)) {
      if (canAddEntity(entity)) {
        this.loadedEntityList.add(entity);
        onEntityAdded(entity);
      } 
    } 
  }
  
  private boolean canAddEntity(Entity entityIn) {
    if (entityIn.isDead) {
      LOGGER.warn("Tried to add entity {} but it was marked as removed already", EntityList.func_191301_a(entityIn));
      return false;
    } 
    UUID uuid = entityIn.getUniqueID();
    if (this.entitiesByUuid.containsKey(uuid)) {
      Entity entity = this.entitiesByUuid.get(uuid);
      if (this.unloadedEntityList.contains(entity)) {
        this.unloadedEntityList.remove(entity);
      } else {
        if (!(entityIn instanceof EntityPlayer)) {
          LOGGER.warn("Keeping entity {} that already exists with UUID {}", EntityList.func_191301_a(entity), uuid.toString());
          return false;
        } 
        LOGGER.warn("Force-added player with duplicate UUID {}", uuid.toString());
      } 
      removeEntityDangerously(entity);
    } 
    return true;
  }
  
  protected void onEntityAdded(Entity entityIn) {
    super.onEntityAdded(entityIn);
    this.entitiesById.addKey(entityIn.getEntityId(), entityIn);
    this.entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
    Entity[] aentity = entityIn.getParts();
    if (aentity != null) {
      byte b;
      int i;
      Entity[] arrayOfEntity;
      for (i = (arrayOfEntity = aentity).length, b = 0; b < i; ) {
        Entity entity = arrayOfEntity[b];
        this.entitiesById.addKey(entity.getEntityId(), entity);
        b++;
      } 
    } 
  }
  
  protected void onEntityRemoved(Entity entityIn) {
    super.onEntityRemoved(entityIn);
    this.entitiesById.removeObject(entityIn.getEntityId());
    this.entitiesByUuid.remove(entityIn.getUniqueID());
    Entity[] aentity = entityIn.getParts();
    if (aentity != null) {
      byte b;
      int i;
      Entity[] arrayOfEntity;
      for (i = (arrayOfEntity = aentity).length, b = 0; b < i; ) {
        Entity entity = arrayOfEntity[b];
        this.entitiesById.removeObject(entity.getEntityId());
        b++;
      } 
    } 
  }
  
  public boolean addWeatherEffect(Entity entityIn) {
    if (super.addWeatherEffect(entityIn)) {
      this.mcServer.getPlayerList().sendToAllNearExcept(null, entityIn.posX, entityIn.posY, entityIn.posZ, 512.0D, this.provider.getDimensionType().getId(), (Packet)new SPacketSpawnGlobalEntity(entityIn));
      return true;
    } 
    return false;
  }
  
  public void setEntityState(Entity entityIn, byte state) {
    getEntityTracker().sendToTrackingAndSelf(entityIn, (Packet)new SPacketEntityStatus(entityIn, state));
  }
  
  public ChunkProviderServer getChunkProvider() {
    return (ChunkProviderServer)super.getChunkProvider();
  }
  
  public Explosion newExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
    Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
    explosion.doExplosionA();
    explosion.doExplosionB(false);
    if (!isSmoking)
      explosion.clearAffectedBlockPositions(); 
    for (EntityPlayer entityplayer : this.playerEntities) {
      if (entityplayer.getDistanceSq(x, y, z) < 4096.0D)
        ((EntityPlayerMP)entityplayer).connection.sendPacket((Packet)new SPacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer))); 
    } 
    return explosion;
  }
  
  public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
    BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);
    for (BlockEventData blockeventdata1 : this.blockEventQueue[this.blockEventCacheIndex]) {
      if (blockeventdata1.equals(blockeventdata))
        return; 
    } 
    this.blockEventQueue[this.blockEventCacheIndex].add(blockeventdata);
  }
  
  private void sendQueuedBlockEvents() {
    while (!this.blockEventQueue[this.blockEventCacheIndex].isEmpty()) {
      int i = this.blockEventCacheIndex;
      this.blockEventCacheIndex ^= 0x1;
      for (BlockEventData blockeventdata : this.blockEventQueue[i]) {
        if (fireBlockEvent(blockeventdata))
          this.mcServer.getPlayerList().sendToAllNearExcept(null, blockeventdata.getPosition().getX(), blockeventdata.getPosition().getY(), blockeventdata.getPosition().getZ(), 64.0D, this.provider.getDimensionType().getId(), (Packet)new SPacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter())); 
      } 
      this.blockEventQueue[i].clear();
    } 
  }
  
  private boolean fireBlockEvent(BlockEventData event) {
    IBlockState iblockstate = getBlockState(event.getPosition());
    return (iblockstate.getBlock() == event.getBlock()) ? iblockstate.onBlockEventReceived(this, event.getPosition(), event.getEventID(), event.getEventParameter()) : false;
  }
  
  public void flush() {
    this.saveHandler.flush();
  }
  
  protected void updateWeather() {
    boolean flag = isRaining();
    super.updateWeather();
    if (this.prevRainingStrength != this.rainingStrength)
      this.mcServer.getPlayerList().sendPacketToAllPlayersInDimension((Packet)new SPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionType().getId()); 
    if (this.prevThunderingStrength != this.thunderingStrength)
      this.mcServer.getPlayerList().sendPacketToAllPlayersInDimension((Packet)new SPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionType().getId()); 
    if (flag != isRaining()) {
      if (flag) {
        this.mcServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(2, 0.0F));
      } else {
        this.mcServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(1, 0.0F));
      } 
      this.mcServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(7, this.rainingStrength));
      this.mcServer.getPlayerList().sendPacketToAllPlayers((Packet)new SPacketChangeGameState(8, this.thunderingStrength));
    } 
  }
  
  @Nullable
  public MinecraftServer getMinecraftServer() {
    return this.mcServer;
  }
  
  public EntityTracker getEntityTracker() {
    return this.theEntityTracker;
  }
  
  public PlayerChunkMap getPlayerChunkMap() {
    return this.thePlayerManager;
  }
  
  public Teleporter getDefaultTeleporter() {
    return this.worldTeleporter;
  }
  
  public TemplateManager getStructureTemplateManager() {
    return this.saveHandler.getStructureTemplateManager();
  }
  
  public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments) {
    spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, xOffset, yOffset, zOffset, particleSpeed, particleArguments);
  }
  
  public void spawnParticle(EnumParticleTypes particleType, boolean longDistance, double xCoord, double yCoord, double zCoord, int numberOfParticles, double xOffset, double yOffset, double zOffset, double particleSpeed, int... particleArguments) {
    SPacketParticles spacketparticles = new SPacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, particleArguments);
    for (int i = 0; i < this.playerEntities.size(); i++) {
      EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playerEntities.get(i);
      sendPacketWithinDistance(entityplayermp, longDistance, xCoord, yCoord, zCoord, (Packet<?>)spacketparticles);
    } 
  }
  
  public void spawnParticle(EntityPlayerMP player, EnumParticleTypes particle, boolean longDistance, double x, double y, double z, int count, double xOffset, double yOffset, double zOffset, double speed, int... arguments) {
    SPacketParticles sPacketParticles = new SPacketParticles(particle, longDistance, (float)x, (float)y, (float)z, (float)xOffset, (float)yOffset, (float)zOffset, (float)speed, count, arguments);
    sendPacketWithinDistance(player, longDistance, x, y, z, (Packet<?>)sPacketParticles);
  }
  
  private void sendPacketWithinDistance(EntityPlayerMP player, boolean longDistance, double x, double y, double z, Packet<?> packetIn) {
    BlockPos blockpos = player.getPosition();
    double d0 = blockpos.distanceSq(x, y, z);
    if (d0 <= 1024.0D || (longDistance && d0 <= 262144.0D))
      player.connection.sendPacket(packetIn); 
  }
  
  @Nullable
  public Entity getEntityFromUuid(UUID uuid) {
    return this.entitiesByUuid.get(uuid);
  }
  
  public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
    return this.mcServer.addScheduledTask(runnableToSchedule);
  }
  
  public boolean isCallingFromMinecraftThread() {
    return this.mcServer.isCallingFromMinecraftThread();
  }
  
  @Nullable
  public BlockPos func_190528_a(String p_190528_1_, BlockPos p_190528_2_, boolean p_190528_3_) {
    return getChunkProvider().getStrongholdGen(this, p_190528_1_, p_190528_2_, p_190528_3_);
  }
  
  public AdvancementManager func_191952_z() {
    return this.field_191951_C;
  }
  
  public FunctionManager func_193037_A() {
    return this.field_193036_D;
  }
  
  static class ServerBlockEventList extends ArrayList<BlockEventData> {
    private ServerBlockEventList() {}
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\WorldServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */