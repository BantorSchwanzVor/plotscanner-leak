package net.minecraft.network.handshake;

import net.minecraft.network.INetHandler;
import net.minecraft.network.handshake.client.C00Handshake;

public interface INetHandlerHandshakeServer extends INetHandler {
  void processHandshake(C00Handshake paramC00Handshake);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\handshake\INetHandlerHandshakeServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */