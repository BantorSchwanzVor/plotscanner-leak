package net.minecraft.entity.passive;

import java.util.Locale;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant {
  private static final Logger field_190674_bx = LogManager.getLogger();
  
  private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
  
  private int randomTickDivider;
  
  private boolean isMating;
  
  private boolean isPlaying;
  
  Village villageObj;
  
  @Nullable
  private EntityPlayer buyingPlayer;
  
  @Nullable
  private MerchantRecipeList buyingList;
  
  private int timeUntilReset;
  
  private boolean needsInitilization;
  
  private boolean isWillingToMate;
  
  private int wealth;
  
  private String lastBuyingPlayer;
  
  private int careerId;
  
  private int careerLevel;
  
  private boolean isLookingForHome;
  
  private boolean areAdditionalTasksSet;
  
  private final InventoryBasic villagerInventory;
  
  private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new ITradeList[][][][] { { { { new EmeraldForItems(Items.WHEAT, new PriceInfo(18, 22)), new EmeraldForItems(Items.POTATO, new PriceInfo(15, 19)), new EmeraldForItems(Items.CARROT, new PriceInfo(15, 19)), new ListItemForEmeralds(Items.BREAD, new PriceInfo(-4, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new PriceInfo(8, 13)), new ListItemForEmeralds(Items.PUMPKIN_PIE, new PriceInfo(-3, -2)) }, { new EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new PriceInfo(7, 12)), new ListItemForEmeralds(Items.APPLE, new PriceInfo(-7, -5)) }, { new ListItemForEmeralds(Items.COOKIE, new PriceInfo(-10, -6)), new ListItemForEmeralds(Items.CAKE, new PriceInfo(1, 1)) } }, { { new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ItemAndEmeraldToItem(Items.FISH, new PriceInfo(6, 6), Items.COOKED_FISH, new PriceInfo(6, 6)) }, { new ListEnchantedItemForEmeralds((Item)Items.FISHING_ROD, new PriceInfo(7, 8)) } }, { { new EmeraldForItems(Item.getItemFromBlock(Blocks.WOOL), new PriceInfo(16, 22)), new ListItemForEmeralds((Item)Items.SHEARS, new PriceInfo(3, 4)) }, { 
            new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL)), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 1), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 2), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 3), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 4), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 5), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 6), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 7), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 8), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 9), new PriceInfo(1, 2)), 
            new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 10), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 11), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 12), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 13), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 14), new PriceInfo(1, 2)), new ListItemForEmeralds(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, 15), new PriceInfo(1, 2)) } }, { { new EmeraldForItems(Items.STRING, new PriceInfo(15, 20)), new ListItemForEmeralds(Items.ARROW, new PriceInfo(-12, -8)) }, { new ListItemForEmeralds((Item)Items.BOW, new PriceInfo(2, 3)), new ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.GRAVEL), new PriceInfo(10, 10), Items.FLINT, new PriceInfo(6, 10)) } } }, { { { new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36)), new ListEnchantedBookForEmeralds() }, { new EmeraldForItems(Items.BOOK, new PriceInfo(8, 10)), new ListItemForEmeralds(Items.COMPASS, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.BOOKSHELF), new PriceInfo(3, 4)) }, { new EmeraldForItems(Items.WRITTEN_BOOK, new PriceInfo(2, 2)), new ListItemForEmeralds(Items.CLOCK, new PriceInfo(10, 12)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLASS), new PriceInfo(-5, -3)) }, { new ListEnchantedBookForEmeralds() }, { new ListEnchantedBookForEmeralds() }, { new ListItemForEmeralds(Items.NAME_TAG, new PriceInfo(20, 22)) } }, { { new EmeraldForItems(Items.PAPER, new PriceInfo(24, 36)) }, { new EmeraldForItems(Items.COMPASS, new PriceInfo(1, 1)) }, { new ListItemForEmeralds((Item)Items.MAP, new PriceInfo(7, 11)) }, { new TreasureMapForEmeralds(new PriceInfo(12, 20), "Monument", MapDecoration.Type.MONUMENT), new TreasureMapForEmeralds(new PriceInfo(16, 28), "Mansion", MapDecoration.Type.MANSION) } } }, { { { new EmeraldForItems(Items.ROTTEN_FLESH, new PriceInfo(36, 40)), new EmeraldForItems(Items.GOLD_INGOT, new PriceInfo(8, 10)) }, { new ListItemForEmeralds(Items.REDSTONE, new PriceInfo(-4, -1)), new ListItemForEmeralds(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), new PriceInfo(-2, -1)) }, { new ListItemForEmeralds(Items.ENDER_PEARL, new PriceInfo(4, 7)), new ListItemForEmeralds(Item.getItemFromBlock(Blocks.GLOWSTONE), new PriceInfo(-3, -1)) }, { new ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new PriceInfo(3, 11)) } } }, { { { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds((Item)Items.IRON_HELMET, new PriceInfo(4, 6)) }, { new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListItemForEmeralds((Item)Items.IRON_CHESTPLATE, new PriceInfo(10, 14)) }, { new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds((Item)Items.DIAMOND_CHESTPLATE, new PriceInfo(16, 19)) }, { new ListItemForEmeralds((Item)Items.CHAINMAIL_BOOTS, new PriceInfo(5, 7)), new ListItemForEmeralds((Item)Items.CHAINMAIL_LEGGINGS, new PriceInfo(9, 11)), new ListItemForEmeralds((Item)Items.CHAINMAIL_HELMET, new PriceInfo(5, 7)), new ListItemForEmeralds((Item)Items.CHAINMAIL_CHESTPLATE, new PriceInfo(11, 15)) } }, { { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.IRON_AXE, new PriceInfo(6, 8)) }, { new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_SWORD, new PriceInfo(9, 10)) }, { new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_SWORD, new PriceInfo(12, 15)), new ListEnchantedItemForEmeralds(Items.DIAMOND_AXE, new PriceInfo(9, 12)) } }, { { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListEnchantedItemForEmeralds(Items.IRON_SHOVEL, new PriceInfo(5, 7)) }, { new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(7, 9)), new ListEnchantedItemForEmeralds(Items.IRON_PICKAXE, new PriceInfo(9, 11)) }, { new EmeraldForItems(Items.DIAMOND, new PriceInfo(3, 4)), new ListEnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, new PriceInfo(12, 15)) } } }, { { { new EmeraldForItems(Items.PORKCHOP, new PriceInfo(14, 18)), new EmeraldForItems(Items.CHICKEN, new PriceInfo(14, 18)) }, { new EmeraldForItems(Items.COAL, new PriceInfo(16, 24)), new ListItemForEmeralds(Items.COOKED_PORKCHOP, new PriceInfo(-7, -5)), new ListItemForEmeralds(Items.COOKED_CHICKEN, new PriceInfo(-8, -6)) } }, { { new EmeraldForItems(Items.LEATHER, new PriceInfo(9, 12)), new ListItemForEmeralds((Item)Items.LEATHER_LEGGINGS, new PriceInfo(2, 4)) }, { new ListEnchantedItemForEmeralds((Item)Items.LEATHER_CHESTPLATE, new PriceInfo(7, 12)) }, { new ListItemForEmeralds(Items.SADDLE, new PriceInfo(8, 10)) } } }, { {} } };
  
  public EntityVillager(World worldIn) {
    this(worldIn, 0);
  }
  
  public EntityVillager(World worldIn, int professionId) {
    super(worldIn);
    this.villagerInventory = new InventoryBasic("Items", false, 8);
    setProfession(professionId);
    setSize(0.6F, 1.95F);
    ((PathNavigateGround)getNavigator()).setBreakDoors(true);
    setCanPickUpLoot(true);
  }
  
  protected void initEntityAI() {
    this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
    this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityVex.class, 8.0F, 0.6D, 0.6D));
    this.tasks.addTask(1, (EntityAIBase)new EntityAITradePlayer(this));
    this.tasks.addTask(1, (EntityAIBase)new EntityAILookAtTradePlayer(this));
    this.tasks.addTask(2, (EntityAIBase)new EntityAIMoveIndoors((EntityCreature)this));
    this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
    this.tasks.addTask(4, (EntityAIBase)new EntityAIOpenDoor((EntityLiving)this, true));
    this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction((EntityCreature)this, 0.6D));
    this.tasks.addTask(6, (EntityAIBase)new EntityAIVillagerMate(this));
    this.tasks.addTask(7, (EntityAIBase)new EntityAIFollowGolem(this));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityPlayer.class, 3.0F, 1.0F));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIVillagerInteract(this));
    this.tasks.addTask(9, (EntityAIBase)new EntityAIWanderAvoidWater((EntityCreature)this, 0.6D));
    this.tasks.addTask(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0F));
  }
  
  private void setAdditionalAItasks() {
    if (!this.areAdditionalTasksSet) {
      this.areAdditionalTasksSet = true;
      if (isChild()) {
        this.tasks.addTask(8, (EntityAIBase)new EntityAIPlay(this, 0.32D));
      } else if (getProfession() == 0) {
        this.tasks.addTask(6, (EntityAIBase)new EntityAIHarvestFarmland(this, 0.6D));
      } 
    } 
  }
  
  protected void onGrowingAdult() {
    if (getProfession() == 0)
      this.tasks.addTask(8, (EntityAIBase)new EntityAIHarvestFarmland(this, 0.6D)); 
    super.onGrowingAdult();
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
  }
  
  protected void updateAITasks() {
    if (--this.randomTickDivider <= 0) {
      BlockPos blockpos = new BlockPos((Entity)this);
      this.world.getVillageCollection().addToVillagerPositionList(blockpos);
      this.randomTickDivider = 70 + this.rand.nextInt(50);
      this.villageObj = this.world.getVillageCollection().getNearestVillage(blockpos, 32);
      if (this.villageObj == null) {
        detachHome();
      } else {
        BlockPos blockpos1 = this.villageObj.getCenter();
        setHomePosAndDistance(blockpos1, this.villageObj.getVillageRadius());
        if (this.isLookingForHome) {
          this.isLookingForHome = false;
          this.villageObj.setDefaultPlayerReputation(5);
        } 
      } 
    } 
    if (!isTrading() && this.timeUntilReset > 0) {
      this.timeUntilReset--;
      if (this.timeUntilReset <= 0) {
        if (this.needsInitilization) {
          for (MerchantRecipe merchantrecipe : this.buyingList) {
            if (merchantrecipe.isRecipeDisabled())
              merchantrecipe.increaseMaxTradeUses(this.rand.nextInt(6) + this.rand.nextInt(6) + 2); 
          } 
          populateBuyingList();
          this.needsInitilization = false;
          if (this.villageObj != null && this.lastBuyingPlayer != null) {
            this.world.setEntityState((Entity)this, (byte)14);
            this.villageObj.modifyPlayerReputation(this.lastBuyingPlayer, 1);
          } 
        } 
        addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));
      } 
    } 
    super.updateAITasks();
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    boolean flag = (itemstack.getItem() == Items.NAME_TAG);
    if (flag) {
      itemstack.interactWithEntity(player, (EntityLivingBase)this, hand);
      return true;
    } 
    if (!func_190669_a(itemstack, getClass()) && isEntityAlive() && !isTrading() && !isChild()) {
      if (this.buyingList == null)
        populateBuyingList(); 
      if (hand == EnumHand.MAIN_HAND)
        player.addStat(StatList.TALKED_TO_VILLAGER); 
      if (!this.world.isRemote && !this.buyingList.isEmpty()) {
        setCustomer(player);
        player.displayVillagerTradeGui(this);
      } else if (this.buyingList.isEmpty()) {
        return super.processInteract(player, hand);
      } 
      return true;
    } 
    return super.processInteract(player, hand);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(PROFESSION, Integer.valueOf(0));
  }
  
  public static void registerFixesVillager(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityVillager.class);
    fixer.registerWalker(FixTypes.ENTITY, (IDataWalker)new ItemStackDataLists(EntityVillager.class, new String[] { "Inventory" }));
    fixer.registerWalker(FixTypes.ENTITY, new IDataWalker() {
          public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
            if (EntityList.func_191306_a(EntityVillager.class).equals(new ResourceLocation(compound.getString("id"))) && compound.hasKey("Offers", 10)) {
              NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
              if (nbttagcompound.hasKey("Recipes", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("Recipes", 10);
                for (int i = 0; i < nbttaglist.tagCount(); i++) {
                  NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                  DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buy");
                  DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "buyB");
                  DataFixesManager.processItemStack(fixer, nbttagcompound1, versionIn, "sell");
                  nbttaglist.set(i, (NBTBase)nbttagcompound1);
                } 
              } 
            } 
            return compound;
          }
        });
  }
  
  public void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setInteger("Profession", getProfession());
    compound.setInteger("Riches", this.wealth);
    compound.setInteger("Career", this.careerId);
    compound.setInteger("CareerLevel", this.careerLevel);
    compound.setBoolean("Willing", this.isWillingToMate);
    if (this.buyingList != null)
      compound.setTag("Offers", (NBTBase)this.buyingList.getRecipiesAsTags()); 
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
      ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
      if (!itemstack.func_190926_b())
        nbttaglist.appendTag((NBTBase)itemstack.writeToNBT(new NBTTagCompound())); 
    } 
    compound.setTag("Inventory", (NBTBase)nbttaglist);
  }
  
  public void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    setProfession(compound.getInteger("Profession"));
    this.wealth = compound.getInteger("Riches");
    this.careerId = compound.getInteger("Career");
    this.careerLevel = compound.getInteger("CareerLevel");
    this.isWillingToMate = compound.getBoolean("Willing");
    if (compound.hasKey("Offers", 10)) {
      NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
      this.buyingList = new MerchantRecipeList(nbttagcompound);
    } 
    NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));
      if (!itemstack.func_190926_b())
        this.villagerInventory.addItem(itemstack); 
    } 
    setCanPickUpLoot(true);
    setAdditionalAItasks();
  }
  
  protected boolean canDespawn() {
    return false;
  }
  
  protected SoundEvent getAmbientSound() {
    return isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
  }
  
  protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
    return SoundEvents.ENTITY_VILLAGER_HURT;
  }
  
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_VILLAGER_DEATH;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.field_191184_at;
  }
  
  public void setProfession(int professionId) {
    this.dataManager.set(PROFESSION, Integer.valueOf(professionId));
  }
  
  public int getProfession() {
    return Math.max(((Integer)this.dataManager.get(PROFESSION)).intValue() % 6, 0);
  }
  
  public boolean isMating() {
    return this.isMating;
  }
  
  public void setMating(boolean mating) {
    this.isMating = mating;
  }
  
  public void setPlaying(boolean playing) {
    this.isPlaying = playing;
  }
  
  public boolean isPlaying() {
    return this.isPlaying;
  }
  
  public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
    super.setRevengeTarget(livingBase);
    if (this.villageObj != null && livingBase != null) {
      this.villageObj.addOrRenewAgressor(livingBase);
      if (livingBase instanceof EntityPlayer) {
        int i = -1;
        if (isChild())
          i = -3; 
        this.villageObj.modifyPlayerReputation(livingBase.getName(), i);
        if (isEntityAlive())
          this.world.setEntityState((Entity)this, (byte)13); 
      } 
    } 
  }
  
  public void onDeath(DamageSource cause) {
    if (this.villageObj != null) {
      Entity entity = cause.getEntity();
      if (entity != null) {
        if (entity instanceof EntityPlayer) {
          this.villageObj.modifyPlayerReputation(entity.getName(), -2);
        } else if (entity instanceof net.minecraft.entity.monster.IMob) {
          this.villageObj.endMatingSeason();
        } 
      } else {
        EntityPlayer entityplayer = this.world.getClosestPlayerToEntity((Entity)this, 16.0D);
        if (entityplayer != null)
          this.villageObj.endMatingSeason(); 
      } 
    } 
    super.onDeath(cause);
  }
  
  public void setCustomer(@Nullable EntityPlayer player) {
    this.buyingPlayer = player;
  }
  
  @Nullable
  public EntityPlayer getCustomer() {
    return this.buyingPlayer;
  }
  
  public boolean isTrading() {
    return (this.buyingPlayer != null);
  }
  
  public boolean getIsWillingToMate(boolean updateFirst) {
    if (!this.isWillingToMate && updateFirst && hasEnoughFoodToBreed()) {
      boolean flag = false;
      for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
        ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
        if (!itemstack.func_190926_b())
          if (itemstack.getItem() == Items.BREAD && itemstack.func_190916_E() >= 3) {
            flag = true;
            this.villagerInventory.decrStackSize(i, 3);
          } else if ((itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT) && itemstack.func_190916_E() >= 12) {
            flag = true;
            this.villagerInventory.decrStackSize(i, 12);
          }  
        if (flag) {
          this.world.setEntityState((Entity)this, (byte)18);
          this.isWillingToMate = true;
          break;
        } 
      } 
    } 
    return this.isWillingToMate;
  }
  
  public void setIsWillingToMate(boolean isWillingToMate) {
    this.isWillingToMate = isWillingToMate;
  }
  
  public void useRecipe(MerchantRecipe recipe) {
    recipe.incrementToolUses();
    this.livingSoundTime = -getTalkInterval();
    playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
    int i = 3 + this.rand.nextInt(4);
    if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
      this.timeUntilReset = 40;
      this.needsInitilization = true;
      this.isWillingToMate = true;
      if (this.buyingPlayer != null) {
        this.lastBuyingPlayer = this.buyingPlayer.getName();
      } else {
        this.lastBuyingPlayer = null;
      } 
      i += 5;
    } 
    if (recipe.getItemToBuy().getItem() == Items.EMERALD)
      this.wealth += recipe.getItemToBuy().func_190916_E(); 
    if (recipe.getRewardsExp())
      this.world.spawnEntityInWorld((Entity)new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i)); 
    if (this.buyingPlayer instanceof EntityPlayerMP)
      CriteriaTriggers.field_192138_r.func_192234_a((EntityPlayerMP)this.buyingPlayer, this, recipe.getItemToSell()); 
  }
  
  public void verifySellingItem(ItemStack stack) {
    if (!this.world.isRemote && this.livingSoundTime > -getTalkInterval() + 20) {
      this.livingSoundTime = -getTalkInterval();
      playSound(stack.func_190926_b() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
    } 
  }
  
  @Nullable
  public MerchantRecipeList getRecipes(EntityPlayer player) {
    if (this.buyingList == null)
      populateBuyingList(); 
    return this.buyingList;
  }
  
  private void populateBuyingList() {
    ITradeList[][][] aentityvillager$itradelist = DEFAULT_TRADE_LIST_MAP[getProfession()];
    if (this.careerId != 0 && this.careerLevel != 0) {
      this.careerLevel++;
    } else {
      this.careerId = this.rand.nextInt(aentityvillager$itradelist.length) + 1;
      this.careerLevel = 1;
    } 
    if (this.buyingList == null)
      this.buyingList = new MerchantRecipeList(); 
    int i = this.careerId - 1;
    int j = this.careerLevel - 1;
    if (i >= 0 && i < aentityvillager$itradelist.length) {
      ITradeList[][] aentityvillager$itradelist1 = aentityvillager$itradelist[i];
      if (j >= 0 && j < aentityvillager$itradelist1.length) {
        ITradeList[] aentityvillager$itradelist2 = aentityvillager$itradelist1[j];
        byte b;
        int k;
        ITradeList[] arrayOfITradeList1;
        for (k = (arrayOfITradeList1 = aentityvillager$itradelist2).length, b = 0; b < k; ) {
          ITradeList entityvillager$itradelist = arrayOfITradeList1[b];
          entityvillager$itradelist.func_190888_a(this, this.buyingList, this.rand);
          b++;
        } 
      } 
    } 
  }
  
  public void setRecipes(@Nullable MerchantRecipeList recipeList) {}
  
  public World func_190670_t_() {
    return this.world;
  }
  
  public BlockPos func_190671_u_() {
    return new BlockPos((Entity)this);
  }
  
  public ITextComponent getDisplayName() {
    Team team = getTeam();
    String s = getCustomNameTag();
    if (s != null && !s.isEmpty()) {
      TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
      textcomponentstring.getStyle().setHoverEvent(getHoverEvent());
      textcomponentstring.getStyle().setInsertion(getCachedUniqueIdString());
      return (ITextComponent)textcomponentstring;
    } 
    if (this.buyingList == null)
      populateBuyingList(); 
    String s1 = null;
    switch (getProfession()) {
      case 0:
        if (this.careerId == 1) {
          s1 = "farmer";
          break;
        } 
        if (this.careerId == 2) {
          s1 = "fisherman";
          break;
        } 
        if (this.careerId == 3) {
          s1 = "shepherd";
          break;
        } 
        if (this.careerId == 4)
          s1 = "fletcher"; 
        break;
      case 1:
        if (this.careerId == 1) {
          s1 = "librarian";
          break;
        } 
        if (this.careerId == 2)
          s1 = "cartographer"; 
        break;
      case 2:
        s1 = "cleric";
        break;
      case 3:
        if (this.careerId == 1) {
          s1 = "armor";
          break;
        } 
        if (this.careerId == 2) {
          s1 = "weapon";
          break;
        } 
        if (this.careerId == 3)
          s1 = "tool"; 
        break;
      case 4:
        if (this.careerId == 1) {
          s1 = "butcher";
          break;
        } 
        if (this.careerId == 2)
          s1 = "leather"; 
        break;
      case 5:
        s1 = "nitwit";
        break;
    } 
    if (s1 != null) {
      TextComponentTranslation textComponentTranslation = new TextComponentTranslation("entity.Villager." + s1, new Object[0]);
      textComponentTranslation.getStyle().setHoverEvent(getHoverEvent());
      textComponentTranslation.getStyle().setInsertion(getCachedUniqueIdString());
      if (team != null)
        textComponentTranslation.getStyle().setColor(team.getChatFormat()); 
      return (ITextComponent)textComponentTranslation;
    } 
    return super.getDisplayName();
  }
  
  public float getEyeHeight() {
    return isChild() ? 0.81F : 1.62F;
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 12) {
      spawnParticles(EnumParticleTypes.HEART);
    } else if (id == 13) {
      spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
    } else if (id == 14) {
      spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  private void spawnParticles(EnumParticleTypes particleType) {
    for (int i = 0; i < 5; i++) {
      double d0 = this.rand.nextGaussian() * 0.02D;
      double d1 = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      this.world.spawnParticle(particleType, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 1.0D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
    } 
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    return func_190672_a(difficulty, livingdata, true);
  }
  
  public IEntityLivingData func_190672_a(DifficultyInstance p_190672_1_, @Nullable IEntityLivingData p_190672_2_, boolean p_190672_3_) {
    p_190672_2_ = super.onInitialSpawn(p_190672_1_, p_190672_2_);
    if (p_190672_3_)
      setProfession(this.world.rand.nextInt(6)); 
    setAdditionalAItasks();
    populateBuyingList();
    return p_190672_2_;
  }
  
  public void setLookingForHome() {
    this.isLookingForHome = true;
  }
  
  public EntityVillager createChild(EntityAgeable ageable) {
    EntityVillager entityvillager = new EntityVillager(this.world);
    entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos((Entity)entityvillager)), (IEntityLivingData)null);
    return entityvillager;
  }
  
  public boolean canBeLeashedTo(EntityPlayer player) {
    return false;
  }
  
  public void onStruckByLightning(EntityLightningBolt lightningBolt) {
    if (!this.world.isRemote && !this.isDead) {
      EntityWitch entitywitch = new EntityWitch(this.world);
      entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      entitywitch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos((Entity)entitywitch)), null);
      entitywitch.setNoAI(isAIDisabled());
      if (hasCustomName()) {
        entitywitch.setCustomNameTag(getCustomNameTag());
        entitywitch.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
      } 
      this.world.spawnEntityInWorld((Entity)entitywitch);
      setDead();
    } 
  }
  
  public InventoryBasic getVillagerInventory() {
    return this.villagerInventory;
  }
  
  protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
    ItemStack itemstack = itemEntity.getEntityItem();
    Item item = itemstack.getItem();
    if (canVillagerPickupItem(item)) {
      ItemStack itemstack1 = this.villagerInventory.addItem(itemstack);
      if (itemstack1.func_190926_b()) {
        itemEntity.setDead();
      } else {
        itemstack.func_190920_e(itemstack1.func_190916_E());
      } 
    } 
  }
  
  private boolean canVillagerPickupItem(Item itemIn) {
    return !(itemIn != Items.BREAD && itemIn != Items.POTATO && itemIn != Items.CARROT && itemIn != Items.WHEAT && itemIn != Items.WHEAT_SEEDS && itemIn != Items.BEETROOT && itemIn != Items.BEETROOT_SEEDS);
  }
  
  public boolean hasEnoughFoodToBreed() {
    return hasEnoughItems(1);
  }
  
  public boolean canAbondonItems() {
    return hasEnoughItems(2);
  }
  
  public boolean wantsMoreFood() {
    boolean flag = (getProfession() == 0);
    if (flag)
      return !hasEnoughItems(5); 
    return !hasEnoughItems(1);
  }
  
  private boolean hasEnoughItems(int multiplier) {
    boolean flag = (getProfession() == 0);
    for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
      ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
      if (!itemstack.func_190926_b()) {
        if ((itemstack.getItem() == Items.BREAD && itemstack.func_190916_E() >= 3 * multiplier) || (itemstack.getItem() == Items.POTATO && itemstack.func_190916_E() >= 12 * multiplier) || (itemstack.getItem() == Items.CARROT && itemstack.func_190916_E() >= 12 * multiplier) || (itemstack.getItem() == Items.BEETROOT && itemstack.func_190916_E() >= 12 * multiplier))
          return true; 
        if (flag && itemstack.getItem() == Items.WHEAT && itemstack.func_190916_E() >= 9 * multiplier)
          return true; 
      } 
    } 
    return false;
  }
  
  public boolean isFarmItemInInventory() {
    for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++) {
      ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
      if (!itemstack.func_190926_b() && (itemstack.getItem() == Items.WHEAT_SEEDS || itemstack.getItem() == Items.POTATO || itemstack.getItem() == Items.CARROT || itemstack.getItem() == Items.BEETROOT_SEEDS))
        return true; 
    } 
    return false;
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    if (super.replaceItemInInventory(inventorySlot, itemStackIn))
      return true; 
    int i = inventorySlot - 300;
    if (i >= 0 && i < this.villagerInventory.getSizeInventory()) {
      this.villagerInventory.setInventorySlotContents(i, itemStackIn);
      return true;
    } 
    return false;
  }
  
  static class EmeraldForItems implements ITradeList {
    public Item buyingItem;
    
    public EntityVillager.PriceInfo price;
    
    public EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
      this.buyingItem = itemIn;
      this.price = priceIn;
    }
    
    public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
      int i = 1;
      if (this.price != null)
        i = this.price.getPrice(p_190888_3_); 
      p_190888_2_.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), Items.EMERALD));
    }
  }
  
  static interface ITradeList {
    void func_190888_a(IMerchant param1IMerchant, MerchantRecipeList param1MerchantRecipeList, Random param1Random);
  }
  
  static class ItemAndEmeraldToItem implements ITradeList {
    public ItemStack buyingItemStack;
    
    public EntityVillager.PriceInfo buyingPriceInfo;
    
    public ItemStack sellingItemstack;
    
    public EntityVillager.PriceInfo sellingPriceInfo;
    
    public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_) {
      this.buyingItemStack = new ItemStack(p_i45813_1_);
      this.buyingPriceInfo = p_i45813_2_;
      this.sellingItemstack = new ItemStack(p_i45813_3_);
      this.sellingPriceInfo = p_i45813_4_;
    }
    
    public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
      int i = this.buyingPriceInfo.getPrice(p_190888_3_);
      int j = this.sellingPriceInfo.getPrice(p_190888_3_);
      p_190888_2_.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
    }
  }
  
  static class ListEnchantedBookForEmeralds implements ITradeList {
    public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
      Enchantment enchantment = (Enchantment)Enchantment.REGISTRY.getRandomObject(p_190888_3_);
      int i = MathHelper.getInt(p_190888_3_, enchantment.getMinLevel(), enchantment.getMaxLevel());
      ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
      int j = 2 + p_190888_3_.nextInt(5 + i * 10) + 3 * i;
      if (enchantment.isTreasureEnchantment())
        j *= 2; 
      if (j > 64)
        j = 64; 
      p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
    }
  }
  
  static class ListEnchantedItemForEmeralds implements ITradeList {
    public ItemStack enchantedItemStack;
    
    public EntityVillager.PriceInfo priceInfo;
    
    public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_) {
      this.enchantedItemStack = new ItemStack(p_i45814_1_);
      this.priceInfo = p_i45814_2_;
    }
    
    public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
      int i = 1;
      if (this.priceInfo != null)
        i = this.priceInfo.getPrice(p_190888_3_); 
      ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
      ItemStack itemstack1 = EnchantmentHelper.addRandomEnchantment(p_190888_3_, new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata()), 5 + p_190888_3_.nextInt(15), false);
      p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
    }
  }
  
  static class ListItemForEmeralds implements ITradeList {
    public ItemStack itemToBuy;
    
    public EntityVillager.PriceInfo priceInfo;
    
    public ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo) {
      this.itemToBuy = new ItemStack(par1Item);
      this.priceInfo = priceInfo;
    }
    
    public ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
      this.itemToBuy = stack;
      this.priceInfo = priceInfo;
    }
    
    public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
      ItemStack itemstack, itemstack1;
      int i = 1;
      if (this.priceInfo != null)
        i = this.priceInfo.getPrice(p_190888_3_); 
      if (i < 0) {
        itemstack = new ItemStack(Items.EMERALD);
        itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
      } else {
        itemstack = new ItemStack(Items.EMERALD, i, 0);
        itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
      } 
      p_190888_2_.add(new MerchantRecipe(itemstack, itemstack1));
    }
  }
  
  static class PriceInfo extends Tuple<Integer, Integer> {
    public PriceInfo(int p_i45810_1_, int p_i45810_2_) {
      super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
      if (p_i45810_2_ < p_i45810_1_)
        EntityVillager.field_190674_bx.warn("PriceRange({}, {}) invalid, {} smaller than {}", Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_2_), Integer.valueOf(p_i45810_1_)); 
    }
    
    public int getPrice(Random rand) {
      return (((Integer)getFirst()).intValue() >= ((Integer)getSecond()).intValue()) ? ((Integer)getFirst()).intValue() : (((Integer)getFirst()).intValue() + rand.nextInt(((Integer)getSecond()).intValue() - ((Integer)getFirst()).intValue() + 1));
    }
  }
  
  static class TreasureMapForEmeralds implements ITradeList {
    public EntityVillager.PriceInfo field_190889_a;
    
    public String field_190890_b;
    
    public MapDecoration.Type field_190891_c;
    
    public TreasureMapForEmeralds(EntityVillager.PriceInfo p_i47340_1_, String p_i47340_2_, MapDecoration.Type p_i47340_3_) {
      this.field_190889_a = p_i47340_1_;
      this.field_190890_b = p_i47340_2_;
      this.field_190891_c = p_i47340_3_;
    }
    
    public void func_190888_a(IMerchant p_190888_1_, MerchantRecipeList p_190888_2_, Random p_190888_3_) {
      int i = this.field_190889_a.getPrice(p_190888_3_);
      World world = p_190888_1_.func_190670_t_();
      BlockPos blockpos = world.func_190528_a(this.field_190890_b, p_190888_1_.func_190671_u_(), true);
      if (blockpos != null) {
        ItemStack itemstack = ItemMap.func_190906_a(world, blockpos.getX(), blockpos.getZ(), (byte)2, true, true);
        ItemMap.func_190905_a(world, itemstack);
        MapData.func_191094_a(itemstack, blockpos, "+", this.field_190891_c);
        itemstack.func_190924_f("filled_map." + this.field_190890_b.toLowerCase(Locale.ROOT));
        p_190888_2_.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */