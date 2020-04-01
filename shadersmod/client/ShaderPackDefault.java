package shadersmod.client;

import java.io.InputStream;

public class ShaderPackDefault implements IShaderPack {
  public void close() {}
  
  public InputStream getResourceAsStream(String resName) {
    return ShaderPackDefault.class.getResourceAsStream(resName);
  }
  
  public String getName() {
    return Shaders.packNameDefault;
  }
  
  public boolean hasDirectory(String name) {
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\ShaderPackDefault.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */