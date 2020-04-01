package com.mysql.cj.jdbc.ha;

import com.mysql.cj.Messages;
import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.exceptions.SQLError;
import java.lang.reflect.InvocationHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomBalanceStrategy implements BalanceStrategy {
  public ConnectionImpl pickConnection(InvocationHandler proxy, List<String> configuredHosts, Map<String, JdbcConnection> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
    int numHosts = configuredHosts.size();
    SQLException ex = null;
    List<String> whiteList = new ArrayList<>(numHosts);
    whiteList.addAll(configuredHosts);
    Map<String, Long> blackList = ((LoadBalancedConnectionProxy)proxy).getGlobalBlacklist();
    whiteList.removeAll(blackList.keySet());
    Map<String, Integer> whiteListMap = getArrayIndexMap(whiteList);
    for (int attempts = 0; attempts < numRetries; ) {
      int random = (int)Math.floor(Math.random() * whiteList.size());
      if (whiteList.size() == 0)
        throw SQLError.createSQLException(Messages.getString("RandomBalanceStrategy.0"), null); 
      String hostPortSpec = whiteList.get(random);
      ConnectionImpl conn = (ConnectionImpl)liveConnections.get(hostPortSpec);
      if (conn == null)
        try {
          conn = ((LoadBalancedConnectionProxy)proxy).createConnectionForHost(hostPortSpec);
        } catch (SQLException sqlEx) {
          ex = sqlEx;
          if (((LoadBalancedConnectionProxy)proxy).shouldExceptionTriggerConnectionSwitch(sqlEx)) {
            Integer whiteListIndex = whiteListMap.get(hostPortSpec);
            if (whiteListIndex != null) {
              whiteList.remove(whiteListIndex.intValue());
              whiteListMap = getArrayIndexMap(whiteList);
            } 
            ((LoadBalancedConnectionProxy)proxy).addToGlobalBlacklist(hostPortSpec);
            if (whiteList.size() == 0) {
              attempts++;
              try {
                Thread.sleep(250L);
              } catch (InterruptedException interruptedException) {}
              whiteListMap = new HashMap<>(numHosts);
              whiteList.addAll(configuredHosts);
              blackList = ((LoadBalancedConnectionProxy)proxy).getGlobalBlacklist();
              whiteList.removeAll(blackList.keySet());
              whiteListMap = getArrayIndexMap(whiteList);
            } 
            continue;
          } 
          throw sqlEx;
        }  
      return conn;
    } 
    if (ex != null)
      throw ex; 
    return null;
  }
  
  private Map<String, Integer> getArrayIndexMap(List<String> l) {
    Map<String, Integer> m = new HashMap<>(l.size());
    for (int i = 0; i < l.size(); i++)
      m.put(l.get(i), Integer.valueOf(i)); 
    return m;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\RandomBalanceStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */