package com.mysql.cj;

import com.mysql.cj.util.StringUtils;
import java.io.UnsupportedEncodingException;

public class ParseInfo {
  protected static final String[] ON_DUPLICATE_KEY_UPDATE_CLAUSE = new String[] { "ON", "DUPLICATE", "KEY", "UPDATE" };
  
  private char firstStmtChar = Character.MIN_VALUE;
  
  private boolean foundLoadData = false;
  
  long lastUsed = 0L;
  
  int statementLength = 0;
  
  int statementStartPos = 0;
  
  boolean canRewriteAsMultiValueInsert = false;
  
  byte[][] staticSql = (byte[][])null;
  
  boolean hasPlaceholders = false;
  
  public int numberOfQueries = 1;
  
  boolean isOnDuplicateKeyUpdate = false;
  
  int locationOfOnDuplicateKeyUpdate = -1;
  
  String valuesClause;
  
  boolean parametersInDuplicateKeyClause = false;
  
  String charEncoding;
  
  private ParseInfo batchHead;
  
  private ParseInfo batchValues;
  
  private ParseInfo batchODKUClause;
  
  private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos) {
    this.firstStmtChar = firstStmtChar;
    this.foundLoadData = foundLoadData;
    this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
    this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
    this.statementLength = statementLength;
    this.statementStartPos = statementStartPos;
    this.staticSql = staticSql;
  }
  
  public ParseInfo(String sql, Session session, String encoding) {
    this(sql, session, encoding, true);
  }
  
  public ParseInfo(String sql, Session session, String encoding, boolean buildRewriteInfo) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: aload_0
    //   5: iconst_0
    //   6: putfield firstStmtChar : C
    //   9: aload_0
    //   10: iconst_0
    //   11: putfield foundLoadData : Z
    //   14: aload_0
    //   15: lconst_0
    //   16: putfield lastUsed : J
    //   19: aload_0
    //   20: iconst_0
    //   21: putfield statementLength : I
    //   24: aload_0
    //   25: iconst_0
    //   26: putfield statementStartPos : I
    //   29: aload_0
    //   30: iconst_0
    //   31: putfield canRewriteAsMultiValueInsert : Z
    //   34: aload_0
    //   35: aconst_null
    //   36: checkcast [[B
    //   39: putfield staticSql : [[B
    //   42: aload_0
    //   43: iconst_0
    //   44: putfield hasPlaceholders : Z
    //   47: aload_0
    //   48: iconst_1
    //   49: putfield numberOfQueries : I
    //   52: aload_0
    //   53: iconst_0
    //   54: putfield isOnDuplicateKeyUpdate : Z
    //   57: aload_0
    //   58: iconst_m1
    //   59: putfield locationOfOnDuplicateKeyUpdate : I
    //   62: aload_0
    //   63: iconst_0
    //   64: putfield parametersInDuplicateKeyClause : Z
    //   67: aload_1
    //   68: ifnonnull -> 91
    //   71: ldc com/mysql/cj/exceptions/WrongArgumentException
    //   73: ldc 'PreparedStatement.61'
    //   75: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
    //   78: aload_2
    //   79: invokeinterface getExceptionInterceptor : ()Lcom/mysql/cj/exceptions/ExceptionInterceptor;
    //   84: invokestatic createException : (Ljava/lang/Class;Ljava/lang/String;Lcom/mysql/cj/exceptions/ExceptionInterceptor;)Lcom/mysql/cj/exceptions/CJException;
    //   87: checkcast com/mysql/cj/exceptions/WrongArgumentException
    //   90: athrow
    //   91: aload_0
    //   92: aload_3
    //   93: putfield charEncoding : Ljava/lang/String;
    //   96: aload_0
    //   97: invokestatic currentTimeMillis : ()J
    //   100: putfield lastUsed : J
    //   103: aload_2
    //   104: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
    //   109: astore #5
    //   111: iconst_0
    //   112: istore #6
    //   114: aload #5
    //   116: ifnull -> 145
    //   119: aload #5
    //   121: ldc ' '
    //   123: invokevirtual equals : (Ljava/lang/Object;)Z
    //   126: ifne -> 145
    //   129: aload #5
    //   131: invokevirtual length : ()I
    //   134: ifle -> 145
    //   137: aload #5
    //   139: iconst_0
    //   140: invokevirtual charAt : (I)C
    //   143: istore #6
    //   145: aload_0
    //   146: aload_1
    //   147: invokevirtual length : ()I
    //   150: putfield statementLength : I
    //   153: new java/util/ArrayList
    //   156: dup
    //   157: invokespecial <init> : ()V
    //   160: astore #7
    //   162: iconst_0
    //   163: istore #8
    //   165: iconst_0
    //   166: istore #9
    //   168: iconst_0
    //   169: istore #10
    //   171: iconst_0
    //   172: istore #11
    //   174: aload_2
    //   175: invokeinterface getServerSession : ()Lcom/mysql/cj/protocol/ServerSession;
    //   180: invokeinterface isNoBackslashEscapesSet : ()Z
    //   185: istore #13
    //   187: aload_0
    //   188: aload_1
    //   189: invokestatic findStartOfStatement : (Ljava/lang/String;)I
    //   192: putfield statementStartPos : I
    //   195: aload_0
    //   196: getfield statementStartPos : I
    //   199: istore #12
    //   201: iload #12
    //   203: aload_0
    //   204: getfield statementLength : I
    //   207: if_icmpge -> 895
    //   210: aload_1
    //   211: iload #12
    //   213: invokevirtual charAt : (I)C
    //   216: istore #14
    //   218: aload_0
    //   219: getfield firstStmtChar : C
    //   222: ifne -> 337
    //   225: iload #14
    //   227: invokestatic isLetter : (C)Z
    //   230: ifeq -> 337
    //   233: aload_0
    //   234: iload #14
    //   236: invokestatic toUpperCase : (C)C
    //   239: putfield firstStmtChar : C
    //   242: aload_0
    //   243: getfield firstStmtChar : C
    //   246: bipush #73
    //   248: if_icmpne -> 337
    //   251: aload_0
    //   252: aload_1
    //   253: aload_2
    //   254: invokeinterface getPropertySet : ()Lcom/mysql/cj/conf/PropertySet;
    //   259: getstatic com/mysql/cj/conf/PropertyKey.dontCheckOnDuplicateKeyUpdateInSQL : Lcom/mysql/cj/conf/PropertyKey;
    //   262: invokeinterface getBooleanProperty : (Lcom/mysql/cj/conf/PropertyKey;)Lcom/mysql/cj/conf/RuntimeProperty;
    //   267: invokeinterface getValue : ()Ljava/lang/Object;
    //   272: checkcast java/lang/Boolean
    //   275: invokevirtual booleanValue : ()Z
    //   278: aload_2
    //   279: invokeinterface getPropertySet : ()Lcom/mysql/cj/conf/PropertySet;
    //   284: getstatic com/mysql/cj/conf/PropertyKey.rewriteBatchedStatements : Lcom/mysql/cj/conf/PropertyKey;
    //   287: invokeinterface getBooleanProperty : (Lcom/mysql/cj/conf/PropertyKey;)Lcom/mysql/cj/conf/RuntimeProperty;
    //   292: invokeinterface getValue : ()Ljava/lang/Object;
    //   297: checkcast java/lang/Boolean
    //   300: invokevirtual booleanValue : ()Z
    //   303: aload_2
    //   304: invokeinterface getServerSession : ()Lcom/mysql/cj/protocol/ServerSession;
    //   309: invokeinterface isNoBackslashEscapesSet : ()Z
    //   314: invokestatic getOnDuplicateKeyLocation : (Ljava/lang/String;ZZZ)I
    //   317: putfield locationOfOnDuplicateKeyUpdate : I
    //   320: aload_0
    //   321: aload_0
    //   322: getfield locationOfOnDuplicateKeyUpdate : I
    //   325: iconst_m1
    //   326: if_icmpeq -> 333
    //   329: iconst_1
    //   330: goto -> 334
    //   333: iconst_0
    //   334: putfield isOnDuplicateKeyUpdate : Z
    //   337: iload #13
    //   339: ifne -> 366
    //   342: iload #14
    //   344: bipush #92
    //   346: if_icmpne -> 366
    //   349: iload #12
    //   351: aload_0
    //   352: getfield statementLength : I
    //   355: iconst_1
    //   356: isub
    //   357: if_icmpge -> 366
    //   360: iinc #12, 1
    //   363: goto -> 889
    //   366: iload #8
    //   368: ifne -> 398
    //   371: iload #6
    //   373: ifeq -> 398
    //   376: iload #14
    //   378: iload #6
    //   380: if_icmpne -> 398
    //   383: iload #10
    //   385: ifne -> 392
    //   388: iconst_1
    //   389: goto -> 393
    //   392: iconst_0
    //   393: istore #10
    //   395: goto -> 746
    //   398: iload #10
    //   400: ifne -> 746
    //   403: iload #8
    //   405: ifeq -> 516
    //   408: iload #14
    //   410: bipush #39
    //   412: if_icmpeq -> 422
    //   415: iload #14
    //   417: bipush #34
    //   419: if_icmpne -> 477
    //   422: iload #14
    //   424: iload #9
    //   426: if_icmpne -> 477
    //   429: iload #12
    //   431: aload_0
    //   432: getfield statementLength : I
    //   435: iconst_1
    //   436: isub
    //   437: if_icmpge -> 459
    //   440: aload_1
    //   441: iload #12
    //   443: iconst_1
    //   444: iadd
    //   445: invokevirtual charAt : (I)C
    //   448: iload #9
    //   450: if_icmpne -> 459
    //   453: iinc #12, 1
    //   456: goto -> 889
    //   459: iload #8
    //   461: ifne -> 468
    //   464: iconst_1
    //   465: goto -> 469
    //   468: iconst_0
    //   469: istore #8
    //   471: iconst_0
    //   472: istore #9
    //   474: goto -> 746
    //   477: iload #14
    //   479: bipush #39
    //   481: if_icmpeq -> 491
    //   484: iload #14
    //   486: bipush #34
    //   488: if_icmpne -> 746
    //   491: iload #14
    //   493: iload #9
    //   495: if_icmpne -> 746
    //   498: iload #8
    //   500: ifne -> 507
    //   503: iconst_1
    //   504: goto -> 508
    //   507: iconst_0
    //   508: istore #8
    //   510: iconst_0
    //   511: istore #9
    //   513: goto -> 746
    //   516: iload #14
    //   518: bipush #35
    //   520: if_icmpeq -> 554
    //   523: iload #14
    //   525: bipush #45
    //   527: if_icmpne -> 600
    //   530: iload #12
    //   532: iconst_1
    //   533: iadd
    //   534: aload_0
    //   535: getfield statementLength : I
    //   538: if_icmpge -> 600
    //   541: aload_1
    //   542: iload #12
    //   544: iconst_1
    //   545: iadd
    //   546: invokevirtual charAt : (I)C
    //   549: bipush #45
    //   551: if_icmpne -> 600
    //   554: aload_0
    //   555: getfield statementLength : I
    //   558: iconst_1
    //   559: isub
    //   560: istore #15
    //   562: iload #12
    //   564: iload #15
    //   566: if_icmpge -> 889
    //   569: aload_1
    //   570: iload #12
    //   572: invokevirtual charAt : (I)C
    //   575: istore #14
    //   577: iload #14
    //   579: bipush #13
    //   581: if_icmpeq -> 889
    //   584: iload #14
    //   586: bipush #10
    //   588: if_icmpne -> 594
    //   591: goto -> 889
    //   594: iinc #12, 1
    //   597: goto -> 562
    //   600: iload #14
    //   602: bipush #47
    //   604: if_icmpne -> 725
    //   607: iload #12
    //   609: iconst_1
    //   610: iadd
    //   611: aload_0
    //   612: getfield statementLength : I
    //   615: if_icmpge -> 725
    //   618: aload_1
    //   619: iload #12
    //   621: iconst_1
    //   622: iadd
    //   623: invokevirtual charAt : (I)C
    //   626: istore #15
    //   628: iload #15
    //   630: bipush #42
    //   632: if_icmpne -> 722
    //   635: iinc #12, 2
    //   638: iload #12
    //   640: istore #16
    //   642: iload #16
    //   644: aload_0
    //   645: getfield statementLength : I
    //   648: if_icmpge -> 722
    //   651: iinc #12, 1
    //   654: aload_1
    //   655: iload #16
    //   657: invokevirtual charAt : (I)C
    //   660: istore #15
    //   662: iload #15
    //   664: bipush #42
    //   666: if_icmpne -> 716
    //   669: iload #16
    //   671: iconst_1
    //   672: iadd
    //   673: aload_0
    //   674: getfield statementLength : I
    //   677: if_icmpge -> 716
    //   680: aload_1
    //   681: iload #16
    //   683: iconst_1
    //   684: iadd
    //   685: invokevirtual charAt : (I)C
    //   688: bipush #47
    //   690: if_icmpne -> 716
    //   693: iinc #12, 1
    //   696: iload #12
    //   698: aload_0
    //   699: getfield statementLength : I
    //   702: if_icmpge -> 722
    //   705: aload_1
    //   706: iload #12
    //   708: invokevirtual charAt : (I)C
    //   711: istore #14
    //   713: goto -> 722
    //   716: iinc #16, 1
    //   719: goto -> 642
    //   722: goto -> 746
    //   725: iload #14
    //   727: bipush #39
    //   729: if_icmpeq -> 739
    //   732: iload #14
    //   734: bipush #34
    //   736: if_icmpne -> 746
    //   739: iconst_1
    //   740: istore #8
    //   742: iload #14
    //   744: istore #9
    //   746: iload #8
    //   748: ifne -> 889
    //   751: iload #10
    //   753: ifne -> 889
    //   756: iload #14
    //   758: bipush #63
    //   760: if_icmpne -> 812
    //   763: aload #7
    //   765: iconst_2
    //   766: newarray int
    //   768: dup
    //   769: iconst_0
    //   770: iload #11
    //   772: iastore
    //   773: dup
    //   774: iconst_1
    //   775: iload #12
    //   777: iastore
    //   778: invokevirtual add : (Ljava/lang/Object;)Z
    //   781: pop
    //   782: iload #12
    //   784: iconst_1
    //   785: iadd
    //   786: istore #11
    //   788: aload_0
    //   789: getfield isOnDuplicateKeyUpdate : Z
    //   792: ifeq -> 889
    //   795: iload #12
    //   797: aload_0
    //   798: getfield locationOfOnDuplicateKeyUpdate : I
    //   801: if_icmple -> 889
    //   804: aload_0
    //   805: iconst_1
    //   806: putfield parametersInDuplicateKeyClause : Z
    //   809: goto -> 889
    //   812: iload #14
    //   814: bipush #59
    //   816: if_icmpne -> 889
    //   819: iload #12
    //   821: iconst_1
    //   822: iadd
    //   823: istore #15
    //   825: iload #15
    //   827: aload_0
    //   828: getfield statementLength : I
    //   831: if_icmpge -> 889
    //   834: iload #15
    //   836: aload_0
    //   837: getfield statementLength : I
    //   840: if_icmpge -> 864
    //   843: aload_1
    //   844: iload #15
    //   846: invokevirtual charAt : (I)C
    //   849: invokestatic isWhitespace : (C)Z
    //   852: ifne -> 858
    //   855: goto -> 864
    //   858: iinc #15, 1
    //   861: goto -> 834
    //   864: iload #15
    //   866: aload_0
    //   867: getfield statementLength : I
    //   870: if_icmpge -> 883
    //   873: aload_0
    //   874: dup
    //   875: getfield numberOfQueries : I
    //   878: iconst_1
    //   879: iadd
    //   880: putfield numberOfQueries : I
    //   883: iload #15
    //   885: iconst_1
    //   886: isub
    //   887: istore #12
    //   889: iinc #12, 1
    //   892: goto -> 201
    //   895: aload_0
    //   896: getfield firstStmtChar : C
    //   899: bipush #76
    //   901: if_icmpne -> 929
    //   904: aload_1
    //   905: ldc 'LOAD DATA'
    //   907: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
    //   910: ifeq -> 921
    //   913: aload_0
    //   914: iconst_1
    //   915: putfield foundLoadData : Z
    //   918: goto -> 934
    //   921: aload_0
    //   922: iconst_0
    //   923: putfield foundLoadData : Z
    //   926: goto -> 934
    //   929: aload_0
    //   930: iconst_0
    //   931: putfield foundLoadData : Z
    //   934: aload #7
    //   936: iconst_2
    //   937: newarray int
    //   939: dup
    //   940: iconst_0
    //   941: iload #11
    //   943: iastore
    //   944: dup
    //   945: iconst_1
    //   946: aload_0
    //   947: getfield statementLength : I
    //   950: iastore
    //   951: invokevirtual add : (Ljava/lang/Object;)Z
    //   954: pop
    //   955: aload_0
    //   956: aload #7
    //   958: invokevirtual size : ()I
    //   961: anewarray [B
    //   964: putfield staticSql : [[B
    //   967: aload_0
    //   968: aload_0
    //   969: getfield staticSql : [[B
    //   972: arraylength
    //   973: iconst_1
    //   974: if_icmple -> 981
    //   977: iconst_1
    //   978: goto -> 982
    //   981: iconst_0
    //   982: putfield hasPlaceholders : Z
    //   985: iconst_0
    //   986: istore #12
    //   988: iload #12
    //   990: aload_0
    //   991: getfield staticSql : [[B
    //   994: arraylength
    //   995: if_icmpge -> 1129
    //   998: aload #7
    //   1000: iload #12
    //   1002: invokevirtual get : (I)Ljava/lang/Object;
    //   1005: checkcast [I
    //   1008: astore #14
    //   1010: aload #14
    //   1012: iconst_1
    //   1013: iaload
    //   1014: istore #15
    //   1016: aload #14
    //   1018: iconst_0
    //   1019: iaload
    //   1020: istore #16
    //   1022: iload #15
    //   1024: iload #16
    //   1026: isub
    //   1027: istore #17
    //   1029: aload_0
    //   1030: getfield foundLoadData : Z
    //   1033: ifeq -> 1054
    //   1036: aload_0
    //   1037: getfield staticSql : [[B
    //   1040: iload #12
    //   1042: aload_1
    //   1043: iload #16
    //   1045: iload #17
    //   1047: invokestatic getBytes : (Ljava/lang/String;II)[B
    //   1050: aastore
    //   1051: goto -> 1123
    //   1054: aload_3
    //   1055: ifnonnull -> 1107
    //   1058: iload #17
    //   1060: newarray byte
    //   1062: astore #18
    //   1064: iconst_0
    //   1065: istore #19
    //   1067: iload #19
    //   1069: iload #17
    //   1071: if_icmpge -> 1095
    //   1074: aload #18
    //   1076: iload #19
    //   1078: aload_1
    //   1079: iload #16
    //   1081: iload #19
    //   1083: iadd
    //   1084: invokevirtual charAt : (I)C
    //   1087: i2b
    //   1088: bastore
    //   1089: iinc #19, 1
    //   1092: goto -> 1067
    //   1095: aload_0
    //   1096: getfield staticSql : [[B
    //   1099: iload #12
    //   1101: aload #18
    //   1103: aastore
    //   1104: goto -> 1123
    //   1107: aload_0
    //   1108: getfield staticSql : [[B
    //   1111: iload #12
    //   1113: aload_1
    //   1114: iload #16
    //   1116: iload #17
    //   1118: aload_3
    //   1119: invokestatic getBytes : (Ljava/lang/String;IILjava/lang/String;)[B
    //   1122: aastore
    //   1123: iinc #12, 1
    //   1126: goto -> 988
    //   1129: goto -> 1164
    //   1132: astore #5
    //   1134: ldc com/mysql/cj/exceptions/WrongArgumentException
    //   1136: ldc 'PreparedStatement.62'
    //   1138: iconst_1
    //   1139: anewarray java/lang/Object
    //   1142: dup
    //   1143: iconst_0
    //   1144: aload_1
    //   1145: aastore
    //   1146: invokestatic getString : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1149: aload #5
    //   1151: aload_2
    //   1152: invokeinterface getExceptionInterceptor : ()Lcom/mysql/cj/exceptions/ExceptionInterceptor;
    //   1157: invokestatic createException : (Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Throwable;Lcom/mysql/cj/exceptions/ExceptionInterceptor;)Lcom/mysql/cj/exceptions/CJException;
    //   1160: checkcast com/mysql/cj/exceptions/WrongArgumentException
    //   1163: athrow
    //   1164: iload #4
    //   1166: ifeq -> 1254
    //   1169: aload_0
    //   1170: aload_0
    //   1171: getfield numberOfQueries : I
    //   1174: iconst_1
    //   1175: if_icmpne -> 1208
    //   1178: aload_0
    //   1179: getfield parametersInDuplicateKeyClause : Z
    //   1182: ifne -> 1208
    //   1185: aload_1
    //   1186: aload_0
    //   1187: getfield isOnDuplicateKeyUpdate : Z
    //   1190: aload_0
    //   1191: getfield locationOfOnDuplicateKeyUpdate : I
    //   1194: aload_0
    //   1195: getfield statementStartPos : I
    //   1198: invokestatic canRewrite : (Ljava/lang/String;ZII)Z
    //   1201: ifeq -> 1208
    //   1204: iconst_1
    //   1205: goto -> 1209
    //   1208: iconst_0
    //   1209: putfield canRewriteAsMultiValueInsert : Z
    //   1212: aload_0
    //   1213: getfield canRewriteAsMultiValueInsert : Z
    //   1216: ifeq -> 1254
    //   1219: aload_2
    //   1220: invokeinterface getPropertySet : ()Lcom/mysql/cj/conf/PropertySet;
    //   1225: getstatic com/mysql/cj/conf/PropertyKey.rewriteBatchedStatements : Lcom/mysql/cj/conf/PropertyKey;
    //   1228: invokeinterface getBooleanProperty : (Lcom/mysql/cj/conf/PropertyKey;)Lcom/mysql/cj/conf/RuntimeProperty;
    //   1233: invokeinterface getValue : ()Ljava/lang/Object;
    //   1238: checkcast java/lang/Boolean
    //   1241: invokevirtual booleanValue : ()Z
    //   1244: ifeq -> 1254
    //   1247: aload_0
    //   1248: aload_1
    //   1249: aload_2
    //   1250: aload_3
    //   1251: invokespecial buildRewriteBatchedParams : (Ljava/lang/String;Lcom/mysql/cj/Session;Ljava/lang/String;)V
    //   1254: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #96	-> 0
    //   #47	-> 4
    //   #49	-> 9
    //   #51	-> 14
    //   #53	-> 19
    //   #55	-> 24
    //   #57	-> 29
    //   #59	-> 34
    //   #61	-> 42
    //   #63	-> 47
    //   #65	-> 52
    //   #67	-> 57
    //   #71	-> 62
    //   #99	-> 67
    //   #100	-> 71
    //   #101	-> 79
    //   #100	-> 84
    //   #104	-> 91
    //   #105	-> 96
    //   #107	-> 103
    //   #109	-> 111
    //   #111	-> 114
    //   #112	-> 137
    //   #115	-> 145
    //   #117	-> 153
    //   #118	-> 162
    //   #119	-> 165
    //   #120	-> 168
    //   #121	-> 171
    //   #124	-> 174
    //   #129	-> 187
    //   #131	-> 195
    //   #132	-> 210
    //   #134	-> 218
    //   #136	-> 233
    //   #139	-> 242
    //   #140	-> 251
    //   #141	-> 254
    //   #142	-> 279
    //   #143	-> 304
    //   #140	-> 314
    //   #144	-> 320
    //   #148	-> 337
    //   #149	-> 360
    //   #150	-> 363
    //   #154	-> 366
    //   #155	-> 383
    //   #156	-> 398
    //   #159	-> 403
    //   #160	-> 408
    //   #161	-> 429
    //   #162	-> 453
    //   #163	-> 456
    //   #166	-> 459
    //   #167	-> 471
    //   #168	-> 477
    //   #169	-> 498
    //   #170	-> 510
    //   #173	-> 516
    //   #175	-> 554
    //   #177	-> 562
    //   #178	-> 569
    //   #180	-> 577
    //   #181	-> 591
    //   #177	-> 594
    //   #186	-> 600
    //   #188	-> 618
    //   #190	-> 628
    //   #191	-> 635
    //   #193	-> 638
    //   #194	-> 651
    //   #195	-> 654
    //   #197	-> 662
    //   #198	-> 680
    //   #199	-> 693
    //   #201	-> 696
    //   #202	-> 705
    //   #193	-> 716
    //   #210	-> 722
    //   #211	-> 739
    //   #212	-> 742
    //   #217	-> 746
    //   #218	-> 756
    //   #219	-> 763
    //   #220	-> 782
    //   #222	-> 788
    //   #223	-> 804
    //   #225	-> 812
    //   #226	-> 819
    //   #227	-> 825
    //   #228	-> 834
    //   #229	-> 843
    //   #230	-> 855
    //   #228	-> 858
    //   #233	-> 864
    //   #234	-> 873
    //   #236	-> 883
    //   #131	-> 889
    //   #242	-> 895
    //   #243	-> 904
    //   #244	-> 913
    //   #246	-> 921
    //   #249	-> 929
    //   #252	-> 934
    //   #253	-> 955
    //   #254	-> 967
    //   #256	-> 985
    //   #257	-> 998
    //   #258	-> 1010
    //   #259	-> 1016
    //   #260	-> 1022
    //   #262	-> 1029
    //   #263	-> 1036
    //   #264	-> 1054
    //   #265	-> 1058
    //   #267	-> 1064
    //   #268	-> 1074
    //   #267	-> 1089
    //   #271	-> 1095
    //   #272	-> 1104
    //   #273	-> 1107
    //   #256	-> 1123
    //   #279	-> 1129
    //   #276	-> 1132
    //   #277	-> 1134
    //   #278	-> 1152
    //   #277	-> 1157
    //   #281	-> 1164
    //   #282	-> 1169
    //   #283	-> 1198
    //   #284	-> 1212
    //   #285	-> 1247
    //   #289	-> 1254
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   562	38	15	endOfStmt	I
    //   642	80	16	j	I
    //   628	94	15	cNext	C
    //   825	64	15	j	I
    //   218	671	14	c	C
    //   1067	28	19	j	I
    //   1064	40	18	buf	[B
    //   1010	113	14	ep	[I
    //   1016	107	15	end	I
    //   1022	101	16	begin	I
    //   1029	94	17	len	I
    //   111	1018	5	quotedIdentifierString	Ljava/lang/String;
    //   114	1015	6	quotedIdentifierChar	C
    //   162	967	7	endpointList	Ljava/util/ArrayList;
    //   165	964	8	inQuotes	Z
    //   168	961	9	quoteChar	C
    //   171	958	10	inQuotedId	Z
    //   174	955	11	lastParmEnd	I
    //   201	928	12	i	I
    //   187	942	13	noBackslashEscapes	Z
    //   1134	30	5	oobEx	Ljava/lang/StringIndexOutOfBoundsException;
    //   0	1255	0	this	Lcom/mysql/cj/ParseInfo;
    //   0	1255	1	sql	Ljava/lang/String;
    //   0	1255	2	session	Lcom/mysql/cj/Session;
    //   0	1255	3	encoding	Ljava/lang/String;
    //   0	1255	4	buildRewriteInfo	Z
    // Local variable type table:
    //   start	length	slot	name	signature
    //   162	967	7	endpointList	Ljava/util/ArrayList<[I>;
    // Exception table:
    //   from	to	target	type
    //   67	1129	1132	java/lang/StringIndexOutOfBoundsException
  }
  
  public byte[][] getStaticSql() {
    return this.staticSql;
  }
  
  public String getValuesClause() {
    return this.valuesClause;
  }
  
  public int getLocationOfOnDuplicateKeyUpdate() {
    return this.locationOfOnDuplicateKeyUpdate;
  }
  
  public boolean canRewriteAsMultiValueInsertAtSqlLevel() {
    return this.canRewriteAsMultiValueInsert;
  }
  
  public boolean containsOnDuplicateKeyUpdateInSQL() {
    return this.isOnDuplicateKeyUpdate;
  }
  
  private void buildRewriteBatchedParams(String sql, Session session, String encoding) {
    this.valuesClause = extractValuesClause(sql, session.getIdentifierQuoteString());
    String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;
    String headSql = null;
    if (this.isOnDuplicateKeyUpdate) {
      headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
    } else {
      headSql = sql;
    } 
    this.batchHead = new ParseInfo(headSql, session, encoding, false);
    this.batchValues = new ParseInfo("," + this.valuesClause, session, encoding, false);
    this.batchODKUClause = null;
    if (odkuClause != null && odkuClause.length() > 0)
      this.batchODKUClause = new ParseInfo("," + this.valuesClause + " " + odkuClause, session, encoding, false); 
  }
  
  private String extractValuesClause(String sql, String quoteCharStr) {
    int indexOfValues = -1;
    int valuesSearchStart = this.statementStartPos;
    while (indexOfValues == -1) {
      if (quoteCharStr.length() > 0) {
        indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, sql, "VALUES", quoteCharStr, quoteCharStr, StringUtils.SEARCH_MODE__MRK_COM_WS);
      } else {
        indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, sql, "VALUES");
      } 
      if (indexOfValues > 0) {
        char c = sql.charAt(indexOfValues - 1);
        if (!Character.isWhitespace(c) && c != ')' && c != '`') {
          valuesSearchStart = indexOfValues + 6;
          indexOfValues = -1;
          continue;
        } 
        c = sql.charAt(indexOfValues + 6);
        if (!Character.isWhitespace(c) && c != '(') {
          valuesSearchStart = indexOfValues + 6;
          indexOfValues = -1;
        } 
      } 
    } 
    if (indexOfValues == -1)
      return null; 
    int indexOfFirstParen = sql.indexOf('(', indexOfValues + 6);
    if (indexOfFirstParen == -1)
      return null; 
    int endOfValuesClause = this.isOnDuplicateKeyUpdate ? this.locationOfOnDuplicateKeyUpdate : sql.length();
    return sql.substring(indexOfFirstParen, endOfValuesClause);
  }
  
  public synchronized ParseInfo getParseInfoForBatch(int numBatch) {
    AppendingBatchVisitor apv = new AppendingBatchVisitor();
    buildInfoForBatch(numBatch, apv);
    ParseInfo batchParseInfo = new ParseInfo(apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);
    return batchParseInfo;
  }
  
  public String getSqlForBatch(int numBatch) throws UnsupportedEncodingException {
    ParseInfo batchInfo = getParseInfoForBatch(numBatch);
    return batchInfo.getSqlForBatch();
  }
  
  public String getSqlForBatch() throws UnsupportedEncodingException {
    int size = 0;
    byte[][] sqlStrings = this.staticSql;
    int sqlStringsLength = sqlStrings.length;
    for (int i = 0; i < sqlStringsLength; i++) {
      size += (sqlStrings[i]).length;
      size++;
    } 
    StringBuilder buf = new StringBuilder(size);
    for (int j = 0; j < sqlStringsLength - 1; j++) {
      buf.append(StringUtils.toString(sqlStrings[j], this.charEncoding));
      buf.append("?");
    } 
    buf.append(StringUtils.toString(sqlStrings[sqlStringsLength - 1]));
    return buf.toString();
  }
  
  private void buildInfoForBatch(int numBatch, BatchVisitor visitor) {
    if (!this.hasPlaceholders) {
      if (numBatch == 1) {
        visitor.append(this.staticSql[0]);
        return;
      } 
      byte[] arrayOfByte1 = this.batchHead.staticSql[0];
      visitor.append(arrayOfByte1).increment();
      int k = numBatch - 1;
      if (this.batchODKUClause != null)
        k--; 
      byte[] arrayOfByte2 = this.batchValues.staticSql[0];
      for (int m = 0; m < k; m++)
        visitor.mergeWithLast(arrayOfByte2).increment(); 
      if (this.batchODKUClause != null) {
        byte[] batchOdkuStaticSql = this.batchODKUClause.staticSql[0];
        visitor.mergeWithLast(batchOdkuStaticSql).increment();
      } 
      return;
    } 
    byte[][] headStaticSql = this.batchHead.staticSql;
    int headStaticSqlLength = headStaticSql.length;
    byte[] endOfHead = headStaticSql[headStaticSqlLength - 1];
    for (int i = 0; i < headStaticSqlLength - 1; i++)
      visitor.append(headStaticSql[i]).increment(); 
    int numValueRepeats = numBatch - 1;
    if (this.batchODKUClause != null)
      numValueRepeats--; 
    byte[][] valuesStaticSql = this.batchValues.staticSql;
    int valuesStaticSqlLength = valuesStaticSql.length;
    byte[] beginOfValues = valuesStaticSql[0];
    byte[] endOfValues = valuesStaticSql[valuesStaticSqlLength - 1];
    for (int j = 0; j < numValueRepeats; j++) {
      visitor.merge(endOfValues, beginOfValues).increment();
      for (int k = 1; k < valuesStaticSqlLength - 1; k++)
        visitor.append(valuesStaticSql[k]).increment(); 
    } 
    if (this.batchODKUClause != null) {
      byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
      int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;
      byte[] beginOfOdku = batchOdkuStaticSql[0];
      byte[] endOfOdku = batchOdkuStaticSql[batchOdkuStaticSqlLength - 1];
      if (numBatch > 1) {
        visitor.merge((numValueRepeats > 0) ? endOfValues : endOfHead, beginOfOdku).increment();
        for (int k = 1; k < batchOdkuStaticSqlLength; k++)
          visitor.append(batchOdkuStaticSql[k]).increment(); 
      } else {
        visitor.append(endOfOdku).increment();
      } 
    } else {
      visitor.append(endOfHead);
    } 
  }
  
  protected static int findStartOfStatement(String sql) {
    int statementStartPos = 0;
    if (StringUtils.startsWithIgnoreCaseAndWs(sql, "/*")) {
      statementStartPos = sql.indexOf("*/");
      if (statementStartPos == -1) {
        statementStartPos = 0;
      } else {
        statementStartPos += 2;
      } 
    } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "--") || StringUtils.startsWithIgnoreCaseAndWs(sql, "#")) {
      statementStartPos = sql.indexOf('\n');
      if (statementStartPos == -1) {
        statementStartPos = sql.indexOf('\r');
        if (statementStartPos == -1)
          statementStartPos = 0; 
      } 
    } 
    return statementStartPos;
  }
  
  public static int getOnDuplicateKeyLocation(String sql, boolean dontCheckOnDuplicateKeyUpdateInSQL, boolean rewriteBatchedStatements, boolean noBackslashEscapes) {
    return (dontCheckOnDuplicateKeyUpdateInSQL && !rewriteBatchedStatements) ? -1 : StringUtils.indexOfIgnoreCase(0, sql, ON_DUPLICATE_KEY_UPDATE_CLAUSE, "\"'`", "\"'`", noBackslashEscapes ? StringUtils.SEARCH_MODE__MRK_COM_WS : StringUtils.SEARCH_MODE__ALL);
  }
  
  protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos) {
    if (StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos)) {
      if (StringUtils.indexOfIgnoreCase(statementStartPos, sql, "SELECT", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) != -1)
        return false; 
      if (isOnDuplicateKeyUpdate) {
        int updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ");
        if (updateClausePos != -1)
          return (StringUtils.indexOfIgnoreCase(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) == -1); 
      } 
      return true;
    } 
    return (StringUtils.startsWithIgnoreCaseAndWs(sql, "REPLACE", statementStartPos) && 
      StringUtils.indexOfIgnoreCase(statementStartPos, sql, "SELECT", "\"'`", "\"'`", StringUtils.SEARCH_MODE__MRK_COM_WS) == -1);
  }
  
  public boolean isFoundLoadData() {
    return this.foundLoadData;
  }
  
  public char getFirstStmtChar() {
    return this.firstStmtChar;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\ParseInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */