package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final WorldServer theWorld;
  
  private final Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
  
  private final IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap();
  
  private int maxTrackingDistanceThreshold;
  
  public EntityTracker(WorldServer theWorldIn) {
    this.theWorld = theWorldIn;
    this.maxTrackingDistanceThreshold = theWorldIn.getMinecraftServer().getPlayerList().getEntityViewDistance();
  }
  
  public static long getPositionLong(double value) {
    return MathHelper.lFloor(value * 4096.0D);
  }
  
  public static void updateServerPosition(Entity entityIn, double x, double y, double z) {
    entityIn.serverPosX = getPositionLong(x);
    entityIn.serverPosY = getPositionLong(y);
    entityIn.serverPosZ = getPositionLong(z);
  }
  
  public void trackEntity(Entity entityIn) {
    if (entityIn instanceof EntityPlayerMP) {
      trackEntity(entityIn, 512, 2);
      EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
      for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
        if (entitytrackerentry.getTrackedEntity() != entityplayermp)
          entitytrackerentry.updatePlayerEntity(entityplayermp); 
      } 
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityFishHook) {
      addEntityToTracker(entityIn, 64, 5, true);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityArrow) {
      addEntityToTracker(entityIn, 64, 20, false);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntitySmallFireball) {
      addEntityToTracker(entityIn, 64, 10, false);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityFireball) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntitySnowball) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityLlamaSpit) {
      addEntityToTracker(entityIn, 64, 10, false);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityEnderPearl) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityEnderEye) {
      addEntityToTracker(entityIn, 64, 4, true);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityEgg) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityPotion) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityExpBottle) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityFireworkRocket) {
      addEntityToTracker(entityIn, 64, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityItem) {
      addEntityToTracker(entityIn, 64, 20, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityMinecart) {
      addEntityToTracker(entityIn, 80, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityBoat) {
      addEntityToTracker(entityIn, 80, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.passive.EntitySquid) {
      addEntityToTracker(entityIn, 64, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.boss.EntityWither) {
      addEntityToTracker(entityIn, 80, 3, false);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityShulkerBullet) {
      addEntityToTracker(entityIn, 80, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.passive.EntityBat) {
      addEntityToTracker(entityIn, 80, 3, false);
    } else if (entityIn instanceof net.minecraft.entity.boss.EntityDragon) {
      addEntityToTracker(entityIn, 160, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.passive.IAnimals) {
      addEntityToTracker(entityIn, 80, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityTNTPrimed) {
      addEntityToTracker(entityIn, 160, 10, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityFallingBlock) {
      addEntityToTracker(entityIn, 160, 20, true);
    } else if (entityIn instanceof EntityHanging) {
      addEntityToTracker(entityIn, 160, 2147483647, false);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityArmorStand) {
      addEntityToTracker(entityIn, 160, 3, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityXPOrb) {
      addEntityToTracker(entityIn, 160, 20, true);
    } else if (entityIn instanceof EntityAreaEffectCloud) {
      addEntityToTracker(entityIn, 160, 2147483647, true);
    } else if (entityIn instanceof net.minecraft.entity.item.EntityEnderCrystal) {
      addEntityToTracker(entityIn, 256, 2147483647, false);
    } else if (entityIn instanceof net.minecraft.entity.projectile.EntityEvokerFangs) {
      addEntityToTracker(entityIn, 160, 2, false);
    } 
  }
  
  public void trackEntity(Entity entityIn, int trackingRange, int updateFrequency) {
    addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
  }
  
  public void addEntityToTracker(Entity entityIn, int trackingRange, final int updateFrequency, boolean sendVelocityUpdates) {
    try {
      if (this.trackedEntityHashTable.containsItem(entityIn.getEntityId()))
        throw new IllegalStateException("Entity is already tracked!"); 
      EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, this.maxTrackingDistanceThreshold, updateFrequency, sendVelocityUpdates);
      this.trackedEntities.add(entitytrackerentry);
      this.trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
      entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
      crashreportcategory.addCrashSection("Tracking range", String.valueOf(trackingRange) + " blocks");
      crashreportcategory.setDetail("Update interval", new ICrashReportDetail<String>() {
            public String call() throws Exception {
              String s = "Once per " + updateFrequency + " ticks";
              if (updateFrequency == Integer.MAX_VALUE)
                s = "Maximum (" + s + ")"; 
              return s;
            }
          });
      entityIn.addEntityCrashInfo(crashreportcategory);
      ((EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId())).getTrackedEntity().addEntityCrashInfo(crashreport.makeCategory("Entity That Is Already Tracked"));
      try {
        throw new ReportedException(crashreport);
      } catch (ReportedException reportedexception) {
        LOGGER.error("\"Silently\" catching entity tracking error.", (Throwable)reportedexception);
      } 
    } 
  }
  
  public void untrackEntity(Entity entityIn) {
    if (entityIn instanceof EntityPlayerMP) {
      EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;
      for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
        entitytrackerentry.removeFromTrackedPlayers(entityplayermp); 
    } 
    EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry)this.trackedEntityHashTable.removeObject(entityIn.getEntityId());
    if (entitytrackerentry1 != null) {
      this.trackedEntities.remove(entitytrackerentry1);
      entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
    } 
  }
  
  public void updateTrackedEntities() {
    List<EntityPlayerMP> list = Lists.newArrayList();
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
      entitytrackerentry.updatePlayerList(this.theWorld.playerEntities);
      if (entitytrackerentry.playerEntitiesUpdated) {
        Entity entity = entitytrackerentry.getTrackedEntity();
        if (entity instanceof EntityPlayerMP)
          list.add((EntityPlayerMP)entity); 
      } 
    } 
    for (int i = 0; i < list.size(); i++) {
      EntityPlayerMP entityplayermp = list.get(i);
      for (EntityTrackerEntry entitytrackerentry1 : this.trackedEntities) {
        if (entitytrackerentry1.getTrackedEntity() != entityplayermp)
          entitytrackerentry1.updatePlayerEntity(entityplayermp); 
      } 
    } 
  }
  
  public void updateVisibility(EntityPlayerMP player) {
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
      if (entitytrackerentry.getTrackedEntity() == player) {
        entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities);
        continue;
      } 
      entitytrackerentry.updatePlayerEntity(player);
    } 
  }
  
  public void sendToAllTrackingEntity(Entity entityIn, Packet<?> packetIn) {
    EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
    if (entitytrackerentry != null)
      entitytrackerentry.sendPacketToTrackedPlayers(packetIn); 
  }
  
  public void sendToTrackingAndSelf(Entity entityIn, Packet<?> packetIn) {
    EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry)this.trackedEntityHashTable.lookup(entityIn.getEntityId());
    if (entitytrackerentry != null)
      entitytrackerentry.sendToTrackingAndSelf(packetIn); 
  }
  
  public void removePlayerFromTrackers(EntityPlayerMP player) {
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
      entitytrackerentry.removeTrackedPlayerSymmetric(player); 
  }
  
  public void sendLeashedEntitiesInChunk(EntityPlayerMP player, Chunk chunkIn) {
    List<Entity> list = Lists.newArrayList();
    List<Entity> list1 = Lists.newArrayList();
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities) {
      Entity entity = entitytrackerentry.getTrackedEntity();
      if (entity != player && entity.chunkCoordX == chunkIn.xPosition && entity.chunkCoordZ == chunkIn.zPosition) {
        entitytrackerentry.updatePlayerEntity(player);
        if (entity instanceof EntityLiving && ((EntityLiving)entity).getLeashedToEntity() != null)
          list.add(entity); 
        if (!entity.getPassengers().isEmpty())
          list1.add(entity); 
      } 
    } 
    if (!list.isEmpty())
      for (Entity entity1 : list)
        player.connection.sendPacket((Packet)new SPacketEntityAttach(entity1, ((EntityLiving)entity1).getLeashedToEntity()));  
    if (!list1.isEmpty())
      for (Entity entity2 : list1)
        player.connection.sendPacket((Packet)new SPacketSetPassengers(entity2));  
  }
  
  public void setViewDistance(int p_187252_1_) {
    this.maxTrackingDistanceThreshold = (p_187252_1_ - 1) * 16;
    for (EntityTrackerEntry entitytrackerentry : this.trackedEntities)
      entitytrackerentry.setMaxRange(this.maxTrackingDistanceThreshold); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\EntityTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */