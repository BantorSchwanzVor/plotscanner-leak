package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class UnixFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  static final String DEFAULT_DATE_FORMAT = "MMM d yyyy";
  
  static final String DEFAULT_RECENT_DATE_FORMAT = "MMM d HH:mm";
  
  static final String NUMERIC_DATE_FORMAT = "yyyy-MM-dd HH:mm";
  
  private static final String JA_MONTH = "月";
  
  private static final String JA_DAY = "日";
  
  private static final String JA_YEAR = "年";
  
  private static final String DEFAULT_DATE_FORMAT_JA = "M'月' d'日' yyyy'年'";
  
  private static final String DEFAULT_RECENT_DATE_FORMAT_JA = "M'月' d'日' HH:mm";
  
  public static final FTPClientConfig NUMERIC_DATE_CONFIG = new FTPClientConfig(
      "UNIX", 
      "yyyy-MM-dd HH:mm", 
      null);
  
  private static final String REGEX = "([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s*(\\d+)\\s+(?:(\\S+(?:\\s\\S+)*?)\\s+)?(?:(\\S+(?:\\s\\S+)*)\\s+)?(\\d+(?:,\\s*\\d+)?)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3})|(?:\\d{1,2}月\\s+\\d{1,2}日))\\s+((?:\\d+(?::\\d+)?)|(?:\\d{4}年))\\s(.*)";
  
  final boolean trimLeadingSpaces;
  
  public UnixFTPEntryParser() {
    this((FTPClientConfig)null);
  }
  
  public UnixFTPEntryParser(FTPClientConfig config) {
    this(config, false);
  }
  
  public UnixFTPEntryParser(FTPClientConfig config, boolean trimLeadingSpaces) {
    super("([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s*(\\d+)\\s+(?:(\\S+(?:\\s\\S+)*?)\\s+)?(?:(\\S+(?:\\s\\S+)*)\\s+)?(\\d+(?:,\\s*\\d+)?)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3})|(?:\\d{1,2}月\\s+\\d{1,2}日))\\s+((?:\\d+(?::\\d+)?)|(?:\\d{4}年))\\s(.*)");
    configure(config);
    this.trimLeadingSpaces = trimLeadingSpaces;
  }
  
  public List<String> preParse(List<String> original) {
    ListIterator<String> iter = original.listIterator();
    while (iter.hasNext()) {
      String entry = iter.next();
      if (entry.matches("^total \\d+$"))
        iter.remove(); 
    } 
    return original;
  }
  
  public FTPFile parseFTPEntry(String entry) {
    FTPFile file = new FTPFile();
    file.setRawListing(entry);
    boolean isDevice = false;
    if (matches(entry)) {
      int type;
      String typeStr = group(1);
      String hardLinkCount = group(15);
      String usr = group(16);
      String grp = group(17);
      String filesize = group(18);
      String datestr = String.valueOf(group(19)) + " " + group(20);
      String name = group(21);
      if (this.trimLeadingSpaces)
        name = name.replaceFirst("^\\s+", ""); 
      try {
        if (group(19).contains("月")) {
          FTPTimestampParserImpl jaParser = new FTPTimestampParserImpl();
          jaParser.configure(new FTPClientConfig(
                "UNIX", "M'月' d'日' yyyy'年'", "M'月' d'日' HH:mm"));
          file.setTimestamp(jaParser.parseTimestamp(datestr));
        } else {
          file.setTimestamp(parseTimestamp(datestr));
        } 
      } catch (ParseException parseException) {}
      switch (typeStr.charAt(0)) {
        case 'd':
          type = 1;
          break;
        case 'e':
          type = 2;
          break;
        case 'l':
          type = 2;
          break;
        case 'b':
        case 'c':
          isDevice = true;
          type = 0;
          break;
        case '-':
        case 'f':
          type = 0;
          break;
        default:
          type = 3;
          break;
      } 
      file.setType(type);
      int g = 4;
      for (int access = 0; access < 3; access++, g += 4) {
        file.setPermission(access, 0, 
            !group(g).equals("-"));
        file.setPermission(access, 1, 
            !group(g + 1).equals("-"));
        String execPerm = group(g + 2);
        if (!execPerm.equals("-") && !Character.isUpperCase(execPerm.charAt(0))) {
          file.setPermission(access, 2, true);
        } else {
          file.setPermission(access, 2, false);
        } 
      } 
      if (!isDevice)
        try {
          file.setHardLinkCount(Integer.parseInt(hardLinkCount));
        } catch (NumberFormatException numberFormatException) {} 
      file.setUser(usr);
      file.setGroup(grp);
      try {
        file.setSize(Long.parseLong(filesize));
      } catch (NumberFormatException numberFormatException) {}
      if (type == 2) {
        int end = name.indexOf(" -> ");
        if (end == -1) {
          file.setName(name);
        } else {
          file.setName(name.substring(0, end));
          file.setLink(name.substring(end + 4));
        } 
      } else {
        file.setName(name);
      } 
      return file;
    } 
    return null;
  }
  
  protected FTPClientConfig getDefaultConfiguration() {
    return new FTPClientConfig(
        "UNIX", 
        "MMM d yyyy", 
        "MMM d HH:mm");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\UnixFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */