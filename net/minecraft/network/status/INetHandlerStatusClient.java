package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;

public interface INetHandlerStatusClient extends INetHandler {
  void handleServerInfo(SPacketServerInfo paramSPacketServerInfo);
  
  void handlePong(SPacketPong paramSPacketPong);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\status\INetHandlerStatusClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */