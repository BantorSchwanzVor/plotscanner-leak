package org.apache.commons.net.examples.unix;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.commons.net.time.TimeTCPClient;
import org.apache.commons.net.time.TimeUDPClient;

public final class rdate {
  public static final void timeTCP(String host) throws IOException {
    TimeTCPClient client = new TimeTCPClient();
    client.setDefaultTimeout(60000);
    client.connect(host);
    System.out.println(client.getDate().toString());
    client.disconnect();
  }
  
  public static final void timeUDP(String host) throws IOException {
    TimeUDPClient client = new TimeUDPClient();
    client.setDefaultTimeout(60000);
    client.open();
    System.out.println(client.getDate(InetAddress.getByName(host)).toString());
    client.close();
  }
  
  public static void main(String[] args) {
    if (args.length == 1) {
      try {
        timeTCP(args[0]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else if (args.length == 2 && args[0].equals("-udp")) {
      try {
        timeUDP(args[1]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else {
      System.err.println("Usage: rdate [-udp] <hostname>");
      System.exit(1);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\unix\rdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */