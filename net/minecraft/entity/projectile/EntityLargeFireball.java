package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball {
  public int explosionPower = 1;
  
  public EntityLargeFireball(World worldIn) {
    super(worldIn);
  }
  
  public EntityLargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn, x, y, z, accelX, accelY, accelZ);
  }
  
  public EntityLargeFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn, shooter, accelX, accelY, accelZ);
  }
  
  protected void onImpact(RayTraceResult result) {
    if (!this.world.isRemote) {
      if (result.entityHit != null) {
        result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, (Entity)this.shootingEntity), 6.0F);
        applyEnchantments(this.shootingEntity, result.entityHit);
      } 
      boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
      this.world.newExplosion(null, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
      setDead();
    } 
  }
  
  public static void registerFixesLargeFireball(DataFixer fixer) {
    EntityFireball.registerFixesFireball(fixer, "Fireball");
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("ExplosionPower", this.explosionPower);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    if (compound.hasKey("ExplosionPower", 99))
      this.explosionPower = compound.getInteger("ExplosionPower"); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntityLargeFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */