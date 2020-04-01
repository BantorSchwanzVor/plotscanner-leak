package net.minecraft.client.renderer.block.model;

import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

public class BlockPartRotation {
  public final Vector3f origin;
  
  public final EnumFacing.Axis axis;
  
  public final float angle;
  
  public final boolean rescale;
  
  public BlockPartRotation(Vector3f originIn, EnumFacing.Axis axisIn, float angleIn, boolean rescaleIn) {
    this.origin = originIn;
    this.axis = axisIn;
    this.angle = angleIn;
    this.rescale = rescaleIn;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\model\BlockPartRotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */