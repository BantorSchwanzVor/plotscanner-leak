package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityElderGuardian extends EntityGuardian {
  public EntityElderGuardian(World p_i47288_1_) {
    super(p_i47288_1_);
    setSize(this.width * 2.35F, this.height * 2.35F);
    enablePersistence();
    if (this.wander != null)
      this.wander.setExecutionChance(400); 
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
    getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
  }
  
  public static void func_190768_b(DataFixer p_190768_0_) {
    EntityLiving.registerFixesMob(p_190768_0_, EntityElderGuardian.class);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_ELDER_GUARDIAN;
  }
  
  public int getAttackDuration() {
    return 60;
  }
  
  public void func_190767_di() {
    this.clientSideSpikesAnimation = 1.0F;
    this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
  }
  
  protected SoundEvent getAmbientSound() {
    return isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEvents.ENTITY_ELDERGUARDIAN_AMBIENTLAND;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_HURT : SoundEvents.ENTITY_ELDER_GUARDIAN_HURT_LAND;
  }
  
  protected SoundEvent getDeathSound() {
    return isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH : SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
  }
  
  protected SoundEvent func_190765_dj() {
    return SoundEvents.field_191240_aK;
  }
  
  protected void updateAITasks() {
    super.updateAITasks();
    int i = 1200;
    if ((this.ticksExisted + getEntityId()) % 1200 == 0) {
      Potion potion = MobEffects.MINING_FATIGUE;
      List<EntityPlayerMP> list = this.world.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>() {
            public boolean apply(@Nullable EntityPlayerMP p_apply_1_) {
              return (EntityElderGuardian.this.getDistanceSqToEntity((Entity)p_apply_1_) < 2500.0D && p_apply_1_.interactionManager.survivalOrAdventure());
            }
          });
      int j = 2;
      int k = 6000;
      int l = 1200;
      for (EntityPlayerMP entityplayermp : list) {
        if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200) {
          entityplayermp.connection.sendPacket((Packet)new SPacketChangeGameState(10, 0.0F));
          entityplayermp.addPotionEffect(new PotionEffect(potion, 6000, 2));
        } 
      } 
    } 
    if (!hasHome())
      setHomePosAndDistance(new BlockPos((Entity)this), 16); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityElderGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */