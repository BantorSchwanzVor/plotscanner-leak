package com.mysql.cj.util;

import com.mysql.cj.Messages;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.WrongArgumentException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {
  public enum SearchMode {
    ALLOW_BACKSLASH_ESCAPE, SKIP_BETWEEN_MARKERS, SKIP_BLOCK_COMMENTS, SKIP_LINE_COMMENTS, SKIP_WHITE_SPACE;
  }
  
  public static final Set<SearchMode> SEARCH_MODE__ALL = Collections.unmodifiableSet(EnumSet.allOf(SearchMode.class));
  
  public static final Set<SearchMode> SEARCH_MODE__MRK_COM_WS = Collections.unmodifiableSet(
      EnumSet.of(SearchMode.SKIP_BETWEEN_MARKERS, SearchMode.SKIP_BLOCK_COMMENTS, SearchMode.SKIP_LINE_COMMENTS, SearchMode.SKIP_WHITE_SPACE));
  
  public static final Set<SearchMode> SEARCH_MODE__BSESC_COM_WS = Collections.unmodifiableSet(
      EnumSet.of(SearchMode.ALLOW_BACKSLASH_ESCAPE, SearchMode.SKIP_BLOCK_COMMENTS, SearchMode.SKIP_LINE_COMMENTS, SearchMode.SKIP_WHITE_SPACE));
  
  public static final Set<SearchMode> SEARCH_MODE__BSESC_MRK_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.ALLOW_BACKSLASH_ESCAPE, SearchMode.SKIP_BETWEEN_MARKERS, SearchMode.SKIP_WHITE_SPACE));
  
  public static final Set<SearchMode> SEARCH_MODE__COM_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.SKIP_BLOCK_COMMENTS, SearchMode.SKIP_LINE_COMMENTS, SearchMode.SKIP_WHITE_SPACE));
  
  public static final Set<SearchMode> SEARCH_MODE__MRK_WS = Collections.unmodifiableSet(EnumSet.of(SearchMode.SKIP_BETWEEN_MARKERS, SearchMode.SKIP_WHITE_SPACE));
  
  public static final Set<SearchMode> SEARCH_MODE__NONE = Collections.unmodifiableSet(EnumSet.noneOf(SearchMode.class));
  
  private static final int NON_COMMENTS_MYSQL_VERSION_REF_LENGTH = 5;
  
  private static final int WILD_COMPARE_MATCH = 0;
  
  private static final int WILD_COMPARE_CONTINUE_WITH_WILD = 1;
  
  private static final int WILD_COMPARE_NO_MATCH = -1;
  
  static final char WILDCARD_MANY = '%';
  
  static final char WILDCARD_ONE = '_';
  
  static final char WILDCARD_ESCAPE = '\\';
  
  private static final String VALID_ID_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@";
  
  public static String dumpAsHex(byte[] byteBuffer, int length) {
    length = Math.min(length, byteBuffer.length);
    StringBuilder fullOutBuilder = new StringBuilder(length * 4);
    StringBuilder asciiOutBuilder = new StringBuilder(16);
    int l;
    for (int p = 0; p < length; l = 0) {
      for (; l < 8 && p < length; p++, l++) {
        int asInt = byteBuffer[p] & 0xFF;
        if (asInt < 16)
          fullOutBuilder.append("0"); 
        fullOutBuilder.append(Integer.toHexString(asInt)).append(" ");
        asciiOutBuilder.append(" ").append((asInt >= 32 && asInt < 127) ? Character.valueOf((char)asInt) : ".");
      } 
      for (; l < 8; l++)
        fullOutBuilder.append("   "); 
      fullOutBuilder.append("   ").append(asciiOutBuilder).append(System.lineSeparator());
      asciiOutBuilder.setLength(0);
    } 
    return fullOutBuilder.toString();
  }
  
  public static String toHexString(byte[] byteBuffer, int length) {
    length = Math.min(length, byteBuffer.length);
    StringBuilder outputBuilder = new StringBuilder(length * 2);
    for (int i = 0; i < length; i++) {
      int asInt = byteBuffer[i] & 0xFF;
      if (asInt < 16)
        outputBuilder.append("0"); 
      outputBuilder.append(Integer.toHexString(asInt));
    } 
    return outputBuilder.toString();
  }
  
  private static boolean endsWith(byte[] dataFrom, String suffix) {
    for (int i = 1; i <= suffix.length(); i++) {
      int dfOffset = dataFrom.length - i;
      int suffixOffset = suffix.length() - i;
      if (dataFrom[dfOffset] != suffix.charAt(suffixOffset))
        return false; 
    } 
    return true;
  }
  
  public static char firstNonWsCharUc(String searchIn) {
    return firstNonWsCharUc(searchIn, 0);
  }
  
  public static char firstNonWsCharUc(String searchIn, int startAt) {
    if (searchIn == null)
      return Character.MIN_VALUE; 
    int length = searchIn.length();
    for (int i = startAt; i < length; i++) {
      char c = searchIn.charAt(i);
      if (!Character.isWhitespace(c))
        return Character.toUpperCase(c); 
    } 
    return Character.MIN_VALUE;
  }
  
  public static char firstAlphaCharUc(String searchIn, int startAt) {
    if (searchIn == null)
      return Character.MIN_VALUE; 
    int length = searchIn.length();
    for (int i = startAt; i < length; i++) {
      char c = searchIn.charAt(i);
      if (Character.isLetter(c))
        return Character.toUpperCase(c); 
    } 
    return Character.MIN_VALUE;
  }
  
  public static String fixDecimalExponent(String dString) {
    int ePos = dString.indexOf('E');
    if (ePos == -1)
      ePos = dString.indexOf('e'); 
    if (ePos != -1 && 
      dString.length() > ePos + 1) {
      char maybeMinusChar = dString.charAt(ePos + 1);
      if (maybeMinusChar != '-' && maybeMinusChar != '+') {
        StringBuilder strBuilder = new StringBuilder(dString.length() + 1);
        strBuilder.append(dString.substring(0, ePos + 1));
        strBuilder.append('+');
        strBuilder.append(dString.substring(ePos + 1, dString.length()));
        dString = strBuilder.toString();
      } 
    } 
    return dString;
  }
  
  public static byte[] getBytes(String s, String encoding) {
    if (encoding == null)
      return getBytes(s); 
    try {
      return s.getBytes(encoding);
    } catch (UnsupportedEncodingException uee) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("StringUtils.0", new Object[] { encoding }), uee);
    } 
  }
  
  public static byte[] getBytesWrapped(String s, char beginWrap, char endWrap, String encoding) {
    byte[] b;
    if (encoding == null) {
      StringBuilder strBuilder = new StringBuilder(s.length() + 2);
      strBuilder.append(beginWrap);
      strBuilder.append(s);
      strBuilder.append(endWrap);
      b = getBytes(strBuilder.toString());
    } else {
      StringBuilder strBuilder = new StringBuilder(s.length() + 2);
      strBuilder.append(beginWrap);
      strBuilder.append(s);
      strBuilder.append(endWrap);
      s = strBuilder.toString();
      b = getBytes(s, encoding);
    } 
    return b;
  }
  
  public static int indexOfIgnoreCase(String searchIn, String searchFor) {
    return indexOfIgnoreCase(0, searchIn, searchFor);
  }
  
  public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor) {
    if (searchIn == null || searchFor == null)
      return -1; 
    int searchInLength = searchIn.length();
    int searchForLength = searchFor.length();
    int stopSearchingAt = searchInLength - searchForLength;
    if (startingPosition > stopSearchingAt || searchForLength == 0)
      return -1; 
    char firstCharOfSearchForUc = Character.toUpperCase(searchFor.charAt(0));
    char firstCharOfSearchForLc = Character.toLowerCase(searchFor.charAt(0));
    for (int i = startingPosition; i <= stopSearchingAt; i++) {
      if (isCharAtPosNotEqualIgnoreCase(searchIn, i, firstCharOfSearchForUc, firstCharOfSearchForLc))
        while (++i <= stopSearchingAt && isCharAtPosNotEqualIgnoreCase(searchIn, i, firstCharOfSearchForUc, firstCharOfSearchForLc)); 
      if (i <= stopSearchingAt && startsWithIgnoreCase(searchIn, i, searchFor))
        return i; 
    } 
    return -1;
  }
  
  public static int indexOfIgnoreCase(int startingPosition, String searchIn, String[] searchForSequence, String openingMarkers, String closingMarkers, Set<SearchMode> searchMode) {
    if (searchIn == null || searchForSequence == null)
      return -1; 
    int searchInLength = searchIn.length();
    int searchForLength = 0;
    for (String searchForPart : searchForSequence)
      searchForLength += searchForPart.length(); 
    if (searchForLength == 0)
      return -1; 
    int searchForWordsCount = searchForSequence.length;
    searchForLength += (searchForWordsCount > 0) ? (searchForWordsCount - 1) : 0;
    int stopSearchingAt = searchInLength - searchForLength;
    if (startingPosition > stopSearchingAt)
      return -1; 
    if (searchMode.contains(SearchMode.SKIP_BETWEEN_MARKERS) && (openingMarkers == null || closingMarkers == null || openingMarkers
      .length() != closingMarkers.length()))
      throw new IllegalArgumentException(Messages.getString("StringUtils.15", new String[] { openingMarkers, closingMarkers })); 
    if (Character.isWhitespace(searchForSequence[0].charAt(0)) && searchMode.contains(SearchMode.SKIP_WHITE_SPACE)) {
      searchMode = EnumSet.copyOf(searchMode);
      searchMode.remove(SearchMode.SKIP_WHITE_SPACE);
    } 
    Set<SearchMode> searchMode2 = EnumSet.of(SearchMode.SKIP_WHITE_SPACE);
    searchMode2.addAll(searchMode);
    searchMode2.remove(SearchMode.SKIP_BETWEEN_MARKERS);
    for (int positionOfFirstWord = startingPosition; positionOfFirstWord <= stopSearchingAt; positionOfFirstWord++) {
      positionOfFirstWord = indexOfIgnoreCase(positionOfFirstWord, searchIn, searchForSequence[0], openingMarkers, closingMarkers, searchMode);
      if (positionOfFirstWord == -1 || positionOfFirstWord > stopSearchingAt)
        return -1; 
      int startingPositionForNextWord = positionOfFirstWord + searchForSequence[0].length();
      int wc = 0;
      boolean match = true;
      while (++wc < searchForWordsCount && match) {
        int positionOfNextWord = indexOfNextChar(startingPositionForNextWord, searchInLength - 1, searchIn, null, null, null, searchMode2);
        if (startingPositionForNextWord == positionOfNextWord || !startsWithIgnoreCase(searchIn, positionOfNextWord, searchForSequence[wc])) {
          match = false;
          continue;
        } 
        startingPositionForNextWord = positionOfNextWord + searchForSequence[wc].length();
      } 
      if (match)
        return positionOfFirstWord; 
    } 
    return -1;
  }
  
  public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor, String openingMarkers, String closingMarkers, Set<SearchMode> searchMode) {
    return indexOfIgnoreCase(startingPosition, searchIn, searchFor, openingMarkers, closingMarkers, "", searchMode);
  }
  
  public static int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor, String openingMarkers, String closingMarkers, String overridingMarkers, Set<SearchMode> searchMode) {
    if (searchIn == null || searchFor == null)
      return -1; 
    int searchInLength = searchIn.length();
    int searchForLength = searchFor.length();
    int stopSearchingAt = searchInLength - searchForLength;
    if (startingPosition > stopSearchingAt || searchForLength == 0)
      return -1; 
    if (searchMode.contains(SearchMode.SKIP_BETWEEN_MARKERS)) {
      if (openingMarkers == null || closingMarkers == null || openingMarkers.length() != closingMarkers.length())
        throw new IllegalArgumentException(Messages.getString("StringUtils.15", new String[] { openingMarkers, closingMarkers })); 
      if (overridingMarkers == null)
        throw new IllegalArgumentException(Messages.getString("StringUtils.16", new String[] { overridingMarkers, openingMarkers })); 
      for (char c : overridingMarkers.toCharArray()) {
        if (openingMarkers.indexOf(c) == -1)
          throw new IllegalArgumentException(Messages.getString("StringUtils.16", new String[] { overridingMarkers, openingMarkers })); 
      } 
    } 
    char firstCharOfSearchForUc = Character.toUpperCase(searchFor.charAt(0));
    char firstCharOfSearchForLc = Character.toLowerCase(searchFor.charAt(0));
    if (Character.isWhitespace(firstCharOfSearchForLc) && searchMode.contains(SearchMode.SKIP_WHITE_SPACE)) {
      searchMode = EnumSet.copyOf(searchMode);
      searchMode.remove(SearchMode.SKIP_WHITE_SPACE);
    } 
    for (int i = startingPosition; i <= stopSearchingAt; i++) {
      i = indexOfNextChar(i, stopSearchingAt, searchIn, openingMarkers, closingMarkers, overridingMarkers, searchMode);
      if (i == -1)
        return -1; 
      char c = searchIn.charAt(i);
      if (isCharEqualIgnoreCase(c, firstCharOfSearchForUc, firstCharOfSearchForLc) && startsWithIgnoreCase(searchIn, i, searchFor))
        return i; 
    } 
    return -1;
  }
  
  private static int indexOfNextChar(int startingPosition, int stopPosition, String searchIn, String openingMarkers, String closingMarkers, String overridingMarkers, Set<SearchMode> searchMode) {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull -> 6
    //   4: iconst_m1
    //   5: ireturn
    //   6: aload_2
    //   7: invokevirtual length : ()I
    //   10: istore #7
    //   12: iload_0
    //   13: iload #7
    //   15: if_icmplt -> 20
    //   18: iconst_m1
    //   19: ireturn
    //   20: iconst_0
    //   21: istore #8
    //   23: aload_2
    //   24: iload_0
    //   25: invokevirtual charAt : (I)C
    //   28: istore #9
    //   30: iload_0
    //   31: iconst_1
    //   32: iadd
    //   33: iload #7
    //   35: if_icmpge -> 48
    //   38: aload_2
    //   39: iload_0
    //   40: iconst_1
    //   41: iadd
    //   42: invokevirtual charAt : (I)C
    //   45: goto -> 49
    //   48: iconst_0
    //   49: istore #10
    //   51: iload_0
    //   52: istore #11
    //   54: iload #11
    //   56: iload_1
    //   57: if_icmpgt -> 998
    //   60: iload #9
    //   62: istore #8
    //   64: iload #10
    //   66: istore #9
    //   68: iload #11
    //   70: iconst_2
    //   71: iadd
    //   72: iload #7
    //   74: if_icmpge -> 88
    //   77: aload_2
    //   78: iload #11
    //   80: iconst_2
    //   81: iadd
    //   82: invokevirtual charAt : (I)C
    //   85: goto -> 89
    //   88: iconst_0
    //   89: istore #10
    //   91: iconst_0
    //   92: istore #12
    //   94: iconst_m1
    //   95: istore #13
    //   97: aload #6
    //   99: getstatic com/mysql/cj/util/StringUtils$SearchMode.ALLOW_BACKSLASH_ESCAPE : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   102: invokeinterface contains : (Ljava/lang/Object;)Z
    //   107: ifeq -> 150
    //   110: iload #8
    //   112: bipush #92
    //   114: if_icmpne -> 150
    //   117: iinc #11, 1
    //   120: iload #10
    //   122: istore #9
    //   124: iload #11
    //   126: iconst_2
    //   127: iadd
    //   128: iload #7
    //   130: if_icmpge -> 144
    //   133: aload_2
    //   134: iload #11
    //   136: iconst_2
    //   137: iadd
    //   138: invokevirtual charAt : (I)C
    //   141: goto -> 145
    //   144: iconst_0
    //   145: istore #10
    //   147: goto -> 992
    //   150: aload #6
    //   152: getstatic com/mysql/cj/util/StringUtils$SearchMode.SKIP_BETWEEN_MARKERS : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   155: invokeinterface contains : (Ljava/lang/Object;)Z
    //   160: ifeq -> 462
    //   163: aload_3
    //   164: iload #8
    //   166: invokevirtual indexOf : (I)I
    //   169: dup
    //   170: istore #13
    //   172: iconst_m1
    //   173: if_icmpeq -> 462
    //   176: iconst_0
    //   177: istore #14
    //   179: iload #8
    //   181: istore #15
    //   183: aload #4
    //   185: iload #13
    //   187: invokevirtual charAt : (I)C
    //   190: istore #16
    //   192: aload #5
    //   194: iload #15
    //   196: invokevirtual indexOf : (I)I
    //   199: iconst_m1
    //   200: if_icmpeq -> 207
    //   203: iconst_1
    //   204: goto -> 208
    //   207: iconst_0
    //   208: istore #17
    //   210: iinc #11, 1
    //   213: iload #11
    //   215: iload_1
    //   216: if_icmpgt -> 413
    //   219: aload_2
    //   220: iload #11
    //   222: invokevirtual charAt : (I)C
    //   225: dup
    //   226: istore #8
    //   228: iload #16
    //   230: if_icmpne -> 238
    //   233: iload #14
    //   235: ifeq -> 413
    //   238: iload #17
    //   240: ifne -> 361
    //   243: aload #5
    //   245: iload #8
    //   247: invokevirtual indexOf : (I)I
    //   250: iconst_m1
    //   251: if_icmpeq -> 361
    //   254: aload_3
    //   255: iload #8
    //   257: invokevirtual indexOf : (I)I
    //   260: istore #18
    //   262: iconst_0
    //   263: istore #19
    //   265: iload #8
    //   267: istore #20
    //   269: aload #4
    //   271: iload #18
    //   273: invokevirtual charAt : (I)C
    //   276: istore #21
    //   278: iinc #11, 1
    //   281: iload #11
    //   283: iload_1
    //   284: if_icmpgt -> 358
    //   287: aload_2
    //   288: iload #11
    //   290: invokevirtual charAt : (I)C
    //   293: dup
    //   294: istore #8
    //   296: iload #21
    //   298: if_icmpne -> 306
    //   301: iload #19
    //   303: ifeq -> 358
    //   306: iload #8
    //   308: iload #20
    //   310: if_icmpne -> 319
    //   313: iinc #19, 1
    //   316: goto -> 278
    //   319: iload #8
    //   321: iload #21
    //   323: if_icmpne -> 332
    //   326: iinc #19, -1
    //   329: goto -> 278
    //   332: aload #6
    //   334: getstatic com/mysql/cj/util/StringUtils$SearchMode.ALLOW_BACKSLASH_ESCAPE : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   337: invokeinterface contains : (Ljava/lang/Object;)Z
    //   342: ifeq -> 278
    //   345: iload #8
    //   347: bipush #92
    //   349: if_icmpne -> 278
    //   352: iinc #11, 1
    //   355: goto -> 278
    //   358: goto -> 210
    //   361: iload #8
    //   363: iload #15
    //   365: if_icmpne -> 374
    //   368: iinc #14, 1
    //   371: goto -> 210
    //   374: iload #8
    //   376: iload #16
    //   378: if_icmpne -> 387
    //   381: iinc #14, -1
    //   384: goto -> 210
    //   387: aload #6
    //   389: getstatic com/mysql/cj/util/StringUtils$SearchMode.ALLOW_BACKSLASH_ESCAPE : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   392: invokeinterface contains : (Ljava/lang/Object;)Z
    //   397: ifeq -> 210
    //   400: iload #8
    //   402: bipush #92
    //   404: if_icmpne -> 210
    //   407: iinc #11, 1
    //   410: goto -> 210
    //   413: iload #11
    //   415: iconst_1
    //   416: iadd
    //   417: iload #7
    //   419: if_icmpge -> 433
    //   422: aload_2
    //   423: iload #11
    //   425: iconst_1
    //   426: iadd
    //   427: invokevirtual charAt : (I)C
    //   430: goto -> 434
    //   433: iconst_0
    //   434: istore #9
    //   436: iload #11
    //   438: iconst_2
    //   439: iadd
    //   440: iload #7
    //   442: if_icmpge -> 456
    //   445: aload_2
    //   446: iload #11
    //   448: iconst_2
    //   449: iadd
    //   450: invokevirtual charAt : (I)C
    //   453: goto -> 457
    //   456: iconst_0
    //   457: istore #10
    //   459: goto -> 992
    //   462: aload #6
    //   464: getstatic com/mysql/cj/util/StringUtils$SearchMode.SKIP_BLOCK_COMMENTS : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   467: invokeinterface contains : (Ljava/lang/Object;)Z
    //   472: ifeq -> 661
    //   475: iload #8
    //   477: bipush #47
    //   479: if_icmpne -> 661
    //   482: iload #9
    //   484: bipush #42
    //   486: if_icmpne -> 661
    //   489: iload #10
    //   491: bipush #33
    //   493: if_icmpeq -> 554
    //   496: iinc #11, 1
    //   499: iinc #11, 1
    //   502: iload #11
    //   504: iload_1
    //   505: if_icmpgt -> 548
    //   508: aload_2
    //   509: iload #11
    //   511: invokevirtual charAt : (I)C
    //   514: bipush #42
    //   516: if_icmpne -> 499
    //   519: iload #11
    //   521: iconst_1
    //   522: iadd
    //   523: iload #7
    //   525: if_icmpge -> 539
    //   528: aload_2
    //   529: iload #11
    //   531: iconst_1
    //   532: iadd
    //   533: invokevirtual charAt : (I)C
    //   536: goto -> 540
    //   539: iconst_0
    //   540: bipush #47
    //   542: if_icmpeq -> 548
    //   545: goto -> 499
    //   548: iinc #11, 1
    //   551: goto -> 612
    //   554: iinc #11, 1
    //   557: iinc #11, 1
    //   560: iconst_1
    //   561: istore #14
    //   563: iload #14
    //   565: iconst_5
    //   566: if_icmpgt -> 603
    //   569: iload #11
    //   571: iload #14
    //   573: iadd
    //   574: iload #7
    //   576: if_icmpge -> 603
    //   579: aload_2
    //   580: iload #11
    //   582: iload #14
    //   584: iadd
    //   585: invokevirtual charAt : (I)C
    //   588: invokestatic isDigit : (C)Z
    //   591: ifne -> 597
    //   594: goto -> 603
    //   597: iinc #14, 1
    //   600: goto -> 563
    //   603: iload #14
    //   605: iconst_5
    //   606: if_icmpne -> 612
    //   609: iinc #11, 5
    //   612: iload #11
    //   614: iconst_1
    //   615: iadd
    //   616: iload #7
    //   618: if_icmpge -> 632
    //   621: aload_2
    //   622: iload #11
    //   624: iconst_1
    //   625: iadd
    //   626: invokevirtual charAt : (I)C
    //   629: goto -> 633
    //   632: iconst_0
    //   633: istore #9
    //   635: iload #11
    //   637: iconst_2
    //   638: iadd
    //   639: iload #7
    //   641: if_icmpge -> 655
    //   644: aload_2
    //   645: iload #11
    //   647: iconst_2
    //   648: iadd
    //   649: invokevirtual charAt : (I)C
    //   652: goto -> 656
    //   655: iconst_0
    //   656: istore #10
    //   658: goto -> 992
    //   661: aload #6
    //   663: getstatic com/mysql/cj/util/StringUtils$SearchMode.SKIP_BLOCK_COMMENTS : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   666: invokeinterface contains : (Ljava/lang/Object;)Z
    //   671: ifeq -> 721
    //   674: iload #8
    //   676: bipush #42
    //   678: if_icmpne -> 721
    //   681: iload #9
    //   683: bipush #47
    //   685: if_icmpne -> 721
    //   688: iinc #11, 1
    //   691: iload #10
    //   693: istore #9
    //   695: iload #11
    //   697: iconst_2
    //   698: iadd
    //   699: iload #7
    //   701: if_icmpge -> 715
    //   704: aload_2
    //   705: iload #11
    //   707: iconst_2
    //   708: iadd
    //   709: invokevirtual charAt : (I)C
    //   712: goto -> 716
    //   715: iconst_0
    //   716: istore #10
    //   718: goto -> 992
    //   721: aload #6
    //   723: getstatic com/mysql/cj/util/StringUtils$SearchMode.SKIP_LINE_COMMENTS : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   726: invokeinterface contains : (Ljava/lang/Object;)Z
    //   731: ifeq -> 968
    //   734: iload #8
    //   736: bipush #45
    //   738: if_icmpne -> 779
    //   741: iload #9
    //   743: bipush #45
    //   745: if_icmpne -> 779
    //   748: iload #10
    //   750: invokestatic isWhitespace : (C)Z
    //   753: ifne -> 786
    //   756: iload #10
    //   758: bipush #59
    //   760: if_icmpne -> 767
    //   763: iconst_1
    //   764: goto -> 768
    //   767: iconst_0
    //   768: dup
    //   769: istore #12
    //   771: ifne -> 786
    //   774: iload #10
    //   776: ifeq -> 786
    //   779: iload #8
    //   781: bipush #35
    //   783: if_icmpne -> 968
    //   786: iload #12
    //   788: ifeq -> 846
    //   791: iinc #11, 1
    //   794: iinc #11, 1
    //   797: iload #11
    //   799: iconst_1
    //   800: iadd
    //   801: iload #7
    //   803: if_icmpge -> 817
    //   806: aload_2
    //   807: iload #11
    //   809: iconst_1
    //   810: iadd
    //   811: invokevirtual charAt : (I)C
    //   814: goto -> 818
    //   817: iconst_0
    //   818: istore #9
    //   820: iload #11
    //   822: iconst_2
    //   823: iadd
    //   824: iload #7
    //   826: if_icmpge -> 840
    //   829: aload_2
    //   830: iload #11
    //   832: iconst_2
    //   833: iadd
    //   834: invokevirtual charAt : (I)C
    //   837: goto -> 841
    //   840: iconst_0
    //   841: istore #10
    //   843: goto -> 992
    //   846: iinc #11, 1
    //   849: iload #11
    //   851: iload_1
    //   852: if_icmpgt -> 879
    //   855: aload_2
    //   856: iload #11
    //   858: invokevirtual charAt : (I)C
    //   861: dup
    //   862: istore #8
    //   864: bipush #10
    //   866: if_icmpeq -> 879
    //   869: iload #8
    //   871: bipush #13
    //   873: if_icmpeq -> 879
    //   876: goto -> 846
    //   879: iload #11
    //   881: iconst_1
    //   882: iadd
    //   883: iload #7
    //   885: if_icmpge -> 899
    //   888: aload_2
    //   889: iload #11
    //   891: iconst_1
    //   892: iadd
    //   893: invokevirtual charAt : (I)C
    //   896: goto -> 900
    //   899: iconst_0
    //   900: istore #9
    //   902: iload #8
    //   904: bipush #13
    //   906: if_icmpne -> 942
    //   909: iload #9
    //   911: bipush #10
    //   913: if_icmpne -> 942
    //   916: iinc #11, 1
    //   919: iload #11
    //   921: iconst_1
    //   922: iadd
    //   923: iload #7
    //   925: if_icmpge -> 939
    //   928: aload_2
    //   929: iload #11
    //   931: iconst_1
    //   932: iadd
    //   933: invokevirtual charAt : (I)C
    //   936: goto -> 940
    //   939: iconst_0
    //   940: istore #9
    //   942: iload #11
    //   944: iconst_2
    //   945: iadd
    //   946: iload #7
    //   948: if_icmpge -> 962
    //   951: aload_2
    //   952: iload #11
    //   954: iconst_2
    //   955: iadd
    //   956: invokevirtual charAt : (I)C
    //   959: goto -> 963
    //   962: iconst_0
    //   963: istore #10
    //   965: goto -> 992
    //   968: aload #6
    //   970: getstatic com/mysql/cj/util/StringUtils$SearchMode.SKIP_WHITE_SPACE : Lcom/mysql/cj/util/StringUtils$SearchMode;
    //   973: invokeinterface contains : (Ljava/lang/Object;)Z
    //   978: ifeq -> 989
    //   981: iload #8
    //   983: invokestatic isWhitespace : (C)Z
    //   986: ifne -> 992
    //   989: iload #11
    //   991: ireturn
    //   992: iinc #11, 1
    //   995: goto -> 54
    //   998: iconst_m1
    //   999: ireturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #596	-> 0
    //   #597	-> 4
    //   #600	-> 6
    //   #602	-> 12
    //   #603	-> 18
    //   #606	-> 20
    //   #607	-> 23
    //   #608	-> 30
    //   #610	-> 51
    //   #611	-> 60
    //   #612	-> 64
    //   #613	-> 68
    //   #615	-> 91
    //   #616	-> 94
    //   #618	-> 97
    //   #619	-> 117
    //   #621	-> 120
    //   #622	-> 124
    //   #624	-> 150
    //   #626	-> 176
    //   #627	-> 179
    //   #628	-> 183
    //   #629	-> 192
    //   #630	-> 210
    //   #631	-> 238
    //   #633	-> 254
    //   #634	-> 262
    //   #635	-> 265
    //   #636	-> 269
    //   #637	-> 278
    //   #639	-> 306
    //   #640	-> 313
    //   #641	-> 319
    //   #642	-> 326
    //   #643	-> 332
    //   #644	-> 352
    //   #647	-> 358
    //   #648	-> 368
    //   #649	-> 374
    //   #650	-> 381
    //   #651	-> 387
    //   #652	-> 407
    //   #656	-> 413
    //   #657	-> 436
    //   #659	-> 459
    //   #660	-> 489
    //   #662	-> 496
    //   #663	-> 499
    //   #664	-> 511
    //   #667	-> 548
    //   #671	-> 554
    //   #672	-> 557
    //   #674	-> 560
    //   #675	-> 563
    //   #676	-> 569
    //   #677	-> 594
    //   #675	-> 597
    //   #680	-> 603
    //   #681	-> 609
    //   #685	-> 612
    //   #686	-> 635
    //   #688	-> 661
    //   #691	-> 688
    //   #693	-> 691
    //   #694	-> 695
    //   #696	-> 721
    //   #697	-> 750
    //   #699	-> 786
    //   #701	-> 791
    //   #702	-> 794
    //   #704	-> 797
    //   #705	-> 820
    //   #708	-> 846
    //   #712	-> 879
    //   #713	-> 902
    //   #715	-> 916
    //   #716	-> 919
    //   #718	-> 942
    //   #721	-> 968
    //   #722	-> 989
    //   #610	-> 992
    //   #726	-> 998
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   262	96	18	overridingMarkerIndex	I
    //   265	93	19	overridingNestedMarkersCount	I
    //   269	89	20	overridingOpeningMarker	C
    //   278	80	21	overridingClosingMarker	C
    //   179	280	14	nestedMarkersCount	I
    //   183	276	15	openingMarker	C
    //   192	267	16	closingMarker	C
    //   210	249	17	outerIsAnOverridingMarker	Z
    //   563	49	14	j	I
    //   94	898	12	dashDashCommentImmediateEnd	Z
    //   97	895	13	markerIndex	I
    //   54	944	11	i	I
    //   0	1000	0	startingPosition	I
    //   0	1000	1	stopPosition	I
    //   0	1000	2	searchIn	Ljava/lang/String;
    //   0	1000	3	openingMarkers	Ljava/lang/String;
    //   0	1000	4	closingMarkers	Ljava/lang/String;
    //   0	1000	5	overridingMarkers	Ljava/lang/String;
    //   0	1000	6	searchMode	Ljava/util/Set;
    //   12	988	7	searchInLength	I
    //   23	977	8	c0	C
    //   30	970	9	c1	C
    //   51	949	10	c2	C
    // Local variable type table:
    //   start	length	slot	name	signature
    //   0	1000	6	searchMode	Ljava/util/Set<Lcom/mysql/cj/util/StringUtils$SearchMode;>;
  }
  
  private static boolean isCharAtPosNotEqualIgnoreCase(String searchIn, int pos, char firstCharOfSearchForUc, char firstCharOfSearchForLc) {
    return (Character.toLowerCase(searchIn.charAt(pos)) != firstCharOfSearchForLc && Character.toUpperCase(searchIn.charAt(pos)) != firstCharOfSearchForUc);
  }
  
  private static boolean isCharEqualIgnoreCase(char charToCompare, char compareToCharUC, char compareToCharLC) {
    return (Character.toLowerCase(charToCompare) == compareToCharLC || Character.toUpperCase(charToCompare) == compareToCharUC);
  }
  
  public static List<String> split(String stringToSplit, String delimiter, boolean trim) {
    if (stringToSplit == null)
      return new ArrayList<>(); 
    if (delimiter == null)
      throw new IllegalArgumentException(); 
    String[] tokens = stringToSplit.split(delimiter, -1);
    Stream<String> tokensStream = Arrays.<String>asList(tokens).stream();
    if (trim)
      tokensStream = tokensStream.map(String::trim); 
    return tokensStream.collect((Collector)Collectors.toList());
  }
  
  public static List<String> split(String stringToSplit, String delimiter, String openingMarkers, String closingMarkers, boolean trim) {
    return split(stringToSplit, delimiter, openingMarkers, closingMarkers, "", trim);
  }
  
  public static List<String> split(String stringToSplit, String delimiter, String openingMarkers, String closingMarkers, boolean trim, Set<SearchMode> searchMode) {
    return split(stringToSplit, delimiter, openingMarkers, closingMarkers, "", trim, searchMode);
  }
  
  public static List<String> split(String stringToSplit, String delimiter, String openingMarkers, String closingMarkers, String overridingMarkers, boolean trim) {
    return split(stringToSplit, delimiter, openingMarkers, closingMarkers, overridingMarkers, trim, SEARCH_MODE__MRK_COM_WS);
  }
  
  public static List<String> split(String stringToSplit, String delimiter, String openingMarkers, String closingMarkers, String overridingMarkers, boolean trim, Set<SearchMode> searchMode) {
    if (stringToSplit == null)
      return new ArrayList<>(); 
    if (delimiter == null)
      throw new IllegalArgumentException(); 
    int delimPos = 0;
    int currentPos = 0;
    List<String> splitTokens = new ArrayList<>();
    while ((delimPos = indexOfIgnoreCase(currentPos, stringToSplit, delimiter, openingMarkers, closingMarkers, overridingMarkers, searchMode)) != -1) {
      String str = stringToSplit.substring(currentPos, delimPos);
      if (trim)
        str = str.trim(); 
      splitTokens.add(str);
      currentPos = delimPos + delimiter.length();
    } 
    String token = stringToSplit.substring(currentPos);
    if (trim)
      token = token.trim(); 
    splitTokens.add(token);
    return splitTokens;
  }
  
  private static boolean startsWith(byte[] dataFrom, String chars) {
    int charsLength = chars.length();
    if (dataFrom.length < charsLength)
      return false; 
    for (int i = 0; i < charsLength; i++) {
      if (dataFrom[i] != chars.charAt(i))
        return false; 
    } 
    return true;
  }
  
  public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor) {
    return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
  }
  
  public static boolean startsWithIgnoreCase(String searchIn, String searchFor) {
    return startsWithIgnoreCase(searchIn, 0, searchFor);
  }
  
  public static boolean startsWithIgnoreCaseAndNonAlphaNumeric(String searchIn, String searchFor) {
    if (searchIn == null)
      return (searchFor == null); 
    int beginPos = 0;
    int inLength = searchIn.length();
    for (; beginPos < inLength; beginPos++) {
      char c = searchIn.charAt(beginPos);
      if (Character.isLetterOrDigit(c))
        break; 
    } 
    return startsWithIgnoreCase(searchIn, beginPos, searchFor);
  }
  
  public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor) {
    return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
  }
  
  public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos) {
    if (searchIn == null)
      return (searchFor == null); 
    int inLength = searchIn.length();
    for (; beginPos < inLength && 
      Character.isWhitespace(searchIn.charAt(beginPos)); beginPos++);
    return startsWithIgnoreCase(searchIn, beginPos, searchFor);
  }
  
  public static int startsWithIgnoreCaseAndWs(String searchIn, String[] searchFor) {
    for (int i = 0; i < searchFor.length; i++) {
      if (startsWithIgnoreCaseAndWs(searchIn, searchFor[i], 0))
        return i; 
    } 
    return -1;
  }
  
  public static byte[] stripEnclosure(byte[] source, String prefix, String suffix) {
    if (source.length >= prefix.length() + suffix.length() && startsWith(source, prefix) && endsWith(source, suffix)) {
      int totalToStrip = prefix.length() + suffix.length();
      int enclosedLength = source.length - totalToStrip;
      byte[] enclosed = new byte[enclosedLength];
      int startPos = prefix.length();
      int numToCopy = enclosed.length;
      System.arraycopy(source, startPos, enclosed, 0, numToCopy);
      return enclosed;
    } 
    return source;
  }
  
  public static String toAsciiString(byte[] buffer) {
    return toAsciiString(buffer, 0, buffer.length);
  }
  
  public static String toAsciiString(byte[] buffer, int startPos, int length) {
    char[] charArray = new char[length];
    int readpoint = startPos;
    for (int i = 0; i < length; i++) {
      charArray[i] = (char)buffer[readpoint];
      readpoint++;
    } 
    return new String(charArray);
  }
  
  public static boolean wildCompareIgnoreCase(String searchIn, String searchFor) {
    return (wildCompareInternal(searchIn, searchFor) == 0);
  }
  
  private static int wildCompareInternal(String searchIn, String searchFor) {
    if (searchIn == null || searchFor == null)
      return -1; 
    if (searchFor.equals("%"))
      return 0; 
    int searchForPos = 0;
    int searchForEnd = searchFor.length();
    int searchInPos = 0;
    int searchInEnd = searchIn.length();
    int result = -1;
    while (searchForPos != searchForEnd) {
      while (searchFor.charAt(searchForPos) != '%' && searchFor.charAt(searchForPos) != '_') {
        if (searchFor.charAt(searchForPos) == '\\' && searchForPos + 1 != searchForEnd)
          searchForPos++; 
        if (searchInPos == searchInEnd || 
          Character.toUpperCase(searchFor.charAt(searchForPos++)) != Character.toUpperCase(searchIn.charAt(searchInPos++)))
          return 1; 
        if (searchForPos == searchForEnd)
          return (searchInPos != searchInEnd) ? 1 : 0; 
        result = 1;
      } 
      if (searchFor.charAt(searchForPos) == '_') {
        do {
          if (searchInPos == searchInEnd)
            return result; 
          searchInPos++;
        } while (++searchForPos < searchForEnd && searchFor.charAt(searchForPos) == '_');
        if (searchForPos == searchForEnd)
          break; 
      } 
      if (searchFor.charAt(searchForPos) == '%') {
        searchForPos++;
        for (; searchForPos != searchForEnd; searchForPos++) {
          if (searchFor.charAt(searchForPos) != '%')
            if (searchFor.charAt(searchForPos) == '_') {
              if (searchInPos == searchInEnd)
                return -1; 
              searchInPos++;
            } else {
              break;
            }  
        } 
        if (searchForPos == searchForEnd)
          return 0; 
        if (searchInPos == searchInEnd)
          return -1; 
        char cmp;
        if ((cmp = searchFor.charAt(searchForPos)) == '\\' && searchForPos + 1 != searchForEnd)
          cmp = searchFor.charAt(++searchForPos); 
        searchForPos++;
        while (true) {
          if (searchInPos != searchInEnd && Character.toUpperCase(searchIn.charAt(searchInPos)) != Character.toUpperCase(cmp)) {
            searchInPos++;
            continue;
          } 
          if (searchInPos++ == searchInEnd)
            return -1; 
          int tmp = wildCompareInternal(searchIn.substring(searchInPos), searchFor.substring(searchForPos));
          if (tmp <= 0)
            return tmp; 
          if (searchInPos == searchInEnd)
            break; 
        } 
        return -1;
      } 
    } 
    return (searchInPos != searchInEnd) ? 1 : 0;
  }
  
  public static int lastIndexOf(byte[] s, char c) {
    if (s == null)
      return -1; 
    for (int i = s.length - 1; i >= 0; i--) {
      if (s[i] == c)
        return i; 
    } 
    return -1;
  }
  
  public static int indexOf(byte[] s, char c) {
    if (s == null)
      return -1; 
    int length = s.length;
    for (int i = 0; i < length; i++) {
      if (s[i] == c)
        return i; 
    } 
    return -1;
  }
  
  public static boolean isNullOrEmpty(String toTest) {
    return (toTest == null || toTest.isEmpty());
  }
  
  public static String stripComments(String src, String stringOpens, String stringCloses, boolean slashStarComments, boolean slashSlashComments, boolean hashComments, boolean dashDashComments) {
    if (src == null)
      return null; 
    StringBuilder strBuilder = new StringBuilder(src.length());
    StringReader sourceReader = new StringReader(src);
    int contextMarker = 0;
    boolean escaped = false;
    int markerTypeFound = -1;
    int ind = 0;
    int currentChar = 0;
    try {
      label81: while ((currentChar = sourceReader.read()) != -1) {
        if (markerTypeFound != -1 && currentChar == stringCloses.charAt(markerTypeFound) && !escaped) {
          contextMarker = 0;
          markerTypeFound = -1;
        } else if ((ind = stringOpens.indexOf(currentChar)) != -1 && !escaped && contextMarker == 0) {
          markerTypeFound = ind;
          contextMarker = currentChar;
        } 
        if (contextMarker == 0 && currentChar == 47 && (slashSlashComments || slashStarComments)) {
          currentChar = sourceReader.read();
          if (currentChar == 42 && slashStarComments) {
            int prevChar = 0;
            while (true) {
              if ((currentChar = sourceReader.read()) != 47 || prevChar != 42) {
                if (currentChar == 13) {
                  currentChar = sourceReader.read();
                  if (currentChar == 10)
                    currentChar = sourceReader.read(); 
                } else if (currentChar == 10) {
                  currentChar = sourceReader.read();
                } 
                if (currentChar < 0)
                  continue label81; 
                prevChar = currentChar;
                continue;
              } 
              continue label81;
            } 
          } 
          if (currentChar == 47 && slashSlashComments)
            while ((currentChar = sourceReader.read()) != 10 && currentChar != 13 && currentChar >= 0); 
        } else if (contextMarker == 0 && currentChar == 35 && hashComments) {
          while ((currentChar = sourceReader.read()) != 10 && currentChar != 13 && currentChar >= 0);
        } else if (contextMarker == 0 && currentChar == 45 && dashDashComments) {
          currentChar = sourceReader.read();
          if (currentChar == -1 || currentChar != 45) {
            strBuilder.append('-');
            if (currentChar != -1)
              strBuilder.append((char)currentChar); 
            continue;
          } 
          while ((currentChar = sourceReader.read()) != 10 && currentChar != 13 && currentChar >= 0);
        } 
        if (currentChar != -1)
          strBuilder.append((char)currentChar); 
      } 
    } catch (IOException iOException) {}
    return strBuilder.toString();
  }
  
  public static String sanitizeProcOrFuncName(String src) {
    if (src == null || src.equals("%"))
      return null; 
    return src;
  }
  
  public static List<String> splitDBdotName(String source, String db, String quoteId, boolean isNoBslashEscSet) {
    String entityName;
    if (source == null || source.equals("%"))
      return Collections.emptyList(); 
    int dotIndex = -1;
    if (" ".equals(quoteId)) {
      dotIndex = source.indexOf(".");
    } else {
      dotIndex = indexOfIgnoreCase(0, source, ".", quoteId, quoteId, isNoBslashEscSet ? SEARCH_MODE__MRK_WS : SEARCH_MODE__BSESC_MRK_WS);
    } 
    String database = db;
    if (dotIndex != -1) {
      database = unQuoteIdentifier(source.substring(0, dotIndex), quoteId);
      entityName = unQuoteIdentifier(source.substring(dotIndex + 1), quoteId);
    } else {
      entityName = unQuoteIdentifier(source, quoteId);
    } 
    return Arrays.asList(new String[] { database, entityName });
  }
  
  public static String getFullyQualifiedName(String db, String entity, String quoteId, boolean isPedantic) {
    StringBuilder fullyQualifiedName = new StringBuilder(quoteIdentifier((db == null) ? "" : db, quoteId, isPedantic));
    fullyQualifiedName.append('.');
    fullyQualifiedName.append(quoteIdentifier(entity, quoteId, isPedantic));
    return fullyQualifiedName.toString();
  }
  
  public static boolean isEmptyOrWhitespaceOnly(String str) {
    if (str == null || str.length() == 0)
      return true; 
    int length = str.length();
    for (int i = 0; i < length; i++) {
      if (!Character.isWhitespace(str.charAt(i)))
        return false; 
    } 
    return true;
  }
  
  public static String escapeQuote(String src, String quotChar) {
    if (src == null)
      return null; 
    src = toString(stripEnclosure(src.getBytes(), quotChar, quotChar));
    int lastNdx = src.indexOf(quotChar);
    String tmpSrc = src.substring(0, lastNdx);
    tmpSrc = tmpSrc + quotChar + quotChar;
    String tmpRest = src.substring(lastNdx + 1, src.length());
    lastNdx = tmpRest.indexOf(quotChar);
    while (lastNdx > -1) {
      tmpSrc = tmpSrc + tmpRest.substring(0, lastNdx);
      tmpSrc = tmpSrc + quotChar + quotChar;
      tmpRest = tmpRest.substring(lastNdx + 1, tmpRest.length());
      lastNdx = tmpRest.indexOf(quotChar);
    } 
    tmpSrc = tmpSrc + tmpRest;
    src = tmpSrc;
    return src;
  }
  
  public static String quoteIdentifier(String identifier, String quoteChar, boolean isPedantic) {
    if (identifier == null)
      return null; 
    identifier = identifier.trim();
    int quoteCharLength = quoteChar.length();
    if (quoteCharLength == 0)
      return identifier; 
    if (!isPedantic && identifier.startsWith(quoteChar) && identifier.endsWith(quoteChar)) {
      String identifierQuoteTrimmed = identifier.substring(quoteCharLength, identifier.length() - quoteCharLength);
      int quoteCharPos = identifierQuoteTrimmed.indexOf(quoteChar);
      while (quoteCharPos >= 0) {
        int quoteCharNextExpectedPos = quoteCharPos + quoteCharLength;
        int quoteCharNextPosition = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextExpectedPos);
        if (quoteCharNextPosition == quoteCharNextExpectedPos)
          quoteCharPos = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextPosition + quoteCharLength); 
      } 
      if (quoteCharPos < 0)
        return identifier; 
    } 
    return quoteChar + identifier.replaceAll(quoteChar, quoteChar + quoteChar) + quoteChar;
  }
  
  public static String quoteIdentifier(String identifier, boolean isPedantic) {
    return quoteIdentifier(identifier, "`", isPedantic);
  }
  
  public static String unQuoteIdentifier(String identifier, String quoteChar) {
    if (identifier == null)
      return null; 
    identifier = identifier.trim();
    int quoteCharLength = quoteChar.length();
    if (quoteCharLength == 0)
      return identifier; 
    if (identifier.startsWith(quoteChar) && identifier.endsWith(quoteChar)) {
      String identifierQuoteTrimmed = identifier.substring(quoteCharLength, identifier.length() - quoteCharLength);
      int quoteCharPos = identifierQuoteTrimmed.indexOf(quoteChar);
      while (quoteCharPos >= 0) {
        int quoteCharNextExpectedPos = quoteCharPos + quoteCharLength;
        int quoteCharNextPosition = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextExpectedPos);
        if (quoteCharNextPosition == quoteCharNextExpectedPos) {
          quoteCharPos = identifierQuoteTrimmed.indexOf(quoteChar, quoteCharNextPosition + quoteCharLength);
          continue;
        } 
        return identifier;
      } 
      return identifier.substring(quoteCharLength, identifier.length() - quoteCharLength).replaceAll(quoteChar + quoteChar, quoteChar);
    } 
    return identifier;
  }
  
  public static int indexOfQuoteDoubleAware(String searchIn, String quoteChar, int startFrom) {
    if (searchIn == null || quoteChar == null || quoteChar.length() == 0 || startFrom > searchIn.length())
      return -1; 
    int lastIndex = searchIn.length() - 1;
    int beginPos = startFrom;
    int pos = -1;
    boolean next = true;
    while (next) {
      pos = searchIn.indexOf(quoteChar, beginPos);
      if (pos == -1 || pos == lastIndex || !searchIn.startsWith(quoteChar, pos + 1)) {
        next = false;
        continue;
      } 
      beginPos = pos + 2;
    } 
    return pos;
  }
  
  public static String toString(byte[] value, int offset, int length, String encoding) {
    if (encoding == null || "null".equalsIgnoreCase(encoding))
      return new String(value, offset, length); 
    try {
      return new String(value, offset, length, encoding);
    } catch (UnsupportedEncodingException uee) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("StringUtils.0", new Object[] { encoding }), uee);
    } 
  }
  
  public static String toString(byte[] value, String encoding) {
    if (encoding == null)
      return new String(value); 
    try {
      return new String(value, encoding);
    } catch (UnsupportedEncodingException uee) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("StringUtils.0", new Object[] { encoding }), uee);
    } 
  }
  
  public static String toString(byte[] value, int offset, int length) {
    return new String(value, offset, length);
  }
  
  public static String toString(byte[] value) {
    return new String(value);
  }
  
  public static byte[] getBytes(char[] value) {
    return getBytes(value, 0, value.length);
  }
  
  public static byte[] getBytes(char[] c, String encoding) {
    return getBytes(c, 0, c.length, encoding);
  }
  
  public static byte[] getBytes(char[] value, int offset, int length) {
    return getBytes(value, offset, length, (String)null);
  }
  
  public static byte[] getBytes(char[] value, int offset, int length, String encoding) {
    Charset cs;
    try {
      if (encoding == null) {
        cs = Charset.defaultCharset();
      } else {
        cs = Charset.forName(encoding);
      } 
    } catch (UnsupportedCharsetException ex) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("StringUtils.0", new Object[] { encoding }), ex);
    } 
    ByteBuffer buf = cs.encode(CharBuffer.wrap(value, offset, length));
    int encodedLen = buf.limit();
    byte[] asBytes = new byte[encodedLen];
    buf.get(asBytes, 0, encodedLen);
    return asBytes;
  }
  
  public static byte[] getBytes(String value) {
    return value.getBytes();
  }
  
  public static byte[] getBytes(String value, int offset, int length) {
    return value.substring(offset, offset + length).getBytes();
  }
  
  public static byte[] getBytes(String value, int offset, int length, String encoding) {
    if (encoding == null)
      return getBytes(value, offset, length); 
    try {
      return value.substring(offset, offset + length).getBytes(encoding);
    } catch (UnsupportedEncodingException uee) {
      throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("StringUtils.0", new Object[] { encoding }), uee);
    } 
  }
  
  public static final boolean isValidIdChar(char c) {
    return ("abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789$_#@".indexOf(c) != -1);
  }
  
  private static final char[] HEX_DIGITS = new char[] { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'a', 'b', 'c', 'd', 'e', 'f' };
  
  public static void appendAsHex(StringBuilder builder, byte[] bytes) {
    builder.append("0x");
    for (byte b : bytes)
      builder.append(HEX_DIGITS[b >>> 4 & 0xF]).append(HEX_DIGITS[b & 0xF]); 
  }
  
  public static void appendAsHex(StringBuilder builder, int value) {
    if (value == 0) {
      builder.append("0x0");
      return;
    } 
    int shift = 32;
    boolean nonZeroFound = false;
    builder.append("0x");
    do {
      shift -= 4;
      byte nibble = (byte)(value >>> shift & 0xF);
      if (nonZeroFound) {
        builder.append(HEX_DIGITS[nibble]);
      } else if (nibble != 0) {
        builder.append(HEX_DIGITS[nibble]);
        nonZeroFound = true;
      } 
    } while (shift != 0);
  }
  
  public static byte[] getBytesNullTerminated(String value, String encoding) {
    Charset cs = Charset.forName(encoding);
    ByteBuffer buf = cs.encode(value);
    int encodedLen = buf.limit();
    byte[] asBytes = new byte[encodedLen + 1];
    buf.get(asBytes, 0, encodedLen);
    asBytes[encodedLen] = 0;
    return asBytes;
  }
  
  public static boolean canHandleAsServerPreparedStatementNoCache(String sql, ServerVersion serverVersion, boolean allowMultiQueries, boolean noBackslashEscapes, boolean useAnsiQuotes) {
    if (startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL"))
      return false; 
    boolean canHandleAsStatement = true;
    boolean allowBackslashEscapes = !noBackslashEscapes;
    String quoteChar = useAnsiQuotes ? "\"" : "'";
    if (allowMultiQueries) {
      if (indexOfIgnoreCase(0, sql, ";", quoteChar, quoteChar, allowBackslashEscapes ? SEARCH_MODE__ALL : SEARCH_MODE__MRK_COM_WS) != -1)
        canHandleAsStatement = false; 
    } else if (startsWithIgnoreCaseAndWs(sql, "XA ")) {
      canHandleAsStatement = false;
    } else if (startsWithIgnoreCaseAndWs(sql, "CREATE TABLE")) {
      canHandleAsStatement = false;
    } else if (startsWithIgnoreCaseAndWs(sql, "DO")) {
      canHandleAsStatement = false;
    } else if (startsWithIgnoreCaseAndWs(sql, "SET")) {
      canHandleAsStatement = false;
    } else if (startsWithIgnoreCaseAndWs(sql, "SHOW WARNINGS") && serverVersion.meetsMinimum(ServerVersion.parseVersion("5.7.2"))) {
      canHandleAsStatement = false;
    } else if (sql.startsWith("/* ping */")) {
      canHandleAsStatement = false;
    } 
    return canHandleAsStatement;
  }
  
  static final char[] EMPTY_SPACE = new char[255];
  
  static {
    for (int i = 0; i < EMPTY_SPACE.length; i++)
      EMPTY_SPACE[i] = ' '; 
  }
  
  public static String padString(String stringVal, int requiredLength) {
    int currentLength = stringVal.length();
    int difference = requiredLength - currentLength;
    if (difference > 0) {
      StringBuilder paddedBuf = new StringBuilder(requiredLength);
      paddedBuf.append(stringVal);
      paddedBuf.append(EMPTY_SPACE, 0, difference);
      return paddedBuf.toString();
    } 
    return stringVal;
  }
  
  public static int safeIntParse(String intAsString) {
    try {
      return Integer.parseInt(intAsString);
    } catch (NumberFormatException nfe) {
      return 0;
    } 
  }
  
  public static boolean isStrictlyNumeric(CharSequence cs) {
    if (cs == null || cs.length() == 0)
      return false; 
    for (int i = 0; i < cs.length(); i++) {
      if (!Character.isDigit(cs.charAt(i)))
        return false; 
    } 
    return true;
  }
  
  public static String safeTrim(String toTrim) {
    return isNullOrEmpty(toTrim) ? toTrim : toTrim.trim();
  }
  
  public static String stringArrayToString(String[] elems, String prefix, String midDelimiter, String lastDelimiter, String suffix) {
    StringBuilder valuesString = new StringBuilder();
    if (elems.length > 1) {
      valuesString.append(Arrays.<CharSequence>stream((CharSequence[])elems).limit((elems.length - 1)).collect(Collectors.joining(midDelimiter, prefix, lastDelimiter)));
    } else {
      valuesString.append(prefix);
    } 
    valuesString.append(elems[elems.length - 1]).append(suffix);
    return valuesString.toString();
  }
  
  public static boolean hasWildcards(String src) {
    return (indexOfIgnoreCase(0, src, "%") > -1 || indexOfIgnoreCase(0, src, "_") > -1);
  }
  
  public static String getUniqueSavepointId() {
    String uuid = UUID.randomUUID().toString();
    return uuid.replaceAll("-", "_");
  }
  
  public static String joinWithSerialComma(List<?> elements) {
    if (elements == null || elements.size() == 0)
      return ""; 
    if (elements.size() == 1)
      return elements.get(0).toString(); 
    if (elements.size() == 2)
      return (new StringBuilder()).append(elements.get(0)).append(" and ").append(elements.get(1)).toString(); 
    return (String)elements.subList(0, elements.size() - 1).stream().map(Object::toString).collect(Collectors.joining(", ", "", ", and ")) + elements
      .get(elements.size() - 1).toString();
  }
  
  public static byte[] unquoteBytes(byte[] bytes) {
    if (bytes[0] == 39 && bytes[bytes.length - 1] == 39) {
      byte[] valNoQuotes = new byte[bytes.length - 2];
      int j = 0;
      int quoteCnt = 0;
      for (int i = 1; i < bytes.length - 1; i++) {
        if (bytes[i] == 39) {
          quoteCnt++;
        } else {
          quoteCnt = 0;
        } 
        if (quoteCnt == 2) {
          quoteCnt = 0;
        } else {
          valNoQuotes[j++] = bytes[i];
        } 
      } 
      byte[] res = new byte[j];
      System.arraycopy(valNoQuotes, 0, res, 0, j);
      return res;
    } 
    return bytes;
  }
  
  public static byte[] quoteBytes(byte[] bytes) {
    byte[] withQuotes = new byte[bytes.length * 2 + 2];
    int j = 0;
    withQuotes[j++] = 39;
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] == 39)
        withQuotes[j++] = 39; 
      withQuotes[j++] = bytes[i];
    } 
    withQuotes[j++] = 39;
    byte[] res = new byte[j];
    System.arraycopy(withQuotes, 0, res, 0, j);
    return res;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\c\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */