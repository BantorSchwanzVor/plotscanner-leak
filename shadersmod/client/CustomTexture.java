package shadersmod.client;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;

public class CustomTexture implements ICustomTexture {
  private int textureUnit = -1;
  
  private String path = null;
  
  private ITextureObject texture = null;
  
  public CustomTexture(int textureUnit, String path, ITextureObject texture) {
    this.textureUnit = textureUnit;
    this.path = path;
    this.texture = texture;
  }
  
  public int getTextureUnit() {
    return this.textureUnit;
  }
  
  public String getPath() {
    return this.path;
  }
  
  public ITextureObject getTexture() {
    return this.texture;
  }
  
  public int getTextureId() {
    return this.texture.getGlTextureId();
  }
  
  public void deleteTexture() {
    TextureUtil.deleteTexture(this.texture.getGlTextureId());
  }
  
  public String toString() {
    return "textureUnit: " + this.textureUnit + ", path: " + this.path + ", glTextureId: " + this.texture.getGlTextureId();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\CustomTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */