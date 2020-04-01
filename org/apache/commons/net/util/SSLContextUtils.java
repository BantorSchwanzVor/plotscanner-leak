package org.apache.commons.net.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class SSLContextUtils {
  public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager) throws IOException {
    (new KeyManager[1])[0] = keyManager;
    (new TrustManager[1])[0] = trustManager;
    return createSSLContext(protocol, (keyManager == null) ? null : new KeyManager[1], (trustManager == null) ? null : new TrustManager[1]);
  }
  
  public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) throws IOException {
    SSLContext ctx;
    try {
      ctx = SSLContext.getInstance(protocol);
      ctx.init(keyManagers, trustManagers, null);
    } catch (GeneralSecurityException e) {
      IOException ioe = new IOException("Could not initialize SSL context");
      ioe.initCause(e);
      throw ioe;
    } 
    return ctx;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\ne\\util\SSLContextUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */