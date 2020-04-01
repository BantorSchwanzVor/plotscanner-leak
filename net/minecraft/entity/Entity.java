package net.minecraft.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Entity implements ICommandSender {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final List<ItemStack> field_190535_b = Collections.emptyList();
  
  private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
  
  private static double renderDistanceWeight = 1.0D;
  
  private static int nextEntityID;
  
  private int entityId;
  
  public boolean preventEntitySpawning;
  
  private final List<Entity> riddenByEntities;
  
  protected int rideCooldown;
  
  private Entity ridingEntity;
  
  public boolean forceSpawn;
  
  public World world;
  
  public double prevPosX;
  
  public double prevPosY;
  
  public double prevPosZ;
  
  public double posX;
  
  public double posY;
  
  public double posZ;
  
  public double motionX;
  
  public double motionY;
  
  public double motionZ;
  
  public float rotationYaw;
  
  public float rotationPitch;
  
  public float prevRotationYaw;
  
  public float prevRotationPitch;
  
  private AxisAlignedBB boundingBox;
  
  public boolean onGround;
  
  public boolean isCollidedHorizontally;
  
  public boolean isCollidedVertically;
  
  public boolean isCollided;
  
  public boolean velocityChanged;
  
  protected boolean isInWeb;
  
  private boolean isOutsideBorder;
  
  public boolean isDead;
  
  public float width;
  
  public float height;
  
  public float prevDistanceWalkedModified;
  
  public float distanceWalkedModified;
  
  public float distanceWalkedOnStepModified;
  
  public float fallDistance;
  
  private int nextStepDistance;
  
  private float field_191959_ay;
  
  public double lastTickPosX;
  
  public double lastTickPosY;
  
  public double lastTickPosZ;
  
  public float stepHeight;
  
  public boolean noClip;
  
  public float entityCollisionReduction;
  
  protected Random rand;
  
  public int ticksExisted;
  
  private int field_190534_ay;
  
  protected boolean inWater;
  
  public int hurtResistantTime;
  
  protected boolean firstUpdate;
  
  protected boolean isImmuneToFire;
  
  protected EntityDataManager dataManager;
  
  protected static final DataParameter<Byte> FLAGS = EntityDataManager.createKey(Entity.class, DataSerializers.BYTE);
  
  private static final DataParameter<Integer> AIR = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);
  
  private static final DataParameter<String> CUSTOM_NAME = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
  
  private static final DataParameter<Boolean> CUSTOM_NAME_VISIBLE = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Boolean> SILENT = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
  
  private static final DataParameter<Boolean> NO_GRAVITY = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
  
  public boolean addedToChunk;
  
  public int chunkCoordX;
  
  public int chunkCoordY;
  
  public int chunkCoordZ;
  
  public long serverPosX;
  
  public long serverPosY;
  
  public long serverPosZ;
  
  public boolean ignoreFrustumCheck;
  
  public boolean isAirBorne;
  
  public int timeUntilPortal;
  
  protected boolean inPortal;
  
  protected int portalCounter;
  
  public int dimension;
  
  protected BlockPos lastPortalPos;
  
  protected Vec3d lastPortalVec;
  
  protected EnumFacing teleportDirection;
  
  private boolean invulnerable;
  
  protected UUID entityUniqueID;
  
  protected String cachedUniqueIdString;
  
  private final CommandResultStats cmdResultStats;
  
  protected boolean glowing;
  
  private final Set<String> tags;
  
  private boolean isPositionDirty;
  
  private final double[] field_191505_aI;
  
  private long field_191506_aJ;
  
  public Entity(World worldIn) {
    this.entityId = nextEntityID++;
    this.riddenByEntities = Lists.newArrayList();
    this.boundingBox = ZERO_AABB;
    this.width = 0.6F;
    this.height = 1.8F;
    this.nextStepDistance = 1;
    this.field_191959_ay = 1.0F;
    this.rand = new Random();
    this.field_190534_ay = -func_190531_bD();
    this.firstUpdate = true;
    this.entityUniqueID = MathHelper.getRandomUUID(this.rand);
    this.cachedUniqueIdString = this.entityUniqueID.toString();
    this.cmdResultStats = new CommandResultStats();
    this.tags = Sets.newHashSet();
    this.field_191505_aI = new double[] { 0.0D, 0.0D, 0.0D };
    this.world = worldIn;
    setPosition(0.0D, 0.0D, 0.0D);
    if (worldIn != null)
      this.dimension = worldIn.provider.getDimensionType().getId(); 
    this.dataManager = new EntityDataManager(this);
    this.dataManager.register(FLAGS, Byte.valueOf((byte)0));
    this.dataManager.register(AIR, Integer.valueOf(300));
    this.dataManager.register(CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
    this.dataManager.register(CUSTOM_NAME, "");
    this.dataManager.register(SILENT, Boolean.valueOf(false));
    this.dataManager.register(NO_GRAVITY, Boolean.valueOf(false));
    entityInit();
  }
  
  public int getEntityId() {
    return this.entityId;
  }
  
  public void setEntityId(int id) {
    this.entityId = id;
  }
  
  public Set<String> getTags() {
    return this.tags;
  }
  
  public boolean addTag(String tag) {
    if (this.tags.size() >= 1024)
      return false; 
    this.tags.add(tag);
    return true;
  }
  
  public boolean removeTag(String tag) {
    return this.tags.remove(tag);
  }
  
  public void onKillCommand() {
    setDead();
  }
  
  public EntityDataManager getDataManager() {
    return this.dataManager;
  }
  
  public boolean equals(Object p_equals_1_) {
    if (p_equals_1_ instanceof Entity)
      return (((Entity)p_equals_1_).entityId == this.entityId); 
    return false;
  }
  
  public int hashCode() {
    return this.entityId;
  }
  
  protected void preparePlayerToSpawn() {
    if (this.world != null) {
      while (this.posY > 0.0D && this.posY < 256.0D) {
        setPosition(this.posX, this.posY, this.posZ);
        if (this.world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty())
          break; 
        this.posY++;
      } 
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      this.rotationPitch = 0.0F;
    } 
  }
  
  public void setDead() {
    this.isDead = true;
  }
  
  public void setDropItemsWhenDead(boolean dropWhenDead) {}
  
  protected void setSize(float width, float height) {
    if (width != this.width || height != this.height) {
      float f = this.width;
      this.width = width;
      this.height = height;
      if (this.width < f) {
        double d0 = width / 2.0D;
        setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + this.height, this.posZ + d0));
        return;
      } 
      AxisAlignedBB axisalignedbb = getEntityBoundingBox();
      setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + this.width, axisalignedbb.minY + this.height, axisalignedbb.minZ + this.width));
      if (this.width > f && !this.firstUpdate && !this.world.isRemote)
        moveEntity(MoverType.SELF, (f - this.width), 0.0D, (f - this.width)); 
    } 
  }
  
  protected void setRotation(float yaw, float pitch) {
    this.rotationYaw = yaw % 360.0F;
    this.rotationPitch = pitch % 360.0F;
  }
  
  public void setPosition(double x, double y, double z) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    float f = this.width / 2.0F;
    float f1 = this.height;
    setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f1, z + f));
  }
  
  public void setAngles(float yaw, float pitch) {
    float f = this.rotationPitch;
    float f1 = this.rotationYaw;
    this.rotationYaw = (float)(this.rotationYaw + yaw * 0.15D);
    this.rotationPitch = (float)(this.rotationPitch - pitch * 0.15D);
    this.rotationPitch = MathHelper.clamp(this.rotationPitch, -90.0F, 90.0F);
    this.prevRotationPitch += this.rotationPitch - f;
    this.prevRotationYaw += this.rotationYaw - f1;
    if (this.ridingEntity != null)
      this.ridingEntity.applyOrientationToEntity(this); 
  }
  
  public void onUpdate() {
    if (!this.world.isRemote)
      setFlag(6, isGlowing()); 
    onEntityUpdate();
  }
  
  public void onEntityUpdate() {
    this.world.theProfiler.startSection("entityBaseTick");
    if (isRiding() && (getRidingEntity()).isDead)
      dismountRidingEntity(); 
    if (this.rideCooldown > 0)
      this.rideCooldown--; 
    this.prevDistanceWalkedModified = this.distanceWalkedModified;
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.prevRotationPitch = this.rotationPitch;
    this.prevRotationYaw = this.rotationYaw;
    if (!this.world.isRemote && this.world instanceof WorldServer) {
      this.world.theProfiler.startSection("portal");
      if (this.inPortal) {
        MinecraftServer minecraftserver = this.world.getMinecraftServer();
        if (minecraftserver.getAllowNether()) {
          if (!isRiding()) {
            int i = getMaxInPortalTime();
            if (this.portalCounter++ >= i) {
              int j;
              this.portalCounter = i;
              this.timeUntilPortal = getPortalCooldown();
              if (this.world.provider.getDimensionType().getId() == -1) {
                j = 0;
              } else {
                j = -1;
              } 
              changeDimension(j);
            } 
          } 
          this.inPortal = false;
        } 
      } else {
        if (this.portalCounter > 0)
          this.portalCounter -= 4; 
        if (this.portalCounter < 0)
          this.portalCounter = 0; 
      } 
      decrementTimeUntilPortal();
      this.world.theProfiler.endSection();
    } 
    spawnRunningParticles();
    handleWaterMovement();
    if (this.world.isRemote) {
      extinguish();
    } else if (this.field_190534_ay > 0) {
      if (this.isImmuneToFire) {
        this.field_190534_ay -= 4;
        if (this.field_190534_ay < 0)
          extinguish(); 
      } else {
        if (this.field_190534_ay % 20 == 0)
          attackEntityFrom(DamageSource.onFire, 1.0F); 
        this.field_190534_ay--;
      } 
    } 
    if (isInLava()) {
      setOnFireFromLava();
      this.fallDistance *= 0.5F;
    } 
    if (this.posY < -64.0D)
      kill(); 
    if (!this.world.isRemote)
      setFlag(0, (this.field_190534_ay > 0)); 
    this.firstUpdate = false;
    this.world.theProfiler.endSection();
  }
  
  protected void decrementTimeUntilPortal() {
    if (this.timeUntilPortal > 0)
      this.timeUntilPortal--; 
  }
  
  public int getMaxInPortalTime() {
    return 1;
  }
  
  protected void setOnFireFromLava() {
    if (!this.isImmuneToFire) {
      attackEntityFrom(DamageSource.lava, 4.0F);
      setFire(15);
    } 
  }
  
  public void setFire(int seconds) {
    int i = seconds * 20;
    if (this instanceof EntityLivingBase)
      i = EnchantmentProtection.getFireTimeForEntity((EntityLivingBase)this, i); 
    if (this.field_190534_ay < i)
      this.field_190534_ay = i; 
  }
  
  public void extinguish() {
    this.field_190534_ay = 0;
  }
  
  protected void kill() {
    setDead();
  }
  
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(x, y, z);
    return isLiquidPresentInAABB(axisalignedbb);
  }
  
  private boolean isLiquidPresentInAABB(AxisAlignedBB bb) {
    return (this.world.getCollisionBoxes(this, bb).isEmpty() && !this.world.containsAnyLiquid(bb));
  }
  
  public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
    if (this.noClip) {
      setEntityBoundingBox(getEntityBoundingBox().offset(p_70091_2_, p_70091_4_, p_70091_6_));
      resetPositionToBB();
    } else {
      if (x == MoverType.PISTON) {
        long i = this.world.getTotalWorldTime();
        if (i != this.field_191506_aJ) {
          Arrays.fill(this.field_191505_aI, 0.0D);
          this.field_191506_aJ = i;
        } 
        if (p_70091_2_ != 0.0D) {
          int j = EnumFacing.Axis.X.ordinal();
          double d0 = MathHelper.clamp(p_70091_2_ + this.field_191505_aI[j], -0.51D, 0.51D);
          p_70091_2_ = d0 - this.field_191505_aI[j];
          this.field_191505_aI[j] = d0;
          if (Math.abs(p_70091_2_) <= 9.999999747378752E-6D)
            return; 
        } else if (p_70091_4_ != 0.0D) {
          int l4 = EnumFacing.Axis.Y.ordinal();
          double d12 = MathHelper.clamp(p_70091_4_ + this.field_191505_aI[l4], -0.51D, 0.51D);
          p_70091_4_ = d12 - this.field_191505_aI[l4];
          this.field_191505_aI[l4] = d12;
          if (Math.abs(p_70091_4_) <= 9.999999747378752E-6D)
            return; 
        } else {
          if (p_70091_6_ == 0.0D)
            return; 
          int i5 = EnumFacing.Axis.Z.ordinal();
          double d13 = MathHelper.clamp(p_70091_6_ + this.field_191505_aI[i5], -0.51D, 0.51D);
          p_70091_6_ = d13 - this.field_191505_aI[i5];
          this.field_191505_aI[i5] = d13;
          if (Math.abs(p_70091_6_) <= 9.999999747378752E-6D)
            return; 
        } 
      } 
      this.world.theProfiler.startSection("move");
      double d10 = this.posX;
      double d11 = this.posY;
      double d1 = this.posZ;
      if (this.isInWeb) {
        this.isInWeb = false;
        p_70091_2_ *= 0.25D;
        p_70091_4_ *= 0.05000000074505806D;
        p_70091_6_ *= 0.25D;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
      } 
      double d2 = p_70091_2_;
      double d3 = p_70091_4_;
      double d4 = p_70091_6_;
      if ((x == MoverType.SELF || x == MoverType.PLAYER) && this.onGround && isSneaking() && this instanceof EntityPlayer) {
        for (double d5 = 0.05D; p_70091_2_ != 0.0D && this.world.getCollisionBoxes(this, getEntityBoundingBox().offset(p_70091_2_, -this.stepHeight, 0.0D)).isEmpty(); d2 = p_70091_2_) {
          if (p_70091_2_ < 0.05D && p_70091_2_ >= -0.05D) {
            p_70091_2_ = 0.0D;
          } else if (p_70091_2_ > 0.0D) {
            p_70091_2_ -= 0.05D;
          } else {
            p_70091_2_ += 0.05D;
          } 
        } 
        for (; p_70091_6_ != 0.0D && this.world.getCollisionBoxes(this, getEntityBoundingBox().offset(0.0D, -this.stepHeight, p_70091_6_)).isEmpty(); d4 = p_70091_6_) {
          if (p_70091_6_ < 0.05D && p_70091_6_ >= -0.05D) {
            p_70091_6_ = 0.0D;
          } else if (p_70091_6_ > 0.0D) {
            p_70091_6_ -= 0.05D;
          } else {
            p_70091_6_ += 0.05D;
          } 
        } 
        for (; p_70091_2_ != 0.0D && p_70091_6_ != 0.0D && this.world.getCollisionBoxes(this, getEntityBoundingBox().offset(p_70091_2_, -this.stepHeight, p_70091_6_)).isEmpty(); d4 = p_70091_6_) {
          if (p_70091_2_ < 0.05D && p_70091_2_ >= -0.05D) {
            p_70091_2_ = 0.0D;
          } else if (p_70091_2_ > 0.0D) {
            p_70091_2_ -= 0.05D;
          } else {
            p_70091_2_ += 0.05D;
          } 
          d2 = p_70091_2_;
          if (p_70091_6_ < 0.05D && p_70091_6_ >= -0.05D) {
            p_70091_6_ = 0.0D;
          } else if (p_70091_6_ > 0.0D) {
            p_70091_6_ -= 0.05D;
          } else {
            p_70091_6_ += 0.05D;
          } 
        } 
      } 
      List<AxisAlignedBB> list1 = this.world.getCollisionBoxes(this, getEntityBoundingBox().addCoord(p_70091_2_, p_70091_4_, p_70091_6_));
      AxisAlignedBB axisalignedbb = getEntityBoundingBox();
      if (p_70091_4_ != 0.0D) {
        int k = 0;
        for (int l = list1.size(); k < l; k++)
          p_70091_4_ = ((AxisAlignedBB)list1.get(k)).calculateYOffset(getEntityBoundingBox(), p_70091_4_); 
        setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, p_70091_4_, 0.0D));
      } 
      if (p_70091_2_ != 0.0D) {
        int j5 = 0;
        for (int l5 = list1.size(); j5 < l5; j5++)
          p_70091_2_ = ((AxisAlignedBB)list1.get(j5)).calculateXOffset(getEntityBoundingBox(), p_70091_2_); 
        if (p_70091_2_ != 0.0D)
          setEntityBoundingBox(getEntityBoundingBox().offset(p_70091_2_, 0.0D, 0.0D)); 
      } 
      if (p_70091_6_ != 0.0D) {
        int k5 = 0;
        for (int i6 = list1.size(); k5 < i6; k5++)
          p_70091_6_ = ((AxisAlignedBB)list1.get(k5)).calculateZOffset(getEntityBoundingBox(), p_70091_6_); 
        if (p_70091_6_ != 0.0D)
          setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, p_70091_6_)); 
      } 
      boolean flag = !(!this.onGround && (d3 == p_70091_4_ || d3 >= 0.0D));
      if (this.stepHeight > 0.0F && flag && (d2 != p_70091_2_ || d4 != p_70091_6_)) {
        double d14 = p_70091_2_;
        double d6 = p_70091_4_;
        double d7 = p_70091_6_;
        AxisAlignedBB axisalignedbb1 = getEntityBoundingBox();
        setEntityBoundingBox(axisalignedbb);
        p_70091_4_ = this.stepHeight;
        List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, getEntityBoundingBox().addCoord(d2, p_70091_4_, d4));
        AxisAlignedBB axisalignedbb2 = getEntityBoundingBox();
        AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(d2, 0.0D, d4);
        double d8 = p_70091_4_;
        int j1 = 0;
        for (int k1 = list.size(); j1 < k1; j1++)
          d8 = ((AxisAlignedBB)list.get(j1)).calculateYOffset(axisalignedbb3, d8); 
        axisalignedbb2 = axisalignedbb2.offset(0.0D, d8, 0.0D);
        double d18 = d2;
        int l1 = 0;
        for (int i2 = list.size(); l1 < i2; l1++)
          d18 = ((AxisAlignedBB)list.get(l1)).calculateXOffset(axisalignedbb2, d18); 
        axisalignedbb2 = axisalignedbb2.offset(d18, 0.0D, 0.0D);
        double d19 = d4;
        int j2 = 0;
        for (int k2 = list.size(); j2 < k2; j2++)
          d19 = ((AxisAlignedBB)list.get(j2)).calculateZOffset(axisalignedbb2, d19); 
        axisalignedbb2 = axisalignedbb2.offset(0.0D, 0.0D, d19);
        AxisAlignedBB axisalignedbb4 = getEntityBoundingBox();
        double d20 = p_70091_4_;
        int l2 = 0;
        for (int i3 = list.size(); l2 < i3; l2++)
          d20 = ((AxisAlignedBB)list.get(l2)).calculateYOffset(axisalignedbb4, d20); 
        axisalignedbb4 = axisalignedbb4.offset(0.0D, d20, 0.0D);
        double d21 = d2;
        int j3 = 0;
        for (int k3 = list.size(); j3 < k3; j3++)
          d21 = ((AxisAlignedBB)list.get(j3)).calculateXOffset(axisalignedbb4, d21); 
        axisalignedbb4 = axisalignedbb4.offset(d21, 0.0D, 0.0D);
        double d22 = d4;
        int l3 = 0;
        for (int i4 = list.size(); l3 < i4; l3++)
          d22 = ((AxisAlignedBB)list.get(l3)).calculateZOffset(axisalignedbb4, d22); 
        axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d22);
        double d23 = d18 * d18 + d19 * d19;
        double d9 = d21 * d21 + d22 * d22;
        if (d23 > d9) {
          p_70091_2_ = d18;
          p_70091_6_ = d19;
          p_70091_4_ = -d8;
          setEntityBoundingBox(axisalignedbb2);
        } else {
          p_70091_2_ = d21;
          p_70091_6_ = d22;
          p_70091_4_ = -d20;
          setEntityBoundingBox(axisalignedbb4);
        } 
        int j4 = 0;
        for (int k4 = list.size(); j4 < k4; j4++)
          p_70091_4_ = ((AxisAlignedBB)list.get(j4)).calculateYOffset(getEntityBoundingBox(), p_70091_4_); 
        setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, p_70091_4_, 0.0D));
        if (d14 * d14 + d7 * d7 >= p_70091_2_ * p_70091_2_ + p_70091_6_ * p_70091_6_) {
          p_70091_2_ = d14;
          p_70091_4_ = d6;
          p_70091_6_ = d7;
          setEntityBoundingBox(axisalignedbb1);
        } 
      } 
      this.world.theProfiler.endSection();
      this.world.theProfiler.startSection("rest");
      resetPositionToBB();
      this.isCollidedHorizontally = !(d2 == p_70091_2_ && d4 == p_70091_6_);
      this.isCollidedVertically = (d3 != p_70091_4_);
      this.onGround = (this.isCollidedVertically && d3 < 0.0D);
      this.isCollided = !(!this.isCollidedHorizontally && !this.isCollidedVertically);
      int j6 = MathHelper.floor(this.posX);
      int i1 = MathHelper.floor(this.posY - 0.20000000298023224D);
      int k6 = MathHelper.floor(this.posZ);
      BlockPos blockpos = new BlockPos(j6, i1, k6);
      IBlockState iblockstate = this.world.getBlockState(blockpos);
      if (iblockstate.getMaterial() == Material.AIR) {
        BlockPos blockpos1 = blockpos.down();
        IBlockState iblockstate1 = this.world.getBlockState(blockpos1);
        Block block1 = iblockstate1.getBlock();
        if (block1 instanceof net.minecraft.block.BlockFence || block1 instanceof net.minecraft.block.BlockWall || block1 instanceof net.minecraft.block.BlockFenceGate) {
          iblockstate = iblockstate1;
          blockpos = blockpos1;
        } 
      } 
      updateFallState(p_70091_4_, this.onGround, iblockstate, blockpos);
      if (d2 != p_70091_2_)
        this.motionX = 0.0D; 
      if (d4 != p_70091_6_)
        this.motionZ = 0.0D; 
      Block block = iblockstate.getBlock();
      if (d3 != p_70091_4_)
        block.onLanded(this.world, this); 
      if (canTriggerWalking() && (!this.onGround || !isSneaking() || !(this instanceof EntityPlayer)) && !isRiding()) {
        double d15 = this.posX - d10;
        double d16 = this.posY - d11;
        double d17 = this.posZ - d1;
        if (block != Blocks.LADDER)
          d16 = 0.0D; 
        if (block != null && this.onGround)
          block.onEntityWalk(this.world, blockpos, this); 
        this.distanceWalkedModified = (float)(this.distanceWalkedModified + MathHelper.sqrt(d15 * d15 + d17 * d17) * 0.6D);
        this.distanceWalkedOnStepModified = (float)(this.distanceWalkedOnStepModified + MathHelper.sqrt(d15 * d15 + d16 * d16 + d17 * d17) * 0.6D);
        if (this.distanceWalkedOnStepModified > this.nextStepDistance && iblockstate.getMaterial() != Material.AIR) {
          this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
          if (isInWater()) {
            Entity entity = (isBeingRidden() && getControllingPassenger() != null) ? getControllingPassenger() : this;
            float f = (entity == this) ? 0.35F : 0.4F;
            float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;
            if (f1 > 1.0F)
              f1 = 1.0F; 
            playSound(getSwimSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
          } else {
            playStepSound(blockpos, block);
          } 
        } else if (this.distanceWalkedOnStepModified > this.field_191959_ay && func_191957_ae() && iblockstate.getMaterial() == Material.AIR) {
          this.field_191959_ay = func_191954_d(this.distanceWalkedOnStepModified);
        } 
      } 
      try {
        doBlockCollisions();
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
        addEntityCrashInfo(crashreportcategory);
        throw new ReportedException(crashreport);
      } 
      boolean flag1 = isWet();
      if (this.world.isFlammableWithin(getEntityBoundingBox().contract(0.001D))) {
        dealFireDamage(1);
        if (!flag1) {
          this.field_190534_ay++;
          if (this.field_190534_ay == 0)
            setFire(8); 
        } 
      } else if (this.field_190534_ay <= 0) {
        this.field_190534_ay = -func_190531_bD();
      } 
      if (flag1 && isBurning()) {
        playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        this.field_190534_ay = -func_190531_bD();
      } 
      this.world.theProfiler.endSection();
    } 
  }
  
  public void resetPositionToBB() {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
    this.posY = axisalignedbb.minY;
    this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
  }
  
  protected SoundEvent getSwimSound() {
    return SoundEvents.ENTITY_GENERIC_SWIM;
  }
  
  protected SoundEvent getSplashSound() {
    return SoundEvents.ENTITY_GENERIC_SPLASH;
  }
  
  protected void doBlockCollisions() {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();
    if (this.world.isAreaLoaded((BlockPos)blockpos$pooledmutableblockpos, (BlockPos)blockpos$pooledmutableblockpos1))
      for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); i++) {
        for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); j++) {
          for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); k++) {
            blockpos$pooledmutableblockpos2.setPos(i, j, k);
            IBlockState iblockstate = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos2);
            try {
              iblockstate.getBlock().onEntityCollidedWithBlock(this.world, (BlockPos)blockpos$pooledmutableblockpos2, iblockstate, this);
              func_191955_a(iblockstate);
            } catch (Throwable throwable) {
              CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
              CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
              CrashReportCategory.addBlockInfo(crashreportcategory, (BlockPos)blockpos$pooledmutableblockpos2, iblockstate);
              throw new ReportedException(crashreport);
            } 
          } 
        } 
      }  
    blockpos$pooledmutableblockpos.release();
    blockpos$pooledmutableblockpos1.release();
    blockpos$pooledmutableblockpos2.release();
  }
  
  protected void func_191955_a(IBlockState p_191955_1_) {}
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    SoundType soundtype = blockIn.getSoundType();
    if (this.world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER) {
      soundtype = Blocks.SNOW_LAYER.getSoundType();
      playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
    } else if (!blockIn.getDefaultState().getMaterial().isLiquid()) {
      playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
    } 
  }
  
  protected float func_191954_d(float p_191954_1_) {
    return 0.0F;
  }
  
  protected boolean func_191957_ae() {
    return false;
  }
  
  public void playSound(SoundEvent soundIn, float volume, float pitch) {
    if (!isSilent())
      this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, getSoundCategory(), volume, pitch); 
  }
  
  public boolean isSilent() {
    return ((Boolean)this.dataManager.get(SILENT)).booleanValue();
  }
  
  public void setSilent(boolean isSilent) {
    this.dataManager.set(SILENT, Boolean.valueOf(isSilent));
  }
  
  public boolean hasNoGravity() {
    return ((Boolean)this.dataManager.get(NO_GRAVITY)).booleanValue();
  }
  
  public void setNoGravity(boolean noGravity) {
    this.dataManager.set(NO_GRAVITY, Boolean.valueOf(noGravity));
  }
  
  protected boolean canTriggerWalking() {
    return true;
  }
  
  protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    if (onGroundIn) {
      if (this.fallDistance > 0.0F)
        state.getBlock().onFallenUpon(this.world, pos, this, this.fallDistance); 
      this.fallDistance = 0.0F;
    } else if (y < 0.0D) {
      this.fallDistance = (float)(this.fallDistance - y);
    } 
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox() {
    return null;
  }
  
  protected void dealFireDamage(int amount) {
    if (!this.isImmuneToFire)
      attackEntityFrom(DamageSource.inFire, amount); 
  }
  
  public final boolean isImmuneToFire() {
    return this.isImmuneToFire;
  }
  
  public void fall(float distance, float damageMultiplier) {
    if (isBeingRidden())
      for (Entity entity : getPassengers())
        entity.fall(distance, damageMultiplier);  
  }
  
  public boolean isWet() {
    if (this.inWater)
      return true; 
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.posY, this.posZ);
    if (!this.world.isRainingAt((BlockPos)blockpos$pooledmutableblockpos) && !this.world.isRainingAt((BlockPos)blockpos$pooledmutableblockpos.setPos(this.posX, this.posY + this.height, this.posZ))) {
      blockpos$pooledmutableblockpos.release();
      return false;
    } 
    blockpos$pooledmutableblockpos.release();
    return true;
  }
  
  public boolean isInWater() {
    return this.inWater;
  }
  
  public boolean func_191953_am() {
    return this.world.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -20.0D, 0.0D).contract(0.001D), Material.WATER, this);
  }
  
  public boolean handleWaterMovement() {
    if (getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat) {
      this.inWater = false;
    } else if (this.world.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D), Material.WATER, this)) {
      if (!this.inWater && !this.firstUpdate)
        resetHeight(); 
      this.fallDistance = 0.0F;
      this.inWater = true;
      extinguish();
    } else {
      this.inWater = false;
    } 
    return this.inWater;
  }
  
  protected void resetHeight() {
    Entity entity = (isBeingRidden() && getControllingPassenger() != null) ? getControllingPassenger() : this;
    float f = (entity == this) ? 0.2F : 0.9F;
    float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;
    if (f1 > 1.0F)
      f1 = 1.0F; 
    playSound(getSplashSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
    float f2 = MathHelper.floor((getEntityBoundingBox()).minY);
    for (int i = 0; i < 1.0F + this.width * 20.0F; i++) {
      float f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      float f4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + f3, (f2 + 1.0F), this.posZ + f4, this.motionX, this.motionY - (this.rand.nextFloat() * 0.2F), this.motionZ, new int[0]);
    } 
    for (int j = 0; j < 1.0F + this.width * 20.0F; j++) {
      float f5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      float f6 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
      this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f5, (f2 + 1.0F), this.posZ + f6, this.motionX, this.motionY, this.motionZ, new int[0]);
    } 
  }
  
  public void spawnRunningParticles() {
    if (isSprinting() && !isInWater())
      createRunningParticles(); 
  }
  
  protected void createRunningParticles() {
    int i = MathHelper.floor(this.posX);
    int j = MathHelper.floor(this.posY - 0.20000000298023224D);
    int k = MathHelper.floor(this.posZ);
    BlockPos blockpos = new BlockPos(i, j, k);
    IBlockState iblockstate = this.world.getBlockState(blockpos);
    if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE)
      this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, (getEntityBoundingBox()).minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] { Block.getStateId(iblockstate) }); 
  }
  
  public boolean isInsideOfMaterial(Material materialIn) {
    if (getRidingEntity() instanceof net.minecraft.entity.item.EntityBoat)
      return false; 
    double d0 = this.posY + getEyeHeight();
    BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
    IBlockState iblockstate = this.world.getBlockState(blockpos);
    if (iblockstate.getMaterial() == materialIn) {
      float f = BlockLiquid.getLiquidHeightPercent(iblockstate.getBlock().getMetaFromState(iblockstate)) - 0.11111111F;
      float f1 = (blockpos.getY() + 1) - f;
      boolean flag = (d0 < f1);
      return (!flag && this instanceof EntityPlayer) ? false : flag;
    } 
    return false;
  }
  
  public boolean isInLava() {
    return this.world.isMaterialInBB(getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
  }
  
  public void func_191958_b(float p_191958_1_, float p_191958_2_, float p_191958_3_, float p_191958_4_) {
    float f = p_191958_1_ * p_191958_1_ + p_191958_2_ * p_191958_2_ + p_191958_3_ * p_191958_3_;
    if (f >= 1.0E-4F) {
      f = MathHelper.sqrt(f);
      if (f < 1.0F)
        f = 1.0F; 
      f = p_191958_4_ / f;
      p_191958_1_ *= f;
      p_191958_2_ *= f;
      p_191958_3_ *= f;
      float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
      float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);
      this.motionX += (p_191958_1_ * f2 - p_191958_3_ * f1);
      this.motionY += p_191958_2_;
      this.motionZ += (p_191958_3_ * f2 + p_191958_1_ * f1);
    } 
  }
  
  public int getBrightnessForRender() {
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
    if (this.world.isBlockLoaded((BlockPos)blockpos$mutableblockpos)) {
      blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + getEyeHeight()));
      return this.world.getCombinedLight((BlockPos)blockpos$mutableblockpos, 0);
    } 
    return 0;
  }
  
  public float getBrightness() {
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
    if (this.world.isBlockLoaded((BlockPos)blockpos$mutableblockpos)) {
      blockpos$mutableblockpos.setY(MathHelper.floor(this.posY + getEyeHeight()));
      return this.world.getLightBrightness((BlockPos)blockpos$mutableblockpos);
    } 
    return 0.0F;
  }
  
  public void setWorld(World worldIn) {
    this.world = worldIn;
  }
  
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    this.posX = MathHelper.clamp(x, -3.0E7D, 3.0E7D);
    this.posY = y;
    this.posZ = MathHelper.clamp(z, -3.0E7D, 3.0E7D);
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
    this.rotationYaw = yaw;
    this.rotationPitch = pitch;
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
    double d0 = (this.prevRotationYaw - yaw);
    if (d0 < -180.0D)
      this.prevRotationYaw += 360.0F; 
    if (d0 >= 180.0D)
      this.prevRotationYaw -= 360.0F; 
    setPosition(this.posX, this.posY, this.posZ);
    setRotation(yaw, pitch);
  }
  
  public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
    setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rotationYawIn, rotationPitchIn);
  }
  
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.lastTickPosX = this.posX;
    this.lastTickPosY = this.posY;
    this.lastTickPosZ = this.posZ;
    this.rotationYaw = yaw;
    this.rotationPitch = pitch;
    setPosition(this.posX, this.posY, this.posZ);
  }
  
  public float getDistanceToEntity(Entity entityIn) {
    float f = (float)(this.posX - entityIn.posX);
    float f1 = (float)(this.posY - entityIn.posY);
    float f2 = (float)(this.posZ - entityIn.posZ);
    return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
  }
  
  public double getDistanceSq(double x, double y, double z) {
    double d0 = this.posX - x;
    double d1 = this.posY - y;
    double d2 = this.posZ - z;
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  public double getDistanceSq(BlockPos pos) {
    return pos.distanceSq(this.posX, this.posY, this.posZ);
  }
  
  public double getDistanceSqToCenter(BlockPos pos) {
    return pos.distanceSqToCenter(this.posX, this.posY, this.posZ);
  }
  
  public double getDistance(double x, double y, double z) {
    double d0 = this.posX - x;
    double d1 = this.posY - y;
    double d2 = this.posZ - z;
    return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
  }
  
  public double getDistanceSqToEntity(Entity entityIn) {
    double d0 = this.posX - entityIn.posX;
    double d1 = this.posY - entityIn.posY;
    double d2 = this.posZ - entityIn.posZ;
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  public void onCollideWithPlayer(EntityPlayer entityIn) {}
  
  public void applyEntityCollision(Entity entityIn) {
    if (!isRidingSameEntity(entityIn))
      if (!entityIn.noClip && !this.noClip) {
        double d0 = entityIn.posX - this.posX;
        double d1 = entityIn.posZ - this.posZ;
        double d2 = MathHelper.absMax(d0, d1);
        if (d2 >= 0.009999999776482582D) {
          d2 = MathHelper.sqrt(d2);
          d0 /= d2;
          d1 /= d2;
          double d3 = 1.0D / d2;
          if (d3 > 1.0D)
            d3 = 1.0D; 
          d0 *= d3;
          d1 *= d3;
          d0 *= 0.05000000074505806D;
          d1 *= 0.05000000074505806D;
          d0 *= (1.0F - this.entityCollisionReduction);
          d1 *= (1.0F - this.entityCollisionReduction);
          if (!isBeingRidden())
            addVelocity(-d0, 0.0D, -d1); 
          if (!entityIn.isBeingRidden())
            entityIn.addVelocity(d0, 0.0D, d1); 
        } 
      }  
  }
  
  public void addVelocity(double x, double y, double z) {
    this.motionX += x;
    this.motionY += y;
    this.motionZ += z;
    this.isAirBorne = true;
  }
  
  protected void setBeenAttacked() {
    this.velocityChanged = true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    setBeenAttacked();
    return false;
  }
  
  public Vec3d getLook(float partialTicks) {
    if (partialTicks == 1.0F)
      return getVectorForRotation(this.rotationPitch, this.rotationYaw); 
    float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
    float f1 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * partialTicks;
    return getVectorForRotation(f, f1);
  }
  
  protected final Vec3d getVectorForRotation(float pitch, float yaw) {
    float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
    float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
    float f2 = -MathHelper.cos(-pitch * 0.017453292F);
    float f3 = MathHelper.sin(-pitch * 0.017453292F);
    return new Vec3d((f1 * f2), f3, (f * f2));
  }
  
  public Vec3d getPositionEyes(float partialTicks) {
    if (partialTicks == 1.0F)
      return new Vec3d(this.posX, this.posY + getEyeHeight(), this.posZ); 
    double d0 = this.prevPosX + (this.posX - this.prevPosX) * partialTicks;
    double d1 = this.prevPosY + (this.posY - this.prevPosY) * partialTicks + getEyeHeight();
    double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks;
    return new Vec3d(d0, d1, d2);
  }
  
  @Nullable
  public RayTraceResult rayTrace(double blockReachDistance, float partialTicks) {
    Vec3d vec3d = getPositionEyes(partialTicks);
    Vec3d vec3d1 = getLook(partialTicks);
    Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * blockReachDistance, vec3d1.yCoord * blockReachDistance, vec3d1.zCoord * blockReachDistance);
    return this.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public boolean canBePushed() {
    return false;
  }
  
  public void func_191956_a(Entity p_191956_1_, int p_191956_2_, DamageSource p_191956_3_) {
    if (p_191956_1_ instanceof EntityPlayerMP)
      CriteriaTriggers.field_192123_c.func_192211_a((EntityPlayerMP)p_191956_1_, this, p_191956_3_); 
  }
  
  public boolean isInRangeToRender3d(double x, double y, double z) {
    double d0 = this.posX - x;
    double d1 = this.posY - y;
    double d2 = this.posZ - z;
    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
    return isInRangeToRenderDist(d3);
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    double d0 = getEntityBoundingBox().getAverageEdgeLength();
    if (Double.isNaN(d0))
      d0 = 1.0D; 
    d0 = d0 * 64.0D * renderDistanceWeight;
    return (distance < d0 * d0);
  }
  
  public boolean writeToNBTAtomically(NBTTagCompound compound) {
    String s = getEntityString();
    if (!this.isDead && s != null) {
      compound.setString("id", s);
      writeToNBT(compound);
      return true;
    } 
    return false;
  }
  
  public boolean writeToNBTOptional(NBTTagCompound compound) {
    String s = getEntityString();
    if (!this.isDead && s != null && !isRiding()) {
      compound.setString("id", s);
      writeToNBT(compound);
      return true;
    } 
    return false;
  }
  
  public static void func_190533_a(DataFixer p_190533_0_) {
    p_190533_0_.registerWalker(FixTypes.ENTITY, new IDataWalker() {
          public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
            if (compound.hasKey("Passengers", 9)) {
              NBTTagList nbttaglist = compound.getTagList("Passengers", 10);
              for (int i = 0; i < nbttaglist.tagCount(); i++)
                nbttaglist.set(i, (NBTBase)fixer.process((IFixType)FixTypes.ENTITY, nbttaglist.getCompoundTagAt(i), versionIn)); 
            } 
            return compound;
          }
        });
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    try {
      compound.setTag("Pos", (NBTBase)newDoubleNBTList(new double[] { this.posX, this.posY, this.posZ }));
      compound.setTag("Motion", (NBTBase)newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
      compound.setTag("Rotation", (NBTBase)newFloatNBTList(new float[] { this.rotationYaw, this.rotationPitch }));
      compound.setFloat("FallDistance", this.fallDistance);
      compound.setShort("Fire", (short)this.field_190534_ay);
      compound.setShort("Air", (short)getAir());
      compound.setBoolean("OnGround", this.onGround);
      compound.setInteger("Dimension", this.dimension);
      compound.setBoolean("Invulnerable", this.invulnerable);
      compound.setInteger("PortalCooldown", this.timeUntilPortal);
      compound.setUniqueId("UUID", getUniqueID());
      if (hasCustomName())
        compound.setString("CustomName", getCustomNameTag()); 
      if (getAlwaysRenderNameTag())
        compound.setBoolean("CustomNameVisible", getAlwaysRenderNameTag()); 
      this.cmdResultStats.writeStatsToNBT(compound);
      if (isSilent())
        compound.setBoolean("Silent", isSilent()); 
      if (hasNoGravity())
        compound.setBoolean("NoGravity", hasNoGravity()); 
      if (this.glowing)
        compound.setBoolean("Glowing", this.glowing); 
      if (!this.tags.isEmpty()) {
        NBTTagList nbttaglist = new NBTTagList();
        for (String s : this.tags)
          nbttaglist.appendTag((NBTBase)new NBTTagString(s)); 
        compound.setTag("Tags", (NBTBase)nbttaglist);
      } 
      writeEntityToNBT(compound);
      if (isBeingRidden()) {
        NBTTagList nbttaglist1 = new NBTTagList();
        for (Entity entity : getPassengers()) {
          NBTTagCompound nbttagcompound = new NBTTagCompound();
          if (entity.writeToNBTAtomically(nbttagcompound))
            nbttaglist1.appendTag((NBTBase)nbttagcompound); 
        } 
        if (!nbttaglist1.hasNoTags())
          compound.setTag("Passengers", (NBTBase)nbttaglist1); 
      } 
      return compound;
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
      addEntityCrashInfo(crashreportcategory);
      throw new ReportedException(crashreport);
    } 
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    try {
      NBTTagList nbttaglist = compound.getTagList("Pos", 6);
      NBTTagList nbttaglist2 = compound.getTagList("Motion", 6);
      NBTTagList nbttaglist3 = compound.getTagList("Rotation", 5);
      this.motionX = nbttaglist2.getDoubleAt(0);
      this.motionY = nbttaglist2.getDoubleAt(1);
      this.motionZ = nbttaglist2.getDoubleAt(2);
      if (Math.abs(this.motionX) > 10.0D)
        this.motionX = 0.0D; 
      if (Math.abs(this.motionY) > 10.0D)
        this.motionY = 0.0D; 
      if (Math.abs(this.motionZ) > 10.0D)
        this.motionZ = 0.0D; 
      this.posX = nbttaglist.getDoubleAt(0);
      this.posY = nbttaglist.getDoubleAt(1);
      this.posZ = nbttaglist.getDoubleAt(2);
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.rotationYaw = nbttaglist3.getFloatAt(0);
      this.rotationPitch = nbttaglist3.getFloatAt(1);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
      setRotationYawHead(this.rotationYaw);
      setRenderYawOffset(this.rotationYaw);
      this.fallDistance = compound.getFloat("FallDistance");
      this.field_190534_ay = compound.getShort("Fire");
      setAir(compound.getShort("Air"));
      this.onGround = compound.getBoolean("OnGround");
      if (compound.hasKey("Dimension"))
        this.dimension = compound.getInteger("Dimension"); 
      this.invulnerable = compound.getBoolean("Invulnerable");
      this.timeUntilPortal = compound.getInteger("PortalCooldown");
      if (compound.hasUniqueId("UUID")) {
        this.entityUniqueID = compound.getUniqueId("UUID");
        this.cachedUniqueIdString = this.entityUniqueID.toString();
      } 
      setPosition(this.posX, this.posY, this.posZ);
      setRotation(this.rotationYaw, this.rotationPitch);
      if (compound.hasKey("CustomName", 8))
        setCustomNameTag(compound.getString("CustomName")); 
      setAlwaysRenderNameTag(compound.getBoolean("CustomNameVisible"));
      this.cmdResultStats.readStatsFromNBT(compound);
      setSilent(compound.getBoolean("Silent"));
      setNoGravity(compound.getBoolean("NoGravity"));
      setGlowing(compound.getBoolean("Glowing"));
      if (compound.hasKey("Tags", 9)) {
        this.tags.clear();
        NBTTagList nbttaglist1 = compound.getTagList("Tags", 8);
        int i = Math.min(nbttaglist1.tagCount(), 1024);
        for (int j = 0; j < i; j++)
          this.tags.add(nbttaglist1.getStringTagAt(j)); 
      } 
      readEntityFromNBT(compound);
      if (shouldSetPosAfterLoading())
        setPosition(this.posX, this.posY, this.posZ); 
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
      addEntityCrashInfo(crashreportcategory);
      throw new ReportedException(crashreport);
    } 
  }
  
  protected boolean shouldSetPosAfterLoading() {
    return true;
  }
  
  @Nullable
  protected final String getEntityString() {
    ResourceLocation resourcelocation = EntityList.func_191301_a(this);
    return (resourcelocation == null) ? null : resourcelocation.toString();
  }
  
  protected NBTTagList newDoubleNBTList(double... numbers) {
    NBTTagList nbttaglist = new NBTTagList();
    byte b;
    int i;
    double[] arrayOfDouble;
    for (i = (arrayOfDouble = numbers).length, b = 0; b < i; ) {
      double d0 = arrayOfDouble[b];
      nbttaglist.appendTag((NBTBase)new NBTTagDouble(d0));
      b++;
    } 
    return nbttaglist;
  }
  
  protected NBTTagList newFloatNBTList(float... numbers) {
    NBTTagList nbttaglist = new NBTTagList();
    byte b;
    int i;
    float[] arrayOfFloat;
    for (i = (arrayOfFloat = numbers).length, b = 0; b < i; ) {
      float f = arrayOfFloat[b];
      nbttaglist.appendTag((NBTBase)new NBTTagFloat(f));
      b++;
    } 
    return nbttaglist;
  }
  
  @Nullable
  public EntityItem dropItem(Item itemIn, int size) {
    return dropItemWithOffset(itemIn, size, 0.0F);
  }
  
  @Nullable
  public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
    return entityDropItem(new ItemStack(itemIn, size, 0), offsetY);
  }
  
  @Nullable
  public EntityItem entityDropItem(ItemStack stack, float offsetY) {
    if (stack.func_190926_b())
      return null; 
    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + offsetY, this.posZ, stack);
    entityitem.setDefaultPickupDelay();
    this.world.spawnEntityInWorld((Entity)entityitem);
    return entityitem;
  }
  
  public boolean isEntityAlive() {
    return !this.isDead;
  }
  
  public boolean isEntityInsideOpaqueBlock() {
    if (this.noClip)
      return false; 
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    for (int i = 0; i < 8; i++) {
      int j = MathHelper.floor(this.posY + ((((i >> 0) % 2) - 0.5F) * 0.1F) + getEyeHeight());
      int k = MathHelper.floor(this.posX + ((((i >> 1) % 2) - 0.5F) * this.width * 0.8F));
      int l = MathHelper.floor(this.posZ + ((((i >> 2) % 2) - 0.5F) * this.width * 0.8F));
      if (blockpos$pooledmutableblockpos.getX() != k || blockpos$pooledmutableblockpos.getY() != j || blockpos$pooledmutableblockpos.getZ() != l) {
        blockpos$pooledmutableblockpos.setPos(k, j, l);
        if (this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos).func_191058_s()) {
          blockpos$pooledmutableblockpos.release();
          return true;
        } 
      } 
    } 
    blockpos$pooledmutableblockpos.release();
    return false;
  }
  
  public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
    return false;
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBox(Entity entityIn) {
    return null;
  }
  
  public void updateRidden() {
    Entity entity = getRidingEntity();
    if (isRiding() && entity.isDead) {
      dismountRidingEntity();
    } else {
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
      onUpdate();
      if (isRiding())
        entity.updatePassenger(this); 
    } 
  }
  
  public void updatePassenger(Entity passenger) {
    if (isPassenger(passenger))
      passenger.setPosition(this.posX, this.posY + getMountedYOffset() + passenger.getYOffset(), this.posZ); 
  }
  
  public void applyOrientationToEntity(Entity entityToUpdate) {}
  
  public double getYOffset() {
    return 0.0D;
  }
  
  public double getMountedYOffset() {
    return this.height * 0.75D;
  }
  
  public boolean startRiding(Entity entityIn) {
    return startRiding(entityIn, false);
  }
  
  public boolean startRiding(Entity entityIn, boolean force) {
    for (Entity entity = entityIn; entity.ridingEntity != null; entity = entity.ridingEntity) {
      if (entity.ridingEntity == this)
        return false; 
    } 
    if (force || (canBeRidden(entityIn) && entityIn.canFitPassenger(this))) {
      if (isRiding())
        dismountRidingEntity(); 
      this.ridingEntity = entityIn;
      this.ridingEntity.addPassenger(this);
      return true;
    } 
    return false;
  }
  
  protected boolean canBeRidden(Entity entityIn) {
    return (this.rideCooldown <= 0);
  }
  
  public void removePassengers() {
    for (int i = this.riddenByEntities.size() - 1; i >= 0; i--)
      ((Entity)this.riddenByEntities.get(i)).dismountRidingEntity(); 
  }
  
  public void dismountRidingEntity() {
    if (this.ridingEntity != null) {
      Entity entity = this.ridingEntity;
      this.ridingEntity = null;
      entity.removePassenger(this);
    } 
  }
  
  protected void addPassenger(Entity passenger) {
    if (passenger.getRidingEntity() != this)
      throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)"); 
    if (!this.world.isRemote && passenger instanceof EntityPlayer && !(getControllingPassenger() instanceof EntityPlayer)) {
      this.riddenByEntities.add(0, passenger);
    } else {
      this.riddenByEntities.add(passenger);
    } 
  }
  
  protected void removePassenger(Entity passenger) {
    if (passenger.getRidingEntity() == this)
      throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)"); 
    this.riddenByEntities.remove(passenger);
    passenger.rideCooldown = 60;
  }
  
  protected boolean canFitPassenger(Entity passenger) {
    return (getPassengers().size() < 1);
  }
  
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    setPosition(x, y, z);
    setRotation(yaw, pitch);
  }
  
  public float getCollisionBorderSize() {
    return 0.0F;
  }
  
  public Vec3d getLookVec() {
    return getVectorForRotation(this.rotationPitch, this.rotationYaw);
  }
  
  public Vec2f getPitchYaw() {
    return new Vec2f(this.rotationPitch, this.rotationYaw);
  }
  
  public Vec3d getForward() {
    return Vec3d.fromPitchYawVector(getPitchYaw());
  }
  
  public void setPortal(BlockPos pos) {
    if (this.timeUntilPortal > 0) {
      this.timeUntilPortal = getPortalCooldown();
    } else {
      if (!this.world.isRemote && !pos.equals(this.lastPortalPos)) {
        this.lastPortalPos = new BlockPos((Vec3i)pos);
        BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.PORTAL.createPatternHelper(this.world, this.lastPortalPos);
        double d0 = (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.getFrontTopLeft().getZ() : blockpattern$patternhelper.getFrontTopLeft().getX();
        double d1 = (blockpattern$patternhelper.getForwards().getAxis() == EnumFacing.Axis.X) ? this.posZ : this.posX;
        d1 = Math.abs(MathHelper.pct(d1 - ((blockpattern$patternhelper.getForwards().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) ? true : false), d0, d0 - blockpattern$patternhelper.getWidth()));
        double d2 = MathHelper.pct(this.posY - 1.0D, blockpattern$patternhelper.getFrontTopLeft().getY(), (blockpattern$patternhelper.getFrontTopLeft().getY() - blockpattern$patternhelper.getHeight()));
        this.lastPortalVec = new Vec3d(d1, d2, 0.0D);
        this.teleportDirection = blockpattern$patternhelper.getForwards();
      } 
      this.inPortal = true;
    } 
  }
  
  public int getPortalCooldown() {
    return 300;
  }
  
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
  }
  
  public void handleStatusUpdate(byte id) {}
  
  public void performHurtAnimation() {}
  
  public Iterable<ItemStack> getHeldEquipment() {
    return field_190535_b;
  }
  
  public Iterable<ItemStack> getArmorInventoryList() {
    return field_190535_b;
  }
  
  public Iterable<ItemStack> getEquipmentAndArmor() {
    return Iterables.concat(getHeldEquipment(), getArmorInventoryList());
  }
  
  public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {}
  
  public boolean isBurning() {
    boolean flag = (this.world != null && this.world.isRemote);
    return (!this.isImmuneToFire && (this.field_190534_ay > 0 || (flag && getFlag(0))));
  }
  
  public boolean isRiding() {
    return (getRidingEntity() != null);
  }
  
  public boolean isBeingRidden() {
    return !getPassengers().isEmpty();
  }
  
  public boolean isSneaking() {
    return getFlag(1);
  }
  
  public void setSneaking(boolean sneaking) {
    setFlag(1, sneaking);
  }
  
  public boolean isSprinting() {
    return getFlag(3);
  }
  
  public void setSprinting(boolean sprinting) {
    setFlag(3, sprinting);
  }
  
  public boolean isGlowing() {
    return !(!this.glowing && (!this.world.isRemote || !getFlag(6)));
  }
  
  public void setGlowing(boolean glowingIn) {
    this.glowing = glowingIn;
    if (!this.world.isRemote)
      setFlag(6, this.glowing); 
  }
  
  public boolean isInvisible() {
    return getFlag(5);
  }
  
  public boolean isInvisibleToPlayer(EntityPlayer player) {
    if (player.isSpectator())
      return false; 
    Team team = getTeam();
    return (team != null && player != null && player.getTeam() == team && team.getSeeFriendlyInvisiblesEnabled()) ? false : isInvisible();
  }
  
  @Nullable
  public Team getTeam() {
    return (Team)this.world.getScoreboard().getPlayersTeam(getCachedUniqueIdString());
  }
  
  public boolean isOnSameTeam(Entity entityIn) {
    return isOnScoreboardTeam(entityIn.getTeam());
  }
  
  public boolean isOnScoreboardTeam(Team teamIn) {
    return (getTeam() != null) ? getTeam().isSameTeam(teamIn) : false;
  }
  
  public void setInvisible(boolean invisible) {
    setFlag(5, invisible);
  }
  
  protected boolean getFlag(int flag) {
    return ((((Byte)this.dataManager.get(FLAGS)).byteValue() & 1 << flag) != 0);
  }
  
  protected void setFlag(int flag, boolean set) {
    byte b0 = ((Byte)this.dataManager.get(FLAGS)).byteValue();
    if (set) {
      this.dataManager.set(FLAGS, Byte.valueOf((byte)(b0 | 1 << flag)));
    } else {
      this.dataManager.set(FLAGS, Byte.valueOf((byte)(b0 & (1 << flag ^ 0xFFFFFFFF))));
    } 
  }
  
  public int getAir() {
    return ((Integer)this.dataManager.get(AIR)).intValue();
  }
  
  public void setAir(int air) {
    this.dataManager.set(AIR, Integer.valueOf(air));
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    attackEntityFrom(DamageSource.lightningBolt, 5.0F);
    this.field_190534_ay++;
    if (this.field_190534_ay == 0)
      setFire(8); 
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn) {}
  
  protected boolean pushOutOfBlocks(double x, double y, double z) {
    BlockPos blockpos = new BlockPos(x, y, z);
    double d0 = x - blockpos.getX();
    double d1 = y - blockpos.getY();
    double d2 = z - blockpos.getZ();
    if (!this.world.collidesWithAnyBlock(getEntityBoundingBox()))
      return false; 
    EnumFacing enumfacing = EnumFacing.UP;
    double d3 = Double.MAX_VALUE;
    if (!this.world.isBlockFullCube(blockpos.west()) && d0 < d3) {
      d3 = d0;
      enumfacing = EnumFacing.WEST;
    } 
    if (!this.world.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3) {
      d3 = 1.0D - d0;
      enumfacing = EnumFacing.EAST;
    } 
    if (!this.world.isBlockFullCube(blockpos.north()) && d2 < d3) {
      d3 = d2;
      enumfacing = EnumFacing.NORTH;
    } 
    if (!this.world.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3) {
      d3 = 1.0D - d2;
      enumfacing = EnumFacing.SOUTH;
    } 
    if (!this.world.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3) {
      d3 = 1.0D - d1;
      enumfacing = EnumFacing.UP;
    } 
    float f = this.rand.nextFloat() * 0.2F + 0.1F;
    float f1 = enumfacing.getAxisDirection().getOffset();
    if (enumfacing.getAxis() == EnumFacing.Axis.X) {
      this.motionX = (f1 * f);
      this.motionY *= 0.75D;
      this.motionZ *= 0.75D;
    } else if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
      this.motionX *= 0.75D;
      this.motionY = (f1 * f);
      this.motionZ *= 0.75D;
    } else if (enumfacing.getAxis() == EnumFacing.Axis.Z) {
      this.motionX *= 0.75D;
      this.motionY *= 0.75D;
      this.motionZ = (f1 * f);
    } 
    return true;
  }
  
  public void setInWeb() {
    this.isInWeb = true;
    this.fallDistance = 0.0F;
  }
  
  public String getName() {
    if (hasCustomName())
      return getCustomNameTag(); 
    String s = EntityList.getEntityString(this);
    if (s == null)
      s = "generic"; 
    return I18n.translateToLocal("entity." + s + ".name");
  }
  
  @Nullable
  public Entity[] getParts() {
    return null;
  }
  
  public boolean isEntityEqual(Entity entityIn) {
    return (this == entityIn);
  }
  
  public float getRotationYawHead() {
    return 0.0F;
  }
  
  public void setRotationYawHead(float rotation) {}
  
  public void setRenderYawOffset(float offset) {}
  
  public boolean canBeAttackedWithItem() {
    return true;
  }
  
  public boolean hitByEntity(Entity entityIn) {
    return false;
  }
  
  public String toString() {
    return String.format("%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getName(), Integer.valueOf(this.entityId), (this.world == null) ? "~NULL~" : this.world.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) });
  }
  
  public boolean isEntityInvulnerable(DamageSource source) {
    return (this.invulnerable && source != DamageSource.outOfWorld && !source.isCreativePlayer());
  }
  
  public boolean func_190530_aW() {
    return this.invulnerable;
  }
  
  public void setEntityInvulnerable(boolean isInvulnerable) {
    this.invulnerable = isInvulnerable;
  }
  
  public void copyLocationAndAnglesFrom(Entity entityIn) {
    setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
  }
  
  private void copyDataFromOld(Entity entityIn) {
    NBTTagCompound nbttagcompound = entityIn.writeToNBT(new NBTTagCompound());
    nbttagcompound.removeTag("Dimension");
    readFromNBT(nbttagcompound);
    this.timeUntilPortal = entityIn.timeUntilPortal;
    this.lastPortalPos = entityIn.lastPortalPos;
    this.lastPortalVec = entityIn.lastPortalVec;
    this.teleportDirection = entityIn.teleportDirection;
  }
  
  @Nullable
  public Entity changeDimension(int dimensionIn) {
    if (!this.world.isRemote && !this.isDead) {
      BlockPos blockpos;
      this.world.theProfiler.startSection("changeDimension");
      MinecraftServer minecraftserver = getServer();
      int i = this.dimension;
      WorldServer worldserver = minecraftserver.worldServerForDimension(i);
      WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionIn);
      this.dimension = dimensionIn;
      if (i == 1 && dimensionIn == 1) {
        worldserver1 = minecraftserver.worldServerForDimension(0);
        this.dimension = 0;
      } 
      this.world.removeEntity(this);
      this.isDead = false;
      this.world.theProfiler.startSection("reposition");
      if (dimensionIn == 1) {
        blockpos = worldserver1.getSpawnCoordinate();
      } else {
        double d0 = this.posX;
        double d1 = this.posZ;
        double d2 = 8.0D;
        if (dimensionIn == -1) {
          d0 = MathHelper.clamp(d0 / 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
          d1 = MathHelper.clamp(d1 / 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
        } else if (dimensionIn == 0) {
          d0 = MathHelper.clamp(d0 * 8.0D, worldserver1.getWorldBorder().minX() + 16.0D, worldserver1.getWorldBorder().maxX() - 16.0D);
          d1 = MathHelper.clamp(d1 * 8.0D, worldserver1.getWorldBorder().minZ() + 16.0D, worldserver1.getWorldBorder().maxZ() - 16.0D);
        } 
        d0 = MathHelper.clamp((int)d0, -29999872, 29999872);
        d1 = MathHelper.clamp((int)d1, -29999872, 29999872);
        float f = this.rotationYaw;
        setLocationAndAngles(d0, this.posY, d1, 90.0F, 0.0F);
        Teleporter teleporter = worldserver1.getDefaultTeleporter();
        teleporter.placeInExistingPortal(this, f);
        blockpos = new BlockPos(this);
      } 
      worldserver.updateEntityWithOptionalForce(this, false);
      this.world.theProfiler.endStartSection("reloading");
      Entity entity = EntityList.func_191304_a((Class)getClass(), (World)worldserver1);
      if (entity != null) {
        entity.copyDataFromOld(this);
        if (i == 1 && dimensionIn == 1) {
          BlockPos blockpos1 = worldserver1.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
          entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
        } else {
          entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
        } 
        boolean flag = entity.forceSpawn;
        entity.forceSpawn = true;
        worldserver1.spawnEntityInWorld(entity);
        entity.forceSpawn = flag;
        worldserver1.updateEntityWithOptionalForce(entity, false);
      } 
      this.isDead = true;
      this.world.theProfiler.endSection();
      worldserver.resetUpdateEntityTick();
      worldserver1.resetUpdateEntityTick();
      this.world.theProfiler.endSection();
      return entity;
    } 
    return null;
  }
  
  public boolean isNonBoss() {
    return true;
  }
  
  public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
    return blockStateIn.getBlock().getExplosionResistance(this);
  }
  
  public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float p_174816_5_) {
    return true;
  }
  
  public int getMaxFallHeight() {
    return 3;
  }
  
  public Vec3d getLastPortalVec() {
    return this.lastPortalVec;
  }
  
  public EnumFacing getTeleportDirection() {
    return this.teleportDirection;
  }
  
  public boolean doesEntityNotTriggerPressurePlate() {
    return false;
  }
  
  public void addEntityCrashInfo(CrashReportCategory category) {
    category.setDetail("Entity Type", new ICrashReportDetail<String>() {
          public String call() throws Exception {
            return EntityList.func_191301_a(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
          }
        });
    category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
    category.setDetail("Entity Name", new ICrashReportDetail<String>() {
          public String call() throws Exception {
            return Entity.this.getName();
          }
        });
    category.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ) }));
    category.addCrashSection("Entity's Block location", CrashReportCategory.getCoordinateInfo(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)));
    category.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ) }));
    category.setDetail("Entity's Passengers", new ICrashReportDetail<String>() {
          public String call() throws Exception {
            return Entity.this.getPassengers().toString();
          }
        });
    category.setDetail("Entity's Vehicle", new ICrashReportDetail<String>() {
          public String call() throws Exception {
            return Entity.this.getRidingEntity().toString();
          }
        });
  }
  
  public boolean canRenderOnFire() {
    return isBurning();
  }
  
  public void setUniqueId(UUID uniqueIdIn) {
    this.entityUniqueID = uniqueIdIn;
    this.cachedUniqueIdString = this.entityUniqueID.toString();
  }
  
  public UUID getUniqueID() {
    return this.entityUniqueID;
  }
  
  public String getCachedUniqueIdString() {
    return this.cachedUniqueIdString;
  }
  
  public boolean isPushedByWater() {
    return true;
  }
  
  public static double getRenderDistanceWeight() {
    return renderDistanceWeight;
  }
  
  public static void setRenderDistanceWeight(double renderDistWeight) {
    renderDistanceWeight = renderDistWeight;
  }
  
  public ITextComponent getDisplayName() {
    TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
    textcomponentstring.getStyle().setHoverEvent(getHoverEvent());
    textcomponentstring.getStyle().setInsertion(getCachedUniqueIdString());
    return (ITextComponent)textcomponentstring;
  }
  
  public void setCustomNameTag(String name) {
    this.dataManager.set(CUSTOM_NAME, name);
  }
  
  public String getCustomNameTag() {
    return (String)this.dataManager.get(CUSTOM_NAME);
  }
  
  public boolean hasCustomName() {
    return !((String)this.dataManager.get(CUSTOM_NAME)).isEmpty();
  }
  
  public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
    this.dataManager.set(CUSTOM_NAME_VISIBLE, Boolean.valueOf(alwaysRenderNameTag));
  }
  
  public boolean getAlwaysRenderNameTag() {
    return ((Boolean)this.dataManager.get(CUSTOM_NAME_VISIBLE)).booleanValue();
  }
  
  public void setPositionAndUpdate(double x, double y, double z) {
    this.isPositionDirty = true;
    setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
    this.world.updateEntityWithOptionalForce(this, false);
  }
  
  public boolean getAlwaysRenderNameTagForRender() {
    return getAlwaysRenderNameTag();
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {}
  
  public EnumFacing getHorizontalFacing() {
    return EnumFacing.getHorizontal(MathHelper.floor((this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3);
  }
  
  public EnumFacing getAdjustedHorizontalFacing() {
    return getHorizontalFacing();
  }
  
  protected HoverEvent getHoverEvent() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    ResourceLocation resourcelocation = EntityList.func_191301_a(this);
    nbttagcompound.setString("id", getCachedUniqueIdString());
    if (resourcelocation != null)
      nbttagcompound.setString("type", resourcelocation.toString()); 
    nbttagcompound.setString("name", getName());
    return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, (ITextComponent)new TextComponentString(nbttagcompound.toString()));
  }
  
  public boolean isSpectatedByPlayer(EntityPlayerMP player) {
    return true;
  }
  
  public AxisAlignedBB getEntityBoundingBox() {
    return this.boundingBox;
  }
  
  public AxisAlignedBB getRenderBoundingBox() {
    return getEntityBoundingBox();
  }
  
  public void setEntityBoundingBox(AxisAlignedBB bb) {
    this.boundingBox = bb;
  }
  
  public float getEyeHeight() {
    return this.height * 0.85F;
  }
  
  public boolean isOutsideBorder() {
    return this.isOutsideBorder;
  }
  
  public void setOutsideBorder(boolean outsideBorder) {
    this.isOutsideBorder = outsideBorder;
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    return false;
  }
  
  public void addChatMessage(ITextComponent component) {}
  
  public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
    return true;
  }
  
  public BlockPos getPosition() {
    return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
  }
  
  public Vec3d getPositionVector() {
    return new Vec3d(this.posX, this.posY, this.posZ);
  }
  
  public World getEntityWorld() {
    return this.world;
  }
  
  public Entity getCommandSenderEntity() {
    return this;
  }
  
  public boolean sendCommandFeedback() {
    return false;
  }
  
  public void setCommandStat(CommandResultStats.Type type, int amount) {
    if (this.world != null && !this.world.isRemote)
      this.cmdResultStats.setCommandStatForSender(this.world.getMinecraftServer(), this, type, amount); 
  }
  
  @Nullable
  public MinecraftServer getServer() {
    return this.world.getMinecraftServer();
  }
  
  public CommandResultStats getCommandStats() {
    return this.cmdResultStats;
  }
  
  public void setCommandStats(Entity entityIn) {
    this.cmdResultStats.addAllStats(entityIn.getCommandStats());
  }
  
  public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand stack) {
    return EnumActionResult.PASS;
  }
  
  public boolean isImmuneToExplosions() {
    return false;
  }
  
  protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
    if (entityIn instanceof EntityLivingBase)
      EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entityIn, entityLivingBaseIn); 
    EnchantmentHelper.applyArthropodEnchantments(entityLivingBaseIn, entityIn);
  }
  
  public void addTrackingPlayer(EntityPlayerMP player) {}
  
  public void removeTrackingPlayer(EntityPlayerMP player) {}
  
  public float getRotatedYaw(Rotation transformRotation) {
    float f = MathHelper.wrapDegrees(this.rotationYaw);
    switch (transformRotation) {
      case null:
        return f + 180.0F;
      case COUNTERCLOCKWISE_90:
        return f + 270.0F;
      case CLOCKWISE_90:
        return f + 90.0F;
    } 
    return f;
  }
  
  public float getMirroredYaw(Mirror transformMirror) {
    float f = MathHelper.wrapDegrees(this.rotationYaw);
    switch (transformMirror) {
      case LEFT_RIGHT:
        return -f;
      case null:
        return 180.0F - f;
    } 
    return f;
  }
  
  public boolean ignoreItemEntityData() {
    return false;
  }
  
  public boolean setPositionNonDirty() {
    boolean flag = this.isPositionDirty;
    this.isPositionDirty = false;
    return flag;
  }
  
  @Nullable
  public Entity getControllingPassenger() {
    return null;
  }
  
  public List<Entity> getPassengers() {
    return this.riddenByEntities.isEmpty() ? Collections.<Entity>emptyList() : Lists.newArrayList(this.riddenByEntities);
  }
  
  public boolean isPassenger(Entity entityIn) {
    for (Entity entity : getPassengers()) {
      if (entity.equals(entityIn))
        return true; 
    } 
    return false;
  }
  
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> set = Sets.newHashSet();
    getRecursivePassengersByType(Entity.class, set);
    return set;
  }
  
  public <T extends Entity> Collection<T> getRecursivePassengersByType(Class<T> entityClass) {
    Set<T> set = Sets.newHashSet();
    getRecursivePassengersByType(entityClass, set);
    return set;
  }
  
  private <T extends Entity> void getRecursivePassengersByType(Class<T> entityClass, Set<T> theSet) {
    for (Entity entity : getPassengers()) {
      if (entityClass.isAssignableFrom(entity.getClass()))
        theSet.add((T)entity); 
      entity.getRecursivePassengersByType(entityClass, theSet);
    } 
  }
  
  public Entity getLowestRidingEntity() {
    for (Entity entity = this; entity.isRiding(); entity = entity.getRidingEntity());
    return entity;
  }
  
  public boolean isRidingSameEntity(Entity entityIn) {
    return (getLowestRidingEntity() == entityIn.getLowestRidingEntity());
  }
  
  public boolean isRidingOrBeingRiddenBy(Entity entityIn) {
    for (Entity entity : getPassengers()) {
      if (entity.equals(entityIn))
        return true; 
      if (entity.isRidingOrBeingRiddenBy(entityIn))
        return true; 
    } 
    return false;
  }
  
  public boolean canPassengerSteer() {
    Entity entity = getControllingPassenger();
    if (entity instanceof EntityPlayer)
      return ((EntityPlayer)entity).isUser(); 
    return !this.world.isRemote;
  }
  
  @Nullable
  public Entity getRidingEntity() {
    return this.ridingEntity;
  }
  
  public EnumPushReaction getPushReaction() {
    return EnumPushReaction.NORMAL;
  }
  
  public SoundCategory getSoundCategory() {
    return SoundCategory.NEUTRAL;
  }
  
  protected int func_190531_bD() {
    return 1;
  }
  
  protected abstract void entityInit();
  
  protected abstract void readEntityFromNBT(NBTTagCompound paramNBTTagCompound);
  
  protected abstract void writeEntityToNBT(NBTTagCompound paramNBTTagCompound);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */