package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item {
  public boolean isMap() {
    return true;
  }
  
  @Nullable
  public Packet<?> createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemMapBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */