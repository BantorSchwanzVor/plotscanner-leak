package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class SVertexFormatElement extends VertexFormatElement {
  int sUsage;
  
  public SVertexFormatElement(int sUsage, VertexFormatElement.EnumType type, int count) {
    super(0, type, VertexFormatElement.EnumUsage.PADDING, count);
    this.sUsage = sUsage;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\SVertexFormatElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */