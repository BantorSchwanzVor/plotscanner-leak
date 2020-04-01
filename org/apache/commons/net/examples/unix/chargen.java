package org.apache.commons.net.examples.unix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.SocketException;
import org.apache.commons.net.chargen.CharGenTCPClient;
import org.apache.commons.net.chargen.CharGenUDPClient;

public final class chargen {
  public static final void chargenTCP(String host) throws IOException {
    int lines = 100;
    CharGenTCPClient client = new CharGenTCPClient();
    client.setDefaultTimeout(60000);
    client.connect(host);
    BufferedReader chargenInput = 
      new BufferedReader(new InputStreamReader(client.getInputStream()));
    while (lines-- > 0) {
      String line;
      if ((line = chargenInput.readLine()) == null)
        break; 
      System.out.println(line);
    } 
    chargenInput.close();
    client.disconnect();
  }
  
  public static final void chargenUDP(String host) throws IOException {
    int packets = 50;
    InetAddress address = InetAddress.getByName(host);
    CharGenUDPClient client = new CharGenUDPClient();
    client.open();
    client.setSoTimeout(5000);
    while (packets-- > 0) {
      byte[] data;
      client.send(address);
      try {
        data = client.receive();
      } catch (SocketException e) {
        System.err.println("SocketException: Timed out and dropped packet");
        continue;
      } catch (InterruptedIOException e) {
        System.err.println(
            "InterruptedIOException: Timed out and dropped packet");
        continue;
      } 
      System.out.write(data);
      System.out.flush();
    } 
    client.close();
  }
  
  public static void main(String[] args) {
    if (args.length == 1) {
      try {
        chargenTCP(args[0]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else if (args.length == 2 && args[0].equals("-udp")) {
      try {
        chargenUDP(args[1]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else {
      System.err.println("Usage: chargen [-udp] <hostname>");
      System.exit(1);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\unix\chargen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */