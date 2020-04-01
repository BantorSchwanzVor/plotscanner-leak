package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import optifine.Config;

public class RenderList extends ChunkRenderContainer {
  public void renderChunkLayer(BlockRenderLayer layer) {
    if (this.initialized) {
      if (this.renderChunks.size() == 0)
        return; 
      for (RenderChunk renderchunk : this.renderChunks) {
        ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
        GlStateManager.pushMatrix();
        preRenderChunk(renderchunk);
        GlStateManager.callList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
        GlStateManager.popMatrix();
      } 
      if (Config.isMultiTexture())
        GlStateManager.bindCurrentTexture(); 
      GlStateManager.resetColor();
      this.renderChunks.clear();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\RenderList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */