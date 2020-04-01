package net.minecraft.advancements.critereon;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.util.ResourceLocation;

public class AbstractCriterionInstance implements ICriterionInstance {
  private final ResourceLocation field_192245_a;
  
  public AbstractCriterionInstance(ResourceLocation p_i47465_1_) {
    this.field_192245_a = p_i47465_1_;
  }
  
  public ResourceLocation func_192244_a() {
    return this.field_192245_a;
  }
  
  public String toString() {
    return "AbstractCriterionInstance{criterion=" + this.field_192245_a + '}';
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\advancements\critereon\AbstractCriterionInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */