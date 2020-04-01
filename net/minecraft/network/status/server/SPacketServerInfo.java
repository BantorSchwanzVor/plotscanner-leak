package net.minecraft.network.status.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class SPacketServerInfo implements Packet<INetHandlerStatusClient> {
  private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(ServerStatusResponse.Version.class, new ServerStatusResponse.Version.Serializer()).registerTypeAdapter(ServerStatusResponse.Players.class, new ServerStatusResponse.Players.Serializer()).registerTypeAdapter(ServerStatusResponse.class, new ServerStatusResponse.Serializer()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeHierarchyAdapter(Style.class, new Style.Serializer()).registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory()).create();
  
  private ServerStatusResponse response;
  
  public SPacketServerInfo() {}
  
  public SPacketServerInfo(ServerStatusResponse responseIn) {
    this.response = responseIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.response = (ServerStatusResponse)JsonUtils.gsonDeserialize(GSON, buf.readStringFromBuffer(32767), ServerStatusResponse.class);
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeString(GSON.toJson(this.response));
  }
  
  public void processPacket(INetHandlerStatusClient handler) {
    handler.handleServerInfo(this);
  }
  
  public ServerStatusResponse getResponse() {
    return this.response;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\status\server\SPacketServerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */