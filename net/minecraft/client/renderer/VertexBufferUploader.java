package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader {
  private VertexBuffer vertexBuffer;
  
  public void draw(BufferBuilder vertexBufferIn) {
    vertexBufferIn.reset();
    this.vertexBuffer.bufferData(vertexBufferIn.getByteBuffer());
  }
  
  public void setVertexBuffer(VertexBuffer vertexBufferIn) {
    this.vertexBuffer = vertexBufferIn;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\VertexBufferUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */