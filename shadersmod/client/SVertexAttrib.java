package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class SVertexAttrib {
  public int index;
  
  public int count;
  
  public VertexFormatElement.EnumType type;
  
  public int offset;
  
  public SVertexAttrib(int index, int count, VertexFormatElement.EnumType type) {
    this.index = index;
    this.count = count;
    this.type = type;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\SVertexAttrib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */