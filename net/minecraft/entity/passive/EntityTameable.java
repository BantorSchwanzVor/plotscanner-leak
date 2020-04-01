package net.minecraft.entity.passive;

import com.google.common.base.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable {
  protected static final DataParameter<Byte> TAMED = EntityDataManager.createKey(EntityTameable.class, DataSerializers.BYTE);
  
  protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityTameable.class, DataSerializers.OPTIONAL_UNIQUE_ID);
  
  protected EntityAISit aiSit;
  
  public EntityTameable(World worldIn) {
    super(worldIn);
    setupTamedAI();
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(TAMED, Byte.valueOf((byte)0));
    this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    if (getOwnerId() == null) {
      compound.setString("OwnerUUID", "");
    } else {
      compound.setString("OwnerUUID", getOwnerId().toString());
    } 
    compound.setBoolean("Sitting", isSitting());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    String s;
    super.readEntityFromNBT(compound);
    if (compound.hasKey("OwnerUUID", 8)) {
      s = compound.getString("OwnerUUID");
    } else {
      String s1 = compound.getString("Owner");
      s = PreYggdrasilConverter.convertMobOwnerIfNeeded(getServer(), s1);
    } 
    if (!s.isEmpty())
      try {
        setOwnerId(UUID.fromString(s));
        setTamed(true);
      } catch (Throwable var4) {
        setTamed(false);
      }  
    if (this.aiSit != null)
      this.aiSit.setSitting(compound.getBoolean("Sitting")); 
    setSitting(compound.getBoolean("Sitting"));
  }
  
  public boolean canBeLeashedTo(EntityPlayer player) {
    return !getLeashed();
  }
  
  protected void playTameEffect(boolean play) {
    EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
    if (!play)
      enumparticletypes = EnumParticleTypes.SMOKE_NORMAL; 
    for (int i = 0; i < 7; i++) {
      double d0 = this.rand.nextGaussian() * 0.02D;
      double d1 = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      this.world.spawnParticle(enumparticletypes, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 7) {
      playTameEffect(true);
    } else if (id == 6) {
      playTameEffect(false);
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public boolean isTamed() {
    return ((((Byte)this.dataManager.get(TAMED)).byteValue() & 0x4) != 0);
  }
  
  public void setTamed(boolean tamed) {
    byte b0 = ((Byte)this.dataManager.get(TAMED)).byteValue();
    if (tamed) {
      this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 | 0x4)));
    } else {
      this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 & 0xFFFFFFFB)));
    } 
    setupTamedAI();
  }
  
  protected void setupTamedAI() {}
  
  public boolean isSitting() {
    return ((((Byte)this.dataManager.get(TAMED)).byteValue() & 0x1) != 0);
  }
  
  public void setSitting(boolean sitting) {
    byte b0 = ((Byte)this.dataManager.get(TAMED)).byteValue();
    if (sitting) {
      this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 | 0x1)));
    } else {
      this.dataManager.set(TAMED, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
    } 
  }
  
  @Nullable
  public UUID getOwnerId() {
    return (UUID)((Optional)this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
  }
  
  public void setOwnerId(@Nullable UUID p_184754_1_) {
    this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
  }
  
  public void func_193101_c(EntityPlayer p_193101_1_) {
    setTamed(true);
    setOwnerId(p_193101_1_.getUniqueID());
    if (p_193101_1_ instanceof EntityPlayerMP)
      CriteriaTriggers.field_193136_w.func_193178_a((EntityPlayerMP)p_193101_1_, this); 
  }
  
  @Nullable
  public EntityLivingBase getOwner() {
    try {
      UUID uuid = getOwnerId();
      return (uuid == null) ? null : (EntityLivingBase)this.world.getPlayerEntityByUUID(uuid);
    } catch (IllegalArgumentException var2) {
      return null;
    } 
  }
  
  public boolean isOwner(EntityLivingBase entityIn) {
    return (entityIn == getOwner());
  }
  
  public EntityAISit getAISit() {
    return this.aiSit;
  }
  
  public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
    return true;
  }
  
  public Team getTeam() {
    if (isTamed()) {
      EntityLivingBase entitylivingbase = getOwner();
      if (entitylivingbase != null)
        return entitylivingbase.getTeam(); 
    } 
    return super.getTeam();
  }
  
  public boolean isOnSameTeam(Entity entityIn) {
    if (isTamed()) {
      EntityLivingBase entitylivingbase = getOwner();
      if (entityIn == entitylivingbase)
        return true; 
      if (entitylivingbase != null)
        return entitylivingbase.isOnSameTeam(entityIn); 
    } 
    return super.isOnSameTeam(entityIn);
  }
  
  public void onDeath(DamageSource cause) {
    if (!this.world.isRemote && this.world.getGameRules().getBoolean("showDeathMessages") && getOwner() instanceof EntityPlayerMP)
      getOwner().addChatMessage(getCombatTracker().getDeathMessage()); 
    super.onDeath(cause);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityTameable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */