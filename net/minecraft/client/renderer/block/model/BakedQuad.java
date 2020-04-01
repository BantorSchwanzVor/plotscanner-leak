package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import optifine.Config;
import optifine.QuadBounds;
import optifine.Reflector;

public class BakedQuad implements IVertexProducer {
  protected int[] vertexData;
  
  protected final int tintIndex;
  
  protected EnumFacing face;
  
  protected TextureAtlasSprite sprite;
  
  private int[] vertexDataSingle = null;
  
  protected boolean applyDiffuseLighting = Reflector.ForgeHooksClient_fillNormal.exists();
  
  protected VertexFormat format = DefaultVertexFormats.ITEM;
  
  private QuadBounds quadBounds;
  
  public BakedQuad(int[] p_i6_1_, int p_i6_2_, EnumFacing p_i6_3_, TextureAtlasSprite p_i6_4_, boolean p_i6_5_, VertexFormat p_i6_6_) {
    this.vertexData = p_i6_1_;
    this.tintIndex = p_i6_2_;
    this.face = p_i6_3_;
    this.sprite = p_i6_4_;
    this.applyDiffuseLighting = p_i6_5_;
    this.format = p_i6_6_;
    fixVertexData();
  }
  
  public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn, TextureAtlasSprite spriteIn) {
    this.vertexData = vertexDataIn;
    this.tintIndex = tintIndexIn;
    this.face = faceIn;
    this.sprite = spriteIn;
    fixVertexData();
  }
  
  public TextureAtlasSprite getSprite() {
    if (this.sprite == null)
      this.sprite = getSpriteByUv(getVertexData()); 
    return this.sprite;
  }
  
  public int[] getVertexData() {
    fixVertexData();
    return this.vertexData;
  }
  
  public boolean hasTintIndex() {
    return (this.tintIndex != -1);
  }
  
  public int getTintIndex() {
    return this.tintIndex;
  }
  
  public EnumFacing getFace() {
    if (this.face == null)
      this.face = FaceBakery.getFacingFromVertexData(getVertexData()); 
    return this.face;
  }
  
  public int[] getVertexDataSingle() {
    if (this.vertexDataSingle == null)
      this.vertexDataSingle = makeVertexDataSingle(getVertexData(), getSprite()); 
    return this.vertexDataSingle;
  }
  
  private static int[] makeVertexDataSingle(int[] p_makeVertexDataSingle_0_, TextureAtlasSprite p_makeVertexDataSingle_1_) {
    int[] aint = (int[])p_makeVertexDataSingle_0_.clone();
    int i = p_makeVertexDataSingle_1_.sheetWidth / p_makeVertexDataSingle_1_.getIconWidth();
    int j = p_makeVertexDataSingle_1_.sheetHeight / p_makeVertexDataSingle_1_.getIconHeight();
    int k = aint.length / 4;
    for (int l = 0; l < 4; l++) {
      int i1 = l * k;
      float f = Float.intBitsToFloat(aint[i1 + 4]);
      float f1 = Float.intBitsToFloat(aint[i1 + 4 + 1]);
      float f2 = p_makeVertexDataSingle_1_.toSingleU(f);
      float f3 = p_makeVertexDataSingle_1_.toSingleV(f1);
      aint[i1 + 4] = Float.floatToRawIntBits(f2);
      aint[i1 + 4 + 1] = Float.floatToRawIntBits(f3);
    } 
    return aint;
  }
  
  public void pipe(IVertexConsumer p_pipe_1_) {
    Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[] { p_pipe_1_, this });
  }
  
  public VertexFormat getFormat() {
    return this.format;
  }
  
  public boolean shouldApplyDiffuseLighting() {
    return this.applyDiffuseLighting;
  }
  
  private static TextureAtlasSprite getSpriteByUv(int[] p_getSpriteByUv_0_) {
    float f = 1.0F;
    float f1 = 1.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    int i = p_getSpriteByUv_0_.length / 4;
    for (int j = 0; j < 4; j++) {
      int k = j * i;
      float f4 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4]);
      float f5 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4 + 1]);
      f = Math.min(f, f4);
      f1 = Math.min(f1, f5);
      f2 = Math.max(f2, f4);
      f3 = Math.max(f3, f5);
    } 
    float f6 = (f + f2) / 2.0F;
    float f7 = (f1 + f3) / 2.0F;
    TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV(f6, f7);
    return textureatlassprite;
  }
  
  protected void fixVertexData() {
    if (Config.isShaders()) {
      if (this.vertexData.length == 28)
        this.vertexData = expandVertexData(this.vertexData); 
    } else if (this.vertexData.length == 56) {
      this.vertexData = compactVertexData(this.vertexData);
    } 
  }
  
  private static int[] expandVertexData(int[] p_expandVertexData_0_) {
    int i = p_expandVertexData_0_.length / 4;
    int j = i * 2;
    int[] aint = new int[j * 4];
    for (int k = 0; k < 4; k++)
      System.arraycopy(p_expandVertexData_0_, k * i, aint, k * j, i); 
    return aint;
  }
  
  private static int[] compactVertexData(int[] p_compactVertexData_0_) {
    int i = p_compactVertexData_0_.length / 4;
    int j = i / 2;
    int[] aint = new int[j * 4];
    for (int k = 0; k < 4; k++)
      System.arraycopy(p_compactVertexData_0_, k * i, aint, k * j, j); 
    return aint;
  }
  
  public QuadBounds getQuadBounds() {
    if (this.quadBounds == null)
      this.quadBounds = new QuadBounds(getVertexData()); 
    return this.quadBounds;
  }
  
  public float getMidX() {
    QuadBounds quadbounds = getQuadBounds();
    return (quadbounds.getMaxX() + quadbounds.getMinX()) / 2.0F;
  }
  
  public double getMidY() {
    QuadBounds quadbounds = getQuadBounds();
    return ((quadbounds.getMaxY() + quadbounds.getMinY()) / 2.0F);
  }
  
  public double getMidZ() {
    QuadBounds quadbounds = getQuadBounds();
    return ((quadbounds.getMaxZ() + quadbounds.getMinZ()) / 2.0F);
  }
  
  public boolean isFaceQuad() {
    QuadBounds quadbounds = getQuadBounds();
    return quadbounds.isFaceQuad(this.face);
  }
  
  public boolean isFullQuad() {
    QuadBounds quadbounds = getQuadBounds();
    return quadbounds.isFullQuad(this.face);
  }
  
  public boolean isFullFaceQuad() {
    return (isFullQuad() && isFaceQuad());
  }
  
  public String toString() {
    return "vertex: " + (this.vertexData.length / 7) + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\model\BakedQuad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */