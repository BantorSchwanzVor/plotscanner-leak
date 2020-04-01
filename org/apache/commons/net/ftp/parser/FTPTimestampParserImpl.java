package org.apache.commons.net.ftp.parser;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClientConfig;

public class FTPTimestampParserImpl implements FTPTimestampParser, Configurable {
  private SimpleDateFormat defaultDateFormat;
  
  private int defaultDateSmallestUnitIndex;
  
  private SimpleDateFormat recentDateFormat;
  
  private int recentDateSmallestUnitIndex;
  
  private boolean lenientFutureDates = false;
  
  private static final int[] CALENDAR_UNITS = new int[] { 14, 
      13, 
      12, 
      11, 
      5, 
      2, 
      1 };
  
  private static int getEntry(SimpleDateFormat dateFormat) {
    if (dateFormat == null)
      return 0; 
    String FORMAT_CHARS = "SsmHdM";
    String pattern = dateFormat.toPattern();
    byte b;
    int i;
    char[] arrayOfChar;
    for (i = (arrayOfChar = "SsmHdM".toCharArray()).length, b = 0; b < i; ) {
      char ch = arrayOfChar[b];
      if (pattern.indexOf(ch) != -1)
        switch (ch) {
          case 'S':
            return indexOf(14);
          case 's':
            return indexOf(13);
          case 'm':
            return indexOf(12);
          case 'H':
            return indexOf(11);
          case 'd':
            return indexOf(5);
          case 'M':
            return indexOf(2);
        }  
      b++;
    } 
    return 0;
  }
  
  private static int indexOf(int calendarUnit) {
    for (int i = 0; i < CALENDAR_UNITS.length; i++) {
      if (calendarUnit == CALENDAR_UNITS[i])
        return i; 
    } 
    return 0;
  }
  
  private static void setPrecision(int index, Calendar working) {
    if (index <= 0)
      return; 
    int field = CALENDAR_UNITS[index - 1];
    int value = working.get(field);
    if (value == 0)
      working.clear(field); 
  }
  
  public FTPTimestampParserImpl() {
    setDefaultDateFormat("MMM d yyyy", null);
    setRecentDateFormat("MMM d HH:mm", null);
  }
  
  public Calendar parseTimestamp(String timestampStr) throws ParseException {
    Calendar now = Calendar.getInstance();
    return parseTimestamp(timestampStr, now);
  }
  
  public Calendar parseTimestamp(String timestampStr, Calendar serverTime) throws ParseException {
    Calendar working = (Calendar)serverTime.clone();
    working.setTimeZone(getServerTimeZone());
    Date parsed = null;
    if (this.recentDateFormat != null) {
      Calendar now = (Calendar)serverTime.clone();
      now.setTimeZone(getServerTimeZone());
      if (this.lenientFutureDates)
        now.add(5, 1); 
      String year = Integer.toString(now.get(1));
      String timeStampStrPlusYear = String.valueOf(timestampStr) + " " + year;
      SimpleDateFormat hackFormatter = new SimpleDateFormat(String.valueOf(this.recentDateFormat.toPattern()) + " yyyy", 
          this.recentDateFormat.getDateFormatSymbols());
      hackFormatter.setLenient(false);
      hackFormatter.setTimeZone(this.recentDateFormat.getTimeZone());
      ParsePosition parsePosition = new ParsePosition(0);
      parsed = hackFormatter.parse(timeStampStrPlusYear, parsePosition);
      if (parsed != null && parsePosition.getIndex() == timeStampStrPlusYear.length()) {
        working.setTime(parsed);
        if (working.after(now))
          working.add(1, -1); 
        setPrecision(this.recentDateSmallestUnitIndex, working);
        return working;
      } 
    } 
    ParsePosition pp = new ParsePosition(0);
    parsed = this.defaultDateFormat.parse(timestampStr, pp);
    if (parsed != null && pp.getIndex() == timestampStr.length()) {
      working.setTime(parsed);
    } else {
      throw new ParseException(
          "Timestamp '" + timestampStr + "' could not be parsed using a server time of " + 
          serverTime.getTime().toString(), 
          pp.getErrorIndex());
    } 
    setPrecision(this.defaultDateSmallestUnitIndex, working);
    return working;
  }
  
  public SimpleDateFormat getDefaultDateFormat() {
    return this.defaultDateFormat;
  }
  
  public String getDefaultDateFormatString() {
    return this.defaultDateFormat.toPattern();
  }
  
  private void setDefaultDateFormat(String format, DateFormatSymbols dfs) {
    if (format != null) {
      if (dfs != null) {
        this.defaultDateFormat = new SimpleDateFormat(format, dfs);
      } else {
        this.defaultDateFormat = new SimpleDateFormat(format);
      } 
      this.defaultDateFormat.setLenient(false);
    } else {
      this.defaultDateFormat = null;
    } 
    this.defaultDateSmallestUnitIndex = getEntry(this.defaultDateFormat);
  }
  
  public SimpleDateFormat getRecentDateFormat() {
    return this.recentDateFormat;
  }
  
  public String getRecentDateFormatString() {
    return this.recentDateFormat.toPattern();
  }
  
  private void setRecentDateFormat(String format, DateFormatSymbols dfs) {
    if (format != null) {
      if (dfs != null) {
        this.recentDateFormat = new SimpleDateFormat(format, dfs);
      } else {
        this.recentDateFormat = new SimpleDateFormat(format);
      } 
      this.recentDateFormat.setLenient(false);
    } else {
      this.recentDateFormat = null;
    } 
    this.recentDateSmallestUnitIndex = getEntry(this.recentDateFormat);
  }
  
  public String[] getShortMonths() {
    return this.defaultDateFormat.getDateFormatSymbols().getShortMonths();
  }
  
  public TimeZone getServerTimeZone() {
    return this.defaultDateFormat.getTimeZone();
  }
  
  private void setServerTimeZone(String serverTimeZoneId) {
    TimeZone serverTimeZone = TimeZone.getDefault();
    if (serverTimeZoneId != null)
      serverTimeZone = TimeZone.getTimeZone(serverTimeZoneId); 
    this.defaultDateFormat.setTimeZone(serverTimeZone);
    if (this.recentDateFormat != null)
      this.recentDateFormat.setTimeZone(serverTimeZone); 
  }
  
  public void configure(FTPClientConfig config) {
    DateFormatSymbols dfs = null;
    String languageCode = config.getServerLanguageCode();
    String shortmonths = config.getShortMonthNames();
    if (shortmonths != null) {
      dfs = FTPClientConfig.getDateFormatSymbols(shortmonths);
    } else if (languageCode != null) {
      dfs = FTPClientConfig.lookupDateFormatSymbols(languageCode);
    } else {
      dfs = FTPClientConfig.lookupDateFormatSymbols("en");
    } 
    String recentFormatString = config.getRecentDateFormatStr();
    setRecentDateFormat(recentFormatString, dfs);
    String defaultFormatString = config.getDefaultDateFormatStr();
    if (defaultFormatString == null)
      throw new IllegalArgumentException("defaultFormatString cannot be null"); 
    setDefaultDateFormat(defaultFormatString, dfs);
    setServerTimeZone(config.getServerTimeZoneId());
    this.lenientFutureDates = config.isLenientFutureDates();
  }
  
  boolean isLenientFutureDates() {
    return this.lenientFutureDates;
  }
  
  void setLenientFutureDates(boolean lenientFutureDates) {
    this.lenientFutureDates = lenientFutureDates;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\FTPTimestampParserImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */