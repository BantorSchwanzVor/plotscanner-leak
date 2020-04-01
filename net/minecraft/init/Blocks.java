package net.minecraft.init;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.util.ResourceLocation;

public class Blocks {
  @Nullable
  private static Block getRegisteredBlock(String blockName) {
    Block block = (Block)Block.REGISTRY.getObject(new ResourceLocation(blockName));
    if (!CACHE.add(block))
      throw new IllegalStateException("Invalid Block requested: " + blockName); 
    return block;
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed Blocks before Bootstrap!"); 
  }
  
  private static final Set<Block> CACHE = Sets.newHashSet();
  
  public static final Block AIR = getRegisteredBlock("air");
  
  public static final Block STONE = getRegisteredBlock("stone");
  
  public static final BlockGrass GRASS = (BlockGrass)getRegisteredBlock("grass");
  
  public static final Block DIRT = getRegisteredBlock("dirt");
  
  public static final Block COBBLESTONE = getRegisteredBlock("cobblestone");
  
  public static final Block PLANKS = getRegisteredBlock("planks");
  
  public static final Block SAPLING = getRegisteredBlock("sapling");
  
  public static final Block BEDROCK = getRegisteredBlock("bedrock");
  
  public static final BlockDynamicLiquid FLOWING_WATER = (BlockDynamicLiquid)getRegisteredBlock("flowing_water");
  
  public static final BlockStaticLiquid WATER = (BlockStaticLiquid)getRegisteredBlock("water");
  
  public static final BlockDynamicLiquid FLOWING_LAVA = (BlockDynamicLiquid)getRegisteredBlock("flowing_lava");
  
  public static final BlockStaticLiquid LAVA = (BlockStaticLiquid)getRegisteredBlock("lava");
  
  public static final BlockSand SAND = (BlockSand)getRegisteredBlock("sand");
  
  public static final Block GRAVEL = getRegisteredBlock("gravel");
  
  public static final Block GOLD_ORE = getRegisteredBlock("gold_ore");
  
  public static final Block IRON_ORE = getRegisteredBlock("iron_ore");
  
  public static final Block COAL_ORE = getRegisteredBlock("coal_ore");
  
  public static final Block LOG = getRegisteredBlock("log");
  
  public static final Block LOG2 = getRegisteredBlock("log2");
  
  public static final BlockLeaves LEAVES = (BlockLeaves)getRegisteredBlock("leaves");
  
  public static final BlockLeaves LEAVES2 = (BlockLeaves)getRegisteredBlock("leaves2");
  
  public static final Block SPONGE = getRegisteredBlock("sponge");
  
  public static final Block GLASS = getRegisteredBlock("glass");
  
  public static final Block LAPIS_ORE = getRegisteredBlock("lapis_ore");
  
  public static final Block LAPIS_BLOCK = getRegisteredBlock("lapis_block");
  
  public static final Block DISPENSER = getRegisteredBlock("dispenser");
  
  public static final Block SANDSTONE = getRegisteredBlock("sandstone");
  
  public static final Block NOTEBLOCK = getRegisteredBlock("noteblock");
  
  public static final Block BED = getRegisteredBlock("bed");
  
  public static final Block GOLDEN_RAIL = getRegisteredBlock("golden_rail");
  
  public static final Block DETECTOR_RAIL = getRegisteredBlock("detector_rail");
  
  public static final BlockPistonBase STICKY_PISTON = (BlockPistonBase)getRegisteredBlock("sticky_piston");
  
  public static final Block WEB = getRegisteredBlock("web");
  
  public static final BlockTallGrass TALLGRASS = (BlockTallGrass)getRegisteredBlock("tallgrass");
  
  public static final BlockDeadBush DEADBUSH = (BlockDeadBush)getRegisteredBlock("deadbush");
  
  public static final BlockPistonBase PISTON = (BlockPistonBase)getRegisteredBlock("piston");
  
  public static final BlockPistonExtension PISTON_HEAD = (BlockPistonExtension)getRegisteredBlock("piston_head");
  
  public static final Block WOOL = getRegisteredBlock("wool");
  
  public static final BlockPistonMoving PISTON_EXTENSION = (BlockPistonMoving)getRegisteredBlock("piston_extension");
  
  public static final BlockFlower YELLOW_FLOWER = (BlockFlower)getRegisteredBlock("yellow_flower");
  
  public static final BlockFlower RED_FLOWER = (BlockFlower)getRegisteredBlock("red_flower");
  
  public static final BlockBush BROWN_MUSHROOM = (BlockBush)getRegisteredBlock("brown_mushroom");
  
  public static final BlockBush RED_MUSHROOM = (BlockBush)getRegisteredBlock("red_mushroom");
  
  public static final Block GOLD_BLOCK = getRegisteredBlock("gold_block");
  
  public static final Block IRON_BLOCK = getRegisteredBlock("iron_block");
  
  public static final BlockSlab DOUBLE_STONE_SLAB = (BlockSlab)getRegisteredBlock("double_stone_slab");
  
  public static final BlockSlab STONE_SLAB = (BlockSlab)getRegisteredBlock("stone_slab");
  
  public static final Block BRICK_BLOCK = getRegisteredBlock("brick_block");
  
  public static final Block TNT = getRegisteredBlock("tnt");
  
  public static final Block BOOKSHELF = getRegisteredBlock("bookshelf");
  
  public static final Block MOSSY_COBBLESTONE = getRegisteredBlock("mossy_cobblestone");
  
  public static final Block OBSIDIAN = getRegisteredBlock("obsidian");
  
  public static final Block TORCH = getRegisteredBlock("torch");
  
  public static final BlockFire FIRE = (BlockFire)getRegisteredBlock("fire");
  
  public static final Block MOB_SPAWNER = getRegisteredBlock("mob_spawner");
  
  public static final Block OAK_STAIRS = getRegisteredBlock("oak_stairs");
  
  public static final BlockChest CHEST = (BlockChest)getRegisteredBlock("chest");
  
  public static final BlockRedstoneWire REDSTONE_WIRE = (BlockRedstoneWire)getRegisteredBlock("redstone_wire");
  
  public static final Block DIAMOND_ORE = getRegisteredBlock("diamond_ore");
  
  public static final Block DIAMOND_BLOCK = getRegisteredBlock("diamond_block");
  
  public static final Block CRAFTING_TABLE = getRegisteredBlock("crafting_table");
  
  public static final Block WHEAT = getRegisteredBlock("wheat");
  
  public static final Block FARMLAND = getRegisteredBlock("farmland");
  
  public static final Block FURNACE = getRegisteredBlock("furnace");
  
  public static final Block LIT_FURNACE = getRegisteredBlock("lit_furnace");
  
  public static final Block STANDING_SIGN = getRegisteredBlock("standing_sign");
  
  public static final BlockDoor OAK_DOOR = (BlockDoor)getRegisteredBlock("wooden_door");
  
  public static final BlockDoor SPRUCE_DOOR = (BlockDoor)getRegisteredBlock("spruce_door");
  
  public static final BlockDoor BIRCH_DOOR = (BlockDoor)getRegisteredBlock("birch_door");
  
  public static final BlockDoor JUNGLE_DOOR = (BlockDoor)getRegisteredBlock("jungle_door");
  
  public static final BlockDoor ACACIA_DOOR = (BlockDoor)getRegisteredBlock("acacia_door");
  
  public static final BlockDoor DARK_OAK_DOOR = (BlockDoor)getRegisteredBlock("dark_oak_door");
  
  public static final Block LADDER = getRegisteredBlock("ladder");
  
  public static final Block RAIL = getRegisteredBlock("rail");
  
  public static final Block STONE_STAIRS = getRegisteredBlock("stone_stairs");
  
  public static final Block WALL_SIGN = getRegisteredBlock("wall_sign");
  
  public static final Block LEVER = getRegisteredBlock("lever");
  
  public static final Block STONE_PRESSURE_PLATE = getRegisteredBlock("stone_pressure_plate");
  
  public static final BlockDoor IRON_DOOR = (BlockDoor)getRegisteredBlock("iron_door");
  
  public static final Block WOODEN_PRESSURE_PLATE = getRegisteredBlock("wooden_pressure_plate");
  
  public static final Block REDSTONE_ORE = getRegisteredBlock("redstone_ore");
  
  public static final Block LIT_REDSTONE_ORE = getRegisteredBlock("lit_redstone_ore");
  
  public static final Block UNLIT_REDSTONE_TORCH = getRegisteredBlock("unlit_redstone_torch");
  
  public static final Block REDSTONE_TORCH = getRegisteredBlock("redstone_torch");
  
  public static final Block STONE_BUTTON = getRegisteredBlock("stone_button");
  
  public static final Block SNOW_LAYER = getRegisteredBlock("snow_layer");
  
  public static final Block ICE = getRegisteredBlock("ice");
  
  public static final Block SNOW = getRegisteredBlock("snow");
  
  public static final BlockCactus CACTUS = (BlockCactus)getRegisteredBlock("cactus");
  
  public static final Block CLAY = getRegisteredBlock("clay");
  
  public static final BlockReed REEDS = (BlockReed)getRegisteredBlock("reeds");
  
  public static final Block JUKEBOX = getRegisteredBlock("jukebox");
  
  public static final Block OAK_FENCE = getRegisteredBlock("fence");
  
  public static final Block SPRUCE_FENCE = getRegisteredBlock("spruce_fence");
  
  public static final Block BIRCH_FENCE = getRegisteredBlock("birch_fence");
  
  public static final Block JUNGLE_FENCE = getRegisteredBlock("jungle_fence");
  
  public static final Block DARK_OAK_FENCE = getRegisteredBlock("dark_oak_fence");
  
  public static final Block ACACIA_FENCE = getRegisteredBlock("acacia_fence");
  
  public static final Block PUMPKIN = getRegisteredBlock("pumpkin");
  
  public static final Block NETHERRACK = getRegisteredBlock("netherrack");
  
  public static final Block SOUL_SAND = getRegisteredBlock("soul_sand");
  
  public static final Block GLOWSTONE = getRegisteredBlock("glowstone");
  
  public static final BlockPortal PORTAL = (BlockPortal)getRegisteredBlock("portal");
  
  public static final Block LIT_PUMPKIN = getRegisteredBlock("lit_pumpkin");
  
  public static final Block CAKE = getRegisteredBlock("cake");
  
  public static final BlockRedstoneRepeater UNPOWERED_REPEATER = (BlockRedstoneRepeater)getRegisteredBlock("unpowered_repeater");
  
  public static final BlockRedstoneRepeater POWERED_REPEATER = (BlockRedstoneRepeater)getRegisteredBlock("powered_repeater");
  
  public static final Block TRAPDOOR = getRegisteredBlock("trapdoor");
  
  public static final Block MONSTER_EGG = getRegisteredBlock("monster_egg");
  
  public static final Block STONEBRICK = getRegisteredBlock("stonebrick");
  
  public static final Block BROWN_MUSHROOM_BLOCK = getRegisteredBlock("brown_mushroom_block");
  
  public static final Block RED_MUSHROOM_BLOCK = getRegisteredBlock("red_mushroom_block");
  
  public static final Block IRON_BARS = getRegisteredBlock("iron_bars");
  
  public static final Block GLASS_PANE = getRegisteredBlock("glass_pane");
  
  public static final Block MELON_BLOCK = getRegisteredBlock("melon_block");
  
  public static final Block PUMPKIN_STEM = getRegisteredBlock("pumpkin_stem");
  
  public static final Block MELON_STEM = getRegisteredBlock("melon_stem");
  
  public static final Block VINE = getRegisteredBlock("vine");
  
  public static final Block OAK_FENCE_GATE = getRegisteredBlock("fence_gate");
  
  public static final Block SPRUCE_FENCE_GATE = getRegisteredBlock("spruce_fence_gate");
  
  public static final Block BIRCH_FENCE_GATE = getRegisteredBlock("birch_fence_gate");
  
  public static final Block JUNGLE_FENCE_GATE = getRegisteredBlock("jungle_fence_gate");
  
  public static final Block DARK_OAK_FENCE_GATE = getRegisteredBlock("dark_oak_fence_gate");
  
  public static final Block ACACIA_FENCE_GATE = getRegisteredBlock("acacia_fence_gate");
  
  public static final Block BRICK_STAIRS = getRegisteredBlock("brick_stairs");
  
  public static final Block STONE_BRICK_STAIRS = getRegisteredBlock("stone_brick_stairs");
  
  public static final BlockMycelium MYCELIUM = (BlockMycelium)getRegisteredBlock("mycelium");
  
  public static final Block WATERLILY = getRegisteredBlock("waterlily");
  
  public static final Block NETHER_BRICK = getRegisteredBlock("nether_brick");
  
  public static final Block NETHER_BRICK_FENCE = getRegisteredBlock("nether_brick_fence");
  
  public static final Block NETHER_BRICK_STAIRS = getRegisteredBlock("nether_brick_stairs");
  
  public static final Block NETHER_WART = getRegisteredBlock("nether_wart");
  
  public static final Block ENCHANTING_TABLE = getRegisteredBlock("enchanting_table");
  
  public static final Block BREWING_STAND = getRegisteredBlock("brewing_stand");
  
  public static final BlockCauldron CAULDRON = (BlockCauldron)getRegisteredBlock("cauldron");
  
  public static final Block END_PORTAL = getRegisteredBlock("end_portal");
  
  public static final Block END_PORTAL_FRAME = getRegisteredBlock("end_portal_frame");
  
  public static final Block END_STONE = getRegisteredBlock("end_stone");
  
  public static final Block DRAGON_EGG = getRegisteredBlock("dragon_egg");
  
  public static final Block REDSTONE_LAMP = getRegisteredBlock("redstone_lamp");
  
  public static final Block LIT_REDSTONE_LAMP = getRegisteredBlock("lit_redstone_lamp");
  
  public static final BlockSlab DOUBLE_WOODEN_SLAB = (BlockSlab)getRegisteredBlock("double_wooden_slab");
  
  public static final BlockSlab WOODEN_SLAB = (BlockSlab)getRegisteredBlock("wooden_slab");
  
  public static final Block COCOA = getRegisteredBlock("cocoa");
  
  public static final Block SANDSTONE_STAIRS = getRegisteredBlock("sandstone_stairs");
  
  public static final Block EMERALD_ORE = getRegisteredBlock("emerald_ore");
  
  public static final Block ENDER_CHEST = getRegisteredBlock("ender_chest");
  
  public static final BlockTripWireHook TRIPWIRE_HOOK = (BlockTripWireHook)getRegisteredBlock("tripwire_hook");
  
  public static final Block TRIPWIRE = getRegisteredBlock("tripwire");
  
  public static final Block EMERALD_BLOCK = getRegisteredBlock("emerald_block");
  
  public static final Block SPRUCE_STAIRS = getRegisteredBlock("spruce_stairs");
  
  public static final Block BIRCH_STAIRS = getRegisteredBlock("birch_stairs");
  
  public static final Block JUNGLE_STAIRS = getRegisteredBlock("jungle_stairs");
  
  public static final Block COMMAND_BLOCK = getRegisteredBlock("command_block");
  
  public static final BlockBeacon BEACON = (BlockBeacon)getRegisteredBlock("beacon");
  
  public static final Block COBBLESTONE_WALL = getRegisteredBlock("cobblestone_wall");
  
  public static final Block FLOWER_POT = getRegisteredBlock("flower_pot");
  
  public static final Block CARROTS = getRegisteredBlock("carrots");
  
  public static final Block POTATOES = getRegisteredBlock("potatoes");
  
  public static final Block WOODEN_BUTTON = getRegisteredBlock("wooden_button");
  
  public static final BlockSkull SKULL = (BlockSkull)getRegisteredBlock("skull");
  
  public static final Block ANVIL = getRegisteredBlock("anvil");
  
  public static final Block TRAPPED_CHEST = getRegisteredBlock("trapped_chest");
  
  public static final Block LIGHT_WEIGHTED_PRESSURE_PLATE = getRegisteredBlock("light_weighted_pressure_plate");
  
  public static final Block HEAVY_WEIGHTED_PRESSURE_PLATE = getRegisteredBlock("heavy_weighted_pressure_plate");
  
  public static final BlockRedstoneComparator UNPOWERED_COMPARATOR = (BlockRedstoneComparator)getRegisteredBlock("unpowered_comparator");
  
  public static final BlockRedstoneComparator POWERED_COMPARATOR = (BlockRedstoneComparator)getRegisteredBlock("powered_comparator");
  
  public static final BlockDaylightDetector DAYLIGHT_DETECTOR = (BlockDaylightDetector)getRegisteredBlock("daylight_detector");
  
  public static final BlockDaylightDetector DAYLIGHT_DETECTOR_INVERTED = (BlockDaylightDetector)getRegisteredBlock("daylight_detector_inverted");
  
  public static final Block REDSTONE_BLOCK = getRegisteredBlock("redstone_block");
  
  public static final Block QUARTZ_ORE = getRegisteredBlock("quartz_ore");
  
  public static final BlockHopper HOPPER = (BlockHopper)getRegisteredBlock("hopper");
  
  public static final Block QUARTZ_BLOCK = getRegisteredBlock("quartz_block");
  
  public static final Block QUARTZ_STAIRS = getRegisteredBlock("quartz_stairs");
  
  public static final Block ACTIVATOR_RAIL = getRegisteredBlock("activator_rail");
  
  public static final Block DROPPER = getRegisteredBlock("dropper");
  
  public static final Block STAINED_HARDENED_CLAY = getRegisteredBlock("stained_hardened_clay");
  
  public static final Block BARRIER = getRegisteredBlock("barrier");
  
  public static final Block IRON_TRAPDOOR = getRegisteredBlock("iron_trapdoor");
  
  public static final Block HAY_BLOCK = getRegisteredBlock("hay_block");
  
  public static final Block CARPET = getRegisteredBlock("carpet");
  
  public static final Block HARDENED_CLAY = getRegisteredBlock("hardened_clay");
  
  public static final Block COAL_BLOCK = getRegisteredBlock("coal_block");
  
  public static final Block PACKED_ICE = getRegisteredBlock("packed_ice");
  
  public static final Block ACACIA_STAIRS = getRegisteredBlock("acacia_stairs");
  
  public static final Block DARK_OAK_STAIRS = getRegisteredBlock("dark_oak_stairs");
  
  public static final Block SLIME_BLOCK = getRegisteredBlock("slime");
  
  public static final BlockDoublePlant DOUBLE_PLANT = (BlockDoublePlant)getRegisteredBlock("double_plant");
  
  public static final BlockStainedGlass STAINED_GLASS = (BlockStainedGlass)getRegisteredBlock("stained_glass");
  
  public static final BlockStainedGlassPane STAINED_GLASS_PANE = (BlockStainedGlassPane)getRegisteredBlock("stained_glass_pane");
  
  public static final Block PRISMARINE = getRegisteredBlock("prismarine");
  
  public static final Block SEA_LANTERN = getRegisteredBlock("sea_lantern");
  
  public static final Block STANDING_BANNER = getRegisteredBlock("standing_banner");
  
  public static final Block WALL_BANNER = getRegisteredBlock("wall_banner");
  
  public static final Block RED_SANDSTONE = getRegisteredBlock("red_sandstone");
  
  public static final Block RED_SANDSTONE_STAIRS = getRegisteredBlock("red_sandstone_stairs");
  
  public static final BlockSlab DOUBLE_STONE_SLAB2 = (BlockSlab)getRegisteredBlock("double_stone_slab2");
  
  public static final BlockSlab STONE_SLAB2 = (BlockSlab)getRegisteredBlock("stone_slab2");
  
  public static final Block END_ROD = getRegisteredBlock("end_rod");
  
  public static final Block CHORUS_PLANT = getRegisteredBlock("chorus_plant");
  
  public static final Block CHORUS_FLOWER = getRegisteredBlock("chorus_flower");
  
  public static final Block PURPUR_BLOCK = getRegisteredBlock("purpur_block");
  
  public static final Block PURPUR_PILLAR = getRegisteredBlock("purpur_pillar");
  
  public static final Block PURPUR_STAIRS = getRegisteredBlock("purpur_stairs");
  
  public static final BlockSlab PURPUR_DOUBLE_SLAB = (BlockSlab)getRegisteredBlock("purpur_double_slab");
  
  public static final BlockSlab PURPUR_SLAB = (BlockSlab)getRegisteredBlock("purpur_slab");
  
  public static final Block END_BRICKS = getRegisteredBlock("end_bricks");
  
  public static final Block BEETROOTS = getRegisteredBlock("beetroots");
  
  public static final Block GRASS_PATH = getRegisteredBlock("grass_path");
  
  public static final Block END_GATEWAY = getRegisteredBlock("end_gateway");
  
  public static final Block REPEATING_COMMAND_BLOCK = getRegisteredBlock("repeating_command_block");
  
  public static final Block CHAIN_COMMAND_BLOCK = getRegisteredBlock("chain_command_block");
  
  public static final Block FROSTED_ICE = getRegisteredBlock("frosted_ice");
  
  public static final Block MAGMA = getRegisteredBlock("magma");
  
  public static final Block NETHER_WART_BLOCK = getRegisteredBlock("nether_wart_block");
  
  public static final Block RED_NETHER_BRICK = getRegisteredBlock("red_nether_brick");
  
  public static final Block BONE_BLOCK = getRegisteredBlock("bone_block");
  
  public static final Block STRUCTURE_VOID = getRegisteredBlock("structure_void");
  
  public static final Block field_190976_dk = getRegisteredBlock("observer");
  
  public static final Block field_190977_dl = getRegisteredBlock("white_shulker_box");
  
  public static final Block field_190978_dm = getRegisteredBlock("orange_shulker_box");
  
  public static final Block field_190979_dn = getRegisteredBlock("magenta_shulker_box");
  
  public static final Block field_190980_do = getRegisteredBlock("light_blue_shulker_box");
  
  public static final Block field_190981_dp = getRegisteredBlock("yellow_shulker_box");
  
  public static final Block field_190982_dq = getRegisteredBlock("lime_shulker_box");
  
  public static final Block field_190983_dr = getRegisteredBlock("pink_shulker_box");
  
  public static final Block field_190984_ds = getRegisteredBlock("gray_shulker_box");
  
  public static final Block field_190985_dt = getRegisteredBlock("silver_shulker_box");
  
  public static final Block field_190986_du = getRegisteredBlock("cyan_shulker_box");
  
  public static final Block field_190987_dv = getRegisteredBlock("purple_shulker_box");
  
  public static final Block field_190988_dw = getRegisteredBlock("blue_shulker_box");
  
  public static final Block field_190989_dx = getRegisteredBlock("brown_shulker_box");
  
  public static final Block field_190990_dy = getRegisteredBlock("green_shulker_box");
  
  public static final Block field_190991_dz = getRegisteredBlock("red_shulker_box");
  
  public static final Block field_190975_dA = getRegisteredBlock("black_shulker_box");
  
  public static final Block field_192427_dB = getRegisteredBlock("white_glazed_terracotta");
  
  public static final Block field_192428_dC = getRegisteredBlock("orange_glazed_terracotta");
  
  public static final Block field_192429_dD = getRegisteredBlock("magenta_glazed_terracotta");
  
  public static final Block field_192430_dE = getRegisteredBlock("light_blue_glazed_terracotta");
  
  public static final Block field_192431_dF = getRegisteredBlock("yellow_glazed_terracotta");
  
  public static final Block field_192432_dG = getRegisteredBlock("lime_glazed_terracotta");
  
  public static final Block field_192433_dH = getRegisteredBlock("pink_glazed_terracotta");
  
  public static final Block field_192434_dI = getRegisteredBlock("gray_glazed_terracotta");
  
  public static final Block field_192435_dJ = getRegisteredBlock("silver_glazed_terracotta");
  
  public static final Block field_192436_dK = getRegisteredBlock("cyan_glazed_terracotta");
  
  public static final Block field_192437_dL = getRegisteredBlock("purple_glazed_terracotta");
  
  public static final Block field_192438_dM = getRegisteredBlock("blue_glazed_terracotta");
  
  public static final Block field_192439_dN = getRegisteredBlock("brown_glazed_terracotta");
  
  public static final Block field_192440_dO = getRegisteredBlock("green_glazed_terracotta");
  
  public static final Block field_192441_dP = getRegisteredBlock("red_glazed_terracotta");
  
  public static final Block field_192442_dQ = getRegisteredBlock("black_glazed_terracotta");
  
  public static final Block field_192443_dR = getRegisteredBlock("concrete");
  
  public static final Block field_192444_dS = getRegisteredBlock("concrete_powder");
  
  public static final Block STRUCTURE_BLOCK = getRegisteredBlock("structure_block");
  
  static {
    CACHE.clear();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\init\Blocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */