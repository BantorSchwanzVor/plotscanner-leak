package net.minecraft.realms;

import net.minecraft.client.renderer.vertex.VertexFormatElement;

public class RealmsVertexFormatElement {
  private final VertexFormatElement v;
  
  public RealmsVertexFormatElement(VertexFormatElement vIn) {
    this.v = vIn;
  }
  
  public VertexFormatElement getVertexFormatElement() {
    return this.v;
  }
  
  public boolean isPosition() {
    return this.v.isPositionElement();
  }
  
  public int getIndex() {
    return this.v.getIndex();
  }
  
  public int getByteSize() {
    return this.v.getSize();
  }
  
  public int getCount() {
    return this.v.getElementCount();
  }
  
  public int hashCode() {
    return this.v.hashCode();
  }
  
  public boolean equals(Object p_equals_1_) {
    return this.v.equals(p_equals_1_);
  }
  
  public String toString() {
    return this.v.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\realms\RealmsVertexFormatElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */