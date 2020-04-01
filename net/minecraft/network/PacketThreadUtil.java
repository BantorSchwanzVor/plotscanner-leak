package net.minecraft.network;

import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
  public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packetIn, final T processor, IThreadListener scheduler) throws ThreadQuickExitException {
    if (!scheduler.isCallingFromMinecraftThread()) {
      scheduler.addScheduledTask(new Runnable() {
            public void run() {
              packetIn.processPacket(processor);
            }
          });
      throw ThreadQuickExitException.INSTANCE;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\PacketThreadUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */