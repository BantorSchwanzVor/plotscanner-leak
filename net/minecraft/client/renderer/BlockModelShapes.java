package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStem;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class BlockModelShapes {
  private final Map<IBlockState, IBakedModel> bakedModelStore = Maps.newIdentityHashMap();
  
  private final BlockStateMapper blockStateMapper = new BlockStateMapper();
  
  private final ModelManager modelManager;
  
  public BlockModelShapes(ModelManager manager) {
    this.modelManager = manager;
    registerAllBlocks();
  }
  
  public BlockStateMapper getBlockStateMapper() {
    return this.blockStateMapper;
  }
  
  public TextureAtlasSprite getTexture(IBlockState state) {
    Block block = state.getBlock();
    IBakedModel ibakedmodel = getModelForState(state);
    if (ibakedmodel == null || ibakedmodel == this.modelManager.getMissingModel()) {
      if (block == Blocks.WALL_SIGN || block == Blocks.STANDING_SIGN || block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.STANDING_BANNER || block == Blocks.WALL_BANNER || block == Blocks.BED)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/planks_oak"); 
      if (block == Blocks.ENDER_CHEST)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/obsidian"); 
      if (block == Blocks.FLOWING_LAVA || block == Blocks.LAVA)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/lava_still"); 
      if (block == Blocks.FLOWING_WATER || block == Blocks.WATER)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/water_still"); 
      if (block == Blocks.SKULL)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/soul_sand"); 
      if (block == Blocks.BARRIER)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/barrier"); 
      if (block == Blocks.STRUCTURE_VOID)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:items/structure_void"); 
      if (block == Blocks.field_190977_dl)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_white"); 
      if (block == Blocks.field_190978_dm)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_orange"); 
      if (block == Blocks.field_190979_dn)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_magenta"); 
      if (block == Blocks.field_190980_do)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_light_blue"); 
      if (block == Blocks.field_190981_dp)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_yellow"); 
      if (block == Blocks.field_190982_dq)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_lime"); 
      if (block == Blocks.field_190983_dr)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_pink"); 
      if (block == Blocks.field_190984_ds)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_gray"); 
      if (block == Blocks.field_190985_dt)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_silver"); 
      if (block == Blocks.field_190986_du)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_cyan"); 
      if (block == Blocks.field_190987_dv)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_purple"); 
      if (block == Blocks.field_190988_dw)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_blue"); 
      if (block == Blocks.field_190989_dx)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_brown"); 
      if (block == Blocks.field_190990_dy)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_green"); 
      if (block == Blocks.field_190991_dz)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_red"); 
      if (block == Blocks.field_190975_dA)
        return this.modelManager.getTextureMap().getAtlasSprite("minecraft:blocks/shulker_top_black"); 
    } 
    if (ibakedmodel == null)
      ibakedmodel = this.modelManager.getMissingModel(); 
    return ibakedmodel.getParticleTexture();
  }
  
  public IBakedModel getModelForState(IBlockState state) {
    IBakedModel ibakedmodel = this.bakedModelStore.get(state);
    if (ibakedmodel == null)
      ibakedmodel = this.modelManager.getMissingModel(); 
    return ibakedmodel;
  }
  
  public ModelManager getModelManager() {
    return this.modelManager;
  }
  
  public void reloadModels() {
    this.bakedModelStore.clear();
    for (Map.Entry<IBlockState, ModelResourceLocation> entry : (Iterable<Map.Entry<IBlockState, ModelResourceLocation>>)this.blockStateMapper.putAllStateModelLocations().entrySet())
      this.bakedModelStore.put(entry.getKey(), this.modelManager.getModel(entry.getValue())); 
  }
  
  public void registerBlockWithStateMapper(Block assoc, IStateMapper stateMapper) {
    this.blockStateMapper.registerBlockStateMapper(assoc, stateMapper);
  }
  
  public void registerBuiltInBlocks(Block... builtIns) {
    this.blockStateMapper.registerBuiltInBlocks(builtIns);
  }
  
  private void registerAllBlocks() {
    registerBuiltInBlocks(new Block[] { 
          Blocks.AIR, (Block)Blocks.FLOWING_WATER, (Block)Blocks.WATER, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.LAVA, (Block)Blocks.PISTON_EXTENSION, (Block)Blocks.CHEST, Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.STANDING_SIGN, 
          (Block)Blocks.SKULL, Blocks.END_PORTAL, Blocks.BARRIER, Blocks.WALL_SIGN, Blocks.WALL_BANNER, Blocks.STANDING_BANNER, Blocks.END_GATEWAY, Blocks.STRUCTURE_VOID, Blocks.field_190977_dl, Blocks.field_190978_dm, 
          Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, 
          Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA, Blocks.BED });
    registerBlockWithStateMapper(Blocks.STONE, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStone.VARIANT).build());
    registerBlockWithStateMapper(Blocks.PRISMARINE, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPrismarine.VARIANT).build());
    registerBlockWithStateMapper((Block)Blocks.LEAVES, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockOldLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[] { (IProperty)BlockLeaves.CHECK_DECAY, (IProperty)BlockLeaves.DECAYABLE }).build());
    registerBlockWithStateMapper((Block)Blocks.LEAVES2, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockNewLeaf.VARIANT).withSuffix("_leaves").ignore(new IProperty[] { (IProperty)BlockLeaves.CHECK_DECAY, (IProperty)BlockLeaves.DECAYABLE }).build());
    registerBlockWithStateMapper((Block)Blocks.CACTUS, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockCactus.AGE }).build());
    registerBlockWithStateMapper((Block)Blocks.REEDS, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockReed.AGE }).build());
    registerBlockWithStateMapper(Blocks.JUKEBOX, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockJukebox.HAS_RECORD }).build());
    registerBlockWithStateMapper(Blocks.COBBLESTONE_WALL, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockWall.VARIANT).withSuffix("_wall").build());
    registerBlockWithStateMapper((Block)Blocks.DOUBLE_PLANT, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockDoublePlant.VARIANT).ignore(new IProperty[] { (IProperty)BlockDoublePlant.FACING }).build());
    registerBlockWithStateMapper(Blocks.OAK_FENCE_GATE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.SPRUCE_FENCE_GATE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.BIRCH_FENCE_GATE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.JUNGLE_FENCE_GATE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.DARK_OAK_FENCE_GATE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.ACACIA_FENCE_GATE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFenceGate.POWERED }).build());
    registerBlockWithStateMapper(Blocks.TRIPWIRE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockTripWire.DISARMED, (IProperty)BlockTripWire.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.DOUBLE_WOODEN_SLAB, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPlanks.VARIANT).withSuffix("_double_slab").build());
    registerBlockWithStateMapper((Block)Blocks.WOODEN_SLAB, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPlanks.VARIANT).withSuffix("_slab").build());
    registerBlockWithStateMapper(Blocks.TNT, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockTNT.EXPLODE }).build());
    registerBlockWithStateMapper((Block)Blocks.FIRE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFire.AGE }).build());
    registerBlockWithStateMapper((Block)Blocks.REDSTONE_WIRE, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockRedstoneWire.POWER }).build());
    registerBlockWithStateMapper((Block)Blocks.OAK_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.SPRUCE_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.BIRCH_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.JUNGLE_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.ACACIA_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.DARK_OAK_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper((Block)Blocks.IRON_DOOR, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDoor.POWERED }).build());
    registerBlockWithStateMapper(Blocks.WOOL, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_wool").build());
    registerBlockWithStateMapper(Blocks.CARPET, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_carpet").build());
    registerBlockWithStateMapper(Blocks.STAINED_HARDENED_CLAY, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_stained_hardened_clay").build());
    registerBlockWithStateMapper((Block)Blocks.STAINED_GLASS_PANE, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_stained_glass_pane").build());
    registerBlockWithStateMapper((Block)Blocks.STAINED_GLASS, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_stained_glass").build());
    registerBlockWithStateMapper(Blocks.SANDSTONE, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSandStone.TYPE).build());
    registerBlockWithStateMapper(Blocks.RED_SANDSTONE, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockRedSandstone.TYPE).build());
    registerBlockWithStateMapper((Block)Blocks.TALLGRASS, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockTallGrass.TYPE).build());
    registerBlockWithStateMapper((Block)Blocks.YELLOW_FLOWER, (IStateMapper)(new StateMap.Builder()).withName(Blocks.YELLOW_FLOWER.getTypeProperty()).build());
    registerBlockWithStateMapper((Block)Blocks.RED_FLOWER, (IStateMapper)(new StateMap.Builder()).withName(Blocks.RED_FLOWER.getTypeProperty()).build());
    registerBlockWithStateMapper((Block)Blocks.STONE_SLAB, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStoneSlab.VARIANT).withSuffix("_slab").build());
    registerBlockWithStateMapper((Block)Blocks.STONE_SLAB2, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStoneSlabNew.VARIANT).withSuffix("_slab").build());
    registerBlockWithStateMapper(Blocks.MONSTER_EGG, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSilverfish.VARIANT).withSuffix("_monster_egg").build());
    registerBlockWithStateMapper(Blocks.STONEBRICK, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockStoneBrick.VARIANT).build());
    registerBlockWithStateMapper(Blocks.DISPENSER, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDispenser.TRIGGERED }).build());
    registerBlockWithStateMapper(Blocks.DROPPER, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockDropper.TRIGGERED }).build());
    registerBlockWithStateMapper(Blocks.LOG, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockOldLog.VARIANT).withSuffix("_log").build());
    registerBlockWithStateMapper(Blocks.LOG2, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockNewLog.VARIANT).withSuffix("_log").build());
    registerBlockWithStateMapper(Blocks.PLANKS, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockPlanks.VARIANT).withSuffix("_planks").build());
    registerBlockWithStateMapper(Blocks.SAPLING, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSapling.TYPE).withSuffix("_sapling").build());
    registerBlockWithStateMapper((Block)Blocks.SAND, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockSand.VARIANT).build());
    registerBlockWithStateMapper((Block)Blocks.HOPPER, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockHopper.ENABLED }).build());
    registerBlockWithStateMapper(Blocks.FLOWER_POT, (IStateMapper)(new StateMap.Builder()).ignore(new IProperty[] { (IProperty)BlockFlowerPot.LEGACY_DATA }).build());
    registerBlockWithStateMapper(Blocks.field_192443_dR, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_concrete").build());
    registerBlockWithStateMapper(Blocks.field_192444_dS, (IStateMapper)(new StateMap.Builder()).withName((IProperty)BlockColored.COLOR).withSuffix("_concrete_powder").build());
    registerBlockWithStateMapper(Blocks.QUARTZ_BLOCK, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue((IProperty)BlockQuartz.VARIANT);
            switch (blockquartz$enumtype) {
              default:
                return new ModelResourceLocation("quartz_block", "normal");
              case null:
                return new ModelResourceLocation("chiseled_quartz_block", "normal");
              case LINES_Y:
                return new ModelResourceLocation("quartz_column", "axis=y");
              case LINES_X:
                return new ModelResourceLocation("quartz_column", "axis=x");
              case LINES_Z:
                break;
            } 
            return new ModelResourceLocation("quartz_column", "axis=z");
          }
        });
    registerBlockWithStateMapper((Block)Blocks.DEADBUSH, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            return new ModelResourceLocation("dead_bush", "normal");
          }
        });
    registerBlockWithStateMapper(Blocks.PUMPKIN_STEM, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap((Map)state.getProperties());
            if (state.getValue((IProperty)BlockStem.FACING) != EnumFacing.UP)
              map.remove(BlockStem.AGE); 
            return new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
          }
        });
    registerBlockWithStateMapper(Blocks.MELON_STEM, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap((Map)state.getProperties());
            if (state.getValue((IProperty)BlockStem.FACING) != EnumFacing.UP)
              map.remove(BlockStem.AGE); 
            return new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString(map));
          }
        });
    registerBlockWithStateMapper(Blocks.DIRT, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap((Map)state.getProperties());
            String s = BlockDirt.VARIANT.getName((Enum)map.remove(BlockDirt.VARIANT));
            if (BlockDirt.DirtType.PODZOL != state.getValue((IProperty)BlockDirt.VARIANT))
              map.remove(BlockDirt.SNOWY); 
            return new ModelResourceLocation(s, getPropertyString(map));
          }
        });
    registerBlockWithStateMapper((Block)Blocks.DOUBLE_STONE_SLAB, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap((Map)state.getProperties());
            String s = BlockStoneSlab.VARIANT.getName((Enum)map.remove(BlockStoneSlab.VARIANT));
            map.remove(BlockStoneSlab.SEAMLESS);
            String s1 = ((Boolean)state.getValue((IProperty)BlockStoneSlab.SEAMLESS)).booleanValue() ? "all" : "normal";
            return new ModelResourceLocation(String.valueOf(s) + "_double_slab", s1);
          }
        });
    registerBlockWithStateMapper((Block)Blocks.DOUBLE_STONE_SLAB2, (IStateMapper)new StateMapperBase() {
          protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            Map<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap((Map)state.getProperties());
            String s = BlockStoneSlabNew.VARIANT.getName((Enum)map.remove(BlockStoneSlabNew.VARIANT));
            map.remove(BlockStoneSlab.SEAMLESS);
            String s1 = ((Boolean)state.getValue((IProperty)BlockStoneSlabNew.SEAMLESS)).booleanValue() ? "all" : "normal";
            return new ModelResourceLocation(String.valueOf(s) + "_double_slab", s1);
          }
        });
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\BlockModelShapes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */