package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import optifine.ChunkUtils;
import optifine.CloudRenderer;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomSky;
import optifine.DynamicLights;
import optifine.Lagometer;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.RenderEnv;
import optifine.RenderInfoLazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;
import shadersmod.client.ShadowUtils;

public class RenderGlobal implements IWorldEventListener, IResourceManagerReloadListener {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
  
  private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
  
  private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
  
  private static final ResourceLocation END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");
  
  private static final ResourceLocation FORCEFIELD_TEXTURES = new ResourceLocation("textures/misc/forcefield.png");
  
  public final Minecraft mc;
  
  private final TextureManager renderEngine;
  
  private final RenderManager renderManager;
  
  private WorldClient theWorld;
  
  private Set<RenderChunk> chunksToUpdate = Sets.newLinkedHashSet();
  
  private List<ContainerLocalRenderInformation> renderInfos = Lists.newArrayListWithCapacity(69696);
  
  private final Set<TileEntity> setTileEntities = Sets.newHashSet();
  
  private ViewFrustum viewFrustum;
  
  private int starGLCallList = -1;
  
  private int glSkyList = -1;
  
  private int glSkyList2 = -1;
  
  private final VertexFormat vertexBufferFormat;
  
  private VertexBuffer starVBO;
  
  private VertexBuffer skyVBO;
  
  private VertexBuffer sky2VBO;
  
  private int cloudTickCounter;
  
  public final Map<Integer, DestroyBlockProgress> damagedBlocks = Maps.newHashMap();
  
  private final Map<BlockPos, ISound> mapSoundPositions = Maps.newHashMap();
  
  private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
  
  private Framebuffer entityOutlineFramebuffer;
  
  private ShaderGroup entityOutlineShader;
  
  private double frustumUpdatePosX = Double.MIN_VALUE;
  
  private double frustumUpdatePosY = Double.MIN_VALUE;
  
  private double frustumUpdatePosZ = Double.MIN_VALUE;
  
  private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
  
  private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
  
  private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
  
  private double lastViewEntityX = Double.MIN_VALUE;
  
  private double lastViewEntityY = Double.MIN_VALUE;
  
  private double lastViewEntityZ = Double.MIN_VALUE;
  
  private double lastViewEntityPitch = Double.MIN_VALUE;
  
  private double lastViewEntityYaw = Double.MIN_VALUE;
  
  private ChunkRenderDispatcher renderDispatcher;
  
  private ChunkRenderContainer renderContainer;
  
  private int renderDistanceChunks = -1;
  
  private int renderEntitiesStartupCounter = 2;
  
  private int countEntitiesTotal;
  
  private int countEntitiesRendered;
  
  private int countEntitiesHidden;
  
  private boolean debugFixTerrainFrustum;
  
  private ClippingHelper debugFixedClippingHelper;
  
  private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
  
  private final Vector3d debugTerrainFrustumPosition = new Vector3d();
  
  private boolean vboEnabled;
  
  IRenderChunkFactory renderChunkFactory;
  
  private double prevRenderSortX;
  
  private double prevRenderSortY;
  
  private double prevRenderSortZ;
  
  public boolean displayListEntitiesDirty = true;
  
  private boolean entityOutlinesRendered;
  
  private final Set<BlockPos> setLightUpdates = Sets.newHashSet();
  
  private CloudRenderer cloudRenderer;
  
  public Entity renderedEntity;
  
  public Set chunksToResortTransparency = new LinkedHashSet();
  
  public Set chunksToUpdateForced = new LinkedHashSet();
  
  private Deque visibilityDeque = new ArrayDeque();
  
  private List renderInfosEntities = new ArrayList(1024);
  
  private List renderInfosTileEntities = new ArrayList(1024);
  
  private List renderInfosNormal = new ArrayList(1024);
  
  private List renderInfosEntitiesNormal = new ArrayList(1024);
  
  private List renderInfosTileEntitiesNormal = new ArrayList(1024);
  
  private List renderInfosShadow = new ArrayList(1024);
  
  private List renderInfosEntitiesShadow = new ArrayList(1024);
  
  private List renderInfosTileEntitiesShadow = new ArrayList(1024);
  
  private int renderDistance = 0;
  
  private int renderDistanceSq = 0;
  
  private static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList((Object[])EnumFacing.VALUES)));
  
  private int countTileEntitiesRendered;
  
  private IChunkProvider worldChunkProvider = null;
  
  private Long2ObjectMap<Chunk> worldChunkProviderMap = null;
  
  private int countLoadedChunksPrev = 0;
  
  private RenderEnv renderEnv;
  
  public boolean renderOverlayDamaged;
  
  public boolean renderOverlayEyes;
  
  static Deque<ContainerLocalRenderInformation> renderInfoCache = new ArrayDeque<>();
  
  public RenderGlobal(Minecraft mcIn) {
    this.renderEnv = new RenderEnv((IBlockAccess)this.theWorld, Blocks.AIR.getDefaultState(), new BlockPos(0, 0, 0));
    this.renderOverlayDamaged = false;
    this.renderOverlayEyes = false;
    this.cloudRenderer = new CloudRenderer(mcIn);
    this.mc = mcIn;
    this.renderManager = mcIn.getRenderManager();
    this.renderEngine = mcIn.getTextureManager();
    this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
    GlStateManager.glTexParameteri(3553, 10242, 10497);
    GlStateManager.glTexParameteri(3553, 10243, 10497);
    GlStateManager.bindTexture(0);
    updateDestroyBlockIcons();
    this.vboEnabled = OpenGlHelper.useVbo();
    if (this.vboEnabled) {
      this.renderContainer = new VboRenderList();
      this.renderChunkFactory = (IRenderChunkFactory)new VboChunkFactory();
    } else {
      this.renderContainer = new RenderList();
      this.renderChunkFactory = (IRenderChunkFactory)new ListChunkFactory();
    } 
    this.vertexBufferFormat = new VertexFormat();
    this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
    generateStars();
    generateSky();
    generateSky2();
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    updateDestroyBlockIcons();
  }
  
  private void updateDestroyBlockIcons() {
    TextureMap texturemap = this.mc.getTextureMapBlocks();
    for (int i = 0; i < this.destroyBlockIcons.length; i++)
      this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i); 
  }
  
  public void makeEntityOutlineShader() {
    if (OpenGlHelper.shadersSupported) {
      if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
        ShaderLinkHelper.setNewStaticShaderLinkHelper(); 
      ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");
      try {
        this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
        this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
        this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
      } catch (IOException ioexception) {
        LOGGER.warn("Failed to load shader: {}", resourcelocation, ioexception);
        this.entityOutlineShader = null;
        this.entityOutlineFramebuffer = null;
      } catch (JsonSyntaxException jsonsyntaxexception) {
        LOGGER.warn("Failed to load shader: {}", resourcelocation, jsonsyntaxexception);
        this.entityOutlineShader = null;
        this.entityOutlineFramebuffer = null;
      } 
    } else {
      this.entityOutlineShader = null;
      this.entityOutlineFramebuffer = null;
    } 
  }
  
  public void renderEntityOutlineFramebuffer() {
    if (isRenderEntityOutlines()) {
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
      this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
      GlStateManager.disableBlend();
    } 
  }
  
  protected boolean isRenderEntityOutlines() {
    if (!Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing())
      return (this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.player != null); 
    return false;
  }
  
  private void generateSky2() {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    if (this.sky2VBO != null)
      this.sky2VBO.deleteGlBuffers(); 
    if (this.glSkyList2 >= 0) {
      GLAllocation.deleteDisplayLists(this.glSkyList2);
      this.glSkyList2 = -1;
    } 
    if (this.vboEnabled) {
      this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
      renderSky(bufferbuilder, -16.0F, true);
      bufferbuilder.finishDrawing();
      bufferbuilder.reset();
      this.sky2VBO.bufferData(bufferbuilder.getByteBuffer());
    } else {
      this.glSkyList2 = GLAllocation.generateDisplayLists(1);
      GlStateManager.glNewList(this.glSkyList2, 4864);
      renderSky(bufferbuilder, -16.0F, true);
      tessellator.draw();
      GlStateManager.glEndList();
    } 
  }
  
  private void generateSky() {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    if (this.skyVBO != null)
      this.skyVBO.deleteGlBuffers(); 
    if (this.glSkyList >= 0) {
      GLAllocation.deleteDisplayLists(this.glSkyList);
      this.glSkyList = -1;
    } 
    if (this.vboEnabled) {
      this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
      renderSky(bufferbuilder, 16.0F, false);
      bufferbuilder.finishDrawing();
      bufferbuilder.reset();
      this.skyVBO.bufferData(bufferbuilder.getByteBuffer());
    } else {
      this.glSkyList = GLAllocation.generateDisplayLists(1);
      GlStateManager.glNewList(this.glSkyList, 4864);
      renderSky(bufferbuilder, 16.0F, false);
      tessellator.draw();
      GlStateManager.glEndList();
    } 
  }
  
  private void renderSky(BufferBuilder worldRendererIn, float posY, boolean reverseX) {
    int i = 64;
    int j = 6;
    worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
    for (int k = -384; k <= 384; k += 64) {
      for (int l = -384; l <= 384; l += 64) {
        float f = k;
        float f1 = (k + 64);
        if (reverseX) {
          f1 = k;
          f = (k + 64);
        } 
        worldRendererIn.pos(f, posY, l).endVertex();
        worldRendererIn.pos(f1, posY, l).endVertex();
        worldRendererIn.pos(f1, posY, (l + 64)).endVertex();
        worldRendererIn.pos(f, posY, (l + 64)).endVertex();
      } 
    } 
  }
  
  private void generateStars() {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    if (this.starVBO != null)
      this.starVBO.deleteGlBuffers(); 
    if (this.starGLCallList >= 0) {
      GLAllocation.deleteDisplayLists(this.starGLCallList);
      this.starGLCallList = -1;
    } 
    if (this.vboEnabled) {
      this.starVBO = new VertexBuffer(this.vertexBufferFormat);
      renderStars(bufferbuilder);
      bufferbuilder.finishDrawing();
      bufferbuilder.reset();
      this.starVBO.bufferData(bufferbuilder.getByteBuffer());
    } else {
      this.starGLCallList = GLAllocation.generateDisplayLists(1);
      GlStateManager.pushMatrix();
      GlStateManager.glNewList(this.starGLCallList, 4864);
      renderStars(bufferbuilder);
      tessellator.draw();
      GlStateManager.glEndList();
      GlStateManager.popMatrix();
    } 
  }
  
  private void renderStars(BufferBuilder worldRendererIn) {
    Random random = new Random(10842L);
    worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
    for (int i = 0; i < 1500; i++) {
      double d0 = (random.nextFloat() * 2.0F - 1.0F);
      double d1 = (random.nextFloat() * 2.0F - 1.0F);
      double d2 = (random.nextFloat() * 2.0F - 1.0F);
      double d3 = (0.15F + random.nextFloat() * 0.1F);
      double d4 = d0 * d0 + d1 * d1 + d2 * d2;
      if (d4 < 1.0D && d4 > 0.01D) {
        d4 = 1.0D / Math.sqrt(d4);
        d0 *= d4;
        d1 *= d4;
        d2 *= d4;
        double d5 = d0 * 100.0D;
        double d6 = d1 * 100.0D;
        double d7 = d2 * 100.0D;
        double d8 = Math.atan2(d0, d2);
        double d9 = Math.sin(d8);
        double d10 = Math.cos(d8);
        double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
        double d12 = Math.sin(d11);
        double d13 = Math.cos(d11);
        double d14 = random.nextDouble() * Math.PI * 2.0D;
        double d15 = Math.sin(d14);
        double d16 = Math.cos(d14);
        for (int j = 0; j < 4; j++) {
          double d17 = 0.0D;
          double d18 = ((j & 0x2) - 1) * d3;
          double d19 = ((j + 1 & 0x2) - 1) * d3;
          double d20 = 0.0D;
          double d21 = d18 * d16 - d19 * d15;
          double d22 = d19 * d16 + d18 * d15;
          double d23 = d21 * d12 + 0.0D * d13;
          double d24 = 0.0D * d12 - d21 * d13;
          double d25 = d24 * d9 - d22 * d10;
          double d26 = d22 * d9 + d24 * d10;
          worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
        } 
      } 
    } 
  }
  
  public void setWorldAndLoadRenderers(@Nullable WorldClient worldClientIn) {
    if (this.theWorld != null)
      this.theWorld.removeEventListener(this); 
    this.frustumUpdatePosX = Double.MIN_VALUE;
    this.frustumUpdatePosY = Double.MIN_VALUE;
    this.frustumUpdatePosZ = Double.MIN_VALUE;
    this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
    this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
    this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    this.renderManager.set((World)worldClientIn);
    this.theWorld = worldClientIn;
    if (Config.isDynamicLights())
      DynamicLights.clear(); 
    if (worldClientIn != null) {
      worldClientIn.addEventListener(this);
      loadRenderers();
    } else {
      this.chunksToUpdate.clear();
      this.renderInfos.clear();
      if (this.viewFrustum != null) {
        this.viewFrustum.deleteGlResources();
        this.viewFrustum = null;
      } 
      if (this.renderDispatcher != null)
        this.renderDispatcher.stopWorkerThreads(); 
      this.renderDispatcher = null;
    } 
  }
  
  public void loadRenderers() {
    if (this.theWorld != null) {
      if (this.renderDispatcher == null)
        this.renderDispatcher = new ChunkRenderDispatcher(); 
      this.displayListEntitiesDirty = true;
      Blocks.LEAVES.setGraphicsLevel(Config.isTreesFancy());
      Blocks.LEAVES2.setGraphicsLevel(Config.isTreesFancy());
      BlockModelRenderer.updateAoLightValue();
      renderInfoCache.clear();
      if (Config.isDynamicLights())
        DynamicLights.clear(); 
      this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
      this.renderDistance = this.renderDistanceChunks * 16;
      this.renderDistanceSq = this.renderDistance * this.renderDistance;
      boolean flag = this.vboEnabled;
      this.vboEnabled = OpenGlHelper.useVbo();
      if (flag && !this.vboEnabled) {
        this.renderContainer = new RenderList();
        this.renderChunkFactory = (IRenderChunkFactory)new ListChunkFactory();
      } else if (!flag && this.vboEnabled) {
        this.renderContainer = new VboRenderList();
        this.renderChunkFactory = (IRenderChunkFactory)new VboChunkFactory();
      } 
      if (flag != this.vboEnabled) {
        generateStars();
        generateSky();
        generateSky2();
      } 
      if (this.viewFrustum != null)
        this.viewFrustum.deleteGlResources(); 
      stopChunkUpdates();
      synchronized (this.setTileEntities) {
        this.setTileEntities.clear();
      } 
      this.viewFrustum = new ViewFrustum((World)this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
      if (this.theWorld != null) {
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null)
          this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ); 
      } 
      this.renderEntitiesStartupCounter = 2;
    } 
  }
  
  protected void stopChunkUpdates() {
    this.chunksToUpdate.clear();
    this.renderDispatcher.stopChunkUpdates();
  }
  
  public void createBindEntityOutlineFbs(int width, int height) {
    if (OpenGlHelper.shadersSupported && this.entityOutlineShader != null)
      this.entityOutlineShader.createBindFramebuffers(width, height); 
  }
  
  public void renderEntities(Entity renderViewEntity, ICamera camera, float partialTicks) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: getstatic optifine/Reflector.MinecraftForgeClient_getRenderPass : Loptifine/ReflectorMethod;
    //   6: invokevirtual exists : ()Z
    //   9: ifeq -> 24
    //   12: getstatic optifine/Reflector.MinecraftForgeClient_getRenderPass : Loptifine/ReflectorMethod;
    //   15: iconst_0
    //   16: anewarray java/lang/Object
    //   19: invokestatic callInt : (Loptifine/ReflectorMethod;[Ljava/lang/Object;)I
    //   22: istore #4
    //   24: aload_0
    //   25: getfield renderEntitiesStartupCounter : I
    //   28: ifle -> 50
    //   31: iload #4
    //   33: ifle -> 37
    //   36: return
    //   37: aload_0
    //   38: dup
    //   39: getfield renderEntitiesStartupCounter : I
    //   42: iconst_1
    //   43: isub
    //   44: putfield renderEntitiesStartupCounter : I
    //   47: goto -> 2208
    //   50: aload_1
    //   51: getfield prevPosX : D
    //   54: aload_1
    //   55: getfield posX : D
    //   58: aload_1
    //   59: getfield prevPosX : D
    //   62: dsub
    //   63: fload_3
    //   64: f2d
    //   65: dmul
    //   66: dadd
    //   67: dstore #5
    //   69: aload_1
    //   70: getfield prevPosY : D
    //   73: aload_1
    //   74: getfield posY : D
    //   77: aload_1
    //   78: getfield prevPosY : D
    //   81: dsub
    //   82: fload_3
    //   83: f2d
    //   84: dmul
    //   85: dadd
    //   86: dstore #7
    //   88: aload_1
    //   89: getfield prevPosZ : D
    //   92: aload_1
    //   93: getfield posZ : D
    //   96: aload_1
    //   97: getfield prevPosZ : D
    //   100: dsub
    //   101: fload_3
    //   102: f2d
    //   103: dmul
    //   104: dadd
    //   105: dstore #9
    //   107: aload_0
    //   108: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   111: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   114: ldc_w 'prepare'
    //   117: invokevirtual startSection : (Ljava/lang/String;)V
    //   120: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   123: aload_0
    //   124: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   127: aload_0
    //   128: getfield mc : Lnet/minecraft/client/Minecraft;
    //   131: invokevirtual getTextureManager : ()Lnet/minecraft/client/renderer/texture/TextureManager;
    //   134: aload_0
    //   135: getfield mc : Lnet/minecraft/client/Minecraft;
    //   138: getfield fontRendererObj : Lnet/minecraft/client/gui/FontRenderer;
    //   141: aload_0
    //   142: getfield mc : Lnet/minecraft/client/Minecraft;
    //   145: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   148: aload_0
    //   149: getfield mc : Lnet/minecraft/client/Minecraft;
    //   152: getfield objectMouseOver : Lnet/minecraft/util/math/RayTraceResult;
    //   155: fload_3
    //   156: invokevirtual prepare : (Lnet/minecraft/world/World;Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/RayTraceResult;F)V
    //   159: aload_0
    //   160: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   163: aload_0
    //   164: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   167: aload_0
    //   168: getfield mc : Lnet/minecraft/client/Minecraft;
    //   171: getfield fontRendererObj : Lnet/minecraft/client/gui/FontRenderer;
    //   174: aload_0
    //   175: getfield mc : Lnet/minecraft/client/Minecraft;
    //   178: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   181: aload_0
    //   182: getfield mc : Lnet/minecraft/client/Minecraft;
    //   185: getfield pointedEntity : Lnet/minecraft/entity/Entity;
    //   188: aload_0
    //   189: getfield mc : Lnet/minecraft/client/Minecraft;
    //   192: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   195: fload_3
    //   196: invokevirtual cacheActiveRenderInfo : (Lnet/minecraft/world/World;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/settings/GameSettings;F)V
    //   199: iload #4
    //   201: ifne -> 224
    //   204: aload_0
    //   205: iconst_0
    //   206: putfield countEntitiesTotal : I
    //   209: aload_0
    //   210: iconst_0
    //   211: putfield countEntitiesRendered : I
    //   214: aload_0
    //   215: iconst_0
    //   216: putfield countEntitiesHidden : I
    //   219: aload_0
    //   220: iconst_0
    //   221: putfield countTileEntitiesRendered : I
    //   224: aload_0
    //   225: getfield mc : Lnet/minecraft/client/Minecraft;
    //   228: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   231: astore #11
    //   233: aload #11
    //   235: getfield lastTickPosX : D
    //   238: aload #11
    //   240: getfield posX : D
    //   243: aload #11
    //   245: getfield lastTickPosX : D
    //   248: dsub
    //   249: fload_3
    //   250: f2d
    //   251: dmul
    //   252: dadd
    //   253: dstore #12
    //   255: aload #11
    //   257: getfield lastTickPosY : D
    //   260: aload #11
    //   262: getfield posY : D
    //   265: aload #11
    //   267: getfield lastTickPosY : D
    //   270: dsub
    //   271: fload_3
    //   272: f2d
    //   273: dmul
    //   274: dadd
    //   275: dstore #14
    //   277: aload #11
    //   279: getfield lastTickPosZ : D
    //   282: aload #11
    //   284: getfield posZ : D
    //   287: aload #11
    //   289: getfield lastTickPosZ : D
    //   292: dsub
    //   293: fload_3
    //   294: f2d
    //   295: dmul
    //   296: dadd
    //   297: dstore #16
    //   299: dload #12
    //   301: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerX : D
    //   304: dload #14
    //   306: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerY : D
    //   309: dload #16
    //   311: putstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.staticPlayerZ : D
    //   314: aload_0
    //   315: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   318: dload #12
    //   320: dload #14
    //   322: dload #16
    //   324: invokevirtual setRenderPosition : (DDD)V
    //   327: aload_0
    //   328: getfield mc : Lnet/minecraft/client/Minecraft;
    //   331: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   334: invokevirtual enableLightmap : ()V
    //   337: aload_0
    //   338: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   341: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   344: ldc_w 'global'
    //   347: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   350: aload_0
    //   351: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   354: invokevirtual getLoadedEntityList : ()Ljava/util/List;
    //   357: astore #18
    //   359: iload #4
    //   361: ifne -> 375
    //   364: aload_0
    //   365: aload #18
    //   367: invokeinterface size : ()I
    //   372: putfield countEntitiesTotal : I
    //   375: invokestatic isFogOff : ()Z
    //   378: ifeq -> 397
    //   381: aload_0
    //   382: getfield mc : Lnet/minecraft/client/Minecraft;
    //   385: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   388: getfield fogStandard : Z
    //   391: ifeq -> 397
    //   394: invokestatic disableFog : ()V
    //   397: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   400: invokevirtual exists : ()Z
    //   403: istore #19
    //   405: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   408: invokevirtual exists : ()Z
    //   411: istore #20
    //   413: iconst_0
    //   414: istore #21
    //   416: goto -> 504
    //   419: aload_0
    //   420: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   423: getfield weatherEffects : Ljava/util/List;
    //   426: iload #21
    //   428: invokeinterface get : (I)Ljava/lang/Object;
    //   433: checkcast net/minecraft/entity/Entity
    //   436: astore #22
    //   438: iload #19
    //   440: ifeq -> 466
    //   443: aload #22
    //   445: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   448: iconst_1
    //   449: anewarray java/lang/Object
    //   452: dup
    //   453: iconst_0
    //   454: iload #4
    //   456: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   459: aastore
    //   460: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   463: ifeq -> 501
    //   466: aload_0
    //   467: dup
    //   468: getfield countEntitiesRendered : I
    //   471: iconst_1
    //   472: iadd
    //   473: putfield countEntitiesRendered : I
    //   476: aload #22
    //   478: dload #5
    //   480: dload #7
    //   482: dload #9
    //   484: invokevirtual isInRangeToRender3d : (DDD)Z
    //   487: ifeq -> 501
    //   490: aload_0
    //   491: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   494: aload #22
    //   496: fload_3
    //   497: iconst_0
    //   498: invokevirtual renderEntityStatic : (Lnet/minecraft/entity/Entity;FZ)V
    //   501: iinc #21, 1
    //   504: iload #21
    //   506: aload_0
    //   507: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   510: getfield weatherEffects : Ljava/util/List;
    //   513: invokeinterface size : ()I
    //   518: if_icmplt -> 419
    //   521: aload_0
    //   522: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   525: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   528: ldc_w 'entities'
    //   531: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   534: invokestatic isShaders : ()Z
    //   537: istore #21
    //   539: iload #21
    //   541: ifeq -> 547
    //   544: invokestatic beginEntities : ()V
    //   547: aload_0
    //   548: getfield mc : Lnet/minecraft/client/Minecraft;
    //   551: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   554: getfield fancyGraphics : Z
    //   557: istore #22
    //   559: aload_0
    //   560: getfield mc : Lnet/minecraft/client/Minecraft;
    //   563: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   566: invokestatic isDroppedItemsFancy : ()Z
    //   569: putfield fancyGraphics : Z
    //   572: invokestatic newArrayList : ()Ljava/util/ArrayList;
    //   575: astore #23
    //   577: invokestatic newArrayList : ()Ljava/util/ArrayList;
    //   580: astore #24
    //   582: invokestatic retain : ()Lnet/minecraft/util/math/BlockPos$PooledMutableBlockPos;
    //   585: astore #25
    //   587: aload_0
    //   588: getfield renderInfosEntities : Ljava/util/List;
    //   591: invokeinterface iterator : ()Ljava/util/Iterator;
    //   596: astore #27
    //   598: goto -> 954
    //   601: aload #27
    //   603: invokeinterface next : ()Ljava/lang/Object;
    //   608: astore #26
    //   610: aload #26
    //   612: checkcast net/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation
    //   615: astore #28
    //   617: aload #28
    //   619: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   622: aload_0
    //   623: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   626: invokevirtual getChunk : (Lnet/minecraft/world/World;)Lnet/minecraft/world/chunk/Chunk;
    //   629: astore #29
    //   631: aload #29
    //   633: invokevirtual getEntityLists : ()[Lnet/minecraft/util/ClassInheritanceMultiMap;
    //   636: aload #28
    //   638: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   641: invokevirtual getPosition : ()Lnet/minecraft/util/math/BlockPos;
    //   644: invokevirtual getY : ()I
    //   647: bipush #16
    //   649: idiv
    //   650: aaload
    //   651: astore #30
    //   653: aload #30
    //   655: invokevirtual isEmpty : ()Z
    //   658: ifne -> 954
    //   661: aload #30
    //   663: invokevirtual iterator : ()Ljava/util/Iterator;
    //   666: astore #32
    //   668: goto -> 944
    //   671: aload #32
    //   673: invokeinterface next : ()Ljava/lang/Object;
    //   678: checkcast net/minecraft/entity/Entity
    //   681: astore #31
    //   683: iload #19
    //   685: ifeq -> 711
    //   688: aload #31
    //   690: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   693: iconst_1
    //   694: anewarray java/lang/Object
    //   697: dup
    //   698: iconst_0
    //   699: iload #4
    //   701: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   704: aastore
    //   705: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   708: ifeq -> 944
    //   711: aload_0
    //   712: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   715: aload #31
    //   717: aload_2
    //   718: dload #5
    //   720: dload #7
    //   722: dload #9
    //   724: invokevirtual shouldRender : (Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z
    //   727: ifne -> 749
    //   730: aload #31
    //   732: aload_0
    //   733: getfield mc : Lnet/minecraft/client/Minecraft;
    //   736: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   739: invokevirtual isRidingOrBeingRiddenBy : (Lnet/minecraft/entity/Entity;)Z
    //   742: ifne -> 749
    //   745: iconst_0
    //   746: goto -> 750
    //   749: iconst_1
    //   750: istore #33
    //   752: iload #33
    //   754: ifeq -> 944
    //   757: aload_0
    //   758: getfield mc : Lnet/minecraft/client/Minecraft;
    //   761: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   764: instanceof net/minecraft/entity/EntityLivingBase
    //   767: ifeq -> 786
    //   770: aload_0
    //   771: getfield mc : Lnet/minecraft/client/Minecraft;
    //   774: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   777: checkcast net/minecraft/entity/EntityLivingBase
    //   780: invokevirtual isPlayerSleeping : ()Z
    //   783: goto -> 787
    //   786: iconst_0
    //   787: istore #34
    //   789: aload #31
    //   791: aload_0
    //   792: getfield mc : Lnet/minecraft/client/Minecraft;
    //   795: invokevirtual getRenderViewEntity : ()Lnet/minecraft/entity/Entity;
    //   798: if_acmpne -> 819
    //   801: aload_0
    //   802: getfield mc : Lnet/minecraft/client/Minecraft;
    //   805: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   808: getfield thirdPersonView : I
    //   811: ifne -> 819
    //   814: iload #34
    //   816: ifeq -> 944
    //   819: aload #31
    //   821: getfield posY : D
    //   824: dconst_0
    //   825: dcmpg
    //   826: iflt -> 858
    //   829: aload #31
    //   831: getfield posY : D
    //   834: ldc2_w 256.0
    //   837: dcmpl
    //   838: ifge -> 858
    //   841: aload_0
    //   842: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   845: aload #25
    //   847: aload #31
    //   849: invokevirtual setPos : (Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/math/BlockPos$PooledMutableBlockPos;
    //   852: invokevirtual isBlockLoaded : (Lnet/minecraft/util/math/BlockPos;)Z
    //   855: ifeq -> 944
    //   858: aload_0
    //   859: dup
    //   860: getfield countEntitiesRendered : I
    //   863: iconst_1
    //   864: iadd
    //   865: putfield countEntitiesRendered : I
    //   868: aload_0
    //   869: aload #31
    //   871: putfield renderedEntity : Lnet/minecraft/entity/Entity;
    //   874: iload #21
    //   876: ifeq -> 884
    //   879: aload #31
    //   881: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
    //   884: aload_0
    //   885: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   888: aload #31
    //   890: fload_3
    //   891: iconst_0
    //   892: invokevirtual renderEntityStatic : (Lnet/minecraft/entity/Entity;FZ)V
    //   895: aload_0
    //   896: aconst_null
    //   897: putfield renderedEntity : Lnet/minecraft/entity/Entity;
    //   900: aload_0
    //   901: aload #31
    //   903: aload #11
    //   905: aload_2
    //   906: invokespecial isOutlineActive : (Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;)Z
    //   909: ifeq -> 922
    //   912: aload #23
    //   914: aload #31
    //   916: invokeinterface add : (Ljava/lang/Object;)Z
    //   921: pop
    //   922: aload_0
    //   923: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   926: aload #31
    //   928: invokevirtual isRenderMultipass : (Lnet/minecraft/entity/Entity;)Z
    //   931: ifeq -> 944
    //   934: aload #24
    //   936: aload #31
    //   938: invokeinterface add : (Ljava/lang/Object;)Z
    //   943: pop
    //   944: aload #32
    //   946: invokeinterface hasNext : ()Z
    //   951: ifne -> 671
    //   954: aload #27
    //   956: invokeinterface hasNext : ()Z
    //   961: ifne -> 601
    //   964: aload #25
    //   966: invokevirtual release : ()V
    //   969: aload #24
    //   971: invokeinterface isEmpty : ()Z
    //   976: ifne -> 1061
    //   979: aload #24
    //   981: invokeinterface iterator : ()Ljava/util/Iterator;
    //   986: astore #27
    //   988: goto -> 1051
    //   991: aload #27
    //   993: invokeinterface next : ()Ljava/lang/Object;
    //   998: checkcast net/minecraft/entity/Entity
    //   1001: astore #26
    //   1003: iload #19
    //   1005: ifeq -> 1031
    //   1008: aload #26
    //   1010: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1013: iconst_1
    //   1014: anewarray java/lang/Object
    //   1017: dup
    //   1018: iconst_0
    //   1019: iload #4
    //   1021: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1024: aastore
    //   1025: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1028: ifeq -> 1051
    //   1031: iload #21
    //   1033: ifeq -> 1041
    //   1036: aload #26
    //   1038: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
    //   1041: aload_0
    //   1042: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1045: aload #26
    //   1047: fload_3
    //   1048: invokevirtual renderMultipass : (Lnet/minecraft/entity/Entity;F)V
    //   1051: aload #27
    //   1053: invokeinterface hasNext : ()Z
    //   1058: ifne -> 991
    //   1061: iload #4
    //   1063: ifne -> 1313
    //   1066: aload_0
    //   1067: invokevirtual isRenderEntityOutlines : ()Z
    //   1070: ifeq -> 1313
    //   1073: aload #23
    //   1075: invokeinterface isEmpty : ()Z
    //   1080: ifeq -> 1090
    //   1083: aload_0
    //   1084: getfield entityOutlinesRendered : Z
    //   1087: ifeq -> 1313
    //   1090: aload_0
    //   1091: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1094: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   1097: ldc_w 'entityOutlines'
    //   1100: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   1103: aload_0
    //   1104: getfield entityOutlineFramebuffer : Lnet/minecraft/client/shader/Framebuffer;
    //   1107: invokevirtual framebufferClear : ()V
    //   1110: aload_0
    //   1111: aload #23
    //   1113: invokeinterface isEmpty : ()Z
    //   1118: ifeq -> 1125
    //   1121: iconst_0
    //   1122: goto -> 1126
    //   1125: iconst_1
    //   1126: putfield entityOutlinesRendered : Z
    //   1129: aload #23
    //   1131: invokeinterface isEmpty : ()Z
    //   1136: ifne -> 1302
    //   1139: sipush #519
    //   1142: invokestatic depthFunc : (I)V
    //   1145: invokestatic disableFog : ()V
    //   1148: aload_0
    //   1149: getfield entityOutlineFramebuffer : Lnet/minecraft/client/shader/Framebuffer;
    //   1152: iconst_0
    //   1153: invokevirtual bindFramebuffer : (Z)V
    //   1156: invokestatic disableStandardItemLighting : ()V
    //   1159: aload_0
    //   1160: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1163: iconst_1
    //   1164: invokevirtual setRenderOutlines : (Z)V
    //   1167: iconst_0
    //   1168: istore #26
    //   1170: goto -> 1239
    //   1173: aload #23
    //   1175: iload #26
    //   1177: invokeinterface get : (I)Ljava/lang/Object;
    //   1182: checkcast net/minecraft/entity/Entity
    //   1185: astore #27
    //   1187: iload #19
    //   1189: ifeq -> 1215
    //   1192: aload #27
    //   1194: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1197: iconst_1
    //   1198: anewarray java/lang/Object
    //   1201: dup
    //   1202: iconst_0
    //   1203: iload #4
    //   1205: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1208: aastore
    //   1209: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1212: ifeq -> 1236
    //   1215: iload #21
    //   1217: ifeq -> 1225
    //   1220: aload #27
    //   1222: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
    //   1225: aload_0
    //   1226: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1229: aload #27
    //   1231: fload_3
    //   1232: iconst_0
    //   1233: invokevirtual renderEntityStatic : (Lnet/minecraft/entity/Entity;FZ)V
    //   1236: iinc #26, 1
    //   1239: iload #26
    //   1241: aload #23
    //   1243: invokeinterface size : ()I
    //   1248: if_icmplt -> 1173
    //   1251: aload_0
    //   1252: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1255: iconst_0
    //   1256: invokevirtual setRenderOutlines : (Z)V
    //   1259: invokestatic enableStandardItemLighting : ()V
    //   1262: iconst_0
    //   1263: invokestatic depthMask : (Z)V
    //   1266: aload_0
    //   1267: getfield entityOutlineShader : Lnet/minecraft/client/shader/ShaderGroup;
    //   1270: fload_3
    //   1271: invokevirtual loadShaderGroup : (F)V
    //   1274: invokestatic enableLighting : ()V
    //   1277: iconst_1
    //   1278: invokestatic depthMask : (Z)V
    //   1281: invokestatic enableFog : ()V
    //   1284: invokestatic enableBlend : ()V
    //   1287: invokestatic enableColorMaterial : ()V
    //   1290: sipush #515
    //   1293: invokestatic depthFunc : (I)V
    //   1296: invokestatic enableDepth : ()V
    //   1299: invokestatic enableAlpha : ()V
    //   1302: aload_0
    //   1303: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1306: invokevirtual getFramebuffer : ()Lnet/minecraft/client/shader/Framebuffer;
    //   1309: iconst_0
    //   1310: invokevirtual bindFramebuffer : (Z)V
    //   1313: aload_0
    //   1314: invokevirtual isRenderEntityOutlines : ()Z
    //   1317: ifne -> 1517
    //   1320: aload #23
    //   1322: invokeinterface isEmpty : ()Z
    //   1327: ifeq -> 1337
    //   1330: aload_0
    //   1331: getfield entityOutlinesRendered : Z
    //   1334: ifeq -> 1517
    //   1337: aload_0
    //   1338: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1341: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   1344: ldc_w 'entityOutlines'
    //   1347: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   1350: aload_0
    //   1351: aload #23
    //   1353: invokeinterface isEmpty : ()Z
    //   1358: ifeq -> 1365
    //   1361: iconst_0
    //   1362: goto -> 1366
    //   1365: iconst_1
    //   1366: putfield entityOutlinesRendered : Z
    //   1369: aload #23
    //   1371: invokeinterface isEmpty : ()Z
    //   1376: ifne -> 1517
    //   1379: invokestatic disableFog : ()V
    //   1382: invokestatic disableDepth : ()V
    //   1385: aload_0
    //   1386: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1389: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   1392: invokevirtual disableLightmap : ()V
    //   1395: invokestatic disableStandardItemLighting : ()V
    //   1398: aload_0
    //   1399: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1402: iconst_1
    //   1403: invokevirtual setRenderOutlines : (Z)V
    //   1406: iconst_0
    //   1407: istore #26
    //   1409: goto -> 1478
    //   1412: aload #23
    //   1414: iload #26
    //   1416: invokeinterface get : (I)Ljava/lang/Object;
    //   1421: checkcast net/minecraft/entity/Entity
    //   1424: astore #27
    //   1426: iload #19
    //   1428: ifeq -> 1454
    //   1431: aload #27
    //   1433: getstatic optifine/Reflector.ForgeEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1436: iconst_1
    //   1437: anewarray java/lang/Object
    //   1440: dup
    //   1441: iconst_0
    //   1442: iload #4
    //   1444: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1447: aastore
    //   1448: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1451: ifeq -> 1475
    //   1454: iload #21
    //   1456: ifeq -> 1464
    //   1459: aload #27
    //   1461: invokestatic nextEntity : (Lnet/minecraft/entity/Entity;)V
    //   1464: aload_0
    //   1465: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1468: aload #27
    //   1470: fload_3
    //   1471: iconst_0
    //   1472: invokevirtual renderEntityStatic : (Lnet/minecraft/entity/Entity;FZ)V
    //   1475: iinc #26, 1
    //   1478: iload #26
    //   1480: aload #23
    //   1482: invokeinterface size : ()I
    //   1487: if_icmplt -> 1412
    //   1490: aload_0
    //   1491: getfield renderManager : Lnet/minecraft/client/renderer/entity/RenderManager;
    //   1494: iconst_0
    //   1495: invokevirtual setRenderOutlines : (Z)V
    //   1498: invokestatic enableStandardItemLighting : ()V
    //   1501: aload_0
    //   1502: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1505: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   1508: invokevirtual enableLightmap : ()V
    //   1511: invokestatic enableDepth : ()V
    //   1514: invokestatic enableFog : ()V
    //   1517: aload_0
    //   1518: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1521: getfield gameSettings : Lnet/minecraft/client/settings/GameSettings;
    //   1524: iload #22
    //   1526: putfield fancyGraphics : Z
    //   1529: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1532: invokevirtual getFontRenderer : ()Lnet/minecraft/client/gui/FontRenderer;
    //   1535: astore #26
    //   1537: iload #21
    //   1539: ifeq -> 1548
    //   1542: invokestatic endEntities : ()V
    //   1545: invokestatic beginBlockEntities : ()V
    //   1548: aload_0
    //   1549: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   1552: getfield theProfiler : Lnet/minecraft/profiler/Profiler;
    //   1555: ldc_w 'blockentities'
    //   1558: invokevirtual endStartSection : (Ljava/lang/String;)V
    //   1561: invokestatic enableStandardItemLighting : ()V
    //   1564: getstatic optifine/Reflector.ForgeTileEntity_hasFastRenderer : Loptifine/ReflectorMethod;
    //   1567: invokevirtual exists : ()Z
    //   1570: ifeq -> 1579
    //   1573: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1576: invokevirtual preDrawBatch : ()V
    //   1579: aload_0
    //   1580: getfield renderInfosTileEntities : Ljava/util/List;
    //   1583: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1588: astore #28
    //   1590: goto -> 1836
    //   1593: aload #28
    //   1595: invokeinterface next : ()Ljava/lang/Object;
    //   1600: astore #27
    //   1602: aload #27
    //   1604: checkcast net/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation
    //   1607: astore #29
    //   1609: aload #29
    //   1611: getfield renderChunk : Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   1614: invokevirtual getCompiledChunk : ()Lnet/minecraft/client/renderer/chunk/CompiledChunk;
    //   1617: invokevirtual getTileEntities : ()Ljava/util/List;
    //   1620: astore #30
    //   1622: aload #30
    //   1624: invokeinterface isEmpty : ()Z
    //   1629: ifne -> 1836
    //   1632: aload #30
    //   1634: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1639: astore #31
    //   1641: aload #31
    //   1643: invokeinterface hasNext : ()Z
    //   1648: ifne -> 1654
    //   1651: goto -> 1836
    //   1654: aload #31
    //   1656: invokeinterface next : ()Ljava/lang/Object;
    //   1661: checkcast net/minecraft/tileentity/TileEntity
    //   1664: astore #32
    //   1666: iload #20
    //   1668: ifne -> 1674
    //   1671: goto -> 1730
    //   1674: aload #32
    //   1676: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1679: iconst_1
    //   1680: anewarray java/lang/Object
    //   1683: dup
    //   1684: iconst_0
    //   1685: iload #4
    //   1687: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1690: aastore
    //   1691: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1694: ifeq -> 1641
    //   1697: aload #32
    //   1699: getstatic optifine/Reflector.ForgeTileEntity_getRenderBoundingBox : Loptifine/ReflectorMethod;
    //   1702: iconst_0
    //   1703: anewarray java/lang/Object
    //   1706: invokestatic call : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Ljava/lang/Object;
    //   1709: checkcast net/minecraft/util/math/AxisAlignedBB
    //   1712: astore #33
    //   1714: aload #33
    //   1716: ifnull -> 1730
    //   1719: aload_2
    //   1720: aload #33
    //   1722: invokeinterface isBoundingBoxInFrustum : (Lnet/minecraft/util/math/AxisAlignedBB;)Z
    //   1727: ifeq -> 1641
    //   1730: aload #32
    //   1732: invokevirtual getClass : ()Ljava/lang/Class;
    //   1735: astore #33
    //   1737: aload #33
    //   1739: ldc_w net/minecraft/tileentity/TileEntitySign
    //   1742: if_acmpne -> 1797
    //   1745: getstatic optifine/Config.zoomMode : Z
    //   1748: ifne -> 1797
    //   1751: aload_0
    //   1752: getfield mc : Lnet/minecraft/client/Minecraft;
    //   1755: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   1758: astore #34
    //   1760: aload #32
    //   1762: aload #34
    //   1764: getfield posX : D
    //   1767: aload #34
    //   1769: getfield posY : D
    //   1772: aload #34
    //   1774: getfield posZ : D
    //   1777: invokevirtual getDistanceSq : (DDD)D
    //   1780: dstore #35
    //   1782: dload #35
    //   1784: ldc2_w 256.0
    //   1787: dcmpl
    //   1788: ifle -> 1797
    //   1791: aload #26
    //   1793: iconst_0
    //   1794: putfield enabled : Z
    //   1797: iload #21
    //   1799: ifeq -> 1807
    //   1802: aload #32
    //   1804: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
    //   1807: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1810: aload #32
    //   1812: fload_3
    //   1813: iconst_m1
    //   1814: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
    //   1817: aload_0
    //   1818: dup
    //   1819: getfield countTileEntitiesRendered : I
    //   1822: iconst_1
    //   1823: iadd
    //   1824: putfield countTileEntitiesRendered : I
    //   1827: aload #26
    //   1829: iconst_1
    //   1830: putfield enabled : Z
    //   1833: goto -> 1641
    //   1836: aload #28
    //   1838: invokeinterface hasNext : ()Z
    //   1843: ifne -> 1593
    //   1846: aload_0
    //   1847: getfield setTileEntities : Ljava/util/Set;
    //   1850: dup
    //   1851: astore #27
    //   1853: monitorenter
    //   1854: aload_0
    //   1855: getfield setTileEntities : Ljava/util/Set;
    //   1858: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1863: astore #29
    //   1865: goto -> 1928
    //   1868: aload #29
    //   1870: invokeinterface next : ()Ljava/lang/Object;
    //   1875: checkcast net/minecraft/tileentity/TileEntity
    //   1878: astore #28
    //   1880: iload #20
    //   1882: ifeq -> 1908
    //   1885: aload #28
    //   1887: getstatic optifine/Reflector.ForgeTileEntity_shouldRenderInPass : Loptifine/ReflectorMethod;
    //   1890: iconst_1
    //   1891: anewarray java/lang/Object
    //   1894: dup
    //   1895: iconst_0
    //   1896: iload #4
    //   1898: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1901: aastore
    //   1902: invokestatic callBoolean : (Ljava/lang/Object;Loptifine/ReflectorMethod;[Ljava/lang/Object;)Z
    //   1905: ifeq -> 1928
    //   1908: iload #21
    //   1910: ifeq -> 1918
    //   1913: aload #28
    //   1915: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
    //   1918: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1921: aload #28
    //   1923: fload_3
    //   1924: iconst_m1
    //   1925: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
    //   1928: aload #29
    //   1930: invokeinterface hasNext : ()Z
    //   1935: ifne -> 1868
    //   1938: aload #27
    //   1940: monitorexit
    //   1941: goto -> 1948
    //   1944: aload #27
    //   1946: monitorexit
    //   1947: athrow
    //   1948: getstatic optifine/Reflector.ForgeTileEntity_hasFastRenderer : Loptifine/ReflectorMethod;
    //   1951: invokevirtual exists : ()Z
    //   1954: ifeq -> 1965
    //   1957: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   1960: iload #4
    //   1962: invokevirtual drawBatch : (I)V
    //   1965: aload_0
    //   1966: iconst_1
    //   1967: putfield renderOverlayDamaged : Z
    //   1970: aload_0
    //   1971: invokespecial preRenderDamagedBlocks : ()V
    //   1974: aload_0
    //   1975: getfield damagedBlocks : Ljava/util/Map;
    //   1978: invokeinterface values : ()Ljava/util/Collection;
    //   1983: invokeinterface iterator : ()Ljava/util/Iterator;
    //   1988: astore #28
    //   1990: goto -> 2169
    //   1993: aload #28
    //   1995: invokeinterface next : ()Ljava/lang/Object;
    //   2000: checkcast net/minecraft/client/renderer/DestroyBlockProgress
    //   2003: astore #27
    //   2005: aload #27
    //   2007: invokevirtual getPosition : ()Lnet/minecraft/util/math/BlockPos;
    //   2010: astore #29
    //   2012: aload_0
    //   2013: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2016: aload #29
    //   2018: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   2021: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   2026: invokevirtual hasTileEntity : ()Z
    //   2029: ifeq -> 2169
    //   2032: aload_0
    //   2033: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2036: aload #29
    //   2038: invokevirtual getTileEntity : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
    //   2041: astore #30
    //   2043: aload #30
    //   2045: instanceof net/minecraft/tileentity/TileEntityChest
    //   2048: ifeq -> 2119
    //   2051: aload #30
    //   2053: checkcast net/minecraft/tileentity/TileEntityChest
    //   2056: astore #31
    //   2058: aload #31
    //   2060: getfield adjacentChestXNeg : Lnet/minecraft/tileentity/TileEntityChest;
    //   2063: ifnull -> 2090
    //   2066: aload #29
    //   2068: getstatic net/minecraft/util/EnumFacing.WEST : Lnet/minecraft/util/EnumFacing;
    //   2071: invokevirtual offset : (Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;
    //   2074: astore #29
    //   2076: aload_0
    //   2077: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2080: aload #29
    //   2082: invokevirtual getTileEntity : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
    //   2085: astore #30
    //   2087: goto -> 2119
    //   2090: aload #31
    //   2092: getfield adjacentChestZNeg : Lnet/minecraft/tileentity/TileEntityChest;
    //   2095: ifnull -> 2119
    //   2098: aload #29
    //   2100: getstatic net/minecraft/util/EnumFacing.NORTH : Lnet/minecraft/util/EnumFacing;
    //   2103: invokevirtual offset : (Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;
    //   2106: astore #29
    //   2108: aload_0
    //   2109: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2112: aload #29
    //   2114: invokevirtual getTileEntity : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;
    //   2117: astore #30
    //   2119: aload_0
    //   2120: getfield theWorld : Lnet/minecraft/client/multiplayer/WorldClient;
    //   2123: aload #29
    //   2125: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   2128: astore #31
    //   2130: aload #30
    //   2132: ifnull -> 2169
    //   2135: aload #31
    //   2137: invokeinterface func_191057_i : ()Z
    //   2142: ifeq -> 2169
    //   2145: iload #21
    //   2147: ifeq -> 2155
    //   2150: aload #30
    //   2152: invokestatic nextBlockEntity : (Lnet/minecraft/tileentity/TileEntity;)V
    //   2155: getstatic net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher.instance : Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;
    //   2158: aload #30
    //   2160: fload_3
    //   2161: aload #27
    //   2163: invokevirtual getPartialBlockDamage : ()I
    //   2166: invokevirtual renderTileEntity : (Lnet/minecraft/tileentity/TileEntity;FI)V
    //   2169: aload #28
    //   2171: invokeinterface hasNext : ()Z
    //   2176: ifne -> 1993
    //   2179: aload_0
    //   2180: invokespecial postRenderDamagedBlocks : ()V
    //   2183: aload_0
    //   2184: iconst_0
    //   2185: putfield renderOverlayDamaged : Z
    //   2188: aload_0
    //   2189: getfield mc : Lnet/minecraft/client/Minecraft;
    //   2192: getfield entityRenderer : Lnet/minecraft/client/renderer/EntityRenderer;
    //   2195: invokevirtual disableLightmap : ()V
    //   2198: aload_0
    //   2199: getfield mc : Lnet/minecraft/client/Minecraft;
    //   2202: getfield mcProfiler : Lnet/minecraft/profiler/Profiler;
    //   2205: invokevirtual endSection : ()V
    //   2208: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #660	-> 0
    //   #662	-> 3
    //   #664	-> 12
    //   #667	-> 24
    //   #669	-> 31
    //   #671	-> 36
    //   #674	-> 37
    //   #675	-> 47
    //   #678	-> 50
    //   #679	-> 69
    //   #680	-> 88
    //   #681	-> 107
    //   #682	-> 120
    //   #683	-> 159
    //   #685	-> 199
    //   #687	-> 204
    //   #688	-> 209
    //   #689	-> 214
    //   #690	-> 219
    //   #693	-> 224
    //   #694	-> 233
    //   #695	-> 255
    //   #696	-> 277
    //   #697	-> 299
    //   #698	-> 304
    //   #699	-> 309
    //   #700	-> 314
    //   #701	-> 327
    //   #702	-> 337
    //   #703	-> 350
    //   #705	-> 359
    //   #707	-> 364
    //   #710	-> 375
    //   #712	-> 394
    //   #715	-> 397
    //   #716	-> 405
    //   #718	-> 413
    //   #720	-> 419
    //   #722	-> 438
    //   #724	-> 466
    //   #726	-> 476
    //   #728	-> 490
    //   #718	-> 501
    //   #733	-> 521
    //   #734	-> 534
    //   #736	-> 539
    //   #738	-> 544
    //   #741	-> 547
    //   #742	-> 559
    //   #743	-> 572
    //   #744	-> 577
    //   #745	-> 582
    //   #747	-> 587
    //   #749	-> 610
    //   #750	-> 617
    //   #751	-> 631
    //   #753	-> 653
    //   #755	-> 661
    //   #757	-> 683
    //   #759	-> 711
    //   #761	-> 752
    //   #763	-> 757
    //   #765	-> 789
    //   #767	-> 858
    //   #768	-> 868
    //   #770	-> 874
    //   #772	-> 879
    //   #775	-> 884
    //   #776	-> 895
    //   #778	-> 900
    //   #780	-> 912
    //   #783	-> 922
    //   #785	-> 934
    //   #755	-> 944
    //   #747	-> 954
    //   #794	-> 964
    //   #796	-> 969
    //   #798	-> 979
    //   #800	-> 1003
    //   #802	-> 1031
    //   #804	-> 1036
    //   #807	-> 1041
    //   #798	-> 1051
    //   #812	-> 1061
    //   #814	-> 1090
    //   #815	-> 1103
    //   #816	-> 1110
    //   #818	-> 1129
    //   #820	-> 1139
    //   #821	-> 1145
    //   #822	-> 1148
    //   #823	-> 1156
    //   #824	-> 1159
    //   #826	-> 1167
    //   #828	-> 1173
    //   #830	-> 1187
    //   #832	-> 1215
    //   #834	-> 1220
    //   #837	-> 1225
    //   #826	-> 1236
    //   #841	-> 1251
    //   #842	-> 1259
    //   #843	-> 1262
    //   #844	-> 1266
    //   #845	-> 1274
    //   #846	-> 1277
    //   #847	-> 1281
    //   #848	-> 1284
    //   #849	-> 1287
    //   #850	-> 1290
    //   #851	-> 1296
    //   #852	-> 1299
    //   #855	-> 1302
    //   #858	-> 1313
    //   #860	-> 1337
    //   #861	-> 1350
    //   #863	-> 1369
    //   #865	-> 1379
    //   #866	-> 1382
    //   #867	-> 1385
    //   #868	-> 1395
    //   #869	-> 1398
    //   #871	-> 1406
    //   #873	-> 1412
    //   #875	-> 1426
    //   #877	-> 1454
    //   #879	-> 1459
    //   #882	-> 1464
    //   #871	-> 1475
    //   #886	-> 1490
    //   #887	-> 1498
    //   #888	-> 1501
    //   #889	-> 1511
    //   #890	-> 1514
    //   #894	-> 1517
    //   #895	-> 1529
    //   #897	-> 1537
    //   #899	-> 1542
    //   #900	-> 1545
    //   #903	-> 1548
    //   #904	-> 1561
    //   #906	-> 1564
    //   #908	-> 1573
    //   #913	-> 1579
    //   #915	-> 1602
    //   #916	-> 1609
    //   #918	-> 1622
    //   #920	-> 1632
    //   #928	-> 1641
    //   #930	-> 1651
    //   #933	-> 1654
    //   #935	-> 1666
    //   #937	-> 1671
    //   #940	-> 1674
    //   #942	-> 1697
    //   #944	-> 1714
    //   #951	-> 1730
    //   #953	-> 1737
    //   #955	-> 1751
    //   #956	-> 1760
    //   #958	-> 1782
    //   #960	-> 1791
    //   #964	-> 1797
    //   #966	-> 1802
    //   #969	-> 1807
    //   #970	-> 1817
    //   #971	-> 1827
    //   #922	-> 1833
    //   #913	-> 1836
    //   #976	-> 1846
    //   #978	-> 1854
    //   #980	-> 1880
    //   #982	-> 1908
    //   #984	-> 1913
    //   #987	-> 1918
    //   #978	-> 1928
    //   #976	-> 1938
    //   #992	-> 1948
    //   #994	-> 1957
    //   #997	-> 1965
    //   #998	-> 1970
    //   #1000	-> 1974
    //   #1002	-> 2005
    //   #1004	-> 2012
    //   #1006	-> 2032
    //   #1008	-> 2043
    //   #1010	-> 2051
    //   #1012	-> 2058
    //   #1014	-> 2066
    //   #1015	-> 2076
    //   #1016	-> 2087
    //   #1017	-> 2090
    //   #1019	-> 2098
    //   #1020	-> 2108
    //   #1024	-> 2119
    //   #1026	-> 2130
    //   #1028	-> 2145
    //   #1030	-> 2150
    //   #1033	-> 2155
    //   #1000	-> 2169
    //   #1038	-> 2179
    //   #1039	-> 2183
    //   #1040	-> 2188
    //   #1041	-> 2198
    //   #1043	-> 2208
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   0	2209	0	this	Lnet/minecraft/client/renderer/RenderGlobal;
    //   0	2209	1	renderViewEntity	Lnet/minecraft/entity/Entity;
    //   0	2209	2	camera	Lnet/minecraft/client/renderer/culling/ICamera;
    //   0	2209	3	partialTicks	F
    //   3	2206	4	i	I
    //   69	2139	5	d0	D
    //   88	2120	7	d1	D
    //   107	2101	9	d2	D
    //   233	1975	11	entity	Lnet/minecraft/entity/Entity;
    //   255	1953	12	d3	D
    //   277	1931	14	d4	D
    //   299	1909	16	d5	D
    //   359	1849	18	list	Ljava/util/List;
    //   405	1803	19	flag	Z
    //   413	1795	20	flag1	Z
    //   416	105	21	j	I
    //   438	63	22	entity1	Lnet/minecraft/entity/Entity;
    //   539	1669	21	flag4	Z
    //   559	1649	22	flag5	Z
    //   577	1631	23	list1	Ljava/util/List;
    //   582	1626	24	list2	Ljava/util/List;
    //   587	1621	25	blockpos$pooledmutableblockpos	Lnet/minecraft/util/math/BlockPos$PooledMutableBlockPos;
    //   610	344	26	renderglobal$containerlocalrenderinformation0	Ljava/lang/Object;
    //   617	337	28	renderglobal$containerlocalrenderinformation	Lnet/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation;
    //   631	323	29	chunk	Lnet/minecraft/world/chunk/Chunk;
    //   653	301	30	classinheritancemultimap	Lnet/minecraft/util/ClassInheritanceMultiMap;
    //   683	261	31	entity2	Lnet/minecraft/entity/Entity;
    //   752	192	33	flag2	Z
    //   789	155	34	flag3	Z
    //   1003	48	26	entity3	Lnet/minecraft/entity/Entity;
    //   1170	81	26	k	I
    //   1187	49	27	entity4	Lnet/minecraft/entity/Entity;
    //   1409	81	26	l	I
    //   1426	49	27	entity5	Lnet/minecraft/entity/Entity;
    //   1537	671	26	fontrenderer	Lnet/minecraft/client/gui/FontRenderer;
    //   1602	234	27	renderglobal$containerlocalrenderinformation10	Ljava/lang/Object;
    //   1609	227	29	renderglobal$containerlocalrenderinformation1	Lnet/minecraft/client/renderer/RenderGlobal$ContainerLocalRenderInformation;
    //   1622	214	30	list3	Ljava/util/List;
    //   1641	195	31	iterator	Ljava/util/Iterator;
    //   1666	167	32	tileentity1	Lnet/minecraft/tileentity/TileEntity;
    //   1714	16	33	axisalignedbb	Lnet/minecraft/util/math/AxisAlignedBB;
    //   1737	96	33	oclass	Ljava/lang/Class;
    //   1760	37	34	entityplayer	Lnet/minecraft/entity/player/EntityPlayer;
    //   1782	15	35	d6	D
    //   1880	48	28	tileentity	Lnet/minecraft/tileentity/TileEntity;
    //   2005	164	27	destroyblockprogress	Lnet/minecraft/client/renderer/DestroyBlockProgress;
    //   2012	157	29	blockpos	Lnet/minecraft/util/math/BlockPos;
    //   2043	126	30	tileentity2	Lnet/minecraft/tileentity/TileEntity;
    //   2058	61	31	tileentitychest	Lnet/minecraft/tileentity/TileEntityChest;
    //   2130	39	31	iblockstate	Lnet/minecraft/block/state/IBlockState;
    // Local variable type table:
    //   start	length	slot	name	signature
    //   359	1849	18	list	Ljava/util/List<Lnet/minecraft/entity/Entity;>;
    //   577	1631	23	list1	Ljava/util/List<Lnet/minecraft/entity/Entity;>;
    //   582	1626	24	list2	Ljava/util/List<Lnet/minecraft/entity/Entity;>;
    //   653	301	30	classinheritancemultimap	Lnet/minecraft/util/ClassInheritanceMultiMap<Lnet/minecraft/entity/Entity;>;
    //   1622	214	30	list3	Ljava/util/List<Lnet/minecraft/tileentity/TileEntity;>;
    // Exception table:
    //   from	to	target	type
    //   1854	1941	1944	finally
    //   1944	1947	1944	finally
  }
  
  private boolean isOutlineActive(Entity p_184383_1_, Entity p_184383_2_, ICamera p_184383_3_) {
    boolean flag = (p_184383_2_ instanceof EntityLivingBase && ((EntityLivingBase)p_184383_2_).isPlayerSleeping());
    if (p_184383_1_ == p_184383_2_ && this.mc.gameSettings.thirdPersonView == 0 && !flag)
      return false; 
    if (p_184383_1_.isGlowing())
      return true; 
    if (this.mc.player.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown() && p_184383_1_ instanceof EntityPlayer)
      return !(!p_184383_1_.ignoreFrustumCheck && !p_184383_3_.isBoundingBoxInFrustum(p_184383_1_.getEntityBoundingBox()) && !p_184383_1_.isRidingOrBeingRiddenBy((Entity)this.mc.player)); 
    return false;
  }
  
  public String getDebugInfoRenders() {
    int i = this.viewFrustum.renderChunks.length;
    int j = getRenderedChunks();
    return String.format("C: %d/%d %sD: %d, L: %d, %s", new Object[] { Integer.valueOf(j), Integer.valueOf(i), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), Integer.valueOf(this.setLightUpdates.size()), (this.renderDispatcher == null) ? "null" : this.renderDispatcher.getDebugInfo() });
  }
  
  protected int getRenderedChunks() {
    int i = 0;
    for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
      CompiledChunk compiledchunk = renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk;
      if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty())
        i++; 
    } 
    return i;
  }
  
  public String getDebugInfoEntities() {
    return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", " + Config.getVersionDebug();
  }
  
  public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
    Frustum frustum;
    if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
      loadRenderers(); 
    this.theWorld.theProfiler.startSection("camera");
    double d0 = viewEntity.posX - this.frustumUpdatePosX;
    double d1 = viewEntity.posY - this.frustumUpdatePosY;
    double d2 = viewEntity.posZ - this.frustumUpdatePosZ;
    if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d0 * d0 + d1 * d1 + d2 * d2 > 16.0D) {
      this.frustumUpdatePosX = viewEntity.posX;
      this.frustumUpdatePosY = viewEntity.posY;
      this.frustumUpdatePosZ = viewEntity.posZ;
      this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
      this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
      this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
      this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
    } 
    if (Config.isDynamicLights())
      DynamicLights.update(this); 
    this.theWorld.theProfiler.endStartSection("renderlistcamera");
    double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
    double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
    double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
    this.renderContainer.initialize(d3, d4, d5);
    this.theWorld.theProfiler.endStartSection("cull");
    if (this.debugFixedClippingHelper != null) {
      Frustum frustum1 = new Frustum(this.debugFixedClippingHelper);
      frustum1.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
      frustum = frustum1;
    } 
    this.mc.mcProfiler.endStartSection("culling");
    BlockPos blockpos1 = new BlockPos(d3, d4 + viewEntity.getEyeHeight(), d5);
    RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos1);
    this.displayListEntitiesDirty = !(!this.displayListEntitiesDirty && this.chunksToUpdate.isEmpty() && viewEntity.posX == this.lastViewEntityX && viewEntity.posY == this.lastViewEntityY && viewEntity.posZ == this.lastViewEntityZ && viewEntity.rotationPitch == this.lastViewEntityPitch && viewEntity.rotationYaw == this.lastViewEntityYaw);
    this.lastViewEntityX = viewEntity.posX;
    this.lastViewEntityY = viewEntity.posY;
    this.lastViewEntityZ = viewEntity.posZ;
    this.lastViewEntityPitch = viewEntity.rotationPitch;
    this.lastViewEntityYaw = viewEntity.rotationYaw;
    boolean flag = (this.debugFixedClippingHelper != null);
    this.mc.mcProfiler.endStartSection("update");
    Lagometer.timerVisibility.start();
    int i = getCountLoadedChunks();
    if (i != this.countLoadedChunksPrev) {
      this.countLoadedChunksPrev = i;
      this.displayListEntitiesDirty = true;
    } 
    if (Shaders.isShadowPass) {
      this.renderInfos = this.renderInfosShadow;
      this.renderInfosEntities = this.renderInfosEntitiesShadow;
      this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
      if (!flag && this.displayListEntitiesDirty) {
        this.renderInfos.clear();
        this.renderInfosEntities.clear();
        this.renderInfosTileEntities.clear();
        RenderInfoLazy renderinfolazy = new RenderInfoLazy();
        Iterator<RenderChunk> iterator = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, viewEntity, this.renderDistanceChunks, this.viewFrustum);
        while (iterator.hasNext()) {
          RenderChunk renderchunk1 = iterator.next();
          if (renderchunk1 != null) {
            renderinfolazy.setRenderChunk(renderchunk1);
            if (!renderchunk1.compiledChunk.isEmpty() || renderchunk1.isNeedsUpdate())
              this.renderInfos.add(renderinfolazy.getRenderInfo()); 
            BlockPos blockpos = renderchunk1.getPosition();
            if (ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(blockpos)))
              this.renderInfosEntities.add(renderinfolazy.getRenderInfo()); 
            if (renderchunk1.getCompiledChunk().getTileEntities().size() > 0)
              this.renderInfosTileEntities.add(renderinfolazy.getRenderInfo()); 
          } 
        } 
      } 
    } else {
      this.renderInfos = this.renderInfosNormal;
      this.renderInfosEntities = this.renderInfosEntitiesNormal;
      this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
    } 
    if (!flag && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
      this.displayListEntitiesDirty = false;
      for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1 : this.renderInfos)
        freeRenderInformation(renderglobal$containerlocalrenderinformation1); 
      this.renderInfos.clear();
      this.renderInfosEntities.clear();
      this.renderInfosTileEntities.clear();
      this.visibilityDeque.clear();
      Deque<ContainerLocalRenderInformation> deque = this.visibilityDeque;
      Entity.setRenderDistanceWeight(MathHelper.clamp(this.mc.gameSettings.renderDistanceChunks / 8.0D, 1.0D, 2.5D));
      boolean flag2 = this.mc.renderChunksMany;
      if (renderchunk != null) {
        boolean flag3 = false;
        ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation3 = new ContainerLocalRenderInformation(renderchunk, null, 0);
        Set set1 = SET_ALL_FACINGS;
        if (set1.size() == 1) {
          Vector3f vector3f = getViewVector(viewEntity, partialTicks);
          EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
          set1.remove(enumfacing);
        } 
        if (set1.isEmpty())
          flag3 = true; 
        if (flag3 && !playerSpectator) {
          this.renderInfos.add(renderglobal$containerlocalrenderinformation3);
        } else {
          if (playerSpectator && this.theWorld.getBlockState(blockpos1).isOpaqueCube())
            flag2 = false; 
          renderchunk.setFrameIndex(frameCount);
          deque.add(renderglobal$containerlocalrenderinformation3);
        } 
      } else {
        int i1 = (blockpos1.getY() > 0) ? 248 : 8;
        for (int j1 = -this.renderDistanceChunks; j1 <= this.renderDistanceChunks; j1++) {
          for (int j = -this.renderDistanceChunks; j <= this.renderDistanceChunks; j++) {
            RenderChunk renderchunk2 = this.viewFrustum.getRenderChunk(new BlockPos((j1 << 4) + 8, i1, (j << 4) + 8));
            if (renderchunk2 != null && frustum.isBoundingBoxInFrustum(renderchunk2.boundingBox)) {
              renderchunk2.setFrameIndex(frameCount);
              deque.add(new ContainerLocalRenderInformation(renderchunk2, null, 0));
            } 
          } 
        } 
      } 
      this.mc.mcProfiler.startSection("iteration");
      EnumFacing[] aenumfacing = EnumFacing.VALUES;
      int k1 = aenumfacing.length;
      while (!deque.isEmpty()) {
        ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation4 = deque.poll();
        RenderChunk renderchunk5 = renderglobal$containerlocalrenderinformation4.renderChunk;
        EnumFacing enumfacing2 = renderglobal$containerlocalrenderinformation4.facing;
        boolean flag1 = false;
        CompiledChunk compiledchunk = renderchunk5.compiledChunk;
        if (!compiledchunk.isEmpty() || renderchunk5.isNeedsUpdate()) {
          this.renderInfos.add(renderglobal$containerlocalrenderinformation4);
          flag1 = true;
        } 
        if (ChunkUtils.hasEntities(renderchunk5.getChunk((World)this.theWorld))) {
          this.renderInfosEntities.add(renderglobal$containerlocalrenderinformation4);
          flag1 = true;
        } 
        if (compiledchunk.getTileEntities().size() > 0) {
          this.renderInfosTileEntities.add(renderglobal$containerlocalrenderinformation4);
          flag1 = true;
        } 
        for (int k = 0; k < k1; k++) {
          EnumFacing enumfacing1 = aenumfacing[k];
          if ((!flag2 || !renderglobal$containerlocalrenderinformation4.hasDirection(enumfacing1.getOpposite())) && (!flag2 || enumfacing2 == null || compiledchunk.isVisible(enumfacing2.getOpposite(), enumfacing1))) {
            RenderChunk renderchunk3 = getRenderChunkOffset(blockpos1, renderchunk5, enumfacing1);
            if (renderchunk3 != null && renderchunk3.setFrameIndex(frameCount) && frustum.isBoundingBoxInFrustum(renderchunk3.boundingBox)) {
              int l = renderglobal$containerlocalrenderinformation4.setFacing | 1 << enumfacing1.ordinal();
              ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation = allocateRenderInformation(renderchunk3, enumfacing1, l);
              deque.add(renderglobal$containerlocalrenderinformation);
            } 
          } 
        } 
        if (!flag1)
          freeRenderInformation(renderglobal$containerlocalrenderinformation4); 
      } 
      this.mc.mcProfiler.endSection();
    } 
    this.mc.mcProfiler.endStartSection("captureFrustum");
    if (this.debugFixTerrainFrustum) {
      fixTerrainFrustum(d3, d4, d5);
      this.debugFixTerrainFrustum = false;
    } 
    Lagometer.timerVisibility.end();
    if (Shaders.isShadowPass) {
      Shaders.mcProfilerEndSection();
    } else {
      this.mc.mcProfiler.endStartSection("rebuildNear");
      Set<RenderChunk> set = this.chunksToUpdate;
      this.chunksToUpdate = Sets.newLinkedHashSet();
      Lagometer.timerChunkUpdate.start();
      for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation2 : this.renderInfos) {
        RenderChunk renderchunk4 = renderglobal$containerlocalrenderinformation2.renderChunk;
        if (renderchunk4.isNeedsUpdate() || set.contains(renderchunk4)) {
          this.displayListEntitiesDirty = true;
          BlockPos blockpos2 = renderchunk4.getPosition();
          boolean flag4 = (blockpos1.distanceSq((blockpos2.getX() + 8), (blockpos2.getY() + 8), (blockpos2.getZ() + 8)) < 768.0D);
          if (!flag4) {
            this.chunksToUpdate.add(renderchunk4);
            continue;
          } 
          if (!renderchunk4.isPlayerUpdate()) {
            this.chunksToUpdateForced.add(renderchunk4);
            continue;
          } 
          this.mc.mcProfiler.startSection("build near");
          this.renderDispatcher.updateChunkNow(renderchunk4);
          renderchunk4.clearNeedsUpdate();
          this.mc.mcProfiler.endSection();
        } 
      } 
      Lagometer.timerChunkUpdate.end();
      this.chunksToUpdate.addAll(set);
      this.mc.mcProfiler.endSection();
    } 
  }
  
  private Set<EnumFacing> getVisibleFacings(BlockPos pos) {
    VisGraph visgraph = new VisGraph();
    BlockPos blockpos = new BlockPos(pos.getX() >> 4 << 4, pos.getY() >> 4 << 4, pos.getZ() >> 4 << 4);
    Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos);
    for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(blockpos, blockpos.add(15, 15, 15))) {
      if (chunk.getBlockState((BlockPos)blockpos$mutableblockpos).isOpaqueCube())
        visgraph.setOpaqueCube((BlockPos)blockpos$mutableblockpos); 
    } 
    return visgraph.getVisibleFacings(pos);
  }
  
  @Nullable
  private RenderChunk getRenderChunkOffset(BlockPos playerPos, RenderChunk renderChunkBase, EnumFacing facing) {
    BlockPos blockpos = renderChunkBase.getBlockPosOffset16(facing);
    if (blockpos.getY() >= 0 && blockpos.getY() < 256) {
      int i = playerPos.getX() - blockpos.getX();
      int j = playerPos.getZ() - blockpos.getZ();
      if (Config.isFogOff()) {
        if (Math.abs(i) > this.renderDistance || Math.abs(j) > this.renderDistance)
          return null; 
      } else {
        int k = i * i + j * j;
        if (k > this.renderDistanceSq)
          return null; 
      } 
      return renderChunkBase.getRenderChunkOffset16(this.viewFrustum, facing);
    } 
    return null;
  }
  
  private void fixTerrainFrustum(double x, double y, double z) {
    this.debugFixedClippingHelper = (ClippingHelper)new ClippingHelperImpl();
    ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
    Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
    matrix4f.transpose();
    Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
    matrix4f1.transpose();
    Matrix4f matrix4f2 = new Matrix4f();
    Matrix4f.mul(matrix4f1, matrix4f, matrix4f2);
    matrix4f2.invert();
    this.debugTerrainFrustumPosition.x = x;
    this.debugTerrainFrustumPosition.y = y;
    this.debugTerrainFrustumPosition.z = z;
    this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
    this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
    this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
    this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
    for (int i = 0; i < 8; i++) {
      Matrix4f.transform(matrix4f2, this.debugTerrainMatrix[i], this.debugTerrainMatrix[i]);
      (this.debugTerrainMatrix[i]).x /= (this.debugTerrainMatrix[i]).w;
      (this.debugTerrainMatrix[i]).y /= (this.debugTerrainMatrix[i]).w;
      (this.debugTerrainMatrix[i]).z /= (this.debugTerrainMatrix[i]).w;
      (this.debugTerrainMatrix[i]).w = 1.0F;
    } 
  }
  
  protected Vector3f getViewVector(Entity entityIn, double partialTicks) {
    float f = (float)(entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
    float f1 = (float)(entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
    if ((Minecraft.getMinecraft()).gameSettings.thirdPersonView == 2)
      f += 180.0F; 
    float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
    float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
    float f4 = -MathHelper.cos(-f * 0.017453292F);
    float f5 = MathHelper.sin(-f * 0.017453292F);
    return new Vector3f(f3 * f4, f5, f2 * f4);
  }
  
  public int renderBlockLayer(BlockRenderLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
    RenderHelper.disableStandardItemLighting();
    if (blockLayerIn == BlockRenderLayer.TRANSLUCENT) {
      this.mc.mcProfiler.startSection("translucent_sort");
      double d0 = entityIn.posX - this.prevRenderSortX;
      double d1 = entityIn.posY - this.prevRenderSortY;
      double d2 = entityIn.posZ - this.prevRenderSortZ;
      if (d0 * d0 + d1 * d1 + d2 * d2 > 1.0D) {
        this.prevRenderSortX = entityIn.posX;
        this.prevRenderSortY = entityIn.posY;
        this.prevRenderSortZ = entityIn.posZ;
        int k = 0;
        this.chunksToResortTransparency.clear();
        for (ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation : this.renderInfos) {
          if (renderglobal$containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && k++ < 15)
            this.chunksToResortTransparency.add(renderglobal$containerlocalrenderinformation.renderChunk); 
        } 
      } 
      this.mc.mcProfiler.endSection();
    } 
    this.mc.mcProfiler.startSection("filterempty");
    int l = 0;
    boolean flag = (blockLayerIn == BlockRenderLayer.TRANSLUCENT);
    int i1 = flag ? (this.renderInfos.size() - 1) : 0;
    int i = flag ? -1 : this.renderInfos.size();
    int j1 = flag ? -1 : 1;
    for (int j = i1; j != i; j += j1) {
      RenderChunk renderchunk = ((ContainerLocalRenderInformation)this.renderInfos.get(j)).renderChunk;
      if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
        l++;
        this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
      } 
    } 
    if (l == 0) {
      this.mc.mcProfiler.endSection();
      return l;
    } 
    if (Config.isFogOff() && this.mc.entityRenderer.fogStandard)
      GlStateManager.disableFog(); 
    this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
    renderBlockLayer(blockLayerIn);
    this.mc.mcProfiler.endSection();
    return l;
  }
  
  private void renderBlockLayer(BlockRenderLayer blockLayerIn) {
    this.mc.entityRenderer.enableLightmap();
    if (OpenGlHelper.useVbo()) {
      GlStateManager.glEnableClientState(32884);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.glEnableClientState(32888);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.glEnableClientState(32888);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.glEnableClientState(32886);
    } 
    if (Config.isShaders())
      ShadersRender.preRenderChunkLayer(blockLayerIn); 
    this.renderContainer.renderChunkLayer(blockLayerIn);
    if (Config.isShaders())
      ShadersRender.postRenderChunkLayer(blockLayerIn); 
    if (OpenGlHelper.useVbo())
      for (VertexFormatElement vertexformatelement : DefaultVertexFormats.BLOCK.getElements()) {
        VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
        int k1 = vertexformatelement.getIndex();
        switch (vertexformatelement$enumusage) {
          case POSITION:
            GlStateManager.glDisableClientState(32884);
          case UV:
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + k1);
            GlStateManager.glDisableClientState(32888);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
          case COLOR:
            GlStateManager.glDisableClientState(32886);
            GlStateManager.resetColor();
        } 
      }  
    this.mc.entityRenderer.disableLightmap();
  }
  
  private void cleanupDamagedBlocks(Iterator<DestroyBlockProgress> iteratorIn) {
    while (iteratorIn.hasNext()) {
      DestroyBlockProgress destroyblockprogress = iteratorIn.next();
      int k1 = destroyblockprogress.getCreationCloudUpdateTick();
      if (this.cloudTickCounter - k1 > 400)
        iteratorIn.remove(); 
    } 
  }
  
  public void updateClouds() {
    if (Config.isShaders() && Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
      Shaders.uninit();
      Shaders.loadShaderPack();
      Reflector.Minecraft_actionKeyF3.setValue(this.mc, Boolean.TRUE);
    } 
    this.cloudTickCounter++;
    if (this.cloudTickCounter % 20 == 0)
      cleanupDamagedBlocks(this.damagedBlocks.values().iterator()); 
    if (!this.setLightUpdates.isEmpty() && !this.renderDispatcher.hasNoFreeRenderBuilders() && this.chunksToUpdate.isEmpty()) {
      Iterator<BlockPos> iterator = this.setLightUpdates.iterator();
      while (iterator.hasNext()) {
        BlockPos blockpos = iterator.next();
        iterator.remove();
        int k1 = blockpos.getX();
        int l1 = blockpos.getY();
        int i2 = blockpos.getZ();
        markBlocksForUpdate(k1 - 1, l1 - 1, i2 - 1, k1 + 1, l1 + 1, i2 + 1, false);
      } 
    } 
  }
  
  private void renderSkyEnd() {
    if (Config.isSkyEnabled()) {
      GlStateManager.disableFog();
      GlStateManager.disableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.depthMask(false);
      this.renderEngine.bindTexture(END_SKY_TEXTURES);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      for (int k1 = 0; k1 < 6; k1++) {
        GlStateManager.pushMatrix();
        if (k1 == 1)
          GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F); 
        if (k1 == 2)
          GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F); 
        if (k1 == 3)
          GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F); 
        if (k1 == 4)
          GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F); 
        if (k1 == 5)
          GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F); 
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int l1 = 40;
        int i2 = 40;
        int j2 = 40;
        if (Config.isCustomColors()) {
          Vec3d vec3d = new Vec3d(l1 / 255.0D, i2 / 255.0D, j2 / 255.0D);
          vec3d = CustomColors.getWorldSkyColor(vec3d, (World)this.theWorld, this.mc.getRenderViewEntity(), 0.0F);
          l1 = (int)(vec3d.xCoord * 255.0D);
          i2 = (int)(vec3d.yCoord * 255.0D);
          j2 = (int)(vec3d.zCoord * 255.0D);
        } 
        bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(l1, i2, j2, 255).endVertex();
        bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(l1, i2, j2, 255).endVertex();
        bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(l1, i2, j2, 255).endVertex();
        bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(l1, i2, j2, 255).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
      } 
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
    } 
  }
  
  public void renderSky(float partialTicks, int pass) {
    if (Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
      WorldProvider worldprovider = this.mc.world.provider;
      Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
      if (object != null) {
        Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
        return;
      } 
    } 
    if (this.mc.world.provider.getDimensionType() == DimensionType.THE_END) {
      renderSkyEnd();
    } else if (this.mc.world.provider.isSurfaceWorld()) {
      GlStateManager.disableTexture2D();
      boolean flag1 = Config.isShaders();
      if (flag1)
        Shaders.disableTexture2D(); 
      Vec3d vec3d = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
      vec3d = CustomColors.getSkyColor(vec3d, (IBlockAccess)this.mc.world, (this.mc.getRenderViewEntity()).posX, (this.mc.getRenderViewEntity()).posY + 1.0D, (this.mc.getRenderViewEntity()).posZ);
      if (flag1)
        Shaders.setSkyColor(vec3d); 
      float f = (float)vec3d.xCoord;
      float f1 = (float)vec3d.yCoord;
      float f2 = (float)vec3d.zCoord;
      if (pass != 2) {
        float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
        float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
        float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
        f = f3;
        f1 = f4;
        f2 = f5;
      } 
      GlStateManager.color(f, f1, f2);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.depthMask(false);
      GlStateManager.enableFog();
      if (flag1)
        Shaders.enableFog(); 
      GlStateManager.color(f, f1, f2);
      if (flag1)
        Shaders.preSkyList(); 
      if (Config.isSkyEnabled())
        if (this.vboEnabled) {
          this.skyVBO.bindBuffer();
          GlStateManager.glEnableClientState(32884);
          GlStateManager.glVertexPointer(3, 5126, 12, 0);
          this.skyVBO.drawArrays(7);
          this.skyVBO.unbindBuffer();
          GlStateManager.glDisableClientState(32884);
        } else {
          GlStateManager.callList(this.glSkyList);
        }  
      GlStateManager.disableFog();
      if (flag1)
        Shaders.disableFog(); 
      GlStateManager.disableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      RenderHelper.disableStandardItemLighting();
      float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
      if (afloat != null && Config.isSunMoonEnabled()) {
        GlStateManager.disableTexture2D();
        if (flag1)
          Shaders.disableTexture2D(); 
        GlStateManager.shadeModel(7425);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F) ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        float f6 = afloat[0];
        float f7 = afloat[1];
        float f8 = afloat[2];
        if (pass != 2) {
          float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
          float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
          float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
          f6 = f9;
          f7 = f10;
          f8 = f11;
        } 
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
        int l1 = 16;
        for (int j2 = 0; j2 <= 16; j2++) {
          float f18 = j2 * 6.2831855F / 16.0F;
          float f12 = MathHelper.sin(f18);
          float f13 = MathHelper.cos(f18);
          bufferbuilder.pos((f12 * 120.0F), (f13 * 120.0F), (-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
        } 
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.shadeModel(7424);
      } 
      GlStateManager.enableTexture2D();
      if (flag1)
        Shaders.enableTexture2D(); 
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.pushMatrix();
      float f15 = 1.0F - this.theWorld.getRainStrength(partialTicks);
      GlStateManager.color(1.0F, 1.0F, 1.0F, f15);
      GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
      CustomSky.renderSky((World)this.theWorld, this.renderEngine, partialTicks);
      if (flag1)
        Shaders.preCelestialRotate(); 
      GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
      if (flag1)
        Shaders.postCelestialRotate(); 
      float f16 = 30.0F;
      if (Config.isSunTexture()) {
        this.renderEngine.bindTexture(SUN_TEXTURES);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-f16, 100.0D, -f16).tex(0.0D, 0.0D).endVertex();
        bufferbuilder.pos(f16, 100.0D, -f16).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(f16, 100.0D, f16).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(-f16, 100.0D, f16).tex(0.0D, 1.0D).endVertex();
        tessellator.draw();
      } 
      f16 = 20.0F;
      if (Config.isMoonTexture()) {
        this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
        int k1 = this.theWorld.getMoonPhase();
        int i2 = k1 % 4;
        int k2 = k1 / 4 % 2;
        float f19 = (i2 + 0) / 4.0F;
        float f21 = (k2 + 0) / 2.0F;
        float f23 = (i2 + 1) / 4.0F;
        float f14 = (k2 + 1) / 2.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-f16, -100.0D, f16).tex(f23, f14).endVertex();
        bufferbuilder.pos(f16, -100.0D, f16).tex(f19, f14).endVertex();
        bufferbuilder.pos(f16, -100.0D, -f16).tex(f19, f21).endVertex();
        bufferbuilder.pos(-f16, -100.0D, -f16).tex(f23, f21).endVertex();
        tessellator.draw();
      } 
      GlStateManager.disableTexture2D();
      if (flag1)
        Shaders.disableTexture2D(); 
      float f17 = this.theWorld.getStarBrightness(partialTicks) * f15;
      if (f17 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers((World)this.theWorld)) {
        GlStateManager.color(f17, f17, f17, f17);
        if (this.vboEnabled) {
          this.starVBO.bindBuffer();
          GlStateManager.glEnableClientState(32884);
          GlStateManager.glVertexPointer(3, 5126, 12, 0);
          this.starVBO.drawArrays(7);
          this.starVBO.unbindBuffer();
          GlStateManager.glDisableClientState(32884);
        } else {
          GlStateManager.callList(this.starGLCallList);
        } 
      } 
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableFog();
      if (flag1)
        Shaders.enableFog(); 
      GlStateManager.popMatrix();
      GlStateManager.disableTexture2D();
      if (flag1)
        Shaders.disableTexture2D(); 
      GlStateManager.color(0.0F, 0.0F, 0.0F);
      double d3 = (this.mc.player.getPositionEyes(partialTicks)).yCoord - this.theWorld.getHorizon();
      if (d3 < 0.0D) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 12.0F, 0.0F);
        if (this.vboEnabled) {
          this.sky2VBO.bindBuffer();
          GlStateManager.glEnableClientState(32884);
          GlStateManager.glVertexPointer(3, 5126, 12, 0);
          this.sky2VBO.drawArrays(7);
          this.sky2VBO.unbindBuffer();
          GlStateManager.glDisableClientState(32884);
        } else {
          GlStateManager.callList(this.glSkyList2);
        } 
        GlStateManager.popMatrix();
        float f20 = 1.0F;
        float f22 = -((float)(d3 + 65.0D));
        float f24 = -1.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(-1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, f22, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, f22, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
      } 
      if (this.theWorld.provider.isSkyColored()) {
        GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
      } else {
        GlStateManager.color(f, f1, f2);
      } 
      if (this.mc.gameSettings.renderDistanceChunks <= 4)
        GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue); 
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, -((float)(d3 - 16.0D)), 0.0F);
      if (Config.isSkyEnabled())
        GlStateManager.callList(this.glSkyList2); 
      GlStateManager.popMatrix();
      GlStateManager.enableTexture2D();
      if (flag1)
        Shaders.enableTexture2D(); 
      GlStateManager.depthMask(true);
    } 
  }
  
  public void renderClouds(float partialTicks, int pass, double p_180447_3_, double p_180447_5_, double p_180447_7_) {
    if (!Config.isCloudsOff()) {
      if (Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
        WorldProvider worldprovider = this.mc.world.provider;
        Object object = Reflector.call(worldprovider, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
        if (object != null) {
          Reflector.callVoid(object, Reflector.IRenderHandler_render, new Object[] { Float.valueOf(partialTicks), this.theWorld, this.mc });
          return;
        } 
      } 
      if (this.mc.world.provider.isSurfaceWorld()) {
        if (Config.isShaders())
          Shaders.beginClouds(); 
        if (Config.isCloudsFancy()) {
          renderCloudsFancy(partialTicks, pass, p_180447_3_, p_180447_5_, p_180447_7_);
        } else {
          float f9 = partialTicks;
          partialTicks = 0.0F;
          GlStateManager.disableCull();
          int l2 = 32;
          int k1 = 8;
          Tessellator tessellator = Tessellator.getInstance();
          BufferBuilder bufferbuilder = tessellator.getBuffer();
          this.renderEngine.bindTexture(CLOUDS_TEXTURES);
          GlStateManager.enableBlend();
          GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
          Vec3d vec3d = this.theWorld.getCloudColour(partialTicks);
          float f = (float)vec3d.xCoord;
          float f1 = (float)vec3d.yCoord;
          float f2 = (float)vec3d.zCoord;
          this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, f9, vec3d);
          if (this.cloudRenderer.shouldUpdateGlList()) {
            this.cloudRenderer.startUpdateGlList();
            if (pass != 2) {
              float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
              float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
              float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
              f = f3;
              f1 = f4;
              f2 = f5;
            } 
            float f10 = 4.8828125E-4F;
            double d5 = (this.cloudTickCounter + partialTicks);
            double d3 = p_180447_3_ + d5 * 0.029999999329447746D;
            int l1 = MathHelper.floor(d3 / 2048.0D);
            int i2 = MathHelper.floor(p_180447_7_ / 2048.0D);
            d3 -= (l1 * 2048);
            double d4 = p_180447_7_ - (i2 * 2048);
            float f6 = this.theWorld.provider.getCloudHeight() - (float)p_180447_5_ + 0.33F;
            f6 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
            float f7 = (float)(d3 * 4.8828125E-4D);
            float f8 = (float)(d4 * 4.8828125E-4D);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            for (int j2 = -256; j2 < 256; j2 += 32) {
              for (int k2 = -256; k2 < 256; k2 += 32) {
                bufferbuilder.pos((j2 + 0), f6, (k2 + 32)).tex(((j2 + 0) * 4.8828125E-4F + f7), ((k2 + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                bufferbuilder.pos((j2 + 32), f6, (k2 + 32)).tex(((j2 + 32) * 4.8828125E-4F + f7), ((k2 + 32) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                bufferbuilder.pos((j2 + 32), f6, (k2 + 0)).tex(((j2 + 32) * 4.8828125E-4F + f7), ((k2 + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
                bufferbuilder.pos((j2 + 0), f6, (k2 + 0)).tex(((j2 + 0) * 4.8828125E-4F + f7), ((k2 + 0) * 4.8828125E-4F + f8)).color(f, f1, f2, 0.8F).endVertex();
              } 
            } 
            tessellator.draw();
            this.cloudRenderer.endUpdateGlList();
          } 
          this.cloudRenderer.renderGlList();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.disableBlend();
          GlStateManager.enableCull();
        } 
        if (Config.isShaders())
          Shaders.endClouds(); 
      } 
    } 
  }
  
  public boolean hasCloudFog(double x, double y, double z, float partialTicks) {
    return false;
  }
  
  private void renderCloudsFancy(float partialTicks, int pass, double p_180445_3_, double p_180445_5_, double p_180445_7_) {
    float f251 = 0.0F;
    GlStateManager.disableCull();
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    float f = 12.0F;
    float f1 = 4.0F;
    double d3 = (this.cloudTickCounter + f251);
    double d4 = (p_180445_3_ + d3 * 0.029999999329447746D) / 12.0D;
    double d5 = p_180445_7_ / 12.0D + 0.33000001311302185D;
    float f2 = this.theWorld.provider.getCloudHeight() - (float)p_180445_5_ + 0.33F;
    f2 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
    int k1 = MathHelper.floor(d4 / 2048.0D);
    int l1 = MathHelper.floor(d5 / 2048.0D);
    d4 -= (k1 * 2048);
    d5 -= (l1 * 2048);
    this.renderEngine.bindTexture(CLOUDS_TEXTURES);
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    Vec3d vec3d = this.theWorld.getCloudColour(f251);
    float f3 = (float)vec3d.xCoord;
    float f4 = (float)vec3d.yCoord;
    float f5 = (float)vec3d.zCoord;
    this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks, vec3d);
    if (pass != 2) {
      float f6 = (f3 * 30.0F + f4 * 59.0F + f5 * 11.0F) / 100.0F;
      float f7 = (f3 * 30.0F + f4 * 70.0F) / 100.0F;
      float f8 = (f3 * 30.0F + f5 * 70.0F) / 100.0F;
      f3 = f6;
      f4 = f7;
      f5 = f8;
    } 
    float f26 = f3 * 0.9F;
    float f27 = f4 * 0.9F;
    float f28 = f5 * 0.9F;
    float f9 = f3 * 0.7F;
    float f10 = f4 * 0.7F;
    float f11 = f5 * 0.7F;
    float f12 = f3 * 0.8F;
    float f13 = f4 * 0.8F;
    float f14 = f5 * 0.8F;
    float f15 = 0.00390625F;
    float f16 = MathHelper.floor(d4) * 0.00390625F;
    float f17 = MathHelper.floor(d5) * 0.00390625F;
    float f18 = (float)(d4 - MathHelper.floor(d4));
    float f19 = (float)(d5 - MathHelper.floor(d5));
    int i2 = 8;
    int j2 = 4;
    float f20 = 9.765625E-4F;
    GlStateManager.scale(12.0F, 1.0F, 12.0F);
    for (int k2 = 0; k2 < 2; k2++) {
      if (k2 == 0) {
        GlStateManager.colorMask(false, false, false, false);
      } else {
        switch (pass) {
          case 0:
            GlStateManager.colorMask(false, true, true, true);
            break;
          case 1:
            GlStateManager.colorMask(true, false, false, true);
            break;
          case 2:
            GlStateManager.colorMask(true, true, true, true);
            break;
        } 
      } 
      this.cloudRenderer.renderGlList();
    } 
    if (this.cloudRenderer.shouldUpdateGlList()) {
      this.cloudRenderer.startUpdateGlList();
      for (int j3 = -3; j3 <= 4; j3++) {
        for (int l2 = -3; l2 <= 4; l2++) {
          bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
          float f21 = (j3 * 8);
          float f22 = (l2 * 8);
          float f23 = f21 - f18;
          float f24 = f22 - f19;
          if (f2 > -5.0F) {
            bufferbuilder.pos((f23 + 0.0F), (f2 + 0.0F), (f24 + 8.0F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
            bufferbuilder.pos((f23 + 8.0F), (f2 + 0.0F), (f24 + 8.0F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
            bufferbuilder.pos((f23 + 8.0F), (f2 + 0.0F), (f24 + 0.0F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
            bufferbuilder.pos((f23 + 0.0F), (f2 + 0.0F), (f24 + 0.0F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f9, f10, f11, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
          } 
          if (f2 <= 5.0F) {
            bufferbuilder.pos((f23 + 0.0F), (f2 + 4.0F - 9.765625E-4F), (f24 + 8.0F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos((f23 + 8.0F), (f2 + 4.0F - 9.765625E-4F), (f24 + 8.0F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos((f23 + 8.0F), (f2 + 4.0F - 9.765625E-4F), (f24 + 0.0F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos((f23 + 0.0F), (f2 + 4.0F - 9.765625E-4F), (f24 + 0.0F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f3, f4, f5, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
          } 
          if (j3 > -1)
            for (int i3 = 0; i3 < 8; i3++) {
              bufferbuilder.pos((f23 + i3 + 0.0F), (f2 + 0.0F), (f24 + 8.0F)).tex(((f21 + i3 + 0.5F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
              bufferbuilder.pos((f23 + i3 + 0.0F), (f2 + 4.0F), (f24 + 8.0F)).tex(((f21 + i3 + 0.5F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
              bufferbuilder.pos((f23 + i3 + 0.0F), (f2 + 4.0F), (f24 + 0.0F)).tex(((f21 + i3 + 0.5F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
              bufferbuilder.pos((f23 + i3 + 0.0F), (f2 + 0.0F), (f24 + 0.0F)).tex(((f21 + i3 + 0.5F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
            }  
          if (j3 <= 1)
            for (int k3 = 0; k3 < 8; k3++) {
              bufferbuilder.pos((f23 + k3 + 1.0F - 9.765625E-4F), (f2 + 0.0F), (f24 + 8.0F)).tex(((f21 + k3 + 0.5F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
              bufferbuilder.pos((f23 + k3 + 1.0F - 9.765625E-4F), (f2 + 4.0F), (f24 + 8.0F)).tex(((f21 + k3 + 0.5F) * 0.00390625F + f16), ((f22 + 8.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
              bufferbuilder.pos((f23 + k3 + 1.0F - 9.765625E-4F), (f2 + 4.0F), (f24 + 0.0F)).tex(((f21 + k3 + 0.5F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
              bufferbuilder.pos((f23 + k3 + 1.0F - 9.765625E-4F), (f2 + 0.0F), (f24 + 0.0F)).tex(((f21 + k3 + 0.5F) * 0.00390625F + f16), ((f22 + 0.0F) * 0.00390625F + f17)).color(f26, f27, f28, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
            }  
          if (l2 > -1)
            for (int l3 = 0; l3 < 8; l3++) {
              bufferbuilder.pos((f23 + 0.0F), (f2 + 4.0F), (f24 + l3 + 0.0F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + l3 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
              bufferbuilder.pos((f23 + 8.0F), (f2 + 4.0F), (f24 + l3 + 0.0F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + l3 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
              bufferbuilder.pos((f23 + 8.0F), (f2 + 0.0F), (f24 + l3 + 0.0F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + l3 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
              bufferbuilder.pos((f23 + 0.0F), (f2 + 0.0F), (f24 + l3 + 0.0F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + l3 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
            }  
          if (l2 <= 1)
            for (int i4 = 0; i4 < 8; i4++) {
              bufferbuilder.pos((f23 + 0.0F), (f2 + 4.0F), (f24 + i4 + 1.0F - 9.765625E-4F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + i4 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
              bufferbuilder.pos((f23 + 8.0F), (f2 + 4.0F), (f24 + i4 + 1.0F - 9.765625E-4F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + i4 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
              bufferbuilder.pos((f23 + 8.0F), (f2 + 0.0F), (f24 + i4 + 1.0F - 9.765625E-4F)).tex(((f21 + 8.0F) * 0.00390625F + f16), ((f22 + i4 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
              bufferbuilder.pos((f23 + 0.0F), (f2 + 0.0F), (f24 + i4 + 1.0F - 9.765625E-4F)).tex(((f21 + 0.0F) * 0.00390625F + f16), ((f22 + i4 + 0.5F) * 0.00390625F + f17)).color(f12, f13, f14, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
            }  
          tessellator.draw();
        } 
      } 
      this.cloudRenderer.endUpdateGlList();
    } 
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.disableBlend();
    GlStateManager.enableCull();
  }
  
  public void updateChunks(long finishTimeNano) {
    finishTimeNano = (long)(finishTimeNano + 1.0E8D);
    this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(finishTimeNano);
    if (this.chunksToUpdateForced.size() > 0) {
      Iterator<RenderChunk> iterator = this.chunksToUpdateForced.iterator();
      while (iterator.hasNext()) {
        RenderChunk renderchunk1 = iterator.next();
        if (!this.renderDispatcher.updateChunkLater(renderchunk1))
          break; 
        renderchunk1.clearNeedsUpdate();
        iterator.remove();
        this.chunksToUpdate.remove(renderchunk1);
        this.chunksToResortTransparency.remove(renderchunk1);
      } 
    } 
    if (this.chunksToResortTransparency.size() > 0) {
      Iterator<RenderChunk> iterator2 = this.chunksToResortTransparency.iterator();
      if (iterator2.hasNext()) {
        RenderChunk renderchunk3 = iterator2.next();
        if (this.renderDispatcher.updateTransparencyLater(renderchunk3))
          iterator2.remove(); 
      } 
    } 
    int l1 = 0;
    int i2 = Config.getUpdatesPerFrame();
    int k1 = i2 * 2;
    if (!this.chunksToUpdate.isEmpty()) {
      Iterator<RenderChunk> iterator1 = this.chunksToUpdate.iterator();
      while (iterator1.hasNext()) {
        boolean flag1;
        RenderChunk renderchunk2 = iterator1.next();
        if (renderchunk2.isNeedsUpdateCustom()) {
          flag1 = this.renderDispatcher.updateChunkNow(renderchunk2);
        } else {
          flag1 = this.renderDispatcher.updateChunkLater(renderchunk2);
        } 
        if (!flag1)
          break; 
        renderchunk2.clearNeedsUpdate();
        iterator1.remove();
        if (renderchunk2.getCompiledChunk().isEmpty() && i2 < k1)
          i2++; 
        l1++;
        if (l1 >= i2)
          break; 
      } 
    } 
  }
  
  public void renderWorldBorder(Entity entityIn, float partialTicks) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    WorldBorder worldborder = this.theWorld.getWorldBorder();
    double d3 = (this.mc.gameSettings.renderDistanceChunks * 16);
    if (entityIn.posX >= worldborder.maxX() - d3 || entityIn.posX <= worldborder.minX() + d3 || entityIn.posZ >= worldborder.maxZ() - d3 || entityIn.posZ <= worldborder.minZ() + d3) {
      double d4 = 1.0D - worldborder.getClosestDistance(entityIn) / d3;
      d4 = Math.pow(d4, 4.0D);
      double d5 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
      double d6 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
      double d7 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      this.renderEngine.bindTexture(FORCEFIELD_TEXTURES);
      GlStateManager.depthMask(false);
      GlStateManager.pushMatrix();
      int k1 = worldborder.getStatus().getID();
      float f = (k1 >> 16 & 0xFF) / 255.0F;
      float f1 = (k1 >> 8 & 0xFF) / 255.0F;
      float f2 = (k1 & 0xFF) / 255.0F;
      GlStateManager.color(f, f1, f2, (float)d4);
      GlStateManager.doPolygonOffset(-3.0F, -3.0F);
      GlStateManager.enablePolygonOffset();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
      GlStateManager.disableCull();
      float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
      float f4 = 0.0F;
      float f5 = 0.0F;
      float f6 = 128.0F;
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.setTranslation(-d5, -d6, -d7);
      double d8 = Math.max(MathHelper.floor(d7 - d3), worldborder.minZ());
      double d9 = Math.min(MathHelper.ceil(d7 + d3), worldborder.maxZ());
      if (d5 > worldborder.maxX() - d3) {
        float f7 = 0.0F;
        for (double d10 = d8; d10 < d9; f7 += 0.5F) {
          double d11 = Math.min(1.0D, d9 - d10);
          float f8 = (float)d11 * 0.5F;
          bufferbuilder.pos(worldborder.maxX(), 256.0D, d10).tex((f3 + f7), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(worldborder.maxX(), 256.0D, d10 + d11).tex((f3 + f8 + f7), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(worldborder.maxX(), 0.0D, d10 + d11).tex((f3 + f8 + f7), (f3 + 128.0F)).endVertex();
          bufferbuilder.pos(worldborder.maxX(), 0.0D, d10).tex((f3 + f7), (f3 + 128.0F)).endVertex();
          d10++;
        } 
      } 
      if (d5 < worldborder.minX() + d3) {
        float f9 = 0.0F;
        for (double d12 = d8; d12 < d9; f9 += 0.5F) {
          double d15 = Math.min(1.0D, d9 - d12);
          float f12 = (float)d15 * 0.5F;
          bufferbuilder.pos(worldborder.minX(), 256.0D, d12).tex((f3 + f9), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(worldborder.minX(), 256.0D, d12 + d15).tex((f3 + f12 + f9), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(worldborder.minX(), 0.0D, d12 + d15).tex((f3 + f12 + f9), (f3 + 128.0F)).endVertex();
          bufferbuilder.pos(worldborder.minX(), 0.0D, d12).tex((f3 + f9), (f3 + 128.0F)).endVertex();
          d12++;
        } 
      } 
      d8 = Math.max(MathHelper.floor(d5 - d3), worldborder.minX());
      d9 = Math.min(MathHelper.ceil(d5 + d3), worldborder.maxX());
      if (d7 > worldborder.maxZ() - d3) {
        float f10 = 0.0F;
        for (double d13 = d8; d13 < d9; f10 += 0.5F) {
          double d16 = Math.min(1.0D, d9 - d13);
          float f13 = (float)d16 * 0.5F;
          bufferbuilder.pos(d13, 256.0D, worldborder.maxZ()).tex((f3 + f10), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(d13 + d16, 256.0D, worldborder.maxZ()).tex((f3 + f13 + f10), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(d13 + d16, 0.0D, worldborder.maxZ()).tex((f3 + f13 + f10), (f3 + 128.0F)).endVertex();
          bufferbuilder.pos(d13, 0.0D, worldborder.maxZ()).tex((f3 + f10), (f3 + 128.0F)).endVertex();
          d13++;
        } 
      } 
      if (d7 < worldborder.minZ() + d3) {
        float f11 = 0.0F;
        for (double d14 = d8; d14 < d9; f11 += 0.5F) {
          double d17 = Math.min(1.0D, d9 - d14);
          float f14 = (float)d17 * 0.5F;
          bufferbuilder.pos(d14, 256.0D, worldborder.minZ()).tex((f3 + f11), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(d14 + d17, 256.0D, worldborder.minZ()).tex((f3 + f14 + f11), (f3 + 0.0F)).endVertex();
          bufferbuilder.pos(d14 + d17, 0.0D, worldborder.minZ()).tex((f3 + f14 + f11), (f3 + 128.0F)).endVertex();
          bufferbuilder.pos(d14, 0.0D, worldborder.minZ()).tex((f3 + f11), (f3 + 128.0F)).endVertex();
          d14++;
        } 
      } 
      tessellator.draw();
      bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
      GlStateManager.enableCull();
      GlStateManager.disableAlpha();
      GlStateManager.doPolygonOffset(0.0F, 0.0F);
      GlStateManager.disablePolygonOffset();
      GlStateManager.enableAlpha();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      GlStateManager.depthMask(true);
    } 
  }
  
  private void preRenderDamagedBlocks() {
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.enableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
    GlStateManager.doPolygonOffset(-3.0F, -3.0F);
    GlStateManager.enablePolygonOffset();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableAlpha();
    GlStateManager.pushMatrix();
    if (Config.isShaders())
      ShadersRender.beginBlockDamage(); 
  }
  
  private void postRenderDamagedBlocks() {
    GlStateManager.disableAlpha();
    GlStateManager.doPolygonOffset(0.0F, 0.0F);
    GlStateManager.disablePolygonOffset();
    GlStateManager.enableAlpha();
    GlStateManager.depthMask(true);
    GlStateManager.popMatrix();
    if (Config.isShaders())
      ShadersRender.endBlockDamage(); 
  }
  
  public void drawBlockDamageTexture(Tessellator tessellatorIn, BufferBuilder worldRendererIn, Entity entityIn, float partialTicks) {
    double d3 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
    double d4 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
    double d5 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
    if (!this.damagedBlocks.isEmpty()) {
      this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      preRenderDamagedBlocks();
      worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
      worldRendererIn.setTranslation(-d3, -d4, -d5);
      worldRendererIn.noColor();
      Iterator<DestroyBlockProgress> iterator = this.damagedBlocks.values().iterator();
      while (iterator.hasNext()) {
        boolean flag1;
        DestroyBlockProgress destroyblockprogress = iterator.next();
        BlockPos blockpos = destroyblockprogress.getPosition();
        double d6 = blockpos.getX() - d3;
        double d7 = blockpos.getY() - d4;
        double d8 = blockpos.getZ() - d5;
        Block block = this.theWorld.getBlockState(blockpos).getBlock();
        if (Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
          boolean flag2 = !(!(block instanceof net.minecraft.block.BlockChest) && !(block instanceof net.minecraft.block.BlockEnderChest) && !(block instanceof net.minecraft.block.BlockSign) && !(block instanceof net.minecraft.block.BlockSkull));
          if (!flag2) {
            TileEntity tileentity = this.theWorld.getTileEntity(blockpos);
            if (tileentity != null)
              flag2 = Reflector.callBoolean(tileentity, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]); 
          } 
          flag1 = !flag2;
        } else {
          flag1 = (!(block instanceof net.minecraft.block.BlockChest) && !(block instanceof net.minecraft.block.BlockEnderChest) && !(block instanceof net.minecraft.block.BlockSign) && !(block instanceof net.minecraft.block.BlockSkull));
        } 
        if (flag1) {
          if (d6 * d6 + d7 * d7 + d8 * d8 > 1024.0D) {
            iterator.remove();
            continue;
          } 
          IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
          if (iblockstate.getMaterial() != Material.AIR) {
            int k1 = destroyblockprogress.getPartialBlockDamage();
            TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[k1];
            BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
            blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, (IBlockAccess)this.theWorld);
          } 
        } 
      } 
      tessellatorIn.draw();
      worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
      postRenderDamagedBlocks();
    } 
  }
  
  public void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks) {
    if (execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK) {
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      if (Config.isShaders())
        Shaders.disableTexture2D(); 
      GlStateManager.depthMask(false);
      BlockPos blockpos = movingObjectPositionIn.getBlockPos();
      IBlockState iblockstate = this.theWorld.getBlockState(blockpos);
      if (iblockstate.getMaterial() != Material.AIR && this.theWorld.getWorldBorder().contains(blockpos)) {
        double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        drawSelectionBoundingBox(iblockstate.getSelectedBoundingBox((World)this.theWorld, blockpos).expandXyz(0.0020000000949949026D).offset(-d3, -d4, -d5), 0.0F, 0.0F, 0.0F, 0.4F);
      } 
      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      if (Config.isShaders())
        Shaders.enableTexture2D(); 
      GlStateManager.disableBlend();
    } 
  }
  
  public static void drawSelectionBoundingBox(AxisAlignedBB box, float red, float green, float blue, float alpha) {
    drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha);
  }
  
  public static void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    drawBoundingBox(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, alpha);
    tessellator.draw();
  }
  
  public static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
    buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(minX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
    buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(maxX, minY, maxZ).color(red, green, blue, 0.0F).endVertex();
    buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
    buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
    buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
  }
  
  public static void renderFilledBox(AxisAlignedBB p_189696_0_, float p_189696_1_, float p_189696_2_, float p_189696_3_, float p_189696_4_) {
    renderFilledBox(p_189696_0_.minX, p_189696_0_.minY, p_189696_0_.minZ, p_189696_0_.maxX, p_189696_0_.maxY, p_189696_0_.maxZ, p_189696_1_, p_189696_2_, p_189696_3_, p_189696_4_);
  }
  
  public static void renderFilledBox(double p_189695_0_, double p_189695_2_, double p_189695_4_, double p_189695_6_, double p_189695_8_, double p_189695_10_, float p_189695_12_, float p_189695_13_, float p_189695_14_, float p_189695_15_) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
    addChainedFilledBoxVertices(bufferbuilder, p_189695_0_, p_189695_2_, p_189695_4_, p_189695_6_, p_189695_8_, p_189695_10_, p_189695_12_, p_189695_13_, p_189695_14_, p_189695_15_);
    tessellator.draw();
  }
  
  public static void addChainedFilledBoxVertices(BufferBuilder p_189693_0_, double p_189693_1_, double p_189693_3_, double p_189693_5_, double p_189693_7_, double p_189693_9_, double p_189693_11_, float p_189693_13_, float p_189693_14_, float p_189693_15_, float p_189693_16_) {
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_3_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_1_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_5_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
    p_189693_0_.pos(p_189693_7_, p_189693_9_, p_189693_11_).color(p_189693_13_, p_189693_14_, p_189693_15_, p_189693_16_).endVertex();
  }
  
  private void markBlocksForUpdate(int p_184385_1_, int p_184385_2_, int p_184385_3_, int p_184385_4_, int p_184385_5_, int p_184385_6_, boolean p_184385_7_) {
    this.viewFrustum.markBlocksForUpdate(p_184385_1_, p_184385_2_, p_184385_3_, p_184385_4_, p_184385_5_, p_184385_6_, p_184385_7_);
  }
  
  public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
    int k1 = pos.getX();
    int l1 = pos.getY();
    int i2 = pos.getZ();
    markBlocksForUpdate(k1 - 1, l1 - 1, i2 - 1, k1 + 1, l1 + 1, i2 + 1, ((flags & 0x8) != 0));
  }
  
  public void notifyLightSet(BlockPos pos) {
    this.setLightUpdates.add(pos.toImmutable());
  }
  
  public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1, false);
  }
  
  public void playRecord(@Nullable SoundEvent soundIn, BlockPos pos) {
    ISound isound = this.mapSoundPositions.get(pos);
    if (isound != null) {
      this.mc.getSoundHandler().stopSound(isound);
      this.mapSoundPositions.remove(pos);
    } 
    if (soundIn != null) {
      ItemRecord itemrecord = ItemRecord.getBySound(soundIn);
      if (itemrecord != null)
        this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal()); 
      PositionedSoundRecord positionedSoundRecord = PositionedSoundRecord.getRecordSoundRecord(soundIn, pos.getX(), pos.getY(), pos.getZ());
      this.mapSoundPositions.put(pos, positionedSoundRecord);
      this.mc.getSoundHandler().playSound((ISound)positionedSoundRecord);
    } 
    func_193054_a((World)this.theWorld, pos, (soundIn != null));
  }
  
  private void func_193054_a(World p_193054_1_, BlockPos p_193054_2_, boolean p_193054_3_) {
    for (EntityLivingBase entitylivingbase : p_193054_1_.getEntitiesWithinAABB(EntityLivingBase.class, (new AxisAlignedBB(p_193054_2_)).expandXyz(3.0D)))
      entitylivingbase.func_191987_a(p_193054_2_, p_193054_3_); 
  }
  
  public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {}
  
  public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    func_190570_a(particleID, ignoreRange, false, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  
  public void func_190570_a(int p_190570_1_, boolean p_190570_2_, boolean p_190570_3_, final double p_190570_4_, final double p_190570_6_, final double p_190570_8_, double p_190570_10_, double p_190570_12_, double p_190570_14_, int... p_190570_16_) {
    try {
      func_190571_b(p_190570_1_, p_190570_2_, p_190570_3_, p_190570_4_, p_190570_6_, p_190570_8_, p_190570_10_, p_190570_12_, p_190570_14_, p_190570_16_);
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
      crashreportcategory.addCrashSection("ID", Integer.valueOf(p_190570_1_));
      if (p_190570_16_ != null)
        crashreportcategory.addCrashSection("Parameters", p_190570_16_); 
      crashreportcategory.setDetail("Position", new ICrashReportDetail<String>() {
            public String call() throws Exception {
              return CrashReportCategory.getCoordinateInfo(p_190570_4_, p_190570_6_, p_190570_8_);
            }
          });
      throw new ReportedException(crashreport);
    } 
  }
  
  private void spawnParticle(EnumParticleTypes particleIn, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    spawnParticle(particleIn.getParticleID(), particleIn.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  
  @Nullable
  private Particle spawnEntityFX(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
    return func_190571_b(particleID, ignoreRange, false, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
  }
  
  @Nullable
  private Particle func_190571_b(int p_190571_1_, boolean p_190571_2_, boolean p_190571_3_, double p_190571_4_, double p_190571_6_, double p_190571_8_, double p_190571_10_, double p_190571_12_, double p_190571_14_, int... p_190571_16_) {
    Entity entity = this.mc.getRenderViewEntity();
    if (this.mc != null && entity != null && this.mc.effectRenderer != null) {
      int k1 = func_190572_a(p_190571_3_);
      double d3 = entity.posX - p_190571_4_;
      double d4 = entity.posY - p_190571_6_;
      double d5 = entity.posZ - p_190571_8_;
      if (p_190571_1_ == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.PORTAL.getParticleID() && !Config.isAnimatedPortal())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava())
        return null; 
      if (p_190571_1_ == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles())
        return null; 
      if (!p_190571_2_) {
        double d6 = 1024.0D;
        if (p_190571_1_ == EnumParticleTypes.CRIT.getParticleID())
          d6 = 38416.0D; 
        if (d3 * d3 + d4 * d4 + d5 * d5 > d6)
          return null; 
        if (k1 > 1)
          return null; 
      } 
      Particle particle = this.mc.effectRenderer.spawnEffectParticle(p_190571_1_, p_190571_4_, p_190571_6_, p_190571_8_, p_190571_10_, p_190571_12_, p_190571_14_, p_190571_16_);
      if (p_190571_1_ == EnumParticleTypes.WATER_BUBBLE.getParticleID())
        CustomColors.updateWaterFX(particle, (IBlockAccess)this.theWorld, p_190571_4_, p_190571_6_, p_190571_8_, this.renderEnv); 
      if (p_190571_1_ == EnumParticleTypes.WATER_SPLASH.getParticleID())
        CustomColors.updateWaterFX(particle, (IBlockAccess)this.theWorld, p_190571_4_, p_190571_6_, p_190571_8_, this.renderEnv); 
      if (p_190571_1_ == EnumParticleTypes.WATER_DROP.getParticleID())
        CustomColors.updateWaterFX(particle, (IBlockAccess)this.theWorld, p_190571_4_, p_190571_6_, p_190571_8_, this.renderEnv); 
      if (p_190571_1_ == EnumParticleTypes.TOWN_AURA.getParticleID())
        CustomColors.updateMyceliumFX(particle); 
      if (p_190571_1_ == EnumParticleTypes.PORTAL.getParticleID())
        CustomColors.updatePortalFX(particle); 
      if (p_190571_1_ == EnumParticleTypes.REDSTONE.getParticleID())
        CustomColors.updateReddustFX(particle, (IBlockAccess)this.theWorld, p_190571_4_, p_190571_6_, p_190571_8_); 
      return particle;
    } 
    return null;
  }
  
  private int func_190572_a(boolean p_190572_1_) {
    int k1 = this.mc.gameSettings.particleSetting;
    if (p_190572_1_ && k1 == 2 && this.theWorld.rand.nextInt(10) == 0)
      k1 = 1; 
    if (k1 == 1 && this.theWorld.rand.nextInt(3) == 0)
      k1 = 2; 
    return k1;
  }
  
  public void onEntityAdded(Entity entityIn) {
    RandomMobs.entityLoaded(entityIn, (World)this.theWorld);
    if (Config.isDynamicLights())
      DynamicLights.entityAdded(entityIn, this); 
  }
  
  public void onEntityRemoved(Entity entityIn) {
    if (Config.isDynamicLights())
      DynamicLights.entityRemoved(entityIn, this); 
  }
  
  public void deleteAllDisplayLists() {}
  
  public void broadcastSound(int soundID, BlockPos pos, int data) {
    Entity entity;
    switch (soundID) {
      case 1023:
      case 1028:
      case 1038:
        entity = this.mc.getRenderViewEntity();
        if (entity != null) {
          double d3 = pos.getX() - entity.posX;
          double d4 = pos.getY() - entity.posY;
          double d5 = pos.getZ() - entity.posZ;
          double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
          double d7 = entity.posX;
          double d8 = entity.posY;
          double d9 = entity.posZ;
          if (d6 > 0.0D) {
            d7 += d3 / d6 * 2.0D;
            d8 += d4 / d6 * 2.0D;
            d9 += d5 / d6 * 2.0D;
          } 
          if (soundID == 1023) {
            this.theWorld.playSound(d7, d8, d9, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
            break;
          } 
          if (soundID == 1038) {
            this.theWorld.playSound(d7, d8, d9, SoundEvents.field_193782_bq, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
            break;
          } 
          this.theWorld.playSound(d7, d8, d9, SoundEvents.ENTITY_ENDERDRAGON_DEATH, SoundCategory.HOSTILE, 5.0F, 1.0F, false);
        } 
        break;
    } 
  }
  
  public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
    int k1, l1;
    double d3, d4, d5;
    int k2;
    Block block;
    double d6, d7, d8;
    int i2;
    float f5, f, f1;
    EnumParticleTypes enumparticletypes;
    int l2;
    double d9, d11, d13;
    int j3;
    double d25;
    int i3, j2;
    Random random = this.theWorld.rand;
    switch (type) {
      case 1000:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        break;
      case 1001:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0F, 1.2F, false);
        break;
      case 1002:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 1.0F, 1.2F, false);
        break;
      case 1003:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 1.0F, 1.2F, false);
        break;
      case 1004:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_FIREWORK_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.2F, false);
        break;
      case 1005:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1006:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1007:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1008:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_FENCE_GATE_OPEN, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1009:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
        break;
      case 1010:
        if (Item.getItemById(data) instanceof ItemRecord) {
          this.theWorld.playRecord(blockPosIn, ((ItemRecord)Item.getItemById(data)).getSound());
          break;
        } 
        this.theWorld.playRecord(blockPosIn, null);
        break;
      case 1011:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1012:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1013:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1014:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_FENCE_GATE_CLOSE, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1015:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_GHAST_WARN, SoundCategory.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1016:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1017:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.HOSTILE, 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1018:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1019:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1020:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1021:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1022:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1024:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1025:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1026:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1027:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
        break;
      case 1029:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1030:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1031:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1032:
        this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRAVEL, random.nextFloat() * 0.4F + 0.8F));
        break;
      case 1033:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        break;
      case 1034:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        break;
      case 1035:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        break;
      case 1036:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 1037:
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 2000:
        k1 = data % 3 - 1;
        l1 = data / 3 % 3 - 1;
        d3 = blockPosIn.getX() + k1 * 0.6D + 0.5D;
        d4 = blockPosIn.getY() + 0.5D;
        d5 = blockPosIn.getZ() + l1 * 0.6D + 0.5D;
        for (k2 = 0; k2 < 10; k2++) {
          double d18 = random.nextDouble() * 0.2D + 0.01D;
          double d19 = d3 + k1 * 0.01D + (random.nextDouble() - 0.5D) * l1 * 0.5D;
          double d20 = d4 + (random.nextDouble() - 0.5D) * 0.5D;
          double d21 = d5 + l1 * 0.01D + (random.nextDouble() - 0.5D) * k1 * 0.5D;
          double d22 = k1 * d18 + random.nextGaussian() * 0.01D;
          double d23 = -0.03D + random.nextGaussian() * 0.01D;
          double d24 = l1 * d18 + random.nextGaussian() * 0.01D;
          spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d19, d20, d21, d22, d23, d24, new int[0]);
        } 
        return;
      case 2001:
        block = Block.getBlockById(data & 0xFFF);
        if (block.getDefaultState().getMaterial() != Material.AIR) {
          SoundType soundtype = block.getSoundType();
          if (Reflector.ForgeBlock_getSoundType.exists())
            soundtype = (SoundType)Reflector.call(block, Reflector.ForgeBlock_getSoundType, new Object[] { Block.getStateById(data), this.theWorld, blockPosIn, null }); 
          this.theWorld.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F, false);
        } 
        this.mc.effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(data >> 12 & 0xFF));
        break;
      case 2002:
      case 2007:
        d6 = blockPosIn.getX();
        d7 = blockPosIn.getY();
        d8 = blockPosIn.getZ();
        for (i2 = 0; i2 < 8; i2++) {
          spawnParticle(EnumParticleTypes.ITEM_CRACK, d6, d7, d8, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem((Item)Items.SPLASH_POTION) });
        } 
        f5 = (data >> 16 & 0xFF) / 255.0F;
        f = (data >> 8 & 0xFF) / 255.0F;
        f1 = (data >> 0 & 0xFF) / 255.0F;
        enumparticletypes = (type == 2007) ? EnumParticleTypes.SPELL_INSTANT : EnumParticleTypes.SPELL;
        for (l2 = 0; l2 < 100; l2++) {
          double d10 = random.nextDouble() * 4.0D;
          double d12 = random.nextDouble() * Math.PI * 2.0D;
          double d14 = Math.cos(d12) * d10;
          double d27 = 0.01D + random.nextDouble() * 0.5D;
          double d29 = Math.sin(d12) * d10;
          Particle particle1 = spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.getShouldIgnoreRange(), d6 + d14 * 0.1D, d7 + 0.3D, d8 + d29 * 0.1D, d14, d27, d29, new int[0]);
          if (particle1 != null) {
            float f4 = 0.75F + random.nextFloat() * 0.25F;
            particle1.setRBGColorF(f5 * f4, f * f4, f1 * f4);
            particle1.multiplyVelocity((float)d10);
          } 
        } 
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 2003:
        d9 = blockPosIn.getX() + 0.5D;
        d11 = blockPosIn.getY();
        d13 = blockPosIn.getZ() + 0.5D;
        for (j3 = 0; j3 < 8; j3++) {
          spawnParticle(EnumParticleTypes.ITEM_CRACK, d9, d11, d13, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] { Item.getIdFromItem(Items.ENDER_EYE) });
        } 
        for (d25 = 0.0D; d25 < 6.283185307179586D; d25 += 0.15707963267948966D) {
          spawnParticle(EnumParticleTypes.PORTAL, d9 + Math.cos(d25) * 5.0D, d11 - 0.4D, d13 + Math.sin(d25) * 5.0D, Math.cos(d25) * -5.0D, 0.0D, Math.sin(d25) * -5.0D, new int[0]);
          spawnParticle(EnumParticleTypes.PORTAL, d9 + Math.cos(d25) * 5.0D, d11 - 0.4D, d13 + Math.sin(d25) * 5.0D, Math.cos(d25) * -7.0D, 0.0D, Math.sin(d25) * -7.0D, new int[0]);
        } 
        return;
      case 2004:
        for (i3 = 0; i3 < 20; i3++) {
          double d26 = blockPosIn.getX() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
          double d28 = blockPosIn.getY() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
          double d30 = blockPosIn.getZ() + 0.5D + (this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
          this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d26, d28, d30, 0.0D, 0.0D, 0.0D, new int[0]);
          this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d26, d28, d30, 0.0D, 0.0D, 0.0D, new int[0]);
        } 
        return;
      case 2005:
        ItemDye.spawnBonemealParticles((World)this.theWorld, blockPosIn, data);
        break;
      case 2006:
        for (j2 = 0; j2 < 200; j2++) {
          float f2 = random.nextFloat() * 4.0F;
          float f3 = random.nextFloat() * 6.2831855F;
          double d15 = (MathHelper.cos(f3) * f2);
          double d16 = 0.01D + random.nextDouble() * 0.5D;
          double d17 = (MathHelper.sin(f3) * f2);
          Particle particle = spawnEntityFX(EnumParticleTypes.DRAGON_BREATH.getParticleID(), false, blockPosIn.getX() + d15 * 0.1D, blockPosIn.getY() + 0.3D, blockPosIn.getZ() + d17 * 0.1D, d15, d16, d17, new int[0]);
          if (particle != null)
            particle.multiplyVelocity(f2); 
        } 
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ENDERDRAGON_FIREBALL_EPLD, SoundCategory.HOSTILE, 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
        break;
      case 3000:
        this.theWorld.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, blockPosIn.getX() + 0.5D, blockPosIn.getY() + 0.5D, blockPosIn.getZ() + 0.5D, 0.0D, 0.0D, 0.0D, new int[0]);
        this.theWorld.playSound(blockPosIn, SoundEvents.BLOCK_END_GATEWAY_SPAWN, SoundCategory.BLOCKS, 10.0F, (1.0F + (this.theWorld.rand.nextFloat() - this.theWorld.rand.nextFloat()) * 0.2F) * 0.7F, false);
        break;
      case 3001:
        this.theWorld.playSound(blockPosIn, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.HOSTILE, 64.0F, 0.8F + this.theWorld.rand.nextFloat() * 0.3F, false);
        break;
    } 
  }
  
  public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
    if (progress >= 0 && progress < 10) {
      DestroyBlockProgress destroyblockprogress = this.damagedBlocks.get(Integer.valueOf(breakerId));
      if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ()) {
        destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
        this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
      } 
      destroyblockprogress.setPartialBlockDamage(progress);
      destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
    } else {
      this.damagedBlocks.remove(Integer.valueOf(breakerId));
    } 
  }
  
  public boolean hasNoChunkUpdates() {
    return (this.chunksToUpdate.isEmpty() && this.renderDispatcher.hasChunkUpdates());
  }
  
  public void setDisplayListEntitiesDirty() {
    this.displayListEntitiesDirty = true;
  }
  
  public void resetClouds() {
    this.cloudRenderer.reset();
  }
  
  public int getCountRenderers() {
    return this.viewFrustum.renderChunks.length;
  }
  
  public int getCountActiveRenderers() {
    return this.renderInfos.size();
  }
  
  public int getCountEntitiesRendered() {
    return this.countEntitiesRendered;
  }
  
  public int getCountTileEntitiesRendered() {
    return this.countTileEntitiesRendered;
  }
  
  public int getCountLoadedChunks() {
    if (this.theWorld == null)
      return 0; 
    ChunkProviderClient chunkProviderClient = this.theWorld.getChunkProvider();
    if (chunkProviderClient == null)
      return 0; 
    if (chunkProviderClient != this.worldChunkProvider) {
      this.worldChunkProvider = (IChunkProvider)chunkProviderClient;
      this.worldChunkProviderMap = (Long2ObjectMap<Chunk>)Reflector.getFieldValue(chunkProviderClient, Reflector.ChunkProviderClient_chunkMapping);
    } 
    return (this.worldChunkProviderMap == null) ? 0 : this.worldChunkProviderMap.size();
  }
  
  public int getCountChunksToUpdate() {
    return this.chunksToUpdate.size();
  }
  
  public RenderChunk getRenderChunk(BlockPos p_getRenderChunk_1_) {
    return this.viewFrustum.getRenderChunk(p_getRenderChunk_1_);
  }
  
  public WorldClient getWorld() {
    return this.theWorld;
  }
  
  public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd) {
    synchronized (this.setTileEntities) {
      this.setTileEntities.removeAll(tileEntitiesToRemove);
      this.setTileEntities.addAll(tileEntitiesToAdd);
    } 
  }
  
  private ContainerLocalRenderInformation allocateRenderInformation(RenderChunk p_allocateRenderInformation_1_, EnumFacing p_allocateRenderInformation_2_, int p_allocateRenderInformation_3_) {
    ContainerLocalRenderInformation renderglobal$containerlocalrenderinformation1;
    if (renderInfoCache.isEmpty()) {
      renderglobal$containerlocalrenderinformation1 = new ContainerLocalRenderInformation(p_allocateRenderInformation_1_, p_allocateRenderInformation_2_, p_allocateRenderInformation_3_);
    } else {
      renderglobal$containerlocalrenderinformation1 = renderInfoCache.pollLast();
      renderglobal$containerlocalrenderinformation1.initialize(p_allocateRenderInformation_1_, p_allocateRenderInformation_2_, p_allocateRenderInformation_3_);
    } 
    renderglobal$containerlocalrenderinformation1.cacheable = true;
    return renderglobal$containerlocalrenderinformation1;
  }
  
  private void freeRenderInformation(ContainerLocalRenderInformation p_freeRenderInformation_1_) {
    if (p_freeRenderInformation_1_.cacheable)
      renderInfoCache.add(p_freeRenderInformation_1_); 
  }
  
  public static class ContainerLocalRenderInformation {
    RenderChunk renderChunk;
    
    EnumFacing facing;
    
    int setFacing;
    
    boolean cacheable = false;
    
    public ContainerLocalRenderInformation(RenderChunk p_i1_1_, EnumFacing p_i1_2_, int p_i1_3_) {
      this.renderChunk = p_i1_1_;
      this.facing = p_i1_2_;
      this.setFacing = p_i1_3_;
    }
    
    public void setDirection(byte p_189561_1_, EnumFacing p_189561_2_) {
      this.setFacing = this.setFacing | p_189561_1_ | 1 << p_189561_2_.ordinal();
    }
    
    public boolean hasDirection(EnumFacing p_189560_1_) {
      return ((this.setFacing & 1 << p_189560_1_.ordinal()) > 0);
    }
    
    private void initialize(RenderChunk p_initialize_1_, EnumFacing p_initialize_2_, int p_initialize_3_) {
      this.renderChunk = p_initialize_1_;
      this.facing = p_initialize_2_;
      this.setFacing = p_initialize_3_;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\RenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */