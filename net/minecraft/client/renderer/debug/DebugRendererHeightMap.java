package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class DebugRendererHeightMap implements DebugRenderer.IDebugRenderer {
  private final Minecraft minecraft;
  
  public DebugRendererHeightMap(Minecraft minecraftIn) {
    this.minecraft = minecraftIn;
  }
  
  public void render(float p_190060_1_, long p_190060_2_) {
    EntityPlayerSP entityPlayerSP = this.minecraft.player;
    WorldClient worldClient = this.minecraft.world;
    double d0 = ((EntityPlayer)entityPlayerSP).lastTickPosX + (((EntityPlayer)entityPlayerSP).posX - ((EntityPlayer)entityPlayerSP).lastTickPosX) * p_190060_1_;
    double d1 = ((EntityPlayer)entityPlayerSP).lastTickPosY + (((EntityPlayer)entityPlayerSP).posY - ((EntityPlayer)entityPlayerSP).lastTickPosY) * p_190060_1_;
    double d2 = ((EntityPlayer)entityPlayerSP).lastTickPosZ + (((EntityPlayer)entityPlayerSP).posZ - ((EntityPlayer)entityPlayerSP).lastTickPosZ) * p_190060_1_;
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.disableTexture2D();
    BlockPos blockpos = new BlockPos(((EntityPlayer)entityPlayerSP).posX, 0.0D, ((EntityPlayer)entityPlayerSP).posZ);
    Iterable<BlockPos> iterable = BlockPos.getAllInBox(blockpos.add(-40, 0, -40), blockpos.add(40, 0, 40));
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
    for (BlockPos blockpos1 : iterable) {
      int i = worldClient.getHeight(blockpos1.getX(), blockpos1.getZ());
      if (worldClient.getBlockState(blockpos1.add(0, i, 0).down()) == Blocks.AIR.getDefaultState()) {
        RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, (blockpos1.getX() + 0.25F) - d0, i - d1, (blockpos1.getZ() + 0.25F) - d2, (blockpos1.getX() + 0.75F) - d0, i + 0.09375D - d1, (blockpos1.getZ() + 0.75F) - d2, 0.0F, 0.0F, 1.0F, 0.5F);
        continue;
      } 
      RenderGlobal.addChainedFilledBoxVertices(bufferbuilder, (blockpos1.getX() + 0.25F) - d0, i - d1, (blockpos1.getZ() + 0.25F) - d2, (blockpos1.getX() + 0.75F) - d0, i + 0.09375D - d1, (blockpos1.getZ() + 0.75F) - d2, 0.0F, 1.0F, 0.0F, 0.5F);
    } 
    tessellator.draw();
    GlStateManager.enableTexture2D();
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\debug\DebugRendererHeightMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */