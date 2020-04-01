package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWater extends EntityAIWander {
  protected final float field_190865_h;
  
  public EntityAIWanderAvoidWater(EntityCreature p_i47301_1_, double p_i47301_2_) {
    this(p_i47301_1_, p_i47301_2_, 0.001F);
  }
  
  public EntityAIWanderAvoidWater(EntityCreature p_i47302_1_, double p_i47302_2_, float p_i47302_4_) {
    super(p_i47302_1_, p_i47302_2_);
    this.field_190865_h = p_i47302_4_;
  }
  
  @Nullable
  protected Vec3d func_190864_f() {
    if (this.entity.isInWater()) {
      Vec3d vec3d = RandomPositionGenerator.func_191377_b(this.entity, 15, 7);
      return (vec3d == null) ? super.func_190864_f() : vec3d;
    } 
    return (this.entity.getRNG().nextFloat() >= this.field_190865_h) ? RandomPositionGenerator.func_191377_b(this.entity, 10, 7) : super.func_190864_f();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\EntityAIWanderAvoidWater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */