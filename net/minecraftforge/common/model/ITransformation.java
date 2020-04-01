package net.minecraftforge.common.model;

import javax.vecmath.Matrix4f;
import net.minecraft.util.EnumFacing;

public interface ITransformation {
  Matrix4f getMatrix();
  
  EnumFacing rotate(EnumFacing paramEnumFacing);
  
  int rotate(EnumFacing paramEnumFacing, int paramInt);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraftforge\common\model\ITransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */