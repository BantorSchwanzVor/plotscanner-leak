package net.minecraft.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball {
  public EntitySmallFireball(World worldIn) {
    super(worldIn);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
    super(worldIn, shooter, accelX, accelY, accelZ);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
    super(worldIn, x, y, z, accelX, accelY, accelZ);
    setSize(0.3125F, 0.3125F);
  }
  
  public static void registerFixesSmallFireball(DataFixer fixer) {
    EntityFireball.registerFixesFireball(fixer, "SmallFireball");
  }
  
  protected void onImpact(RayTraceResult result) {
    if (!this.world.isRemote) {
      if (result.entityHit != null) {
        if (!result.entityHit.isImmuneToFire()) {
          boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, (Entity)this.shootingEntity), 5.0F);
          if (flag) {
            applyEnchantments(this.shootingEntity, result.entityHit);
            result.entityHit.setFire(5);
          } 
        } 
      } else {
        boolean flag1 = true;
        if (this.shootingEntity != null && this.shootingEntity instanceof net.minecraft.entity.EntityLiving)
          flag1 = this.world.getGameRules().getBoolean("mobGriefing"); 
        if (flag1) {
          BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
          if (this.world.isAirBlock(blockpos))
            this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState()); 
        } 
      } 
      setDead();
    } 
  }
  
  public boolean canBeCollidedWith() {
    return false;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\projectile\EntitySmallFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */