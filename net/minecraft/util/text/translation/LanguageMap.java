package net.minecraft.util.text.translation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class LanguageMap {
  private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
  
  private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
  
  private static final LanguageMap instance = new LanguageMap();
  
  private final Map<String, String> languageList = Maps.newHashMap();
  
  private long lastUpdateTimeInMilliseconds;
  
  public LanguageMap() {
    try {
      InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/minecraft/lang/en_us.lang");
      for (String s : IOUtils.readLines(inputstream, StandardCharsets.UTF_8)) {
        if (!s.isEmpty() && s.charAt(0) != '#') {
          String[] astring = (String[])Iterables.toArray(EQUAL_SIGN_SPLITTER.split(s), String.class);
          if (astring != null && astring.length == 2) {
            String s1 = astring[0];
            String s2 = NUMERIC_VARIABLE_PATTERN.matcher(astring[1]).replaceAll("%$1s");
            this.languageList.put(s1, s2);
          } 
        } 
      } 
      this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    } catch (IOException iOException) {}
  }
  
  static LanguageMap getInstance() {
    return instance;
  }
  
  public static synchronized void replaceWith(Map<String, String> p_135063_0_) {
    instance.languageList.clear();
    instance.languageList.putAll(p_135063_0_);
    instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
  }
  
  public synchronized String translateKey(String key) {
    return tryTranslateKey(key);
  }
  
  public synchronized String translateKeyFormat(String key, Object... format) {
    String s = tryTranslateKey(key);
    try {
      return String.format(s, format);
    } catch (IllegalFormatException var5) {
      return "Format error: " + s;
    } 
  }
  
  private String tryTranslateKey(String key) {
    String s = this.languageList.get(key);
    return (s == null) ? key : s;
  }
  
  public synchronized boolean isKeyTranslated(String key) {
    return this.languageList.containsKey(key);
  }
  
  public long getLastUpdateTimeInMilliseconds() {
    return this.lastUpdateTimeInMilliseconds;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\text\translation\LanguageMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */