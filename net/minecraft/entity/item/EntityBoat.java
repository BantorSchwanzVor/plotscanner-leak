package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityBoat extends Entity {
  private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
  
  private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
  
  private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(EntityBoat.class, DataSerializers.FLOAT);
  
  private static final DataParameter<Integer> BOAT_TYPE = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
  
  private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[] { EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN) };
  
  private final float[] paddlePositions;
  
  private float momentum;
  
  private float outOfControlTicks;
  
  private float deltaRotation;
  
  private int lerpSteps;
  
  private double boatPitch;
  
  private double lerpY;
  
  private double lerpZ;
  
  private double boatYaw;
  
  private double lerpXRot;
  
  private boolean leftInputDown;
  
  private boolean rightInputDown;
  
  private boolean forwardInputDown;
  
  private boolean backInputDown;
  
  private double waterLevel;
  
  private float boatGlide;
  
  private Status status;
  
  private Status previousStatus;
  
  private double lastYd;
  
  public EntityBoat(World worldIn) {
    super(worldIn);
    this.paddlePositions = new float[2];
    this.preventEntitySpawning = true;
    setSize(1.375F, 0.5625F);
  }
  
  public EntityBoat(World worldIn, double x, double y, double z) {
    this(worldIn);
    setPosition(x, y, z);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected void entityInit() {
    this.dataManager.register(TIME_SINCE_HIT, Integer.valueOf(0));
    this.dataManager.register(FORWARD_DIRECTION, Integer.valueOf(1));
    this.dataManager.register(DAMAGE_TAKEN, Float.valueOf(0.0F));
    this.dataManager.register(BOAT_TYPE, Integer.valueOf(Type.OAK.ordinal()));
    byte b;
    int i;
    DataParameter<Boolean>[] arrayOfDataParameter;
    for (i = (arrayOfDataParameter = DATA_ID_PADDLE).length, b = 0; b < i; ) {
      DataParameter<Boolean> dataparameter = arrayOfDataParameter[b];
      this.dataManager.register(dataparameter, Boolean.valueOf(false));
      b++;
    } 
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBox(Entity entityIn) {
    return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
  }
  
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox() {
    return getEntityBoundingBox();
  }
  
  public boolean canBePushed() {
    return true;
  }
  
  public double getMountedYOffset() {
    return -0.1D;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (!this.world.isRemote && !this.isDead) {
      if (source instanceof net.minecraft.util.EntityDamageSourceIndirect && source.getEntity() != null && isPassenger(source.getEntity()))
        return false; 
      setForwardDirection(-getForwardDirection());
      setTimeSinceHit(10);
      setDamageTaken(getDamageTaken() + amount * 10.0F);
      setBeenAttacked();
      boolean flag = (source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode);
      if (flag || getDamageTaken() > 40.0F) {
        if (!flag && this.world.getGameRules().getBoolean("doEntityDrops"))
          dropItemWithOffset(getItemBoat(), 1, 0.0F); 
        setDead();
      } 
      return true;
    } 
    return true;
  }
  
  public void applyEntityCollision(Entity entityIn) {
    if (entityIn instanceof EntityBoat) {
      if ((entityIn.getEntityBoundingBox()).minY < (getEntityBoundingBox()).maxY)
        super.applyEntityCollision(entityIn); 
    } else if ((entityIn.getEntityBoundingBox()).minY <= (getEntityBoundingBox()).minY) {
      super.applyEntityCollision(entityIn);
    } 
  }
  
  public Item getItemBoat() {
    switch (getBoatType()) {
      default:
        return Items.BOAT;
      case SPRUCE:
        return Items.SPRUCE_BOAT;
      case BIRCH:
        return Items.BIRCH_BOAT;
      case JUNGLE:
        return Items.JUNGLE_BOAT;
      case null:
        return Items.ACACIA_BOAT;
      case DARK_OAK:
        break;
    } 
    return Items.DARK_OAK_BOAT;
  }
  
  public void performHurtAnimation() {
    setForwardDirection(-getForwardDirection());
    setTimeSinceHit(10);
    setDamageTaken(getDamageTaken() * 11.0F);
  }
  
  public boolean canBeCollidedWith() {
    return !this.isDead;
  }
  
  public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    this.boatPitch = x;
    this.lerpY = y;
    this.lerpZ = z;
    this.boatYaw = yaw;
    this.lerpXRot = pitch;
    this.lerpSteps = 10;
  }
  
  public EnumFacing getAdjustedHorizontalFacing() {
    return getHorizontalFacing().rotateY();
  }
  
  public void onUpdate() {
    this.previousStatus = this.status;
    this.status = getBoatStatus();
    if (this.status != Status.UNDER_WATER && this.status != Status.UNDER_FLOWING_WATER) {
      this.outOfControlTicks = 0.0F;
    } else {
      this.outOfControlTicks++;
    } 
    if (!this.world.isRemote && this.outOfControlTicks >= 60.0F)
      removePassengers(); 
    if (getTimeSinceHit() > 0)
      setTimeSinceHit(getTimeSinceHit() - 1); 
    if (getDamageTaken() > 0.0F)
      setDamageTaken(getDamageTaken() - 1.0F); 
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    super.onUpdate();
    tickLerp();
    if (canPassengerSteer()) {
      if (getPassengers().isEmpty() || !(getPassengers().get(0) instanceof EntityPlayer))
        setPaddleState(false, false); 
      updateMotion();
      if (this.world.isRemote) {
        controlBoat();
        this.world.sendPacketToServer((Packet)new CPacketSteerBoat(getPaddleState(0), getPaddleState(1)));
      } 
      moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    } else {
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;
    } 
    for (int i = 0; i <= 1; i++) {
      if (getPaddleState(i)) {
        if (!isSilent() && (this.paddlePositions[i] % 6.2831855F) <= 0.7853981633974483D && (this.paddlePositions[i] + 0.39269909262657166D) % 6.283185307179586D >= 0.7853981633974483D) {
          SoundEvent soundevent = func_193047_k();
          if (soundevent != null) {
            Vec3d vec3d = getLook(1.0F);
            double d0 = (i == 1) ? -vec3d.zCoord : vec3d.zCoord;
            double d1 = (i == 1) ? vec3d.xCoord : -vec3d.xCoord;
            this.world.playSound(null, this.posX + d0, this.posY, this.posZ + d1, soundevent, getSoundCategory(), 1.0F, 0.8F + 0.4F * this.rand.nextFloat());
          } 
        } 
        this.paddlePositions[i] = (float)(this.paddlePositions[i] + 0.39269909262657166D);
      } else {
        this.paddlePositions[i] = 0.0F;
      } 
    } 
    doBlockCollisions();
    List<Entity> list = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));
    if (!list.isEmpty()) {
      boolean flag = (!this.world.isRemote && !(getControllingPassenger() instanceof EntityPlayer));
      for (int j = 0; j < list.size(); j++) {
        Entity entity = list.get(j);
        if (!entity.isPassenger(this))
          if (flag && getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof net.minecraft.entity.EntityLivingBase && !(entity instanceof net.minecraft.entity.passive.EntityWaterMob) && !(entity instanceof EntityPlayer)) {
            entity.startRiding(this);
          } else {
            applyEntityCollision(entity);
          }  
      } 
    } 
  }
  
  @Nullable
  protected SoundEvent func_193047_k() {
    switch (getBoatStatus()) {
      case IN_WATER:
      case UNDER_WATER:
      case UNDER_FLOWING_WATER:
        return SoundEvents.field_193779_I;
      case ON_LAND:
        return SoundEvents.field_193778_H;
    } 
    return null;
  }
  
  private void tickLerp() {
    if (this.lerpSteps > 0 && !canPassengerSteer()) {
      double d0 = this.posX + (this.boatPitch - this.posX) / this.lerpSteps;
      double d1 = this.posY + (this.lerpY - this.posY) / this.lerpSteps;
      double d2 = this.posZ + (this.lerpZ - this.posZ) / this.lerpSteps;
      double d3 = MathHelper.wrapDegrees(this.boatYaw - this.rotationYaw);
      this.rotationYaw = (float)(this.rotationYaw + d3 / this.lerpSteps);
      this.rotationPitch = (float)(this.rotationPitch + (this.lerpXRot - this.rotationPitch) / this.lerpSteps);
      this.lerpSteps--;
      setPosition(d0, d1, d2);
      setRotation(this.rotationYaw, this.rotationPitch);
    } 
  }
  
  public void setPaddleState(boolean p_184445_1_, boolean p_184445_2_) {
    this.dataManager.set(DATA_ID_PADDLE[0], Boolean.valueOf(p_184445_1_));
    this.dataManager.set(DATA_ID_PADDLE[1], Boolean.valueOf(p_184445_2_));
  }
  
  public float getRowingTime(int p_184448_1_, float limbSwing) {
    return getPaddleState(p_184448_1_) ? (float)MathHelper.clampedLerp(this.paddlePositions[p_184448_1_] - 0.39269909262657166D, this.paddlePositions[p_184448_1_], limbSwing) : 0.0F;
  }
  
  private Status getBoatStatus() {
    Status entityboat$status = getUnderwaterStatus();
    if (entityboat$status != null) {
      this.waterLevel = (getEntityBoundingBox()).maxY;
      return entityboat$status;
    } 
    if (checkInWater())
      return Status.IN_WATER; 
    float f = getBoatGlide();
    if (f > 0.0F) {
      this.boatGlide = f;
      return Status.ON_LAND;
    } 
    return Status.IN_AIR;
  }
  
  public float getWaterLevelAbove() {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    int i = MathHelper.floor(axisalignedbb.minX);
    int j = MathHelper.ceil(axisalignedbb.maxX);
    int k = MathHelper.floor(axisalignedbb.maxY);
    int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
    int i1 = MathHelper.floor(axisalignedbb.minZ);
    int j1 = MathHelper.ceil(axisalignedbb.maxZ);
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    try {
      for (int k1 = k; k1 < l; k1++) {
        float f = 0.0F;
        int l1 = i;
        label28: while (true) {
          if (l1 >= j) {
            if (f < 1.0F) {
              float f2 = blockpos$pooledmutableblockpos.getY() + f;
              return f2;
            } 
            break;
          } 
          for (int i2 = i1; i2 < j1; i2++) {
            blockpos$pooledmutableblockpos.setPos(l1, k1, i2);
            IBlockState iblockstate = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos);
            if (iblockstate.getMaterial() == Material.WATER)
              f = Math.max(f, BlockLiquid.func_190973_f(iblockstate, (IBlockAccess)this.world, (BlockPos)blockpos$pooledmutableblockpos)); 
            if (f >= 1.0F)
              break label28; 
          } 
          l1++;
        } 
      } 
      float f1 = (l + 1);
      return f1;
    } finally {
      blockpos$pooledmutableblockpos.release();
    } 
  }
  
  public float getBoatGlide() {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
    int i = MathHelper.floor(axisalignedbb1.minX) - 1;
    int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
    int k = MathHelper.floor(axisalignedbb1.minY) - 1;
    int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
    int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
    int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
    List<AxisAlignedBB> list = Lists.newArrayList();
    float f = 0.0F;
    int k1 = 0;
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    try {
      for (int l1 = i; l1 < j; l1++) {
        for (int i2 = i1; i2 < j1; i2++) {
          int j2 = ((l1 != i && l1 != j - 1) ? 0 : 1) + ((i2 != i1 && i2 != j1 - 1) ? 0 : 1);
          if (j2 != 2)
            for (int k2 = k; k2 < l; k2++) {
              if (j2 <= 0 || (k2 != k && k2 != l - 1)) {
                blockpos$pooledmutableblockpos.setPos(l1, k2, i2);
                IBlockState iblockstate = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos);
                iblockstate.addCollisionBoxToList(this.world, (BlockPos)blockpos$pooledmutableblockpos, axisalignedbb1, list, this, false);
                if (!list.isEmpty()) {
                  f += (iblockstate.getBlock()).slipperiness;
                  k1++;
                } 
                list.clear();
              } 
            }  
        } 
      } 
    } finally {
      blockpos$pooledmutableblockpos.release();
    } 
    return f / k1;
  }
  
  private boolean checkInWater() {
    int m;
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    int i = MathHelper.floor(axisalignedbb.minX);
    int j = MathHelper.ceil(axisalignedbb.maxX);
    int k = MathHelper.floor(axisalignedbb.minY);
    int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
    int i1 = MathHelper.floor(axisalignedbb.minZ);
    int j1 = MathHelper.ceil(axisalignedbb.maxZ);
    boolean flag = false;
    this.waterLevel = Double.MIN_VALUE;
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    try {
      for (int k1 = i; k1 < j; k1++) {
        for (int l1 = k; l1 < l; l1++) {
          for (int i2 = i1; i2 < j1; i2++) {
            blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
            IBlockState iblockstate = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos);
            if (iblockstate.getMaterial() == Material.WATER) {
              float f = BlockLiquid.func_190972_g(iblockstate, (IBlockAccess)this.world, (BlockPos)blockpos$pooledmutableblockpos);
              this.waterLevel = Math.max(f, this.waterLevel);
              m = flag | ((axisalignedbb.minY < f) ? 1 : 0);
            } 
          } 
        } 
      } 
    } finally {
      blockpos$pooledmutableblockpos.release();
    } 
    return m;
  }
  
  @Nullable
  private Status getUnderwaterStatus() {
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    double d0 = axisalignedbb.maxY + 0.001D;
    int i = MathHelper.floor(axisalignedbb.minX);
    int j = MathHelper.ceil(axisalignedbb.maxX);
    int k = MathHelper.floor(axisalignedbb.maxY);
    int l = MathHelper.ceil(d0);
    int i1 = MathHelper.floor(axisalignedbb.minZ);
    int j1 = MathHelper.ceil(axisalignedbb.maxZ);
    boolean flag = false;
    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
    try {
      for (int k1 = i; k1 < j; k1++) {
        for (int l1 = k; l1 < l; l1++) {
          for (int i2 = i1; i2 < j1; i2++) {
            blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
            IBlockState iblockstate = this.world.getBlockState((BlockPos)blockpos$pooledmutableblockpos);
            if (iblockstate.getMaterial() == Material.WATER && d0 < BlockLiquid.func_190972_g(iblockstate, (IBlockAccess)this.world, (BlockPos)blockpos$pooledmutableblockpos)) {
              if (((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() != 0) {
                Status entityboat$status = Status.UNDER_FLOWING_WATER;
                return entityboat$status;
              } 
              flag = true;
            } 
          } 
        } 
      } 
    } finally {
      blockpos$pooledmutableblockpos.release();
    } 
    return flag ? Status.UNDER_WATER : null;
  }
  
  private void updateMotion() {
    double d0 = -0.03999999910593033D;
    double d1 = hasNoGravity() ? 0.0D : -0.03999999910593033D;
    double d2 = 0.0D;
    this.momentum = 0.05F;
    if (this.previousStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
      this.waterLevel = (getEntityBoundingBox()).minY + this.height;
      setPosition(this.posX, (getWaterLevelAbove() - this.height) + 0.101D, this.posZ);
      this.motionY = 0.0D;
      this.lastYd = 0.0D;
      this.status = Status.IN_WATER;
    } else {
      if (this.status == Status.IN_WATER) {
        d2 = (this.waterLevel - (getEntityBoundingBox()).minY) / this.height;
        this.momentum = 0.9F;
      } else if (this.status == Status.UNDER_FLOWING_WATER) {
        d1 = -7.0E-4D;
        this.momentum = 0.9F;
      } else if (this.status == Status.UNDER_WATER) {
        d2 = 0.009999999776482582D;
        this.momentum = 0.45F;
      } else if (this.status == Status.IN_AIR) {
        this.momentum = 0.9F;
      } else if (this.status == Status.ON_LAND) {
        this.momentum = this.boatGlide;
        if (getControllingPassenger() instanceof EntityPlayer)
          this.boatGlide /= 2.0F; 
      } 
      this.motionX *= this.momentum;
      this.motionZ *= this.momentum;
      this.deltaRotation *= this.momentum;
      this.motionY += d1;
      if (d2 > 0.0D) {
        double d3 = 0.65D;
        this.motionY += d2 * 0.06153846016296973D;
        double d4 = 0.75D;
        this.motionY *= 0.75D;
      } 
    } 
  }
  
  private void controlBoat() {
    if (isBeingRidden()) {
      float f = 0.0F;
      if (this.leftInputDown)
        this.deltaRotation--; 
      if (this.rightInputDown)
        this.deltaRotation++; 
      if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown)
        f += 0.005F; 
      this.rotationYaw += this.deltaRotation;
      if (this.forwardInputDown)
        f += 0.04F; 
      if (this.backInputDown)
        f -= 0.005F; 
      this.motionX += (MathHelper.sin(-this.rotationYaw * 0.017453292F) * f);
      this.motionZ += (MathHelper.cos(this.rotationYaw * 0.017453292F) * f);
      setPaddleState(!((!this.rightInputDown || this.leftInputDown) && !this.forwardInputDown), !((!this.leftInputDown || this.rightInputDown) && !this.forwardInputDown));
    } 
  }
  
  public void updatePassenger(Entity passenger) {
    if (isPassenger(passenger)) {
      float f = 0.0F;
      float f1 = (float)((this.isDead ? 0.009999999776482582D : getMountedYOffset()) + passenger.getYOffset());
      if (getPassengers().size() > 1) {
        int i = getPassengers().indexOf(passenger);
        if (i == 0) {
          f = 0.2F;
        } else {
          f = -0.6F;
        } 
        if (passenger instanceof EntityAnimal)
          f = (float)(f + 0.2D); 
      } 
      Vec3d vec3d = (new Vec3d(f, 0.0D, 0.0D)).rotateYaw(-this.rotationYaw * 0.017453292F - 1.5707964F);
      passenger.setPosition(this.posX + vec3d.xCoord, this.posY + f1, this.posZ + vec3d.zCoord);
      passenger.rotationYaw += this.deltaRotation;
      passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
      applyYawToEntity(passenger);
      if (passenger instanceof EntityAnimal && getPassengers().size() > 1) {
        int j = (passenger.getEntityId() % 2 == 0) ? 90 : 270;
        passenger.setRenderYawOffset(((EntityAnimal)passenger).renderYawOffset + j);
        passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
      } 
    } 
  }
  
  protected void applyYawToEntity(Entity entityToUpdate) {
    entityToUpdate.setRenderYawOffset(this.rotationYaw);
    float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
    float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
    entityToUpdate.prevRotationYaw += f1 - f;
    entityToUpdate.rotationYaw += f1 - f;
    entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
  }
  
  public void applyOrientationToEntity(Entity entityToUpdate) {
    applyYawToEntity(entityToUpdate);
  }
  
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setString("Type", getBoatType().getName());
  }
  
  protected void readEntityFromNBT(NBTTagCompound compound) {
    if (compound.hasKey("Type", 8))
      setBoatType(Type.getTypeFromString(compound.getString("Type"))); 
  }
  
  public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
    if (player.isSneaking())
      return false; 
    if (!this.world.isRemote && this.outOfControlTicks < 60.0F)
      player.startRiding(this); 
    return true;
  }
  
  protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    this.lastYd = this.motionY;
    if (!isRiding())
      if (onGroundIn) {
        if (this.fallDistance > 3.0F) {
          if (this.status != Status.ON_LAND) {
            this.fallDistance = 0.0F;
            return;
          } 
          fall(this.fallDistance, 1.0F);
          if (!this.world.isRemote && !this.isDead) {
            setDead();
            if (this.world.getGameRules().getBoolean("doEntityDrops")) {
              for (int i = 0; i < 3; i++)
                entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, getBoatType().getMetadata()), 0.0F); 
              for (int j = 0; j < 2; j++)
                dropItemWithOffset(Items.STICK, 1, 0.0F); 
            } 
          } 
        } 
        this.fallDistance = 0.0F;
      } else if (this.world.getBlockState((new BlockPos(this)).down()).getMaterial() != Material.WATER && y < 0.0D) {
        this.fallDistance = (float)(this.fallDistance - y);
      }  
  }
  
  public boolean getPaddleState(int p_184457_1_) {
    return (((Boolean)this.dataManager.get(DATA_ID_PADDLE[p_184457_1_])).booleanValue() && getControllingPassenger() != null);
  }
  
  public void setDamageTaken(float damageTaken) {
    this.dataManager.set(DAMAGE_TAKEN, Float.valueOf(damageTaken));
  }
  
  public float getDamageTaken() {
    return ((Float)this.dataManager.get(DAMAGE_TAKEN)).floatValue();
  }
  
  public void setTimeSinceHit(int timeSinceHit) {
    this.dataManager.set(TIME_SINCE_HIT, Integer.valueOf(timeSinceHit));
  }
  
  public int getTimeSinceHit() {
    return ((Integer)this.dataManager.get(TIME_SINCE_HIT)).intValue();
  }
  
  public void setForwardDirection(int forwardDirection) {
    this.dataManager.set(FORWARD_DIRECTION, Integer.valueOf(forwardDirection));
  }
  
  public int getForwardDirection() {
    return ((Integer)this.dataManager.get(FORWARD_DIRECTION)).intValue();
  }
  
  public void setBoatType(Type boatType) {
    this.dataManager.set(BOAT_TYPE, Integer.valueOf(boatType.ordinal()));
  }
  
  public Type getBoatType() {
    return Type.byId(((Integer)this.dataManager.get(BOAT_TYPE)).intValue());
  }
  
  protected boolean canFitPassenger(Entity passenger) {
    return (getPassengers().size() < 2);
  }
  
  @Nullable
  public Entity getControllingPassenger() {
    List<Entity> list = getPassengers();
    return list.isEmpty() ? null : list.get(0);
  }
  
  public void updateInputs(boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_) {
    this.leftInputDown = p_184442_1_;
    this.rightInputDown = p_184442_2_;
    this.forwardInputDown = p_184442_3_;
    this.backInputDown = p_184442_4_;
  }
  
  public enum Status {
    IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER, ON_LAND, IN_AIR;
  }
  
  public enum Type {
    OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"),
    SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"),
    BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"),
    JUNGLE(BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"),
    ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"),
    DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");
    
    private final String name;
    
    private final int metadata;
    
    Type(int metadataIn, String nameIn) {
      this.name = nameIn;
      this.metadata = metadataIn;
    }
    
    public String getName() {
      return this.name;
    }
    
    public int getMetadata() {
      return this.metadata;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static Type byId(int id) {
      if (id < 0 || id >= (values()).length)
        id = 0; 
      return values()[id];
    }
    
    public static Type getTypeFromString(String nameIn) {
      for (int i = 0; i < (values()).length; i++) {
        if (values()[i].getName().equals(nameIn))
          return values()[i]; 
      } 
      return values()[0];
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */