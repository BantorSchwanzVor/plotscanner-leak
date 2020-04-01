package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.seltak.anubis.utils.Timer;

public final class ItemStack {
  public static final ItemStack field_190927_a = new ItemStack(null);
  
  public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
  
  private int stackSize;
  
  private int animationsToGo;
  
  private final Item item;
  
  private NBTTagCompound stackTagCompound;
  
  private boolean field_190928_g;
  
  private int itemDamage;
  
  public Timer timer = new Timer();
  
  private EntityItemFrame itemFrame;
  
  private Block canDestroyCacheBlock;
  
  private boolean canDestroyCacheResult;
  
  private Block canPlaceOnCacheBlock;
  
  private boolean canPlaceOnCacheResult;
  
  public ItemStack(Block blockIn) {
    this(blockIn, 1);
  }
  
  public ItemStack(Block blockIn, int amount) {
    this(blockIn, amount, 0);
  }
  
  public ItemStack(Block blockIn, int amount, int meta) {
    this(Item.getItemFromBlock(blockIn), amount, meta);
  }
  
  public ItemStack(Item itemIn) {
    this(itemIn, 1);
  }
  
  public ItemStack(Item itemIn, int amount) {
    this(itemIn, amount, 0);
  }
  
  public ItemStack(Item itemIn, int amount, int meta) {
    this.item = itemIn;
    this.itemDamage = meta;
    this.stackSize = amount;
    if (this.itemDamage < 0)
      this.itemDamage = 0; 
    func_190923_F();
  }
  
  private void func_190923_F() {
    this.field_190928_g = func_190926_b();
  }
  
  public ItemStack(NBTTagCompound p_i47263_1_) {
    this.item = Item.getByNameOrId(p_i47263_1_.getString("id"));
    this.stackSize = p_i47263_1_.getByte("Count");
    this.itemDamage = Math.max(0, p_i47263_1_.getShort("Damage"));
    if (p_i47263_1_.hasKey("tag", 10)) {
      this.stackTagCompound = p_i47263_1_.getCompoundTag("tag");
      if (this.item != null)
        this.item.updateItemStackNBT(p_i47263_1_); 
    } 
    func_190923_F();
  }
  
  public boolean func_190926_b() {
    if (this == field_190927_a)
      return true; 
    if (this.item != null && this.item != Item.getItemFromBlock(Blocks.AIR)) {
      if (this.stackSize <= 0)
        return true; 
      return !(this.itemDamage >= -32768 && this.itemDamage <= 65535);
    } 
    return true;
  }
  
  public static void registerFixes(DataFixer fixer) {
    fixer.registerWalker(FixTypes.ITEM_INSTANCE, (IDataWalker)new BlockEntityTag());
    fixer.registerWalker(FixTypes.ITEM_INSTANCE, (IDataWalker)new EntityTag());
  }
  
  public ItemStack splitStack(int amount) {
    int i = Math.min(amount, this.stackSize);
    ItemStack itemstack = copy();
    itemstack.func_190920_e(i);
    func_190918_g(i);
    return itemstack;
  }
  
  public Item getItem() {
    return this.field_190928_g ? Item.getItemFromBlock(Blocks.AIR) : this.item;
  }
  
  public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    EnumActionResult enumactionresult = getItem().onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
    if (enumactionresult == EnumActionResult.SUCCESS)
      playerIn.addStat(StatList.getObjectUseStats(this.item)); 
    return enumactionresult;
  }
  
  public float getStrVsBlock(IBlockState blockIn) {
    return getItem().getStrVsBlock(this, blockIn);
  }
  
  public ActionResult<ItemStack> useItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    return getItem().onItemRightClick(worldIn, playerIn, hand);
  }
  
  public ItemStack onItemUseFinish(World worldIn, EntityLivingBase entityLiving) {
    return getItem().onItemUseFinish(this, worldIn, entityLiving);
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(this.item);
    nbt.setString("id", (resourcelocation == null) ? "minecraft:air" : resourcelocation.toString());
    nbt.setByte("Count", (byte)this.stackSize);
    nbt.setShort("Damage", (short)this.itemDamage);
    if (this.stackTagCompound != null)
      nbt.setTag("tag", (NBTBase)this.stackTagCompound); 
    return nbt;
  }
  
  public int getMaxStackSize() {
    return getItem().getItemStackLimit();
  }
  
  public boolean isStackable() {
    return (getMaxStackSize() > 1 && (!isItemStackDamageable() || !isItemDamaged()));
  }
  
  public boolean isItemStackDamageable() {
    if (this.field_190928_g)
      return false; 
    if (this.item.getMaxDamage() <= 0)
      return false; 
    return !(hasTagCompound() && getTagCompound().getBoolean("Unbreakable"));
  }
  
  public boolean getHasSubtypes() {
    return getItem().getHasSubtypes();
  }
  
  public boolean isItemDamaged() {
    return (isItemStackDamageable() && this.itemDamage > 0);
  }
  
  public int getItemDamage() {
    return this.itemDamage;
  }
  
  public int getMetadata() {
    return this.itemDamage;
  }
  
  public void setItemDamage(int meta) {
    this.itemDamage = meta;
    if (this.itemDamage < 0)
      this.itemDamage = 0; 
  }
  
  public int getMaxDamage() {
    return getItem().getMaxDamage();
  }
  
  public boolean attemptDamageItem(int amount, Random rand, @Nullable EntityPlayerMP p_96631_3_) {
    if (!isItemStackDamageable())
      return false; 
    if (amount > 0) {
      int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
      int j = 0;
      for (int k = 0; i > 0 && k < amount; k++) {
        if (EnchantmentDurability.negateDamage(this, i, rand))
          j++; 
      } 
      amount -= j;
      if (amount <= 0)
        return false; 
    } 
    if (p_96631_3_ != null && amount != 0)
      CriteriaTriggers.field_193132_s.func_193158_a(p_96631_3_, this, this.itemDamage + amount); 
    this.itemDamage += amount;
    return (this.itemDamage > getMaxDamage());
  }
  
  public void damageItem(int amount, EntityLivingBase entityIn) {
    if (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).capabilities.isCreativeMode)
      if (isItemStackDamageable())
        if (attemptDamageItem(amount, entityIn.getRNG(), (entityIn instanceof EntityPlayerMP) ? (EntityPlayerMP)entityIn : null)) {
          entityIn.renderBrokenItemStack(this);
          func_190918_g(1);
          if (entityIn instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entityIn;
            entityplayer.addStat(StatList.getObjectBreakStats(this.item));
          } 
          this.itemDamage = 0;
        }   
  }
  
  public void hitEntity(EntityLivingBase entityIn, EntityPlayer playerIn) {
    boolean flag = this.item.hitEntity(this, entityIn, (EntityLivingBase)playerIn);
    if (flag)
      playerIn.addStat(StatList.getObjectUseStats(this.item)); 
  }
  
  public void onBlockDestroyed(World worldIn, IBlockState blockIn, BlockPos pos, EntityPlayer playerIn) {
    boolean flag = getItem().onBlockDestroyed(this, worldIn, blockIn, pos, (EntityLivingBase)playerIn);
    if (flag)
      playerIn.addStat(StatList.getObjectUseStats(this.item)); 
  }
  
  public boolean canHarvestBlock(IBlockState blockIn) {
    return getItem().canHarvestBlock(blockIn);
  }
  
  public boolean interactWithEntity(EntityPlayer playerIn, EntityLivingBase entityIn, EnumHand hand) {
    return getItem().itemInteractionForEntity(this, playerIn, entityIn, hand);
  }
  
  public ItemStack copy() {
    ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage);
    itemstack.func_190915_d(func_190921_D());
    if (this.stackTagCompound != null)
      itemstack.stackTagCompound = this.stackTagCompound.copy(); 
    return itemstack;
  }
  
  public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
    if (stackA.func_190926_b() && stackB.func_190926_b())
      return true; 
    if (!stackA.func_190926_b() && !stackB.func_190926_b()) {
      if (stackA.stackTagCompound == null && stackB.stackTagCompound != null)
        return false; 
      return !(stackA.stackTagCompound != null && !stackA.stackTagCompound.equals(stackB.stackTagCompound));
    } 
    return false;
  }
  
  public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
    if (stackA.func_190926_b() && stackB.func_190926_b())
      return true; 
    return (!stackA.func_190926_b() && !stackB.func_190926_b()) ? stackA.isItemStackEqual(stackB) : false;
  }
  
  private boolean isItemStackEqual(ItemStack other) {
    if (this.stackSize != other.stackSize)
      return false; 
    if (getItem() != other.getItem())
      return false; 
    if (this.itemDamage != other.itemDamage)
      return false; 
    if (this.stackTagCompound == null && other.stackTagCompound != null)
      return false; 
    return !(this.stackTagCompound != null && !this.stackTagCompound.equals(other.stackTagCompound));
  }
  
  public static boolean areItemsEqual(ItemStack stackA, ItemStack stackB) {
    if (stackA == stackB)
      return true; 
    return (!stackA.func_190926_b() && !stackB.func_190926_b()) ? stackA.isItemEqual(stackB) : false;
  }
  
  public static boolean areItemsEqualIgnoreDurability(ItemStack stackA, ItemStack stackB) {
    if (stackA == stackB)
      return true; 
    return (!stackA.func_190926_b() && !stackB.func_190926_b()) ? stackA.isItemEqualIgnoreDurability(stackB) : false;
  }
  
  public boolean isItemEqual(ItemStack other) {
    return (!other.func_190926_b() && this.item == other.item && this.itemDamage == other.itemDamage);
  }
  
  public boolean isItemEqualIgnoreDurability(ItemStack stack) {
    if (!isItemStackDamageable())
      return isItemEqual(stack); 
    return (!stack.func_190926_b() && this.item == stack.item);
  }
  
  public String getUnlocalizedName() {
    return getItem().getUnlocalizedName(this);
  }
  
  public String toString() {
    return String.valueOf(this.stackSize) + "x" + getItem().getUnlocalizedName() + "@" + this.itemDamage;
  }
  
  public void updateAnimation(World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
    if (this.animationsToGo > 0)
      this.animationsToGo--; 
    if (this.item != null)
      this.item.onUpdate(this, worldIn, entityIn, inventorySlot, isCurrentItem); 
  }
  
  public void onCrafting(World worldIn, EntityPlayer playerIn, int amount) {
    playerIn.addStat(StatList.getCraftStats(this.item), amount);
    getItem().onCreated(this, worldIn, playerIn);
  }
  
  public int getMaxItemUseDuration() {
    return getItem().getMaxItemUseDuration(this);
  }
  
  public EnumAction getItemUseAction() {
    return getItem().getItemUseAction(this);
  }
  
  public void onPlayerStoppedUsing(World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    getItem().onPlayerStoppedUsing(this, worldIn, entityLiving, timeLeft);
  }
  
  public boolean hasTagCompound() {
    return (!this.field_190928_g && this.stackTagCompound != null);
  }
  
  @Nullable
  public NBTTagCompound getTagCompound() {
    return this.stackTagCompound;
  }
  
  public NBTTagCompound func_190925_c(String p_190925_1_) {
    if (this.stackTagCompound != null && this.stackTagCompound.hasKey(p_190925_1_, 10))
      return this.stackTagCompound.getCompoundTag(p_190925_1_); 
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    setTagInfo(p_190925_1_, (NBTBase)nbttagcompound);
    return nbttagcompound;
  }
  
  @Nullable
  public NBTTagCompound getSubCompound(String key) {
    return (this.stackTagCompound != null && this.stackTagCompound.hasKey(key, 10)) ? this.stackTagCompound.getCompoundTag(key) : null;
  }
  
  public void func_190919_e(String p_190919_1_) {
    if (this.stackTagCompound != null && this.stackTagCompound.hasKey(p_190919_1_, 10))
      this.stackTagCompound.removeTag(p_190919_1_); 
  }
  
  public NBTTagList getEnchantmentTagList() {
    return (this.stackTagCompound != null) ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList();
  }
  
  public void setTagCompound(@Nullable NBTTagCompound nbt) {
    this.stackTagCompound = nbt;
  }
  
  public String getDisplayName() {
    NBTTagCompound nbttagcompound = getSubCompound("display");
    if (nbttagcompound != null) {
      if (nbttagcompound.hasKey("Name", 8))
        return nbttagcompound.getString("Name"); 
      if (nbttagcompound.hasKey("LocName", 8))
        return I18n.translateToLocal(nbttagcompound.getString("LocName")); 
    } 
    return getItem().getItemStackDisplayName(this);
  }
  
  public ItemStack func_190924_f(String p_190924_1_) {
    func_190925_c("display").setString("LocName", p_190924_1_);
    return this;
  }
  
  public ItemStack setStackDisplayName(String displayName) {
    func_190925_c("display").setString("Name", displayName);
    return this;
  }
  
  public void clearCustomName() {
    NBTTagCompound nbttagcompound = getSubCompound("display");
    if (nbttagcompound != null) {
      nbttagcompound.removeTag("Name");
      if (nbttagcompound.hasNoTags())
        func_190919_e("display"); 
    } 
    if (this.stackTagCompound != null && this.stackTagCompound.hasNoTags())
      this.stackTagCompound = null; 
  }
  
  public boolean hasDisplayName() {
    NBTTagCompound nbttagcompound = getSubCompound("display");
    return (nbttagcompound != null && nbttagcompound.hasKey("Name", 8));
  }
  
  public List<String> getTooltip(@Nullable EntityPlayer playerIn, ITooltipFlag advanced) {
    List<String> list = Lists.newArrayList();
    String s = getDisplayName();
    if (hasDisplayName())
      s = TextFormatting.ITALIC + s; 
    s = String.valueOf(s) + TextFormatting.RESET;
    if (advanced.func_194127_a()) {
      String s1 = "";
      if (!s.isEmpty()) {
        s = String.valueOf(s) + " (";
        s1 = ")";
      } 
      int j = Item.getIdFromItem(this.item);
      if (getHasSubtypes()) {
        s = String.valueOf(s) + String.format("#%04d/%d%s", new Object[] { Integer.valueOf(j), Integer.valueOf(this.itemDamage), s1 });
      } else {
        s = String.valueOf(s) + String.format("#%04d%s", new Object[] { Integer.valueOf(j), s1 });
      } 
    } else if (!hasDisplayName() && this.item == Items.FILLED_MAP) {
      s = String.valueOf(s) + " #" + this.itemDamage;
    } 
    list.add(s);
    int i1 = 0;
    if (hasTagCompound() && this.stackTagCompound.hasKey("HideFlags", 99))
      i1 = this.stackTagCompound.getInteger("HideFlags"); 
    if ((i1 & 0x20) == 0)
      getItem().addInformation(this, (playerIn == null) ? null : playerIn.world, list, advanced); 
    if (hasTagCompound()) {
      if ((i1 & 0x1) == 0) {
        NBTTagList nbttaglist = getEnchantmentTagList();
        for (int j = 0; j < nbttaglist.tagCount(); j++) {
          NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(j);
          int k = nbttagcompound.getShort("id");
          int l = nbttagcompound.getShort("lvl");
          Enchantment enchantment = Enchantment.getEnchantmentByID(k);
          if (enchantment != null)
            list.add(enchantment.getTranslatedName(l)); 
        } 
      } 
      if (this.stackTagCompound.hasKey("display", 10)) {
        NBTTagCompound nbttagcompound1 = this.stackTagCompound.getCompoundTag("display");
        if (nbttagcompound1.hasKey("color", 3))
          if (advanced.func_194127_a()) {
            list.add(I18n.translateToLocalFormatted("item.color", new Object[] { String.format("#%06X", new Object[] { Integer.valueOf(nbttagcompound1.getInteger("color")) }) }));
          } else {
            list.add(TextFormatting.ITALIC + I18n.translateToLocal("item.dyed"));
          }  
        if (nbttagcompound1.getTagId("Lore") == 9) {
          NBTTagList nbttaglist3 = nbttagcompound1.getTagList("Lore", 8);
          if (!nbttaglist3.hasNoTags())
            for (int l1 = 0; l1 < nbttaglist3.tagCount(); l1++)
              list.add(TextFormatting.DARK_PURPLE + TextFormatting.ITALIC + nbttaglist3.getStringTagAt(l1));  
        } 
      } 
    } 
    byte b;
    int i;
    EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
    for (i = (arrayOfEntityEquipmentSlot = EntityEquipmentSlot.values()).length, b = 0; b < i; ) {
      EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
      Multimap<String, AttributeModifier> multimap = getAttributeModifiers(entityequipmentslot);
      if (!multimap.isEmpty() && (i1 & 0x2) == 0) {
        list.add("");
        list.add(I18n.translateToLocal("item.modifiers." + entityequipmentslot.getName()));
        for (Map.Entry<String, AttributeModifier> entry : (Iterable<Map.Entry<String, AttributeModifier>>)multimap.entries()) {
          double d1;
          AttributeModifier attributemodifier = entry.getValue();
          double d0 = attributemodifier.getAmount();
          boolean flag = false;
          if (playerIn != null)
            if (attributemodifier.getID() == Item.ATTACK_DAMAGE_MODIFIER) {
              d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
              d0 += EnchantmentHelper.getModifierForCreature(this, EnumCreatureAttribute.UNDEFINED);
              flag = true;
            } else if (attributemodifier.getID() == Item.ATTACK_SPEED_MODIFIER) {
              d0 += playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
              flag = true;
            }  
          if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2) {
            d1 = d0;
          } else {
            d1 = d0 * 100.0D;
          } 
          if (flag) {
            list.add(" " + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), new Object[] { DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey()) }));
            continue;
          } 
          if (d0 > 0.0D) {
            list.add(TextFormatting.BLUE + " " + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), new Object[] { DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey()) }));
            continue;
          } 
          if (d0 < 0.0D) {
            d1 *= -1.0D;
            list.add(TextFormatting.RED + " " + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), new Object[] { DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)entry.getKey()) }));
          } 
        } 
      } 
      b++;
    } 
    if (hasTagCompound() && getTagCompound().getBoolean("Unbreakable") && (i1 & 0x4) == 0)
      list.add(TextFormatting.BLUE + I18n.translateToLocal("item.unbreakable")); 
    if (hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9) && (i1 & 0x8) == 0) {
      NBTTagList nbttaglist1 = this.stackTagCompound.getTagList("CanDestroy", 8);
      if (!nbttaglist1.hasNoTags()) {
        list.add("");
        list.add(TextFormatting.GRAY + I18n.translateToLocal("item.canBreak"));
        for (int j1 = 0; j1 < nbttaglist1.tagCount(); j1++) {
          Block block = Block.getBlockFromName(nbttaglist1.getStringTagAt(j1));
          if (block != null) {
            list.add(TextFormatting.DARK_GRAY + block.getLocalizedName());
          } else {
            list.add(TextFormatting.DARK_GRAY + "missingno");
          } 
        } 
      } 
    } 
    if (hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9) && (i1 & 0x10) == 0) {
      NBTTagList nbttaglist2 = this.stackTagCompound.getTagList("CanPlaceOn", 8);
      if (!nbttaglist2.hasNoTags()) {
        list.add("");
        list.add(TextFormatting.GRAY + I18n.translateToLocal("item.canPlace"));
        for (int k1 = 0; k1 < nbttaglist2.tagCount(); k1++) {
          Block block1 = Block.getBlockFromName(nbttaglist2.getStringTagAt(k1));
          if (block1 != null) {
            list.add(TextFormatting.DARK_GRAY + block1.getLocalizedName());
          } else {
            list.add(TextFormatting.DARK_GRAY + "missingno");
          } 
        } 
      } 
    } 
    if (advanced.func_194127_a()) {
      if (isItemDamaged())
        list.add(I18n.translateToLocalFormatted("item.durability", new Object[] { Integer.valueOf(getMaxDamage() - getItemDamage()), Integer.valueOf(getMaxDamage()) })); 
      list.add(TextFormatting.DARK_GRAY + ((ResourceLocation)Item.REGISTRY.getNameForObject(this.item)).toString());
      if (hasTagCompound())
        list.add(TextFormatting.DARK_GRAY + I18n.translateToLocalFormatted("item.nbt_tags", new Object[] { Integer.valueOf(getTagCompound().getKeySet().size()) })); 
    } 
    return list;
  }
  
  public boolean hasEffect() {
    return getItem().hasEffect(this);
  }
  
  public EnumRarity getRarity() {
    return getItem().getRarity(this);
  }
  
  public boolean isItemEnchantable() {
    if (!getItem().isItemTool(this))
      return false; 
    return !isItemEnchanted();
  }
  
  public void addEnchantment(Enchantment ench, int level) {
    if (this.stackTagCompound == null)
      setTagCompound(new NBTTagCompound()); 
    if (!this.stackTagCompound.hasKey("ench", 9))
      this.stackTagCompound.setTag("ench", (NBTBase)new NBTTagList()); 
    NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    nbttagcompound.setShort("id", (short)Enchantment.getEnchantmentID(ench));
    nbttagcompound.setShort("lvl", (short)(byte)level);
    nbttaglist.appendTag((NBTBase)nbttagcompound);
  }
  
  public boolean isItemEnchanted() {
    if (this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9))
      return !this.stackTagCompound.getTagList("ench", 10).hasNoTags(); 
    return false;
  }
  
  public void setTagInfo(String key, NBTBase value) {
    if (this.stackTagCompound == null)
      setTagCompound(new NBTTagCompound()); 
    this.stackTagCompound.setTag(key, value);
  }
  
  public boolean canEditBlocks() {
    return getItem().canItemEditBlocks();
  }
  
  public boolean isOnItemFrame() {
    return (this.itemFrame != null);
  }
  
  public void setItemFrame(EntityItemFrame frame) {
    this.itemFrame = frame;
  }
  
  @Nullable
  public EntityItemFrame getItemFrame() {
    return this.field_190928_g ? null : this.itemFrame;
  }
  
  public int getRepairCost() {
    return (hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3)) ? this.stackTagCompound.getInteger("RepairCost") : 0;
  }
  
  public void setRepairCost(int cost) {
    if (!hasTagCompound())
      this.stackTagCompound = new NBTTagCompound(); 
    this.stackTagCompound.setInteger("RepairCost", cost);
  }
  
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
    Multimap<String, AttributeModifier> multimap;
    if (hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
      HashMultimap hashMultimap = HashMultimap.create();
      NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
        AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);
        if (attributemodifier != null && (!nbttagcompound.hasKey("Slot", 8) || nbttagcompound.getString("Slot").equals(equipmentSlot.getName())) && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L)
          hashMultimap.put(nbttagcompound.getString("AttributeName"), attributemodifier); 
      } 
    } else {
      multimap = getItem().getItemAttributeModifiers(equipmentSlot);
    } 
    return multimap;
  }
  
  public void addAttributeModifier(String attributeName, AttributeModifier modifier, @Nullable EntityEquipmentSlot equipmentSlot) {
    if (this.stackTagCompound == null)
      this.stackTagCompound = new NBTTagCompound(); 
    if (!this.stackTagCompound.hasKey("AttributeModifiers", 9))
      this.stackTagCompound.setTag("AttributeModifiers", (NBTBase)new NBTTagList()); 
    NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
    NBTTagCompound nbttagcompound = SharedMonsterAttributes.writeAttributeModifierToNBT(modifier);
    nbttagcompound.setString("AttributeName", attributeName);
    if (equipmentSlot != null)
      nbttagcompound.setString("Slot", equipmentSlot.getName()); 
    nbttaglist.appendTag((NBTBase)nbttagcompound);
  }
  
  public ITextComponent getTextComponent() {
    TextComponentString textcomponentstring = new TextComponentString(getDisplayName());
    if (hasDisplayName())
      textcomponentstring.getStyle().setItalic(Boolean.valueOf(true)); 
    ITextComponent itextcomponent = (new TextComponentString("[")).appendSibling((ITextComponent)textcomponentstring).appendText("]");
    if (!this.field_190928_g) {
      NBTTagCompound nbttagcompound = writeToNBT(new NBTTagCompound());
      itextcomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, (ITextComponent)new TextComponentString(nbttagcompound.toString())));
      itextcomponent.getStyle().setColor((getRarity()).rarityColor);
    } 
    return itextcomponent;
  }
  
  public boolean canDestroy(Block blockIn) {
    if (blockIn == this.canDestroyCacheBlock)
      return this.canDestroyCacheResult; 
    this.canDestroyCacheBlock = blockIn;
    if (hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
      NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
        if (block == blockIn) {
          this.canDestroyCacheResult = true;
          return true;
        } 
      } 
    } 
    this.canDestroyCacheResult = false;
    return false;
  }
  
  public boolean canPlaceOn(Block blockIn) {
    if (blockIn == this.canPlaceOnCacheBlock)
      return this.canPlaceOnCacheResult; 
    this.canPlaceOnCacheBlock = blockIn;
    if (hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
      NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        Block block = Block.getBlockFromName(nbttaglist.getStringTagAt(i));
        if (block == blockIn) {
          this.canPlaceOnCacheResult = true;
          return true;
        } 
      } 
    } 
    this.canPlaceOnCacheResult = false;
    return false;
  }
  
  public int func_190921_D() {
    return this.animationsToGo;
  }
  
  public void func_190915_d(int p_190915_1_) {
    this.animationsToGo = p_190915_1_;
  }
  
  public int func_190916_E() {
    return this.field_190928_g ? 0 : this.stackSize;
  }
  
  public void func_190920_e(int p_190920_1_) {
    this.stackSize = p_190920_1_;
    func_190923_F();
  }
  
  public void func_190917_f(int p_190917_1_) {
    func_190920_e(this.stackSize + p_190917_1_);
  }
  
  public void func_190918_g(int p_190918_1_) {
    func_190917_f(-p_190918_1_);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */