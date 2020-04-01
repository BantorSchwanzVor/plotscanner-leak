package net.minecraft.client.resources.data;

public class FontMetadataSection implements IMetadataSection {
  private final float[] charWidths;
  
  private final float[] charLefts;
  
  private final float[] charSpacings;
  
  public FontMetadataSection(float[] charWidthsIn, float[] charLeftsIn, float[] charSpacingsIn) {
    this.charWidths = charWidthsIn;
    this.charLefts = charLeftsIn;
    this.charSpacings = charSpacingsIn;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\data\FontMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */