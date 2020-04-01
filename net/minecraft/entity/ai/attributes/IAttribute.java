package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public interface IAttribute {
  String getAttributeUnlocalizedName();
  
  double clampValue(double paramDouble);
  
  double getDefaultValue();
  
  boolean getShouldWatch();
  
  @Nullable
  IAttribute getParent();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\attributes\IAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */