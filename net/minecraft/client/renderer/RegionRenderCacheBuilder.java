package net.minecraft.client.renderer;

import net.minecraft.util.BlockRenderLayer;

public class RegionRenderCacheBuilder {
  private final BufferBuilder[] worldRenderers = new BufferBuilder[(BlockRenderLayer.values()).length];
  
  public RegionRenderCacheBuilder() {
    this.worldRenderers[BlockRenderLayer.SOLID.ordinal()] = new BufferBuilder(2097152);
    this.worldRenderers[BlockRenderLayer.CUTOUT.ordinal()] = new BufferBuilder(131072);
    this.worldRenderers[BlockRenderLayer.CUTOUT_MIPPED.ordinal()] = new BufferBuilder(131072);
    this.worldRenderers[BlockRenderLayer.TRANSLUCENT.ordinal()] = new BufferBuilder(262144);
  }
  
  public BufferBuilder getWorldRendererByLayer(BlockRenderLayer layer) {
    return this.worldRenderers[layer.ordinal()];
  }
  
  public BufferBuilder getWorldRendererByLayerId(int id) {
    return this.worldRenderers[id];
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\RegionRenderCacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */