package org.apache.commons.net.examples.ntp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Impl;
import org.apache.commons.net.ntp.TimeStamp;

public class SimpleNTPServer implements Runnable {
  private int port;
  
  private volatile boolean running;
  
  private boolean started;
  
  private DatagramSocket socket;
  
  public SimpleNTPServer() {
    this(123);
  }
  
  public SimpleNTPServer(int port) {
    if (port < 0)
      throw new IllegalArgumentException(); 
    this.port = port;
  }
  
  public int getPort() {
    return this.port;
  }
  
  public boolean isRunning() {
    return this.running;
  }
  
  public boolean isStarted() {
    return this.started;
  }
  
  public void connect() throws IOException {
    if (this.socket == null) {
      this.socket = new DatagramSocket(this.port);
      if (this.port == 0)
        this.port = this.socket.getLocalPort(); 
      System.out.println("Running NTP service on port " + this.port + "/UDP");
    } 
  }
  
  public void start() throws IOException {
    if (this.socket == null)
      connect(); 
    if (!this.started) {
      this.started = true;
      (new Thread(this)).start();
    } 
  }
  
  public void run() {
    this.running = true;
    byte[] buffer = new byte[48];
    DatagramPacket request = new DatagramPacket(buffer, buffer.length);
    do {
      try {
        this.socket.receive(request);
        long rcvTime = System.currentTimeMillis();
        handlePacket(request, rcvTime);
      } catch (IOException e) {
        if (this.running)
          e.printStackTrace(); 
      } 
    } while (this.running);
  }
  
  protected void handlePacket(DatagramPacket request, long rcvTime) throws IOException {
    NtpV3Impl ntpV3Impl = new NtpV3Impl();
    ntpV3Impl.setDatagramPacket(request);
    System.out.printf("NTP packet from %s mode=%s%n", new Object[] { request.getAddress().getHostAddress(), 
          NtpUtils.getModeName(ntpV3Impl.getMode()) });
    if (ntpV3Impl.getMode() == 3) {
      NtpV3Impl ntpV3Impl1 = new NtpV3Impl();
      ntpV3Impl1.setStratum(1);
      ntpV3Impl1.setMode(4);
      ntpV3Impl1.setVersion(3);
      ntpV3Impl1.setPrecision(-20);
      ntpV3Impl1.setPoll(0);
      ntpV3Impl1.setRootDelay(62);
      ntpV3Impl1.setRootDispersion(1081);
      ntpV3Impl1.setOriginateTimeStamp(ntpV3Impl.getTransmitTimeStamp());
      ntpV3Impl1.setReceiveTimeStamp(TimeStamp.getNtpTime(rcvTime));
      ntpV3Impl1.setReferenceTime(ntpV3Impl1.getReceiveTimeStamp());
      ntpV3Impl1.setReferenceId(1279478784);
      ntpV3Impl1.setTransmitTime(TimeStamp.getNtpTime(System.currentTimeMillis()));
      DatagramPacket dp = ntpV3Impl1.getDatagramPacket();
      dp.setPort(request.getPort());
      dp.setAddress(request.getAddress());
      this.socket.send(dp);
    } 
  }
  
  public void stop() {
    this.running = false;
    if (this.socket != null) {
      this.socket.close();
      this.socket = null;
    } 
    this.started = false;
  }
  
  public static void main(String[] args) {
    int port = 123;
    if (args.length != 0)
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException nfe) {
        nfe.printStackTrace();
      }  
    SimpleNTPServer timeServer = new SimpleNTPServer(port);
    try {
      timeServer.start();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\ntp\SimpleNTPServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */