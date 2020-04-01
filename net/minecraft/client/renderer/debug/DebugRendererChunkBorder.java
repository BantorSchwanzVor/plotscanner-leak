package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRendererChunkBorder implements DebugRenderer.IDebugRenderer {
  private final Minecraft minecraft;
  
  public DebugRendererChunkBorder(Minecraft minecraftIn) {
    this.minecraft = minecraftIn;
  }
  
  public void render(float p_190060_1_, long p_190060_2_) {
    EntityPlayerSP entityPlayerSP = this.minecraft.player;
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    double d0 = ((EntityPlayer)entityPlayerSP).lastTickPosX + (((EntityPlayer)entityPlayerSP).posX - ((EntityPlayer)entityPlayerSP).lastTickPosX) * p_190060_1_;
    double d1 = ((EntityPlayer)entityPlayerSP).lastTickPosY + (((EntityPlayer)entityPlayerSP).posY - ((EntityPlayer)entityPlayerSP).lastTickPosY) * p_190060_1_;
    double d2 = ((EntityPlayer)entityPlayerSP).lastTickPosZ + (((EntityPlayer)entityPlayerSP).posZ - ((EntityPlayer)entityPlayerSP).lastTickPosZ) * p_190060_1_;
    double d3 = 0.0D - d1;
    double d4 = 256.0D - d1;
    GlStateManager.disableTexture2D();
    GlStateManager.disableBlend();
    double d5 = (((EntityPlayer)entityPlayerSP).chunkCoordX << 4) - d0;
    double d6 = (((EntityPlayer)entityPlayerSP).chunkCoordZ << 4) - d2;
    GlStateManager.glLineWidth(1.0F);
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    for (int i = -16; i <= 32; i += 16) {
      for (int j = -16; j <= 32; j += 16) {
        bufferbuilder.pos(d5 + i, d3, d6 + j).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
        bufferbuilder.pos(d5 + i, d3, d6 + j).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
        bufferbuilder.pos(d5 + i, d4, d6 + j).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
        bufferbuilder.pos(d5 + i, d4, d6 + j).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
      } 
    } 
    for (int k = 2; k < 16; k += 2) {
      bufferbuilder.pos(d5 + k, d3, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5 + k, d3, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + k, d4, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + k, d4, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5 + k, d3, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5 + k, d3, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + k, d4, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + k, d4, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
    } 
    for (int l = 2; l < 16; l += 2) {
      bufferbuilder.pos(d5, d3, d6 + l).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5, d3, d6 + l).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d4, d6 + l).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d4, d6 + l).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d3, d6 + l).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d3, d6 + l).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d4, d6 + l).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d4, d6 + l).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
    } 
    for (int i1 = 0; i1 <= 256; i1 += 2) {
      double d7 = i1 - d1;
      bufferbuilder.pos(d5, d7, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5, d7, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d7, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d7, d6 + 16.0D).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d7, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d7, d6).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d7, d6).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
    } 
    tessellator.draw();
    GlStateManager.glLineWidth(2.0F);
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    for (int j1 = 0; j1 <= 16; j1 += 16) {
      for (int l1 = 0; l1 <= 16; l1 += 16) {
        bufferbuilder.pos(d5 + j1, d3, d6 + l1).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(d5 + j1, d3, d6 + l1).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(d5 + j1, d4, d6 + l1).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
        bufferbuilder.pos(d5 + j1, d4, d6 + l1).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
      } 
    } 
    for (int k1 = 0; k1 <= 256; k1 += 16) {
      double d8 = k1 - d1;
      bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
      bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d8, d6 + 16.0D).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d8, d6 + 16.0D).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5 + 16.0D, d8, d6).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
      bufferbuilder.pos(d5, d8, d6).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
    } 
    tessellator.draw();
    GlStateManager.glLineWidth(1.0F);
    GlStateManager.enableBlend();
    GlStateManager.enableTexture2D();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\debug\DebugRendererChunkBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */