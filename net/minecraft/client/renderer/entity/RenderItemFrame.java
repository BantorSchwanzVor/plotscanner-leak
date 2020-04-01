package net.minecraft.client.renderer.entity;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorForge;

public class RenderItemFrame extends Render<EntityItemFrame> {
  private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
  
  private final Minecraft mc = Minecraft.getMinecraft();
  
  private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
  
  private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
  
  private final RenderItem itemRenderer;
  
  public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn) {
    super(renderManagerIn);
    this.itemRenderer = itemRendererIn;
  }
  
  public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
    IBakedModel ibakedmodel;
    GlStateManager.pushMatrix();
    BlockPos blockpos = entity.getHangingPosition();
    double d0 = blockpos.getX() - entity.posX + x;
    double d1 = blockpos.getY() - entity.posY + y;
    double d2 = blockpos.getZ() - entity.posZ + z;
    GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
    GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
    this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
    ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
    if (entity.getDisplayedItem().getItem() instanceof net.minecraft.item.ItemMap) {
      ibakedmodel = modelmanager.getModel(this.mapModel);
    } else {
      ibakedmodel = modelmanager.getModel(this.itemFrameModel);
    } 
    GlStateManager.pushMatrix();
    GlStateManager.translate(-0.5F, -0.5F, -0.5F);
    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(getTeamColor(entity));
    } 
    blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    } 
    GlStateManager.popMatrix();
    GlStateManager.translate(0.0F, 0.0F, 0.4375F);
    renderItem(entity);
    GlStateManager.popMatrix();
    renderName(entity, x + (entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (entity.facingDirection.getFrontOffsetZ() * 0.3F));
  }
  
  @Nullable
  protected ResourceLocation getEntityTexture(EntityItemFrame entity) {
    return null;
  }
  
  private void renderItem(EntityItemFrame itemFrame) {
    ItemStack itemstack = itemFrame.getDisplayedItem();
    if (!itemstack.func_190926_b()) {
      if (!Config.zoomMode) {
        EntityPlayerSP entityPlayerSP = this.mc.player;
        double d0 = itemFrame.getDistanceSq(((Entity)entityPlayerSP).posX, ((Entity)entityPlayerSP).posY, ((Entity)entityPlayerSP).posZ);
        if (d0 > 4096.0D)
          return; 
      } 
      GlStateManager.pushMatrix();
      GlStateManager.disableLighting();
      boolean flag = itemstack.getItem() instanceof net.minecraft.item.ItemMap;
      int i = flag ? (itemFrame.getRotation() % 4 * 2) : itemFrame.getRotation();
      GlStateManager.rotate(i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
      if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, new Object[] { itemFrame, this }))
        if (flag) {
          this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
          GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
          float f = 0.0078125F;
          GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
          GlStateManager.translate(-64.0F, -64.0F, 0.0F);
          MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, itemstack, itemFrame.world);
          GlStateManager.translate(0.0F, 0.0F, -1.0F);
          if (mapdata != null)
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true); 
        } else {
          GlStateManager.scale(0.5F, 0.5F, 0.5F);
          GlStateManager.pushAttrib();
          RenderHelper.enableStandardItemLighting();
          this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
          RenderHelper.disableStandardItemLighting();
          GlStateManager.popAttrib();
        }  
      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
    } 
  }
  
  protected void renderName(EntityItemFrame entity, double x, double y, double z) {
    if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().func_190926_b() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
      double d0 = entity.getDistanceSqToEntity(this.renderManager.renderViewEntity);
      float f = entity.isSneaking() ? 32.0F : 64.0F;
      if (d0 < (f * f)) {
        String s = entity.getDisplayedItem().getDisplayName();
        renderLivingLabel(entity, s, x, y, z, 64);
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderItemFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */