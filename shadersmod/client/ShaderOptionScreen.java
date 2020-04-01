package shadersmod.client;

public class ShaderOptionScreen extends ShaderOption {
  public ShaderOptionScreen(String name) {
    super(name, null, null, new String[0], null, null);
  }
  
  public String getNameText() {
    return Shaders.translate("screen." + getName(), getName());
  }
  
  public String getDescriptionText() {
    return Shaders.translate("screen." + getName() + ".comment", null);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\ShaderOptionScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */