package net.minecraft.client.renderer.tileentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntitySkullRenderer extends TileEntitySpecialRenderer<TileEntitySkull> {
  private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
  
  private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
  
  private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
  
  private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
  
  private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");
  
  private final ModelDragonHead dragonHead = new ModelDragonHead(0.0F);
  
  public static TileEntitySkullRenderer instance;
  
  private final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
  
  private final ModelSkeletonHead humanoidHead = (ModelSkeletonHead)new ModelHumanoidHead();
  
  public void func_192841_a(TileEntitySkull p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
    EnumFacing enumfacing = EnumFacing.getFront(p_192841_1_.getBlockMetadata() & 0x7);
    float f = p_192841_1_.getAnimationProgress(p_192841_8_);
    renderSkull((float)p_192841_2_, (float)p_192841_4_, (float)p_192841_6_, enumfacing, (p_192841_1_.getSkullRotation() * 360) / 16.0F, p_192841_1_.getSkullType(), p_192841_1_.getPlayerProfile(), p_192841_9_, f);
  }
  
  public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn) {
    super.setRendererDispatcher(rendererDispatcherIn);
    instance = this;
  }
  
  public void renderSkull(float x, float y, float z, EnumFacing facing, float p_188190_5_, int skullType, @Nullable GameProfile profile, int destroyStage, float animateTicks) {
    ModelDragonHead modelDragonHead;
    ModelSkeletonHead modelSkeletonHead = this.skeletonHead;
    if (destroyStage >= 0) {
      bindTexture(DESTROY_STAGES[destroyStage]);
      GlStateManager.matrixMode(5890);
      GlStateManager.pushMatrix();
      GlStateManager.scale(4.0F, 2.0F, 1.0F);
      GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
      GlStateManager.matrixMode(5888);
    } else {
      ResourceLocation resourcelocation;
      switch (skullType) {
        default:
          bindTexture(SKELETON_TEXTURES);
          break;
        case 1:
          bindTexture(WITHER_SKELETON_TEXTURES);
          break;
        case 2:
          bindTexture(ZOMBIE_TEXTURES);
          modelSkeletonHead = this.humanoidHead;
          break;
        case 3:
          modelSkeletonHead = this.humanoidHead;
          resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
          if (profile != null) {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
              resourcelocation = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
              UUID uuid = EntityPlayer.getUUID(profile);
              resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
            } 
          } 
          bindTexture(resourcelocation);
          break;
        case 4:
          bindTexture(CREEPER_TEXTURES);
          break;
        case 5:
          bindTexture(DRAGON_TEXTURES);
          modelDragonHead = this.dragonHead;
          break;
      } 
    } 
    GlStateManager.pushMatrix();
    GlStateManager.disableCull();
    if (facing == EnumFacing.UP) {
      GlStateManager.translate(x + 0.5F, y, z + 0.5F);
    } else {
      switch (facing) {
        case NORTH:
          GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
          break;
        case SOUTH:
          GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
          p_188190_5_ = 180.0F;
          break;
        case WEST:
          GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
          p_188190_5_ = 270.0F;
          break;
        default:
          GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
          p_188190_5_ = 90.0F;
          break;
      } 
    } 
    float f = 0.0625F;
    GlStateManager.enableRescaleNormal();
    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
    GlStateManager.enableAlpha();
    if (skullType == 3)
      GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN); 
    modelDragonHead.render(null, animateTicks, 0.0F, 0.0F, p_188190_5_, 0.0F, 0.0625F);
    GlStateManager.popMatrix();
    if (destroyStage >= 0) {
      GlStateManager.matrixMode(5890);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\tileentity\TileEntitySkullRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */