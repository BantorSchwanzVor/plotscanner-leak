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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ReplicationDnsSrvConnectionUrl extends ConnectionUrl {
  private static final String DEFAULT_HOST = "";
  
  private static final int DEFAULT_PORT = -1;
  
  private static final String TYPE_MASTER = "MASTER";
  
  private static final String TYPE_SLAVE = "SLAVE";
  
  private List<HostInfo> masterHosts = new ArrayList<>();
  
  private List<HostInfo> slaveHosts = new ArrayList<>();
  
  public ReplicationDnsSrvConnectionUrl(ConnectionUrlParser connStrParser, Properties info) {
    super(connStrParser, info);
    this.type = ConnectionUrl.Type.REPLICATION_DNS_SRV_CONNECTION;
    LinkedList<HostInfo> undefinedHosts = new LinkedList<>();
    for (HostInfo hi : this.hosts) {
      Map<String, String> hostProperties = hi.getHostProperties();
      if (hostProperties.containsKey(PropertyKey.TYPE.getKeyName())) {
        if ("MASTER".equalsIgnoreCase(hostProperties.get(PropertyKey.TYPE.getKeyName()))) {
          this.masterHosts.add(hi);
          continue;
        } 
        if ("SLAVE".equalsIgnoreCase(hostProperties.get(PropertyKey.TYPE.getKeyName()))) {
          this.slaveHosts.add(hi);
          continue;
        } 
        undefinedHosts.add(hi);
        continue;
      } 
      undefinedHosts.add(hi);
    } 
    if (!undefinedHosts.isEmpty()) {
      if (this.masterHosts.isEmpty())
        this.masterHosts.add(undefinedHosts.removeFirst()); 
      this.slaveHosts.addAll(undefinedHosts);
    } 
    HostInfo srvHostMaster = this.masterHosts.isEmpty() ? null : this.masterHosts.get(0);
    Map<String, String> hostPropsMaster = (srvHostMaster == null) ? Collections.<String, String>emptyMap() : srvHostMaster.getHostProperties();
    HostInfo srvHostSlave = this.slaveHosts.isEmpty() ? null : this.slaveHosts.get(0);
    Map<String, String> hostPropsSlave = (srvHostSlave == null) ? Collections.<String, String>emptyMap() : srvHostSlave.getHostProperties();
    if (srvHostMaster == null || srvHostSlave == null || "".equals(srvHostMaster.getHost()) || "".equals(srvHostSlave.getHost()))
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.20")); 
    if (this.masterHosts.size() != 1 || this.slaveHosts.size() != 1)
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.21")); 
    if (srvHostMaster.getPort() != -1 || srvHostSlave.getPort() != -1)
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.22")); 
    if ((hostPropsMaster.containsKey(PropertyKey.dnsSrv.getKeyName()) || hostPropsSlave.containsKey(PropertyKey.dnsSrv.getKeyName())) && (
      !BooleanPropertyDefinition.booleanFrom(PropertyKey.dnsSrv.getKeyName(), hostPropsMaster.get(PropertyKey.dnsSrv.getKeyName()), null).booleanValue() || 
      !BooleanPropertyDefinition.booleanFrom(PropertyKey.dnsSrv.getKeyName(), hostPropsSlave.get(PropertyKey.dnsSrv.getKeyName()), null).booleanValue()))
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, 
          Messages.getString("ConnectionString.23", new Object[] { PropertyKey.dnsSrv.getKeyName() })); 
    if ((hostPropsMaster.containsKey(PropertyKey.PROTOCOL.getKeyName()) && ((String)hostPropsMaster.get(PropertyKey.PROTOCOL.getKeyName())).equalsIgnoreCase("PIPE")) || (hostPropsSlave
      .containsKey(PropertyKey.PROTOCOL.getKeyName()) && ((String)hostPropsSlave
      .get(PropertyKey.PROTOCOL.getKeyName())).equalsIgnoreCase("PIPE")))
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, Messages.getString("ConnectionString.24")); 
    if (hostPropsMaster.containsKey(PropertyKey.replicationConnectionGroup.getKeyName()) || hostPropsSlave
      .containsKey(PropertyKey.replicationConnectionGroup.getKeyName()))
      throw (InvalidConnectionAttributeException)ExceptionFactory.createException(InvalidConnectionAttributeException.class, 
          Messages.getString("ConnectionString.25", new Object[] { PropertyKey.replicationConnectionGroup.getKeyName() })); 
  }
  
  public String getDefaultHost() {
    return "";
  }
  
  public int getDefaultPort() {
    return -1;
  }
  
  public List<HostInfo> getHostsList(HostsListView view) {
    switch (view) {
      case MASTERS:
        return getHostsListFromDnsSrv(this.masterHosts.get(0));
      case SLAVES:
        return getHostsListFromDnsSrv(this.slaveHosts.get(0));
    } 
    return super.getHostsList(HostsListView.ALL);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\con\\url\ReplicationDnsSrvConnectionUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */