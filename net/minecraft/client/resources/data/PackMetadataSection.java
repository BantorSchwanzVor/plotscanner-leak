package net.minecraft.client.resources.data;

import net.minecraft.util.text.ITextComponent;

public class PackMetadataSection implements IMetadataSection {
  private final ITextComponent packDescription;
  
  private final int packFormat;
  
  public PackMetadataSection(ITextComponent packDescriptionIn, int packFormatIn) {
    this.packDescription = packDescriptionIn;
    this.packFormat = packFormatIn;
  }
  
  public ITextComponent getPackDescription() {
    return this.packDescription;
  }
  
  public int getPackFormat() {
    return this.packFormat;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\data\PackMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */