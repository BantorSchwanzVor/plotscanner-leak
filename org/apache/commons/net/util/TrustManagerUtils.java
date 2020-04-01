package org.apache.commons.net.util;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class TrustManagerUtils {
  private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[0];
  
  private static class TrustManager implements X509TrustManager {
    private final boolean checkServerValidity;
    
    TrustManager(boolean checkServerValidity) {
      this.checkServerValidity = checkServerValidity;
    }
    
    public void checkClientTrusted(X509Certificate[] certificates, String authType) {}
    
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
      if (this.checkServerValidity) {
        byte b;
        int i;
        X509Certificate[] arrayOfX509Certificate;
        for (i = (arrayOfX509Certificate = certificates).length, b = 0; b < i; ) {
          X509Certificate certificate = arrayOfX509Certificate[b];
          certificate.checkValidity();
          b++;
        } 
      } 
    }
    
    public X509Certificate[] getAcceptedIssuers() {
      return TrustManagerUtils.EMPTY_X509CERTIFICATE_ARRAY;
    }
  }
  
  private static final X509TrustManager ACCEPT_ALL = new TrustManager(false);
  
  private static final X509TrustManager CHECK_SERVER_VALIDITY = new TrustManager(true);
  
  public static X509TrustManager getAcceptAllTrustManager() {
    return ACCEPT_ALL;
  }
  
  public static X509TrustManager getValidateServerCertificateTrustManager() {
    return CHECK_SERVER_VALIDITY;
  }
  
  public static X509TrustManager getDefaultTrustManager(KeyStore keyStore) throws GeneralSecurityException {
    String defaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory instance = TrustManagerFactory.getInstance(defaultAlgorithm);
    instance.init(keyStore);
    return (X509TrustManager)instance.getTrustManagers()[0];
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\ne\\util\TrustManagerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */