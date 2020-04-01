package net.minecraft.world.storage;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData {
  void writePlayerData(EntityPlayer paramEntityPlayer);
  
  @Nullable
  NBTTagCompound readPlayerData(EntityPlayer paramEntityPlayer);
  
  String[] getAvailablePlayerDat();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\IPlayerFileData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */