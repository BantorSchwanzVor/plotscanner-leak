package net.minecraft.client.resources.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public class MetadataSerializer {
  private final IRegistry<String, Registration<? extends IMetadataSection>> metadataSectionSerializerRegistry = (IRegistry<String, Registration<? extends IMetadataSection>>)new RegistrySimple();
  
  private final GsonBuilder gsonBuilder = new GsonBuilder();
  
  private Gson gson;
  
  public MetadataSerializer() {
    this.gsonBuilder.registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer());
    this.gsonBuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
    this.gsonBuilder.registerTypeAdapterFactory((TypeAdapterFactory)new EnumTypeAdapterFactory());
  }
  
  public <T extends IMetadataSection> void registerMetadataSectionType(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazz) {
    this.metadataSectionSerializerRegistry.putObject(metadataSectionSerializer.getSectionName(), new Registration<>(metadataSectionSerializer, clazz, null));
    this.gsonBuilder.registerTypeAdapter(clazz, metadataSectionSerializer);
    this.gson = null;
  }
  
  public <T extends IMetadataSection> T parseMetadataSection(String sectionName, JsonObject json) {
    if (sectionName == null)
      throw new IllegalArgumentException("Metadata section name cannot be null"); 
    if (!json.has(sectionName))
      return null; 
    if (!json.get(sectionName).isJsonObject())
      throw new IllegalArgumentException("Invalid metadata for '" + sectionName + "' - expected object, found " + json.get(sectionName)); 
    Registration<?> registration = (Registration)this.metadataSectionSerializerRegistry.getObject(sectionName);
    if (registration == null)
      throw new IllegalArgumentException("Don't know how to handle metadata section '" + sectionName + "'"); 
    return (T)getGson().fromJson((JsonElement)json.getAsJsonObject(sectionName), registration.clazz);
  }
  
  private Gson getGson() {
    if (this.gson == null)
      this.gson = this.gsonBuilder.create(); 
    return this.gson;
  }
  
  class Registration<T extends IMetadataSection> {
    final IMetadataSectionSerializer<T> section;
    
    final Class<T> clazz;
    
    private Registration(IMetadataSectionSerializer<T> metadataSectionSerializer, Class<T> clazzToRegister) {
      this.section = metadataSectionSerializer;
      this.clazz = clazzToRegister;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\data\MetadataSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */