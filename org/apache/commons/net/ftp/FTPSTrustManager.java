package org.apache.commons.net.ftp;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

@Deprecated
public class FTPSTrustManager implements X509TrustManager {
  private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[0];
  
  public void checkClientTrusted(X509Certificate[] certificates, String authType) {}
  
  public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
    byte b;
    int i;
    X509Certificate[] arrayOfX509Certificate;
    for (i = (arrayOfX509Certificate = certificates).length, b = 0; b < i; ) {
      X509Certificate certificate = arrayOfX509Certificate[b];
      certificate.checkValidity();
      b++;
    } 
  }
  
  public X509Certificate[] getAcceptedIssuers() {
    return EMPTY_X509CERTIFICATE_ARRAY;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\ftp\FTPSTrustManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */