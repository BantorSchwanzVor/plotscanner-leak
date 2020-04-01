package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityDamageSource extends DamageSource {
  @Nullable
  protected Entity damageSourceEntity;
  
  private boolean isThornsDamage;
  
  public EntityDamageSource(String damageTypeIn, @Nullable Entity damageSourceEntityIn) {
    super(damageTypeIn);
    this.damageSourceEntity = damageSourceEntityIn;
  }
  
  public EntityDamageSource setIsThornsDamage() {
    this.isThornsDamage = true;
    return this;
  }
  
  public boolean getIsThornsDamage() {
    return this.isThornsDamage;
  }
  
  @Nullable
  public Entity getEntity() {
    return this.damageSourceEntity;
  }
  
  public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
    ItemStack itemstack = (this.damageSourceEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.damageSourceEntity).getHeldItemMainhand() : ItemStack.field_190927_a;
    String s = "death.attack." + this.damageType;
    String s1 = String.valueOf(s) + ".item";
    return (!itemstack.func_190926_b() && itemstack.hasDisplayName() && I18n.canTranslate(s1)) ? (ITextComponent)new TextComponentTranslation(s1, new Object[] { entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getTextComponent() }) : (ITextComponent)new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName() });
  }
  
  public boolean isDifficultyScaled() {
    return (this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof net.minecraft.entity.player.EntityPlayer));
  }
  
  @Nullable
  public Vec3d getDamageLocation() {
    return new Vec3d(this.damageSourceEntity.posX, this.damageSourceEntity.posY, this.damageSourceEntity.posZ);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\EntityDamageSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */