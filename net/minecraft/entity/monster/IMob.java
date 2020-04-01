package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {
  public static final Predicate<Entity> MOB_SELECTOR = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity p_apply_1_) {
        return p_apply_1_ instanceof IMob;
      }
    };
  
  public static final Predicate<Entity> VISIBLE_MOB_SELECTOR = new Predicate<Entity>() {
      public boolean apply(@Nullable Entity p_apply_1_) {
        return (p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible());
      }
    };
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\monster\IMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */