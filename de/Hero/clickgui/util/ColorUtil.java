package de.Hero.clickgui.util;

import java.awt.Color;
import org.seltak.anubis.Anubis;

public class ColorUtil {
  public static Color getClickGUIColor() {
    return new Color((int)Anubis.setmgr.getSettingByName("GuiRed").getValDouble(), (int)Anubis.setmgr.getSettingByName("GuiGreen").getValDouble(), (int)Anubis.setmgr.getSettingByName("GuiBlue").getValDouble());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\de\Hero\clickgu\\util\ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */