package net.minecraft.world;

import javax.annotation.concurrent.Immutable;
import net.minecraft.nbt.NBTTagCompound;

@Immutable
public class LockCode {
  public static final LockCode EMPTY_CODE = new LockCode("");
  
  private final String lock;
  
  public LockCode(String code) {
    this.lock = code;
  }
  
  public boolean isEmpty() {
    return !(this.lock != null && !this.lock.isEmpty());
  }
  
  public String getLock() {
    return this.lock;
  }
  
  public void toNBT(NBTTagCompound nbt) {
    nbt.setString("Lock", this.lock);
  }
  
  public static LockCode fromNBT(NBTTagCompound nbt) {
    if (nbt.hasKey("Lock", 8)) {
      String s = nbt.getString("Lock");
      return new LockCode(s);
    } 
    return EMPTY_CODE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\LockCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */