package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class LegacyV2Adapter implements IResourcePack {
  private final IResourcePack field_191383_a;
  
  public LegacyV2Adapter(IResourcePack p_i47182_1_) {
    this.field_191383_a = p_i47182_1_;
  }
  
  public InputStream getInputStream(ResourceLocation location) throws IOException {
    return this.field_191383_a.getInputStream(func_191382_c(location));
  }
  
  private ResourceLocation func_191382_c(ResourceLocation p_191382_1_) {
    String s = p_191382_1_.getResourcePath();
    if (!"lang/swg_de.lang".equals(s) && s.startsWith("lang/") && s.endsWith(".lang")) {
      int i = s.indexOf('_');
      if (i != -1) {
        final String s1 = String.valueOf(s.substring(0, i + 1)) + s.substring(i + 1, s.indexOf('.', i)).toUpperCase() + ".lang";
        return new ResourceLocation(p_191382_1_.getResourceDomain(), "") {
            public String getResourcePath() {
              return s1;
            }
          };
      } 
    } 
    return p_191382_1_;
  }
  
  public boolean resourceExists(ResourceLocation location) {
    return this.field_191383_a.resourceExists(func_191382_c(location));
  }
  
  public Set<String> getResourceDomains() {
    return this.field_191383_a.getResourceDomains();
  }
  
  @Nullable
  public <T extends net.minecraft.client.resources.data.IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
    return this.field_191383_a.getPackMetadata(metadataSerializer, metadataSectionName);
  }
  
  public BufferedImage getPackImage() throws IOException {
    return this.field_191383_a.getPackImage();
  }
  
  public String getPackName() {
    return this.field_191383_a.getPackName();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\LegacyV2Adapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */