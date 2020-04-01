package com.mysql.cj.conf.url;

import com.mysql.cj.Messages;
import com.mysql.cj.conf.BooleanPropertyDefinition;
import com.mysql.cj.conf.ConnectionUrl;
import com.mysql.cj.conf.ConnectionUrlParser;
import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.HostsListView;
import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.exceptions.ExceptionFactory;
import com.mysql.cj.exceptions.InvalidConnectionAttributeException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FailoverDnsSrvConnectionUrl extends ConnectionUrl {
  private static final String DEFAULT_HOST = "";
  
  private static final int DEFAULT_PORT = -1;
  
  public FailoverDnsSrvConnectionUrl(ConnectionUrlParser connStrParser, Properties info) {
    super(connStrParser, info);
    this.type = ConnectionUrl.Type.FAILOVER_DNS_SRV_CONNECTION;
    HostInfo srvHost = getMainHost();
    Map<String, String> hostProps = srvHost.getHostProperties();
    if ("".equals(srvHost.getHost()))
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.18")); 
    if (this.hosts.size() != 1)
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.19")); 
    if (srvHost.getPort() != -1)
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.22")); 
    if (hostProps.containsKey(PropertyKey.dnsSrv.getKeyName()) && 
      !BooleanPropertyDefinition.booleanFrom(PropertyKey.dnsSrv.getKeyName(), hostProps.get(PropertyKey.dnsSrv.getKeyName()), null).booleanValue())
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, 
          Messages.getString("ConnectionString.23", new Object[] { PropertyKey.dnsSrv.getKeyName() })); 
    if (hostProps.containsKey(PropertyKey.PROTOCOL.getKeyName()) && ((String)hostProps.get(PropertyKey.PROTOCOL.getKeyName())).equalsIgnoreCase("PIPE"))
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.24")); 
  }
  
  public String getDefaultHost() {
    return "";
  }
  
  public int getDefaultPort() {
    return -1;
  }
  
  public List<HostInfo> getHostsList(HostsListView view) {
    return getHostsListFromDnsSrv(getMainHost());
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\con\\url\FailoverDnsSrvConnectionUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */