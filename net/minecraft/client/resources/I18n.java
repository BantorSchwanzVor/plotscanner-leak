package net.minecraft.client.resources;

import java.util.Map;

public class I18n {
  private static Locale i18nLocale;
  
  static void setLocale(Locale i18nLocaleIn) {
    i18nLocale = i18nLocaleIn;
  }
  
  public static String format(String translateKey, Object... parameters) {
    return i18nLocale.formatMessage(translateKey, parameters);
  }
  
  public static boolean hasKey(String key) {
    return i18nLocale.hasKey(key);
  }
  
  public static Map getLocaleProperties() {
    return i18nLocale.properties;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\I18n.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */