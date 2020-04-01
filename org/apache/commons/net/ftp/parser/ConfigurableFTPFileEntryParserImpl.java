package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClientConfig;

public abstract class ConfigurableFTPFileEntryParserImpl extends RegexFTPFileEntryParserImpl implements Configurable {
  private final FTPTimestampParser timestampParser;
  
  public ConfigurableFTPFileEntryParserImpl(String regex) {
    super(regex);
    this.timestampParser = new FTPTimestampParserImpl();
  }
  
  public ConfigurableFTPFileEntryParserImpl(String regex, int flags) {
    super(regex, flags);
    this.timestampParser = new FTPTimestampParserImpl();
  }
  
  public Calendar parseTimestamp(String timestampStr) throws ParseException {
    return this.timestampParser.parseTimestamp(timestampStr);
  }
  
  public void configure(FTPClientConfig config) {
    if (this.timestampParser instanceof Configurable) {
      FTPClientConfig defaultCfg = getDefaultConfiguration();
      if (config != null) {
        if (config.getDefaultDateFormatStr() == null)
          config.setDefaultDateFormatStr(defaultCfg.getDefaultDateFormatStr()); 
        if (config.getRecentDateFormatStr() == null)
          config.setRecentDateFormatStr(defaultCfg.getRecentDateFormatStr()); 
        ((Configurable)this.timestampParser).configure(config);
      } else {
        ((Configurable)this.timestampParser).configure(defaultCfg);
      } 
    } 
  }
  
  protected abstract FTPClientConfig getDefaultConfiguration();
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\parser\ConfigurableFTPFileEntryParserImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */