package org.apache.commons.net.examples.mail;

import java.io.IOException;
import java.net.URI;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.imap.IMAPSClient;

class IMAPUtils {
  static IMAPClient imapLogin(URI uri, int defaultTimeout, ProtocolCommandListener listener) throws IOException {
    IMAPClient imap;
    String userInfo = uri.getUserInfo();
    if (userInfo == null)
      throw new IllegalArgumentException("Missing userInfo details"); 
    String[] userpass = userInfo.split(":");
    if (userpass.length != 2)
      throw new IllegalArgumentException("Invalid userInfo details: '" + userInfo + "'"); 
    String username = userpass[0];
    String password = userpass[1];
    password = Utils.getPassword(username, password);
    String scheme = uri.getScheme();
    if ("imaps".equalsIgnoreCase(scheme)) {
      System.out.println("Using secure protocol");
      IMAPSClient iMAPSClient = new IMAPSClient(true);
    } else if ("imap".equalsIgnoreCase(scheme)) {
      imap = new IMAPClient();
    } else {
      throw new IllegalArgumentException("Invalid protocol: " + scheme);
    } 
    int port = uri.getPort();
    if (port != -1)
      imap.setDefaultPort(port); 
    imap.setDefaultTimeout(defaultTimeout);
    if (listener != null)
      imap.addProtocolCommandListener(listener); 
    String server = uri.getHost();
    System.out.println("Connecting to server " + server + " on " + imap.getDefaultPort());
    try {
      imap.connect(server);
      System.out.println("Successfully connected");
    } catch (IOException e) {
      throw new RuntimeException("Could not connect to server.", e);
    } 
    if (!imap.login(username, password)) {
      imap.disconnect();
      throw new RuntimeException("Could not login to server. Check login details.");
    } 
    return imap;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\mail\IMAPUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */