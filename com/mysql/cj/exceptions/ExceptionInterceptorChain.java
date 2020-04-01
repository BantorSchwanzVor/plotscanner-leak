package com.mysql.cj.exceptions;

import com.mysql.cj.log.Log;
import com.mysql.cj.util.Util;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ExceptionInterceptorChain implements ExceptionInterceptor {
  List<ExceptionInterceptor> interceptors;
  
  public ExceptionInterceptorChain(String interceptorClasses, Properties props, Log log) {
    this
      .interceptors = (List<ExceptionInterceptor>)Util.loadClasses(interceptorClasses, "Connection.BadExceptionInterceptor", this).stream().map(o -> o.init(props, log)).collect(Collectors.toList());
  }
  
  public void addRingZero(ExceptionInterceptor interceptor) {
    this.interceptors.add(0, interceptor);
  }
  
  public Exception interceptException(Exception sqlEx) {
    if (this.interceptors != null) {
      Iterator<ExceptionInterceptor> iter = this.interceptors.iterator();
      while (iter.hasNext())
        sqlEx = ((ExceptionInterceptor)iter.next()).interceptException(sqlEx); 
    } 
    return sqlEx;
  }
  
  public void destroy() {
    if (this.interceptors != null) {
      Iterator<ExceptionInterceptor> iter = this.interceptors.iterator();
      while (iter.hasNext())
        ((ExceptionInterceptor)iter.next()).destroy(); 
    } 
  }
  
  public ExceptionInterceptor init(Properties properties, Log log) {
    if (this.interceptors != null) {
      Iterator<ExceptionInterceptor> iter = this.interceptors.iterator();
      while (iter.hasNext())
        ((ExceptionInterceptor)iter.next()).init(properties, log); 
    } 
    return this;
  }
  
  public List<ExceptionInterceptor> getInterceptors() {
    return this.interceptors;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\com\mysql\cj\exceptions\ExceptionInterceptorChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */