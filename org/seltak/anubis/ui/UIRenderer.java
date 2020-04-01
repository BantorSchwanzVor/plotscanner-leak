package org.seltak.anubis.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.seltak.anubis.modules.gui.ArrayListModule;

public class UIRenderer extends Gui {
  protected Minecraft mc = Minecraft.getMinecraft();
  
  public void draw() {
    ArrayListModule al = new ArrayListModule();
    al.onRender();
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    GlStateManager.scale(0.5D, 0.5D, 0.5D);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubi\\ui\UIRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */