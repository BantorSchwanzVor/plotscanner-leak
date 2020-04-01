package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityCaveSpider extends EntitySpider {
  public EntityCaveSpider(World worldIn) {
    super(worldIn);
    setSize(0.7F, 0.5F);
  }
  
  public static void registerFixesCaveSpider(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityCaveSpider.class);
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
  }
  
  public boolean attackEntityAsMob(Entity entityIn) {
    if (super.attackEntityAsMob(entityIn)) {
      if (entityIn instanceof EntityLivingBase) {
        int i = 0;
        if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
          i = 7;
        } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
          i = 15;
        } 
        if (i > 0)
          ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, i * 20, 0)); 
      } 
      return true;
    } 
    return false;
  }
  
  @Nullable
  public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
    return livingdata;
  }
  
  public float getEyeHeight() {
    return 0.45F;
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_CAVE_SPIDER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\EntityCaveSpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */