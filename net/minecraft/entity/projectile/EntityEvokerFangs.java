package net.minecraft.entity.projectile;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityEvokerFangs extends Entity {
  private int field_190553_a;
  
  private boolean field_190554_b;
  
  private int field_190555_c;
  
  private boolean field_190556_d;
  
  private EntityLivingBase field_190557_e;
  
  private UUID field_190558_f;
  
  public EntityEvokerFangs(World p_i47275_1_) {
    super(p_i47275_1_);
    this.field_190555_c = 22;
    setSize(0.5F, 0.8F);
  }
  
  public EntityEvokerFangs(World p_i47276_1_, double p_i47276_2_, double p_i47276_4_, double p_i47276_6_, float p_i47276_8_, int p_i47276_9_, EntityLivingBase p_i47276_10_) {
    this(p_i47276_1_);
    this.field_190553_a = p_i47276_9_;
    func_190549_a(p_i47276_10_);
    this.rotationYaw = p_i47276_8_ * 57.295776F;
    setPosition(p_i47276_2_, p_i47276_4_, p_i47276_6_);
  }
  
  protected void entityInit() {}
  
  public void func_190549_a(@Nullable EntityLivingBase p_190549_1_) {
    this.field_190557_e = p_190549_1_;
    this.field_190558_f = (p_190549_1_ == null) ? null : p_190549_1_.getUniqueID();
  }
  
  @Nullable
  public EntityLivingBase func_190552_j() {
    if (this.field_190557_e == null && this.field_190558_f != null && this.world instanceof WorldServer) {
      Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.field_190558_f);
      if (entity instanceof EntityLivingBase)
        this.field_190557_e = (EntityLivingBase)entity; 
    } 
    return this.field_190557_e;
  }
  
  protected void readEntityFromNBT(NBTTagCompound compound) {
    this.field_190553_a = compound.getInteger("Warmup");
    this.field_190558_f = compound.getUniqueId("OwnerUUID");
  }
  
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setInteger("Warmup", this.field_190553_a);
    if (this.field_190558_f != null)
      compound.setUniqueId("OwnerUUID", this.field_190558_f); 
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (this.world.isRemote) {
      if (this.field_190556_d) {
        this.field_190555_c--;
        if (this.field_190555_c == 14)
          for (int i = 0; i < 12; i++) {
            double d0 = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * this.width * 0.5D;
            double d1 = this.posY + 0.05D + this.rand.nextDouble() * 1.0D;
            double d2 = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * this.width * 0.5D;
            double d3 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
            double d4 = 0.3D + this.rand.nextDouble() * 0.3D;
            double d5 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
            this.world.spawnParticle(EnumParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5, new int[0]);
          }  
      } 
    } else if (--this.field_190553_a < 0) {
      if (this.field_190553_a == -8)
        for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(0.2D, 0.0D, 0.2D)))
          func_190551_c(entitylivingbase);  
      if (!this.field_190554_b) {
        this.world.setEntityState(this, (byte)4);
        this.field_190554_b = true;
      } 
      if (--this.field_190555_c < 0)
        setDead(); 
    } 
  }
  
  private void func_190551_c(EntityLivingBase p_190551_1_) {
    EntityLivingBase entitylivingbase = func_190552_j();
    if (p_190551_1_.isEntityAlive() && !p_190551_1_.func_190530_aW() && p_190551_1_ != entitylivingbase)
      if (entitylivingbase == null) {
        p_190551_1_.attackEntityFrom(DamageSource.magic, 6.0F);
      } else {
        if (entitylivingbase.isOnSameTeam((Entity)p_190551_1_))
          return; 
        p_190551_1_.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, (Entity)entitylivingbase), 6.0F);
      }  
  }
  
  public void handleStatusUpdate(byte id) {
    super.handleStatusUpdate(id);
    if (id == 4) {
      this.field_190556_d = true;
      if (!isSilent())
        this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.field_191242_bl, getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.2F + 0.85F, false); 
    } 
  }
  
  public float func_190550_a(float p_190550_1_) {
    if (!this.field_190556_d)
      return 0.0F; 
    int i = this.field_190555_c - 2;
    return (i <= 0) ? 1.0F : (1.0F - (i - p_190550_1_) / 20.0F);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntityEvokerFangs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */