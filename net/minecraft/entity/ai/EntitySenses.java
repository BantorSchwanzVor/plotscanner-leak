package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntitySenses {
  EntityLiving entityObj;
  
  List<Entity> seenEntities = Lists.newArrayList();
  
  List<Entity> unseenEntities = Lists.newArrayList();
  
  public EntitySenses(EntityLiving entityObjIn) {
    this.entityObj = entityObjIn;
  }
  
  public void clearSensingCache() {
    this.seenEntities.clear();
    this.unseenEntities.clear();
  }
  
  public boolean canSee(Entity entityIn) {
    if (this.seenEntities.contains(entityIn))
      return true; 
    if (this.unseenEntities.contains(entityIn))
      return false; 
    this.entityObj.world.theProfiler.startSection("canSee");
    boolean flag = this.entityObj.canEntityBeSeen(entityIn);
    this.entityObj.world.theProfiler.endSection();
    if (flag) {
      this.seenEntities.add(entityIn);
    } else {
      this.unseenEntities.add(entityIn);
    } 
    return flag;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntitySenses.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */