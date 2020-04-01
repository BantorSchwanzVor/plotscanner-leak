package org.apache.commons.net.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.net.ssl.SSLSocket;

public class SSLSocketUtils {
  public static boolean enableEndpointNameVerification(SSLSocket socket) {
    try {
      Class<?> cls = Class.forName("javax.net.ssl.SSLParameters");
      Method setEndpointIdentificationAlgorithm = cls.getDeclaredMethod("setEndpointIdentificationAlgorithm", new Class[] { String.class });
      Method getSSLParameters = SSLSocket.class.getDeclaredMethod("getSSLParameters", new Class[0]);
      Method setSSLParameters = SSLSocket.class.getDeclaredMethod("setSSLParameters", new Class[] { cls });
      if (setEndpointIdentificationAlgorithm != null && getSSLParameters != null && setSSLParameters != null) {
        Object sslParams = getSSLParameters.invoke(socket, new Object[0]);
        if (sslParams != null) {
          setEndpointIdentificationAlgorithm.invoke(sslParams, new Object[] { "HTTPS" });
          setSSLParameters.invoke(socket, new Object[] { sslParams });
          return true;
        } 
      } 
    } catch (SecurityException securityException) {
    
    } catch (ClassNotFoundException classNotFoundException) {
    
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalArgumentException illegalArgumentException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    return false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\ne\\util\SSLSocketUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */