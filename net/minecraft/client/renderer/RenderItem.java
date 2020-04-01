package net.minecraft.client.renderer;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class RenderItem implements IResourceManagerReloadListener {
  private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
  
  private boolean notRenderingEffectsInGUI = true;
  
  public float zLevel;
  
  private final ItemModelMesher itemModelMesher;
  
  private final TextureManager textureManager;
  
  private final ItemColors itemColors;
  
  private ResourceLocation modelLocation = null;
  
  private boolean renderItemGui = false;
  
  public ModelManager modelManager = null;
  
  public RenderItem(TextureManager p_i46552_1_, ModelManager p_i46552_2_, ItemColors p_i46552_3_) {
    this.textureManager = p_i46552_1_;
    this.modelManager = p_i46552_2_;
    if (Reflector.ItemModelMesherForge_Constructor.exists()) {
      this.itemModelMesher = (ItemModelMesher)Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, new Object[] { p_i46552_2_ });
    } else {
      this.itemModelMesher = new ItemModelMesher(p_i46552_2_);
    } 
    registerItems();
    this.itemColors = p_i46552_3_;
  }
  
  public ItemModelMesher getItemModelMesher() {
    return this.itemModelMesher;
  }
  
  protected void registerItem(Item itm, int subType, String identifier) {
    this.itemModelMesher.register(itm, subType, new ModelResourceLocation(identifier, "inventory"));
  }
  
  protected void registerBlock(Block blk, int subType, String identifier) {
    registerItem(Item.getItemFromBlock(blk), subType, identifier);
  }
  
  private void registerBlock(Block blk, String identifier) {
    registerBlock(blk, 0, identifier);
  }
  
  private void registerItem(Item itm, String identifier) {
    registerItem(itm, 0, identifier);
  }
  
  private void func_191961_a(IBakedModel p_191961_1_, ItemStack p_191961_2_) {
    func_191967_a(p_191961_1_, -1, p_191961_2_);
  }
  
  public void func_191965_a(IBakedModel p_191965_1_, int p_191965_2_) {
    func_191967_a(p_191965_1_, p_191965_2_, ItemStack.field_190927_a);
  }
  
  private void func_191967_a(IBakedModel p_191967_1_, int p_191967_2_, ItemStack p_191967_3_) {
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    boolean flag = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
    boolean flag1 = (Config.isMultiTexture() && flag);
    if (flag1)
      bufferbuilder.setBlockLayer(BlockRenderLayer.SOLID); 
    bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = EnumFacing.VALUES).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      func_191970_a(bufferbuilder, p_191967_1_.getQuads(null, enumfacing, 0L), p_191967_2_, p_191967_3_);
      b++;
    } 
    func_191970_a(bufferbuilder, p_191967_1_.getQuads(null, null, 0L), p_191967_2_, p_191967_3_);
    tessellator.draw();
    if (flag1) {
      bufferbuilder.setBlockLayer(null);
      GlStateManager.bindCurrentTexture();
    } 
  }
  
  public void renderItem(ItemStack stack, IBakedModel model) {
    if (!stack.func_190926_b()) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(-0.5F, -0.5F, -0.5F);
      if (model.isBuiltInRenderer()) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        TileEntityItemStackRenderer.instance.renderByItem(stack);
      } else {
        if (Config.isCustomItems()) {
          model = CustomItems.getCustomItemModel(stack, model, this.modelLocation, false);
          this.modelLocation = null;
        } 
        func_191961_a(model, stack);
        if (stack.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, stack, model)))
          func_191966_a(model); 
      } 
      GlStateManager.popMatrix();
    } 
  }
  
  private void func_191966_a(IBakedModel p_191966_1_) {
    if (!Config.isCustomItems() || CustomItems.isUseGlint())
      if (!Config.isShaders() || !Shaders.isShadowPass) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
        this.textureManager.bindTexture(RES_ITEM_GLINT);
        if (Config.isShaders() && !this.renderItemGui)
          ShadersRender.renderEnchantedGlintBegin(); 
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        func_191965_a(p_191966_1_, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(8.0F, 8.0F, 8.0F);
        float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        func_191965_a(p_191966_1_, -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Config.isShaders() && !this.renderItemGui)
          ShadersRender.renderEnchantedGlintEnd(); 
      }  
  }
  
  private void putQuadNormal(BufferBuilder renderer, BakedQuad quad) {
    Vec3i vec3i = quad.getFace().getDirectionVec();
    renderer.putNormal(vec3i.getX(), vec3i.getY(), vec3i.getZ());
  }
  
  private void func_191969_a(BufferBuilder p_191969_1_, BakedQuad p_191969_2_, int p_191969_3_) {
    if (p_191969_1_.isMultiTexture()) {
      p_191969_1_.addVertexData(p_191969_2_.getVertexDataSingle());
      p_191969_1_.putSprite(p_191969_2_.getSprite());
    } else {
      p_191969_1_.addVertexData(p_191969_2_.getVertexData());
    } 
    if (Reflector.ForgeHooksClient_putQuadColor.exists()) {
      Reflector.call(Reflector.ForgeHooksClient_putQuadColor, new Object[] { p_191969_1_, p_191969_2_, Integer.valueOf(p_191969_3_) });
    } else {
      p_191969_1_.putColor4(p_191969_3_);
    } 
    putQuadNormal(p_191969_1_, p_191969_2_);
  }
  
  private void func_191970_a(BufferBuilder p_191970_1_, List<BakedQuad> p_191970_2_, int p_191970_3_, ItemStack p_191970_4_) {
    boolean flag = (p_191970_3_ == -1 && !p_191970_4_.func_190926_b());
    int i = 0;
    for (int j = p_191970_2_.size(); i < j; i++) {
      BakedQuad bakedquad = p_191970_2_.get(i);
      int k = p_191970_3_;
      if (flag && bakedquad.hasTintIndex()) {
        k = this.itemColors.getColorFromItemstack(p_191970_4_, bakedquad.getTintIndex());
        if (Config.isCustomColors())
          k = CustomColors.getColorFromItemStack(p_191970_4_, bakedquad.getTintIndex(), k); 
        if (EntityRenderer.anaglyphEnable)
          k = TextureUtil.anaglyphColor(k); 
        k |= 0xFF000000;
      } 
      func_191969_a(p_191970_1_, bakedquad, k);
    } 
  }
  
  public boolean shouldRenderItemIn3D(ItemStack stack) {
    IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
    return (ibakedmodel == null) ? false : ibakedmodel.isGui3d();
  }
  
  public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType cameraTransformType) {
    if (!stack.func_190926_b()) {
      IBakedModel ibakedmodel = getItemModelWithOverrides(stack, null, null);
      renderItemModel(stack, ibakedmodel, cameraTransformType, false);
    } 
  }
  
  public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {
    IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
    Item item = stack.getItem();
    if (Config.isCustomItems()) {
      if (item != null && item.hasCustomProperties())
        this.modelLocation = ibakedmodel.getOverrides().applyOverride(stack, worldIn, entitylivingbaseIn); 
      IBakedModel ibakedmodel1 = CustomItems.getCustomItemModel(stack, ibakedmodel, this.modelLocation, true);
      if (ibakedmodel1 != ibakedmodel)
        return ibakedmodel1; 
    } 
    if (Reflector.ForgeItemOverrideList_handleItemState.exists())
      return (IBakedModel)Reflector.call(ibakedmodel.getOverrides(), Reflector.ForgeItemOverrideList_handleItemState, new Object[] { ibakedmodel, stack, worldIn, entitylivingbaseIn }); 
    if (item != null && item.hasCustomProperties()) {
      ResourceLocation resourcelocation = ibakedmodel.getOverrides().applyOverride(stack, worldIn, entitylivingbaseIn);
      return (resourcelocation == null) ? ibakedmodel : this.itemModelMesher.getModelManager().getModel(new ModelResourceLocation(resourcelocation, "inventory"));
    } 
    return ibakedmodel;
  }
  
  public void renderItem(ItemStack stack, EntityLivingBase entitylivingbaseIn, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
    if (!stack.func_190926_b() && entitylivingbaseIn != null) {
      IBakedModel ibakedmodel = getItemModelWithOverrides(stack, entitylivingbaseIn.world, entitylivingbaseIn);
      renderItemModel(stack, ibakedmodel, transform, leftHanded);
    } 
  }
  
  protected void renderItemModel(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
    if (!stack.func_190926_b()) {
      this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.pushMatrix();
      if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
        bakedmodel = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[] { bakedmodel, transform, Boolean.valueOf(leftHanded) });
      } else {
        ItemCameraTransforms itemcameratransforms = bakedmodel.getItemCameraTransforms();
        ItemCameraTransforms.applyTransformSide(itemcameratransforms.getTransform(transform), leftHanded);
        if (isThereOneNegativeScale(itemcameratransforms.getTransform(transform)))
          GlStateManager.cullFace(GlStateManager.CullFace.FRONT); 
      } 
      CustomItems.setRenderOffHand(leftHanded);
      renderItem(stack, bakedmodel);
      CustomItems.setRenderOffHand(false);
      GlStateManager.cullFace(GlStateManager.CullFace.BACK);
      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    } 
  }
  
  private boolean isThereOneNegativeScale(ItemTransformVec3f itemTranformVec) {
    return ((itemTranformVec.scale.x < 0.0F)) ^ ((itemTranformVec.scale.y < 0.0F)) ^ ((itemTranformVec.scale.z < 0.0F) ? 1 : 0);
  }
  
  public void renderItemIntoGUI(ItemStack stack, int x, int y) {
    func_191962_a(stack, x, y, getItemModelWithOverrides(stack, null, null));
  }
  
  protected void func_191962_a(ItemStack p_191962_1_, int p_191962_2_, int p_191962_3_, IBakedModel p_191962_4_) {
    this.renderItemGui = true;
    GlStateManager.pushMatrix();
    this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableAlpha();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    setupGuiTransform(p_191962_2_, p_191962_3_, p_191962_4_.isGui3d());
    if (Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
      p_191962_4_ = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[] { p_191962_4_, ItemCameraTransforms.TransformType.GUI, Boolean.valueOf(false) });
    } else {
      p_191962_4_.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GUI);
    } 
    renderItem(p_191962_1_, p_191962_4_);
    GlStateManager.disableAlpha();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableLighting();
    GlStateManager.popMatrix();
    this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    this.renderItemGui = false;
  }
  
  private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
    GlStateManager.translate(xPosition, yPosition, 100.0F + this.zLevel);
    GlStateManager.translate(8.0F, 8.0F, 0.0F);
    GlStateManager.scale(1.0F, -1.0F, 1.0F);
    GlStateManager.scale(16.0F, 16.0F, 16.0F);
    if (isGui3d) {
      GlStateManager.enableLighting();
    } else {
      GlStateManager.disableLighting();
    } 
  }
  
  public void renderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition) {
    renderItemAndEffectIntoGUI((EntityLivingBase)(Minecraft.getMinecraft()).player, stack, xPosition, yPosition);
  }
  
  public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase p_184391_1_, final ItemStack p_184391_2_, int p_184391_3_, int p_184391_4_) {
    if (!p_184391_2_.func_190926_b()) {
      this.zLevel += 50.0F;
      try {
        func_191962_a(p_184391_2_, p_184391_3_, p_184391_4_, getItemModelWithOverrides(p_184391_2_, null, p_184391_1_));
      } catch (Throwable throwable) {
        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
        crashreportcategory.setDetail("Item Type", new ICrashReportDetail<String>() {
              public String call() throws Exception {
                return String.valueOf(p_184391_2_.getItem());
              }
            });
        crashreportcategory.setDetail("Item Aux", new ICrashReportDetail<String>() {
              public String call() throws Exception {
                return String.valueOf(p_184391_2_.getMetadata());
              }
            });
        crashreportcategory.setDetail("Item NBT", new ICrashReportDetail<String>() {
              public String call() throws Exception {
                return String.valueOf(p_184391_2_.getTagCompound());
              }
            });
        crashreportcategory.setDetail("Item Foil", new ICrashReportDetail<String>() {
              public String call() throws Exception {
                return String.valueOf(p_184391_2_.hasEffect());
              }
            });
        throw new ReportedException(crashreport);
      } 
      this.zLevel -= 50.0F;
    } 
  }
  
  public void renderItemOverlays(FontRenderer fr, ItemStack stack, int xPosition, int yPosition) {
    renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, null);
  }
  
  public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
    if (!stack.func_190926_b()) {
      if (stack.func_190916_E() != 1 || text != null) {
        String s = (text == null) ? String.valueOf(stack.func_190916_E()) : text;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        fr.drawStringWithShadow(s, (xPosition + 19 - 2 - fr.getStringWidth(s)), (yPosition + 6 + 3), 16777215);
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
      } 
      if (ReflectorForge.isItemDamaged(stack)) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        float f = stack.getItemDamage();
        float f1 = stack.getMaxDamage();
        float f2 = Math.max(0.0F, (f1 - f) / f1);
        int i = Math.round(13.0F - f * 13.0F / f1);
        int j = MathHelper.hsvToRGB(f2 / 3.0F, 1.0F, 1.0F);
        if (Reflector.ForgeItem_getDurabilityForDisplay.exists() && Reflector.ForgeItem_getRGBDurabilityForDisplay.exists()) {
          double d0 = Reflector.callDouble(stack.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[] { stack });
          int k = Reflector.callInt(stack.getItem(), Reflector.ForgeItem_getRGBDurabilityForDisplay, new Object[] { stack });
          i = Math.round(13.0F - (float)d0 * 13.0F);
          j = k;
        } 
        if (Config.isCustomColors())
          j = CustomColors.getDurabilityColor(f2, j); 
        if (Reflector.ForgeItem_getDurabilityForDisplay.exists() && Reflector.ForgeItem_getRGBDurabilityForDisplay.exists()) {
          double d1 = Reflector.callDouble(stack.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[] { stack });
          int l = Reflector.callInt(stack.getItem(), Reflector.ForgeItem_getRGBDurabilityForDisplay, new Object[] { stack });
          i = Math.round(13.0F - (float)d1 * 13.0F);
          j = l;
        } 
        if (Config.isCustomColors())
          j = CustomColors.getDurabilityColor(f2, j); 
        draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
        draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 0xFF, j >> 8 & 0xFF, j & 0xFF, 255);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
      } 
      EntityPlayerSP entityplayersp = (Minecraft.getMinecraft()).player;
      float f3 = (entityplayersp == null) ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
      if (f3 > 0.0F) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        Tessellator tessellator1 = Tessellator.getInstance();
        BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
        draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
      } 
    } 
  }
  
  private void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
    renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    renderer.pos((x + 0), (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
    renderer.pos((x + 0), (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
    renderer.pos((x + width), (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
    renderer.pos((x + width), (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
    Tessellator.getInstance().draw();
  }
  
  private void registerItems() {
    registerBlock(Blocks.ANVIL, "anvil_intact");
    registerBlock(Blocks.ANVIL, 1, "anvil_slightly_damaged");
    registerBlock(Blocks.ANVIL, 2, "anvil_very_damaged");
    registerBlock(Blocks.CARPET, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.RED.getMetadata(), "red_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
    registerBlock(Blocks.CARPET, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
    registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
    registerBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
    registerBlock(Blocks.DIRT, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
    registerBlock(Blocks.DIRT, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
    registerBlock(Blocks.DIRT, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
    registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
    registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
    registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
    registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
    registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
    registerBlock((Block)Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
    registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
    registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
    registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
    registerBlock((Block)Blocks.LEAVES, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
    registerBlock((Block)Blocks.LEAVES2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
    registerBlock((Block)Blocks.LEAVES2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
    registerBlock(Blocks.LOG, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
    registerBlock(Blocks.LOG, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
    registerBlock(Blocks.LOG, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
    registerBlock(Blocks.LOG, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
    registerBlock(Blocks.LOG2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
    registerBlock(Blocks.LOG2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
    registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
    registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
    registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
    registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
    registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
    registerBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
    registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
    registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
    registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
    registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
    registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
    registerBlock(Blocks.PLANKS, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
    registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
    registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
    registerBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
    registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
    registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
    registerBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
    registerBlock((Block)Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
    registerBlock((Block)Blocks.SAND, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
    registerBlock((Block)Blocks.SAND, BlockSand.EnumType.SAND.getMetadata(), "sand");
    registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
    registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
    registerBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
    registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
    registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
    registerBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
    registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
    registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
    registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
    registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
    registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
    registerBlock(Blocks.SAPLING, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
    registerBlock(Blocks.SPONGE, 0, "sponge");
    registerBlock(Blocks.SPONGE, 1, "sponge_wet");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
    registerBlock((Block)Blocks.STAINED_GLASS_PANE, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
    registerBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
    registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
    registerBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
    registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
    registerBlock(Blocks.STONE, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
    registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
    registerBlock(Blocks.STONE, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
    registerBlock(Blocks.STONE, BlockStone.EnumType.STONE.getMetadata(), "stone");
    registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
    registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
    registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
    registerBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
    registerBlock((Block)Blocks.STONE_SLAB, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
    registerBlock((Block)Blocks.STONE_SLAB2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
    registerBlock((Block)Blocks.TALLGRASS, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
    registerBlock((Block)Blocks.TALLGRASS, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
    registerBlock((Block)Blocks.TALLGRASS, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
    registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
    registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
    registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
    registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
    registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
    registerBlock((Block)Blocks.WOODEN_SLAB, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
    registerBlock(Blocks.WOOL, EnumDyeColor.BLACK.getMetadata(), "black_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.GREEN.getMetadata(), "green_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.LIME.getMetadata(), "lime_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.PINK.getMetadata(), "pink_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.RED.getMetadata(), "red_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.WHITE.getMetadata(), "white_wool");
    registerBlock(Blocks.WOOL, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
    registerBlock(Blocks.FARMLAND, "farmland");
    registerBlock(Blocks.ACACIA_STAIRS, "acacia_stairs");
    registerBlock(Blocks.ACTIVATOR_RAIL, "activator_rail");
    registerBlock((Block)Blocks.BEACON, "beacon");
    registerBlock(Blocks.BEDROCK, "bedrock");
    registerBlock(Blocks.BIRCH_STAIRS, "birch_stairs");
    registerBlock(Blocks.BOOKSHELF, "bookshelf");
    registerBlock(Blocks.BRICK_BLOCK, "brick_block");
    registerBlock(Blocks.BRICK_BLOCK, "brick_block");
    registerBlock(Blocks.BRICK_STAIRS, "brick_stairs");
    registerBlock((Block)Blocks.BROWN_MUSHROOM, "brown_mushroom");
    registerBlock((Block)Blocks.CACTUS, "cactus");
    registerBlock(Blocks.CLAY, "clay");
    registerBlock(Blocks.COAL_BLOCK, "coal_block");
    registerBlock(Blocks.COAL_ORE, "coal_ore");
    registerBlock(Blocks.COBBLESTONE, "cobblestone");
    registerBlock(Blocks.CRAFTING_TABLE, "crafting_table");
    registerBlock(Blocks.DARK_OAK_STAIRS, "dark_oak_stairs");
    registerBlock((Block)Blocks.DAYLIGHT_DETECTOR, "daylight_detector");
    registerBlock((Block)Blocks.DEADBUSH, "dead_bush");
    registerBlock(Blocks.DETECTOR_RAIL, "detector_rail");
    registerBlock(Blocks.DIAMOND_BLOCK, "diamond_block");
    registerBlock(Blocks.DIAMOND_ORE, "diamond_ore");
    registerBlock(Blocks.DISPENSER, "dispenser");
    registerBlock(Blocks.DROPPER, "dropper");
    registerBlock(Blocks.EMERALD_BLOCK, "emerald_block");
    registerBlock(Blocks.EMERALD_ORE, "emerald_ore");
    registerBlock(Blocks.ENCHANTING_TABLE, "enchanting_table");
    registerBlock(Blocks.END_PORTAL_FRAME, "end_portal_frame");
    registerBlock(Blocks.END_STONE, "end_stone");
    registerBlock(Blocks.OAK_FENCE, "oak_fence");
    registerBlock(Blocks.SPRUCE_FENCE, "spruce_fence");
    registerBlock(Blocks.BIRCH_FENCE, "birch_fence");
    registerBlock(Blocks.JUNGLE_FENCE, "jungle_fence");
    registerBlock(Blocks.DARK_OAK_FENCE, "dark_oak_fence");
    registerBlock(Blocks.ACACIA_FENCE, "acacia_fence");
    registerBlock(Blocks.OAK_FENCE_GATE, "oak_fence_gate");
    registerBlock(Blocks.SPRUCE_FENCE_GATE, "spruce_fence_gate");
    registerBlock(Blocks.BIRCH_FENCE_GATE, "birch_fence_gate");
    registerBlock(Blocks.JUNGLE_FENCE_GATE, "jungle_fence_gate");
    registerBlock(Blocks.DARK_OAK_FENCE_GATE, "dark_oak_fence_gate");
    registerBlock(Blocks.ACACIA_FENCE_GATE, "acacia_fence_gate");
    registerBlock(Blocks.FURNACE, "furnace");
    registerBlock(Blocks.GLASS, "glass");
    registerBlock(Blocks.GLASS_PANE, "glass_pane");
    registerBlock(Blocks.GLOWSTONE, "glowstone");
    registerBlock(Blocks.GOLDEN_RAIL, "golden_rail");
    registerBlock(Blocks.GOLD_BLOCK, "gold_block");
    registerBlock(Blocks.GOLD_ORE, "gold_ore");
    registerBlock((Block)Blocks.GRASS, "grass");
    registerBlock(Blocks.GRASS_PATH, "grass_path");
    registerBlock(Blocks.GRAVEL, "gravel");
    registerBlock(Blocks.HARDENED_CLAY, "hardened_clay");
    registerBlock(Blocks.HAY_BLOCK, "hay_block");
    registerBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "heavy_weighted_pressure_plate");
    registerBlock((Block)Blocks.HOPPER, "hopper");
    registerBlock(Blocks.ICE, "ice");
    registerBlock(Blocks.IRON_BARS, "iron_bars");
    registerBlock(Blocks.IRON_BLOCK, "iron_block");
    registerBlock(Blocks.IRON_ORE, "iron_ore");
    registerBlock(Blocks.IRON_TRAPDOOR, "iron_trapdoor");
    registerBlock(Blocks.JUKEBOX, "jukebox");
    registerBlock(Blocks.JUNGLE_STAIRS, "jungle_stairs");
    registerBlock(Blocks.LADDER, "ladder");
    registerBlock(Blocks.LAPIS_BLOCK, "lapis_block");
    registerBlock(Blocks.LAPIS_ORE, "lapis_ore");
    registerBlock(Blocks.LEVER, "lever");
    registerBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "light_weighted_pressure_plate");
    registerBlock(Blocks.LIT_PUMPKIN, "lit_pumpkin");
    registerBlock(Blocks.MELON_BLOCK, "melon_block");
    registerBlock(Blocks.MOSSY_COBBLESTONE, "mossy_cobblestone");
    registerBlock((Block)Blocks.MYCELIUM, "mycelium");
    registerBlock(Blocks.NETHERRACK, "netherrack");
    registerBlock(Blocks.NETHER_BRICK, "nether_brick");
    registerBlock(Blocks.NETHER_BRICK_FENCE, "nether_brick_fence");
    registerBlock(Blocks.NETHER_BRICK_STAIRS, "nether_brick_stairs");
    registerBlock(Blocks.NOTEBLOCK, "noteblock");
    registerBlock(Blocks.OAK_STAIRS, "oak_stairs");
    registerBlock(Blocks.OBSIDIAN, "obsidian");
    registerBlock(Blocks.PACKED_ICE, "packed_ice");
    registerBlock((Block)Blocks.PISTON, "piston");
    registerBlock(Blocks.PUMPKIN, "pumpkin");
    registerBlock(Blocks.QUARTZ_ORE, "quartz_ore");
    registerBlock(Blocks.QUARTZ_STAIRS, "quartz_stairs");
    registerBlock(Blocks.RAIL, "rail");
    registerBlock(Blocks.REDSTONE_BLOCK, "redstone_block");
    registerBlock(Blocks.REDSTONE_LAMP, "redstone_lamp");
    registerBlock(Blocks.REDSTONE_ORE, "redstone_ore");
    registerBlock(Blocks.REDSTONE_TORCH, "redstone_torch");
    registerBlock((Block)Blocks.RED_MUSHROOM, "red_mushroom");
    registerBlock(Blocks.SANDSTONE_STAIRS, "sandstone_stairs");
    registerBlock(Blocks.RED_SANDSTONE_STAIRS, "red_sandstone_stairs");
    registerBlock(Blocks.SEA_LANTERN, "sea_lantern");
    registerBlock(Blocks.SLIME_BLOCK, "slime");
    registerBlock(Blocks.SNOW, "snow");
    registerBlock(Blocks.SNOW_LAYER, "snow_layer");
    registerBlock(Blocks.SOUL_SAND, "soul_sand");
    registerBlock(Blocks.SPRUCE_STAIRS, "spruce_stairs");
    registerBlock((Block)Blocks.STICKY_PISTON, "sticky_piston");
    registerBlock(Blocks.STONE_BRICK_STAIRS, "stone_brick_stairs");
    registerBlock(Blocks.STONE_BUTTON, "stone_button");
    registerBlock(Blocks.STONE_PRESSURE_PLATE, "stone_pressure_plate");
    registerBlock(Blocks.STONE_STAIRS, "stone_stairs");
    registerBlock(Blocks.TNT, "tnt");
    registerBlock(Blocks.TORCH, "torch");
    registerBlock(Blocks.TRAPDOOR, "trapdoor");
    registerBlock((Block)Blocks.TRIPWIRE_HOOK, "tripwire_hook");
    registerBlock(Blocks.VINE, "vine");
    registerBlock(Blocks.WATERLILY, "waterlily");
    registerBlock(Blocks.WEB, "web");
    registerBlock(Blocks.WOODEN_BUTTON, "wooden_button");
    registerBlock(Blocks.WOODEN_PRESSURE_PLATE, "wooden_pressure_plate");
    registerBlock((Block)Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
    registerBlock(Blocks.END_ROD, "end_rod");
    registerBlock(Blocks.CHORUS_PLANT, "chorus_plant");
    registerBlock(Blocks.CHORUS_FLOWER, "chorus_flower");
    registerBlock(Blocks.PURPUR_BLOCK, "purpur_block");
    registerBlock(Blocks.PURPUR_PILLAR, "purpur_pillar");
    registerBlock(Blocks.PURPUR_STAIRS, "purpur_stairs");
    registerBlock((Block)Blocks.PURPUR_SLAB, "purpur_slab");
    registerBlock((Block)Blocks.PURPUR_DOUBLE_SLAB, "purpur_double_slab");
    registerBlock(Blocks.END_BRICKS, "end_bricks");
    registerBlock(Blocks.MAGMA, "magma");
    registerBlock(Blocks.NETHER_WART_BLOCK, "nether_wart_block");
    registerBlock(Blocks.RED_NETHER_BRICK, "red_nether_brick");
    registerBlock(Blocks.BONE_BLOCK, "bone_block");
    registerBlock(Blocks.STRUCTURE_VOID, "structure_void");
    registerBlock(Blocks.field_190976_dk, "observer");
    registerBlock(Blocks.field_190977_dl, "white_shulker_box");
    registerBlock(Blocks.field_190978_dm, "orange_shulker_box");
    registerBlock(Blocks.field_190979_dn, "magenta_shulker_box");
    registerBlock(Blocks.field_190980_do, "light_blue_shulker_box");
    registerBlock(Blocks.field_190981_dp, "yellow_shulker_box");
    registerBlock(Blocks.field_190982_dq, "lime_shulker_box");
    registerBlock(Blocks.field_190983_dr, "pink_shulker_box");
    registerBlock(Blocks.field_190984_ds, "gray_shulker_box");
    registerBlock(Blocks.field_190985_dt, "silver_shulker_box");
    registerBlock(Blocks.field_190986_du, "cyan_shulker_box");
    registerBlock(Blocks.field_190987_dv, "purple_shulker_box");
    registerBlock(Blocks.field_190988_dw, "blue_shulker_box");
    registerBlock(Blocks.field_190989_dx, "brown_shulker_box");
    registerBlock(Blocks.field_190990_dy, "green_shulker_box");
    registerBlock(Blocks.field_190991_dz, "red_shulker_box");
    registerBlock(Blocks.field_190975_dA, "black_shulker_box");
    registerBlock(Blocks.field_192427_dB, "white_glazed_terracotta");
    registerBlock(Blocks.field_192428_dC, "orange_glazed_terracotta");
    registerBlock(Blocks.field_192429_dD, "magenta_glazed_terracotta");
    registerBlock(Blocks.field_192430_dE, "light_blue_glazed_terracotta");
    registerBlock(Blocks.field_192431_dF, "yellow_glazed_terracotta");
    registerBlock(Blocks.field_192432_dG, "lime_glazed_terracotta");
    registerBlock(Blocks.field_192433_dH, "pink_glazed_terracotta");
    registerBlock(Blocks.field_192434_dI, "gray_glazed_terracotta");
    registerBlock(Blocks.field_192435_dJ, "silver_glazed_terracotta");
    registerBlock(Blocks.field_192436_dK, "cyan_glazed_terracotta");
    registerBlock(Blocks.field_192437_dL, "purple_glazed_terracotta");
    registerBlock(Blocks.field_192438_dM, "blue_glazed_terracotta");
    registerBlock(Blocks.field_192439_dN, "brown_glazed_terracotta");
    registerBlock(Blocks.field_192440_dO, "green_glazed_terracotta");
    registerBlock(Blocks.field_192441_dP, "red_glazed_terracotta");
    registerBlock(Blocks.field_192442_dQ, "black_glazed_terracotta");
    byte b;
    int i;
    EnumDyeColor[] arrayOfEnumDyeColor;
    for (i = (arrayOfEnumDyeColor = EnumDyeColor.values()).length, b = 0; b < i; ) {
      EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[b];
      registerBlock(Blocks.field_192443_dR, enumdyecolor.getMetadata(), String.valueOf(enumdyecolor.func_192396_c()) + "_concrete");
      registerBlock(Blocks.field_192444_dS, enumdyecolor.getMetadata(), String.valueOf(enumdyecolor.func_192396_c()) + "_concrete_powder");
      b++;
    } 
    registerBlock((Block)Blocks.CHEST, "chest");
    registerBlock(Blocks.TRAPPED_CHEST, "trapped_chest");
    registerBlock(Blocks.ENDER_CHEST, "ender_chest");
    registerItem(Items.IRON_SHOVEL, "iron_shovel");
    registerItem(Items.IRON_PICKAXE, "iron_pickaxe");
    registerItem(Items.IRON_AXE, "iron_axe");
    registerItem(Items.FLINT_AND_STEEL, "flint_and_steel");
    registerItem(Items.APPLE, "apple");
    registerItem((Item)Items.BOW, "bow");
    registerItem(Items.ARROW, "arrow");
    registerItem(Items.SPECTRAL_ARROW, "spectral_arrow");
    registerItem(Items.TIPPED_ARROW, "tipped_arrow");
    registerItem(Items.COAL, 0, "coal");
    registerItem(Items.COAL, 1, "charcoal");
    registerItem(Items.DIAMOND, "diamond");
    registerItem(Items.IRON_INGOT, "iron_ingot");
    registerItem(Items.GOLD_INGOT, "gold_ingot");
    registerItem(Items.IRON_SWORD, "iron_sword");
    registerItem(Items.WOODEN_SWORD, "wooden_sword");
    registerItem(Items.WOODEN_SHOVEL, "wooden_shovel");
    registerItem(Items.WOODEN_PICKAXE, "wooden_pickaxe");
    registerItem(Items.WOODEN_AXE, "wooden_axe");
    registerItem(Items.STONE_SWORD, "stone_sword");
    registerItem(Items.STONE_SHOVEL, "stone_shovel");
    registerItem(Items.STONE_PICKAXE, "stone_pickaxe");
    registerItem(Items.STONE_AXE, "stone_axe");
    registerItem(Items.DIAMOND_SWORD, "diamond_sword");
    registerItem(Items.DIAMOND_SHOVEL, "diamond_shovel");
    registerItem(Items.DIAMOND_PICKAXE, "diamond_pickaxe");
    registerItem(Items.DIAMOND_AXE, "diamond_axe");
    registerItem(Items.STICK, "stick");
    registerItem(Items.BOWL, "bowl");
    registerItem(Items.MUSHROOM_STEW, "mushroom_stew");
    registerItem(Items.GOLDEN_SWORD, "golden_sword");
    registerItem(Items.GOLDEN_SHOVEL, "golden_shovel");
    registerItem(Items.GOLDEN_PICKAXE, "golden_pickaxe");
    registerItem(Items.GOLDEN_AXE, "golden_axe");
    registerItem(Items.STRING, "string");
    registerItem(Items.FEATHER, "feather");
    registerItem(Items.GUNPOWDER, "gunpowder");
    registerItem(Items.WOODEN_HOE, "wooden_hoe");
    registerItem(Items.STONE_HOE, "stone_hoe");
    registerItem(Items.IRON_HOE, "iron_hoe");
    registerItem(Items.DIAMOND_HOE, "diamond_hoe");
    registerItem(Items.GOLDEN_HOE, "golden_hoe");
    registerItem(Items.WHEAT_SEEDS, "wheat_seeds");
    registerItem(Items.WHEAT, "wheat");
    registerItem(Items.BREAD, "bread");
    registerItem((Item)Items.LEATHER_HELMET, "leather_helmet");
    registerItem((Item)Items.LEATHER_CHESTPLATE, "leather_chestplate");
    registerItem((Item)Items.LEATHER_LEGGINGS, "leather_leggings");
    registerItem((Item)Items.LEATHER_BOOTS, "leather_boots");
    registerItem((Item)Items.CHAINMAIL_HELMET, "chainmail_helmet");
    registerItem((Item)Items.CHAINMAIL_CHESTPLATE, "chainmail_chestplate");
    registerItem((Item)Items.CHAINMAIL_LEGGINGS, "chainmail_leggings");
    registerItem((Item)Items.CHAINMAIL_BOOTS, "chainmail_boots");
    registerItem((Item)Items.IRON_HELMET, "iron_helmet");
    registerItem((Item)Items.IRON_CHESTPLATE, "iron_chestplate");
    registerItem((Item)Items.IRON_LEGGINGS, "iron_leggings");
    registerItem((Item)Items.IRON_BOOTS, "iron_boots");
    registerItem((Item)Items.DIAMOND_HELMET, "diamond_helmet");
    registerItem((Item)Items.DIAMOND_CHESTPLATE, "diamond_chestplate");
    registerItem((Item)Items.DIAMOND_LEGGINGS, "diamond_leggings");
    registerItem((Item)Items.DIAMOND_BOOTS, "diamond_boots");
    registerItem((Item)Items.GOLDEN_HELMET, "golden_helmet");
    registerItem((Item)Items.GOLDEN_CHESTPLATE, "golden_chestplate");
    registerItem((Item)Items.GOLDEN_LEGGINGS, "golden_leggings");
    registerItem((Item)Items.GOLDEN_BOOTS, "golden_boots");
    registerItem(Items.FLINT, "flint");
    registerItem(Items.PORKCHOP, "porkchop");
    registerItem(Items.COOKED_PORKCHOP, "cooked_porkchop");
    registerItem(Items.PAINTING, "painting");
    registerItem(Items.GOLDEN_APPLE, "golden_apple");
    registerItem(Items.GOLDEN_APPLE, 1, "golden_apple");
    registerItem(Items.SIGN, "sign");
    registerItem(Items.OAK_DOOR, "oak_door");
    registerItem(Items.SPRUCE_DOOR, "spruce_door");
    registerItem(Items.BIRCH_DOOR, "birch_door");
    registerItem(Items.JUNGLE_DOOR, "jungle_door");
    registerItem(Items.ACACIA_DOOR, "acacia_door");
    registerItem(Items.DARK_OAK_DOOR, "dark_oak_door");
    registerItem(Items.BUCKET, "bucket");
    registerItem(Items.WATER_BUCKET, "water_bucket");
    registerItem(Items.LAVA_BUCKET, "lava_bucket");
    registerItem(Items.MINECART, "minecart");
    registerItem(Items.SADDLE, "saddle");
    registerItem(Items.IRON_DOOR, "iron_door");
    registerItem(Items.REDSTONE, "redstone");
    registerItem(Items.SNOWBALL, "snowball");
    registerItem(Items.BOAT, "oak_boat");
    registerItem(Items.SPRUCE_BOAT, "spruce_boat");
    registerItem(Items.BIRCH_BOAT, "birch_boat");
    registerItem(Items.JUNGLE_BOAT, "jungle_boat");
    registerItem(Items.ACACIA_BOAT, "acacia_boat");
    registerItem(Items.DARK_OAK_BOAT, "dark_oak_boat");
    registerItem(Items.LEATHER, "leather");
    registerItem(Items.MILK_BUCKET, "milk_bucket");
    registerItem(Items.BRICK, "brick");
    registerItem(Items.CLAY_BALL, "clay_ball");
    registerItem(Items.REEDS, "reeds");
    registerItem(Items.PAPER, "paper");
    registerItem(Items.BOOK, "book");
    registerItem(Items.SLIME_BALL, "slime_ball");
    registerItem(Items.CHEST_MINECART, "chest_minecart");
    registerItem(Items.FURNACE_MINECART, "furnace_minecart");
    registerItem(Items.EGG, "egg");
    registerItem(Items.COMPASS, "compass");
    registerItem((Item)Items.FISHING_ROD, "fishing_rod");
    registerItem(Items.CLOCK, "clock");
    registerItem(Items.GLOWSTONE_DUST, "glowstone_dust");
    registerItem(Items.FISH, ItemFishFood.FishType.COD.getMetadata(), "cod");
    registerItem(Items.FISH, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
    registerItem(Items.FISH, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
    registerItem(Items.FISH, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
    registerItem(Items.COOKED_FISH, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
    registerItem(Items.COOKED_FISH, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
    registerItem(Items.DYE, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
    registerItem(Items.DYE, EnumDyeColor.RED.getDyeDamage(), "dye_red");
    registerItem(Items.DYE, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
    registerItem(Items.DYE, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
    registerItem(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
    registerItem(Items.DYE, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
    registerItem(Items.DYE, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
    registerItem(Items.DYE, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
    registerItem(Items.DYE, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
    registerItem(Items.DYE, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
    registerItem(Items.DYE, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
    registerItem(Items.DYE, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
    registerItem(Items.DYE, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
    registerItem(Items.DYE, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
    registerItem(Items.DYE, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
    registerItem(Items.DYE, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
    registerItem(Items.BONE, "bone");
    registerItem(Items.SUGAR, "sugar");
    registerItem(Items.CAKE, "cake");
    registerItem(Items.REPEATER, "repeater");
    registerItem(Items.COOKIE, "cookie");
    registerItem((Item)Items.SHEARS, "shears");
    registerItem(Items.MELON, "melon");
    registerItem(Items.PUMPKIN_SEEDS, "pumpkin_seeds");
    registerItem(Items.MELON_SEEDS, "melon_seeds");
    registerItem(Items.BEEF, "beef");
    registerItem(Items.COOKED_BEEF, "cooked_beef");
    registerItem(Items.CHICKEN, "chicken");
    registerItem(Items.COOKED_CHICKEN, "cooked_chicken");
    registerItem(Items.RABBIT, "rabbit");
    registerItem(Items.COOKED_RABBIT, "cooked_rabbit");
    registerItem(Items.MUTTON, "mutton");
    registerItem(Items.COOKED_MUTTON, "cooked_mutton");
    registerItem(Items.RABBIT_FOOT, "rabbit_foot");
    registerItem(Items.RABBIT_HIDE, "rabbit_hide");
    registerItem(Items.RABBIT_STEW, "rabbit_stew");
    registerItem(Items.ROTTEN_FLESH, "rotten_flesh");
    registerItem(Items.ENDER_PEARL, "ender_pearl");
    registerItem(Items.BLAZE_ROD, "blaze_rod");
    registerItem(Items.GHAST_TEAR, "ghast_tear");
    registerItem(Items.GOLD_NUGGET, "gold_nugget");
    registerItem(Items.NETHER_WART, "nether_wart");
    registerItem(Items.BEETROOT, "beetroot");
    registerItem(Items.BEETROOT_SEEDS, "beetroot_seeds");
    registerItem(Items.BEETROOT_SOUP, "beetroot_soup");
    registerItem(Items.field_190929_cY, "totem");
    registerItem((Item)Items.POTIONITEM, "bottle_drinkable");
    registerItem((Item)Items.SPLASH_POTION, "bottle_splash");
    registerItem((Item)Items.LINGERING_POTION, "bottle_lingering");
    registerItem(Items.GLASS_BOTTLE, "glass_bottle");
    registerItem(Items.DRAGON_BREATH, "dragon_breath");
    registerItem(Items.SPIDER_EYE, "spider_eye");
    registerItem(Items.FERMENTED_SPIDER_EYE, "fermented_spider_eye");
    registerItem(Items.BLAZE_POWDER, "blaze_powder");
    registerItem(Items.MAGMA_CREAM, "magma_cream");
    registerItem(Items.BREWING_STAND, "brewing_stand");
    registerItem(Items.CAULDRON, "cauldron");
    registerItem(Items.ENDER_EYE, "ender_eye");
    registerItem(Items.SPECKLED_MELON, "speckled_melon");
    this.itemModelMesher.register(Items.SPAWN_EGG, new ItemMeshDefinition() {
          public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation("spawn_egg", "inventory");
          }
        });
    registerItem(Items.EXPERIENCE_BOTTLE, "experience_bottle");
    registerItem(Items.FIRE_CHARGE, "fire_charge");
    registerItem(Items.WRITABLE_BOOK, "writable_book");
    registerItem(Items.EMERALD, "emerald");
    registerItem(Items.ITEM_FRAME, "item_frame");
    registerItem(Items.FLOWER_POT, "flower_pot");
    registerItem(Items.CARROT, "carrot");
    registerItem(Items.POTATO, "potato");
    registerItem(Items.BAKED_POTATO, "baked_potato");
    registerItem(Items.POISONOUS_POTATO, "poisonous_potato");
    registerItem((Item)Items.MAP, "map");
    registerItem(Items.GOLDEN_CARROT, "golden_carrot");
    registerItem(Items.SKULL, 0, "skull_skeleton");
    registerItem(Items.SKULL, 1, "skull_wither");
    registerItem(Items.SKULL, 2, "skull_zombie");
    registerItem(Items.SKULL, 3, "skull_char");
    registerItem(Items.SKULL, 4, "skull_creeper");
    registerItem(Items.SKULL, 5, "skull_dragon");
    registerItem(Items.CARROT_ON_A_STICK, "carrot_on_a_stick");
    registerItem(Items.NETHER_STAR, "nether_star");
    registerItem(Items.END_CRYSTAL, "end_crystal");
    registerItem(Items.PUMPKIN_PIE, "pumpkin_pie");
    registerItem(Items.FIREWORK_CHARGE, "firework_charge");
    registerItem(Items.COMPARATOR, "comparator");
    registerItem(Items.NETHERBRICK, "netherbrick");
    registerItem(Items.QUARTZ, "quartz");
    registerItem(Items.TNT_MINECART, "tnt_minecart");
    registerItem(Items.HOPPER_MINECART, "hopper_minecart");
    registerItem((Item)Items.ARMOR_STAND, "armor_stand");
    registerItem(Items.IRON_HORSE_ARMOR, "iron_horse_armor");
    registerItem(Items.GOLDEN_HORSE_ARMOR, "golden_horse_armor");
    registerItem(Items.DIAMOND_HORSE_ARMOR, "diamond_horse_armor");
    registerItem(Items.LEAD, "lead");
    registerItem(Items.NAME_TAG, "name_tag");
    this.itemModelMesher.register(Items.BANNER, new ItemMeshDefinition() {
          public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation("banner", "inventory");
          }
        });
    this.itemModelMesher.register(Items.BED, new ItemMeshDefinition() {
          public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation("bed", "inventory");
          }
        });
    this.itemModelMesher.register(Items.SHIELD, new ItemMeshDefinition() {
          public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation("shield", "inventory");
          }
        });
    registerItem(Items.ELYTRA, "elytra");
    registerItem(Items.CHORUS_FRUIT, "chorus_fruit");
    registerItem(Items.CHORUS_FRUIT_POPPED, "chorus_fruit_popped");
    registerItem(Items.field_190930_cZ, "shulker_shell");
    registerItem(Items.field_191525_da, "iron_nugget");
    registerItem(Items.RECORD_13, "record_13");
    registerItem(Items.RECORD_CAT, "record_cat");
    registerItem(Items.RECORD_BLOCKS, "record_blocks");
    registerItem(Items.RECORD_CHIRP, "record_chirp");
    registerItem(Items.RECORD_FAR, "record_far");
    registerItem(Items.RECORD_MALL, "record_mall");
    registerItem(Items.RECORD_MELLOHI, "record_mellohi");
    registerItem(Items.RECORD_STAL, "record_stal");
    registerItem(Items.RECORD_STRAD, "record_strad");
    registerItem(Items.RECORD_WARD, "record_ward");
    registerItem(Items.RECORD_11, "record_11");
    registerItem(Items.RECORD_WAIT, "record_wait");
    registerItem(Items.PRISMARINE_SHARD, "prismarine_shard");
    registerItem(Items.PRISMARINE_CRYSTALS, "prismarine_crystals");
    registerItem(Items.field_192397_db, "knowledge_book");
    this.itemModelMesher.register(Items.ENCHANTED_BOOK, new ItemMeshDefinition() {
          public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation("enchanted_book", "inventory");
          }
        });
    this.itemModelMesher.register((Item)Items.FILLED_MAP, new ItemMeshDefinition() {
          public ModelResourceLocation getModelLocation(ItemStack stack) {
            return new ModelResourceLocation("filled_map", "inventory");
          }
        });
    registerBlock(Blocks.COMMAND_BLOCK, "command_block");
    registerItem(Items.FIREWORKS, "fireworks");
    registerItem(Items.COMMAND_BLOCK_MINECART, "command_block_minecart");
    registerBlock(Blocks.BARRIER, "barrier");
    registerBlock(Blocks.MOB_SPAWNER, "mob_spawner");
    registerItem(Items.WRITTEN_BOOK, "written_book");
    registerBlock(Blocks.BROWN_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
    registerBlock(Blocks.RED_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
    registerBlock(Blocks.DRAGON_EGG, "dragon_egg");
    registerBlock(Blocks.REPEATING_COMMAND_BLOCK, "repeating_command_block");
    registerBlock(Blocks.CHAIN_COMMAND_BLOCK, "chain_command_block");
    registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.SAVE.getModeId(), "structure_block");
    registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.LOAD.getModeId(), "structure_block");
    registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.CORNER.getModeId(), "structure_block");
    registerBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.DATA.getModeId(), "structure_block");
    if (Reflector.ModelLoader_onRegisterItems.exists())
      Reflector.call(Reflector.ModelLoader_onRegisterItems, new Object[] { this.itemModelMesher }); 
  }
  
  public void onResourceManagerReload(IResourceManager resourceManager) {
    this.itemModelMesher.rebuildCache();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\RenderItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */