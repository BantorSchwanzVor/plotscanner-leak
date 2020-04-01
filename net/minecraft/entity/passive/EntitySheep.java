package net.minecraft.entity.passive;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySheep extends EntityAnimal {
  private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
  
  private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container() {
        public boolean canInteractWith(EntityPlayer playerIn) {
          return false;
        }
      },  2, 1);
  
  private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
  
  private int sheepTimer;
  
  private EntityAIEatGrass entityAIEatGrass;
  
  private static float[] func_192020_c(EnumDyeColor p_192020_0_) {
    float[] afloat = p_192020_0_.func_193349_f();
    float f = 0.75F;
    return new float[] { afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F };
  }
  
  public static float[] getDyeRgb(EnumDyeColor dyeColor) {
    return DYE_TO_RGB.get(dyeColor);
  }
  
  public EntitySheep(World worldIn) {
    super(worldIn);
    setSize(0.9F, 1.3F);
    this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
    this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
  }
  
  protected void initEntityAI() {
    this.entityAIEatGrass = new EntityAIEatGrass((EntityLiving)this);
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
    this.tasks.addTask(3, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.1D, Items.WHEAT, false));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
    this.tasks.addTask(5, (EntityAIBase)this.entityAIEatGrass);
    this.tasks.addTask(6, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 1.0D));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
  }
  
  protected void updateAITasks() {
    this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
    super.updateAITasks();
  }
  
  public void onLivingUpdate() {
    if (this.world.isRemote)
      this.sheepTimer = Math.max(0, this.sheepTimer - 1); 
    super.onLivingUpdate();
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(DYE_COLOR, Byte.valueOf((byte)0));
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    if (getSheared())
      return LootTableList.ENTITIES_SHEEP; 
    switch (getFleeceColor()) {
      default:
        return LootTableList.ENTITIES_SHEEP_WHITE;
      case ORANGE:
        return LootTableList.ENTITIES_SHEEP_ORANGE;
      case MAGENTA:
        return LootTableList.ENTITIES_SHEEP_MAGENTA;
      case LIGHT_BLUE:
        return LootTableList.ENTITIES_SHEEP_LIGHT_BLUE;
      case YELLOW:
        return LootTableList.ENTITIES_SHEEP_YELLOW;
      case LIME:
        return LootTableList.ENTITIES_SHEEP_LIME;
      case PINK:
        return LootTableList.ENTITIES_SHEEP_PINK;
      case GRAY:
        return LootTableList.ENTITIES_SHEEP_GRAY;
      case SILVER:
        return LootTableList.ENTITIES_SHEEP_SILVER;
      case CYAN:
        return LootTableList.ENTITIES_SHEEP_CYAN;
      case PURPLE:
        return LootTableList.ENTITIES_SHEEP_PURPLE;
      case BLUE:
        return LootTableList.ENTITIES_SHEEP_BLUE;
      case BROWN:
        return LootTableList.ENTITIES_SHEEP_BROWN;
      case GREEN:
        return LootTableList.ENTITIES_SHEEP_GREEN;
      case RED:
        return LootTableList.ENTITIES_SHEEP_RED;
      case null:
        break;
    } 
    return LootTableList.ENTITIES_SHEEP_BLACK;
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 10) {
      this.sheepTimer = 40;
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public float getHeadRotationPointY(float p_70894_1_) {
    if (this.sheepTimer <= 0)
      return 0.0F; 
    if (this.sheepTimer >= 4 && this.sheepTimer <= 36)
      return 1.0F; 
    return (this.sheepTimer < 4) ? ((this.sheepTimer - p_70894_1_) / 4.0F) : (-((this.sheepTimer - 40) - p_70894_1_) / 4.0F);
  }
  
  public float getHeadRotationAngleX(float p_70890_1_) {
    if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
      float f = ((this.sheepTimer - 4) - p_70890_1_) / 32.0F;
      return 0.62831855F + 0.2199115F * MathHelper.sin(f * 28.7F);
    } 
    return (this.sheepTimer > 0) ? 0.62831855F : (this.rotationPitch * 0.017453292F);
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (itemstack.getItem() == Items.SHEARS && !getSheared() && !isChild()) {
      if (!this.world.isRemote) {
        setSheared(true);
        int i = 1 + this.rand.nextInt(3);
        for (int j = 0; j < i; j++) {
          EntityItem entityitem = entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, getFleeceColor().getMetadata()), 1.0F);
          entityitem.motionY += (this.rand.nextFloat() * 0.05F);
          entityitem.motionX += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
          entityitem.motionZ += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
        } 
      } 
      itemstack.damageItem(1, (EntityLivingBase)player);
      playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
    } 
    return super.processInteract(player, hand);
  }
  
  public static void registerFixesSheep(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntitySheep.class);
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setBoolean("Sheared", getSheared());
    compound.setByte("Color", (byte)getFleeceColor().getMetadata());
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setSheared(compound.getBoolean("Sheared"));
    setFleeceColor(EnumDyeColor.byMetadata(compound.getByte("Color")));
  }
  
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_SHEEP_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_SHEEP_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_SHEEP_DEATH;
  }
  
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
  }
  
  public EnumDyeColor getFleeceColor() {
    return EnumDyeColor.byMetadata(((Byte)this.dataManager.get(DYE_COLOR)).byteValue() & 0xF);
  }
  
  public void setFleeceColor(EnumDyeColor color) {
    byte b0 = ((Byte)this.dataManager.get(DYE_COLOR)).byteValue();
    this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 0xF0 | color.getMetadata() & 0xF)));
  }
  
  public boolean getSheared() {
    return ((((Byte)this.dataManager.get(DYE_COLOR)).byteValue() & 0x10) != 0);
  }
  
  public void setSheared(boolean sheared) {
    byte b0 = ((Byte)this.dataManager.get(DYE_COLOR)).byteValue();
    if (sheared) {
      this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 | 0x10)));
    } else {
      this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
    } 
  }
  
  public static EnumDyeColor getRandomSheepColor(Random random) {
    int i = random.nextInt(100);
    if (i < 5)
      return EnumDyeColor.BLACK; 
    if (i < 10)
      return EnumDyeColor.GRAY; 
    if (i < 15)
      return EnumDyeColor.SILVER; 
    if (i < 18)
      return EnumDyeColor.BROWN; 
    return (random.nextInt(500) == 0) ? EnumDyeColor.PINK : EnumDyeColor.WHITE;
  }
  
  public EntitySheep createChild(EntityAgeable ageable) {
    EntitySheep entitysheep = (EntitySheep)ageable;
    EntitySheep entitysheep1 = new EntitySheep(this.world);
    entitysheep1.setFleeceColor(getDyeColorMixFromParents(this, entitysheep));
    return entitysheep1;
  }
  
  public void eatGrassBonus() {
    setSheared(false);
    if (isChild())
      addGrowth(60); 
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    livingdata = super.onInitialSpawn(difficulty, livingdata);
    setFleeceColor(getRandomSheepColor(this.world.rand));
    return livingdata;
  }
  
  private EnumDyeColor getDyeColorMixFromParents(EntityAnimal father, EntityAnimal mother) {
    int k, i = ((EntitySheep)father).getFleeceColor().getDyeDamage();
    int j = ((EntitySheep)mother).getFleeceColor().getDyeDamage();
    this.inventoryCrafting.getStackInSlot(0).setItemDamage(i);
    this.inventoryCrafting.getStackInSlot(1).setItemDamage(j);
    ItemStack itemstack = CraftingManager.findMatchingRecipe(this.inventoryCrafting, ((EntitySheep)father).world);
    if (itemstack.getItem() == Items.DYE) {
      k = itemstack.getMetadata();
    } else {
      k = this.world.rand.nextBoolean() ? i : j;
    } 
    return EnumDyeColor.byDyeDamage(k);
  }
  
  public float getEyeHeight() {
    return 0.95F * this.height;
  }
  
  static {
    byte b;
    int i;
    EnumDyeColor[] arrayOfEnumDyeColor;
    for (i = (arrayOfEnumDyeColor = EnumDyeColor.values()).length, b = 0; b < i; ) {
      EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[b];
      DYE_TO_RGB.put(enumdyecolor, func_192020_c(enumdyecolor));
      b++;
    } 
    DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 0.9019608F, 0.9019608F, 0.9019608F });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntitySheep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */