package net.minecraft.world;

public class ColorizerGrass {
  private static int[] grassBuffer = new int[65536];
  
  public static void setGrassBiomeColorizer(int[] grassBufferIn) {
    grassBuffer = grassBufferIn;
  }
  
  public static int getGrassColor(double temperature, double humidity) {
    humidity *= temperature;
    int i = (int)((1.0D - temperature) * 255.0D);
    int j = (int)((1.0D - humidity) * 255.0D);
    int k = j << 8 | i;
    return (k > grassBuffer.length) ? -65281 : grassBuffer[k];
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\ColorizerGrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */