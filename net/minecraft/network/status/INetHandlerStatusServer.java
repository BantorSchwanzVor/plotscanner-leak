package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;

public interface INetHandlerStatusServer extends INetHandler {
  void processPing(CPacketPing paramCPacketPing);
  
  void processServerQuery(CPacketServerQuery paramCPacketServerQuery);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\status\INetHandlerStatusServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */