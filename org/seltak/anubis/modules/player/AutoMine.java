package org.seltak.anubis.modules.player;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class AutoMine extends Module {
  public AutoMine() {
    super("AutoMine", Category.PLAYER, 0);
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
      BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
      if (this.mc.world.getBlockState(blockpos).getMaterial() != Material.AIR && this.mc.playerController.onPlayerDamageBlock(blockpos, this.mc.objectMouseOver.sideHit)) {
        this.mc.effectRenderer.addBlockHitEffects(blockpos, this.mc.objectMouseOver.sideHit);
        this.mc.player.swingArm(EnumHand.MAIN_HAND);
      } 
    } else {
      this.mc.playerController.resetBlockRemoving();
    } 
  }
  
  public void onDisable() {
    super.onDisable();
    this.mc.gameSettings.keyBindJump.setPressed(false);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\player\AutoMine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */