package net.minecraft.stats;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class StatList {
  protected static final Map<String, StatBase> ID_TO_STAT_MAP = Maps.newHashMap();
  
  public static final List<StatBase> ALL_STATS = Lists.newArrayList();
  
  public static final List<StatBase> BASIC_STATS = Lists.newArrayList();
  
  public static final List<StatCrafting> USE_ITEM_STATS = Lists.newArrayList();
  
  public static final List<StatCrafting> MINE_BLOCK_STATS = Lists.newArrayList();
  
  public static final StatBase LEAVE_GAME = (new StatBasic("stat.leaveGame", (ITextComponent)new TextComponentTranslation("stat.leaveGame", new Object[0]))).initIndependentStat().registerStat();
  
  public static final StatBase PLAY_ONE_MINUTE = (new StatBasic("stat.playOneMinute", (ITextComponent)new TextComponentTranslation("stat.playOneMinute", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
  
  public static final StatBase TIME_SINCE_DEATH = (new StatBasic("stat.timeSinceDeath", (ITextComponent)new TextComponentTranslation("stat.timeSinceDeath", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
  
  public static final StatBase SNEAK_TIME = (new StatBasic("stat.sneakTime", (ITextComponent)new TextComponentTranslation("stat.sneakTime", new Object[0]), StatBase.timeStatType)).initIndependentStat().registerStat();
  
  public static final StatBase WALK_ONE_CM = (new StatBasic("stat.walkOneCm", (ITextComponent)new TextComponentTranslation("stat.walkOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase CROUCH_ONE_CM = (new StatBasic("stat.crouchOneCm", (ITextComponent)new TextComponentTranslation("stat.crouchOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase SPRINT_ONE_CM = (new StatBasic("stat.sprintOneCm", (ITextComponent)new TextComponentTranslation("stat.sprintOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase SWIM_ONE_CM = (new StatBasic("stat.swimOneCm", (ITextComponent)new TextComponentTranslation("stat.swimOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase FALL_ONE_CM = (new StatBasic("stat.fallOneCm", (ITextComponent)new TextComponentTranslation("stat.fallOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase CLIMB_ONE_CM = (new StatBasic("stat.climbOneCm", (ITextComponent)new TextComponentTranslation("stat.climbOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase FLY_ONE_CM = (new StatBasic("stat.flyOneCm", (ITextComponent)new TextComponentTranslation("stat.flyOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase DIVE_ONE_CM = (new StatBasic("stat.diveOneCm", (ITextComponent)new TextComponentTranslation("stat.diveOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase MINECART_ONE_CM = (new StatBasic("stat.minecartOneCm", (ITextComponent)new TextComponentTranslation("stat.minecartOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase BOAT_ONE_CM = (new StatBasic("stat.boatOneCm", (ITextComponent)new TextComponentTranslation("stat.boatOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase PIG_ONE_CM = (new StatBasic("stat.pigOneCm", (ITextComponent)new TextComponentTranslation("stat.pigOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase HORSE_ONE_CM = (new StatBasic("stat.horseOneCm", (ITextComponent)new TextComponentTranslation("stat.horseOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase AVIATE_ONE_CM = (new StatBasic("stat.aviateOneCm", (ITextComponent)new TextComponentTranslation("stat.aviateOneCm", new Object[0]), StatBase.distanceStatType)).initIndependentStat().registerStat();
  
  public static final StatBase JUMP = (new StatBasic("stat.jump", (ITextComponent)new TextComponentTranslation("stat.jump", new Object[0]))).initIndependentStat().registerStat();
  
  public static final StatBase DROP = (new StatBasic("stat.drop", (ITextComponent)new TextComponentTranslation("stat.drop", new Object[0]))).initIndependentStat().registerStat();
  
  public static final StatBase DAMAGE_DEALT = (new StatBasic("stat.damageDealt", (ITextComponent)new TextComponentTranslation("stat.damageDealt", new Object[0]), StatBase.divideByTen)).registerStat();
  
  public static final StatBase DAMAGE_TAKEN = (new StatBasic("stat.damageTaken", (ITextComponent)new TextComponentTranslation("stat.damageTaken", new Object[0]), StatBase.divideByTen)).registerStat();
  
  public static final StatBase DEATHS = (new StatBasic("stat.deaths", (ITextComponent)new TextComponentTranslation("stat.deaths", new Object[0]))).registerStat();
  
  public static final StatBase MOB_KILLS = (new StatBasic("stat.mobKills", (ITextComponent)new TextComponentTranslation("stat.mobKills", new Object[0]))).registerStat();
  
  public static final StatBase ANIMALS_BRED = (new StatBasic("stat.animalsBred", (ITextComponent)new TextComponentTranslation("stat.animalsBred", new Object[0]))).registerStat();
  
  public static final StatBase PLAYER_KILLS = (new StatBasic("stat.playerKills", (ITextComponent)new TextComponentTranslation("stat.playerKills", new Object[0]))).registerStat();
  
  public static final StatBase FISH_CAUGHT = (new StatBasic("stat.fishCaught", (ITextComponent)new TextComponentTranslation("stat.fishCaught", new Object[0]))).registerStat();
  
  public static final StatBase TALKED_TO_VILLAGER = (new StatBasic("stat.talkedToVillager", (ITextComponent)new TextComponentTranslation("stat.talkedToVillager", new Object[0]))).registerStat();
  
  public static final StatBase TRADED_WITH_VILLAGER = (new StatBasic("stat.tradedWithVillager", (ITextComponent)new TextComponentTranslation("stat.tradedWithVillager", new Object[0]))).registerStat();
  
  public static final StatBase CAKE_SLICES_EATEN = (new StatBasic("stat.cakeSlicesEaten", (ITextComponent)new TextComponentTranslation("stat.cakeSlicesEaten", new Object[0]))).registerStat();
  
  public static final StatBase CAULDRON_FILLED = (new StatBasic("stat.cauldronFilled", (ITextComponent)new TextComponentTranslation("stat.cauldronFilled", new Object[0]))).registerStat();
  
  public static final StatBase CAULDRON_USED = (new StatBasic("stat.cauldronUsed", (ITextComponent)new TextComponentTranslation("stat.cauldronUsed", new Object[0]))).registerStat();
  
  public static final StatBase ARMOR_CLEANED = (new StatBasic("stat.armorCleaned", (ITextComponent)new TextComponentTranslation("stat.armorCleaned", new Object[0]))).registerStat();
  
  public static final StatBase BANNER_CLEANED = (new StatBasic("stat.bannerCleaned", (ITextComponent)new TextComponentTranslation("stat.bannerCleaned", new Object[0]))).registerStat();
  
  public static final StatBase BREWINGSTAND_INTERACTION = (new StatBasic("stat.brewingstandInteraction", (ITextComponent)new TextComponentTranslation("stat.brewingstandInteraction", new Object[0]))).registerStat();
  
  public static final StatBase BEACON_INTERACTION = (new StatBasic("stat.beaconInteraction", (ITextComponent)new TextComponentTranslation("stat.beaconInteraction", new Object[0]))).registerStat();
  
  public static final StatBase DROPPER_INSPECTED = (new StatBasic("stat.dropperInspected", (ITextComponent)new TextComponentTranslation("stat.dropperInspected", new Object[0]))).registerStat();
  
  public static final StatBase HOPPER_INSPECTED = (new StatBasic("stat.hopperInspected", (ITextComponent)new TextComponentTranslation("stat.hopperInspected", new Object[0]))).registerStat();
  
  public static final StatBase DISPENSER_INSPECTED = (new StatBasic("stat.dispenserInspected", (ITextComponent)new TextComponentTranslation("stat.dispenserInspected", new Object[0]))).registerStat();
  
  public static final StatBase NOTEBLOCK_PLAYED = (new StatBasic("stat.noteblockPlayed", (ITextComponent)new TextComponentTranslation("stat.noteblockPlayed", new Object[0]))).registerStat();
  
  public static final StatBase NOTEBLOCK_TUNED = (new StatBasic("stat.noteblockTuned", (ITextComponent)new TextComponentTranslation("stat.noteblockTuned", new Object[0]))).registerStat();
  
  public static final StatBase FLOWER_POTTED = (new StatBasic("stat.flowerPotted", (ITextComponent)new TextComponentTranslation("stat.flowerPotted", new Object[0]))).registerStat();
  
  public static final StatBase TRAPPED_CHEST_TRIGGERED = (new StatBasic("stat.trappedChestTriggered", (ITextComponent)new TextComponentTranslation("stat.trappedChestTriggered", new Object[0]))).registerStat();
  
  public static final StatBase ENDERCHEST_OPENED = (new StatBasic("stat.enderchestOpened", (ITextComponent)new TextComponentTranslation("stat.enderchestOpened", new Object[0]))).registerStat();
  
  public static final StatBase ITEM_ENCHANTED = (new StatBasic("stat.itemEnchanted", (ITextComponent)new TextComponentTranslation("stat.itemEnchanted", new Object[0]))).registerStat();
  
  public static final StatBase RECORD_PLAYED = (new StatBasic("stat.recordPlayed", (ITextComponent)new TextComponentTranslation("stat.recordPlayed", new Object[0]))).registerStat();
  
  public static final StatBase FURNACE_INTERACTION = (new StatBasic("stat.furnaceInteraction", (ITextComponent)new TextComponentTranslation("stat.furnaceInteraction", new Object[0]))).registerStat();
  
  public static final StatBase CRAFTING_TABLE_INTERACTION = (new StatBasic("stat.craftingTableInteraction", (ITextComponent)new TextComponentTranslation("stat.workbenchInteraction", new Object[0]))).registerStat();
  
  public static final StatBase CHEST_OPENED = (new StatBasic("stat.chestOpened", (ITextComponent)new TextComponentTranslation("stat.chestOpened", new Object[0]))).registerStat();
  
  public static final StatBase SLEEP_IN_BED = (new StatBasic("stat.sleepInBed", (ITextComponent)new TextComponentTranslation("stat.sleepInBed", new Object[0]))).registerStat();
  
  public static final StatBase field_191272_ae = (new StatBasic("stat.shulkerBoxOpened", (ITextComponent)new TextComponentTranslation("stat.shulkerBoxOpened", new Object[0]))).registerStat();
  
  private static final StatBase[] BLOCKS_STATS = new StatBase[4096];
  
  private static final StatBase[] CRAFTS_STATS = new StatBase[32000];
  
  private static final StatBase[] OBJECT_USE_STATS = new StatBase[32000];
  
  private static final StatBase[] OBJECT_BREAK_STATS = new StatBase[32000];
  
  private static final StatBase[] OBJECTS_PICKED_UP_STATS = new StatBase[32000];
  
  private static final StatBase[] OBJECTS_DROPPED_STATS = new StatBase[32000];
  
  @Nullable
  public static StatBase getBlockStats(Block blockIn) {
    return BLOCKS_STATS[Block.getIdFromBlock(blockIn)];
  }
  
  @Nullable
  public static StatBase getCraftStats(Item itemIn) {
    return CRAFTS_STATS[Item.getIdFromItem(itemIn)];
  }
  
  @Nullable
  public static StatBase getObjectUseStats(Item itemIn) {
    return OBJECT_USE_STATS[Item.getIdFromItem(itemIn)];
  }
  
  @Nullable
  public static StatBase getObjectBreakStats(Item itemIn) {
    return OBJECT_BREAK_STATS[Item.getIdFromItem(itemIn)];
  }
  
  @Nullable
  public static StatBase getObjectsPickedUpStats(Item itemIn) {
    return OBJECTS_PICKED_UP_STATS[Item.getIdFromItem(itemIn)];
  }
  
  @Nullable
  public static StatBase getDroppedObjectStats(Item itemIn) {
    return OBJECTS_DROPPED_STATS[Item.getIdFromItem(itemIn)];
  }
  
  public static void init() {
    initMiningStats();
    initStats();
    initItemDepleteStats();
    initCraftableStats();
    initPickedUpAndDroppedStats();
  }
  
  private static void initCraftableStats() {
    Set<Item> set = Sets.newHashSet();
    for (IRecipe irecipe : CraftingManager.field_193380_a) {
      ItemStack itemstack = irecipe.getRecipeOutput();
      if (!itemstack.func_190926_b())
        set.add(irecipe.getRecipeOutput().getItem()); 
    } 
    for (ItemStack itemstack1 : FurnaceRecipes.instance().getSmeltingList().values())
      set.add(itemstack1.getItem()); 
    for (Item item : set) {
      if (item != null) {
        int i = Item.getIdFromItem(item);
        String s = getItemName(item);
        if (s != null)
          CRAFTS_STATS[i] = (new StatCrafting("stat.craftItem.", s, (ITextComponent)new TextComponentTranslation("stat.craftItem", new Object[] { (new ItemStack(item)).getTextComponent() }), item)).registerStat(); 
      } 
    } 
    replaceAllSimilarBlocks(CRAFTS_STATS);
  }
  
  private static void initMiningStats() {
    for (Block block : Block.REGISTRY) {
      Item item = Item.getItemFromBlock(block);
      if (item != Items.field_190931_a) {
        int i = Block.getIdFromBlock(block);
        String s = getItemName(item);
        if (s != null && block.getEnableStats()) {
          BLOCKS_STATS[i] = (new StatCrafting("stat.mineBlock.", s, (ITextComponent)new TextComponentTranslation("stat.mineBlock", new Object[] { (new ItemStack(block)).getTextComponent() }), item)).registerStat();
          MINE_BLOCK_STATS.add((StatCrafting)BLOCKS_STATS[i]);
        } 
      } 
    } 
    replaceAllSimilarBlocks(BLOCKS_STATS);
  }
  
  private static void initStats() {
    for (Item item : Item.REGISTRY) {
      if (item != null) {
        int i = Item.getIdFromItem(item);
        String s = getItemName(item);
        if (s != null) {
          OBJECT_USE_STATS[i] = (new StatCrafting("stat.useItem.", s, (ITextComponent)new TextComponentTranslation("stat.useItem", new Object[] { (new ItemStack(item)).getTextComponent() }), item)).registerStat();
          if (!(item instanceof net.minecraft.item.ItemBlock))
            USE_ITEM_STATS.add((StatCrafting)OBJECT_USE_STATS[i]); 
        } 
      } 
    } 
    replaceAllSimilarBlocks(OBJECT_USE_STATS);
  }
  
  private static void initItemDepleteStats() {
    for (Item item : Item.REGISTRY) {
      if (item != null) {
        int i = Item.getIdFromItem(item);
        String s = getItemName(item);
        if (s != null && item.isDamageable())
          OBJECT_BREAK_STATS[i] = (new StatCrafting("stat.breakItem.", s, (ITextComponent)new TextComponentTranslation("stat.breakItem", new Object[] { (new ItemStack(item)).getTextComponent() }), item)).registerStat(); 
      } 
    } 
    replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
  }
  
  private static void initPickedUpAndDroppedStats() {
    for (Item item : Item.REGISTRY) {
      if (item != null) {
        int i = Item.getIdFromItem(item);
        String s = getItemName(item);
        if (s != null) {
          OBJECTS_PICKED_UP_STATS[i] = (new StatCrafting("stat.pickup.", s, (ITextComponent)new TextComponentTranslation("stat.pickup", new Object[] { (new ItemStack(item)).getTextComponent() }), item)).registerStat();
          OBJECTS_DROPPED_STATS[i] = (new StatCrafting("stat.drop.", s, (ITextComponent)new TextComponentTranslation("stat.drop", new Object[] { (new ItemStack(item)).getTextComponent() }), item)).registerStat();
        } 
      } 
    } 
    replaceAllSimilarBlocks(OBJECT_BREAK_STATS);
  }
  
  private static String getItemName(Item itemIn) {
    ResourceLocation resourcelocation = (ResourceLocation)Item.REGISTRY.getNameForObject(itemIn);
    return (resourcelocation != null) ? resourcelocation.toString().replace(':', '.') : null;
  }
  
  private static void replaceAllSimilarBlocks(StatBase[] stat) {
    mergeStatBases(stat, (Block)Blocks.WATER, (Block)Blocks.FLOWING_WATER);
    mergeStatBases(stat, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_LAVA);
    mergeStatBases(stat, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
    mergeStatBases(stat, Blocks.LIT_FURNACE, Blocks.FURNACE);
    mergeStatBases(stat, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
    mergeStatBases(stat, (Block)Blocks.POWERED_REPEATER, (Block)Blocks.UNPOWERED_REPEATER);
    mergeStatBases(stat, (Block)Blocks.POWERED_COMPARATOR, (Block)Blocks.UNPOWERED_COMPARATOR);
    mergeStatBases(stat, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
    mergeStatBases(stat, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
    mergeStatBases(stat, (Block)Blocks.DOUBLE_STONE_SLAB, (Block)Blocks.STONE_SLAB);
    mergeStatBases(stat, (Block)Blocks.DOUBLE_WOODEN_SLAB, (Block)Blocks.WOODEN_SLAB);
    mergeStatBases(stat, (Block)Blocks.DOUBLE_STONE_SLAB2, (Block)Blocks.STONE_SLAB2);
    mergeStatBases(stat, (Block)Blocks.GRASS, Blocks.DIRT);
    mergeStatBases(stat, Blocks.FARMLAND, Blocks.DIRT);
  }
  
  private static void mergeStatBases(StatBase[] statBaseIn, Block block1, Block block2) {
    int i = Block.getIdFromBlock(block1);
    int j = Block.getIdFromBlock(block2);
    if (statBaseIn[i] != null && statBaseIn[j] == null) {
      statBaseIn[j] = statBaseIn[i];
    } else {
      ALL_STATS.remove(statBaseIn[i]);
      MINE_BLOCK_STATS.remove(statBaseIn[i]);
      BASIC_STATS.remove(statBaseIn[i]);
      statBaseIn[i] = statBaseIn[j];
    } 
  }
  
  public static StatBase getStatKillEntity(EntityList.EntityEggInfo eggInfo) {
    String s = EntityList.func_191302_a(eggInfo.spawnedID);
    return (s == null) ? null : (new StatBase("stat.killEntity." + s, (ITextComponent)new TextComponentTranslation("stat.entityKill", new Object[] { new TextComponentTranslation("entity." + s + ".name", new Object[0]) }))).registerStat();
  }
  
  public static StatBase getStatEntityKilledBy(EntityList.EntityEggInfo eggInfo) {
    String s = EntityList.func_191302_a(eggInfo.spawnedID);
    return (s == null) ? null : (new StatBase("stat.entityKilledBy." + s, (ITextComponent)new TextComponentTranslation("stat.entityKilledBy", new Object[] { new TextComponentTranslation("entity." + s + ".name", new Object[0]) }))).registerStat();
  }
  
  @Nullable
  public static StatBase getOneShotStat(String statName) {
    return ID_TO_STAT_MAP.get(statName);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\stats\StatList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */