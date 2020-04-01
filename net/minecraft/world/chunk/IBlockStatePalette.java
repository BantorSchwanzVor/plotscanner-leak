package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;

public interface IBlockStatePalette {
  int idFor(IBlockState paramIBlockState);
  
  @Nullable
  IBlockState getBlockState(int paramInt);
  
  void read(PacketBuffer paramPacketBuffer);
  
  void write(PacketBuffer paramPacketBuffer);
  
  int getSerializedState();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\chunk\IBlockStatePalette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */