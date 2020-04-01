package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntitySpellcasterIllager extends AbstractIllager {
  private static final DataParameter<Byte> field_193088_c = EntityDataManager.createKey(EntitySpellcasterIllager.class, DataSerializers.BYTE);
  
  protected int field_193087_b;
  
  private SpellType field_193089_bx = SpellType.NONE;
  
  public EntitySpellcasterIllager(World p_i47506_1_) {
    super(p_i47506_1_);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(field_193088_c, Byte.valueOf((byte)0));
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.field_193087_b = compound.getInteger("SpellTicks");
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("SpellTicks", this.field_193087_b);
  }
  
  public AbstractIllager.IllagerArmPose func_193077_p() {
    return func_193082_dl() ? AbstractIllager.IllagerArmPose.SPELLCASTING : AbstractIllager.IllagerArmPose.CROSSED;
  }
  
  public boolean func_193082_dl() {
    if (this.world.isRemote)
      return (((Byte)this.dataManager.get(field_193088_c)).byteValue() > 0); 
    return (this.field_193087_b > 0);
  }
  
  public void func_193081_a(SpellType p_193081_1_) {
    this.field_193089_bx = p_193081_1_;
    this.dataManager.set(field_193088_c, Byte.valueOf((byte)p_193081_1_.field_193345_g));
  }
  
  protected SpellType func_193083_dm() {
    return !this.world.isRemote ? this.field_193089_bx : SpellType.func_193337_a(((Byte)this.dataManager.get(field_193088_c)).byteValue());
  }
  
  protected void updateAITasks() {
    super.updateAITasks();
    if (this.field_193087_b > 0)
      this.field_193087_b--; 
  }
  
  public void onUpdate() {
    super.onUpdate();
    if (this.world.isRemote && func_193082_dl()) {
      SpellType entityspellcasterillager$spelltype = func_193083_dm();
      double d0 = entityspellcasterillager$spelltype.field_193346_h[0];
      double d1 = entityspellcasterillager$spelltype.field_193346_h[1];
      double d2 = entityspellcasterillager$spelltype.field_193346_h[2];
      float f = this.renderYawOffset * 0.017453292F + MathHelper.cos(this.ticksExisted * 0.6662F) * 0.25F;
      float f1 = MathHelper.cos(f);
      float f2 = MathHelper.sin(f);
      this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f1 * 0.6D, this.posY + 1.8D, this.posZ + f2 * 0.6D, d0, d1, d2, new int[0]);
      this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - f1 * 0.6D, this.posY + 1.8D, this.posZ - f2 * 0.6D, d0, d1, d2, new int[0]);
    } 
  }
  
  protected int func_193085_dn() {
    return this.field_193087_b;
  }
  
  protected abstract SoundEvent func_193086_dk();
  
  public class AICastingApell extends EntityAIBase {
    public AICastingApell() {
      setMutexBits(3);
    }
    
    public boolean shouldExecute() {
      return (EntitySpellcasterIllager.this.func_193085_dn() > 0);
    }
    
    public void startExecuting() {
      super.startExecuting();
      EntitySpellcasterIllager.this.navigator.clearPathEntity();
    }
    
    public void resetTask() {
      super.resetTask();
      EntitySpellcasterIllager.this.func_193081_a(EntitySpellcasterIllager.SpellType.NONE);
    }
    
    public void updateTask() {
      if (EntitySpellcasterIllager.this.getAttackTarget() != null)
        EntitySpellcasterIllager.this.getLookHelper().setLookPositionWithEntity((Entity)EntitySpellcasterIllager.this.getAttackTarget(), EntitySpellcasterIllager.this.getHorizontalFaceSpeed(), EntitySpellcasterIllager.this.getVerticalFaceSpeed()); 
    }
  }
  
  public abstract class AIUseSpell extends EntityAIBase {
    protected int field_193321_c;
    
    protected int field_193322_d;
    
    public boolean shouldExecute() {
      if (EntitySpellcasterIllager.this.getAttackTarget() == null)
        return false; 
      if (EntitySpellcasterIllager.this.func_193082_dl())
        return false; 
      return (EntitySpellcasterIllager.this.ticksExisted >= this.field_193322_d);
    }
    
    public boolean continueExecuting() {
      return (EntitySpellcasterIllager.this.getAttackTarget() != null && this.field_193321_c > 0);
    }
    
    public void startExecuting() {
      this.field_193321_c = func_190867_m();
      EntitySpellcasterIllager.this.field_193087_b = func_190869_f();
      this.field_193322_d = EntitySpellcasterIllager.this.ticksExisted + func_190872_i();
      SoundEvent soundevent = func_190871_k();
      if (soundevent != null)
        EntitySpellcasterIllager.this.playSound(soundevent, 1.0F, 1.0F); 
      EntitySpellcasterIllager.this.func_193081_a(func_193320_l());
    }
    
    public void updateTask() {
      this.field_193321_c--;
      if (this.field_193321_c == 0) {
        func_190868_j();
        EntitySpellcasterIllager.this.playSound(EntitySpellcasterIllager.this.func_193086_dk(), 1.0F, 1.0F);
      } 
    }
    
    protected abstract void func_190868_j();
    
    protected int func_190867_m() {
      return 20;
    }
    
    protected abstract int func_190869_f();
    
    protected abstract int func_190872_i();
    
    @Nullable
    protected abstract SoundEvent func_190871_k();
    
    protected abstract EntitySpellcasterIllager.SpellType func_193320_l();
  }
  
  public enum SpellType {
    NONE(0, 0.0D, 0.0D, 0.0D),
    SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
    FANGS(2, 0.4D, 0.3D, 0.35D),
    WOLOLO(3, 0.7D, 0.5D, 0.2D),
    DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
    BLINDNESS(5, 0.1D, 0.1D, 0.2D);
    
    private final int field_193345_g;
    
    private final double[] field_193346_h;
    
    SpellType(int p_i47561_3_, double p_i47561_4_, double p_i47561_6_, double p_i47561_8_) {
      this.field_193345_g = p_i47561_3_;
      this.field_193346_h = new double[] { p_i47561_4_, p_i47561_6_, p_i47561_8_ };
    }
    
    public static SpellType func_193337_a(int p_193337_0_) {
      byte b;
      int i;
      SpellType[] arrayOfSpellType;
      for (i = (arrayOfSpellType = values()).length, b = 0; b < i; ) {
        SpellType entityspellcasterillager$spelltype = arrayOfSpellType[b];
        if (p_193337_0_ == entityspellcasterillager$spelltype.field_193345_g)
          return entityspellcasterillager$spelltype; 
        b++;
      } 
      return NONE;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntitySpellcasterIllager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */