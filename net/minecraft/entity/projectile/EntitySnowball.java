package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySnowball extends EntityThrowable {
  public EntitySnowball(World worldIn) {
    super(worldIn);
  }
  
  public EntitySnowball(World worldIn, EntityLivingBase throwerIn) {
    super(worldIn, throwerIn);
  }
  
  public EntitySnowball(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  
  public static void registerFixesSnowball(DataFixer fixer) {
    EntityThrowable.registerFixesThrowable(fixer, "Snowball");
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 3)
      for (int i = 0; i < 8; i++)
        this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);  
  }
  
  protected void onImpact(RayTraceResult result) {
    if (result.entityHit != null) {
      int i = 0;
      if (result.entityHit instanceof net.minecraft.entity.monster.EntityBlaze)
        i = 3; 
      result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, (Entity)getThrower()), i);
    } 
    if (!this.world.isRemote) {
      this.world.setEntityState(this, (byte)3);
      setDead();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntitySnowball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */