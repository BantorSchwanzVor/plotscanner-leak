package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityExpBottle extends EntityThrowable {
  public EntityExpBottle(World worldIn) {
    super(worldIn);
  }
  
  public EntityExpBottle(World worldIn, EntityLivingBase throwerIn) {
    super(worldIn, throwerIn);
  }
  
  public EntityExpBottle(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  
  public static void registerFixesExpBottle(DataFixer fixer) {
    EntityThrowable.registerFixesThrowable(fixer, "ThrowableExpBottle");
  }
  
  protected float getGravityVelocity() {
    return 0.07F;
  }
  
  protected void onImpact(RayTraceResult result) {
    if (!this.world.isRemote) {
      this.world.playEvent(2002, new BlockPos((Entity)this), PotionUtils.getPotionColor(PotionTypes.WATER));
      int i = 3 + this.world.rand.nextInt(5) + this.world.rand.nextInt(5);
      while (i > 0) {
        int j = EntityXPOrb.getXPSplit(i);
        i -= j;
        this.world.spawnEntityInWorld(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
      } 
      setDead();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityExpBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */