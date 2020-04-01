package net.minecraft.entity.boss;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityDragon extends EntityLiving implements IEntityMultiPart, IMob {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityDragon.class, DataSerializers.VARINT);
  
  public double[][] ringBuffer = new double[64][3];
  
  public int ringBufferIndex = -1;
  
  public MultiPartEntityPart[] dragonPartArray;
  
  public MultiPartEntityPart dragonPartHead = new MultiPartEntityPart(this, "head", 6.0F, 6.0F);
  
  public MultiPartEntityPart dragonPartNeck = new MultiPartEntityPart(this, "neck", 6.0F, 6.0F);
  
  public MultiPartEntityPart dragonPartBody = new MultiPartEntityPart(this, "body", 8.0F, 8.0F);
  
  public MultiPartEntityPart dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
  
  public MultiPartEntityPart dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
  
  public MultiPartEntityPart dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0F, 4.0F);
  
  public MultiPartEntityPart dragonPartWing1 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
  
  public MultiPartEntityPart dragonPartWing2 = new MultiPartEntityPart(this, "wing", 4.0F, 4.0F);
  
  public float prevAnimTime;
  
  public float animTime;
  
  public boolean slowed;
  
  public int deathTicks;
  
  public EntityEnderCrystal healingEnderCrystal;
  
  private final DragonFightManager fightManager;
  
  private final PhaseManager phaseManager;
  
  private int growlTime = 200;
  
  private int sittingDamageReceived;
  
  private final PathPoint[] pathPoints = new PathPoint[24];
  
  private final int[] neighbors = new int[24];
  
  private final PathHeap pathFindQueue = new PathHeap();
  
  public EntityDragon(World worldIn) {
    super(worldIn);
    this.dragonPartArray = new MultiPartEntityPart[] { this.dragonPartHead, this.dragonPartNeck, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartWing1, this.dragonPartWing2 };
    setHealth(getMaxHealth());
    setSize(16.0F, 8.0F);
    this.noClip = true;
    this.isImmuneToFire = true;
    this.growlTime = 100;
    this.ignoreFrustumCheck = true;
    if (!worldIn.isRemote && worldIn.provider instanceof WorldProviderEnd) {
      this.fightManager = ((WorldProviderEnd)worldIn.provider).getDragonFightManager();
    } else {
      this.fightManager = null;
    } 
    this.phaseManager = new PhaseManager(this);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
  }
  
  protected void entityInit() {
    super.entityInit();
    getDataManager().register(PHASE, Integer.valueOf(PhaseList.HOVER.getId()));
  }
  
  public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_) {
    if (getHealth() <= 0.0F)
      p_70974_2_ = 0.0F; 
    p_70974_2_ = 1.0F - p_70974_2_;
    int i = this.ringBufferIndex - p_70974_1_ & 0x3F;
    int j = this.ringBufferIndex - p_70974_1_ - 1 & 0x3F;
    double[] adouble = new double[3];
    double d0 = this.ringBuffer[i][0];
    double d1 = MathHelper.wrapDegrees(this.ringBuffer[j][0] - d0);
    adouble[0] = d0 + d1 * p_70974_2_;
    d0 = this.ringBuffer[i][1];
    d1 = this.ringBuffer[j][1] - d0;
    adouble[1] = d0 + d1 * p_70974_2_;
    adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * p_70974_2_;
    return adouble;
  }
  
  public void onLivingUpdate() {
    if (this.world.isRemote) {
      setHealth(getHealth());
      if (!isSilent()) {
        float f = MathHelper.cos(this.animTime * 6.2831855F);
        float f1 = MathHelper.cos(this.prevAnimTime * 6.2831855F);
        if (f1 <= -0.3F && f >= -0.3F)
          this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false); 
        if (!this.phaseManager.getCurrentPhase().getIsStationary() && --this.growlTime < 0) {
          this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, getSoundCategory(), 2.5F, 0.8F + this.rand.nextFloat() * 0.3F, false);
          this.growlTime = 200 + this.rand.nextInt(200);
        } 
      } 
    } 
    this.prevAnimTime = this.animTime;
    if (getHealth() <= 0.0F) {
      float f12 = (this.rand.nextFloat() - 0.5F) * 8.0F;
      float f13 = (this.rand.nextFloat() - 0.5F) * 4.0F;
      float f15 = (this.rand.nextFloat() - 0.5F) * 8.0F;
      this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f12, this.posY + 2.0D + f13, this.posZ + f15, 0.0D, 0.0D, 0.0D, new int[0]);
    } else {
      updateDragonEnderCrystal();
      float f11 = 0.2F / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
      f11 *= (float)Math.pow(2.0D, this.motionY);
      if (this.phaseManager.getCurrentPhase().getIsStationary()) {
        this.animTime += 0.1F;
      } else if (this.slowed) {
        this.animTime += f11 * 0.5F;
      } else {
        this.animTime += f11;
      } 
      this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
      if (isAIDisabled()) {
        this.animTime = 0.5F;
      } else {
        if (this.ringBufferIndex < 0)
          for (int i = 0; i < this.ringBuffer.length; i++) {
            this.ringBuffer[i][0] = this.rotationYaw;
            this.ringBuffer[i][1] = this.posY;
          }  
        if (++this.ringBufferIndex == this.ringBuffer.length)
          this.ringBufferIndex = 0; 
        this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
        this.ringBuffer[this.ringBufferIndex][1] = this.posY;
        if (this.world.isRemote) {
          if (this.newPosRotationIncrements > 0) {
            double d5 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
            double d0 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
            double d1 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
            double d2 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
            this.rotationYaw = (float)(this.rotationYaw + d2 / this.newPosRotationIncrements);
            this.rotationPitch = (float)(this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
            this.newPosRotationIncrements--;
            setPosition(d5, d0, d1);
            setRotation(this.rotationYaw, this.rotationPitch);
          } 
          this.phaseManager.getCurrentPhase().doClientRenderEffects();
        } else {
          IPhase iphase = this.phaseManager.getCurrentPhase();
          iphase.doLocalUpdate();
          if (this.phaseManager.getCurrentPhase() != iphase) {
            iphase = this.phaseManager.getCurrentPhase();
            iphase.doLocalUpdate();
          } 
          Vec3d vec3d = iphase.getTargetLocation();
          if (vec3d != null) {
            double d6 = vec3d.xCoord - this.posX;
            double d7 = vec3d.yCoord - this.posY;
            double d8 = vec3d.zCoord - this.posZ;
            double d3 = d6 * d6 + d7 * d7 + d8 * d8;
            float f5 = iphase.getMaxRiseOrFall();
            d7 = MathHelper.clamp(d7 / MathHelper.sqrt(d6 * d6 + d8 * d8), -f5, f5);
            this.motionY += d7 * 0.10000000149011612D;
            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
            double d4 = MathHelper.clamp(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d6, d8) * 57.29577951308232D - this.rotationYaw), -50.0D, 50.0D);
            Vec3d vec3d1 = (new Vec3d(vec3d.xCoord - this.posX, vec3d.yCoord - this.posY, vec3d.zCoord - this.posZ)).normalize();
            Vec3d vec3d2 = (new Vec3d(MathHelper.sin(this.rotationYaw * 0.017453292F), this.motionY, -MathHelper.cos(this.rotationYaw * 0.017453292F))).normalize();
            float f7 = Math.max(((float)vec3d2.dotProduct(vec3d1) + 0.5F) / 1.5F, 0.0F);
            this.randomYawVelocity *= 0.8F;
            this.randomYawVelocity = (float)(this.randomYawVelocity + d4 * iphase.getYawFactor());
            this.rotationYaw += this.randomYawVelocity * 0.1F;
            float f8 = (float)(2.0D / (d3 + 1.0D));
            float f9 = 0.06F;
            func_191958_b(0.0F, 0.0F, -1.0F, 0.06F * (f7 * f8 + 1.0F - f8));
            if (this.slowed) {
              moveEntity(MoverType.SELF, this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
            } else {
              moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            } 
            Vec3d vec3d3 = (new Vec3d(this.motionX, this.motionY, this.motionZ)).normalize();
            float f10 = ((float)vec3d3.dotProduct(vec3d2) + 1.0F) / 2.0F;
            f10 = 0.8F + 0.15F * f10;
            this.motionX *= f10;
            this.motionZ *= f10;
            this.motionY *= 0.9100000262260437D;
          } 
        } 
        this.renderYawOffset = this.rotationYaw;
        this.dragonPartHead.width = 1.0F;
        this.dragonPartHead.height = 1.0F;
        this.dragonPartNeck.width = 3.0F;
        this.dragonPartNeck.height = 3.0F;
        this.dragonPartTail1.width = 2.0F;
        this.dragonPartTail1.height = 2.0F;
        this.dragonPartTail2.width = 2.0F;
        this.dragonPartTail2.height = 2.0F;
        this.dragonPartTail3.width = 2.0F;
        this.dragonPartTail3.height = 2.0F;
        this.dragonPartBody.height = 3.0F;
        this.dragonPartBody.width = 5.0F;
        this.dragonPartWing1.height = 2.0F;
        this.dragonPartWing1.width = 4.0F;
        this.dragonPartWing2.height = 3.0F;
        this.dragonPartWing2.width = 4.0F;
        Vec3d[] avec3d = new Vec3d[this.dragonPartArray.length];
        for (int j = 0; j < this.dragonPartArray.length; j++)
          avec3d[j] = new Vec3d((this.dragonPartArray[j]).posX, (this.dragonPartArray[j]).posY, (this.dragonPartArray[j]).posZ); 
        float f14 = (float)(getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F * 0.017453292F;
        float f16 = MathHelper.cos(f14);
        float f2 = MathHelper.sin(f14);
        float f17 = this.rotationYaw * 0.017453292F;
        float f3 = MathHelper.sin(f17);
        float f18 = MathHelper.cos(f17);
        this.dragonPartBody.onUpdate();
        this.dragonPartBody.setLocationAndAngles(this.posX + (f3 * 0.5F), this.posY, this.posZ - (f18 * 0.5F), 0.0F, 0.0F);
        this.dragonPartWing1.onUpdate();
        this.dragonPartWing1.setLocationAndAngles(this.posX + (f18 * 4.5F), this.posY + 2.0D, this.posZ + (f3 * 4.5F), 0.0F, 0.0F);
        this.dragonPartWing2.onUpdate();
        this.dragonPartWing2.setLocationAndAngles(this.posX - (f18 * 4.5F), this.posY + 2.0D, this.posZ - (f3 * 4.5F), 0.0F, 0.0F);
        if (!this.world.isRemote && this.hurtTime == 0) {
          collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
          collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
          attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartHead.getEntityBoundingBox().expandXyz(1.0D)));
          attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartNeck.getEntityBoundingBox().expandXyz(1.0D)));
        } 
        double[] adouble = getMovementOffsets(5, 1.0F);
        float f19 = MathHelper.sin(this.rotationYaw * 0.017453292F - this.randomYawVelocity * 0.01F);
        float f4 = MathHelper.cos(this.rotationYaw * 0.017453292F - this.randomYawVelocity * 0.01F);
        this.dragonPartHead.onUpdate();
        this.dragonPartNeck.onUpdate();
        float f20 = getHeadYOffset(1.0F);
        this.dragonPartHead.setLocationAndAngles(this.posX + (f19 * 6.5F * f16), this.posY + f20 + (f2 * 6.5F), this.posZ - (f4 * 6.5F * f16), 0.0F, 0.0F);
        this.dragonPartNeck.setLocationAndAngles(this.posX + (f19 * 5.5F * f16), this.posY + f20 + (f2 * 5.5F), this.posZ - (f4 * 5.5F * f16), 0.0F, 0.0F);
        for (int k = 0; k < 3; k++) {
          MultiPartEntityPart multipartentitypart = null;
          if (k == 0)
            multipartentitypart = this.dragonPartTail1; 
          if (k == 1)
            multipartentitypart = this.dragonPartTail2; 
          if (k == 2)
            multipartentitypart = this.dragonPartTail3; 
          double[] adouble1 = getMovementOffsets(12 + k * 2, 1.0F);
          float f21 = this.rotationYaw * 0.017453292F + simplifyAngle(adouble1[0] - adouble[0]) * 0.017453292F;
          float f6 = MathHelper.sin(f21);
          float f22 = MathHelper.cos(f21);
          float f23 = 1.5F;
          float f24 = (k + 1) * 2.0F;
          multipartentitypart.onUpdate();
          multipartentitypart.setLocationAndAngles(this.posX - ((f3 * 1.5F + f6 * f24) * f16), this.posY + adouble1[1] - adouble[1] - ((f24 + 1.5F) * f2) + 1.5D, this.posZ + ((f18 * 1.5F + f22 * f24) * f16), 0.0F, 0.0F);
        } 
        if (!this.world.isRemote) {
          this.slowed = destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox()) | destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
          if (this.fightManager != null)
            this.fightManager.dragonUpdate(this); 
        } 
        for (int l = 0; l < this.dragonPartArray.length; l++) {
          (this.dragonPartArray[l]).prevPosX = (avec3d[l]).xCoord;
          (this.dragonPartArray[l]).prevPosY = (avec3d[l]).yCoord;
          (this.dragonPartArray[l]).prevPosZ = (avec3d[l]).zCoord;
        } 
      } 
    } 
  }
  
  private float getHeadYOffset(float p_184662_1_) {
    double d0;
    if (this.phaseManager.getCurrentPhase().getIsStationary()) {
      d0 = -1.0D;
    } else {
      double[] adouble = getMovementOffsets(5, 1.0F);
      double[] adouble1 = getMovementOffsets(0, 1.0F);
      d0 = adouble[1] - adouble1[1];
    } 
    return (float)d0;
  }
  
  private void updateDragonEnderCrystal() {
    if (this.healingEnderCrystal != null)
      if (this.healingEnderCrystal.isDead) {
        this.healingEnderCrystal = null;
      } else if (this.ticksExisted % 10 == 0 && getHealth() < getMaxHealth()) {
        setHealth(getHealth() + 1.0F);
      }  
    if (this.rand.nextInt(10) == 0) {
      List<EntityEnderCrystal> list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, getEntityBoundingBox().expandXyz(32.0D));
      EntityEnderCrystal entityendercrystal = null;
      double d0 = Double.MAX_VALUE;
      for (EntityEnderCrystal entityendercrystal1 : list) {
        double d1 = entityendercrystal1.getDistanceSqToEntity((Entity)this);
        if (d1 < d0) {
          d0 = d1;
          entityendercrystal = entityendercrystal1;
        } 
      } 
      this.healingEnderCrystal = entityendercrystal;
    } 
  }
  
  private void collideWithEntities(List<Entity> p_70970_1_) {
    double d0 = ((this.dragonPartBody.getEntityBoundingBox()).minX + (this.dragonPartBody.getEntityBoundingBox()).maxX) / 2.0D;
    double d1 = ((this.dragonPartBody.getEntityBoundingBox()).minZ + (this.dragonPartBody.getEntityBoundingBox()).maxZ) / 2.0D;
    for (Entity entity : p_70970_1_) {
      if (entity instanceof EntityLivingBase) {
        double d2 = entity.posX - d0;
        double d3 = entity.posZ - d1;
        double d4 = d2 * d2 + d3 * d3;
        entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
        if (!this.phaseManager.getCurrentPhase().getIsStationary() && ((EntityLivingBase)entity).getRevengeTimer() < entity.ticksExisted - 2) {
          entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 5.0F);
          applyEnchantments((EntityLivingBase)this, entity);
        } 
      } 
    } 
  }
  
  private void attackEntitiesInList(List<Entity> p_70971_1_) {
    for (int i = 0; i < p_70971_1_.size(); i++) {
      Entity entity = p_70971_1_.get(i);
      if (entity instanceof EntityLivingBase) {
        entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 10.0F);
        applyEnchantments((EntityLivingBase)this, entity);
      } 
    } 
  }
  
  private float simplifyAngle(double p_70973_1_) {
    return (float)MathHelper.wrapDegrees(p_70973_1_);
  }
  
  private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_) {
    int i = MathHelper.floor(p_70972_1_.minX);
    int j = MathHelper.floor(p_70972_1_.minY);
    int k = MathHelper.floor(p_70972_1_.minZ);
    int l = MathHelper.floor(p_70972_1_.maxX);
    int i1 = MathHelper.floor(p_70972_1_.maxY);
    int j1 = MathHelper.floor(p_70972_1_.maxZ);
    boolean flag = false;
    boolean flag1 = false;
    for (int k1 = i; k1 <= l; k1++) {
      for (int l1 = j; l1 <= i1; l1++) {
        for (int i2 = k; i2 <= j1; i2++) {
          BlockPos blockpos = new BlockPos(k1, l1, i2);
          IBlockState iblockstate = this.world.getBlockState(blockpos);
          Block block = iblockstate.getBlock();
          if (iblockstate.getMaterial() != Material.AIR && iblockstate.getMaterial() != Material.FIRE)
            if (!this.world.getGameRules().getBoolean("mobGriefing")) {
              flag = true;
            } else if (block != Blocks.BARRIER && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME) {
              if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY) {
                flag1 = !(!this.world.setBlockToAir(blockpos) && !flag1);
              } else {
                flag = true;
              } 
            } else {
              flag = true;
            }  
        } 
      } 
    } 
    if (flag1) {
      double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * this.rand.nextFloat();
      double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * this.rand.nextFloat();
      double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * this.rand.nextFloat();
      this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
    return flag;
  }
  
  public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
    damage = this.phaseManager.getCurrentPhase().getAdjustedDamage(dragonPart, source, damage);
    if (dragonPart != this.dragonPartHead)
      damage = damage / 4.0F + Math.min(damage, 1.0F); 
    if (damage < 0.01F)
      return false; 
    if (source.getEntity() instanceof EntityPlayer || source.isExplosion()) {
      float f = getHealth();
      attackDragonFrom(source, damage);
      if (getHealth() <= 0.0F && !this.phaseManager.getCurrentPhase().getIsStationary()) {
        setHealth(1.0F);
        this.phaseManager.setPhase(PhaseList.DYING);
      } 
      if (this.phaseManager.getCurrentPhase().getIsStationary()) {
        this.sittingDamageReceived = (int)(this.sittingDamageReceived + f - getHealth());
        if (this.sittingDamageReceived > 0.25F * getMaxHealth()) {
          this.sittingDamageReceived = 0;
          this.phaseManager.setPhase(PhaseList.TAKEOFF);
        } 
      } 
    } 
    return true;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
      attackEntityFromPart(this.dragonPartBody, source, amount); 
    return false;
  }
  
  protected boolean attackDragonFrom(DamageSource source, float amount) {
    return super.attackEntityFrom(source, amount);
  }
  
  public void onKillCommand() {
    setDead();
    if (this.fightManager != null) {
      this.fightManager.dragonUpdate(this);
      this.fightManager.processDragonDeath(this);
    } 
  }
  
  protected void onDeathUpdate() {
    if (this.fightManager != null)
      this.fightManager.dragonUpdate(this); 
    this.deathTicks++;
    if (this.deathTicks >= 180 && this.deathTicks <= 200) {
      float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
      float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
      float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
      this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
    } 
    boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
    int i = 500;
    if (this.fightManager != null && !this.fightManager.hasPreviouslyKilledDragon())
      i = 12000; 
    if (!this.world.isRemote) {
      if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag)
        dropExperience(MathHelper.floor(i * 0.08F)); 
      if (this.deathTicks == 1)
        this.world.playBroadcastSound(1028, new BlockPos((Entity)this), 0); 
    } 
    moveEntity(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
    this.rotationYaw += 20.0F;
    this.renderYawOffset = this.rotationYaw;
    if (this.deathTicks == 200 && !this.world.isRemote) {
      if (flag)
        dropExperience(MathHelper.floor(i * 0.2F)); 
      if (this.fightManager != null)
        this.fightManager.processDragonDeath(this); 
      setDead();
    } 
  }
  
  private void dropExperience(int p_184668_1_) {
    while (p_184668_1_ > 0) {
      int i = EntityXPOrb.getXPSplit(p_184668_1_);
      p_184668_1_ -= i;
      this.world.spawnEntityInWorld((Entity)new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
    } 
  }
  
  public int initPathPoints() {
    if (this.pathPoints[0] == null) {
      for (int i = 0; i < 24; i++) {
        int l, i1, j = 5;
        if (i < 12) {
          l = (int)(60.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.2617994F * i)));
          i1 = (int)(60.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.2617994F * i)));
        } else if (i < 20) {
          int lvt_3_1_ = i - 12;
          l = (int)(40.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.3926991F * lvt_3_1_)));
          i1 = (int)(40.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.3926991F * lvt_3_1_)));
          j += 10;
        } else {
          int k1 = i - 20;
          l = (int)(20.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.7853982F * k1)));
          i1 = (int)(20.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.7853982F * k1)));
        } 
        int j1 = Math.max(this.world.getSeaLevel() + 10, this.world.getTopSolidOrLiquidBlock(new BlockPos(l, 0, i1)).getY() + j);
        this.pathPoints[i] = new PathPoint(l, j1, i1);
      } 
      this.neighbors[0] = 6146;
      this.neighbors[1] = 8197;
      this.neighbors[2] = 8202;
      this.neighbors[3] = 16404;
      this.neighbors[4] = 32808;
      this.neighbors[5] = 32848;
      this.neighbors[6] = 65696;
      this.neighbors[7] = 131392;
      this.neighbors[8] = 131712;
      this.neighbors[9] = 263424;
      this.neighbors[10] = 526848;
      this.neighbors[11] = 525313;
      this.neighbors[12] = 1581057;
      this.neighbors[13] = 3166214;
      this.neighbors[14] = 2138120;
      this.neighbors[15] = 6373424;
      this.neighbors[16] = 4358208;
      this.neighbors[17] = 12910976;
      this.neighbors[18] = 9044480;
      this.neighbors[19] = 9706496;
      this.neighbors[20] = 15216640;
      this.neighbors[21] = 13688832;
      this.neighbors[22] = 11763712;
      this.neighbors[23] = 8257536;
    } 
    return getNearestPpIdx(this.posX, this.posY, this.posZ);
  }
  
  public int getNearestPpIdx(double x, double y, double z) {
    float f = 10000.0F;
    int i = 0;
    PathPoint pathpoint = new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    int j = 0;
    if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0)
      j = 12; 
    for (int k = j; k < 24; k++) {
      if (this.pathPoints[k] != null) {
        float f1 = this.pathPoints[k].distanceToSquared(pathpoint);
        if (f1 < f) {
          f = f1;
          i = k;
        } 
      } 
    } 
    return i;
  }
  
  @Nullable
  public Path findPath(int startIdx, int finishIdx, @Nullable PathPoint andThen) {
    for (int i = 0; i < 24; i++) {
      PathPoint pathpoint = this.pathPoints[i];
      pathpoint.visited = false;
      pathpoint.distanceToTarget = 0.0F;
      pathpoint.totalPathDistance = 0.0F;
      pathpoint.distanceToNext = 0.0F;
      pathpoint.previous = null;
      pathpoint.index = -1;
    } 
    PathPoint pathpoint4 = this.pathPoints[startIdx];
    PathPoint pathpoint5 = this.pathPoints[finishIdx];
    pathpoint4.totalPathDistance = 0.0F;
    pathpoint4.distanceToNext = pathpoint4.distanceTo(pathpoint5);
    pathpoint4.distanceToTarget = pathpoint4.distanceToNext;
    this.pathFindQueue.clearPath();
    this.pathFindQueue.addPoint(pathpoint4);
    PathPoint pathpoint1 = pathpoint4;
    int j = 0;
    if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0)
      j = 12; 
    while (!this.pathFindQueue.isPathEmpty()) {
      PathPoint pathpoint2 = this.pathFindQueue.dequeue();
      if (pathpoint2.equals(pathpoint5)) {
        if (andThen != null) {
          andThen.previous = pathpoint5;
          pathpoint5 = andThen;
        } 
        return makePath(pathpoint4, pathpoint5);
      } 
      if (pathpoint2.distanceTo(pathpoint5) < pathpoint1.distanceTo(pathpoint5))
        pathpoint1 = pathpoint2; 
      pathpoint2.visited = true;
      int k = 0;
      for (int l = 0; l < 24; l++) {
        if (this.pathPoints[l] == pathpoint2) {
          k = l;
          break;
        } 
      } 
      for (int i1 = j; i1 < 24; i1++) {
        if ((this.neighbors[k] & 1 << i1) > 0) {
          PathPoint pathpoint3 = this.pathPoints[i1];
          if (!pathpoint3.visited) {
            float f = pathpoint2.totalPathDistance + pathpoint2.distanceTo(pathpoint3);
            if (!pathpoint3.isAssigned() || f < pathpoint3.totalPathDistance) {
              pathpoint3.previous = pathpoint2;
              pathpoint3.totalPathDistance = f;
              pathpoint3.distanceToNext = pathpoint3.distanceTo(pathpoint5);
              if (pathpoint3.isAssigned()) {
                this.pathFindQueue.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
              } else {
                pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
                this.pathFindQueue.addPoint(pathpoint3);
              } 
            } 
          } 
        } 
      } 
    } 
    if (pathpoint1 == pathpoint4)
      return null; 
    LOGGER.debug("Failed to find path from {} to {}", Integer.valueOf(startIdx), Integer.valueOf(finishIdx));
    if (andThen != null) {
      andThen.previous = pathpoint1;
      pathpoint1 = andThen;
    } 
    return makePath(pathpoint4, pathpoint1);
  }
  
  private Path makePath(PathPoint start, PathPoint finish) {
    int i = 1;
    for (PathPoint pathpoint = finish; pathpoint.previous != null; pathpoint = pathpoint.previous)
      i++; 
    PathPoint[] apathpoint = new PathPoint[i];
    PathPoint pathpoint1 = finish;
    i--;
    for (apathpoint[i] = finish; pathpoint1.previous != null; apathpoint[i] = pathpoint1) {
      pathpoint1 = pathpoint1.previous;
      i--;
    } 
    return new Path(apathpoint);
  }
  
  public static void registerFixesDragon(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityDragon.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("DragonPhase", this.phaseManager.getCurrentPhase().getPhaseList().getId());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("DragonPhase"))
      this.phaseManager.setPhase(PhaseList.getById(compound.getInteger("DragonPhase"))); 
  }
  
  protected void despawnEntity() {}
  
  public Entity[] getParts() {
    return (Entity[])this.dragonPartArray;
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public World getWorld() {
    return this.world;
  }
  
  public SoundCategory getSoundCategory() {
    return SoundCategory.HOSTILE;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_ENDERDRAGON_HURT;
  }
  
  protected float getSoundVolume() {
    return 5.0F;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.field_191189_ay;
  }
  
  public float getHeadPartYOffset(int p_184667_1_, double[] p_184667_2_, double[] p_184667_3_) {
    double d0;
    IPhase iphase = this.phaseManager.getCurrentPhase();
    PhaseList<? extends IPhase> phaselist = iphase.getPhaseList();
    if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
      if (iphase.getIsStationary()) {
        d0 = p_184667_1_;
      } else if (p_184667_1_ == 6) {
        d0 = 0.0D;
      } else {
        d0 = p_184667_3_[1] - p_184667_2_[1];
      } 
    } else {
      BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
      float f = Math.max(MathHelper.sqrt(getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
      d0 = (p_184667_1_ / f);
    } 
    return (float)d0;
  }
  
  public Vec3d getHeadLookVec(float p_184665_1_) {
    Vec3d vec3d;
    IPhase iphase = this.phaseManager.getCurrentPhase();
    PhaseList<? extends IPhase> phaselist = iphase.getPhaseList();
    if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
      if (iphase.getIsStationary()) {
        float f4 = this.rotationPitch;
        float f5 = 1.5F;
        this.rotationPitch = -45.0F;
        vec3d = getLook(p_184665_1_);
        this.rotationPitch = f4;
      } else {
        vec3d = getLook(p_184665_1_);
      } 
    } else {
      BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
      float f = Math.max(MathHelper.sqrt(getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
      float f1 = 6.0F / f;
      float f2 = this.rotationPitch;
      float f3 = 1.5F;
      this.rotationPitch = -f1 * 1.5F * 5.0F;
      vec3d = getLook(p_184665_1_);
      this.rotationPitch = f2;
    } 
    return vec3d;
  }
  
  public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc) {
    EntityPlayer entityplayer;
    if (dmgSrc.getEntity() instanceof EntityPlayer) {
      entityplayer = (EntityPlayer)dmgSrc.getEntity();
    } else {
      entityplayer = this.world.getNearestAttackablePlayer(pos, 64.0D, 64.0D);
    } 
    if (crystal == this.healingEnderCrystal)
      attackEntityFromPart(this.dragonPartHead, DamageSource.causeExplosionDamage((EntityLivingBase)entityplayer), 10.0F); 
    this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, dmgSrc, entityplayer);
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (PHASE.equals(key) && this.world.isRemote)
      this.phaseManager.setPhase(PhaseList.getById(((Integer)getDataManager().get(PHASE)).intValue())); 
    super.notifyDataManagerChange(key);
  }
  
  public PhaseManager getPhaseManager() {
    return this.phaseManager;
  }
  
  @Nullable
  public DragonFightManager getFightManager() {
    return this.fightManager;
  }
  
  public void addPotionEffect(PotionEffect potioneffectIn) {}
  
  protected boolean canBeRidden(Entity entityIn) {
    return false;
  }
  
  public boolean isNonBoss() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\EntityDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */