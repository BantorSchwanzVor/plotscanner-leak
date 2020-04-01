package net.minecraft.world;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum DimensionType {
  OVERWORLD(0, "overworld", "", (Class)WorldProviderSurface.class),
  NETHER(-1, "the_nether", "_nether", (Class)WorldProviderHell.class),
  THE_END(1, "the_end", "_end", (Class)WorldProviderEnd.class);
  
  private final int id;
  
  private final String name;
  
  private final String suffix;
  
  private final Class<? extends WorldProvider> clazz;
  
  DimensionType(int idIn, String nameIn, String suffixIn, Class<? extends WorldProvider> clazzIn) {
    this.id = idIn;
    this.name = nameIn;
    this.suffix = suffixIn;
    this.clazz = clazzIn;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getSuffix() {
    return this.suffix;
  }
  
  public WorldProvider createDimension() {
    try {
      Constructor<? extends WorldProvider> constructor = this.clazz.getConstructor(new Class[0]);
      return constructor.newInstance(new Object[0]);
    } catch (NoSuchMethodException nosuchmethodexception) {
      throw new Error("Could not create new dimension", nosuchmethodexception);
    } catch (InvocationTargetException invocationtargetexception) {
      throw new Error("Could not create new dimension", invocationtargetexception);
    } catch (InstantiationException instantiationexception) {
      throw new Error("Could not create new dimension", instantiationexception);
    } catch (IllegalAccessException illegalaccessexception) {
      throw new Error("Could not create new dimension", illegalaccessexception);
    } 
  }
  
  public static DimensionType getById(int id) {
    byte b;
    int i;
    DimensionType[] arrayOfDimensionType;
    for (i = (arrayOfDimensionType = values()).length, b = 0; b < i; ) {
      DimensionType dimensiontype = arrayOfDimensionType[b];
      if (dimensiontype.getId() == id)
        return dimensiontype; 
      b++;
    } 
    throw new IllegalArgumentException("Invalid dimension id " + id);
  }
  
  public static DimensionType func_193417_a(String p_193417_0_) {
    byte b;
    int i;
    DimensionType[] arrayOfDimensionType;
    for (i = (arrayOfDimensionType = values()).length, b = 0; b < i; ) {
      DimensionType dimensiontype = arrayOfDimensionType[b];
      if (dimensiontype.getName().equals(p_193417_0_))
        return dimensiontype; 
      b++;
    } 
    throw new IllegalArgumentException("Invalid dimension " + p_193417_0_);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\DimensionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */