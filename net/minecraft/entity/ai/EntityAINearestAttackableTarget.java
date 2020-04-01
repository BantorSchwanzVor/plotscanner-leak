package net.minecraft.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget {
  protected final Class<T> targetClass;
  
  private final int targetChance;
  
  protected final Sorter theNearestAttackableTargetSorter;
  
  protected final Predicate<? super T> targetEntitySelector;
  
  protected T targetEntity;
  
  public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight) {
    this(creature, classTarget, checkSight, false);
  }
  
  public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
    this(creature, classTarget, 10, checkSight, onlyNearby, (Predicate<? super T>)null);
  }
  
  public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
    super(creature, checkSight, onlyNearby);
    this.targetClass = classTarget;
    this.targetChance = chance;
    this.theNearestAttackableTargetSorter = new Sorter((Entity)creature);
    setMutexBits(1);
    this.targetEntitySelector = new Predicate<T>() {
        public boolean apply(@Nullable T p_apply_1_) {
          if (p_apply_1_ == null)
            return false; 
          if (targetSelector != null && !targetSelector.apply(p_apply_1_))
            return false; 
          return !EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) ? false : EntityAINearestAttackableTarget.this.isSuitableTarget((EntityLivingBase)p_apply_1_, false);
        }
      };
  }
  
  public boolean shouldExecute() {
    if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
      return false; 
    if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class) {
      List<T> list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, getTargetableArea(getTargetDistance()), this.targetEntitySelector);
      if (list.isEmpty())
        return false; 
      Collections.sort(list, this.theNearestAttackableTargetSorter);
      this.targetEntity = list.get(0);
      return true;
    } 
    this.targetEntity = (T)this.taskOwner.world.getNearestAttackablePlayer(this.taskOwner.posX, this.taskOwner.posY + this.taskOwner.getEyeHeight(), this.taskOwner.posZ, getTargetDistance(), getTargetDistance(), new Function<EntityPlayer, Double>() {
          @Nullable
          public Double apply(@Nullable EntityPlayer p_apply_1_) {
            ItemStack itemstack = p_apply_1_.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (itemstack.getItem() == Items.SKULL) {
              int i = itemstack.getItemDamage();
              boolean flag = (EntityAINearestAttackableTarget.this.taskOwner instanceof net.minecraft.entity.monster.EntitySkeleton && i == 0);
              boolean flag1 = (EntityAINearestAttackableTarget.this.taskOwner instanceof net.minecraft.entity.monster.EntityZombie && i == 2);
              boolean flag2 = (EntityAINearestAttackableTarget.this.taskOwner instanceof net.minecraft.entity.monster.EntityCreeper && i == 4);
              if (flag || flag1 || flag2)
                return Double.valueOf(0.5D); 
            } 
            return Double.valueOf(1.0D);
          }
        },  this.targetEntitySelector);
    return (this.targetEntity != null);
  }
  
  protected AxisAlignedBB getTargetableArea(double targetDistance) {
    return this.taskOwner.getEntityBoundingBox().expand(targetDistance, 4.0D, targetDistance);
  }
  
  public void startExecuting() {
    this.taskOwner.setAttackTarget((EntityLivingBase)this.targetEntity);
    super.startExecuting();
  }
  
  public static class Sorter implements Comparator<Entity> {
    private final Entity theEntity;
    
    public Sorter(Entity theEntityIn) {
      this.theEntity = theEntityIn;
    }
    
    public int compare(Entity p_compare_1_, Entity p_compare_2_) {
      double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
      double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
      if (d0 < d1)
        return -1; 
      return (d0 > d1) ? 1 : 0;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAINearestAttackableTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */