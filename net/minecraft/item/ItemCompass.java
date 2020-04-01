package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCompass extends Item {
  public ItemCompass() {
    addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
          double rotation;
          
          double rota;
          
          long lastUpdateTick;
          
          public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
            double d0;
            if (entityIn == null && !stack.isOnItemFrame())
              return 0.0F; 
            boolean flag = (entityIn != null);
            Entity entity = flag ? (Entity)entityIn : (Entity)stack.getItemFrame();
            if (worldIn == null)
              worldIn = entity.world; 
            if (worldIn.provider.isSurfaceWorld()) {
              double d1 = flag ? entity.rotationYaw : getFrameRotation((EntityItemFrame)entity);
              d1 = MathHelper.func_191273_b(d1 / 360.0D, 1.0D);
              double d2 = getSpawnToAngle(worldIn, entity) / 6.283185307179586D;
              d0 = 0.5D - d1 - 0.25D - d2;
            } else {
              d0 = Math.random();
            } 
            if (flag)
              d0 = wobble(worldIn, d0); 
            return MathHelper.positiveModulo((float)d0, 1.0F);
          }
          
          private double wobble(World worldIn, double p_185093_2_) {
            if (worldIn.getTotalWorldTime() != this.lastUpdateTick) {
              this.lastUpdateTick = worldIn.getTotalWorldTime();
              double d0 = p_185093_2_ - this.rotation;
              d0 = MathHelper.func_191273_b(d0 + 0.5D, 1.0D) - 0.5D;
              this.rota += d0 * 0.1D;
              this.rota *= 0.8D;
              this.rotation = MathHelper.func_191273_b(this.rotation + this.rota, 1.0D);
            } 
            return this.rotation;
          }
          
          private double getFrameRotation(EntityItemFrame p_185094_1_) {
            return MathHelper.clampAngle(180 + p_185094_1_.facingDirection.getHorizontalIndex() * 90);
          }
          
          private double getSpawnToAngle(World p_185092_1_, Entity p_185092_2_) {
            BlockPos blockpos = p_185092_1_.getSpawnPoint();
            return Math.atan2(blockpos.getZ() - p_185092_2_.posZ, blockpos.getX() - p_185092_2_.posX);
          }
        });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemCompass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */