package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BakedQuadRetextured extends BakedQuad {
  private final TextureAtlasSprite texture;
  
  private final TextureAtlasSprite spriteOld;
  
  public BakedQuadRetextured(BakedQuad quad, TextureAtlasSprite textureIn) {
    super(Arrays.copyOf(quad.getVertexData(), (quad.getVertexData()).length), quad.tintIndex, FaceBakery.getFacingFromVertexData(quad.getVertexData()), textureIn);
    this.texture = textureIn;
    this.format = quad.format;
    this.applyDiffuseLighting = quad.applyDiffuseLighting;
    this.spriteOld = quad.getSprite();
    remapQuad();
    fixVertexData();
  }
  
  private void remapQuad() {
    for (int i = 0; i < 4; i++) {
      int j = this.format.getIntegerSize() * i;
      int k = this.format.getUvOffsetById(0) / 4;
      this.vertexData[j + k] = Float.floatToRawIntBits(this.texture.getInterpolatedU(this.spriteOld.getUnInterpolatedU(Float.intBitsToFloat(this.vertexData[j + k]))));
      this.vertexData[j + k + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV(this.spriteOld.getUnInterpolatedV(Float.intBitsToFloat(this.vertexData[j + k + 1]))));
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\model\BakedQuadRetextured.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */