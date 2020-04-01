package org.apache.commons.net.smtp;

import java.util.Enumeration;
import java.util.Vector;

public final class RelayPath {
  Vector<String> _path;
  
  String _emailAddress;
  
  public RelayPath(String emailAddress) {
    this._path = new Vector<>();
    this._emailAddress = emailAddress;
  }
  
  public void addRelay(String hostname) {
    this._path.addElement(hostname);
  }
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append('<');
    Enumeration<String> hosts = this._path.elements();
    if (hosts.hasMoreElements()) {
      buffer.append('@');
      buffer.append(hosts.nextElement());
      while (hosts.hasMoreElements()) {
        buffer.append(",@");
        buffer.append(hosts.nextElement());
      } 
      buffer.append(':');
    } 
    buffer.append(this._emailAddress);
    buffer.append('>');
    return buffer.toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\smtp\RelayPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */