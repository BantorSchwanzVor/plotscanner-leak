package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapGenStructureIO {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final Map<String, Class<? extends StructureStart>> startNameToClassMap = Maps.newHashMap();
  
  private static final Map<Class<? extends StructureStart>, String> startClassToNameMap = Maps.newHashMap();
  
  private static final Map<String, Class<? extends StructureComponent>> componentNameToClassMap = Maps.newHashMap();
  
  private static final Map<Class<? extends StructureComponent>, String> componentClassToNameMap = Maps.newHashMap();
  
  private static void registerStructure(Class<? extends StructureStart> startClass, String structureName) {
    startNameToClassMap.put(structureName, startClass);
    startClassToNameMap.put(startClass, structureName);
  }
  
  static void registerStructureComponent(Class<? extends StructureComponent> componentClass, String componentName) {
    componentNameToClassMap.put(componentName, componentClass);
    componentClassToNameMap.put(componentClass, componentName);
  }
  
  public static String getStructureStartName(StructureStart start) {
    return startClassToNameMap.get(start.getClass());
  }
  
  public static String getStructureComponentName(StructureComponent component) {
    return componentClassToNameMap.get(component.getClass());
  }
  
  @Nullable
  public static StructureStart getStructureStart(NBTTagCompound tagCompound, World worldIn) {
    StructureStart structurestart = null;
    try {
      Class<? extends StructureStart> oclass = startNameToClassMap.get(tagCompound.getString("id"));
      if (oclass != null)
        structurestart = oclass.newInstance(); 
    } catch (Exception exception) {
      LOGGER.warn("Failed Start with id {}", tagCompound.getString("id"));
      exception.printStackTrace();
    } 
    if (structurestart != null) {
      structurestart.readStructureComponentsFromNBT(worldIn, tagCompound);
    } else {
      LOGGER.warn("Skipping Structure with id {}", tagCompound.getString("id"));
    } 
    return structurestart;
  }
  
  public static StructureComponent getStructureComponent(NBTTagCompound tagCompound, World worldIn) {
    StructureComponent structurecomponent = null;
    try {
      Class<? extends StructureComponent> oclass = componentNameToClassMap.get(tagCompound.getString("id"));
      if (oclass != null)
        structurecomponent = oclass.newInstance(); 
    } catch (Exception exception) {
      LOGGER.warn("Failed Piece with id {}", tagCompound.getString("id"));
      exception.printStackTrace();
    } 
    if (structurecomponent != null) {
      structurecomponent.readStructureBaseNBT(worldIn, tagCompound);
    } else {
      LOGGER.warn("Skipping Piece with id {}", tagCompound.getString("id"));
    } 
    return structurecomponent;
  }
  
  static {
    registerStructure((Class)StructureMineshaftStart.class, "Mineshaft");
    registerStructure((Class)MapGenVillage.Start.class, "Village");
    registerStructure((Class)MapGenNetherBridge.Start.class, "Fortress");
    registerStructure((Class)MapGenStronghold.Start.class, "Stronghold");
    registerStructure((Class)MapGenScatteredFeature.Start.class, "Temple");
    registerStructure((Class)StructureOceanMonument.StartMonument.class, "Monument");
    registerStructure((Class)MapGenEndCity.Start.class, "EndCity");
    registerStructure((Class)WoodlandMansion.Start.class, "Mansion");
    StructureMineshaftPieces.registerStructurePieces();
    StructureVillagePieces.registerVillagePieces();
    StructureNetherBridgePieces.registerNetherFortressPieces();
    StructureStrongholdPieces.registerStrongholdPieces();
    ComponentScatteredFeaturePieces.registerScatteredFeaturePieces();
    StructureOceanMonumentPieces.registerOceanMonumentPieces();
    StructureEndCityPieces.registerPieces();
    WoodlandMansionPieces.func_191153_a();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\structure\MapGenStructureIO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */