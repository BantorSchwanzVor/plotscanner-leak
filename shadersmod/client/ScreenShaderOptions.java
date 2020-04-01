package shadersmod.client;

public class ScreenShaderOptions {
  private String name;
  
  private ShaderOption[] shaderOptions;
  
  private int columns;
  
  public ScreenShaderOptions(String name, ShaderOption[] shaderOptions, int columns) {
    this.name = name;
    this.shaderOptions = shaderOptions;
    this.columns = columns;
  }
  
  public String getName() {
    return this.name;
  }
  
  public ShaderOption[] getShaderOptions() {
    return this.shaderOptions;
  }
  
  public int getColumns() {
    return this.columns;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\ScreenShaderOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */