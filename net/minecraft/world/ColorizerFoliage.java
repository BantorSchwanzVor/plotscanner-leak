package net.minecraft.world;

public class ColorizerFoliage {
  private static int[] foliageBuffer = new int[65536];
  
  public static void setFoliageBiomeColorizer(int[] foliageBufferIn) {
    foliageBuffer = foliageBufferIn;
  }
  
  public static int getFoliageColor(double temperature, double humidity) {
    humidity *= temperature;
    int i = (int)((1.0D - temperature) * 255.0D);
    int j = (int)((1.0D - humidity) * 255.0D);
    return foliageBuffer[j << 8 | i];
  }
  
  public static int getFoliageColorPine() {
    return 6396257;
  }
  
  public static int getFoliageColorBirch() {
    return 8431445;
  }
  
  public static int getFoliageColorBasic() {
    return 4764952;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\ColorizerFoliage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */