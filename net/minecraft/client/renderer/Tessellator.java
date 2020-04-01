package net.minecraft.client.renderer;

public class Tessellator {
  private final BufferBuilder worldRenderer;
  
  private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
  
  private static final Tessellator INSTANCE = new Tessellator(2097152);
  
  public static Tessellator getInstance() {
    return INSTANCE;
  }
  
  public Tessellator(int bufferSize) {
    this.worldRenderer = new BufferBuilder(bufferSize);
  }
  
  public void draw() {
    this.worldRenderer.finishDrawing();
    this.vboUploader.draw(this.worldRenderer);
  }
  
  public BufferBuilder getBuffer() {
    return this.worldRenderer;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\Tessellator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */