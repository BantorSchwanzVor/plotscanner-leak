package net.minecraft.util;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class EntityDamageSourceIndirect extends EntityDamageSource {
  private final Entity indirectEntity;
  
  public EntityDamageSourceIndirect(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn) {
    super(damageTypeIn, source);
    this.indirectEntity = indirectEntityIn;
  }
  
  @Nullable
  public Entity getSourceOfDamage() {
    return this.damageSourceEntity;
  }
  
  @Nullable
  public Entity getEntity() {
    return this.indirectEntity;
  }
  
  public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
    ITextComponent itextcomponent = (this.indirectEntity == null) ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
    ItemStack itemstack = (this.indirectEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.indirectEntity).getHeldItemMainhand() : ItemStack.field_190927_a;
    String s = "death.attack." + this.damageType;
    String s1 = String.valueOf(s) + ".item";
    return (!itemstack.func_190926_b() && itemstack.hasDisplayName() && I18n.canTranslate(s1)) ? (ITextComponent)new TextComponentTranslation(s1, new Object[] { entityLivingBaseIn.getDisplayName(), itextcomponent, itemstack.getTextComponent() }) : (ITextComponent)new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), itextcomponent });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\EntityDamageSourceIndirect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */