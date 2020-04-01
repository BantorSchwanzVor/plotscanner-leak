package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketBlockChange implements Packet<INetHandlerPlayClient> {
  private BlockPos blockPosition;
  
  private IBlockState blockState;
  
  public SPacketBlockChange() {}
  
  public SPacketBlockChange(World worldIn, BlockPos posIn) {
    this.blockPosition = posIn;
    this.blockState = worldIn.getBlockState(posIn);
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.blockPosition = buf.readBlockPos();
    this.blockState = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer());
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeBlockPos(this.blockPosition);
    buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.blockState));
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleBlockChange(this);
  }
  
  public IBlockState getBlockState() {
    return this.blockState;
  }
  
  public BlockPos getBlockPosition() {
    return this.blockPosition;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketBlockChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */