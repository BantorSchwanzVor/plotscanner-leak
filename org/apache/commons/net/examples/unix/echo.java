package org.apache.commons.net.examples.unix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.SocketException;
import org.apache.commons.net.echo.EchoTCPClient;
import org.apache.commons.net.echo.EchoUDPClient;

public final class echo {
  public static final void echoTCP(String host) throws IOException {
    EchoTCPClient client = new EchoTCPClient();
    client.setDefaultTimeout(60000);
    client.connect(host);
    System.out.println("Connected to " + host + ".");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter echoOutput = 
      new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
    BufferedReader echoInput = 
      new BufferedReader(new InputStreamReader(client.getInputStream()));
    String line;
    while ((line = input.readLine()) != null) {
      echoOutput.println(line);
      System.out.println(echoInput.readLine());
    } 
    echoOutput.close();
    echoInput.close();
    echoInput.close();
    client.disconnect();
  }
  
  public static final void echoUDP(String host) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    InetAddress address = InetAddress.getByName(host);
    EchoUDPClient client = new EchoUDPClient();
    client.open();
    client.setSoTimeout(5000);
    System.out.println("Ready to echo to " + host + ".");
    String line;
    while ((line = input.readLine()) != null) {
      byte[] data = line.getBytes();
      client.send(data, address);
      int count = 0;
      do {
        int length;
        try {
          length = client.receive(data);
        } catch (SocketException e) {
          System.err.println(
              "SocketException: Timed out and dropped packet");
          break;
        } catch (InterruptedIOException e) {
          System.err.println(
              "InterruptedIOException: Timed out and dropped packet");
          break;
        } 
        System.out.print(new String(data, 0, length));
        count += length;
      } while (count < data.length);
      System.out.println();
    } 
    client.close();
  }
  
  public static void main(String[] args) {
    if (args.length == 1) {
      try {
        echoTCP(args[0]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else if (args.length == 2 && args[0].equals("-udp")) {
      try {
        echoUDP(args[1]);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      } 
    } else {
      System.err.println("Usage: echo [-udp] <hostname>");
      System.exit(1);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\example\\unix\echo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */