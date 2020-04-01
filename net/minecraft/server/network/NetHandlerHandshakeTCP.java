package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer {
  private final MinecraftServer server;
  
  private final NetworkManager networkManager;
  
  public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager) {
    this.server = serverIn;
    this.networkManager = netManager;
  }
  
  public void processHandshake(C00Handshake packetIn) {
    switch (packetIn.getRequestedState()) {
      case LOGIN:
        this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
        if (packetIn.getProtocolVersion() > 340) {
          TextComponentTranslation textComponentTranslation = new TextComponentTranslation("multiplayer.disconnect.outdated_server", new Object[] { "1.12.2" });
          this.networkManager.sendPacket((Packet)new SPacketDisconnect((ITextComponent)textComponentTranslation));
          this.networkManager.closeChannel((ITextComponent)textComponentTranslation);
        } else if (packetIn.getProtocolVersion() < 340) {
          TextComponentTranslation textComponentTranslation = new TextComponentTranslation("multiplayer.disconnect.outdated_client", new Object[] { "1.12.2" });
          this.networkManager.sendPacket((Packet)new SPacketDisconnect((ITextComponent)textComponentTranslation));
          this.networkManager.closeChannel((ITextComponent)textComponentTranslation);
        } else {
          this.networkManager.setNetHandler((INetHandler)new NetHandlerLoginServer(this.server, this.networkManager));
        } 
        return;
      case STATUS:
        this.networkManager.setConnectionState(EnumConnectionState.STATUS);
        this.networkManager.setNetHandler((INetHandler)new NetHandlerStatusServer(this.server, this.networkManager));
        return;
    } 
    throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
  }
  
  public void onDisconnect(ITextComponent reason) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\network\NetHandlerHandshakeTCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */