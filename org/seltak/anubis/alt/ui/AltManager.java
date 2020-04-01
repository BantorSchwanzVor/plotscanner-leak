package org.seltak.anubis.alt.ui;

import java.util.ArrayList;

public class AltManager {
  public static Alt lastAlt;
  
  public static ArrayList<Alt> registry = new ArrayList<>();
  
  static {
    registry = AltUtils.getAlts();
  }
  
  public ArrayList<Alt> getRegistry() {
    return registry;
  }
  
  public void setLastAlt(Alt alt2) {
    lastAlt = alt2;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\al\\ui\AltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */