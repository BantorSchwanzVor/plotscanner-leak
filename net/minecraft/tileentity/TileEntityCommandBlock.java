package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity {
  private boolean powered;
  
  private boolean auto;
  
  private boolean conditionMet;
  
  private boolean sendToClient;
  
  private final CommandBlockBaseLogic commandBlockLogic;
  
  public TileEntityCommandBlock() {
    this.commandBlockLogic = new CommandBlockBaseLogic() {
        public BlockPos getPosition() {
          return TileEntityCommandBlock.this.pos;
        }
        
        public Vec3d getPositionVector() {
          return new Vec3d(TileEntityCommandBlock.this.pos.getX() + 0.5D, TileEntityCommandBlock.this.pos.getY() + 0.5D, TileEntityCommandBlock.this.pos.getZ() + 0.5D);
        }
        
        public World getEntityWorld() {
          return TileEntityCommandBlock.this.getWorld();
        }
        
        public void setCommand(String command) {
          super.setCommand(command);
          TileEntityCommandBlock.this.markDirty();
        }
        
        public void updateCommand() {
          IBlockState iblockstate = TileEntityCommandBlock.this.world.getBlockState(TileEntityCommandBlock.this.pos);
          TileEntityCommandBlock.this.getWorld().notifyBlockUpdate(TileEntityCommandBlock.this.pos, iblockstate, iblockstate, 3);
        }
        
        public int getCommandBlockType() {
          return 0;
        }
        
        public void fillInInfo(ByteBuf buf) {
          buf.writeInt(TileEntityCommandBlock.this.pos.getX());
          buf.writeInt(TileEntityCommandBlock.this.pos.getY());
          buf.writeInt(TileEntityCommandBlock.this.pos.getZ());
        }
        
        public MinecraftServer getServer() {
          return TileEntityCommandBlock.this.world.getMinecraftServer();
        }
      };
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    this.commandBlockLogic.writeToNBT(compound);
    compound.setBoolean("powered", isPowered());
    compound.setBoolean("conditionMet", isConditionMet());
    compound.setBoolean("auto", isAuto());
    return compound;
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.commandBlockLogic.readDataFromNBT(compound);
    this.powered = compound.getBoolean("powered");
    this.conditionMet = compound.getBoolean("conditionMet");
    setAuto(compound.getBoolean("auto"));
  }
  
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    if (isSendToClient()) {
      setSendToClient(false);
      NBTTagCompound nbttagcompound = writeToNBT(new NBTTagCompound());
      return new SPacketUpdateTileEntity(this.pos, 2, nbttagcompound);
    } 
    return null;
  }
  
  public boolean onlyOpsCanSetNbt() {
    return true;
  }
  
  public CommandBlockBaseLogic getCommandBlockLogic() {
    return this.commandBlockLogic;
  }
  
  public CommandResultStats getCommandResultStats() {
    return this.commandBlockLogic.getCommandResultStats();
  }
  
  public void setPowered(boolean poweredIn) {
    this.powered = poweredIn;
  }
  
  public boolean isPowered() {
    return this.powered;
  }
  
  public boolean isAuto() {
    return this.auto;
  }
  
  public void setAuto(boolean autoIn) {
    boolean flag = this.auto;
    this.auto = autoIn;
    if (!flag && autoIn && !this.powered && this.world != null && getMode() != Mode.SEQUENCE) {
      Block block = getBlockType();
      if (block instanceof BlockCommandBlock) {
        setConditionMet();
        this.world.scheduleUpdate(this.pos, block, block.tickRate(this.world));
      } 
    } 
  }
  
  public boolean isConditionMet() {
    return this.conditionMet;
  }
  
  public boolean setConditionMet() {
    this.conditionMet = true;
    if (isConditional()) {
      BlockPos blockpos = this.pos.offset(((EnumFacing)this.world.getBlockState(this.pos).getValue((IProperty)BlockCommandBlock.FACING)).getOpposite());
      if (this.world.getBlockState(blockpos).getBlock() instanceof BlockCommandBlock) {
        TileEntity tileentity = this.world.getTileEntity(blockpos);
        this.conditionMet = (tileentity instanceof TileEntityCommandBlock && ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() > 0);
      } else {
        this.conditionMet = false;
      } 
    } 
    return this.conditionMet;
  }
  
  public boolean isSendToClient() {
    return this.sendToClient;
  }
  
  public void setSendToClient(boolean p_184252_1_) {
    this.sendToClient = p_184252_1_;
  }
  
  public Mode getMode() {
    Block block = getBlockType();
    if (block == Blocks.COMMAND_BLOCK)
      return Mode.REDSTONE; 
    if (block == Blocks.REPEATING_COMMAND_BLOCK)
      return Mode.AUTO; 
    return (block == Blocks.CHAIN_COMMAND_BLOCK) ? Mode.SEQUENCE : Mode.REDSTONE;
  }
  
  public boolean isConditional() {
    IBlockState iblockstate = this.world.getBlockState(getPos());
    return (iblockstate.getBlock() instanceof BlockCommandBlock) ? ((Boolean)iblockstate.getValue((IProperty)BlockCommandBlock.CONDITIONAL)).booleanValue() : false;
  }
  
  public void validate() {
    this.blockType = null;
    super.validate();
  }
  
  public enum Mode {
    SEQUENCE, AUTO, REDSTONE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */