package org.apache.commons.net.examples.unix;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.commons.net.daytime.DaytimeTCPClient;
import org.apache.commons.net.daytime.DaytimeUDPClient;

public final class daytime {
  public static final void daytimeTCP(String host) throws IOException {
    DaytimeTCPClient client = new DaytimeTCPClient();
    client.setDefaultTimeout(60000);
    client.connect(host);
    System.out.println(client.getTime().trim());
    client.disconnect();
  }
  
  public static final void daytimeUDP(String host) throws IOException {
    DaytimeUDPClient client = new DaytimeUDPClient();
    client.setDefaultTimeout(60000);
    client.open();
    System.out.println(client.getTime(
          InetAddress.getByName(host)).trim());
    client.close();
  }
  
  public static void main(String[] args) {
    if (args.length == 1) {
      try {
        daytimeTCP(args[0]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else if (args.length == 2 && args[0].equals("-udp")) {
      try {
        daytimeUDP(args[1]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else {
      System.err.println("Usage: daytime [-udp] <hostname>");
      System.exit(1);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\unix\daytime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */