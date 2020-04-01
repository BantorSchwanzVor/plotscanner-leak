package shadersmod.client;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;

public class DefaultTexture extends AbstractTexture {
  public DefaultTexture() {
    loadTexture(null);
  }
  
  public void loadTexture(IResourceManager resourcemanager) {
    int[] aint = ShadersTex.createAIntImage(1, -1);
    ShadersTex.setupTexture(getMultiTexID(), aint, 1, 1, false, false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\DefaultTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */