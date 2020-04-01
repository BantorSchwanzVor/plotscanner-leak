package org.seltak.anubis.modules.render;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.utils.BlockUtil;

public class ChestESP extends Module {
  public ChestESP() {
    super("ChestESP", Category.RENDER, 44);
  }
  
  public void onRender() {
    for (Object o : this.mc.world.loadedTileEntityList) {
      if (o instanceof TileEntityChest) {
        BlockUtil.blockESPBox(((TileEntityChest)o).getPos());
        continue;
      } 
      if (o instanceof TileEntityEnderChest)
        BlockUtil.blockESPBox(((TileEntityEnderChest)o).getPos()); 
    } 
    super.onRender();
  }
  
  public void onDisable() {
    super.onDisable();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\render\ChestESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */