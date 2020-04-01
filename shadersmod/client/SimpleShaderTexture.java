package shadersmod.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import org.apache.commons.io.IOUtils;
import shadersmod.common.SMCLog;

public class SimpleShaderTexture extends AbstractTexture {
  private String texturePath;
  
  private static final MetadataSerializer METADATA_SERIALIZER = makeMetadataSerializer();
  
  public SimpleShaderTexture(String texturePath) {
    this.texturePath = texturePath;
  }
  
  public void loadTexture(IResourceManager resourceManager) throws IOException {
    deleteGlTexture();
    InputStream inputstream = Shaders.getShaderPackResourceStream(this.texturePath);
    if (inputstream == null)
      throw new FileNotFoundException("Shader texture not found: " + this.texturePath); 
    try {
      BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
      TextureMetadataSection texturemetadatasection = loadTextureMetadataSection();
      TextureUtil.uploadTextureImageAllocate(getGlTextureId(), bufferedimage, texturemetadatasection.getTextureBlur(), texturemetadatasection.getTextureClamp());
    } finally {
      IOUtils.closeQuietly(inputstream);
    } 
  }
  
  private TextureMetadataSection loadTextureMetadataSection() {
    String s = String.valueOf(this.texturePath) + ".mcmeta";
    String s1 = "texture";
    InputStream inputstream = Shaders.getShaderPackResourceStream(s);
    if (inputstream != null) {
      TextureMetadataSection texturemetadatasection1;
      MetadataSerializer metadataserializer = METADATA_SERIALIZER;
      BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
      try {
        JsonObject jsonobject = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
        TextureMetadataSection texturemetadatasection = (TextureMetadataSection)metadataserializer.parseMetadataSection(s1, jsonobject);
      } catch (RuntimeException runtimeexception) {
        SMCLog.warning("Error reading metadata: " + s);
        SMCLog.warning(runtimeexception.getClass().getName() + ": " + runtimeexception.getMessage());
        return new TextureMetadataSection(false, false);
      } finally {
        IOUtils.closeQuietly(bufferedreader);
        IOUtils.closeQuietly(inputstream);
      } 
      IOUtils.closeQuietly(bufferedreader);
      IOUtils.closeQuietly(inputstream);
      return texturemetadatasection1;
    } 
    return new TextureMetadataSection(false, false);
  }
  
  private static MetadataSerializer makeMetadataSerializer() {
    MetadataSerializer metadataserializer = new MetadataSerializer();
    metadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
    metadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new FontMetadataSectionSerializer(), FontMetadataSection.class);
    metadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
    metadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new PackMetadataSectionSerializer(), PackMetadataSection.class);
    metadataserializer.registerMetadataSectionType((IMetadataSectionSerializer)new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    return metadataserializer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\SimpleShaderTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */