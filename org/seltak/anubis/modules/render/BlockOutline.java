package org.seltak.anubis.modules.render;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.utils.BlockUtil;

public class BlockOutline extends Module {
  public BlockOutline() {
    super("BlockOutline", Category.RENDER, 0);
  }
  
  public void onRender() {
    try {
      if (this.mc.objectMouseOver.getBlockPos() != null && 
        this.mc.world.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() != null && this.mc.world.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() != Block.getBlockById(0))
        BlockUtil.blockESPBox(this.mc.objectMouseOver.getBlockPos()); 
    } catch (Exception exception) {}
    super.onRender();
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  private int blockY() {
    BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
    if (blockpos != null)
      return blockpos.getY(); 
    return -1;
  }
  
  private int blockX() {
    BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
    if (blockpos != null)
      return blockpos.getX(); 
    return -1;
  }
  
  private int blockZ() {
    BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
    if (blockpos != null)
      return blockpos.getZ(); 
    return -1;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\render\BlockOutline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */