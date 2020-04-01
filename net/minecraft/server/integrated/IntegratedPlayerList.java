package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;

public class IntegratedPlayerList extends PlayerList {
  private NBTTagCompound hostPlayerData;
  
  public IntegratedPlayerList(IntegratedServer server) {
    super(server);
    setViewDistance(10);
  }
  
  protected void writePlayerData(EntityPlayerMP playerIn) {
    if (playerIn.getName().equals(getServerInstance().getServerOwner()))
      this.hostPlayerData = playerIn.writeToNBT(new NBTTagCompound()); 
    super.writePlayerData(playerIn);
  }
  
  public String allowUserToConnect(SocketAddress address, GameProfile profile) {
    return (profile.getName().equalsIgnoreCase(getServerInstance().getServerOwner()) && getPlayerByUsername(profile.getName()) != null) ? "That name is already taken." : super.allowUserToConnect(address, profile);
  }
  
  public IntegratedServer getServerInstance() {
    return (IntegratedServer)super.getServerInstance();
  }
  
  public NBTTagCompound getHostPlayerData() {
    return this.hostPlayerData;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\integrated\IntegratedPlayerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */