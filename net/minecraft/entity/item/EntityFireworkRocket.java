package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity {
  private static final DataParameter<ItemStack> FIREWORK_ITEM = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.OPTIONAL_ITEM_STACK);
  
  private static final DataParameter<Integer> field_191512_b = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.VARINT);
  
  private int fireworkAge;
  
  private int lifetime;
  
  private EntityLivingBase field_191513_e;
  
  public EntityFireworkRocket(World worldIn) {
    super(worldIn);
    setSize(0.25F, 0.25F);
  }
  
  protected void entityInit() {
    this.dataManager.register(FIREWORK_ITEM, ItemStack.field_190927_a);
    this.dataManager.register(field_191512_b, Integer.valueOf(0));
  }
  
  public boolean isInRangeToRenderDist(double distance) {
    return (distance < 4096.0D && !func_191511_j());
  }
  
  public boolean isInRangeToRender3d(double x, double y, double z) {
    return (super.isInRangeToRender3d(x, y, z) && !func_191511_j());
  }
  
  public EntityFireworkRocket(World worldIn, double x, double y, double z, ItemStack givenItem) {
    super(worldIn);
    this.fireworkAge = 0;
    setSize(0.25F, 0.25F);
    setPosition(x, y, z);
    int i = 1;
    if (!givenItem.func_190926_b() && givenItem.hasTagCompound()) {
      this.dataManager.set(FIREWORK_ITEM, givenItem.copy());
      NBTTagCompound nbttagcompound = givenItem.getTagCompound();
      NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Fireworks");
      i += nbttagcompound1.getByte("Flight");
    } 
    this.motionX = this.rand.nextGaussian() * 0.001D;
    this.motionZ = this.rand.nextGaussian() * 0.001D;
    this.motionY = 0.05D;
    this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
  }
  
  public EntityFireworkRocket(World p_i47367_1_, ItemStack p_i47367_2_, EntityLivingBase p_i47367_3_) {
    this(p_i47367_1_, p_i47367_3_.posX, p_i47367_3_.posY, p_i47367_3_.posZ, p_i47367_2_);
    this.dataManager.set(field_191512_b, Integer.valueOf(p_i47367_3_.getEntityId()));
    this.field_191513_e = p_i47367_3_;
  }
  
  public void setVelocity(double x, double y, double z) {
    this.motionX = x;
    this.motionY = y;
    this.motionZ = z;
    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt(x * x + z * z);
      this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232D);
      this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232D);
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
    } 
  }
  
  public void onUpdate() {
    this.lastTickPosX = this.posX;
    this.lastTickPosY = this.posY;
    this.lastTickPosZ = this.posZ;
    super.onUpdate();
    if (func_191511_j()) {
      if (this.field_191513_e == null) {
        Entity entity = this.world.getEntityByID(((Integer)this.dataManager.get(field_191512_b)).intValue());
        if (entity instanceof EntityLivingBase)
          this.field_191513_e = (EntityLivingBase)entity; 
      } 
      if (this.field_191513_e != null) {
        if (this.field_191513_e.isElytraFlying()) {
          Vec3d vec3d = this.field_191513_e.getLookVec();
          double d0 = 1.5D;
          double d1 = 0.1D;
          this.field_191513_e.motionX += vec3d.xCoord * 0.1D + (vec3d.xCoord * 1.5D - this.field_191513_e.motionX) * 0.5D;
          this.field_191513_e.motionY += vec3d.yCoord * 0.1D + (vec3d.yCoord * 1.5D - this.field_191513_e.motionY) * 0.5D;
          this.field_191513_e.motionZ += vec3d.zCoord * 0.1D + (vec3d.zCoord * 1.5D - this.field_191513_e.motionZ) * 0.5D;
        } 
        setPosition(this.field_191513_e.posX, this.field_191513_e.posY, this.field_191513_e.posZ);
        this.motionX = this.field_191513_e.motionX;
        this.motionY = this.field_191513_e.motionY;
        this.motionZ = this.field_191513_e.motionZ;
      } 
    } else {
      this.motionX *= 1.15D;
      this.motionZ *= 1.15D;
      this.motionY += 0.04D;
      moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    } 
    float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232D);
    for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
    while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
      this.prevRotationPitch += 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw < -180.0F)
      this.prevRotationYaw -= 360.0F; 
    while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
      this.prevRotationYaw += 360.0F; 
    this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
    this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    if (this.fireworkAge == 0 && !isSilent())
      this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0F, 1.0F); 
    this.fireworkAge++;
    if (this.world.isRemote && this.fireworkAge % 2 < 2)
      this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3D, this.posZ, this.rand.nextGaussian() * 0.05D, -this.motionY * 0.5D, this.rand.nextGaussian() * 0.05D, new int[0]); 
    if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
      this.world.setEntityState(this, (byte)17);
      func_191510_k();
      setDead();
    } 
  }
  
  private void func_191510_k() {
    float f = 0.0F;
    ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
    NBTTagCompound nbttagcompound = itemstack.func_190926_b() ? null : itemstack.getSubCompound("Fireworks");
    NBTTagList nbttaglist = (nbttagcompound != null) ? nbttagcompound.getTagList("Explosions", 10) : null;
    if (nbttaglist != null && !nbttaglist.hasNoTags())
      f = (5 + nbttaglist.tagCount() * 2); 
    if (f > 0.0F) {
      if (this.field_191513_e != null)
        this.field_191513_e.attackEntityFrom(DamageSource.field_191552_t, (5 + nbttaglist.tagCount() * 2)); 
      double d0 = 5.0D;
      Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
      for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expandXyz(5.0D))) {
        if (entitylivingbase != this.field_191513_e && getDistanceSqToEntity((Entity)entitylivingbase) <= 25.0D) {
          boolean flag = false;
          for (int i = 0; i < 2; i++) {
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, new Vec3d(entitylivingbase.posX, entitylivingbase.posY + entitylivingbase.height * 0.5D * i, entitylivingbase.posZ), false, true, false);
            if (raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS) {
              flag = true;
              break;
            } 
          } 
          if (flag) {
            float f1 = f * (float)Math.sqrt((5.0D - getDistanceToEntity((Entity)entitylivingbase)) / 5.0D);
            entitylivingbase.attackEntityFrom(DamageSource.field_191552_t, f1);
          } 
        } 
      } 
    } 
  }
  
  public boolean func_191511_j() {
    return (((Integer)this.dataManager.get(field_191512_b)).intValue() > 0);
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 17 && this.world.isRemote) {
      ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
      NBTTagCompound nbttagcompound = itemstack.func_190926_b() ? null : itemstack.getSubCompound("Fireworks");
      this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
    } 
    super.handleStatusUpdate(id);
  }
  
  public static void registerFixesFireworkRocket(DataFixer fixer) {
    fixer.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackData(EntityFireworkRocket.class, new String[] { "FireworksItem" }));
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    compound.setInteger("Life", this.fireworkAge);
    compound.setInteger("LifeTime", this.lifetime);
    ItemStack itemstack = (ItemStack)this.dataManager.get(FIREWORK_ITEM);
    if (!itemstack.func_190926_b())
      compound.setTag("FireworksItem", (NBTBase)itemstack.writeToNBT(new NBTTagCompound())); 
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    this.fireworkAge = compound.getInteger("Life");
    this.lifetime = compound.getInteger("LifeTime");
    NBTTagCompound nbttagcompound = compound.getCompoundTag("FireworksItem");
    if (nbttagcompound != null) {
      ItemStack itemstack = new ItemStack(nbttagcompound);
      if (!itemstack.func_190926_b())
        this.dataManager.set(FIREWORK_ITEM, itemstack); 
    } 
  }
  
  public boolean canBeAttackedWithItem() {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityFireworkRocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */