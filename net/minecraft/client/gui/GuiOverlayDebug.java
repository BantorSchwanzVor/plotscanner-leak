package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import optifine.Reflector;
import org.lwjgl.opengl.Display;

public class GuiOverlayDebug extends Gui {
  private final Minecraft mc;
  
  private final FontRenderer fontRenderer;
  
  public GuiOverlayDebug(Minecraft mc) {
    this.mc = mc;
    this.fontRenderer = mc.fontRendererObj;
  }
  
  public void renderDebugInfo(ScaledResolution scaledResolutionIn) {
    this.mc.mcProfiler.startSection("debug");
    GlStateManager.pushMatrix();
    renderDebugInfoLeft();
    renderDebugInfoRight(scaledResolutionIn);
    GlStateManager.popMatrix();
    if (this.mc.gameSettings.showLagometer)
      renderLagometer(); 
    this.mc.mcProfiler.endSection();
  }
  
  protected void renderDebugInfoLeft() {
    List<String> list = call();
    list.add("");
    list.add("Debug: Pie [shift]: " + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden") + " FPS [alt]: " + (this.mc.gameSettings.showLagometer ? "visible" : "hidden"));
    list.add("For help: press F3 + Q");
    for (int i = 0; i < list.size(); i++) {
      String s = list.get(i);
      if (!Strings.isNullOrEmpty(s)) {
        int j = this.fontRenderer.FONT_HEIGHT;
        int k = this.fontRenderer.getStringWidth(s);
        int l = 2;
        int i1 = 2 + j * i;
        drawRect(1, i1 - 1, 2 + k + 1, i1 + j - 1, -1873784752);
        this.fontRenderer.drawString(s, 2, i1, 14737632);
      } 
    } 
  }
  
  protected void renderDebugInfoRight(ScaledResolution scaledRes) {
    List<String> list = getDebugInfoRight();
    for (int i = 0; i < list.size(); i++) {
      String s = list.get(i);
      if (!Strings.isNullOrEmpty(s)) {
        int j = this.fontRenderer.FONT_HEIGHT;
        int k = this.fontRenderer.getStringWidth(s);
        int l = scaledRes.getScaledWidth() - 2 - k;
        int i1 = 2 + j * i;
        drawRect(l - 1, i1 - 1, l + k + 1, i1 + j - 1, -1873784752);
        this.fontRenderer.drawString(s, l, i1, 14737632);
      } 
    } 
  }
  
  protected List<String> call() {
    BlockPos blockpos = new BlockPos((this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity().getEntityBoundingBox()).minY, (this.mc.getRenderViewEntity()).posZ);
    if (this.mc.isReducedDebug())
      return Lists.newArrayList((Object[])new String[] { "Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format("Chunk-relative: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 0xF), Integer.valueOf(blockpos.getY() & 0xF), Integer.valueOf(blockpos.getZ() & 0xF) }) }); 
    Entity entity = this.mc.getRenderViewEntity();
    EnumFacing enumfacing = entity.getHorizontalFacing();
    String s = "Invalid";
    switch (enumfacing) {
      case NORTH:
        s = "Towards negative Z";
        break;
      case SOUTH:
        s = "Towards positive Z";
        break;
      case WEST:
        s = "Towards negative X";
        break;
      case EAST:
        s = "Towards positive X";
        break;
    } 
    List<String> list = Lists.newArrayList((Object[])new String[] { 
          "Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : ("/" + this.mc.getVersionType())) + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", new Object[] { Double.valueOf((this.mc.getRenderViewEntity()).posX), Double.valueOf((this.mc.getRenderViewEntity().getEntityBoundingBox()).minY), Double.valueOf((this.mc.getRenderViewEntity()).posZ) }), String.format("Block: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) }), String.format("Chunk: %d %d %d in %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 0xF), Integer.valueOf(blockpos.getY() & 0xF), Integer.valueOf(blockpos.getZ() & 0xF), Integer.valueOf(blockpos.getX() >> 4), Integer.valueOf(blockpos.getY() >> 4), Integer.valueOf(blockpos.getZ() >> 4) }), 
          String.format("Facing: %s (%s) (%.1f / %.1f)", new Object[] { enumfacing, s, Float.valueOf(MathHelper.wrapDegrees(entity.rotationYaw)), Float.valueOf(MathHelper.wrapDegrees(entity.rotationPitch)) }) });
    if (this.mc.world != null) {
      Chunk chunk = this.mc.world.getChunkFromBlockCoords(blockpos);
      if (this.mc.world.isBlockLoaded(blockpos) && blockpos.getY() >= 0 && blockpos.getY() < 256) {
        if (!chunk.isEmpty()) {
          list.add("Biome: " + chunk.getBiome(blockpos, this.mc.world.getBiomeProvider()).getBiomeName());
          list.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)");
          DifficultyInstance difficultyinstance = this.mc.world.getDifficultyForLocation(blockpos);
          if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
            EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.getUniqueID());
            if (entityplayermp != null)
              difficultyinstance = entityplayermp.world.getDifficultyForLocation(new BlockPos((Entity)entityplayermp)); 
          } 
          list.add(String.format("Local Difficulty: %.2f // %.2f (Day %d)", new Object[] { Float.valueOf(difficultyinstance.getAdditionalDifficulty()), Float.valueOf(difficultyinstance.getClampedAdditionalDifficulty()), Long.valueOf(this.mc.world.getWorldTime() / 24000L) }));
        } else {
          list.add("Waiting for chunk...");
        } 
      } else {
        list.add("Outside of world...");
      } 
    } 
    if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive())
      list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName()); 
    if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
      BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
      list.add(String.format("Looking at: %d %d %d", new Object[] { Integer.valueOf(blockpos1.getX()), Integer.valueOf(blockpos1.getY()), Integer.valueOf(blockpos1.getZ()) }));
    } 
    return list;
  }
  
  protected <T extends Comparable<T>> List<String> getDebugInfoRight() {
    long i = Runtime.getRuntime().maxMemory();
    long j = Runtime.getRuntime().totalMemory();
    long k = Runtime.getRuntime().freeMemory();
    long l = j - k;
    List<String> list = Lists.newArrayList((Object[])new String[] { String.format("Java: %s %dbit", new Object[] { System.getProperty("java.version"), Integer.valueOf(this.mc.isJava64bit() ? 64 : 32) }), String.format("Mem: % 2d%% %03d/%03dMB", new Object[] { Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)), Long.valueOf(bytesToMb(i)) }), String.format("Allocated: % 2d%% %03dMB", new Object[] { Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j)) }), "", String.format("CPU: %s", new Object[] { OpenGlHelper.getCpu() }), "", String.format("Display: %dx%d (%s)", new Object[] { Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()), GlStateManager.glGetString(7936) }), GlStateManager.glGetString(7937), GlStateManager.glGetString(7938) });
    if (Reflector.FMLCommonHandler_getBrandings.exists()) {
      Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
      list.add("");
      list.addAll((Collection<? extends String>)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, new Object[] { Boolean.valueOf(false) }));
    } 
    if (this.mc.isReducedDebug())
      return list; 
    if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
      BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
      IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
      if (this.mc.world.getWorldType() != WorldType.DEBUG_WORLD)
        iblockstate = iblockstate.getActualState((IBlockAccess)this.mc.world, blockpos); 
      list.add("");
      list.add(String.valueOf(Block.REGISTRY.getNameForObject(iblockstate.getBlock())));
      for (UnmodifiableIterator unmodifiableiterator = iblockstate.getProperties().entrySet().iterator(); unmodifiableiterator.hasNext(); list.add(String.valueOf(iproperty.getName()) + ": " + s)) {
        Map.Entry<IProperty<?>, Comparable<?>> entry = (Map.Entry<IProperty<?>, Comparable<?>>)unmodifiableiterator.next();
        IProperty<T> iproperty = (IProperty<T>)entry.getKey();
        Comparable comparable = entry.getValue();
        String s = iproperty.getName(comparable);
        if (Boolean.TRUE.equals(comparable)) {
          s = TextFormatting.GREEN + s;
        } else if (Boolean.FALSE.equals(comparable)) {
          s = TextFormatting.RED + s;
        } 
      } 
    } 
    return list;
  }
  
  private void renderLagometer() {}
  
  private int getFrameColor(int p_181552_1_, int p_181552_2_, int p_181552_3_, int p_181552_4_) {
    return (p_181552_1_ < p_181552_3_) ? blendColors(-16711936, -256, p_181552_1_ / p_181552_3_) : blendColors(-256, -65536, (p_181552_1_ - p_181552_3_) / (p_181552_4_ - p_181552_3_));
  }
  
  private int blendColors(int p_181553_1_, int p_181553_2_, float p_181553_3_) {
    int i = p_181553_1_ >> 24 & 0xFF;
    int j = p_181553_1_ >> 16 & 0xFF;
    int k = p_181553_1_ >> 8 & 0xFF;
    int l = p_181553_1_ & 0xFF;
    int i1 = p_181553_2_ >> 24 & 0xFF;
    int j1 = p_181553_2_ >> 16 & 0xFF;
    int k1 = p_181553_2_ >> 8 & 0xFF;
    int l1 = p_181553_2_ & 0xFF;
    int i2 = MathHelper.clamp((int)(i + (i1 - i) * p_181553_3_), 0, 255);
    int j2 = MathHelper.clamp((int)(j + (j1 - j) * p_181553_3_), 0, 255);
    int k2 = MathHelper.clamp((int)(k + (k1 - k) * p_181553_3_), 0, 255);
    int l2 = MathHelper.clamp((int)(l + (l1 - l) * p_181553_3_), 0, 255);
    return i2 << 24 | j2 << 16 | k2 << 8 | l2;
  }
  
  private static long bytesToMb(long bytes) {
    return bytes / 1024L / 1024L;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiOverlayDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */