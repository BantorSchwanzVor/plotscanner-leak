package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

public class MacOsPeterFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
  static final String DEFAULT_DATE_FORMAT = "MMM d yyyy";
  
  static final String DEFAULT_RECENT_DATE_FORMAT = "MMM d HH:mm";
  
  private static final String REGEX = "([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s+((folder\\s+)|((\\d+)\\s+(\\d+)\\s+))(\\d+)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3}))\\s+(\\d+(?::\\d+)?)\\s+(\\S*)(\\s*.*)";
  
  public MacOsPeterFTPEntryParser() {
    this((FTPClientConfig)null);
  }
  
  public MacOsPeterFTPEntryParser(FTPClientConfig config) {
    super("([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s+((folder\\s+)|((\\d+)\\s+(\\d+)\\s+))(\\d+)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3}))\\s+(\\d+(?::\\d+)?)\\s+(\\S*)(\\s*.*)");
    configure(config);
  }
  
  public FTPFile parseFTPEntry(String entry) {
    FTPFile file = new FTPFile();
    file.setRawListing(entry);
    boolean isDevice = false;
    if (matches(entry)) {
      int type;
      String typeStr = group(1);
      String hardLinkCount = "0";
      String usr = null;
      String grp = null;
      String filesize = group(20);
      String datestr = String.valueOf(group(21)) + " " + group(22);
      String name = group(23);
      String endtoken = group(24);
      try {
        file.setTimestamp(parseTimestamp(datestr));
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
      if (endtoken == null) {
        file.setName(name);
      } else {
        name = String.valueOf(name) + endtoken;
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\MacOsPeterFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */