package shadersmod.client;

import java.io.InputStream;

public class ShaderPackNone implements IShaderPack {
  public void close() {}
  
  public InputStream getResourceAsStream(String resName) {
    return null;
  }
  
  public boolean hasDirectory(String name) {
    return false;
  }
  
  public String getName() {
    return Shaders.packNameNone;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\ShaderPackNone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */