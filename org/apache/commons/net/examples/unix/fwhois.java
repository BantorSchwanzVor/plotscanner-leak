package org.apache.commons.net.examples.unix;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.net.whois.WhoisClient;

public final class fwhois {
  public static void main(String[] args) {
    String handle, host;
    InetAddress address = null;
    if (args.length != 1) {
      System.err.println("usage: fwhois handle[@<server>]");
      System.exit(1);
    } 
    int index = args[0].lastIndexOf('@');
    WhoisClient whois = new WhoisClient();
    whois.setDefaultTimeout(60000);
    if (index == -1) {
      handle = args[0];
      host = "whois.internic.net";
    } else {
      handle = args[0].substring(0, index);
      host = args[0].substring(index + 1);
    } 
    try {
      address = InetAddress.getByName(host);
      System.out.println("[" + address.getHostName() + "]");
    } catch (UnknownHostException e) {
      System.err.println("Error unknown host: " + e.getMessage());
      System.exit(1);
    } 
    try {
      whois.connect(address);
      System.out.print(whois.query(handle));
      whois.disconnect();
    } catch (IOException e) {
      System.err.println("Error I/O exception: " + e.getMessage());
      System.exit(1);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\unix\fwhois.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */