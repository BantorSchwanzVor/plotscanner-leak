package com.mysql.cj.jdbc.ha;

import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.jdbc.JdbcConnection;
import java.lang.reflect.InvocationHandler;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BestResponseTimeBalanceStrategy implements BalanceStrategy {
  public ConnectionImpl pickConnection(InvocationHandler proxy, List<String> configuredHosts, Map<String, JdbcConnection> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
    Map<String, Long> blackList = ((LoadBalancedConnectionProxy)proxy).getGlobalBlacklist();
    SQLException ex = null;
    for (int attempts = 0; attempts < numRetries; ) {
      long minResponseTime = Long.MAX_VALUE;
      int bestHostIndex = 0;
      if (blackList.size() == configuredHosts.size())
        blackList = ((LoadBalancedConnectionProxy)proxy).getGlobalBlacklist(); 
      for (int i = 0; i < responseTimes.length; i++) {
        long candidateResponseTime = responseTimes[i];
        if (candidateResponseTime < minResponseTime && !blackList.containsKey(configuredHosts.get(i))) {
          if (candidateResponseTime == 0L) {
            bestHostIndex = i;
            break;
          } 
          bestHostIndex = i;
          minResponseTime = candidateResponseTime;
        } 
      } 
      String bestHost = configuredHosts.get(bestHostIndex);
      ConnectionImpl conn = (ConnectionImpl)liveConnections.get(bestHost);
      if (conn == null)
        try {
          conn = ((LoadBalancedConnectionProxy)proxy).createConnectionForHost(bestHost);
        } catch (SQLException sqlEx) {
          ex = sqlEx;
          if (((LoadBalancedConnectionProxy)proxy).shouldExceptionTriggerConnectionSwitch(sqlEx)) {
            ((LoadBalancedConnectionProxy)proxy).addToGlobalBlacklist(bestHost);
            blackList.put(bestHost, null);
            if (blackList.size() == configuredHosts.size()) {
              attempts++;
              try {
                Thread.sleep(250L);
              } catch (InterruptedException interruptedException) {}
              blackList = ((LoadBalancedConnectionProxy)proxy).getGlobalBlacklist();
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
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\BestResponseTimeBalanceStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */