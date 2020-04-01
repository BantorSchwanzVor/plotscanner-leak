package net.minecraft.client.network;

import net.minecraft.client.Minecraft;

public class LanServerInfo {
  private final String lanServerMotd;
  
  private final String lanServerIpPort;
  
  private long timeLastSeen;
  
  public LanServerInfo(String p_i47130_1_, String p_i47130_2_) {
    this.lanServerMotd = p_i47130_1_;
    this.lanServerIpPort = p_i47130_2_;
    this.timeLastSeen = Minecraft.getSystemTime();
  }
  
  public String getServerMotd() {
    return this.lanServerMotd;
  }
  
  public String getServerIpPort() {
    return this.lanServerIpPort;
  }
  
  public void updateLastSeen() {
    this.timeLastSeen = Minecraft.getSystemTime();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\network\LanServerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */