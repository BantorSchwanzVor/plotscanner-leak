package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityDonkey extends AbstractChestHorse {
  public EntityDonkey(World p_i47298_1_) {
    super(p_i47298_1_);
  }
  
  public static void func_190699_b(DataFixer p_190699_0_) {
    AbstractChestHorse.func_190694_b(p_190699_0_, EntityDonkey.class);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.field_191190_H;
  }
  
  protected SoundEvent getAmbientSound() {
    super.getAmbientSound();
    return SoundEvents.ENTITY_DONKEY_AMBIENT;
  }
  
  protected SoundEvent getDeathSound() {
    super.getDeathSound();
    return SoundEvents.ENTITY_DONKEY_DEATH;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    super.getHurtSound(p_184601_1_);
    return SoundEvents.ENTITY_DONKEY_HURT;
  }
  
  public boolean canMateWith(EntityAnimal otherAnimal) {
    if (otherAnimal == this)
      return false; 
    if (!(otherAnimal instanceof EntityDonkey) && !(otherAnimal instanceof EntityHorse))
      return false; 
    return (canMate() && ((AbstractHorse)otherAnimal).canMate());
  }
  
  public EntityAgeable createChild(EntityAgeable ageable) {
    AbstractHorse abstracthorse = (ageable instanceof EntityHorse) ? new EntityMule(this.world) : new EntityDonkey(this.world);
    func_190681_a(ageable, abstracthorse);
    return abstracthorse;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityDonkey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */