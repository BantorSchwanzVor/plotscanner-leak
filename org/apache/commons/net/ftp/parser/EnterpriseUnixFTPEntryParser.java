package org.apache.commons.net.ftp.parser;

import java.util.Calendar;
import org.apache.commons.net.ftp.FTPFile;

public class EnterpriseUnixFTPEntryParser extends RegexFTPFileEntryParserImpl {
  private static final String MONTHS = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
  
  private static final String REGEX = "(([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z]))(\\S*)\\s*(\\S+)\\s*(\\S*)\\s*(\\d*)\\s*(\\d*)\\s*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s*((?:[012]\\d*)|(?:3[01]))\\s*((\\d\\d\\d\\d)|((?:[01]\\d)|(?:2[0123])):([012345]\\d))\\s(\\S*)(\\s*.*)";
  
  public EnterpriseUnixFTPEntryParser() {
    super("(([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z])([\\-]|[A-Z]))(\\S*)\\s*(\\S+)\\s*(\\S*)\\s*(\\d*)\\s*(\\d*)\\s*(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s*((?:[012]\\d*)|(?:3[01]))\\s*((\\d\\d\\d\\d)|((?:[01]\\d)|(?:2[0123])):([012345]\\d))\\s(\\S*)(\\s*.*)");
  }
  
  public FTPFile parseFTPEntry(String entry) {
    FTPFile file = new FTPFile();
    file.setRawListing(entry);
    if (matches(entry)) {
      String usr = group(14);
      String grp = group(15);
      String filesize = group(16);
      String mo = group(17);
      String da = group(18);
      String yr = group(20);
      String hr = group(21);
      String min = group(22);
      String name = group(23);
      file.setType(0);
      file.setUser(usr);
      file.setGroup(grp);
      try {
        file.setSize(Long.parseLong(filesize));
      } catch (NumberFormatException numberFormatException) {}
      Calendar cal = Calendar.getInstance();
      cal.set(14, 0);
      cal.set(13, 0);
      cal.set(12, 0);
      cal.set(11, 0);
      int pos = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)".indexOf(mo);
      int month = pos / 4;
      try {
        int missingUnit;
        if (yr != null) {
          cal.set(1, Integer.parseInt(yr));
          missingUnit = 11;
        } else {
          missingUnit = 13;
          int year = cal.get(1);
          if (cal.get(2) < month)
            year--; 
          cal.set(1, year);
          cal.set(11, Integer.parseInt(hr));
          cal.set(12, Integer.parseInt(min));
        } 
        cal.set(2, month);
        cal.set(5, Integer.parseInt(da));
        cal.clear(missingUnit);
        file.setTimestamp(cal);
      } catch (NumberFormatException numberFormatException) {}
      file.setName(name);
      return file;
    } 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\EnterpriseUnixFTPEntryParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */