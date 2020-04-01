package net.minecraft.command;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface ICommandSender {
  String getName();
  
  default ITextComponent getDisplayName() {
    return (ITextComponent)new TextComponentString(getName());
  }
  
  default void addChatMessage(ITextComponent component) {}
  
  boolean canCommandSenderUseCommand(int paramInt, String paramString);
  
  default BlockPos getPosition() {
    return BlockPos.ORIGIN;
  }
  
  default Vec3d getPositionVector() {
    return Vec3d.ZERO;
  }
  
  World getEntityWorld();
  
  @Nullable
  default Entity getCommandSenderEntity() {
    return null;
  }
  
  default boolean sendCommandFeedback() {
    return false;
  }
  
  default void setCommandStat(CommandResultStats.Type type, int amount) {}
  
  @Nullable
  MinecraftServer getServer();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\ICommandSender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */