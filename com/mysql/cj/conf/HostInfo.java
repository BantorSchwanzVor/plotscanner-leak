package com.mysql.cj.conf;

import com.mysql.cj.util.StringUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HostInfo implements DatabaseUrlContainer {
  public static final int NO_PORT = -1;
  
  private static final String HOST_PORT_SEPARATOR = ":";
  
  private final DatabaseUrlContainer originalUrl;
  
  private final String host;
  
  private final int port;
  
  private final String user;
  
  private final String password;
  
  private final boolean isPasswordless;
  
  private final Map<String, String> hostProperties = new HashMap<>();
  
  public HostInfo() {
    this(null, null, -1, null, null, true, null);
  }
  
  public HostInfo(DatabaseUrlContainer url, String host, int port, String user, String password) {
    this(url, host, port, user, password, (password == null), null);
  }
  
  public HostInfo(DatabaseUrlContainer url, String host, int port, String user, String password, Map<String, String> properties) {
    this(url, host, port, user, password, (password == null), properties);
  }
  
  public HostInfo(DatabaseUrlContainer url, String host, int port, String user, String password, boolean isPasswordless, Map<String, String> properties) {
    this.originalUrl = url;
    this.host = host;
    this.port = port;
    this.user = user;
    this.password = password;
    this.isPasswordless = isPasswordless;
    if (properties != null)
      this.hostProperties.putAll(properties); 
  }
  
  public String getHost() {
    return this.host;
  }
  
  public int getPort() {
    return this.port;
  }
  
  public String getHostPortPair() {
    return this.host + ":" + this.port;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public boolean isPasswordless() {
    return this.isPasswordless;
  }
  
  public Map<String, String> getHostProperties() {
    return Collections.unmodifiableMap(this.hostProperties);
  }
  
  public String getProperty(String key) {
    return this.hostProperties.get(key);
  }
  
  public String getDatabase() {
    String database = this.hostProperties.get(PropertyKey.DBNAME.getKeyName());
    return StringUtils.isNullOrEmpty(database) ? "" : database;
  }
  
  public Properties exposeAsProperties() {
    Properties props = new Properties();
    this.hostProperties.entrySet().stream().forEach(e -> props.setProperty((String)e.getKey(), (e.getValue() == null) ? "" : (String)e.getValue()));
    props.setProperty(PropertyKey.HOST.getKeyName(), getHost());
    props.setProperty(PropertyKey.PORT.getKeyName(), String.valueOf(getPort()));
    props.setProperty(PropertyKey.USER.getKeyName(), getUser());
    props.setProperty(PropertyKey.PASSWORD.getKeyName(), getPassword());
    return props;
  }
  
  public String getDatabaseUrl() {
    return (this.originalUrl != null) ? this.originalUrl.getDatabaseUrl() : "";
  }
  
  public boolean equalHostPortPair(HostInfo hi) {
    return (((getHost() != null && getHost().equals(hi.getHost())) || (getHost() == null && hi.getHost() == null)) && getPort() == hi.getPort());
  }
  
  public String toString() {
    StringBuilder asStr = new StringBuilder(super.toString());
    asStr.append(String.format(" :: {host: \"%s\", port: %d, hostProperties: %s}", new Object[] { this.host, Integer.valueOf(this.port), this.hostProperties }));
    return asStr.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\conf\HostInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */