package optifine;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class RenderInfoLazy {
  private RenderChunk renderChunk;
  
  private RenderGlobal.ContainerLocalRenderInformation renderInfo;
  
  public RenderChunk getRenderChunk() {
    return this.renderChunk;
  }
  
  public void setRenderChunk(RenderChunk p_setRenderChunk_1_) {
    this.renderChunk = p_setRenderChunk_1_;
    this.renderInfo = null;
  }
  
  public RenderGlobal.ContainerLocalRenderInformation getRenderInfo() {
    if (this.renderInfo == null)
      this.renderInfo = new RenderGlobal.ContainerLocalRenderInformation(this.renderChunk, null, 0); 
    return this.renderInfo;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\RenderInfoLazy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */