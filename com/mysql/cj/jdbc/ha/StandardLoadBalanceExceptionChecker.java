package com.mysql.cj.jdbc.ha;

import com.mysql.cj.conf.PropertyKey;
import com.mysql.cj.util.StringUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class StandardLoadBalanceExceptionChecker implements LoadBalanceExceptionChecker {
  private List<String> sqlStateList;
  
  private List<Class<?>> sqlExClassList;
  
  public boolean shouldExceptionTriggerFailover(Throwable ex) {
    String sqlState = (ex instanceof SQLException) ? ((SQLException)ex).getSQLState() : null;
    if (sqlState != null) {
      if (sqlState.startsWith("08"))
        return true; 
      if (this.sqlStateList != null)
        for (Iterator<String> i = this.sqlStateList.iterator(); i.hasNext();) {
          if (sqlState.startsWith(((String)i.next()).toString()))
            return true; 
        }  
    } 
    if (ex instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException || ex instanceof com.mysql.cj.exceptions.CJCommunicationsException)
      return true; 
    if (this.sqlExClassList != null)
      for (Iterator<Class<?>> i = this.sqlExClassList.iterator(); i.hasNext();) {
        if (((Class)i.next()).isInstance(ex))
          return true; 
      }  
    return false;
  }
  
  public void destroy() {}
  
  public void init(Properties props) {
    configureSQLStateList(props.getProperty(PropertyKey.loadBalanceSQLStateFailover.getKeyName(), null));
    configureSQLExceptionSubclassList(props.getProperty(PropertyKey.loadBalanceSQLExceptionSubclassFailover.getKeyName(), null));
  }
  
  private void configureSQLStateList(String sqlStates) {
    if (sqlStates == null || "".equals(sqlStates))
      return; 
    List<String> states = StringUtils.split(sqlStates, ",", true);
    List<String> newStates = new ArrayList<>();
    for (String state : states) {
      if (state.length() > 0)
        newStates.add(state); 
    } 
    if (newStates.size() > 0)
      this.sqlStateList = newStates; 
  }
  
  private void configureSQLExceptionSubclassList(String sqlExClasses) {
    if (sqlExClasses == null || "".equals(sqlExClasses))
      return; 
    List<String> classes = StringUtils.split(sqlExClasses, ",", true);
    List<Class<?>> newClasses = new ArrayList<>();
    for (String exClass : classes) {
      try {
        Class<?> c = Class.forName(exClass);
        newClasses.add(c);
      } catch (Exception exception) {}
    } 
    if (newClasses.size() > 0)
      this.sqlExClassList = newClasses; 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\jdbc\ha\StandardLoadBalanceExceptionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */