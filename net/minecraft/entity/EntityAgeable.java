package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {
  private static final DataParameter<Boolean> BABY = EntityDataManager.createKey(EntityAgeable.class, DataSerializers.BOOLEAN);
  
  protected int growingAge;
  
  protected int forcedAge;
  
  protected int forcedAgeTimer;
  
  private float ageWidth = -1.0F;
  
  private float ageHeight;
  
  public EntityAgeable(World worldIn) {
    super(worldIn);
  }
  
  @Nullable
  public abstract EntityAgeable createChild(EntityAgeable paramEntityAgeable);
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (itemstack.getItem() == Items.SPAWN_EGG) {
      if (!this.world.isRemote) {
        Class<? extends Entity> oclass = (Class<? extends Entity>)EntityList.field_191308_b.getObject(ItemMonsterPlacer.func_190908_h(itemstack));
        if (oclass != null && getClass() == oclass) {
          EntityAgeable entityageable = createChild(this);
          if (entityageable != null) {
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
            this.world.spawnEntityInWorld(entityageable);
            if (itemstack.hasDisplayName())
              entityageable.setCustomNameTag(itemstack.getDisplayName()); 
            if (!player.capabilities.isCreativeMode)
              itemstack.func_190918_g(1); 
          } 
        } 
      } 
      return true;
    } 
    return false;
  }
  
  protected boolean func_190669_a(ItemStack p_190669_1_, Class<? extends Entity> p_190669_2_) {
    if (p_190669_1_.getItem() != Items.SPAWN_EGG)
      return false; 
    Class<? extends Entity> oclass = (Class<? extends Entity>)EntityList.field_191308_b.getObject(ItemMonsterPlacer.func_190908_h(p_190669_1_));
    return (oclass != null && p_190669_2_ == oclass);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(BABY, Boolean.valueOf(false));
  }
  
  public int getGrowingAge() {
    if (this.world.isRemote)
      return ((Boolean)this.dataManager.get(BABY)).booleanValue() ? -1 : 1; 
    return this.growingAge;
  }
  
  public void ageUp(int p_175501_1_, boolean p_175501_2_) {
    int i = getGrowingAge();
    int j = i;
    i += p_175501_1_ * 20;
    if (i > 0) {
      i = 0;
      if (j < 0)
        onGrowingAdult(); 
    } 
    int k = i - j;
    setGrowingAge(i);
    if (p_175501_2_) {
      this.forcedAge += k;
      if (this.forcedAgeTimer == 0)
        this.forcedAgeTimer = 40; 
    } 
    if (getGrowingAge() == 0)
      setGrowingAge(this.forcedAge); 
  }
  
  public void addGrowth(int growth) {
    ageUp(growth, false);
  }
  
  public void setGrowingAge(int age) {
    this.dataManager.set(BABY, Boolean.valueOf((age < 0)));
    this.growingAge = age;
    setScaleForAge(isChild());
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("Age", getGrowingAge());
    compound.setInteger("ForcedAge", this.forcedAge);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setGrowingAge(compound.getInteger("Age"));
    this.forcedAge = compound.getInteger("ForcedAge");
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (BABY.equals(key))
      setScaleForAge(isChild()); 
    super.notifyDataManagerChange(key);
  }
  
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.world.isRemote) {
      if (this.forcedAgeTimer > 0) {
        if (this.forcedAgeTimer % 4 == 0)
          this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, 0.0D, 0.0D, 0.0D, new int[0]); 
        this.forcedAgeTimer--;
      } 
    } else {
      int i = getGrowingAge();
      if (i < 0) {
        i++;
        setGrowingAge(i);
        if (i == 0)
          onGrowingAdult(); 
      } else if (i > 0) {
        i--;
        setGrowingAge(i);
      } 
    } 
  }
  
  protected void onGrowingAdult() {}
  
  public boolean isChild() {
    return (getGrowingAge() < 0);
  }
  
  public void setScaleForAge(boolean child) {
    setScale(child ? 0.5F : 1.0F);
  }
  
  protected final void setSize(float width, float height) {
    boolean flag = (this.ageWidth > 0.0F);
    this.ageWidth = width;
    this.ageHeight = height;
    if (!flag)
      setScale(1.0F); 
  }
  
  protected final void setScale(float scale) {
    super.setSize(this.ageWidth * scale, this.ageHeight * scale);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\EntityAgeable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */