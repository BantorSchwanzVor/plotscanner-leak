package net.minecraft.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundEvents {
  private static SoundEvent getRegisteredSoundEvent(String id) {
    SoundEvent soundevent = (SoundEvent)SoundEvent.REGISTRY.getObject(new ResourceLocation(id));
    if (soundevent == null)
      throw new IllegalStateException("Invalid Sound requested: " + id); 
    return soundevent;
  }
  
  static {
    if (!Bootstrap.isRegistered())
      throw new RuntimeException("Accessed Sounds before Bootstrap!"); 
  }
  
  public static final SoundEvent AMBIENT_CAVE = getRegisteredSoundEvent("ambient.cave");
  
  public static final SoundEvent BLOCK_ANVIL_BREAK = getRegisteredSoundEvent("block.anvil.break");
  
  public static final SoundEvent BLOCK_ANVIL_DESTROY = getRegisteredSoundEvent("block.anvil.destroy");
  
  public static final SoundEvent BLOCK_ANVIL_FALL = getRegisteredSoundEvent("block.anvil.fall");
  
  public static final SoundEvent BLOCK_ANVIL_HIT = getRegisteredSoundEvent("block.anvil.hit");
  
  public static final SoundEvent BLOCK_ANVIL_LAND = getRegisteredSoundEvent("block.anvil.land");
  
  public static final SoundEvent BLOCK_ANVIL_PLACE = getRegisteredSoundEvent("block.anvil.place");
  
  public static final SoundEvent BLOCK_ANVIL_STEP = getRegisteredSoundEvent("block.anvil.step");
  
  public static final SoundEvent BLOCK_ANVIL_USE = getRegisteredSoundEvent("block.anvil.use");
  
  public static final SoundEvent ENTITY_ARMORSTAND_BREAK = getRegisteredSoundEvent("entity.armorstand.break");
  
  public static final SoundEvent ENTITY_ARMORSTAND_FALL = getRegisteredSoundEvent("entity.armorstand.fall");
  
  public static final SoundEvent ENTITY_ARMORSTAND_HIT = getRegisteredSoundEvent("entity.armorstand.hit");
  
  public static final SoundEvent ENTITY_ARMORSTAND_PLACE = getRegisteredSoundEvent("entity.armorstand.place");
  
  public static final SoundEvent ITEM_ARMOR_EQUIP_CHAIN = getRegisteredSoundEvent("item.armor.equip_chain");
  
  public static final SoundEvent ITEM_ARMOR_EQUIP_DIAMOND = getRegisteredSoundEvent("item.armor.equip_diamond");
  
  public static final SoundEvent field_191258_p = getRegisteredSoundEvent("item.armor.equip_elytra");
  
  public static final SoundEvent ITEM_ARMOR_EQUIP_GENERIC = getRegisteredSoundEvent("item.armor.equip_generic");
  
  public static final SoundEvent ITEM_ARMOR_EQUIP_GOLD = getRegisteredSoundEvent("item.armor.equip_gold");
  
  public static final SoundEvent ITEM_ARMOR_EQUIP_IRON = getRegisteredSoundEvent("item.armor.equip_iron");
  
  public static final SoundEvent ITEM_ARMOR_EQUIP_LEATHER = getRegisteredSoundEvent("item.armor.equip_leather");
  
  public static final SoundEvent ENTITY_ARROW_HIT = getRegisteredSoundEvent("entity.arrow.hit");
  
  public static final SoundEvent ENTITY_ARROW_HIT_PLAYER = getRegisteredSoundEvent("entity.arrow.hit_player");
  
  public static final SoundEvent ENTITY_ARROW_SHOOT = getRegisteredSoundEvent("entity.arrow.shoot");
  
  public static final SoundEvent ENTITY_BAT_AMBIENT = getRegisteredSoundEvent("entity.bat.ambient");
  
  public static final SoundEvent ENTITY_BAT_DEATH = getRegisteredSoundEvent("entity.bat.death");
  
  public static final SoundEvent ENTITY_BAT_HURT = getRegisteredSoundEvent("entity.bat.hurt");
  
  public static final SoundEvent ENTITY_BAT_LOOP = getRegisteredSoundEvent("entity.bat.loop");
  
  public static final SoundEvent ENTITY_BAT_TAKEOFF = getRegisteredSoundEvent("entity.bat.takeoff");
  
  public static final SoundEvent ENTITY_BLAZE_AMBIENT = getRegisteredSoundEvent("entity.blaze.ambient");
  
  public static final SoundEvent ENTITY_BLAZE_BURN = getRegisteredSoundEvent("entity.blaze.burn");
  
  public static final SoundEvent ENTITY_BLAZE_DEATH = getRegisteredSoundEvent("entity.blaze.death");
  
  public static final SoundEvent ENTITY_BLAZE_HURT = getRegisteredSoundEvent("entity.blaze.hurt");
  
  public static final SoundEvent ENTITY_BLAZE_SHOOT = getRegisteredSoundEvent("entity.blaze.shoot");
  
  public static final SoundEvent field_193778_H = getRegisteredSoundEvent("entity.boat.paddle_land");
  
  public static final SoundEvent field_193779_I = getRegisteredSoundEvent("entity.boat.paddle_water");
  
  public static final SoundEvent field_193780_J = getRegisteredSoundEvent("entity.bobber.retrieve");
  
  public static final SoundEvent ENTITY_BOBBER_SPLASH = getRegisteredSoundEvent("entity.bobber.splash");
  
  public static final SoundEvent ENTITY_BOBBER_THROW = getRegisteredSoundEvent("entity.bobber.throw");
  
  public static final SoundEvent field_191241_J = getRegisteredSoundEvent("item.bottle.empty");
  
  public static final SoundEvent ITEM_BOTTLE_FILL = getRegisteredSoundEvent("item.bottle.fill");
  
  public static final SoundEvent ITEM_BOTTLE_FILL_DRAGONBREATH = getRegisteredSoundEvent("item.bottle.fill_dragonbreath");
  
  public static final SoundEvent BLOCK_BREWING_STAND_BREW = getRegisteredSoundEvent("block.brewing_stand.brew");
  
  public static final SoundEvent ITEM_BUCKET_EMPTY = getRegisteredSoundEvent("item.bucket.empty");
  
  public static final SoundEvent ITEM_BUCKET_EMPTY_LAVA = getRegisteredSoundEvent("item.bucket.empty_lava");
  
  public static final SoundEvent ITEM_BUCKET_FILL = getRegisteredSoundEvent("item.bucket.fill");
  
  public static final SoundEvent ITEM_BUCKET_FILL_LAVA = getRegisteredSoundEvent("item.bucket.fill_lava");
  
  public static final SoundEvent ENTITY_CAT_AMBIENT = getRegisteredSoundEvent("entity.cat.ambient");
  
  public static final SoundEvent ENTITY_CAT_DEATH = getRegisteredSoundEvent("entity.cat.death");
  
  public static final SoundEvent ENTITY_CAT_HISS = getRegisteredSoundEvent("entity.cat.hiss");
  
  public static final SoundEvent ENTITY_CAT_HURT = getRegisteredSoundEvent("entity.cat.hurt");
  
  public static final SoundEvent ENTITY_CAT_PURR = getRegisteredSoundEvent("entity.cat.purr");
  
  public static final SoundEvent ENTITY_CAT_PURREOW = getRegisteredSoundEvent("entity.cat.purreow");
  
  public static final SoundEvent BLOCK_CHEST_CLOSE = getRegisteredSoundEvent("block.chest.close");
  
  public static final SoundEvent BLOCK_CHEST_LOCKED = getRegisteredSoundEvent("block.chest.locked");
  
  public static final SoundEvent BLOCK_CHEST_OPEN = getRegisteredSoundEvent("block.chest.open");
  
  public static final SoundEvent ENTITY_CHICKEN_AMBIENT = getRegisteredSoundEvent("entity.chicken.ambient");
  
  public static final SoundEvent ENTITY_CHICKEN_DEATH = getRegisteredSoundEvent("entity.chicken.death");
  
  public static final SoundEvent ENTITY_CHICKEN_EGG = getRegisteredSoundEvent("entity.chicken.egg");
  
  public static final SoundEvent ENTITY_CHICKEN_HURT = getRegisteredSoundEvent("entity.chicken.hurt");
  
  public static final SoundEvent ENTITY_CHICKEN_STEP = getRegisteredSoundEvent("entity.chicken.step");
  
  public static final SoundEvent BLOCK_CHORUS_FLOWER_DEATH = getRegisteredSoundEvent("block.chorus_flower.death");
  
  public static final SoundEvent BLOCK_CHORUS_FLOWER_GROW = getRegisteredSoundEvent("block.chorus_flower.grow");
  
  public static final SoundEvent ITEM_CHORUS_FRUIT_TELEPORT = getRegisteredSoundEvent("item.chorus_fruit.teleport");
  
  public static final SoundEvent BLOCK_CLOTH_BREAK = getRegisteredSoundEvent("block.cloth.break");
  
  public static final SoundEvent BLOCK_CLOTH_FALL = getRegisteredSoundEvent("block.cloth.fall");
  
  public static final SoundEvent BLOCK_CLOTH_HIT = getRegisteredSoundEvent("block.cloth.hit");
  
  public static final SoundEvent BLOCK_CLOTH_PLACE = getRegisteredSoundEvent("block.cloth.place");
  
  public static final SoundEvent BLOCK_CLOTH_STEP = getRegisteredSoundEvent("block.cloth.step");
  
  public static final SoundEvent BLOCK_COMPARATOR_CLICK = getRegisteredSoundEvent("block.comparator.click");
  
  public static final SoundEvent ENTITY_COW_AMBIENT = getRegisteredSoundEvent("entity.cow.ambient");
  
  public static final SoundEvent ENTITY_COW_DEATH = getRegisteredSoundEvent("entity.cow.death");
  
  public static final SoundEvent ENTITY_COW_HURT = getRegisteredSoundEvent("entity.cow.hurt");
  
  public static final SoundEvent ENTITY_COW_MILK = getRegisteredSoundEvent("entity.cow.milk");
  
  public static final SoundEvent ENTITY_COW_STEP = getRegisteredSoundEvent("entity.cow.step");
  
  public static final SoundEvent ENTITY_CREEPER_DEATH = getRegisteredSoundEvent("entity.creeper.death");
  
  public static final SoundEvent ENTITY_CREEPER_HURT = getRegisteredSoundEvent("entity.creeper.hurt");
  
  public static final SoundEvent ENTITY_CREEPER_PRIMED = getRegisteredSoundEvent("entity.creeper.primed");
  
  public static final SoundEvent BLOCK_DISPENSER_DISPENSE = getRegisteredSoundEvent("block.dispenser.dispense");
  
  public static final SoundEvent BLOCK_DISPENSER_FAIL = getRegisteredSoundEvent("block.dispenser.fail");
  
  public static final SoundEvent BLOCK_DISPENSER_LAUNCH = getRegisteredSoundEvent("block.dispenser.launch");
  
  public static final SoundEvent ENTITY_DONKEY_AMBIENT = getRegisteredSoundEvent("entity.donkey.ambient");
  
  public static final SoundEvent ENTITY_DONKEY_ANGRY = getRegisteredSoundEvent("entity.donkey.angry");
  
  public static final SoundEvent ENTITY_DONKEY_CHEST = getRegisteredSoundEvent("entity.donkey.chest");
  
  public static final SoundEvent ENTITY_DONKEY_DEATH = getRegisteredSoundEvent("entity.donkey.death");
  
  public static final SoundEvent ENTITY_DONKEY_HURT = getRegisteredSoundEvent("entity.donkey.hurt");
  
  public static final SoundEvent ENTITY_EGG_THROW = getRegisteredSoundEvent("entity.egg.throw");
  
  public static final SoundEvent ENTITY_ELDER_GUARDIAN_AMBIENT = getRegisteredSoundEvent("entity.elder_guardian.ambient");
  
  public static final SoundEvent ENTITY_ELDERGUARDIAN_AMBIENTLAND = getRegisteredSoundEvent("entity.elder_guardian.ambient_land");
  
  public static final SoundEvent ENTITY_ELDER_GUARDIAN_CURSE = getRegisteredSoundEvent("entity.elder_guardian.curse");
  
  public static final SoundEvent ENTITY_ELDER_GUARDIAN_DEATH = getRegisteredSoundEvent("entity.elder_guardian.death");
  
  public static final SoundEvent ENTITY_ELDER_GUARDIAN_DEATH_LAND = getRegisteredSoundEvent("entity.elder_guardian.death_land");
  
  public static final SoundEvent field_191240_aK = getRegisteredSoundEvent("entity.elder_guardian.flop");
  
  public static final SoundEvent ENTITY_ELDER_GUARDIAN_HURT = getRegisteredSoundEvent("entity.elder_guardian.hurt");
  
  public static final SoundEvent ENTITY_ELDER_GUARDIAN_HURT_LAND = getRegisteredSoundEvent("entity.elder_guardian.hurt_land");
  
  public static final SoundEvent ITEM_ELYTRA_FLYING = getRegisteredSoundEvent("item.elytra.flying");
  
  public static final SoundEvent BLOCK_ENCHANTMENT_TABLE_USE = getRegisteredSoundEvent("block.enchantment_table.use");
  
  public static final SoundEvent BLOCK_ENDERCHEST_CLOSE = getRegisteredSoundEvent("block.enderchest.close");
  
  public static final SoundEvent BLOCK_ENDERCHEST_OPEN = getRegisteredSoundEvent("block.enderchest.open");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_AMBIENT = getRegisteredSoundEvent("entity.enderdragon.ambient");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_DEATH = getRegisteredSoundEvent("entity.enderdragon.death");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_FIREBALL_EPLD = getRegisteredSoundEvent("entity.enderdragon_fireball.explode");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_FLAP = getRegisteredSoundEvent("entity.enderdragon.flap");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_GROWL = getRegisteredSoundEvent("entity.enderdragon.growl");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_HURT = getRegisteredSoundEvent("entity.enderdragon.hurt");
  
  public static final SoundEvent ENTITY_ENDERDRAGON_SHOOT = getRegisteredSoundEvent("entity.enderdragon.shoot");
  
  public static final SoundEvent field_193777_bb = getRegisteredSoundEvent("entity.endereye.death");
  
  public static final SoundEvent ENTITY_ENDEREYE_LAUNCH = getRegisteredSoundEvent("entity.endereye.launch");
  
  public static final SoundEvent ENTITY_ENDERMEN_AMBIENT = getRegisteredSoundEvent("entity.endermen.ambient");
  
  public static final SoundEvent ENTITY_ENDERMEN_DEATH = getRegisteredSoundEvent("entity.endermen.death");
  
  public static final SoundEvent ENTITY_ENDERMEN_HURT = getRegisteredSoundEvent("entity.endermen.hurt");
  
  public static final SoundEvent ENTITY_ENDERMEN_SCREAM = getRegisteredSoundEvent("entity.endermen.scream");
  
  public static final SoundEvent ENTITY_ENDERMEN_STARE = getRegisteredSoundEvent("entity.endermen.stare");
  
  public static final SoundEvent ENTITY_ENDERMEN_TELEPORT = getRegisteredSoundEvent("entity.endermen.teleport");
  
  public static final SoundEvent ENTITY_ENDERMITE_AMBIENT = getRegisteredSoundEvent("entity.endermite.ambient");
  
  public static final SoundEvent ENTITY_ENDERMITE_DEATH = getRegisteredSoundEvent("entity.endermite.death");
  
  public static final SoundEvent ENTITY_ENDERMITE_HURT = getRegisteredSoundEvent("entity.endermite.hurt");
  
  public static final SoundEvent ENTITY_ENDERMITE_STEP = getRegisteredSoundEvent("entity.endermite.step");
  
  public static final SoundEvent ENTITY_ENDERPEARL_THROW = getRegisteredSoundEvent("entity.enderpearl.throw");
  
  public static final SoundEvent BLOCK_END_GATEWAY_SPAWN = getRegisteredSoundEvent("block.end_gateway.spawn");
  
  public static final SoundEvent field_193781_bp = getRegisteredSoundEvent("block.end_portal_frame.fill");
  
  public static final SoundEvent field_193782_bq = getRegisteredSoundEvent("block.end_portal.spawn");
  
  public static final SoundEvent field_191242_bl = getRegisteredSoundEvent("entity.evocation_fangs.attack");
  
  public static final SoundEvent field_191243_bm = getRegisteredSoundEvent("entity.evocation_illager.ambient");
  
  public static final SoundEvent field_191244_bn = getRegisteredSoundEvent("entity.evocation_illager.cast_spell");
  
  public static final SoundEvent field_191245_bo = getRegisteredSoundEvent("entity.evocation_illager.death");
  
  public static final SoundEvent field_191246_bp = getRegisteredSoundEvent("entity.evocation_illager.hurt");
  
  public static final SoundEvent field_191247_bq = getRegisteredSoundEvent("entity.evocation_illager.prepare_attack");
  
  public static final SoundEvent field_191248_br = getRegisteredSoundEvent("entity.evocation_illager.prepare_summon");
  
  public static final SoundEvent field_191249_bs = getRegisteredSoundEvent("entity.evocation_illager.prepare_wololo");
  
  public static final SoundEvent ENTITY_EXPERIENCE_BOTTLE_THROW = getRegisteredSoundEvent("entity.experience_bottle.throw");
  
  public static final SoundEvent ENTITY_EXPERIENCE_ORB_PICKUP = getRegisteredSoundEvent("entity.experience_orb.pickup");
  
  public static final SoundEvent BLOCK_FENCE_GATE_CLOSE = getRegisteredSoundEvent("block.fence_gate.close");
  
  public static final SoundEvent BLOCK_FENCE_GATE_OPEN = getRegisteredSoundEvent("block.fence_gate.open");
  
  public static final SoundEvent ITEM_FIRECHARGE_USE = getRegisteredSoundEvent("item.firecharge.use");
  
  public static final SoundEvent ENTITY_FIREWORK_BLAST = getRegisteredSoundEvent("entity.firework.blast");
  
  public static final SoundEvent ENTITY_FIREWORK_BLAST_FAR = getRegisteredSoundEvent("entity.firework.blast_far");
  
  public static final SoundEvent ENTITY_FIREWORK_LARGE_BLAST = getRegisteredSoundEvent("entity.firework.large_blast");
  
  public static final SoundEvent ENTITY_FIREWORK_LARGE_BLAST_FAR = getRegisteredSoundEvent("entity.firework.large_blast_far");
  
  public static final SoundEvent ENTITY_FIREWORK_LAUNCH = getRegisteredSoundEvent("entity.firework.launch");
  
  public static final SoundEvent ENTITY_FIREWORK_SHOOT = getRegisteredSoundEvent("entity.firework.shoot");
  
  public static final SoundEvent ENTITY_FIREWORK_TWINKLE = getRegisteredSoundEvent("entity.firework.twinkle");
  
  public static final SoundEvent ENTITY_FIREWORK_TWINKLE_FAR = getRegisteredSoundEvent("entity.firework.twinkle_far");
  
  public static final SoundEvent BLOCK_FIRE_AMBIENT = getRegisteredSoundEvent("block.fire.ambient");
  
  public static final SoundEvent BLOCK_FIRE_EXTINGUISH = getRegisteredSoundEvent("block.fire.extinguish");
  
  public static final SoundEvent ITEM_FLINTANDSTEEL_USE = getRegisteredSoundEvent("item.flintandsteel.use");
  
  public static final SoundEvent BLOCK_FURNACE_FIRE_CRACKLE = getRegisteredSoundEvent("block.furnace.fire_crackle");
  
  public static final SoundEvent ENTITY_GENERIC_BIG_FALL = getRegisteredSoundEvent("entity.generic.big_fall");
  
  public static final SoundEvent ENTITY_GENERIC_BURN = getRegisteredSoundEvent("entity.generic.burn");
  
  public static final SoundEvent ENTITY_GENERIC_DEATH = getRegisteredSoundEvent("entity.generic.death");
  
  public static final SoundEvent ENTITY_GENERIC_DRINK = getRegisteredSoundEvent("entity.generic.drink");
  
  public static final SoundEvent ENTITY_GENERIC_EAT = getRegisteredSoundEvent("entity.generic.eat");
  
  public static final SoundEvent ENTITY_GENERIC_EXPLODE = getRegisteredSoundEvent("entity.generic.explode");
  
  public static final SoundEvent ENTITY_GENERIC_EXTINGUISH_FIRE = getRegisteredSoundEvent("entity.generic.extinguish_fire");
  
  public static final SoundEvent ENTITY_GENERIC_HURT = getRegisteredSoundEvent("entity.generic.hurt");
  
  public static final SoundEvent ENTITY_GENERIC_SMALL_FALL = getRegisteredSoundEvent("entity.generic.small_fall");
  
  public static final SoundEvent ENTITY_GENERIC_SPLASH = getRegisteredSoundEvent("entity.generic.splash");
  
  public static final SoundEvent ENTITY_GENERIC_SWIM = getRegisteredSoundEvent("entity.generic.swim");
  
  public static final SoundEvent ENTITY_GHAST_AMBIENT = getRegisteredSoundEvent("entity.ghast.ambient");
  
  public static final SoundEvent ENTITY_GHAST_DEATH = getRegisteredSoundEvent("entity.ghast.death");
  
  public static final SoundEvent ENTITY_GHAST_HURT = getRegisteredSoundEvent("entity.ghast.hurt");
  
  public static final SoundEvent ENTITY_GHAST_SCREAM = getRegisteredSoundEvent("entity.ghast.scream");
  
  public static final SoundEvent ENTITY_GHAST_SHOOT = getRegisteredSoundEvent("entity.ghast.shoot");
  
  public static final SoundEvent ENTITY_GHAST_WARN = getRegisteredSoundEvent("entity.ghast.warn");
  
  public static final SoundEvent BLOCK_GLASS_BREAK = getRegisteredSoundEvent("block.glass.break");
  
  public static final SoundEvent BLOCK_GLASS_FALL = getRegisteredSoundEvent("block.glass.fall");
  
  public static final SoundEvent BLOCK_GLASS_HIT = getRegisteredSoundEvent("block.glass.hit");
  
  public static final SoundEvent BLOCK_GLASS_PLACE = getRegisteredSoundEvent("block.glass.place");
  
  public static final SoundEvent BLOCK_GLASS_STEP = getRegisteredSoundEvent("block.glass.step");
  
  public static final SoundEvent BLOCK_GRASS_BREAK = getRegisteredSoundEvent("block.grass.break");
  
  public static final SoundEvent BLOCK_GRASS_FALL = getRegisteredSoundEvent("block.grass.fall");
  
  public static final SoundEvent BLOCK_GRASS_HIT = getRegisteredSoundEvent("block.grass.hit");
  
  public static final SoundEvent BLOCK_GRASS_PLACE = getRegisteredSoundEvent("block.grass.place");
  
  public static final SoundEvent BLOCK_GRASS_STEP = getRegisteredSoundEvent("block.grass.step");
  
  public static final SoundEvent BLOCK_GRAVEL_BREAK = getRegisteredSoundEvent("block.gravel.break");
  
  public static final SoundEvent BLOCK_GRAVEL_FALL = getRegisteredSoundEvent("block.gravel.fall");
  
  public static final SoundEvent BLOCK_GRAVEL_HIT = getRegisteredSoundEvent("block.gravel.hit");
  
  public static final SoundEvent BLOCK_GRAVEL_PLACE = getRegisteredSoundEvent("block.gravel.place");
  
  public static final SoundEvent BLOCK_GRAVEL_STEP = getRegisteredSoundEvent("block.gravel.step");
  
  public static final SoundEvent ENTITY_GUARDIAN_AMBIENT = getRegisteredSoundEvent("entity.guardian.ambient");
  
  public static final SoundEvent ENTITY_GUARDIAN_AMBIENT_LAND = getRegisteredSoundEvent("entity.guardian.ambient_land");
  
  public static final SoundEvent ENTITY_GUARDIAN_ATTACK = getRegisteredSoundEvent("entity.guardian.attack");
  
  public static final SoundEvent ENTITY_GUARDIAN_DEATH = getRegisteredSoundEvent("entity.guardian.death");
  
  public static final SoundEvent ENTITY_GUARDIAN_DEATH_LAND = getRegisteredSoundEvent("entity.guardian.death_land");
  
  public static final SoundEvent ENTITY_GUARDIAN_FLOP = getRegisteredSoundEvent("entity.guardian.flop");
  
  public static final SoundEvent ENTITY_GUARDIAN_HURT = getRegisteredSoundEvent("entity.guardian.hurt");
  
  public static final SoundEvent ENTITY_GUARDIAN_HURT_LAND = getRegisteredSoundEvent("entity.guardian.hurt_land");
  
  public static final SoundEvent ITEM_HOE_TILL = getRegisteredSoundEvent("item.hoe.till");
  
  public static final SoundEvent ENTITY_HORSE_AMBIENT = getRegisteredSoundEvent("entity.horse.ambient");
  
  public static final SoundEvent ENTITY_HORSE_ANGRY = getRegisteredSoundEvent("entity.horse.angry");
  
  public static final SoundEvent ENTITY_HORSE_ARMOR = getRegisteredSoundEvent("entity.horse.armor");
  
  public static final SoundEvent ENTITY_HORSE_BREATHE = getRegisteredSoundEvent("entity.horse.breathe");
  
  public static final SoundEvent ENTITY_HORSE_DEATH = getRegisteredSoundEvent("entity.horse.death");
  
  public static final SoundEvent ENTITY_HORSE_EAT = getRegisteredSoundEvent("entity.horse.eat");
  
  public static final SoundEvent ENTITY_HORSE_GALLOP = getRegisteredSoundEvent("entity.horse.gallop");
  
  public static final SoundEvent ENTITY_HORSE_HURT = getRegisteredSoundEvent("entity.horse.hurt");
  
  public static final SoundEvent ENTITY_HORSE_JUMP = getRegisteredSoundEvent("entity.horse.jump");
  
  public static final SoundEvent ENTITY_HORSE_LAND = getRegisteredSoundEvent("entity.horse.land");
  
  public static final SoundEvent ENTITY_HORSE_SADDLE = getRegisteredSoundEvent("entity.horse.saddle");
  
  public static final SoundEvent ENTITY_HORSE_STEP = getRegisteredSoundEvent("entity.horse.step");
  
  public static final SoundEvent ENTITY_HORSE_STEP_WOOD = getRegisteredSoundEvent("entity.horse.step_wood");
  
  public static final SoundEvent ENTITY_HOSTILE_BIG_FALL = getRegisteredSoundEvent("entity.hostile.big_fall");
  
  public static final SoundEvent ENTITY_HOSTILE_DEATH = getRegisteredSoundEvent("entity.hostile.death");
  
  public static final SoundEvent ENTITY_HOSTILE_HURT = getRegisteredSoundEvent("entity.hostile.hurt");
  
  public static final SoundEvent ENTITY_HOSTILE_SMALL_FALL = getRegisteredSoundEvent("entity.hostile.small_fall");
  
  public static final SoundEvent ENTITY_HOSTILE_SPLASH = getRegisteredSoundEvent("entity.hostile.splash");
  
  public static final SoundEvent ENTITY_HOSTILE_SWIM = getRegisteredSoundEvent("entity.hostile.swim");
  
  public static final SoundEvent ENTITY_HUSK_AMBIENT = getRegisteredSoundEvent("entity.husk.ambient");
  
  public static final SoundEvent ENTITY_HUSK_DEATH = getRegisteredSoundEvent("entity.husk.death");
  
  public static final SoundEvent ENTITY_HUSK_HURT = getRegisteredSoundEvent("entity.husk.hurt");
  
  public static final SoundEvent ENTITY_HUSK_STEP = getRegisteredSoundEvent("entity.husk.step");
  
  public static final SoundEvent field_193783_dc = getRegisteredSoundEvent("entity.illusion_illager.ambient");
  
  public static final SoundEvent field_193784_dd = getRegisteredSoundEvent("entity.illusion_illager.cast_spell");
  
  public static final SoundEvent field_193786_de = getRegisteredSoundEvent("entity.illusion_illager.death");
  
  public static final SoundEvent field_193787_df = getRegisteredSoundEvent("entity.illusion_illager.hurt");
  
  public static final SoundEvent field_193788_dg = getRegisteredSoundEvent("entity.illusion_illager.mirror_move");
  
  public static final SoundEvent field_193789_dh = getRegisteredSoundEvent("entity.illusion_illager.prepare_blindness");
  
  public static final SoundEvent field_193790_di = getRegisteredSoundEvent("entity.illusion_illager.prepare_mirror");
  
  public static final SoundEvent ENTITY_IRONGOLEM_ATTACK = getRegisteredSoundEvent("entity.irongolem.attack");
  
  public static final SoundEvent ENTITY_IRONGOLEM_DEATH = getRegisteredSoundEvent("entity.irongolem.death");
  
  public static final SoundEvent ENTITY_IRONGOLEM_HURT = getRegisteredSoundEvent("entity.irongolem.hurt");
  
  public static final SoundEvent ENTITY_IRONGOLEM_STEP = getRegisteredSoundEvent("entity.irongolem.step");
  
  public static final SoundEvent BLOCK_IRON_DOOR_CLOSE = getRegisteredSoundEvent("block.iron_door.close");
  
  public static final SoundEvent BLOCK_IRON_DOOR_OPEN = getRegisteredSoundEvent("block.iron_door.open");
  
  public static final SoundEvent BLOCK_IRON_TRAPDOOR_CLOSE = getRegisteredSoundEvent("block.iron_trapdoor.close");
  
  public static final SoundEvent BLOCK_IRON_TRAPDOOR_OPEN = getRegisteredSoundEvent("block.iron_trapdoor.open");
  
  public static final SoundEvent ENTITY_ITEMFRAME_ADD_ITEM = getRegisteredSoundEvent("entity.itemframe.add_item");
  
  public static final SoundEvent ENTITY_ITEMFRAME_BREAK = getRegisteredSoundEvent("entity.itemframe.break");
  
  public static final SoundEvent ENTITY_ITEMFRAME_PLACE = getRegisteredSoundEvent("entity.itemframe.place");
  
  public static final SoundEvent ENTITY_ITEMFRAME_REMOVE_ITEM = getRegisteredSoundEvent("entity.itemframe.remove_item");
  
  public static final SoundEvent ENTITY_ITEMFRAME_ROTATE_ITEM = getRegisteredSoundEvent("entity.itemframe.rotate_item");
  
  public static final SoundEvent ENTITY_ITEM_BREAK = getRegisteredSoundEvent("entity.item.break");
  
  public static final SoundEvent ENTITY_ITEM_PICKUP = getRegisteredSoundEvent("entity.item.pickup");
  
  public static final SoundEvent BLOCK_LADDER_BREAK = getRegisteredSoundEvent("block.ladder.break");
  
  public static final SoundEvent BLOCK_LADDER_FALL = getRegisteredSoundEvent("block.ladder.fall");
  
  public static final SoundEvent BLOCK_LADDER_HIT = getRegisteredSoundEvent("block.ladder.hit");
  
  public static final SoundEvent BLOCK_LADDER_PLACE = getRegisteredSoundEvent("block.ladder.place");
  
  public static final SoundEvent BLOCK_LADDER_STEP = getRegisteredSoundEvent("block.ladder.step");
  
  public static final SoundEvent BLOCK_LAVA_AMBIENT = getRegisteredSoundEvent("block.lava.ambient");
  
  public static final SoundEvent BLOCK_LAVA_EXTINGUISH = getRegisteredSoundEvent("block.lava.extinguish");
  
  public static final SoundEvent BLOCK_LAVA_POP = getRegisteredSoundEvent("block.lava.pop");
  
  public static final SoundEvent ENTITY_LEASHKNOT_BREAK = getRegisteredSoundEvent("entity.leashknot.break");
  
  public static final SoundEvent ENTITY_LEASHKNOT_PLACE = getRegisteredSoundEvent("entity.leashknot.place");
  
  public static final SoundEvent BLOCK_LEVER_CLICK = getRegisteredSoundEvent("block.lever.click");
  
  public static final SoundEvent ENTITY_LIGHTNING_IMPACT = getRegisteredSoundEvent("entity.lightning.impact");
  
  public static final SoundEvent ENTITY_LIGHTNING_THUNDER = getRegisteredSoundEvent("entity.lightning.thunder");
  
  public static final SoundEvent ENTITY_LINGERINGPOTION_THROW = getRegisteredSoundEvent("entity.lingeringpotion.throw");
  
  public static final SoundEvent field_191260_dz = getRegisteredSoundEvent("entity.llama.ambient");
  
  public static final SoundEvent field_191250_dA = getRegisteredSoundEvent("entity.llama.angry");
  
  public static final SoundEvent field_191251_dB = getRegisteredSoundEvent("entity.llama.chest");
  
  public static final SoundEvent field_191252_dC = getRegisteredSoundEvent("entity.llama.death");
  
  public static final SoundEvent field_191253_dD = getRegisteredSoundEvent("entity.llama.eat");
  
  public static final SoundEvent field_191254_dE = getRegisteredSoundEvent("entity.llama.hurt");
  
  public static final SoundEvent field_191255_dF = getRegisteredSoundEvent("entity.llama.spit");
  
  public static final SoundEvent field_191256_dG = getRegisteredSoundEvent("entity.llama.step");
  
  public static final SoundEvent field_191257_dH = getRegisteredSoundEvent("entity.llama.swag");
  
  public static final SoundEvent ENTITY_MAGMACUBE_DEATH = getRegisteredSoundEvent("entity.magmacube.death");
  
  public static final SoundEvent ENTITY_MAGMACUBE_HURT = getRegisteredSoundEvent("entity.magmacube.hurt");
  
  public static final SoundEvent ENTITY_MAGMACUBE_JUMP = getRegisteredSoundEvent("entity.magmacube.jump");
  
  public static final SoundEvent ENTITY_MAGMACUBE_SQUISH = getRegisteredSoundEvent("entity.magmacube.squish");
  
  public static final SoundEvent BLOCK_METAL_BREAK = getRegisteredSoundEvent("block.metal.break");
  
  public static final SoundEvent BLOCK_METAL_FALL = getRegisteredSoundEvent("block.metal.fall");
  
  public static final SoundEvent BLOCK_METAL_HIT = getRegisteredSoundEvent("block.metal.hit");
  
  public static final SoundEvent BLOCK_METAL_PLACE = getRegisteredSoundEvent("block.metal.place");
  
  public static final SoundEvent BLOCK_METAL_PRESSPLATE_CLICK_OFF = getRegisteredSoundEvent("block.metal_pressureplate.click_off");
  
  public static final SoundEvent BLOCK_METAL_PRESSPLATE_CLICK_ON = getRegisteredSoundEvent("block.metal_pressureplate.click_on");
  
  public static final SoundEvent BLOCK_METAL_STEP = getRegisteredSoundEvent("block.metal.step");
  
  public static final SoundEvent ENTITY_MINECART_INSIDE = getRegisteredSoundEvent("entity.minecart.inside");
  
  public static final SoundEvent ENTITY_MINECART_RIDING = getRegisteredSoundEvent("entity.minecart.riding");
  
  public static final SoundEvent ENTITY_MOOSHROOM_SHEAR = getRegisteredSoundEvent("entity.mooshroom.shear");
  
  public static final SoundEvent ENTITY_MULE_AMBIENT = getRegisteredSoundEvent("entity.mule.ambient");
  
  public static final SoundEvent field_191259_dX = getRegisteredSoundEvent("entity.mule.chest");
  
  public static final SoundEvent ENTITY_MULE_DEATH = getRegisteredSoundEvent("entity.mule.death");
  
  public static final SoundEvent ENTITY_MULE_HURT = getRegisteredSoundEvent("entity.mule.hurt");
  
  public static final SoundEvent MUSIC_CREATIVE = getRegisteredSoundEvent("music.creative");
  
  public static final SoundEvent MUSIC_CREDITS = getRegisteredSoundEvent("music.credits");
  
  public static final SoundEvent MUSIC_DRAGON = getRegisteredSoundEvent("music.dragon");
  
  public static final SoundEvent MUSIC_END = getRegisteredSoundEvent("music.end");
  
  public static final SoundEvent MUSIC_GAME = getRegisteredSoundEvent("music.game");
  
  public static final SoundEvent MUSIC_MENU = getRegisteredSoundEvent("music.menu");
  
  public static final SoundEvent MUSIC_NETHER = getRegisteredSoundEvent("music.nether");
  
  public static final SoundEvent BLOCK_NOTE_BASEDRUM = getRegisteredSoundEvent("block.note.basedrum");
  
  public static final SoundEvent BLOCK_NOTE_BASS = getRegisteredSoundEvent("block.note.bass");
  
  public static final SoundEvent field_193807_ew = getRegisteredSoundEvent("block.note.bell");
  
  public static final SoundEvent field_193808_ex = getRegisteredSoundEvent("block.note.chime");
  
  public static final SoundEvent field_193809_ey = getRegisteredSoundEvent("block.note.flute");
  
  public static final SoundEvent field_193810_ez = getRegisteredSoundEvent("block.note.guitar");
  
  public static final SoundEvent BLOCK_NOTE_HARP = getRegisteredSoundEvent("block.note.harp");
  
  public static final SoundEvent BLOCK_NOTE_HAT = getRegisteredSoundEvent("block.note.hat");
  
  public static final SoundEvent BLOCK_NOTE_PLING = getRegisteredSoundEvent("block.note.pling");
  
  public static final SoundEvent BLOCK_NOTE_SNARE = getRegisteredSoundEvent("block.note.snare");
  
  public static final SoundEvent field_193785_eE = getRegisteredSoundEvent("block.note.xylophone");
  
  public static final SoundEvent ENTITY_PAINTING_BREAK = getRegisteredSoundEvent("entity.painting.break");
  
  public static final SoundEvent ENTITY_PAINTING_PLACE = getRegisteredSoundEvent("entity.painting.place");
  
  public static final SoundEvent field_192792_ep = getRegisteredSoundEvent("entity.parrot.ambient");
  
  public static final SoundEvent field_192793_eq = getRegisteredSoundEvent("entity.parrot.death");
  
  public static final SoundEvent field_192797_eu = getRegisteredSoundEvent("entity.parrot.eat");
  
  public static final SoundEvent field_192796_et = getRegisteredSoundEvent("entity.parrot.fly");
  
  public static final SoundEvent field_192794_er = getRegisteredSoundEvent("entity.parrot.hurt");
  
  public static final SoundEvent field_193791_eM = getRegisteredSoundEvent("entity.parrot.imitate.blaze");
  
  public static final SoundEvent field_193792_eN = getRegisteredSoundEvent("entity.parrot.imitate.creeper");
  
  public static final SoundEvent field_193793_eO = getRegisteredSoundEvent("entity.parrot.imitate.elder_guardian");
  
  public static final SoundEvent field_193794_eP = getRegisteredSoundEvent("entity.parrot.imitate.enderdragon");
  
  public static final SoundEvent field_193795_eQ = getRegisteredSoundEvent("entity.parrot.imitate.enderman");
  
  public static final SoundEvent field_193796_eR = getRegisteredSoundEvent("entity.parrot.imitate.endermite");
  
  public static final SoundEvent field_193797_eS = getRegisteredSoundEvent("entity.parrot.imitate.evocation_illager");
  
  public static final SoundEvent field_193798_eT = getRegisteredSoundEvent("entity.parrot.imitate.ghast");
  
  public static final SoundEvent field_193799_eU = getRegisteredSoundEvent("entity.parrot.imitate.husk");
  
  public static final SoundEvent field_193800_eV = getRegisteredSoundEvent("entity.parrot.imitate.illusion_illager");
  
  public static final SoundEvent field_193801_eW = getRegisteredSoundEvent("entity.parrot.imitate.magmacube");
  
  public static final SoundEvent field_193802_eX = getRegisteredSoundEvent("entity.parrot.imitate.polar_bear");
  
  public static final SoundEvent field_193803_eY = getRegisteredSoundEvent("entity.parrot.imitate.shulker");
  
  public static final SoundEvent field_193804_eZ = getRegisteredSoundEvent("entity.parrot.imitate.silverfish");
  
  public static final SoundEvent field_193811_fa = getRegisteredSoundEvent("entity.parrot.imitate.skeleton");
  
  public static final SoundEvent field_193812_fb = getRegisteredSoundEvent("entity.parrot.imitate.slime");
  
  public static final SoundEvent field_193813_fc = getRegisteredSoundEvent("entity.parrot.imitate.spider");
  
  public static final SoundEvent field_193814_fd = getRegisteredSoundEvent("entity.parrot.imitate.stray");
  
  public static final SoundEvent field_193815_fe = getRegisteredSoundEvent("entity.parrot.imitate.vex");
  
  public static final SoundEvent field_193816_ff = getRegisteredSoundEvent("entity.parrot.imitate.vindication_illager");
  
  public static final SoundEvent field_193817_fg = getRegisteredSoundEvent("entity.parrot.imitate.witch");
  
  public static final SoundEvent field_193818_fh = getRegisteredSoundEvent("entity.parrot.imitate.wither");
  
  public static final SoundEvent field_193819_fi = getRegisteredSoundEvent("entity.parrot.imitate.wither_skeleton");
  
  public static final SoundEvent field_193820_fj = getRegisteredSoundEvent("entity.parrot.imitate.wolf");
  
  public static final SoundEvent field_193821_fk = getRegisteredSoundEvent("entity.parrot.imitate.zombie");
  
  public static final SoundEvent field_193822_fl = getRegisteredSoundEvent("entity.parrot.imitate.zombie_pigman");
  
  public static final SoundEvent field_193823_fm = getRegisteredSoundEvent("entity.parrot.imitate.zombie_villager");
  
  public static final SoundEvent field_192795_es = getRegisteredSoundEvent("entity.parrot.step");
  
  public static final SoundEvent ENTITY_PIG_AMBIENT = getRegisteredSoundEvent("entity.pig.ambient");
  
  public static final SoundEvent ENTITY_PIG_DEATH = getRegisteredSoundEvent("entity.pig.death");
  
  public static final SoundEvent ENTITY_PIG_HURT = getRegisteredSoundEvent("entity.pig.hurt");
  
  public static final SoundEvent ENTITY_PIG_SADDLE = getRegisteredSoundEvent("entity.pig.saddle");
  
  public static final SoundEvent ENTITY_PIG_STEP = getRegisteredSoundEvent("entity.pig.step");
  
  public static final SoundEvent BLOCK_PISTON_CONTRACT = getRegisteredSoundEvent("block.piston.contract");
  
  public static final SoundEvent BLOCK_PISTON_EXTEND = getRegisteredSoundEvent("block.piston.extend");
  
  public static final SoundEvent ENTITY_PLAYER_ATTACK_CRIT = getRegisteredSoundEvent("entity.player.attack.crit");
  
  public static final SoundEvent ENTITY_PLAYER_ATTACK_KNOCKBACK = getRegisteredSoundEvent("entity.player.attack.knockback");
  
  public static final SoundEvent ENTITY_PLAYER_ATTACK_NODAMAGE = getRegisteredSoundEvent("entity.player.attack.nodamage");
  
  public static final SoundEvent ENTITY_PLAYER_ATTACK_STRONG = getRegisteredSoundEvent("entity.player.attack.strong");
  
  public static final SoundEvent ENTITY_PLAYER_ATTACK_SWEEP = getRegisteredSoundEvent("entity.player.attack.sweep");
  
  public static final SoundEvent ENTITY_PLAYER_ATTACK_WEAK = getRegisteredSoundEvent("entity.player.attack.weak");
  
  public static final SoundEvent ENTITY_PLAYER_BIG_FALL = getRegisteredSoundEvent("entity.player.big_fall");
  
  public static final SoundEvent ENTITY_PLAYER_BREATH = getRegisteredSoundEvent("entity.player.breath");
  
  public static final SoundEvent ENTITY_PLAYER_BURP = getRegisteredSoundEvent("entity.player.burp");
  
  public static final SoundEvent ENTITY_PLAYER_DEATH = getRegisteredSoundEvent("entity.player.death");
  
  public static final SoundEvent ENTITY_PLAYER_HURT = getRegisteredSoundEvent("entity.player.hurt");
  
  public static final SoundEvent field_193805_fG = getRegisteredSoundEvent("entity.player.hurt_drown");
  
  public static final SoundEvent field_193806_fH = getRegisteredSoundEvent("entity.player.hurt_on_fire");
  
  public static final SoundEvent ENTITY_PLAYER_LEVELUP = getRegisteredSoundEvent("entity.player.levelup");
  
  public static final SoundEvent ENTITY_PLAYER_SMALL_FALL = getRegisteredSoundEvent("entity.player.small_fall");
  
  public static final SoundEvent ENTITY_PLAYER_SPLASH = getRegisteredSoundEvent("entity.player.splash");
  
  public static final SoundEvent ENTITY_PLAYER_SWIM = getRegisteredSoundEvent("entity.player.swim");
  
  public static final SoundEvent ENTITY_POLAR_BEAR_AMBIENT = getRegisteredSoundEvent("entity.polar_bear.ambient");
  
  public static final SoundEvent ENTITY_POLAR_BEAR_BABY_AMBIENT = getRegisteredSoundEvent("entity.polar_bear.baby_ambient");
  
  public static final SoundEvent ENTITY_POLAR_BEAR_DEATH = getRegisteredSoundEvent("entity.polar_bear.death");
  
  public static final SoundEvent ENTITY_POLAR_BEAR_HURT = getRegisteredSoundEvent("entity.polar_bear.hurt");
  
  public static final SoundEvent ENTITY_POLAR_BEAR_STEP = getRegisteredSoundEvent("entity.polar_bear.step");
  
  public static final SoundEvent ENTITY_POLAR_BEAR_WARNING = getRegisteredSoundEvent("entity.polar_bear.warning");
  
  public static final SoundEvent BLOCK_PORTAL_AMBIENT = getRegisteredSoundEvent("block.portal.ambient");
  
  public static final SoundEvent BLOCK_PORTAL_TRAVEL = getRegisteredSoundEvent("block.portal.travel");
  
  public static final SoundEvent BLOCK_PORTAL_TRIGGER = getRegisteredSoundEvent("block.portal.trigger");
  
  public static final SoundEvent ENTITY_RABBIT_AMBIENT = getRegisteredSoundEvent("entity.rabbit.ambient");
  
  public static final SoundEvent ENTITY_RABBIT_ATTACK = getRegisteredSoundEvent("entity.rabbit.attack");
  
  public static final SoundEvent ENTITY_RABBIT_DEATH = getRegisteredSoundEvent("entity.rabbit.death");
  
  public static final SoundEvent ENTITY_RABBIT_HURT = getRegisteredSoundEvent("entity.rabbit.hurt");
  
  public static final SoundEvent ENTITY_RABBIT_JUMP = getRegisteredSoundEvent("entity.rabbit.jump");
  
  public static final SoundEvent RECORD_11 = getRegisteredSoundEvent("record.11");
  
  public static final SoundEvent RECORD_13 = getRegisteredSoundEvent("record.13");
  
  public static final SoundEvent RECORD_BLOCKS = getRegisteredSoundEvent("record.blocks");
  
  public static final SoundEvent RECORD_CAT = getRegisteredSoundEvent("record.cat");
  
  public static final SoundEvent RECORD_CHIRP = getRegisteredSoundEvent("record.chirp");
  
  public static final SoundEvent RECORD_FAR = getRegisteredSoundEvent("record.far");
  
  public static final SoundEvent RECORD_MALL = getRegisteredSoundEvent("record.mall");
  
  public static final SoundEvent RECORD_MELLOHI = getRegisteredSoundEvent("record.mellohi");
  
  public static final SoundEvent RECORD_STAL = getRegisteredSoundEvent("record.stal");
  
  public static final SoundEvent RECORD_STRAD = getRegisteredSoundEvent("record.strad");
  
  public static final SoundEvent RECORD_WAIT = getRegisteredSoundEvent("record.wait");
  
  public static final SoundEvent RECORD_WARD = getRegisteredSoundEvent("record.ward");
  
  public static final SoundEvent BLOCK_REDSTONE_TORCH_BURNOUT = getRegisteredSoundEvent("block.redstone_torch.burnout");
  
  public static final SoundEvent BLOCK_SAND_BREAK = getRegisteredSoundEvent("block.sand.break");
  
  public static final SoundEvent BLOCK_SAND_FALL = getRegisteredSoundEvent("block.sand.fall");
  
  public static final SoundEvent BLOCK_SAND_HIT = getRegisteredSoundEvent("block.sand.hit");
  
  public static final SoundEvent BLOCK_SAND_PLACE = getRegisteredSoundEvent("block.sand.place");
  
  public static final SoundEvent BLOCK_SAND_STEP = getRegisteredSoundEvent("block.sand.step");
  
  public static final SoundEvent ENTITY_SHEEP_AMBIENT = getRegisteredSoundEvent("entity.sheep.ambient");
  
  public static final SoundEvent ENTITY_SHEEP_DEATH = getRegisteredSoundEvent("entity.sheep.death");
  
  public static final SoundEvent ENTITY_SHEEP_HURT = getRegisteredSoundEvent("entity.sheep.hurt");
  
  public static final SoundEvent ENTITY_SHEEP_SHEAR = getRegisteredSoundEvent("entity.sheep.shear");
  
  public static final SoundEvent ENTITY_SHEEP_STEP = getRegisteredSoundEvent("entity.sheep.step");
  
  public static final SoundEvent ITEM_SHIELD_BLOCK = getRegisteredSoundEvent("item.shield.block");
  
  public static final SoundEvent ITEM_SHIELD_BREAK = getRegisteredSoundEvent("item.shield.break");
  
  public static final SoundEvent ITEM_SHOVEL_FLATTEN = getRegisteredSoundEvent("item.shovel.flatten");
  
  public static final SoundEvent ENTITY_SHULKER_AMBIENT = getRegisteredSoundEvent("entity.shulker.ambient");
  
  public static final SoundEvent field_191261_fA = getRegisteredSoundEvent("block.shulker_box.close");
  
  public static final SoundEvent field_191262_fB = getRegisteredSoundEvent("block.shulker_box.open");
  
  public static final SoundEvent ENTITY_SHULKER_BULLET_HIT = getRegisteredSoundEvent("entity.shulker_bullet.hit");
  
  public static final SoundEvent ENTITY_SHULKER_BULLET_HURT = getRegisteredSoundEvent("entity.shulker_bullet.hurt");
  
  public static final SoundEvent ENTITY_SHULKER_CLOSE = getRegisteredSoundEvent("entity.shulker.close");
  
  public static final SoundEvent ENTITY_SHULKER_DEATH = getRegisteredSoundEvent("entity.shulker.death");
  
  public static final SoundEvent ENTITY_SHULKER_HURT = getRegisteredSoundEvent("entity.shulker.hurt");
  
  public static final SoundEvent ENTITY_SHULKER_HURT_CLOSED = getRegisteredSoundEvent("entity.shulker.hurt_closed");
  
  public static final SoundEvent ENTITY_SHULKER_OPEN = getRegisteredSoundEvent("entity.shulker.open");
  
  public static final SoundEvent ENTITY_SHULKER_SHOOT = getRegisteredSoundEvent("entity.shulker.shoot");
  
  public static final SoundEvent ENTITY_SHULKER_TELEPORT = getRegisteredSoundEvent("entity.shulker.teleport");
  
  public static final SoundEvent ENTITY_SILVERFISH_AMBIENT = getRegisteredSoundEvent("entity.silverfish.ambient");
  
  public static final SoundEvent ENTITY_SILVERFISH_DEATH = getRegisteredSoundEvent("entity.silverfish.death");
  
  public static final SoundEvent ENTITY_SILVERFISH_HURT = getRegisteredSoundEvent("entity.silverfish.hurt");
  
  public static final SoundEvent ENTITY_SILVERFISH_STEP = getRegisteredSoundEvent("entity.silverfish.step");
  
  public static final SoundEvent ENTITY_SKELETON_AMBIENT = getRegisteredSoundEvent("entity.skeleton.ambient");
  
  public static final SoundEvent ENTITY_SKELETON_DEATH = getRegisteredSoundEvent("entity.skeleton.death");
  
  public static final SoundEvent ENTITY_SKELETON_HORSE_AMBIENT = getRegisteredSoundEvent("entity.skeleton_horse.ambient");
  
  public static final SoundEvent ENTITY_SKELETON_HORSE_DEATH = getRegisteredSoundEvent("entity.skeleton_horse.death");
  
  public static final SoundEvent ENTITY_SKELETON_HORSE_HURT = getRegisteredSoundEvent("entity.skeleton_horse.hurt");
  
  public static final SoundEvent ENTITY_SKELETON_HURT = getRegisteredSoundEvent("entity.skeleton.hurt");
  
  public static final SoundEvent ENTITY_SKELETON_SHOOT = getRegisteredSoundEvent("entity.skeleton.shoot");
  
  public static final SoundEvent ENTITY_SKELETON_STEP = getRegisteredSoundEvent("entity.skeleton.step");
  
  public static final SoundEvent ENTITY_SLIME_ATTACK = getRegisteredSoundEvent("entity.slime.attack");
  
  public static final SoundEvent BLOCK_SLIME_BREAK = getRegisteredSoundEvent("block.slime.break");
  
  public static final SoundEvent ENTITY_SLIME_DEATH = getRegisteredSoundEvent("entity.slime.death");
  
  public static final SoundEvent BLOCK_SLIME_FALL = getRegisteredSoundEvent("block.slime.fall");
  
  public static final SoundEvent BLOCK_SLIME_HIT = getRegisteredSoundEvent("block.slime.hit");
  
  public static final SoundEvent ENTITY_SLIME_HURT = getRegisteredSoundEvent("entity.slime.hurt");
  
  public static final SoundEvent ENTITY_SLIME_JUMP = getRegisteredSoundEvent("entity.slime.jump");
  
  public static final SoundEvent BLOCK_SLIME_PLACE = getRegisteredSoundEvent("block.slime.place");
  
  public static final SoundEvent ENTITY_SLIME_SQUISH = getRegisteredSoundEvent("entity.slime.squish");
  
  public static final SoundEvent BLOCK_SLIME_STEP = getRegisteredSoundEvent("block.slime.step");
  
  public static final SoundEvent ENTITY_SMALL_MAGMACUBE_DEATH = getRegisteredSoundEvent("entity.small_magmacube.death");
  
  public static final SoundEvent ENTITY_SMALL_MAGMACUBE_HURT = getRegisteredSoundEvent("entity.small_magmacube.hurt");
  
  public static final SoundEvent ENTITY_SMALL_MAGMACUBE_SQUISH = getRegisteredSoundEvent("entity.small_magmacube.squish");
  
  public static final SoundEvent ENTITY_SMALL_SLIME_DEATH = getRegisteredSoundEvent("entity.small_slime.death");
  
  public static final SoundEvent ENTITY_SMALL_SLIME_HURT = getRegisteredSoundEvent("entity.small_slime.hurt");
  
  public static final SoundEvent ENTITY_SMALL_SLIME_JUMP = getRegisteredSoundEvent("entity.small_slime.jump");
  
  public static final SoundEvent ENTITY_SMALL_SLIME_SQUISH = getRegisteredSoundEvent("entity.small_slime.squish");
  
  public static final SoundEvent ENTITY_SNOWBALL_THROW = getRegisteredSoundEvent("entity.snowball.throw");
  
  public static final SoundEvent ENTITY_SNOWMAN_AMBIENT = getRegisteredSoundEvent("entity.snowman.ambient");
  
  public static final SoundEvent ENTITY_SNOWMAN_DEATH = getRegisteredSoundEvent("entity.snowman.death");
  
  public static final SoundEvent ENTITY_SNOWMAN_HURT = getRegisteredSoundEvent("entity.snowman.hurt");
  
  public static final SoundEvent ENTITY_SNOWMAN_SHOOT = getRegisteredSoundEvent("entity.snowman.shoot");
  
  public static final SoundEvent BLOCK_SNOW_BREAK = getRegisteredSoundEvent("block.snow.break");
  
  public static final SoundEvent BLOCK_SNOW_FALL = getRegisteredSoundEvent("block.snow.fall");
  
  public static final SoundEvent BLOCK_SNOW_HIT = getRegisteredSoundEvent("block.snow.hit");
  
  public static final SoundEvent BLOCK_SNOW_PLACE = getRegisteredSoundEvent("block.snow.place");
  
  public static final SoundEvent BLOCK_SNOW_STEP = getRegisteredSoundEvent("block.snow.step");
  
  public static final SoundEvent ENTITY_SPIDER_AMBIENT = getRegisteredSoundEvent("entity.spider.ambient");
  
  public static final SoundEvent ENTITY_SPIDER_DEATH = getRegisteredSoundEvent("entity.spider.death");
  
  public static final SoundEvent ENTITY_SPIDER_HURT = getRegisteredSoundEvent("entity.spider.hurt");
  
  public static final SoundEvent ENTITY_SPIDER_STEP = getRegisteredSoundEvent("entity.spider.step");
  
  public static final SoundEvent ENTITY_SPLASH_POTION_BREAK = getRegisteredSoundEvent("entity.splash_potion.break");
  
  public static final SoundEvent ENTITY_SPLASH_POTION_THROW = getRegisteredSoundEvent("entity.splash_potion.throw");
  
  public static final SoundEvent ENTITY_SQUID_AMBIENT = getRegisteredSoundEvent("entity.squid.ambient");
  
  public static final SoundEvent ENTITY_SQUID_DEATH = getRegisteredSoundEvent("entity.squid.death");
  
  public static final SoundEvent ENTITY_SQUID_HURT = getRegisteredSoundEvent("entity.squid.hurt");
  
  public static final SoundEvent BLOCK_STONE_BREAK = getRegisteredSoundEvent("block.stone.break");
  
  public static final SoundEvent BLOCK_STONE_BUTTON_CLICK_OFF = getRegisteredSoundEvent("block.stone_button.click_off");
  
  public static final SoundEvent BLOCK_STONE_BUTTON_CLICK_ON = getRegisteredSoundEvent("block.stone_button.click_on");
  
  public static final SoundEvent BLOCK_STONE_FALL = getRegisteredSoundEvent("block.stone.fall");
  
  public static final SoundEvent BLOCK_STONE_HIT = getRegisteredSoundEvent("block.stone.hit");
  
  public static final SoundEvent BLOCK_STONE_PLACE = getRegisteredSoundEvent("block.stone.place");
  
  public static final SoundEvent BLOCK_STONE_PRESSPLATE_CLICK_OFF = getRegisteredSoundEvent("block.stone_pressureplate.click_off");
  
  public static final SoundEvent BLOCK_STONE_PRESSPLATE_CLICK_ON = getRegisteredSoundEvent("block.stone_pressureplate.click_on");
  
  public static final SoundEvent BLOCK_STONE_STEP = getRegisteredSoundEvent("block.stone.step");
  
  public static final SoundEvent ENTITY_STRAY_AMBIENT = getRegisteredSoundEvent("entity.stray.ambient");
  
  public static final SoundEvent ENTITY_STRAY_DEATH = getRegisteredSoundEvent("entity.stray.death");
  
  public static final SoundEvent ENTITY_STRAY_HURT = getRegisteredSoundEvent("entity.stray.hurt");
  
  public static final SoundEvent ENTITY_STRAY_STEP = getRegisteredSoundEvent("entity.stray.step");
  
  public static final SoundEvent ENCHANT_THORNS_HIT = getRegisteredSoundEvent("enchant.thorns.hit");
  
  public static final SoundEvent ENTITY_TNT_PRIMED = getRegisteredSoundEvent("entity.tnt.primed");
  
  public static final SoundEvent field_191263_gW = getRegisteredSoundEvent("item.totem.use");
  
  public static final SoundEvent BLOCK_TRIPWIRE_ATTACH = getRegisteredSoundEvent("block.tripwire.attach");
  
  public static final SoundEvent BLOCK_TRIPWIRE_CLICK_OFF = getRegisteredSoundEvent("block.tripwire.click_off");
  
  public static final SoundEvent BLOCK_TRIPWIRE_CLICK_ON = getRegisteredSoundEvent("block.tripwire.click_on");
  
  public static final SoundEvent BLOCK_TRIPWIRE_DETACH = getRegisteredSoundEvent("block.tripwire.detach");
  
  public static final SoundEvent UI_BUTTON_CLICK = getRegisteredSoundEvent("ui.button.click");
  
  public static final SoundEvent field_194226_id = getRegisteredSoundEvent("ui.toast.in");
  
  public static final SoundEvent field_194227_ie = getRegisteredSoundEvent("ui.toast.out");
  
  public static final SoundEvent field_194228_if = getRegisteredSoundEvent("ui.toast.challenge_complete");
  
  public static final SoundEvent field_191264_hc = getRegisteredSoundEvent("entity.vex.ambient");
  
  public static final SoundEvent field_191265_hd = getRegisteredSoundEvent("entity.vex.charge");
  
  public static final SoundEvent field_191266_he = getRegisteredSoundEvent("entity.vex.death");
  
  public static final SoundEvent field_191267_hf = getRegisteredSoundEvent("entity.vex.hurt");
  
  public static final SoundEvent ENTITY_VILLAGER_AMBIENT = getRegisteredSoundEvent("entity.villager.ambient");
  
  public static final SoundEvent ENTITY_VILLAGER_DEATH = getRegisteredSoundEvent("entity.villager.death");
  
  public static final SoundEvent ENTITY_VILLAGER_HURT = getRegisteredSoundEvent("entity.villager.hurt");
  
  public static final SoundEvent ENTITY_VILLAGER_NO = getRegisteredSoundEvent("entity.villager.no");
  
  public static final SoundEvent ENTITY_VILLAGER_TRADING = getRegisteredSoundEvent("entity.villager.trading");
  
  public static final SoundEvent ENTITY_VILLAGER_YES = getRegisteredSoundEvent("entity.villager.yes");
  
  public static final SoundEvent field_191268_hm = getRegisteredSoundEvent("entity.vindication_illager.ambient");
  
  public static final SoundEvent field_191269_hn = getRegisteredSoundEvent("entity.vindication_illager.death");
  
  public static final SoundEvent field_191270_ho = getRegisteredSoundEvent("entity.vindication_illager.hurt");
  
  public static final SoundEvent BLOCK_WATERLILY_PLACE = getRegisteredSoundEvent("block.waterlily.place");
  
  public static final SoundEvent BLOCK_WATER_AMBIENT = getRegisteredSoundEvent("block.water.ambient");
  
  public static final SoundEvent WEATHER_RAIN = getRegisteredSoundEvent("weather.rain");
  
  public static final SoundEvent WEATHER_RAIN_ABOVE = getRegisteredSoundEvent("weather.rain.above");
  
  public static final SoundEvent ENTITY_WITCH_AMBIENT = getRegisteredSoundEvent("entity.witch.ambient");
  
  public static final SoundEvent ENTITY_WITCH_DEATH = getRegisteredSoundEvent("entity.witch.death");
  
  public static final SoundEvent ENTITY_WITCH_DRINK = getRegisteredSoundEvent("entity.witch.drink");
  
  public static final SoundEvent ENTITY_WITCH_HURT = getRegisteredSoundEvent("entity.witch.hurt");
  
  public static final SoundEvent ENTITY_WITCH_THROW = getRegisteredSoundEvent("entity.witch.throw");
  
  public static final SoundEvent ENTITY_WITHER_AMBIENT = getRegisteredSoundEvent("entity.wither.ambient");
  
  public static final SoundEvent ENTITY_WITHER_BREAK_BLOCK = getRegisteredSoundEvent("entity.wither.break_block");
  
  public static final SoundEvent ENTITY_WITHER_DEATH = getRegisteredSoundEvent("entity.wither.death");
  
  public static final SoundEvent ENTITY_WITHER_HURT = getRegisteredSoundEvent("entity.wither.hurt");
  
  public static final SoundEvent ENTITY_WITHER_SHOOT = getRegisteredSoundEvent("entity.wither.shoot");
  
  public static final SoundEvent ENTITY_WITHER_SKELETON_AMBIENT = getRegisteredSoundEvent("entity.wither_skeleton.ambient");
  
  public static final SoundEvent ENTITY_WITHER_SKELETON_DEATH = getRegisteredSoundEvent("entity.wither_skeleton.death");
  
  public static final SoundEvent ENTITY_WITHER_SKELETON_HURT = getRegisteredSoundEvent("entity.wither_skeleton.hurt");
  
  public static final SoundEvent ENTITY_WITHER_SKELETON_STEP = getRegisteredSoundEvent("entity.wither_skeleton.step");
  
  public static final SoundEvent ENTITY_WITHER_SPAWN = getRegisteredSoundEvent("entity.wither.spawn");
  
  public static final SoundEvent ENTITY_WOLF_AMBIENT = getRegisteredSoundEvent("entity.wolf.ambient");
  
  public static final SoundEvent ENTITY_WOLF_DEATH = getRegisteredSoundEvent("entity.wolf.death");
  
  public static final SoundEvent ENTITY_WOLF_GROWL = getRegisteredSoundEvent("entity.wolf.growl");
  
  public static final SoundEvent ENTITY_WOLF_HOWL = getRegisteredSoundEvent("entity.wolf.howl");
  
  public static final SoundEvent ENTITY_WOLF_HURT = getRegisteredSoundEvent("entity.wolf.hurt");
  
  public static final SoundEvent ENTITY_WOLF_PANT = getRegisteredSoundEvent("entity.wolf.pant");
  
  public static final SoundEvent ENTITY_WOLF_SHAKE = getRegisteredSoundEvent("entity.wolf.shake");
  
  public static final SoundEvent ENTITY_WOLF_STEP = getRegisteredSoundEvent("entity.wolf.step");
  
  public static final SoundEvent ENTITY_WOLF_WHINE = getRegisteredSoundEvent("entity.wolf.whine");
  
  public static final SoundEvent BLOCK_WOODEN_DOOR_CLOSE = getRegisteredSoundEvent("block.wooden_door.close");
  
  public static final SoundEvent BLOCK_WOODEN_DOOR_OPEN = getRegisteredSoundEvent("block.wooden_door.open");
  
  public static final SoundEvent BLOCK_WOODEN_TRAPDOOR_CLOSE = getRegisteredSoundEvent("block.wooden_trapdoor.close");
  
  public static final SoundEvent BLOCK_WOODEN_TRAPDOOR_OPEN = getRegisteredSoundEvent("block.wooden_trapdoor.open");
  
  public static final SoundEvent BLOCK_WOOD_BREAK = getRegisteredSoundEvent("block.wood.break");
  
  public static final SoundEvent BLOCK_WOOD_BUTTON_CLICK_OFF = getRegisteredSoundEvent("block.wood_button.click_off");
  
  public static final SoundEvent BLOCK_WOOD_BUTTON_CLICK_ON = getRegisteredSoundEvent("block.wood_button.click_on");
  
  public static final SoundEvent BLOCK_WOOD_FALL = getRegisteredSoundEvent("block.wood.fall");
  
  public static final SoundEvent BLOCK_WOOD_HIT = getRegisteredSoundEvent("block.wood.hit");
  
  public static final SoundEvent BLOCK_WOOD_PLACE = getRegisteredSoundEvent("block.wood.place");
  
  public static final SoundEvent BLOCK_WOOD_PRESSPLATE_CLICK_OFF = getRegisteredSoundEvent("block.wood_pressureplate.click_off");
  
  public static final SoundEvent BLOCK_WOOD_PRESSPLATE_CLICK_ON = getRegisteredSoundEvent("block.wood_pressureplate.click_on");
  
  public static final SoundEvent BLOCK_WOOD_STEP = getRegisteredSoundEvent("block.wood.step");
  
  public static final SoundEvent ENTITY_ZOMBIE_AMBIENT = getRegisteredSoundEvent("entity.zombie.ambient");
  
  public static final SoundEvent ENTITY_ZOMBIE_ATTACK_DOOR_WOOD = getRegisteredSoundEvent("entity.zombie.attack_door_wood");
  
  public static final SoundEvent ENTITY_ZOMBIE_ATTACK_IRON_DOOR = getRegisteredSoundEvent("entity.zombie.attack_iron_door");
  
  public static final SoundEvent ENTITY_ZOMBIE_BREAK_DOOR_WOOD = getRegisteredSoundEvent("entity.zombie.break_door_wood");
  
  public static final SoundEvent ENTITY_ZOMBIE_DEATH = getRegisteredSoundEvent("entity.zombie.death");
  
  public static final SoundEvent ENTITY_ZOMBIE_HORSE_AMBIENT = getRegisteredSoundEvent("entity.zombie_horse.ambient");
  
  public static final SoundEvent ENTITY_ZOMBIE_HORSE_DEATH = getRegisteredSoundEvent("entity.zombie_horse.death");
  
  public static final SoundEvent ENTITY_ZOMBIE_HORSE_HURT = getRegisteredSoundEvent("entity.zombie_horse.hurt");
  
  public static final SoundEvent ENTITY_ZOMBIE_HURT = getRegisteredSoundEvent("entity.zombie.hurt");
  
  public static final SoundEvent ENTITY_ZOMBIE_INFECT = getRegisteredSoundEvent("entity.zombie.infect");
  
  public static final SoundEvent ENTITY_ZOMBIE_PIG_AMBIENT = getRegisteredSoundEvent("entity.zombie_pig.ambient");
  
  public static final SoundEvent ENTITY_ZOMBIE_PIG_ANGRY = getRegisteredSoundEvent("entity.zombie_pig.angry");
  
  public static final SoundEvent ENTITY_ZOMBIE_PIG_DEATH = getRegisteredSoundEvent("entity.zombie_pig.death");
  
  public static final SoundEvent ENTITY_ZOMBIE_PIG_HURT = getRegisteredSoundEvent("entity.zombie_pig.hurt");
  
  public static final SoundEvent ENTITY_ZOMBIE_STEP = getRegisteredSoundEvent("entity.zombie.step");
  
  public static final SoundEvent ENTITY_ZOMBIE_VILLAGER_AMBIENT = getRegisteredSoundEvent("entity.zombie_villager.ambient");
  
  public static final SoundEvent ENTITY_ZOMBIE_VILLAGER_CONVERTED = getRegisteredSoundEvent("entity.zombie_villager.converted");
  
  public static final SoundEvent ENTITY_ZOMBIE_VILLAGER_CURE = getRegisteredSoundEvent("entity.zombie_villager.cure");
  
  public static final SoundEvent ENTITY_ZOMBIE_VILLAGER_DEATH = getRegisteredSoundEvent("entity.zombie_villager.death");
  
  public static final SoundEvent ENTITY_ZOMBIE_VILLAGER_HURT = getRegisteredSoundEvent("entity.zombie_villager.hurt");
  
  public static final SoundEvent ENTITY_ZOMBIE_VILLAGER_STEP = getRegisteredSoundEvent("entity.zombie_villager.step");
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\init\SoundEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */