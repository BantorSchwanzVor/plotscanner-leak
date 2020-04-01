package org.seltak.anubis.modules.movement;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class Fastbridge extends Module {
  public Fastbridge() {
    super("Fastbridge", Category.MOVEMENT, 0);
  }
  
  public void onPreUpdate() {
    if (this.mc.player != null && this.mc.world != null) {
      ItemStack i = this.mc.player.getHeldItemMainhand();
      BlockPos bP = new BlockPos(this.mc.player.posX, this.mc.player.posY - 1.0D, this.mc.player.posZ);
      if (i != null && 
        i.getItem() instanceof net.minecraft.item.ItemBlock) {
        this.mc.gameSettings.keyBindSneak.pressed = false;
        if (this.mc.world.getBlockState(bP).getBlock() == Blocks.AIR)
          this.mc.gameSettings.keyBindSneak.pressed = true; 
      } 
    } 
    super.onPreUpdate();
  }
  
  public void onDisable() {
    this.mc.gameSettings.keyBindSneak.pressed = false;
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\movement\Fastbridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */