package net.minecraft.client.network;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.toasts.RecipeToast;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.client.renderer.debug.DebugRendererPathfinding;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seltak.anubis.Anubis;

public class NetHandlerPlayClient implements INetHandlerPlayClient {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final NetworkManager netManager;
  
  private final GameProfile profile;
  
  private final GuiScreen guiScreenServer;
  
  private Minecraft gameController;
  
  private WorldClient clientWorldController;
  
  private boolean doneLoadingTerrain;
  
  private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
  
  public int currentServerMaxPlayers = 20;
  
  private boolean hasStatistics;
  
  private final ClientAdvancementManager field_191983_k;
  
  private final Random avRandomizer = new Random();
  
  public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager networkManagerIn, GameProfile profileIn) {
    this.gameController = mcIn;
    this.guiScreenServer = p_i46300_2_;
    this.netManager = networkManagerIn;
    this.profile = profileIn;
    this.field_191983_k = new ClientAdvancementManager(mcIn);
  }
  
  public void cleanup() {
    this.clientWorldController = null;
  }
  
  public void handleJoinGame(SPacketJoinGame packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
    this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), this.gameController.mcProfiler);
    this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
    this.gameController.loadWorld(this.clientWorldController);
    this.gameController.player.dimension = packetIn.getDimension();
    this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain());
    this.gameController.player.setEntityId(packetIn.getPlayerId());
    this.currentServerMaxPlayers = packetIn.getMaxPlayers();
    this.gameController.player.setReducedDebug(packetIn.isReducedDebugInfo());
    this.gameController.playerController.setGameType(packetIn.getGameType());
    this.gameController.gameSettings.sendSettingsToServer();
    this.netManager.sendPacket((Packet)new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
  }
  
  public void handleSpawnObject(SPacketSpawnObject packetIn) {
    EntityAreaEffectCloud entityAreaEffectCloud;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    Entity entity = null;
    if (packetIn.getType() == 10) {
      EntityMinecart entityMinecart = EntityMinecart.create((World)this.clientWorldController, d0, d1, d2, EntityMinecart.Type.getById(packetIn.getData()));
    } else if (packetIn.getType() == 90) {
      Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getData());
      if (entity1 instanceof EntityPlayer)
        EntityFishHook entityFishHook = new EntityFishHook((World)this.clientWorldController, (EntityPlayer)entity1, d0, d1, d2); 
      packetIn.setData(0);
    } else if (packetIn.getType() == 60) {
      EntityTippedArrow entityTippedArrow = new EntityTippedArrow((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 91) {
      EntitySpectralArrow entitySpectralArrow = new EntitySpectralArrow((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 61) {
      EntitySnowball entitySnowball = new EntitySnowball((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 68) {
      EntityLlamaSpit entityLlamaSpit = new EntityLlamaSpit((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
    } else if (packetIn.getType() == 71) {
      EntityItemFrame entityItemFrame = new EntityItemFrame((World)this.clientWorldController, new BlockPos(d0, d1, d2), EnumFacing.getHorizontal(packetIn.getData()));
      packetIn.setData(0);
    } else if (packetIn.getType() == 77) {
      EntityLeashKnot entityLeashKnot = new EntityLeashKnot((World)this.clientWorldController, new BlockPos(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2)));
      packetIn.setData(0);
    } else if (packetIn.getType() == 65) {
      EntityEnderPearl entityEnderPearl = new EntityEnderPearl((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 72) {
      EntityEnderEye entityEnderEye = new EntityEnderEye((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 76) {
      EntityFireworkRocket entityFireworkRocket = new EntityFireworkRocket((World)this.clientWorldController, d0, d1, d2, ItemStack.field_190927_a);
    } else if (packetIn.getType() == 63) {
      EntityLargeFireball entityLargeFireball = new EntityLargeFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.setData(0);
    } else if (packetIn.getType() == 93) {
      EntityDragonFireball entityDragonFireball = new EntityDragonFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.setData(0);
    } else if (packetIn.getType() == 64) {
      EntitySmallFireball entitySmallFireball = new EntitySmallFireball((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.setData(0);
    } else if (packetIn.getType() == 66) {
      EntityWitherSkull entityWitherSkull = new EntityWitherSkull((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.setData(0);
    } else if (packetIn.getType() == 67) {
      EntityShulkerBullet entityShulkerBullet = new EntityShulkerBullet((World)this.clientWorldController, d0, d1, d2, packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      packetIn.setData(0);
    } else if (packetIn.getType() == 62) {
      EntityEgg entityEgg = new EntityEgg((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 79) {
      EntityEvokerFangs entityEvokerFangs = new EntityEvokerFangs((World)this.clientWorldController, d0, d1, d2, 0.0F, 0, null);
    } else if (packetIn.getType() == 73) {
      EntityPotion entityPotion = new EntityPotion((World)this.clientWorldController, d0, d1, d2, ItemStack.field_190927_a);
      packetIn.setData(0);
    } else if (packetIn.getType() == 75) {
      EntityExpBottle entityExpBottle = new EntityExpBottle((World)this.clientWorldController, d0, d1, d2);
      packetIn.setData(0);
    } else if (packetIn.getType() == 1) {
      EntityBoat entityBoat = new EntityBoat((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 50) {
      EntityTNTPrimed entityTNTPrimed = new EntityTNTPrimed((World)this.clientWorldController, d0, d1, d2, null);
    } else if (packetIn.getType() == 78) {
      EntityArmorStand entityArmorStand = new EntityArmorStand((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 51) {
      EntityEnderCrystal entityEnderCrystal = new EntityEnderCrystal((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 2) {
      EntityItem entityItem = new EntityItem((World)this.clientWorldController, d0, d1, d2);
    } else if (packetIn.getType() == 70) {
      EntityFallingBlock entityFallingBlock = new EntityFallingBlock((World)this.clientWorldController, d0, d1, d2, Block.getStateById(packetIn.getData() & 0xFFFF));
      packetIn.setData(0);
    } else if (packetIn.getType() == 3) {
      entityAreaEffectCloud = new EntityAreaEffectCloud((World)this.clientWorldController, d0, d1, d2);
    } 
    if (entityAreaEffectCloud != null) {
      EntityTracker.updateServerPosition((Entity)entityAreaEffectCloud, d0, d1, d2);
      ((Entity)entityAreaEffectCloud).rotationPitch = (packetIn.getPitch() * 360) / 256.0F;
      ((Entity)entityAreaEffectCloud).rotationYaw = (packetIn.getYaw() * 360) / 256.0F;
      Entity[] aentity = entityAreaEffectCloud.getParts();
      if (aentity != null) {
        int i = packetIn.getEntityID() - entityAreaEffectCloud.getEntityId();
        byte b;
        int j;
        Entity[] arrayOfEntity;
        for (j = (arrayOfEntity = aentity).length, b = 0; b < j; ) {
          Entity entity2 = arrayOfEntity[b];
          entity2.setEntityId(entity2.getEntityId() + i);
          b++;
        } 
      } 
      entityAreaEffectCloud.setEntityId(packetIn.getEntityID());
      entityAreaEffectCloud.setUniqueId(packetIn.getUniqueId());
      this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityAreaEffectCloud);
      if (packetIn.getData() > 0) {
        if (packetIn.getType() == 60 || packetIn.getType() == 91) {
          Entity entity3 = this.clientWorldController.getEntityByID(packetIn.getData() - 1);
          if (entity3 instanceof EntityLivingBase && entityAreaEffectCloud instanceof EntityArrow)
            ((EntityArrow)entityAreaEffectCloud).shootingEntity = entity3; 
        } 
        entityAreaEffectCloud.setVelocity(packetIn.getSpeedX() / 8000.0D, packetIn.getSpeedY() / 8000.0D, packetIn.getSpeedZ() / 8000.0D);
      } 
    } 
  }
  
  public void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    EntityXPOrb entityXPOrb = new EntityXPOrb((World)this.clientWorldController, d0, d1, d2, packetIn.getXPValue());
    EntityTracker.updateServerPosition((Entity)entityXPOrb, d0, d1, d2);
    ((Entity)entityXPOrb).rotationYaw = 0.0F;
    ((Entity)entityXPOrb).rotationPitch = 0.0F;
    entityXPOrb.setEntityId(packetIn.getEntityID());
    this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityXPOrb);
  }
  
  public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity packetIn) {
    EntityLightningBolt entityLightningBolt;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    Entity entity = null;
    if (packetIn.getType() == 1)
      entityLightningBolt = new EntityLightningBolt((World)this.clientWorldController, d0, d1, d2, false); 
    if (entityLightningBolt != null) {
      EntityTracker.updateServerPosition((Entity)entityLightningBolt, d0, d1, d2);
      ((Entity)entityLightningBolt).rotationYaw = 0.0F;
      ((Entity)entityLightningBolt).rotationPitch = 0.0F;
      entityLightningBolt.setEntityId(packetIn.getEntityId());
      this.clientWorldController.addWeatherEffect((Entity)entityLightningBolt);
    } 
  }
  
  public void handleSpawnPainting(SPacketSpawnPainting packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPainting entitypainting = new EntityPainting((World)this.clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
    entitypainting.setUniqueId(packetIn.getUniqueId());
    this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitypainting);
  }
  
  public void handleEntityVelocity(SPacketEntityVelocity packetIn) {
    if (!Anubis.setmgr.getSettingByName("Velocity Mode").getValString().equalsIgnoreCase("aac") && !Anubis.setmgr.getSettingByName("Velocity Mode").getValString().equalsIgnoreCase("ncp") && !Anubis.setmgr.getSettingByName("Velocity Mode").getValString().equalsIgnoreCase("vanilla")) {
      PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
      if (entity != null)
        entity.setVelocity(packetIn.getMotionX() / 8000.0D, packetIn.getMotionY() / 8000.0D, packetIn.getMotionZ() / 8000.0D); 
    } 
  }
  
  public void handleEntityMetadata(SPacketEntityMetadata packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity != null && packetIn.getDataManagerEntries() != null)
      entity.getDataManager().setEntryValues(packetIn.getDataManagerEntries()); 
  }
  
  public void handleSpawnPlayer(SPacketSpawnPlayer packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    float f = (packetIn.getYaw() * 360) / 256.0F;
    float f1 = (packetIn.getPitch() * 360) / 256.0F;
    EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP((World)this.gameController.world, getPlayerInfo(packetIn.getUniqueId()).getGameProfile());
    entityotherplayermp.prevPosX = d0;
    entityotherplayermp.lastTickPosX = d0;
    entityotherplayermp.prevPosY = d1;
    entityotherplayermp.lastTickPosY = d1;
    entityotherplayermp.prevPosZ = d2;
    entityotherplayermp.lastTickPosZ = d2;
    EntityTracker.updateServerPosition((Entity)entityotherplayermp, d0, d1, d2);
    entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
    this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entityotherplayermp);
    List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
    if (list != null)
      entityotherplayermp.getDataManager().setEntryValues(list); 
  }
  
  public void handleEntityTeleport(SPacketEntityTeleport packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity != null) {
      double d0 = packetIn.getX();
      double d1 = packetIn.getY();
      double d2 = packetIn.getZ();
      EntityTracker.updateServerPosition(entity, d0, d1, d2);
      if (!entity.canPassengerSteer()) {
        float f = (packetIn.getYaw() * 360) / 256.0F;
        float f1 = (packetIn.getPitch() * 360) / 256.0F;
        if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D) {
          entity.setPositionAndRotationDirect(entity.posX, entity.posY, entity.posZ, f, f1, 0, true);
        } else {
          entity.setPositionAndRotationDirect(d0, d1, d2, f, f1, 3, true);
        } 
        entity.onGround = packetIn.getOnGround();
      } 
    } 
  }
  
  public void handleHeldItemChange(SPacketHeldItemChange packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (InventoryPlayer.isHotbar(packetIn.getHeldItemHotbarIndex()))
      this.gameController.player.inventory.currentItem = packetIn.getHeldItemHotbarIndex(); 
  }
  
  public void handleEntityMovement(SPacketEntity packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null) {
      entity.serverPosX += packetIn.getX();
      entity.serverPosY += packetIn.getY();
      entity.serverPosZ += packetIn.getZ();
      double d0 = entity.serverPosX / 4096.0D;
      double d1 = entity.serverPosY / 4096.0D;
      double d2 = entity.serverPosZ / 4096.0D;
      if (!entity.canPassengerSteer()) {
        float f = packetIn.isRotating() ? ((packetIn.getYaw() * 360) / 256.0F) : entity.rotationYaw;
        float f1 = packetIn.isRotating() ? ((packetIn.getPitch() * 360) / 256.0F) : entity.rotationPitch;
        entity.setPositionAndRotationDirect(d0, d1, d2, f, f1, 3, false);
        entity.onGround = packetIn.getOnGround();
      } 
    } 
  }
  
  public void handleEntityHeadLook(SPacketEntityHeadLook packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null) {
      float f = (packetIn.getYaw() * 360) / 256.0F;
      entity.setRotationYawHead(f);
    } 
  }
  
  public void handleDestroyEntities(SPacketDestroyEntities packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    for (int i = 0; i < (packetIn.getEntityIDs()).length; i++)
      this.clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]); 
  }
  
  public void handlePlayerPosLook(SPacketPlayerPosLook packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    float f = packetIn.getYaw();
    float f1 = packetIn.getPitch();
    if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X)) {
      d0 += ((EntityPlayer)entityPlayerSP).posX;
    } else {
      ((EntityPlayer)entityPlayerSP).motionX = 0.0D;
    } 
    if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y)) {
      d1 += ((EntityPlayer)entityPlayerSP).posY;
    } else {
      ((EntityPlayer)entityPlayerSP).motionY = 0.0D;
    } 
    if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z)) {
      d2 += ((EntityPlayer)entityPlayerSP).posZ;
    } else {
      ((EntityPlayer)entityPlayerSP).motionZ = 0.0D;
    } 
    if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT))
      f1 += ((EntityPlayer)entityPlayerSP).rotationPitch; 
    if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT))
      f += ((EntityPlayer)entityPlayerSP).rotationYaw; 
    entityPlayerSP.setPositionAndRotation(d0, d1, d2, f, f1);
    this.netManager.sendPacket((Packet)new CPacketConfirmTeleport(packetIn.getTeleportId()));
    this.netManager.sendPacket((Packet)new CPacketPlayer.PositionRotation(((EntityPlayer)entityPlayerSP).posX, (entityPlayerSP.getEntityBoundingBox()).minY, ((EntityPlayer)entityPlayerSP).posZ, ((EntityPlayer)entityPlayerSP).rotationYaw, ((EntityPlayer)entityPlayerSP).rotationPitch, false));
    if (!this.doneLoadingTerrain) {
      this.gameController.player.prevPosX = this.gameController.player.posX;
      this.gameController.player.prevPosY = this.gameController.player.posY;
      this.gameController.player.prevPosZ = this.gameController.player.posZ;
      this.doneLoadingTerrain = true;
      this.gameController.displayGuiScreen(null);
    } 
  }
  
  public void handleMultiBlockChange(SPacketMultiBlockChange packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    byte b;
    int i;
    SPacketMultiBlockChange.BlockUpdateData[] arrayOfBlockUpdateData;
    for (i = (arrayOfBlockUpdateData = packetIn.getChangedBlocks()).length, b = 0; b < i; ) {
      SPacketMultiBlockChange.BlockUpdateData spacketmultiblockchange$blockupdatedata = arrayOfBlockUpdateData[b];
      this.clientWorldController.invalidateRegionAndSetBlock(spacketmultiblockchange$blockupdatedata.getPos(), spacketmultiblockchange$blockupdatedata.getBlockState());
      b++;
    } 
  }
  
  public void handleChunkData(SPacketChunkData packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.doChunkLoad())
      this.clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true); 
    this.clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
    Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
    chunk.fillChunk(packetIn.getReadBuffer(), packetIn.getExtractedSize(), packetIn.doChunkLoad());
    this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
    if (!packetIn.doChunkLoad() || !(this.clientWorldController.provider instanceof net.minecraft.world.WorldProviderSurface))
      chunk.resetRelightChecks(); 
    for (NBTTagCompound nbttagcompound : packetIn.getTileEntityTags()) {
      BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
      TileEntity tileentity = this.clientWorldController.getTileEntity(blockpos);
      if (tileentity != null)
        tileentity.readFromNBT(nbttagcompound); 
    } 
  }
  
  public void processChunkUnload(SPacketUnloadChunk packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.clientWorldController.doPreChunk(packetIn.getX(), packetIn.getZ(), false);
  }
  
  public void handleBlockChange(SPacketBlockChange packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
  }
  
  public void handleDisconnect(SPacketDisconnect packetIn) {
    this.netManager.closeChannel(packetIn.getReason());
  }
  
  public void onDisconnect(ITextComponent reason) {
    this.gameController.loadWorld(null);
    if (this.guiScreenServer != null) {
      if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
        this.gameController.displayGuiScreen((GuiScreen)(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).getProxy(), "disconnect.lost", reason)).getProxy());
      } else {
        this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
      } 
    } else {
      this.gameController.displayGuiScreen((GuiScreen)new GuiDisconnected((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), "disconnect.lost", reason));
    } 
  }
  
  public void sendPacket(Packet<?> packetIn) {
    this.netManager.sendPacket(packetIn);
  }
  
  public void handleCollectItem(SPacketCollectItem packetIn) {
    EntityPlayerSP entityPlayerSP;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
    EntityLivingBase entitylivingbase = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.getEntityID());
    if (entitylivingbase == null)
      entityPlayerSP = this.gameController.player; 
    if (entity != null) {
      if (entity instanceof EntityXPOrb) {
        this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.35F + 0.9F, false);
      } else {
        this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 1.4F + 2.0F, false);
      } 
      if (entity instanceof EntityItem)
        ((EntityItem)entity).getEntityItem().func_190920_e(packetIn.func_191208_c()); 
      this.gameController.effectRenderer.addEffect((Particle)new ParticleItemPickup((World)this.clientWorldController, entity, (Entity)entityPlayerSP, 0.5F));
      this.clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
    } 
  }
  
  public void handleChat(SPacketChat packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.ingameGUI.func_191742_a(packetIn.func_192590_c(), packetIn.getChatComponent());
  }
  
  public void handleAnimation(SPacketAnimation packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
    if (entity != null)
      if (packetIn.getAnimationType() == 0) {
        EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
        entitylivingbase.swingArm(EnumHand.MAIN_HAND);
      } else if (packetIn.getAnimationType() == 3) {
        EntityLivingBase entitylivingbase1 = (EntityLivingBase)entity;
        entitylivingbase1.swingArm(EnumHand.OFF_HAND);
      } else if (packetIn.getAnimationType() == 1) {
        entity.performHurtAnimation();
      } else if (packetIn.getAnimationType() == 2) {
        EntityPlayer entityplayer = (EntityPlayer)entity;
        entityplayer.wakeUpPlayer(false, false, false);
      } else if (packetIn.getAnimationType() == 4) {
        this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
      } else if (packetIn.getAnimationType() == 5) {
        this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
      }  
  }
  
  public void handleUseBed(SPacketUseBed packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    packetIn.getPlayer((World)this.clientWorldController).trySleep(packetIn.getBedPosition());
  }
  
  public void handleSpawnMob(SPacketSpawnMob packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    double d0 = packetIn.getX();
    double d1 = packetIn.getY();
    double d2 = packetIn.getZ();
    float f = (packetIn.getYaw() * 360) / 256.0F;
    float f1 = (packetIn.getPitch() * 360) / 256.0F;
    EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), (World)this.gameController.world);
    if (entitylivingbase != null) {
      EntityTracker.updateServerPosition((Entity)entitylivingbase, d0, d1, d2);
      entitylivingbase.renderYawOffset = (packetIn.getHeadPitch() * 360) / 256.0F;
      entitylivingbase.rotationYawHead = (packetIn.getHeadPitch() * 360) / 256.0F;
      Entity[] aentity = entitylivingbase.getParts();
      if (aentity != null) {
        int i = packetIn.getEntityID() - entitylivingbase.getEntityId();
        byte b;
        int j;
        Entity[] arrayOfEntity;
        for (j = (arrayOfEntity = aentity).length, b = 0; b < j; ) {
          Entity entity = arrayOfEntity[b];
          entity.setEntityId(entity.getEntityId() + i);
          b++;
        } 
      } 
      entitylivingbase.setEntityId(packetIn.getEntityID());
      entitylivingbase.setUniqueId(packetIn.getUniqueId());
      entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
      entitylivingbase.motionX = (packetIn.getVelocityX() / 8000.0F);
      entitylivingbase.motionY = (packetIn.getVelocityY() / 8000.0F);
      entitylivingbase.motionZ = (packetIn.getVelocityZ() / 8000.0F);
      this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), (Entity)entitylivingbase);
      List<EntityDataManager.DataEntry<?>> list = packetIn.getDataManagerEntries();
      if (list != null)
        entitylivingbase.getDataManager().setEntryValues(list); 
    } else {
      LOGGER.warn("Skipping Entity with id {}", Integer.valueOf(packetIn.getEntityType()));
    } 
  }
  
  public void handleTimeUpdate(SPacketTimeUpdate packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.world.setTotalWorldTime(packetIn.getTotalWorldTime());
    this.gameController.world.setWorldTime(packetIn.getWorldTime());
  }
  
  public void handleSpawnPosition(SPacketSpawnPosition packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.player.setSpawnPoint(packetIn.getSpawnPos(), true);
    this.gameController.world.getWorldInfo().setSpawn(packetIn.getSpawnPos());
  }
  
  public void handleSetPassengers(SPacketSetPassengers packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity == null) {
      LOGGER.warn("Received passengers for unknown entity");
    } else {
      boolean flag = entity.isRidingOrBeingRiddenBy((Entity)this.gameController.player);
      entity.removePassengers();
      byte b;
      int i, arrayOfInt[];
      for (i = (arrayOfInt = packetIn.getPassengerIds()).length, b = 0; b < i; ) {
        int j = arrayOfInt[b];
        Entity entity1 = this.clientWorldController.getEntityByID(j);
        if (entity1 != null) {
          entity1.startRiding(entity, true);
          if (entity1 == this.gameController.player && !flag)
            this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", new Object[] { GameSettings.getKeyDisplayString(this.gameController.gameSettings.keyBindSneak.getKeyCode()) }), false); 
        } 
        b++;
      } 
    } 
  }
  
  public void handleEntityAttach(SPacketEntityAttach packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    Entity entity1 = this.clientWorldController.getEntityByID(packetIn.getVehicleEntityId());
    if (entity instanceof EntityLiving)
      if (entity1 != null) {
        ((EntityLiving)entity).setLeashedToEntity(entity1, false);
      } else {
        ((EntityLiving)entity).clearLeashed(false, false);
      }  
  }
  
  public void handleEntityStatus(SPacketEntityStatus packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null)
      if (packetIn.getOpCode() == 21) {
        this.gameController.getSoundHandler().playSound((ISound)new GuardianSound((EntityGuardian)entity));
      } else if (packetIn.getOpCode() == 35) {
        int i = 40;
        this.gameController.effectRenderer.func_191271_a(entity, EnumParticleTypes.TOTEM, 30);
        this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.field_191263_gW, entity.getSoundCategory(), 1.0F, 1.0F, false);
        if (entity == this.gameController.player)
          this.gameController.entityRenderer.func_190565_a(new ItemStack(Items.field_190929_cY)); 
      } else {
        entity.handleStatusUpdate(packetIn.getOpCode());
      }  
  }
  
  public void handleUpdateHealth(SPacketUpdateHealth packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.player.setPlayerSPHealth(packetIn.getHealth());
    this.gameController.player.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
    this.gameController.player.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
  }
  
  public void handleSetExperience(SPacketSetExperience packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.player.setXPStats(packetIn.getExperienceBar(), packetIn.getTotalExperience(), packetIn.getLevel());
  }
  
  public void handleRespawn(SPacketRespawn packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.getDimensionID() != this.gameController.player.dimension) {
      this.doneLoadingTerrain = false;
      Scoreboard scoreboard = this.clientWorldController.getScoreboard();
      this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, this.gameController.world.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), this.gameController.mcProfiler);
      this.clientWorldController.setWorldScoreboard(scoreboard);
      this.gameController.loadWorld(this.clientWorldController);
      this.gameController.player.dimension = packetIn.getDimensionID();
      this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain());
    } 
    this.gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
    this.gameController.playerController.setGameType(packetIn.getGameType());
  }
  
  public void handleExplosion(SPacketExplosion packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Explosion explosion = new Explosion((World)this.gameController.world, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
    explosion.doExplosionB(true);
    this.gameController.player.motionX += packetIn.getMotionX();
    this.gameController.player.motionY += packetIn.getMotionY();
    this.gameController.player.motionZ += packetIn.getMotionZ();
  }
  
  public void handleOpenWindow(SPacketOpenWindow packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityplayersp = this.gameController.player;
    if ("minecraft:container".equals(packetIn.getGuiId())) {
      entityplayersp.displayGUIChest((IInventory)new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
      entityplayersp.openContainer.windowId = packetIn.getWindowId();
    } else if ("minecraft:villager".equals(packetIn.getGuiId())) {
      entityplayersp.displayVillagerTradeGui((IMerchant)new NpcMerchant((EntityPlayer)entityplayersp, packetIn.getWindowTitle()));
      entityplayersp.openContainer.windowId = packetIn.getWindowId();
    } else if ("EntityHorse".equals(packetIn.getGuiId())) {
      Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
      if (entity instanceof AbstractHorse) {
        entityplayersp.openGuiHorseInventory((AbstractHorse)entity, (IInventory)new ContainerHorseChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
        entityplayersp.openContainer.windowId = packetIn.getWindowId();
      } 
    } else if (!packetIn.hasSlots()) {
      entityplayersp.displayGui((IInteractionObject)new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
      entityplayersp.openContainer.windowId = packetIn.getWindowId();
    } else {
      ContainerLocalMenu containerLocalMenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
      entityplayersp.displayGUIChest((IInventory)containerLocalMenu);
      entityplayersp.openContainer.windowId = packetIn.getWindowId();
    } 
  }
  
  public void handleSetSlot(SPacketSetSlot packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    ItemStack itemstack = packetIn.getStack();
    int i = packetIn.getSlot();
    this.gameController.func_193032_ao().func_193301_a(itemstack);
    if (packetIn.getWindowId() == -1) {
      ((EntityPlayer)entityPlayerSP).inventory.setItemStack(itemstack);
    } else if (packetIn.getWindowId() == -2) {
      ((EntityPlayer)entityPlayerSP).inventory.setInventorySlotContents(i, itemstack);
    } else {
      boolean flag = false;
      if (this.gameController.currentScreen instanceof GuiContainerCreative) {
        GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
        flag = (guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex());
      } 
      if (packetIn.getWindowId() == 0 && packetIn.getSlot() >= 36 && i < 45) {
        if (!itemstack.func_190926_b()) {
          ItemStack itemstack1 = ((EntityPlayer)entityPlayerSP).inventoryContainer.getSlot(i).getStack();
          if (itemstack1.func_190926_b() || itemstack1.func_190916_E() < itemstack.func_190916_E())
            itemstack.func_190915_d(5); 
        } 
        ((EntityPlayer)entityPlayerSP).inventoryContainer.putStackInSlot(i, itemstack);
      } else if (packetIn.getWindowId() == ((EntityPlayer)entityPlayerSP).openContainer.windowId && (packetIn.getWindowId() != 0 || !flag)) {
        ((EntityPlayer)entityPlayerSP).openContainer.putStackInSlot(i, itemstack);
      } 
    } 
  }
  
  public void handleConfirmTransaction(SPacketConfirmTransaction packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Container container = null;
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    if (packetIn.getWindowId() == 0) {
      container = ((EntityPlayer)entityPlayerSP).inventoryContainer;
    } else if (packetIn.getWindowId() == ((EntityPlayer)entityPlayerSP).openContainer.windowId) {
      container = ((EntityPlayer)entityPlayerSP).openContainer;
    } 
    if (container != null && !packetIn.wasAccepted())
      sendPacket((Packet<?>)new CPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true)); 
  }
  
  public void handleWindowItems(SPacketWindowItems packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    if (packetIn.getWindowId() == 0) {
      ((EntityPlayer)entityPlayerSP).inventoryContainer.func_190896_a(packetIn.getItemStacks());
    } else if (packetIn.getWindowId() == ((EntityPlayer)entityPlayerSP).openContainer.windowId) {
      ((EntityPlayer)entityPlayerSP).openContainer.func_190896_a(packetIn.getItemStacks());
    } 
  }
  
  public void handleSignEditorOpen(SPacketSignEditorOpen packetIn) {
    TileEntitySign tileEntitySign;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    TileEntity tileentity = this.clientWorldController.getTileEntity(packetIn.getSignPosition());
    if (!(tileentity instanceof TileEntitySign)) {
      tileEntitySign = new TileEntitySign();
      tileEntitySign.setWorldObj((World)this.clientWorldController);
      tileEntitySign.setPos(packetIn.getSignPosition());
    } 
    this.gameController.player.openEditSign(tileEntitySign);
  }
  
  public void handleUpdateTileEntity(SPacketUpdateTileEntity packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (this.gameController.world.isBlockLoaded(packetIn.getPos())) {
      TileEntity tileentity = this.gameController.world.getTileEntity(packetIn.getPos());
      int i = packetIn.getTileEntityType();
      boolean flag = (i == 2 && tileentity instanceof net.minecraft.tileentity.TileEntityCommandBlock);
      if ((i == 1 && tileentity instanceof net.minecraft.tileentity.TileEntityMobSpawner) || flag || (i == 3 && tileentity instanceof net.minecraft.tileentity.TileEntityBeacon) || (i == 4 && tileentity instanceof net.minecraft.tileentity.TileEntitySkull) || (i == 5 && tileentity instanceof net.minecraft.tileentity.TileEntityFlowerPot) || (i == 6 && tileentity instanceof net.minecraft.tileentity.TileEntityBanner) || (i == 7 && tileentity instanceof net.minecraft.tileentity.TileEntityStructure) || (i == 8 && tileentity instanceof net.minecraft.tileentity.TileEntityEndGateway) || (i == 9 && tileentity instanceof TileEntitySign) || (i == 10 && tileentity instanceof net.minecraft.tileentity.TileEntityShulkerBox) || (i == 11 && tileentity instanceof net.minecraft.tileentity.TileEntityBed))
        tileentity.readFromNBT(packetIn.getNbtCompound()); 
      if (flag && this.gameController.currentScreen instanceof GuiCommandBlock)
        ((GuiCommandBlock)this.gameController.currentScreen).updateGui(); 
    } 
  }
  
  public void handleWindowProperty(SPacketWindowProperty packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    if (((EntityPlayer)entityPlayerSP).openContainer != null && ((EntityPlayer)entityPlayerSP).openContainer.windowId == packetIn.getWindowId())
      ((EntityPlayer)entityPlayerSP).openContainer.updateProgressBar(packetIn.getProperty(), packetIn.getValue()); 
  }
  
  public void handleEntityEquipment(SPacketEntityEquipment packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
    if (entity != null)
      entity.setItemStackToSlot(packetIn.getEquipmentSlot(), packetIn.getItemStack()); 
  }
  
  public void handleCloseWindow(SPacketCloseWindow packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.player.closeScreenAndDropStack();
  }
  
  public void handleBlockAction(SPacketBlockAction packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.world.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
  }
  
  public void handleBlockBreakAnim(SPacketBlockBreakAnim packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.world.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
  }
  
  public void handleChangeGameState(SPacketChangeGameState packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    int i = packetIn.getGameState();
    float f = packetIn.getValue();
    int j = MathHelper.floor(f + 0.5F);
    if (i >= 0 && i < SPacketChangeGameState.MESSAGE_NAMES.length && SPacketChangeGameState.MESSAGE_NAMES[i] != null)
      entityPlayerSP.addChatComponentMessage((ITextComponent)new TextComponentTranslation(SPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]), false); 
    if (i == 1) {
      this.clientWorldController.getWorldInfo().setRaining(true);
      this.clientWorldController.setRainStrength(0.0F);
    } else if (i == 2) {
      this.clientWorldController.getWorldInfo().setRaining(false);
      this.clientWorldController.setRainStrength(1.0F);
    } else if (i == 3) {
      this.gameController.playerController.setGameType(GameType.getByID(j));
    } else if (i == 4) {
      if (j == 0) {
        this.gameController.player.connection.sendPacket((Packet<?>)new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
        this.gameController.displayGuiScreen((GuiScreen)new GuiDownloadTerrain());
      } else if (j == 1) {
        this.gameController.displayGuiScreen((GuiScreen)new GuiWinGame(true, () -> this.gameController.player.connection.sendPacket((Packet<?>)new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN))));
      } 
    } else if (i == 5) {
      GameSettings gamesettings = this.gameController.gameSettings;
      if (f == 0.0F) {
        this.gameController.displayGuiScreen((GuiScreen)new GuiScreenDemo());
      } else if (f == 101.0F) {
        this.gameController.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentTranslation("demo.help.movement", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }));
      } else if (f == 102.0F) {
        this.gameController.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentTranslation("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }));
      } else if (f == 103.0F) {
        this.gameController.ingameGUI.getChatGUI().printChatMessage((ITextComponent)new TextComponentTranslation("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }));
      } 
    } else if (i == 6) {
      this.clientWorldController.playSound((EntityPlayer)entityPlayerSP, ((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY + entityPlayerSP.getEyeHeight(), ((EntityPlayer)entityPlayerSP).posZ, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.18F, 0.45F);
    } else if (i == 7) {
      this.clientWorldController.setRainStrength(f);
    } else if (i == 8) {
      this.clientWorldController.setThunderStrength(f);
    } else if (i == 10) {
      this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, ((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY, ((EntityPlayer)entityPlayerSP).posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      this.clientWorldController.playSound((EntityPlayer)entityPlayerSP, ((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY, ((EntityPlayer)entityPlayerSP).posZ, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0F, 1.0F);
    } 
  }
  
  public void handleMaps(SPacketMaps packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    MapItemRenderer mapitemrenderer = this.gameController.entityRenderer.getMapItemRenderer();
    MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), (World)this.gameController.world);
    if (mapdata == null) {
      String s = "map_" + packetIn.getMapId();
      mapdata = new MapData(s);
      if (mapitemrenderer.func_191205_a(s) != null) {
        MapData mapdata1 = mapitemrenderer.func_191207_a(mapitemrenderer.func_191205_a(s));
        if (mapdata1 != null)
          mapdata = mapdata1; 
      } 
      this.gameController.world.setItemData(s, (WorldSavedData)mapdata);
    } 
    packetIn.setMapdataTo(mapdata);
    mapitemrenderer.updateMapTexture(mapdata);
  }
  
  public void handleEffect(SPacketEffect packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.isSoundServerwide()) {
      this.gameController.world.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
    } else {
      this.gameController.world.playEvent(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
    } 
  }
  
  public void func_191981_a(SPacketAdvancementInfo p_191981_1_) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)p_191981_1_, (INetHandler)this, (IThreadListener)this.gameController);
    this.field_191983_k.func_192799_a(p_191981_1_);
  }
  
  public void func_194022_a(SPacketSelectAdvancementsTab p_194022_1_) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)p_194022_1_, (INetHandler)this, (IThreadListener)this.gameController);
    ResourceLocation resourcelocation = p_194022_1_.func_194154_a();
    if (resourcelocation == null) {
      this.field_191983_k.func_194230_a(null, false);
    } else {
      Advancement advancement = this.field_191983_k.func_194229_a().func_192084_a(resourcelocation);
      this.field_191983_k.func_194230_a(advancement, false);
    } 
  }
  
  public void handleStatistics(SPacketStatistics packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    for (Map.Entry<StatBase, Integer> entry : (Iterable<Map.Entry<StatBase, Integer>>)packetIn.getStatisticMap().entrySet()) {
      StatBase statbase = entry.getKey();
      int k = ((Integer)entry.getValue()).intValue();
      this.gameController.player.getStatFileWriter().unlockAchievement((EntityPlayer)this.gameController.player, statbase, k);
    } 
    this.hasStatistics = true;
    if (this.gameController.currentScreen instanceof IProgressMeter)
      ((IProgressMeter)this.gameController.currentScreen).func_193026_g(); 
  }
  
  public void func_191980_a(SPacketRecipeBook p_191980_1_) {
    Iterator<IRecipe> iterator;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)p_191980_1_, (INetHandler)this, (IThreadListener)this.gameController);
    RecipeBook recipebook = this.gameController.player.func_192035_E();
    recipebook.func_192813_a(p_191980_1_.func_192593_c());
    recipebook.func_192810_b(p_191980_1_.func_192594_d());
    SPacketRecipeBook.State spacketrecipebook$state = p_191980_1_.func_194151_e();
    switch (spacketrecipebook$state) {
      case REMOVE:
        iterator = p_191980_1_.func_192595_a().iterator();
        while (iterator.hasNext()) {
          IRecipe irecipe = iterator.next();
          recipebook.func_193831_b(irecipe);
        } 
        break;
      case INIT:
        p_191980_1_.func_192595_a().forEach(recipebook::func_194073_a);
        p_191980_1_.func_193644_b().forEach(recipebook::func_193825_e);
        break;
      case null:
        p_191980_1_.func_192595_a().forEach(p_194025_2_ -> {
              paramRecipeBook.func_194073_a(p_194025_2_);
              paramRecipeBook.func_193825_e(p_194025_2_);
              RecipeToast.func_193665_a(this.gameController.func_193033_an(), p_194025_2_);
            });
        break;
    } 
    RecipeBookClient.field_194087_f.forEach(p_194023_1_ -> p_194023_1_.func_194214_a(paramRecipeBook));
    if (this.gameController.currentScreen instanceof IRecipeShownListener)
      ((IRecipeShownListener)this.gameController.currentScreen).func_192043_J_(); 
  }
  
  public void handleEntityEffect(SPacketEntityEffect packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity instanceof EntityLivingBase) {
      Potion potion = Potion.getPotionById(packetIn.getEffectId());
      if (potion != null) {
        PotionEffect potioneffect = new PotionEffect(potion, packetIn.getDuration(), packetIn.getAmplifier(), packetIn.getIsAmbient(), packetIn.doesShowParticles());
        potioneffect.setPotionDurationMax(packetIn.isMaxDuration());
        ((EntityLivingBase)entity).addPotionEffect(potioneffect);
      } 
    } 
  }
  
  public void handleCombatEvent(SPacketCombatEvent packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
      Entity entity = this.clientWorldController.getEntityByID(packetIn.playerId);
      if (entity == this.gameController.player)
        this.gameController.displayGuiScreen((GuiScreen)new GuiGameOver(packetIn.deathMessage)); 
    } 
  }
  
  public void handleServerDifficulty(SPacketServerDifficulty packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.world.getWorldInfo().setDifficulty(packetIn.getDifficulty());
    this.gameController.world.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
  }
  
  public void handleCamera(SPacketCamera packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity != null)
      this.gameController.setRenderViewEntity(entity); 
  }
  
  public void handleWorldBorder(SPacketWorldBorder packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    packetIn.apply(this.clientWorldController.getWorldBorder());
  }
  
  public void handleTitle(SPacketTitle packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    SPacketTitle.Type spackettitle$type = packetIn.getType();
    String s = null;
    String s1 = null;
    String s2 = (packetIn.getMessage() != null) ? packetIn.getMessage().getFormattedText() : "";
    switch (spackettitle$type) {
      case TITLE:
        s = s2;
        break;
      case SUBTITLE:
        s1 = s2;
        break;
      case null:
        this.gameController.ingameGUI.setRecordPlaying(s2, false);
        return;
      case RESET:
        this.gameController.ingameGUI.displayTitle("", "", -1, -1, -1);
        this.gameController.ingameGUI.setDefaultTitlesTimes();
        return;
    } 
    this.gameController.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
  }
  
  public void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter packetIn) {
    this.gameController.ingameGUI.getTabList().setHeader(packetIn.getHeader().getFormattedText().isEmpty() ? null : packetIn.getHeader());
    this.gameController.ingameGUI.getTabList().setFooter(packetIn.getFooter().getFormattedText().isEmpty() ? null : packetIn.getFooter());
  }
  
  public void handleRemoveEntityEffect(SPacketRemoveEntityEffect packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = packetIn.getEntity((World)this.clientWorldController);
    if (entity instanceof EntityLivingBase)
      ((EntityLivingBase)entity).removeActivePotionEffect(packetIn.getPotion()); 
  }
  
  public void handlePlayerListItem(SPacketPlayerListItem packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    for (SPacketPlayerListItem.AddPlayerData spacketplayerlistitem$addplayerdata : packetIn.getEntries()) {
      if (packetIn.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
        this.playerInfoMap.remove(spacketplayerlistitem$addplayerdata.getProfile().getId());
        continue;
      } 
      NetworkPlayerInfo networkplayerinfo = this.playerInfoMap.get(spacketplayerlistitem$addplayerdata.getProfile().getId());
      if (packetIn.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
        networkplayerinfo = new NetworkPlayerInfo(spacketplayerlistitem$addplayerdata);
        this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
      } 
      if (networkplayerinfo != null)
        switch (packetIn.getAction()) {
          case null:
            networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
            networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
          case UPDATE_GAME_MODE:
            networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
          case UPDATE_LATENCY:
            networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
          case UPDATE_DISPLAY_NAME:
            networkplayerinfo.setDisplayName(spacketplayerlistitem$addplayerdata.getDisplayName());
        }  
    } 
  }
  
  public void handleKeepAlive(SPacketKeepAlive packetIn) {
    sendPacket((Packet<?>)new CPacketKeepAlive(packetIn.getId()));
  }
  
  public void handlePlayerAbilities(SPacketPlayerAbilities packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    EntityPlayerSP entityPlayerSP = this.gameController.player;
    ((EntityPlayer)entityPlayerSP).capabilities.isFlying = packetIn.isFlying();
    ((EntityPlayer)entityPlayerSP).capabilities.isCreativeMode = packetIn.isCreativeMode();
    ((EntityPlayer)entityPlayerSP).capabilities.disableDamage = packetIn.isInvulnerable();
    ((EntityPlayer)entityPlayerSP).capabilities.allowFlying = packetIn.isAllowFlying();
    ((EntityPlayer)entityPlayerSP).capabilities.setFlySpeed(packetIn.getFlySpeed());
    ((EntityPlayer)entityPlayerSP).capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
  }
  
  public void handleTabComplete(SPacketTabComplete packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    String[] astring = packetIn.getMatches();
    Arrays.sort((Object[])astring);
    if (this.gameController.currentScreen instanceof ITabCompleter)
      ((ITabCompleter)this.gameController.currentScreen).setCompletions(astring); 
  }
  
  public void handleSoundEffect(SPacketSoundEffect packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.world.playSound((EntityPlayer)this.gameController.player, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSound(), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch());
  }
  
  public void handleCustomSound(SPacketCustomSound packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.getSoundHandler().playSound((ISound)new PositionedSoundRecord(new ResourceLocation(packetIn.getSoundName()), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch(), false, 0, ISound.AttenuationType.LINEAR, (float)packetIn.getX(), (float)packetIn.getY(), (float)packetIn.getZ()));
  }
  
  public void handleResourcePack(SPacketResourcePackSend packetIn) {
    final String s = packetIn.getURL();
    final String s1 = packetIn.getHash();
    if (validateResourcePackUrl(s))
      if (s.startsWith("level://")) {
        try {
          String s2 = URLDecoder.decode(s.substring("level://".length()), StandardCharsets.UTF_8.toString());
          File file1 = new File(this.gameController.mcDataDir, "saves");
          File file2 = new File(file1, s2);
          if (file2.isFile()) {
            this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
            Futures.addCallback(this.gameController.getResourcePackRepository().setResourcePackInstance(file2), createDownloadCallback());
            return;
          } 
        } catch (UnsupportedEncodingException unsupportedEncodingException) {}
        this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
      } else {
        ServerData serverdata = this.gameController.getCurrentServerData();
        if (serverdata != null && serverdata.getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
          this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
          Futures.addCallback(this.gameController.getResourcePackRepository().downloadResourcePack(s, s1), createDownloadCallback());
        } else if (serverdata != null && serverdata.getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
          this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
        } else {
          this.gameController.addScheduledTask(new Runnable() {
                public void run() {
                  NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen)new GuiYesNo(new GuiYesNoCallback() {
                          public void confirmClicked(boolean result, int id) {
                            (NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this)).gameController = Minecraft.getMinecraft();
                            ServerData serverdata1 = (NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this)).gameController.getCurrentServerData();
                            if (result) {
                              if (serverdata1 != null)
                                serverdata1.setResourceMode(ServerData.ServerResourceMode.ENABLED); 
                              (NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this)).netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                              Futures.addCallback((NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this)).gameController.getResourcePackRepository().downloadResourcePack(s, s1), NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this).createDownloadCallback());
                            } else {
                              if (serverdata1 != null)
                                serverdata1.setResourceMode(ServerData.ServerResourceMode.DISABLED); 
                              (NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this)).netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                            } 
                            ServerList.saveSingleServer(serverdata1);
                            (NetHandlerPlayClient.null.access$0(NetHandlerPlayClient.null.this)).gameController.displayGuiScreen(null);
                          }
                        }I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
                }
              });
        } 
      }  
  }
  
  private boolean validateResourcePackUrl(String p_189688_1_) {
    try {
      URI uri = new URI(p_189688_1_);
      String s = uri.getScheme();
      boolean flag = "level".equals(s);
      if (!"http".equals(s) && !"https".equals(s) && !flag)
        throw new URISyntaxException(p_189688_1_, "Wrong protocol"); 
      if (!flag || (!p_189688_1_.contains("..") && p_189688_1_.endsWith("/resources.zip")))
        return true; 
      throw new URISyntaxException(p_189688_1_, "Invalid levelstorage resourcepack path");
    } catch (URISyntaxException var5) {
      this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
      return false;
    } 
  }
  
  private FutureCallback<Object> createDownloadCallback() {
    return new FutureCallback<Object>() {
        public void onSuccess(@Nullable Object p_onSuccess_1_) {
          NetHandlerPlayClient.this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
        }
        
        public void onFailure(Throwable p_onFailure_1_) {
          NetHandlerPlayClient.this.netManager.sendPacket((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
        }
      };
  }
  
  public void handleUpdateEntityNBT(SPacketUpdateBossInfo packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    this.gameController.ingameGUI.getBossOverlay().read(packetIn);
  }
  
  public void handleCooldown(SPacketCooldown packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.getTicks() == 0) {
      this.gameController.player.getCooldownTracker().removeCooldown(packetIn.getItem());
    } else {
      this.gameController.player.getCooldownTracker().setCooldown(packetIn.getItem(), packetIn.getTicks());
    } 
  }
  
  public void handleMoveVehicle(SPacketMoveVehicle packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.gameController.player.getLowestRidingEntity();
    if (entity != this.gameController.player && entity.canPassengerSteer()) {
      entity.setPositionAndRotation(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getYaw(), packetIn.getPitch());
      this.netManager.sendPacket((Packet)new CPacketVehicleMove(entity));
    } 
  }
  
  public void handleCustomPayload(SPacketCustomPayload packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if ("MC|TrList".equals(packetIn.getChannelName())) {
      PacketBuffer packetbuffer = packetIn.getBufferData();
      try {
        int k = packetbuffer.readInt();
        GuiScreen guiscreen = this.gameController.currentScreen;
        if (guiscreen != null && guiscreen instanceof GuiMerchant && k == this.gameController.player.openContainer.windowId) {
          IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
          MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
          imerchant.setRecipes(merchantrecipelist);
        } 
      } catch (IOException ioexception) {
        LOGGER.error("Couldn't load trade info", ioexception);
      } finally {
        packetbuffer.release();
      } 
    } else if ("MC|Brand".equals(packetIn.getChannelName())) {
      this.gameController.player.setServerBrand(packetIn.getBufferData().readStringFromBuffer(32767));
    } else if ("MC|BOpen".equals(packetIn.getChannelName())) {
      EnumHand enumhand = (EnumHand)packetIn.getBufferData().readEnumValue(EnumHand.class);
      ItemStack itemstack = (enumhand == EnumHand.OFF_HAND) ? this.gameController.player.getHeldItemOffhand() : this.gameController.player.getHeldItemMainhand();
      if (itemstack.getItem() == Items.WRITTEN_BOOK)
        this.gameController.displayGuiScreen((GuiScreen)new GuiScreenBook((EntityPlayer)this.gameController.player, itemstack, false)); 
    } else if ("MC|DebugPath".equals(packetIn.getChannelName())) {
      PacketBuffer packetbuffer1 = packetIn.getBufferData();
      int l = packetbuffer1.readInt();
      float f1 = packetbuffer1.readFloat();
      Path path = Path.read(packetbuffer1);
      ((DebugRendererPathfinding)this.gameController.debugRenderer.debugRendererPathfinding).addPath(l, path, f1);
    } else if ("MC|DebugNeighborsUpdate".equals(packetIn.getChannelName())) {
      PacketBuffer packetbuffer2 = packetIn.getBufferData();
      long i1 = packetbuffer2.readVarLong();
      BlockPos blockpos = packetbuffer2.readBlockPos();
      ((DebugRendererNeighborsUpdate)this.gameController.debugRenderer.field_191557_f).func_191553_a(i1, blockpos);
    } else if ("MC|StopSound".equals(packetIn.getChannelName())) {
      PacketBuffer packetbuffer3 = packetIn.getBufferData();
      String s = packetbuffer3.readStringFromBuffer(32767);
      String s1 = packetbuffer3.readStringFromBuffer(256);
      this.gameController.getSoundHandler().stop(s1, SoundCategory.getByName(s));
    } 
  }
  
  public void handleScoreboardObjective(SPacketScoreboardObjective packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Scoreboard scoreboard = this.clientWorldController.getScoreboard();
    if (packetIn.getAction() == 0) {
      ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.getObjectiveName(), IScoreCriteria.DUMMY);
      scoreobjective.setDisplayName(packetIn.getObjectiveValue());
      scoreobjective.setRenderType(packetIn.getRenderType());
    } else {
      ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.getObjectiveName());
      if (packetIn.getAction() == 1) {
        scoreboard.removeObjective(scoreobjective1);
      } else if (packetIn.getAction() == 2) {
        scoreobjective1.setDisplayName(packetIn.getObjectiveValue());
        scoreobjective1.setRenderType(packetIn.getRenderType());
      } 
    } 
  }
  
  public void handleUpdateScore(SPacketUpdateScore packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Scoreboard scoreboard = this.clientWorldController.getScoreboard();
    ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());
    if (packetIn.getScoreAction() == SPacketUpdateScore.Action.CHANGE) {
      Score score = scoreboard.getOrCreateScore(packetIn.getPlayerName(), scoreobjective);
      score.setScorePoints(packetIn.getScoreValue());
    } else if (packetIn.getScoreAction() == SPacketUpdateScore.Action.REMOVE) {
      if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName())) {
        scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
      } else if (scoreobjective != null) {
        scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
      } 
    } 
  }
  
  public void handleDisplayObjective(SPacketDisplayObjective packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Scoreboard scoreboard = this.clientWorldController.getScoreboard();
    if (packetIn.getName().isEmpty()) {
      scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), null);
    } else {
      ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getName());
      scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), scoreobjective);
    } 
  }
  
  public void handleTeams(SPacketTeams packetIn) {
    ScorePlayerTeam scoreplayerteam;
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Scoreboard scoreboard = this.clientWorldController.getScoreboard();
    if (packetIn.getAction() == 0) {
      scoreplayerteam = scoreboard.createTeam(packetIn.getName());
    } else {
      scoreplayerteam = scoreboard.getTeam(packetIn.getName());
    } 
    if (packetIn.getAction() == 0 || packetIn.getAction() == 2) {
      scoreplayerteam.setTeamName(packetIn.getDisplayName());
      scoreplayerteam.setNamePrefix(packetIn.getPrefix());
      scoreplayerteam.setNameSuffix(packetIn.getSuffix());
      scoreplayerteam.setChatFormat(TextFormatting.fromColorIndex(packetIn.getColor()));
      scoreplayerteam.setFriendlyFlags(packetIn.getFriendlyFlags());
      Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(packetIn.getNameTagVisibility());
      if (team$enumvisible != null)
        scoreplayerteam.setNameTagVisibility(team$enumvisible); 
      Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(packetIn.getCollisionRule());
      if (team$collisionrule != null)
        scoreplayerteam.setCollisionRule(team$collisionrule); 
    } 
    if (packetIn.getAction() == 0 || packetIn.getAction() == 3)
      for (String s : packetIn.getPlayers())
        scoreboard.addPlayerToTeam(s, packetIn.getName());  
    if (packetIn.getAction() == 4)
      for (String s1 : packetIn.getPlayers())
        scoreboard.removePlayerFromTeam(s1, scoreplayerteam);  
    if (packetIn.getAction() == 1)
      scoreboard.removeTeam(scoreplayerteam); 
  }
  
  public void handleParticles(SPacketParticles packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    if (packetIn.getParticleCount() == 0) {
      double d0 = (packetIn.getParticleSpeed() * packetIn.getXOffset());
      double d2 = (packetIn.getParticleSpeed() * packetIn.getYOffset());
      double d4 = (packetIn.getParticleSpeed() * packetIn.getZOffset());
      try {
        this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
      } catch (Throwable var17) {
        LOGGER.warn("Could not spawn particle effect {}", packetIn.getParticleType());
      } 
    } else {
      for (int k = 0; k < packetIn.getParticleCount(); k++) {
        double d1 = this.avRandomizer.nextGaussian() * packetIn.getXOffset();
        double d3 = this.avRandomizer.nextGaussian() * packetIn.getYOffset();
        double d5 = this.avRandomizer.nextGaussian() * packetIn.getZOffset();
        double d6 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
        double d7 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
        double d8 = this.avRandomizer.nextGaussian() * packetIn.getParticleSpeed();
        try {
          this.clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
        } catch (Throwable var16) {
          LOGGER.warn("Could not spawn particle effect {}", packetIn.getParticleType());
          return;
        } 
      } 
    } 
  }
  
  public void handleEntityProperties(SPacketEntityProperties packetIn) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)packetIn, (INetHandler)this, (IThreadListener)this.gameController);
    Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());
    if (entity != null) {
      if (!(entity instanceof EntityLivingBase))
        throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")"); 
      AbstractAttributeMap abstractattributemap = ((EntityLivingBase)entity).getAttributeMap();
      for (SPacketEntityProperties.Snapshot spacketentityproperties$snapshot : packetIn.getSnapshots()) {
        IAttributeInstance iattributeinstance = abstractattributemap.getAttributeInstanceByName(spacketentityproperties$snapshot.getName());
        if (iattributeinstance == null)
          iattributeinstance = abstractattributemap.registerAttribute((IAttribute)new RangedAttribute(null, spacketentityproperties$snapshot.getName(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE)); 
        iattributeinstance.setBaseValue(spacketentityproperties$snapshot.getBaseValue());
        iattributeinstance.removeAllModifiers();
        for (AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers())
          iattributeinstance.applyModifier(attributemodifier); 
      } 
    } 
  }
  
  public void func_194307_a(SPacketPlaceGhostRecipe p_194307_1_) {
    PacketThreadUtil.checkThreadAndEnqueue((Packet)p_194307_1_, (INetHandler)this, (IThreadListener)this.gameController);
    Container container = this.gameController.player.openContainer;
    if (container.windowId == p_194307_1_.func_194313_b() && container.getCanCraft((EntityPlayer)this.gameController.player))
      if (this.gameController.currentScreen instanceof IRecipeShownListener) {
        GuiRecipeBook guirecipebook = ((IRecipeShownListener)this.gameController.currentScreen).func_194310_f();
        guirecipebook.func_193951_a(p_194307_1_.func_194311_a(), container.inventorySlots);
      }  
  }
  
  public NetworkManager getNetworkManager() {
    return this.netManager;
  }
  
  public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
    return this.playerInfoMap.values();
  }
  
  public NetworkPlayerInfo getPlayerInfo(UUID uniqueId) {
    return this.playerInfoMap.get(uniqueId);
  }
  
  @Nullable
  public NetworkPlayerInfo getPlayerInfo(String name) {
    for (NetworkPlayerInfo networkplayerinfo : this.playerInfoMap.values()) {
      if (networkplayerinfo.getGameProfile().getName().equals(name))
        return networkplayerinfo; 
    } 
    return null;
  }
  
  public GameProfile getGameProfile() {
    return this.profile;
  }
  
  public ClientAdvancementManager func_191982_f() {
    return this.field_191983_k;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\network\NetHandlerPlayClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */