package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class ScaledResolution {
  private final double scaledWidthD;
  
  private final double scaledHeightD;
  
  private int scaledWidth;
  
  private int scaledHeight;
  
  private int scaleFactor;
  
  public ScaledResolution(Minecraft minecraftClient) {
    this.scaledWidth = minecraftClient.displayWidth;
    this.scaledHeight = minecraftClient.displayHeight;
    this.scaleFactor = 1;
    boolean flag = minecraftClient.isUnicode();
    int i = minecraftClient.gameSettings.guiScale;
    if (i == 0)
      i = 1000; 
    while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240)
      this.scaleFactor++; 
    if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1)
      this.scaleFactor--; 
    this.scaledWidthD = this.scaledWidth / this.scaleFactor;
    this.scaledHeightD = this.scaledHeight / this.scaleFactor;
    this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
    this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
  }
  
  public int getScaledWidth() {
    return this.scaledWidth;
  }
  
  public int getScaledHeight() {
    return this.scaledHeight;
  }
  
  public double getScaledWidth_double() {
    return this.scaledWidthD;
  }
  
  public double getScaledHeight_double() {
    return this.scaledHeightD;
  }
  
  public int getScaleFactor() {
    return this.scaleFactor;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\ScaledResolution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */