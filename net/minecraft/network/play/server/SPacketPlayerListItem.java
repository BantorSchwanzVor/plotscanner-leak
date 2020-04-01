package net.minecraft.network.play.server;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

public class SPacketPlayerListItem implements Packet<INetHandlerPlayClient> {
  private Action action;
  
  private final List<AddPlayerData> players = Lists.newArrayList();
  
  public SPacketPlayerListItem(Action actionIn, EntityPlayerMP... playersIn) {
    this.action = actionIn;
    byte b;
    int i;
    EntityPlayerMP[] arrayOfEntityPlayerMP;
    for (i = (arrayOfEntityPlayerMP = playersIn).length, b = 0; b < i; ) {
      EntityPlayerMP entityplayermp = arrayOfEntityPlayerMP[b];
      this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName()));
      b++;
    } 
  }
  
  public SPacketPlayerListItem(Action actionIn, Iterable<EntityPlayerMP> playersIn) {
    this.action = actionIn;
    for (EntityPlayerMP entityplayermp : playersIn)
      this.players.add(new AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.interactionManager.getGameType(), entityplayermp.getTabListDisplayName())); 
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.action = (Action)buf.readEnumValue(Action.class);
    int i = buf.readVarIntFromBuffer();
    for (int j = 0; j < i; j++) {
      int l, i1;
      GameProfile gameprofile = null;
      int k = 0;
      GameType gametype = null;
      ITextComponent itextcomponent = null;
      switch (this.action) {
        case null:
          gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
          l = buf.readVarIntFromBuffer();
          i1 = 0;
          for (; i1 < l; i1++) {
            String s = buf.readStringFromBuffer(32767);
            String s1 = buf.readStringFromBuffer(32767);
            if (buf.readBoolean()) {
              gameprofile.getProperties().put(s, new Property(s, s1, buf.readStringFromBuffer(32767)));
            } else {
              gameprofile.getProperties().put(s, new Property(s, s1));
            } 
          } 
          gametype = GameType.getByID(buf.readVarIntFromBuffer());
          k = buf.readVarIntFromBuffer();
          if (buf.readBoolean())
            itextcomponent = buf.readTextComponent(); 
          break;
        case UPDATE_GAME_MODE:
          gameprofile = new GameProfile(buf.readUuid(), null);
          gametype = GameType.getByID(buf.readVarIntFromBuffer());
          break;
        case UPDATE_LATENCY:
          gameprofile = new GameProfile(buf.readUuid(), null);
          k = buf.readVarIntFromBuffer();
          break;
        case UPDATE_DISPLAY_NAME:
          gameprofile = new GameProfile(buf.readUuid(), null);
          if (buf.readBoolean())
            itextcomponent = buf.readTextComponent(); 
          break;
        case REMOVE_PLAYER:
          gameprofile = new GameProfile(buf.readUuid(), null);
          break;
      } 
      this.players.add(new AddPlayerData(gameprofile, k, gametype, itextcomponent));
    } 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeEnumValue(this.action);
    buf.writeVarIntToBuffer(this.players.size());
    for (AddPlayerData spacketplayerlistitem$addplayerdata : this.players) {
      switch (this.action) {
        case null:
          buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
          buf.writeString(spacketplayerlistitem$addplayerdata.getProfile().getName());
          buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getProfile().getProperties().size());
          for (Property property : spacketplayerlistitem$addplayerdata.getProfile().getProperties().values()) {
            buf.writeString(property.getName());
            buf.writeString(property.getValue());
            if (property.hasSignature()) {
              buf.writeBoolean(true);
              buf.writeString(property.getSignature());
              continue;
            } 
            buf.writeBoolean(false);
          } 
          buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getGameMode().getID());
          buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getPing());
          if (spacketplayerlistitem$addplayerdata.getDisplayName() == null) {
            buf.writeBoolean(false);
            continue;
          } 
          buf.writeBoolean(true);
          buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
        case UPDATE_GAME_MODE:
          buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
          buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getGameMode().getID());
        case UPDATE_LATENCY:
          buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
          buf.writeVarIntToBuffer(spacketplayerlistitem$addplayerdata.getPing());
        case UPDATE_DISPLAY_NAME:
          buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
          if (spacketplayerlistitem$addplayerdata.getDisplayName() == null) {
            buf.writeBoolean(false);
            continue;
          } 
          buf.writeBoolean(true);
          buf.writeTextComponent(spacketplayerlistitem$addplayerdata.getDisplayName());
        case REMOVE_PLAYER:
          buf.writeUuid(spacketplayerlistitem$addplayerdata.getProfile().getId());
      } 
    } 
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handlePlayerListItem(this);
  }
  
  public List<AddPlayerData> getEntries() {
    return this.players;
  }
  
  public Action getAction() {
    return this.action;
  }
  
  public String toString() {
    return MoreObjects.toStringHelper(this).add("action", this.action).add("entries", this.players).toString();
  }
  
  public SPacketPlayerListItem() {}
  
  public enum Action {
    ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER;
  }
  
  public class AddPlayerData {
    private final int ping;
    
    private final GameType gamemode;
    
    private final GameProfile profile;
    
    private final ITextComponent displayName;
    
    public AddPlayerData(GameProfile profileIn, int latencyIn, @Nullable GameType gameModeIn, ITextComponent displayNameIn) {
      this.profile = profileIn;
      this.ping = latencyIn;
      this.gamemode = gameModeIn;
      this.displayName = displayNameIn;
    }
    
    public GameProfile getProfile() {
      return this.profile;
    }
    
    public int getPing() {
      return this.ping;
    }
    
    public GameType getGameMode() {
      return this.gamemode;
    }
    
    @Nullable
    public ITextComponent getDisplayName() {
      return this.displayName;
    }
    
    public String toString() {
      return MoreObjects.toStringHelper(this).add("latency", this.ping).add("gameMode", this.gamemode).add("profile", this.profile).add("displayName", (this.displayName == null) ? null : ITextComponent.Serializer.componentToJson(this.displayName)).toString();
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketPlayerListItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */