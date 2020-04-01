package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketTabComplete implements Packet<INetHandlerPlayClient> {
  private String[] matches;
  
  public SPacketTabComplete() {}
  
  public SPacketTabComplete(String[] matchesIn) {
    this.matches = matchesIn;
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.matches = new String[buf.readVarIntFromBuffer()];
    for (int i = 0; i < this.matches.length; i++)
      this.matches[i] = buf.readStringFromBuffer(32767); 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.matches.length);
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = this.matches).length, b = 0; b < i; ) {
      String s = arrayOfString[b];
      buf.writeString(s);
      b++;
    } 
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleTabComplete(this);
  }
  
  public String[] getMatches() {
    return this.matches;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketTabComplete.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */