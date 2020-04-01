package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.ResourceLocation;

public class RenderZombieVillager extends RenderBiped<EntityZombieVillager> {
  private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
  
  private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_farmer.png");
  
  private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOC = new ResourceLocation("textures/entity/zombie_villager/zombie_librarian.png");
  
  private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");
  
  private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_smith.png");
  
  private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_butcher.png");
  
  public RenderZombieVillager(RenderManager p_i47186_1_) {
    super(p_i47186_1_, (ModelBiped)new ModelZombieVillager(), 0.5F);
    addLayer(new LayerVillagerArmor(this));
  }
  
  protected ResourceLocation getEntityTexture(EntityZombieVillager entity) {
    switch (entity.func_190736_dl()) {
      case 0:
        return ZOMBIE_VILLAGER_FARMER_LOCATION;
      case 1:
        return ZOMBIE_VILLAGER_LIBRARIAN_LOC;
      case 2:
        return ZOMBIE_VILLAGER_PRIEST_LOCATION;
      case 3:
        return ZOMBIE_VILLAGER_SMITH_LOCATION;
      case 4:
        return ZOMBIE_VILLAGER_BUTCHER_LOCATION;
    } 
    return ZOMBIE_VILLAGER_TEXTURES;
  }
  
  protected void rotateCorpse(EntityZombieVillager entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
    if (entityLiving.isConverting())
      p_77043_3_ += (float)(Math.cos(entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D); 
    super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\entity\RenderZombieVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */