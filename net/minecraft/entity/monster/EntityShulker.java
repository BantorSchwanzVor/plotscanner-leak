package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityShulker extends EntityGolem implements IMob {
  private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
  
  private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = (new AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0D, 0)).setSaved(false);
  
  protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
  
  protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
  
  protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
  
  protected static final DataParameter<Byte> field_190770_bw = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
  
  public static final EnumDyeColor field_190771_bx = EnumDyeColor.PURPLE;
  
  private float prevPeekAmount;
  
  private float peekAmount;
  
  private BlockPos currentAttachmentPosition;
  
  private int clientSideTeleportInterpolation;
  
  public EntityShulker(World worldIn) {
    super(worldIn);
    setSize(1.0F, 1.0F);
    this.prevRenderYawOffset = 180.0F;
    this.renderYawOffset = 180.0F;
    this.isImmuneToFire = true;
    this.currentAttachmentPosition = null;
    this.experienceValue = 5;
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    this.renderYawOffset = 180.0F;
    this.prevRenderYawOffset = 180.0F;
    this.rotationYaw = 180.0F;
    this.prevRotationYaw = 180.0F;
    this.rotationYawHead = 180.0F;
    this.prevRotationYawHead = 180.0F;
    return super.onInitialSpawn(difficulty, livingdata);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(1, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(4, new AIAttack());
    this.tasks.addTask(7, new AIPeek(null));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
    this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
    this.targetTasks.addTask(2, (EntityAIBase)new AIAttackNearest(this));
    this.targetTasks.addTask(3, (EntityAIBase)new AIDefenseAttack(this));
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  public SoundCategory getSoundCategory() {
    return SoundCategory.HOSTILE;
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_SHULKER_AMBIENT;
  }
  
  public void playLivingSound() {
    if (!isClosed())
      super.playLivingSound(); 
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_SHULKER_DEATH;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(ATTACHED_FACE, EnumFacing.DOWN);
    this.dataManager.register(ATTACHED_BLOCK_POS, Optional.absent());
    this.dataManager.register(PEEK_TICK, Byte.valueOf((byte)0));
    this.dataManager.register(field_190770_bw, Byte.valueOf((byte)field_190771_bx.getMetadata()));
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
  }
  
  protected EntityBodyHelper createBodyHelper() {
    return new BodyHelper((EntityLivingBase)this);
  }
  
  public static void registerFixesShulker(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityShulker.class);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.dataManager.set(ATTACHED_FACE, EnumFacing.getFront(compound.getByte("AttachFace")));
    this.dataManager.set(PEEK_TICK, Byte.valueOf(compound.getByte("Peek")));
    this.dataManager.set(field_190770_bw, Byte.valueOf(compound.getByte("Color")));
    if (compound.hasKey("APX")) {
      int i = compound.getInteger("APX");
      int j = compound.getInteger("APY");
      int k = compound.getInteger("APZ");
      this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
    } else {
      this.dataManager.set(ATTACHED_BLOCK_POS, Optional.absent());
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setByte("AttachFace", (byte)((EnumFacing)this.dataManager.get(ATTACHED_FACE)).getIndex());
    compound.setByte("Peek", ((Byte)this.dataManager.get(PEEK_TICK)).byteValue());
    compound.setByte("Color", ((Byte)this.dataManager.get(field_190770_bw)).byteValue());
    BlockPos blockpos = getAttachmentPos();
    if (blockpos != null) {
      compound.setInteger("APX", blockpos.getX());
      compound.setInteger("APY", blockpos.getY());
      compound.setInteger("APZ", blockpos.getZ());
    } 
  }
  
  public void onUpdate() {
    super.onUpdate();
    BlockPos blockpos = (BlockPos)((Optional)this.dataManager.get(ATTACHED_BLOCK_POS)).orNull();
    if (blockpos == null && !this.world.isRemote) {
      blockpos = new BlockPos((Entity)this);
      this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
    } 
    if (isRiding()) {
      blockpos = null;
      float f = (getRidingEntity()).rotationYaw;
      this.rotationYaw = f;
      this.renderYawOffset = f;
      this.prevRenderYawOffset = f;
      this.clientSideTeleportInterpolation = 0;
    } else if (!this.world.isRemote) {
      IBlockState iblockstate = this.world.getBlockState(blockpos);
      if (iblockstate.getMaterial() != Material.AIR)
        if (iblockstate.getBlock() == Blocks.PISTON_EXTENSION) {
          EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)BlockPistonBase.FACING);
          if (this.world.isAirBlock(blockpos.offset(enumfacing))) {
            blockpos = blockpos.offset(enumfacing);
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
          } else {
            tryTeleportToNewPosition();
          } 
        } else if (iblockstate.getBlock() == Blocks.PISTON_HEAD) {
          EnumFacing enumfacing3 = (EnumFacing)iblockstate.getValue((IProperty)BlockPistonExtension.FACING);
          if (this.world.isAirBlock(blockpos.offset(enumfacing3))) {
            blockpos = blockpos.offset(enumfacing3);
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
          } else {
            tryTeleportToNewPosition();
          } 
        } else {
          tryTeleportToNewPosition();
        }  
      BlockPos blockpos1 = blockpos.offset(getAttachmentFacing());
      if (!this.world.isBlockNormalCube(blockpos1, false)) {
        boolean flag = false;
        byte b;
        int i;
        EnumFacing[] arrayOfEnumFacing;
        for (i = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < i; ) {
          EnumFacing enumfacing1 = arrayOfEnumFacing[b];
          blockpos1 = blockpos.offset(enumfacing1);
          if (this.world.isBlockNormalCube(blockpos1, false)) {
            this.dataManager.set(ATTACHED_FACE, enumfacing1);
            flag = true;
            break;
          } 
          b++;
        } 
        if (!flag)
          tryTeleportToNewPosition(); 
      } 
      BlockPos blockpos2 = blockpos.offset(getAttachmentFacing().getOpposite());
      if (this.world.isBlockNormalCube(blockpos2, false))
        tryTeleportToNewPosition(); 
    } 
    float f1 = getPeekTick() * 0.01F;
    this.prevPeekAmount = this.peekAmount;
    if (this.peekAmount > f1) {
      this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05F, f1, 1.0F);
    } else if (this.peekAmount < f1) {
      this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05F, 0.0F, f1);
    } 
    if (blockpos != null) {
      if (this.world.isRemote)
        if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null) {
          this.clientSideTeleportInterpolation--;
        } else {
          this.currentAttachmentPosition = blockpos;
        }  
      this.posX = blockpos.getX() + 0.5D;
      this.posY = blockpos.getY();
      this.posZ = blockpos.getZ() + 0.5D;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      double d3 = 0.5D - MathHelper.sin((0.5F + this.peekAmount) * 3.1415927F) * 0.5D;
      double d4 = 0.5D - MathHelper.sin((0.5F + this.prevPeekAmount) * 3.1415927F) * 0.5D;
      double d5 = d3 - d4;
      double d0 = 0.0D;
      double d1 = 0.0D;
      double d2 = 0.0D;
      EnumFacing enumfacing2 = getAttachmentFacing();
      switch (enumfacing2) {
        case null:
          setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D + d3, this.posZ + 0.5D));
          d1 = d5;
          break;
        case UP:
          setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY - d3, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
          d1 = -d5;
          break;
        case NORTH:
          setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D + d3));
          d2 = d5;
          break;
        case SOUTH:
          setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D - d3, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
          d2 = -d5;
          break;
        case WEST:
          setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D + d3, this.posY + 1.0D, this.posZ + 0.5D));
          d0 = d5;
          break;
        case EAST:
          setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D - d3, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
          d0 = -d5;
          break;
      } 
      if (d5 > 0.0D) {
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity)this, getEntityBoundingBox());
        if (!list.isEmpty())
          for (Entity entity : list) {
            if (!(entity instanceof EntityShulker) && !entity.noClip)
              entity.moveEntity(MoverType.SHULKER, d0, d1, d2); 
          }  
      } 
    } 
  }
  
  public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
    if (x == MoverType.SHULKER_BOX) {
      tryTeleportToNewPosition();
    } else {
      super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
    } 
  }
  
  public void setPosition(double x, double y, double z) {
    super.setPosition(x, y, z);
    if (this.dataManager != null && this.ticksExisted != 0) {
      Optional<BlockPos> optional = (Optional<BlockPos>)this.dataManager.get(ATTACHED_BLOCK_POS);
      Optional<BlockPos> optional1 = Optional.of(new BlockPos(x, y, z));
      if (!optional1.equals(optional)) {
        this.dataManager.set(ATTACHED_BLOCK_POS, optional1);
        this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
        this.isAirBorne = true;
      } 
    } 
  }
  
  protected boolean tryTeleportToNewPosition() {
    if (!isAIDisabled() && isEntityAlive()) {
      BlockPos blockpos = new BlockPos((Entity)this);
      for (int i = 0; i < 5; i++) {
        BlockPos blockpos1 = blockpos.add(8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17));
        if (blockpos1.getY() > 0 && this.world.isAirBlock(blockpos1) && this.world.func_191503_g((Entity)this) && this.world.getCollisionBoxes((Entity)this, new AxisAlignedBB(blockpos1)).isEmpty()) {
          boolean flag = false;
          byte b;
          int j;
          EnumFacing[] arrayOfEnumFacing;
          for (j = (arrayOfEnumFacing = EnumFacing.values()).length, b = 0; b < j; ) {
            EnumFacing enumfacing = arrayOfEnumFacing[b];
            if (this.world.isBlockNormalCube(blockpos1.offset(enumfacing), false)) {
              this.dataManager.set(ATTACHED_FACE, enumfacing);
              flag = true;
              break;
            } 
            b++;
          } 
          if (flag) {
            playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
            this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos1));
            this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
            setAttackTarget(null);
            return true;
          } 
        } 
      } 
      return false;
    } 
    return true;
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.prevRenderYawOffset = 180.0F;
    this.renderYawOffset = 180.0F;
    this.rotationYaw = 180.0F;
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (ATTACHED_BLOCK_POS.equals(key) && this.world.isRemote && !isRiding()) {
      BlockPos blockpos = getAttachmentPos();
      if (blockpos != null) {
        if (this.currentAttachmentPosition == null) {
          this.currentAttachmentPosition = blockpos;
        } else {
          this.clientSideTeleportInterpolation = 6;
        } 
        this.posX = blockpos.getX() + 0.5D;
        this.posY = blockpos.getY();
        this.posZ = blockpos.getZ() + 0.5D;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
      } 
    } 
    super.notifyDataManagerChange(key);
  }
  
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    this.newPosRotationIncrements = 0;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isClosed()) {
      Entity entity = source.getSourceOfDamage();
      if (entity instanceof net.minecraft.entity.projectile.EntityArrow)
        return false; 
    } 
    if (super.attackEntityFrom(source, amount)) {
      if (getHealth() < getMaxHealth() * 0.5D && this.rand.nextInt(4) == 0)
        tryTeleportToNewPosition(); 
      return true;
    } 
    return false;
  }
  
  private boolean isClosed() {
    return (getPeekTick() == 0);
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox() {
    return isEntityAlive() ? getEntityBoundingBox() : null;
  }
  
  public EnumFacing getAttachmentFacing() {
    return (EnumFacing)this.dataManager.get(ATTACHED_FACE);
  }
  
  @Nullable
  public BlockPos getAttachmentPos() {
    return (BlockPos)((Optional)this.dataManager.get(ATTACHED_BLOCK_POS)).orNull();
  }
  
  public void setAttachmentPos(@Nullable BlockPos pos) {
    this.dataManager.set(ATTACHED_BLOCK_POS, Optional.fromNullable(pos));
  }
  
  public int getPeekTick() {
    return ((Byte)this.dataManager.get(PEEK_TICK)).byteValue();
  }
  
  public void updateArmorModifier(int p_184691_1_) {
    if (!this.world.isRemote) {
      getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER);
      if (p_184691_1_ == 0) {
        getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER);
        playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
      } else {
        playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
      } 
    } 
    this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)p_184691_1_));
  }
  
  public float getClientPeekAmount(float p_184688_1_) {
    return this.prevPeekAmount + (this.peekAmount - this.prevPeekAmount) * p_184688_1_;
  }
  
  public int getClientTeleportInterp() {
    return this.clientSideTeleportInterpolation;
  }
  
  public BlockPos getOldAttachPos() {
    return this.currentAttachmentPosition;
  }
  
  public float getEyeHeight() {
    return 0.5F;
  }
  
  public int getVerticalFaceSpeed() {
    return 180;
  }
  
  public int getHorizontalFaceSpeed() {
    return 180;
  }
  
  public void applyEntityCollision(Entity entityIn) {}
  
  public float getCollisionBorderSize() {
    return 0.0F;
  }
  
  public boolean isAttachedToBlock() {
    return (this.currentAttachmentPosition != null && getAttachmentPos() != null);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_SHULKER;
  }
  
  public EnumDyeColor func_190769_dn() {
    return EnumDyeColor.byMetadata(((Byte)this.dataManager.get(field_190770_bw)).byteValue());
  }
  
  class AIAttack extends EntityAIBase {
    private int attackTime;
    
    public AIAttack() {
      setMutexBits(3);
    }
    
    public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
      if (entitylivingbase != null && entitylivingbase.isEntityAlive())
        return (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL); 
      return false;
    }
    
    public void startExecuting() {
      this.attackTime = 20;
      EntityShulker.this.updateArmorModifier(100);
    }
    
    public void resetTask() {
      EntityShulker.this.updateArmorModifier(0);
    }
    
    public void updateTask() {
      if (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
        this.attackTime--;
        EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
        EntityShulker.this.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 180.0F, 180.0F);
        double d0 = EntityShulker.this.getDistanceSqToEntity((Entity)entitylivingbase);
        if (d0 < 400.0D) {
          if (this.attackTime <= 0) {
            this.attackTime = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
            EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(EntityShulker.this.world, (EntityLivingBase)EntityShulker.this, (Entity)entitylivingbase, EntityShulker.this.getAttachmentFacing().getAxis());
            EntityShulker.this.world.spawnEntityInWorld((Entity)entityshulkerbullet);
            EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.0F);
          } 
        } else {
          EntityShulker.this.setAttackTarget(null);
        } 
        super.updateTask();
      } 
    }
  }
  
  class AIAttackNearest extends EntityAINearestAttackableTarget<EntityPlayer> {
    public AIAttackNearest(EntityShulker shulker) {
      super(shulker, EntityPlayer.class, true);
    }
    
    public boolean shouldExecute() {
      return (EntityShulker.this.world.getDifficulty() == EnumDifficulty.PEACEFUL) ? false : super.shouldExecute();
    }
    
    protected AxisAlignedBB getTargetableArea(double targetDistance) {
      EnumFacing enumfacing = ((EntityShulker)this.taskOwner).getAttachmentFacing();
      if (enumfacing.getAxis() == EnumFacing.Axis.X)
        return this.taskOwner.getEntityBoundingBox().expand(4.0D, targetDistance, targetDistance); 
      return (enumfacing.getAxis() == EnumFacing.Axis.Z) ? this.taskOwner.getEntityBoundingBox().expand(targetDistance, targetDistance, 4.0D) : this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }
  }
  
  static class AIDefenseAttack extends EntityAINearestAttackableTarget<EntityLivingBase> {
    public AIDefenseAttack(EntityShulker shulker) {
      super(shulker, EntityLivingBase.class, 10, true, false, new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
              return p_apply_1_ instanceof IMob;
            }
          });
    }
    
    public boolean shouldExecute() {
      return (this.taskOwner.getTeam() == null) ? false : super.shouldExecute();
    }
    
    protected AxisAlignedBB getTargetableArea(double targetDistance) {
      EnumFacing enumfacing = ((EntityShulker)this.taskOwner).getAttachmentFacing();
      if (enumfacing.getAxis() == EnumFacing.Axis.X)
        return this.taskOwner.getEntityBoundingBox().expand(4.0D, targetDistance, targetDistance); 
      return (enumfacing.getAxis() == EnumFacing.Axis.Z) ? this.taskOwner.getEntityBoundingBox().expand(targetDistance, targetDistance, 4.0D) : this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
    }
  }
  
  class AIPeek extends EntityAIBase {
    private int peekTime;
    
    private AIPeek() {}
    
    public boolean shouldExecute() {
      return (EntityShulker.this.getAttackTarget() == null && EntityShulker.this.rand.nextInt(40) == 0);
    }
    
    public boolean continueExecuting() {
      return (EntityShulker.this.getAttackTarget() == null && this.peekTime > 0);
    }
    
    public void startExecuting() {
      this.peekTime = 20 * (1 + EntityShulker.this.rand.nextInt(3));
      EntityShulker.this.updateArmorModifier(30);
    }
    
    public void resetTask() {
      if (EntityShulker.this.getAttackTarget() == null)
        EntityShulker.this.updateArmorModifier(0); 
    }
    
    public void updateTask() {
      this.peekTime--;
    }
  }
  
  class BodyHelper extends EntityBodyHelper {
    public BodyHelper(EntityLivingBase p_i47062_2_) {
      super(p_i47062_2_);
    }
    
    public void updateRenderAngles() {}
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityShulker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */