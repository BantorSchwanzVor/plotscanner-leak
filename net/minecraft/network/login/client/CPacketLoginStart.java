package net.minecraft.network.login.client;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginServer;

public class CPacketLoginStart implements Packet<INetHandlerLoginServer> {
  private GameProfile profile;
  
  public CPacketLoginStart() {}
  
  public CPacketLoginStart(GameProfile profileIn) {
    this.profile = profileIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.profile = new GameProfile(null, buf.readStringFromBuffer(16));
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeString(this.profile.getName());
  }
  
  public void processPacket(INetHandlerLoginServer handler) {
    handler.processLoginStart(this);
  }
  
  public GameProfile getProfile() {
    return this.profile;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\login\client\CPacketLoginStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */