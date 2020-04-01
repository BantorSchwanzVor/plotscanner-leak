package net.minecraft.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
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
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import org.apache.logging.log4j.LogManager;

public enum EnumConnectionState {
  HANDSHAKING(-1) {
    EnumConnectionState(int $anonymous0) {
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)C00Handshake.class);
    }
  },
  PLAY(0) {
    EnumConnectionState(int $anonymous0) {
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnObject.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnExperienceOrb.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnGlobalEntity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnMob.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnPainting.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnPlayer.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketAnimation.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketStatistics.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketBlockBreakAnim.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketUpdateTileEntity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketBlockAction.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketBlockChange.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketUpdateBossInfo.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketServerDifficulty.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketTabComplete.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketChat.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketMultiBlockChange.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketConfirmTransaction.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCloseWindow.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketOpenWindow.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketWindowItems.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketWindowProperty.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSetSlot.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCooldown.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCustomPayload.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCustomSound.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketDisconnect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityStatus.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketExplosion.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketUnloadChunk.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketChangeGameState.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketKeepAlive.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketChunkData.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketParticles.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketJoinGame.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketMaps.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntity.S15PacketEntityRelMove.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntity.S17PacketEntityLookMove.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntity.S16PacketEntityLook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketMoveVehicle.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSignEditorOpen.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketPlaceGhostRecipe.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketPlayerAbilities.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCombatEvent.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketPlayerListItem.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketPlayerPosLook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketUseBed.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketRecipeBook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketDestroyEntities.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketRemoveEntityEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketResourcePackSend.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketRespawn.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityHeadLook.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSelectAdvancementsTab.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketWorldBorder.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCamera.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketHeldItemChange.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketDisplayObjective.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityMetadata.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityAttach.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityVelocity.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityEquipment.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSetExperience.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketUpdateHealth.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketScoreboardObjective.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSetPassengers.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketTeams.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketUpdateScore.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSpawnPosition.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketTimeUpdate.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketTitle.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketSoundEffect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketPlayerListHeaderFooter.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketCollectItem.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityTeleport.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketAdvancementInfo.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityProperties.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEntityEffect.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketConfirmTeleport.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketTabComplete.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketChatMessage.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketClientStatus.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketClientSettings.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketConfirmTransaction.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketEnchantItem.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketClickWindow.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketCloseWindow.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketCustomPayload.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketUseEntity.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketKeepAlive.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayer.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayer.Position.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayer.PositionRotation.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayer.Rotation.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketVehicleMove.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketSteerBoat.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlaceRecipe.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayerAbilities.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayerDigging.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketEntityAction.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketInput.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketRecipeInfo.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketResourcePackStatus.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketSeenAdvancements.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketHeldItemChange.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketCreativeInventoryAction.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketUpdateSign.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketAnimation.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketSpectate.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayerTryUseItemOnBlock.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPlayerTryUseItem.class);
    }
  },
  STATUS(1) {
    EnumConnectionState(int $anonymous0) {
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketServerQuery.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketServerInfo.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketPing.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketPong.class);
    }
  },
  LOGIN(2) {
    EnumConnectionState(int $anonymous0) {
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketDisconnect.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEncryptionRequest.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketLoginSuccess.class);
      registerPacket(EnumPacketDirection.CLIENTBOUND, (Class)SPacketEnableCompression.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketLoginStart.class);
      registerPacket(EnumPacketDirection.SERVERBOUND, (Class)CPacketEncryptionResponse.class);
    }
  };
  
  private static final EnumConnectionState[] STATES_BY_ID;
  
  private static final Map<Class<? extends Packet<?>>, EnumConnectionState> STATES_BY_CLASS;
  
  private final int id;
  
  private final Map<EnumPacketDirection, BiMap<Integer, Class<? extends Packet<?>>>> directionMaps;
  
  static {
    STATES_BY_ID = new EnumConnectionState[4];
    STATES_BY_CLASS = Maps.newHashMap();
    byte b;
    int i;
    EnumConnectionState[] arrayOfEnumConnectionState;
    for (i = (arrayOfEnumConnectionState = values()).length, b = 0; b < i; ) {
      EnumConnectionState enumconnectionstate = arrayOfEnumConnectionState[b];
      int j = enumconnectionstate.getId();
      if (j < -1 || j > 2)
        throw new Error("Invalid protocol ID " + Integer.toString(j)); 
      STATES_BY_ID[j - -1] = enumconnectionstate;
      for (EnumPacketDirection enumpacketdirection : enumconnectionstate.directionMaps.keySet()) {
        for (Class<? extends Packet<?>> oclass : (Iterable<Class<? extends Packet<?>>>)((BiMap)enumconnectionstate.directionMaps.get(enumpacketdirection)).values()) {
          if (STATES_BY_CLASS.containsKey(oclass) && STATES_BY_CLASS.get(oclass) != enumconnectionstate)
            throw new Error("Packet " + oclass + " is already assigned to protocol " + STATES_BY_CLASS.get(oclass) + " - can't reassign to " + enumconnectionstate); 
          try {
            oclass.newInstance();
          } catch (Throwable var10) {
            throw new Error("Packet " + oclass + " fails instantiation checks! " + oclass);
          } 
          STATES_BY_CLASS.put(oclass, enumconnectionstate);
        } 
      } 
      b++;
    } 
  }
  
  EnumConnectionState(int protocolId) {
    this.directionMaps = Maps.newEnumMap(EnumPacketDirection.class);
    this.id = protocolId;
  }
  
  protected EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet<?>> packetClass) {
    HashBiMap hashBiMap;
    BiMap<Integer, Class<? extends Packet<?>>> bimap = this.directionMaps.get(direction);
    if (bimap == null) {
      hashBiMap = HashBiMap.create();
      this.directionMaps.put(direction, hashBiMap);
    } 
    if (hashBiMap.containsValue(packetClass)) {
      String s = direction + " packet " + packetClass + " is already known to ID " + hashBiMap.inverse().get(packetClass);
      LogManager.getLogger().fatal(s);
      throw new IllegalArgumentException(s);
    } 
    hashBiMap.put(Integer.valueOf(hashBiMap.size()), packetClass);
    return this;
  }
  
  public Integer getPacketId(EnumPacketDirection direction, Packet<?> packetIn) throws Exception {
    return (Integer)((BiMap)this.directionMaps.get(direction)).inverse().get(packetIn.getClass());
  }
  
  @Nullable
  public Packet<?> getPacket(EnumPacketDirection direction, int packetId) throws InstantiationException, IllegalAccessException {
    Class<? extends Packet<?>> oclass = (Class<? extends Packet<?>>)((BiMap)this.directionMaps.get(direction)).get(Integer.valueOf(packetId));
    return (oclass == null) ? null : oclass.newInstance();
  }
  
  public int getId() {
    return this.id;
  }
  
  public static EnumConnectionState getById(int stateId) {
    return (stateId >= -1 && stateId <= 2) ? STATES_BY_ID[stateId - -1] : null;
  }
  
  public static EnumConnectionState getFromPacket(Packet<?> packetIn) {
    return STATES_BY_CLASS.get(packetIn.getClass());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\EnumConnectionState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */