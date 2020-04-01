package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

public class MapItemRenderer {
  private static final ResourceLocation TEXTURE_MAP_ICONS = new ResourceLocation("textures/map/map_icons.png");
  
  private final TextureManager textureManager;
  
  private final Map<String, Instance> loadedMaps = Maps.newHashMap();
  
  public MapItemRenderer(TextureManager textureManagerIn) {
    this.textureManager = textureManagerIn;
  }
  
  public void updateMapTexture(MapData mapdataIn) {
    getMapRendererInstance(mapdataIn).updateMapTexture();
  }
  
  public void renderMap(MapData mapdataIn, boolean p_148250_2_) {
    getMapRendererInstance(mapdataIn).render(p_148250_2_);
  }
  
  private Instance getMapRendererInstance(MapData mapdataIn) {
    Instance mapitemrenderer$instance = this.loadedMaps.get(mapdataIn.mapName);
    if (mapitemrenderer$instance == null) {
      mapitemrenderer$instance = new Instance(mapdataIn, null);
      this.loadedMaps.put(mapdataIn.mapName, mapitemrenderer$instance);
    } 
    return mapitemrenderer$instance;
  }
  
  @Nullable
  public Instance func_191205_a(String p_191205_1_) {
    return this.loadedMaps.get(p_191205_1_);
  }
  
  public void clearLoadedMaps() {
    for (Instance mapitemrenderer$instance : this.loadedMaps.values())
      this.textureManager.deleteTexture(mapitemrenderer$instance.location); 
    this.loadedMaps.clear();
  }
  
  @Nullable
  public MapData func_191207_a(@Nullable Instance p_191207_1_) {
    return (p_191207_1_ != null) ? p_191207_1_.mapData : null;
  }
  
  class Instance {
    private final MapData mapData;
    
    private final DynamicTexture mapTexture;
    
    private final ResourceLocation location;
    
    private final int[] mapTextureData;
    
    private Instance(MapData mapdataIn) {
      this.mapData = mapdataIn;
      this.mapTexture = new DynamicTexture(128, 128);
      this.mapTextureData = this.mapTexture.getTextureData();
      this.location = MapItemRenderer.this.textureManager.getDynamicTextureLocation("map/" + mapdataIn.mapName, this.mapTexture);
      for (int i = 0; i < this.mapTextureData.length; i++)
        this.mapTextureData[i] = 0; 
    }
    
    private void updateMapTexture() {
      for (int i = 0; i < 16384; i++) {
        int j = this.mapData.colors[i] & 0xFF;
        if (j / 4 == 0) {
          this.mapTextureData[i] = (i + i / 128 & 0x1) * 8 + 16 << 24;
        } else {
          this.mapTextureData[i] = MapColor.COLORS[j / 4].getMapColor(j & 0x3);
        } 
      } 
      this.mapTexture.updateDynamicTexture();
    }
    
    private void render(boolean noOverlayRendering) {
      int i = 0;
      int j = 0;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      float f = 0.0F;
      MapItemRenderer.this.textureManager.bindTexture(this.location);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
      GlStateManager.disableAlpha();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos(0.0D, 128.0D, -0.009999999776482582D).tex(0.0D, 1.0D).endVertex();
      bufferbuilder.pos(128.0D, 128.0D, -0.009999999776482582D).tex(1.0D, 1.0D).endVertex();
      bufferbuilder.pos(128.0D, 0.0D, -0.009999999776482582D).tex(1.0D, 0.0D).endVertex();
      bufferbuilder.pos(0.0D, 0.0D, -0.009999999776482582D).tex(0.0D, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      MapItemRenderer.this.textureManager.bindTexture(MapItemRenderer.TEXTURE_MAP_ICONS);
      int k = 0;
      for (MapDecoration mapdecoration : this.mapData.mapDecorations.values()) {
        if (!noOverlayRendering || mapdecoration.func_191180_f()) {
          GlStateManager.pushMatrix();
          GlStateManager.translate(0.0F + mapdecoration.getX() / 2.0F + 64.0F, 0.0F + mapdecoration.getY() / 2.0F + 64.0F, -0.02F);
          GlStateManager.rotate((mapdecoration.getRotation() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.scale(4.0F, 4.0F, 3.0F);
          GlStateManager.translate(-0.125F, 0.125F, 0.0F);
          byte b0 = mapdecoration.getType();
          float f1 = (b0 % 4 + 0) / 4.0F;
          float f2 = (b0 / 4 + 0) / 4.0F;
          float f3 = (b0 % 4 + 1) / 4.0F;
          float f4 = (b0 / 4 + 1) / 4.0F;
          bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
          float f5 = -0.001F;
          bufferbuilder.pos(-1.0D, 1.0D, (k * -0.001F)).tex(f1, f2).endVertex();
          bufferbuilder.pos(1.0D, 1.0D, (k * -0.001F)).tex(f3, f2).endVertex();
          bufferbuilder.pos(1.0D, -1.0D, (k * -0.001F)).tex(f3, f4).endVertex();
          bufferbuilder.pos(-1.0D, -1.0D, (k * -0.001F)).tex(f1, f4).endVertex();
          tessellator.draw();
          GlStateManager.popMatrix();
          k++;
        } 
      } 
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, 0.0F, -0.04F);
      GlStateManager.scale(1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\MapItemRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */