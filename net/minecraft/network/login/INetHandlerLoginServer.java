package net.minecraft.network.login;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;

public interface INetHandlerLoginServer extends INetHandler {
  void processLoginStart(CPacketLoginStart paramCPacketLoginStart);
  
  void processEncryptionResponse(CPacketEncryptionResponse paramCPacketEncryptionResponse);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\login\INetHandlerLoginServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */