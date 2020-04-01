package net.minecraft.util.text.translation;

@Deprecated
public class I18n {
  private static final LanguageMap localizedName = LanguageMap.getInstance();
  
  private static final LanguageMap fallbackTranslator = new LanguageMap();
  
  @Deprecated
  public static String translateToLocal(String key) {
    return localizedName.translateKey(key);
  }
  
  @Deprecated
  public static String translateToLocalFormatted(String key, Object... format) {
    return localizedName.translateKeyFormat(key, format);
  }
  
  @Deprecated
  public static String translateToFallback(String key) {
    return fallbackTranslator.translateKey(key);
  }
  
  @Deprecated
  public static boolean canTranslate(String key) {
    return localizedName.isKeyTranslated(key);
  }
  
  public static long getLastTranslationUpdateTimeInMilliseconds() {
    return localizedName.getLastUpdateTimeInMilliseconds();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\translation\I18n.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */