package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer {
  private final Minecraft minecraft;
  
  private final Map<Integer, Path> pathMap = Maps.newHashMap();
  
  private final Map<Integer, Float> pathMaxDistance = Maps.newHashMap();
  
  private final Map<Integer, Long> creationMap = Maps.newHashMap();
  
  private EntityPlayer player;
  
  private double xo;
  
  private double yo;
  
  private double zo;
  
  public DebugRendererPathfinding(Minecraft minecraftIn) {
    this.minecraft = minecraftIn;
  }
  
  public void addPath(int p_188289_1_, Path p_188289_2_, float p_188289_3_) {
    this.pathMap.put(Integer.valueOf(p_188289_1_), p_188289_2_);
    this.creationMap.put(Integer.valueOf(p_188289_1_), Long.valueOf(System.currentTimeMillis()));
    this.pathMaxDistance.put(Integer.valueOf(p_188289_1_), Float.valueOf(p_188289_3_));
  }
  
  public void render(float p_190060_1_, long p_190060_2_) {
    if (!this.pathMap.isEmpty()) {
      long i = System.currentTimeMillis();
      this.player = (EntityPlayer)this.minecraft.player;
      this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * p_190060_1_;
      this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * p_190060_1_;
      this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * p_190060_1_;
      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.color(0.0F, 1.0F, 0.0F, 0.75F);
      GlStateManager.disableTexture2D();
      GlStateManager.glLineWidth(6.0F);
      for (Integer integer : this.pathMap.keySet()) {
        Path path = this.pathMap.get(integer);
        float f = ((Float)this.pathMaxDistance.get(integer)).floatValue();
        renderPathLine(p_190060_1_, path);
        PathPoint pathpoint = path.getTarget();
        if (addDistanceToPlayer(pathpoint) <= 40.0F) {
          RenderGlobal.renderFilledBox((new AxisAlignedBB((pathpoint.xCoord + 0.25F), (pathpoint.yCoord + 0.25F), pathpoint.zCoord + 0.25D, (pathpoint.xCoord + 0.75F), (pathpoint.yCoord + 0.75F), (pathpoint.zCoord + 0.75F))).offset(-this.xo, -this.yo, -this.zo), 0.0F, 1.0F, 0.0F, 0.5F);
          for (int k = 0; k < path.getCurrentPathLength(); k++) {
            PathPoint pathpoint1 = path.getPathPointFromIndex(k);
            if (addDistanceToPlayer(pathpoint1) <= 40.0F) {
              float f1 = (k == path.getCurrentPathIndex()) ? 1.0F : 0.0F;
              float f2 = (k == path.getCurrentPathIndex()) ? 0.0F : 1.0F;
              RenderGlobal.renderFilledBox((new AxisAlignedBB((pathpoint1.xCoord + 0.5F - f), (pathpoint1.yCoord + 0.01F * k), (pathpoint1.zCoord + 0.5F - f), (pathpoint1.xCoord + 0.5F + f), (pathpoint1.yCoord + 0.25F + 0.01F * k), (pathpoint1.zCoord + 0.5F + f))).offset(-this.xo, -this.yo, -this.zo), f1, 0.0F, f2, 0.5F);
            } 
          } 
        } 
      } 
      for (Integer integer1 : this.pathMap.keySet()) {
        Path path1 = this.pathMap.get(integer1);
        byte b1;
        int m;
        PathPoint[] arrayOfPathPoint;
        for (m = (arrayOfPathPoint = path1.getClosedSet()).length, b1 = 0; b1 < m; ) {
          PathPoint pathpoint3 = arrayOfPathPoint[b1];
          if (addDistanceToPlayer(pathpoint3) <= 40.0F) {
            DebugRenderer.renderDebugText(String.format("%s", new Object[] { pathpoint3.nodeType }), pathpoint3.xCoord + 0.5D, pathpoint3.yCoord + 0.75D, pathpoint3.zCoord + 0.5D, p_190060_1_, -65536);
            DebugRenderer.renderDebugText(String.format("%.2f", new Object[] { Float.valueOf(pathpoint3.costMalus) }), pathpoint3.xCoord + 0.5D, pathpoint3.yCoord + 0.25D, pathpoint3.zCoord + 0.5D, p_190060_1_, -65536);
          } 
          b1++;
        } 
        for (m = (arrayOfPathPoint = path1.getOpenSet()).length, b1 = 0; b1 < m; ) {
          PathPoint pathpoint4 = arrayOfPathPoint[b1];
          if (addDistanceToPlayer(pathpoint4) <= 40.0F) {
            DebugRenderer.renderDebugText(String.format("%s", new Object[] { pathpoint4.nodeType }), pathpoint4.xCoord + 0.5D, pathpoint4.yCoord + 0.75D, pathpoint4.zCoord + 0.5D, p_190060_1_, -16776961);
            DebugRenderer.renderDebugText(String.format("%.2f", new Object[] { Float.valueOf(pathpoint4.costMalus) }), pathpoint4.xCoord + 0.5D, pathpoint4.yCoord + 0.25D, pathpoint4.zCoord + 0.5D, p_190060_1_, -16776961);
          } 
          b1++;
        } 
        for (int k = 0; k < path1.getCurrentPathLength(); k++) {
          PathPoint pathpoint2 = path1.getPathPointFromIndex(k);
          if (addDistanceToPlayer(pathpoint2) <= 40.0F) {
            DebugRenderer.renderDebugText(String.format("%s", new Object[] { pathpoint2.nodeType }), pathpoint2.xCoord + 0.5D, pathpoint2.yCoord + 0.75D, pathpoint2.zCoord + 0.5D, p_190060_1_, -1);
            DebugRenderer.renderDebugText(String.format("%.2f", new Object[] { Float.valueOf(pathpoint2.costMalus) }), pathpoint2.xCoord + 0.5D, pathpoint2.yCoord + 0.25D, pathpoint2.zCoord + 0.5D, p_190060_1_, -1);
          } 
        } 
      } 
      byte b;
      int j;
      Integer[] arrayOfInteger;
      for (j = (arrayOfInteger = (Integer[])this.creationMap.keySet().toArray((Object[])new Integer[0])).length, b = 0; b < j; ) {
        Integer integer2 = arrayOfInteger[b];
        if (i - ((Long)this.creationMap.get(integer2)).longValue() > 20000L) {
          this.pathMap.remove(integer2);
          this.creationMap.remove(integer2);
        } 
        b++;
      } 
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
    } 
  }
  
  public void renderPathLine(float p_190067_1_, Path p_190067_2_) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    for (int i = 0; i < p_190067_2_.getCurrentPathLength(); i++) {
      PathPoint pathpoint = p_190067_2_.getPathPointFromIndex(i);
      if (addDistanceToPlayer(pathpoint) <= 40.0F) {
        float f = i / p_190067_2_.getCurrentPathLength() * 0.33F;
        int j = (i == 0) ? 0 : MathHelper.hsvToRGB(f, 0.9F, 0.9F);
        int k = j >> 16 & 0xFF;
        int l = j >> 8 & 0xFF;
        int i1 = j & 0xFF;
        bufferbuilder.pos(pathpoint.xCoord - this.xo + 0.5D, pathpoint.yCoord - this.yo + 0.5D, pathpoint.zCoord - this.zo + 0.5D).color(k, l, i1, 255).endVertex();
      } 
    } 
    tessellator.draw();
  }
  
  private float addDistanceToPlayer(PathPoint p_190066_1_) {
    return (float)(Math.abs(p_190066_1_.xCoord - this.player.posX) + Math.abs(p_190066_1_.yCoord - this.player.posY) + Math.abs(p_190066_1_.zCoord - this.player.posZ));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\debug\DebugRendererPathfinding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */