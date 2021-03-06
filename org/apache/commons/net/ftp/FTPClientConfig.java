package org.apache.commons.net.ftp;

import java.text.DateFormatSymbols;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class FTPClientConfig {
  public static final String SYST_UNIX = "UNIX";
  
  public static final String SYST_UNIX_TRIM_LEADING = "UNIX_LTRIM";
  
  public static final String SYST_VMS = "VMS";
  
  public static final String SYST_NT = "WINDOWS";
  
  public static final String SYST_OS2 = "OS/2";
  
  public static final String SYST_OS400 = "OS/400";
  
  public static final String SYST_AS400 = "AS/400";
  
  public static final String SYST_MVS = "MVS";
  
  public static final String SYST_L8 = "TYPE: L8";
  
  public static final String SYST_NETWARE = "NETWARE";
  
  public static final String SYST_MACOS_PETER = "MACOS PETER";
  
  private final String serverSystemKey;
  
  private String defaultDateFormatStr = null;
  
  private String recentDateFormatStr = null;
  
  private boolean lenientFutureDates = true;
  
  private String serverLanguageCode = null;
  
  private String shortMonthNames = null;
  
  private String serverTimeZoneId = null;
  
  private boolean saveUnparseableEntries = false;
  
  public FTPClientConfig(String systemKey) {
    this.serverSystemKey = systemKey;
  }
  
  public FTPClientConfig() {
    this("UNIX");
  }
  
  public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr) {
    this(systemKey);
    this.defaultDateFormatStr = defaultDateFormatStr;
    this.recentDateFormatStr = recentDateFormatStr;
  }
  
  public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr, String serverLanguageCode, String shortMonthNames, String serverTimeZoneId) {
    this(systemKey);
    this.defaultDateFormatStr = defaultDateFormatStr;
    this.recentDateFormatStr = recentDateFormatStr;
    this.serverLanguageCode = serverLanguageCode;
    this.shortMonthNames = shortMonthNames;
    this.serverTimeZoneId = serverTimeZoneId;
  }
  
  public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr, String serverLanguageCode, String shortMonthNames, String serverTimeZoneId, boolean lenientFutureDates, boolean saveUnparseableEntries) {
    this(systemKey);
    this.defaultDateFormatStr = defaultDateFormatStr;
    this.lenientFutureDates = lenientFutureDates;
    this.recentDateFormatStr = recentDateFormatStr;
    this.saveUnparseableEntries = saveUnparseableEntries;
    this.serverLanguageCode = serverLanguageCode;
    this.shortMonthNames = shortMonthNames;
    this.serverTimeZoneId = serverTimeZoneId;
  }
  
  FTPClientConfig(String systemKey, FTPClientConfig config) {
    this.serverSystemKey = systemKey;
    this.defaultDateFormatStr = config.defaultDateFormatStr;
    this.lenientFutureDates = config.lenientFutureDates;
    this.recentDateFormatStr = config.recentDateFormatStr;
    this.saveUnparseableEntries = config.saveUnparseableEntries;
    this.serverLanguageCode = config.serverLanguageCode;
    this.serverTimeZoneId = config.serverTimeZoneId;
    this.shortMonthNames = config.shortMonthNames;
  }
  
  public FTPClientConfig(FTPClientConfig config) {
    this.serverSystemKey = config.serverSystemKey;
    this.defaultDateFormatStr = config.defaultDateFormatStr;
    this.lenientFutureDates = config.lenientFutureDates;
    this.recentDateFormatStr = config.recentDateFormatStr;
    this.saveUnparseableEntries = config.saveUnparseableEntries;
    this.serverLanguageCode = config.serverLanguageCode;
    this.serverTimeZoneId = config.serverTimeZoneId;
    this.shortMonthNames = config.shortMonthNames;
  }
  
  private static final Map<String, Object> LANGUAGE_CODE_MAP = new TreeMap<>();
  
  static {
    LANGUAGE_CODE_MAP.put("en", Locale.ENGLISH);
    LANGUAGE_CODE_MAP.put("de", Locale.GERMAN);
    LANGUAGE_CODE_MAP.put("it", Locale.ITALIAN);
    LANGUAGE_CODE_MAP.put("es", new Locale("es", "", ""));
    LANGUAGE_CODE_MAP.put("pt", new Locale("pt", "", ""));
    LANGUAGE_CODE_MAP.put("da", new Locale("da", "", ""));
    LANGUAGE_CODE_MAP.put("sv", new Locale("sv", "", ""));
    LANGUAGE_CODE_MAP.put("no", new Locale("no", "", ""));
    LANGUAGE_CODE_MAP.put("nl", new Locale("nl", "", ""));
    LANGUAGE_CODE_MAP.put("ro", new Locale("ro", "", ""));
    LANGUAGE_CODE_MAP.put("sq", new Locale("sq", "", ""));
    LANGUAGE_CODE_MAP.put("sh", new Locale("sh", "", ""));
    LANGUAGE_CODE_MAP.put("sk", new Locale("sk", "", ""));
    LANGUAGE_CODE_MAP.put("sl", new Locale("sl", "", ""));
    LANGUAGE_CODE_MAP.put("fr", 
        "jan|fév|mar|avr|mai|jun|jui|aoû|sep|oct|nov|déc");
  }
  
  public String getServerSystemKey() {
    return this.serverSystemKey;
  }
  
  public String getDefaultDateFormatStr() {
    return this.defaultDateFormatStr;
  }
  
  public String getRecentDateFormatStr() {
    return this.recentDateFormatStr;
  }
  
  public String getServerTimeZoneId() {
    return this.serverTimeZoneId;
  }
  
  public String getShortMonthNames() {
    return this.shortMonthNames;
  }
  
  public String getServerLanguageCode() {
    return this.serverLanguageCode;
  }
  
  public boolean isLenientFutureDates() {
    return this.lenientFutureDates;
  }
  
  public void setDefaultDateFormatStr(String defaultDateFormatStr) {
    this.defaultDateFormatStr = defaultDateFormatStr;
  }
  
  public void setRecentDateFormatStr(String recentDateFormatStr) {
    this.recentDateFormatStr = recentDateFormatStr;
  }
  
  public void setLenientFutureDates(boolean lenientFutureDates) {
    this.lenientFutureDates = lenientFutureDates;
  }
  
  public void setServerTimeZoneId(String serverTimeZoneId) {
    this.serverTimeZoneId = serverTimeZoneId;
  }
  
  public void setShortMonthNames(String shortMonthNames) {
    this.shortMonthNames = shortMonthNames;
  }
  
  public void setServerLanguageCode(String serverLanguageCode) {
    this.serverLanguageCode = serverLanguageCode;
  }
  
  public static DateFormatSymbols lookupDateFormatSymbols(String languageCode) {
    Object lang = LANGUAGE_CODE_MAP.get(languageCode);
    if (lang != null) {
      if (lang instanceof Locale)
        return new DateFormatSymbols((Locale)lang); 
      if (lang instanceof String)
        return getDateFormatSymbols((String)lang); 
    } 
    return new DateFormatSymbols(Locale.US);
  }
  
  public static DateFormatSymbols getDateFormatSymbols(String shortmonths) {
    String[] months = splitShortMonthString(shortmonths);
    DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
    dfs.setShortMonths(months);
    return dfs;
  }
  
  private static String[] splitShortMonthString(String shortmonths) {
    StringTokenizer st = new StringTokenizer(shortmonths, "|");
    int monthcnt = st.countTokens();
    if (12 != monthcnt)
      throw new IllegalArgumentException(
          "expecting a pipe-delimited string containing 12 tokens"); 
    String[] months = new String[13];
    int pos = 0;
    while (st.hasMoreTokens())
      months[pos++] = st.nextToken(); 
    months[pos] = "";
    return months;
  }
  
  public static Collection<String> getSupportedLanguageCodes() {
    return LANGUAGE_CODE_MAP.keySet();
  }
  
  public void setUnparseableEntries(boolean saveUnparseable) {
    this.saveUnparseableEntries = saveUnparseable;
  }
  
  public boolean getUnparseableEntries() {
    return this.saveUnparseableEntries;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPClientConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */