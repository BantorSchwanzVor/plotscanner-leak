package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class CPacketServerQuery implements Packet<INetHandlerStatusServer> {
  public void readPacketData(PacketBuffer buf) throws IOException {}
  
  public void writePacketData(PacketBuffer buf) throws IOException {}
  
  public void processPacket(INetHandlerStatusServer handler) {
    handler.processServerQuery(this);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\status\client\CPacketServerQuery.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */