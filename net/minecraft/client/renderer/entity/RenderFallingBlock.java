package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RenderFallingBlock extends Render<EntityFallingBlock> {
  public RenderFallingBlock(RenderManager renderManagerIn) {
    super(renderManagerIn);
    this.shadowSize = 0.5F;
  }
  
  public void doRender(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
    if (entity.getBlock() != null) {
      IBlockState iblockstate = entity.getBlock();
      if (iblockstate.getRenderType() == EnumBlockRenderType.MODEL) {
        World world = entity.getWorldObj();
        if (iblockstate != world.getBlockState(new BlockPos((Entity)entity)) && iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
          bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
          GlStateManager.pushMatrix();
          GlStateManager.disableLighting();
          Tessellator tessellator = Tessellator.getInstance();
          BufferBuilder bufferbuilder = tessellator.getBuffer();
          if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
          } 
          bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
          BlockPos blockpos = new BlockPos(entity.posX, (entity.getEntityBoundingBox()).maxY, entity.posZ);
          GlStateManager.translate((float)(x - blockpos.getX() - 0.5D), (float)(y - blockpos.getY()), (float)(z - blockpos.getZ() - 0.5D));
          BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
          blockrendererdispatcher.getBlockModelRenderer().renderModel((IBlockAccess)world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, MathHelper.getPositionRandom((Vec3i)entity.getOrigin()));
          tessellator.draw();
          if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
          } 
          GlStateManager.enableLighting();
          GlStateManager.popMatrix();
          super.doRender(entity, x, y, z, entityYaw, partialTicks);
        } 
      } 
    } 
  }
  
  protected ResourceLocation getEntityTexture(EntityFallingBlock entity) {
    return TextureMap.LOCATION_BLOCKS_TEXTURE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */