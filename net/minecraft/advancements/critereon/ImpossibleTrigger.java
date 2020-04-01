package net.minecraft.advancements.critereon;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.ResourceLocation;

public class ImpossibleTrigger implements ICriterionTrigger<ImpossibleTrigger.Instance> {
  private static final ResourceLocation field_192205_a = new ResourceLocation("impossible");
  
  public ResourceLocation func_192163_a() {
    return field_192205_a;
  }
  
  public void func_192165_a(PlayerAdvancements p_192165_1_, ICriterionTrigger.Listener<Instance> p_192165_2_) {}
  
  public void func_192164_b(PlayerAdvancements p_192164_1_, ICriterionTrigger.Listener<Instance> p_192164_2_) {}
  
  public void func_192167_a(PlayerAdvancements p_192167_1_) {}
  
  public Instance func_192166_a(JsonObject p_192166_1_, JsonDeserializationContext p_192166_2_) {
    return new Instance();
  }
  
  public static class Instance extends AbstractCriterionInstance {
    public Instance() {
      super(ImpossibleTrigger.field_192205_a);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\advancements\critereon\ImpossibleTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */