package net.minecraft.entity;

import java.util.UUID;
import javax.annotation.Nullable;

public interface IEntityOwnable {
  @Nullable
  UUID getOwnerId();
  
  @Nullable
  Entity getOwner();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\IEntityOwnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */