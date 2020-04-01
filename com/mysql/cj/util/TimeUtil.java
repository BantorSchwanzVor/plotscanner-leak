package com.mysql.cj.util;

import com.mysql.cj.Messages;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.exceptions.InvalidConnectionAttributeException;
import com.mysql.cj.exceptions.WrongArgumentException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class TimeUtil {
  static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
  
  private static final String TIME_ZONE_MAPPINGS_RESOURCE = "/com/mysql/cj/util/TimeZoneMapping.properties";
  
  private static Properties timeZoneMappings = null;
  
  protected static final Method systemNanoTimeMethod;
  
  static {
    Method aMethod;
  }
  
  static {
    try {
      aMethod = System.class.getMethod("nanoTime", (Class[])null);
    } catch (SecurityException e) {
      aMethod = null;
    } catch (NoSuchMethodException e) {
      aMethod = null;
    } 
    systemNanoTimeMethod = aMethod;
  }
  
  public static boolean nanoTimeAvailable() {
    return (systemNanoTimeMethod != null);
  }
  
  public static long getCurrentTimeNanosOrMillis() {
    if (systemNanoTimeMethod != null)
      try {
        return ((Long)systemNanoTimeMethod.invoke(null, (Object[])null)).longValue();
      } catch (IllegalArgumentException illegalArgumentException) {
      
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {} 
    return System.currentTimeMillis();
  }
  
  public static String getCanonicalTimezone(String timezoneStr, ExceptionInterceptor exceptionInterceptor) {
    if (timezoneStr == null)
      return null; 
    timezoneStr = timezoneStr.trim();
    if (timezoneStr.length() > 2 && (
      timezoneStr.charAt(0) == '+' || timezoneStr.charAt(0) == '-') && Character.isDigit(timezoneStr.charAt(1)))
      return "GMT" + timezoneStr; 
    synchronized (TimeUtil.class) {
      if (timeZoneMappings == null)
        loadTimeZoneMappings(exceptionInterceptor); 
    } 
    String canonicalTz;
    if ((canonicalTz = timeZoneMappings.getProperty(timezoneStr)) != null)
      return canonicalTz; 
    throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, 
        Messages.getString("TimeUtil.UnrecognizedTimezoneId", new Object[] { timezoneStr }), exceptionInterceptor);
  }
  
  public static Timestamp adjustTimestampNanosPrecision(Timestamp ts, int fsp, boolean serverRoundFracSecs) {
    if (fsp < 0 || fsp > 6)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "fsp value must be in 0 to 6 range."); 
    Timestamp res = (Timestamp)ts.clone();
    int nanos = res.getNanos();
    double tail = Math.pow(10.0D, (9 - fsp));
    if (serverRoundFracSecs) {
      nanos = (int)Math.round(nanos / tail) * (int)tail;
      if (nanos > 999999999) {
        nanos %= 1000000000;
        res.setTime(res.getTime() + 1000L);
      } 
    } else {
      nanos = (int)(nanos / tail) * (int)tail;
    } 
    res.setNanos(nanos);
    return res;
  }
  
  public static String formatNanos(int nanos, int fsp) {
    return formatNanos(nanos, fsp, true);
  }
  
  public static String formatNanos(int nanos, int fsp, boolean truncateTrailingZeros) {
    if (nanos < 0 || nanos > 999999999)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "nanos value must be in 0 to 999999999 range but was " + nanos); 
    if (fsp < 0 || fsp > 6)
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "fsp value must be in 0 to 6 range but was " + fsp); 
    if (fsp == 0 || nanos == 0)
      return "0"; 
    nanos = (int)(nanos / Math.pow(10.0D, (9 - fsp)));
    if (nanos == 0)
      return "0"; 
    String nanosString = Integer.toString(nanos);
    String zeroPadding = "000000000";
    nanosString = "000000000".substring(0, fsp - nanosString.length()) + nanosString;
    if (truncateTrailingZeros) {
      int pos = fsp - 1;
      while (nanosString.charAt(pos) == '0')
        pos--; 
      nanosString = nanosString.substring(0, pos + 1);
    } 
    return nanosString;
  }
  
  private static void loadTimeZoneMappings(ExceptionInterceptor exceptionInterceptor) {
    timeZoneMappings = new Properties();
    try {
      timeZoneMappings.load(TimeUtil.class.getResourceAsStream("/com/mysql/cj/util/TimeZoneMapping.properties"));
    } catch (IOException e) {
      throw ExceptionFactory.createException(Messages.getString("TimeUtil.LoadTimeZoneMappingError"), exceptionInterceptor);
    } 
    for (String tz : TimeZone.getAvailableIDs()) {
      if (!timeZoneMappings.containsKey(tz))
        timeZoneMappings.put(tz, tz); 
    } 
  }
  
  public static Timestamp truncateFractionalSeconds(Timestamp timestamp) {
    Timestamp truncatedTimestamp = new Timestamp(timestamp.getTime());
    truncatedTimestamp.setNanos(0);
    return truncatedTimestamp;
  }
  
  public static SimpleDateFormat getSimpleDateFormat(SimpleDateFormat cachedSimpleDateFormat, String pattern, Calendar cal, TimeZone tz) {
    SimpleDateFormat sdf = (cachedSimpleDateFormat != null) ? cachedSimpleDateFormat : new SimpleDateFormat(pattern, Locale.US);
    if (cal != null)
      sdf.setCalendar((Calendar)cal.clone()); 
    if (tz != null)
      sdf.setTimeZone(tz); 
    return sdf;
  }
  
  public static final String getDateTimePattern(String dt, boolean toTime) throws IOException {
    int dtLength = (dt != null) ? dt.length() : 0;
    if (dtLength >= 8 && dtLength <= 10) {
      int dashCount = 0;
      boolean isDateOnly = true;
      for (int k = 0; k < dtLength; k++) {
        char c = dt.charAt(k);
        if (!Character.isDigit(c) && c != '-') {
          isDateOnly = false;
          break;
        } 
        if (c == '-')
          dashCount++; 
      } 
      if (isDateOnly && dashCount == 2)
        return "yyyy-MM-dd"; 
    } 
    boolean colonsOnly = true;
    for (int i = 0; i < dtLength; i++) {
      char c = dt.charAt(i);
      if (!Character.isDigit(c) && c != ':') {
        colonsOnly = false;
        break;
      } 
    } 
    if (colonsOnly)
      return "HH:mm:ss"; 
    StringReader reader = new StringReader(dt + " ");
    ArrayList<Object[]> vec = new ArrayList();
    ArrayList<Object[]> vecRemovelist = new ArrayList();
    Object[] nv = new Object[3];
    nv[0] = Character.valueOf('y');
    nv[1] = new StringBuilder();
    nv[2] = Integer.valueOf(0);
    vec.add(nv);
    if (toTime) {
      nv = new Object[3];
      nv[0] = Character.valueOf('h');
      nv[1] = new StringBuilder();
      nv[2] = Integer.valueOf(0);
      vec.add(nv);
    } 
    int z;
    while ((z = reader.read()) != -1) {
      char separator = (char)z;
      int maxvecs = vec.size();
      for (int count = 0; count < maxvecs; count++) {
        Object[] arrayOfObject = vec.get(count);
        int n = ((Integer)arrayOfObject[2]).intValue();
        char c = getSuccessor(((Character)arrayOfObject[0]).charValue(), n);
        if (!Character.isLetterOrDigit(separator)) {
          if (c == ((Character)arrayOfObject[0]).charValue() && c != 'S') {
            vecRemovelist.add(arrayOfObject);
          } else {
            ((StringBuilder)arrayOfObject[1]).append(separator);
            if (c == 'X' || c == 'Y')
              arrayOfObject[2] = Integer.valueOf(4); 
          } 
        } else {
          if (c == 'X') {
            c = 'y';
            nv = new Object[3];
            nv[1] = (new StringBuilder(((StringBuilder)arrayOfObject[1]).toString())).append('M');
            nv[0] = Character.valueOf('M');
            nv[2] = Integer.valueOf(1);
            vec.add(nv);
          } else if (c == 'Y') {
            c = 'M';
            nv = new Object[3];
            nv[1] = (new StringBuilder(((StringBuilder)arrayOfObject[1]).toString())).append('d');
            nv[0] = Character.valueOf('d');
            nv[2] = Integer.valueOf(1);
            vec.add(nv);
          } 
          ((StringBuilder)arrayOfObject[1]).append(c);
          if (c == ((Character)arrayOfObject[0]).charValue()) {
            arrayOfObject[2] = Integer.valueOf(n + 1);
          } else {
            arrayOfObject[0] = Character.valueOf(c);
            arrayOfObject[2] = Integer.valueOf(1);
          } 
        } 
      } 
      int k = vecRemovelist.size();
      for (int m = 0; m < k; m++) {
        Object[] arrayOfObject = vecRemovelist.get(m);
        vec.remove(arrayOfObject);
      } 
      vecRemovelist.clear();
    } 
    int size = vec.size();
    int j;
    for (j = 0; j < size; j++) {
      Object[] arrayOfObject = vec.get(j);
      char c = ((Character)arrayOfObject[0]).charValue();
      int n = ((Integer)arrayOfObject[2]).intValue();
      boolean bk = (getSuccessor(c, n) != c);
      boolean atEnd = ((c == 's' || c == 'm' || (c == 'h' && toTime)) && bk);
      boolean finishesAtDate = (bk && c == 'd' && !toTime);
      boolean containsEnd = (((StringBuilder)arrayOfObject[1]).toString().indexOf('W') != -1);
      if ((!atEnd && !finishesAtDate) || containsEnd)
        vecRemovelist.add(arrayOfObject); 
    } 
    size = vecRemovelist.size();
    for (j = 0; j < size; j++)
      vec.remove(vecRemovelist.get(j)); 
    vecRemovelist.clear();
    Object[] v = vec.get(0);
    StringBuilder format = (StringBuilder)v[1];
    format.setLength(format.length() - 1);
    return format.toString();
  }
  
  private static final char getSuccessor(char c, int n) {
    return (c == 'y' && n == 2) ? 'X' : ((c == 'y' && n < 4) ? 'y' : ((c == 'y') ? 'M' : ((c == 'M' && n == 2) ? 'Y' : ((c == 'M' && n < 3) ? 'M' : ((c == 'M') ? 'd' : ((c == 'd' && n < 2) ? 'd' : ((c == 'd') ? 'H' : ((c == 'H' && n < 2) ? 'H' : ((c == 'H') ? 'm' : ((c == 'm' && n < 2) ? 'm' : ((c == 'm') ? 's' : ((c == 's' && n < 2) ? 's' : 'W'))))))))))));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\c\\util\TimeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */