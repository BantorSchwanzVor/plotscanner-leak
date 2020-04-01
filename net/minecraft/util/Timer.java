package net.minecraft.util;

import net.minecraft.client.Minecraft;

public class Timer {
  public int elapsedTicks;
  
  public float field_194147_b;
  
  public float field_194148_c;
  
  private long lastSyncSysClock;
  
  private float field_194149_e;
  
  public Timer(float tps) {
    this.field_194149_e = 1000.0F / tps;
    this.lastSyncSysClock = Minecraft.getSystemTime();
  }
  
  public void updateTimer() {
    long i = Minecraft.getSystemTime();
    this.field_194148_c = (float)(i - this.lastSyncSysClock) / this.field_194149_e;
    this.lastSyncSysClock = i;
    this.field_194147_b += this.field_194148_c;
    this.elapsedTicks = (int)this.field_194147_b;
    this.field_194147_b -= this.elapsedTicks;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\Timer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */