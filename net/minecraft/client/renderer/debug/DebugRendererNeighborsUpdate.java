package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererNeighborsUpdate implements DebugRenderer.IDebugRenderer {
  private final Minecraft field_191554_a;
  
  private final Map<Long, Map<BlockPos, Integer>> field_191555_b = Maps.newTreeMap((Comparator)Ordering.natural().reverse());
  
  DebugRendererNeighborsUpdate(Minecraft p_i47365_1_) {
    this.field_191554_a = p_i47365_1_;
  }
  
  public void func_191553_a(long p_191553_1_, BlockPos p_191553_3_) {
    Map<BlockPos, Integer> map = this.field_191555_b.get(Long.valueOf(p_191553_1_));
    if (map == null) {
      map = Maps.newHashMap();
      this.field_191555_b.put(Long.valueOf(p_191553_1_), map);
    } 
    Integer integer = map.get(p_191553_3_);
    if (integer == null)
      integer = Integer.valueOf(0); 
    map.put(p_191553_3_, Integer.valueOf(integer.intValue() + 1));
  }
  
  public void render(float p_190060_1_, long p_190060_2_) {
    long i = this.field_191554_a.world.getTotalWorldTime();
    EntityPlayerSP entityPlayerSP = this.field_191554_a.player;
    double d0 = ((EntityPlayer)entityPlayerSP).lastTickPosX + (((EntityPlayer)entityPlayerSP).posX - ((EntityPlayer)entityPlayerSP).lastTickPosX) * p_190060_1_;
    double d1 = ((EntityPlayer)entityPlayerSP).lastTickPosY + (((EntityPlayer)entityPlayerSP).posY - ((EntityPlayer)entityPlayerSP).lastTickPosY) * p_190060_1_;
    double d2 = ((EntityPlayer)entityPlayerSP).lastTickPosZ + (((EntityPlayer)entityPlayerSP).posZ - ((EntityPlayer)entityPlayerSP).lastTickPosZ) * p_190060_1_;
    World world = this.field_191554_a.player.world;
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.glLineWidth(2.0F);
    GlStateManager.disableTexture2D();
    GlStateManager.depthMask(false);
    int j = 200;
    double d3 = 0.0025D;
    Set<BlockPos> set = Sets.newHashSet();
    Map<BlockPos, Integer> map = Maps.newHashMap();
    Iterator<Map.Entry<Long, Map<BlockPos, Integer>>> iterator = this.field_191555_b.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Long, Map<BlockPos, Integer>> entry = iterator.next();
      Long olong = entry.getKey();
      Map<BlockPos, Integer> map1 = entry.getValue();
      long k = i - olong.longValue();
      if (k > 200L) {
        iterator.remove();
        continue;
      } 
      for (Map.Entry<BlockPos, Integer> entry1 : map1.entrySet()) {
        BlockPos blockpos = entry1.getKey();
        Integer integer = entry1.getValue();
        if (set.add(blockpos)) {
          RenderGlobal.drawSelectionBoundingBox((new AxisAlignedBB(BlockPos.ORIGIN)).expandXyz(0.002D).contract(0.0025D * k).offset(blockpos.getX(), blockpos.getY(), blockpos.getZ()).offset(-d0, -d1, -d2), 1.0F, 1.0F, 1.0F, 1.0F);
          map.put(blockpos, integer);
        } 
      } 
    } 
    for (Map.Entry<BlockPos, Integer> entry2 : map.entrySet()) {
      BlockPos blockpos1 = entry2.getKey();
      Integer integer1 = entry2.getValue();
      DebugRenderer.func_191556_a(String.valueOf(integer1), blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), p_190060_1_, -1);
    } 
    GlStateManager.depthMask(true);
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\debug\DebugRendererNeighborsUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */