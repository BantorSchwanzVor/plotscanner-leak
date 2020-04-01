package com.mysql.cj.conf.url;

import com.mysql.cj.conf.ConnectionUrl;
import com.mysql.cj.conf.ConnectionUrlParser;
import java.util.Properties;

public class SingleConnectionUrl extends ConnectionUrl {
  public SingleConnectionUrl(ConnectionUrlParser connStrParser, Properties info) {
    super(connStrParser, info);
    this.type = ConnectionUrl.Type.SINGLE_CONNECTION;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\con\\url\SingleConnectionUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */