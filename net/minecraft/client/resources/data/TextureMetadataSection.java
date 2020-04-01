package net.minecraft.client.resources.data;

public class TextureMetadataSection implements IMetadataSection {
  private final boolean textureBlur;
  
  private final boolean textureClamp;
  
  public TextureMetadataSection(boolean textureBlurIn, boolean textureClampIn) {
    this.textureBlur = textureBlurIn;
    this.textureClamp = textureClampIn;
  }
  
  public boolean getTextureBlur() {
    return this.textureBlur;
  }
  
  public boolean getTextureClamp() {
    return this.textureClamp;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\data\TextureMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */