package org.apache.commons.net.examples.ntp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

public final class NTPClient {
  private static final NumberFormat numberFormat = new DecimalFormat("0.00");
  
  public static void processResponse(TimeInfo info) {
    String refType;
    NtpV3Packet message = info.getMessage();
    int stratum = message.getStratum();
    if (stratum <= 0) {
      refType = "(Unspecified or Unavailable)";
    } else if (stratum == 1) {
      refType = "(Primary Reference; e.g., GPS)";
    } else {
      refType = "(Secondary Reference; e.g. via NTP or SNTP)";
    } 
    System.out.println(" Stratum: " + stratum + " " + refType);
    int version = message.getVersion();
    int li = message.getLeapIndicator();
    System.out.println(" leap=" + li + ", version=" + 
        version + ", precision=" + message.getPrecision());
    System.out.println(" mode: " + message.getModeName() + " (" + message.getMode() + ")");
    int poll = message.getPoll();
    System.out.println(" poll: " + ((poll <= 0) ? 1 : (int)Math.pow(2.0D, poll)) + 
        " seconds" + " (2 ** " + poll + ")");
    double disp = message.getRootDispersionInMillisDouble();
    System.out.println(" rootdelay=" + numberFormat.format(message.getRootDelayInMillisDouble()) + 
        ", rootdispersion(ms): " + numberFormat.format(disp));
    int refId = message.getReferenceId();
    String refAddr = NtpUtils.getHostAddress(refId);
    String refName = null;
    if (refId != 0)
      if (refAddr.equals("127.127.1.0")) {
        refName = "LOCAL";
      } else if (stratum >= 2) {
        if (!refAddr.startsWith("127.127"))
          try {
            InetAddress addr = InetAddress.getByName(refAddr);
            String name = addr.getHostName();
            if (name != null && !name.equals(refAddr))
              refName = name; 
          } catch (UnknownHostException e) {
            refName = NtpUtils.getReferenceClock(message);
          }  
      } else if (version >= 3 && (stratum == 0 || stratum == 1)) {
        refName = NtpUtils.getReferenceClock(message);
      }  
    if (refName != null && refName.length() > 1)
      refAddr = String.valueOf(refAddr) + " (" + refName + ")"; 
    System.out.println(" Reference Identifier:\t" + refAddr);
    TimeStamp refNtpTime = message.getReferenceTimeStamp();
    System.out.println(" Reference Timestamp:\t" + refNtpTime + "  " + refNtpTime.toDateString());
    TimeStamp origNtpTime = message.getOriginateTimeStamp();
    System.out.println(" Originate Timestamp:\t" + origNtpTime + "  " + origNtpTime.toDateString());
    long destTime = info.getReturnTime();
    TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
    System.out.println(" Receive Timestamp:\t" + rcvNtpTime + "  " + rcvNtpTime.toDateString());
    TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
    System.out.println(" Transmit Timestamp:\t" + xmitNtpTime + "  " + xmitNtpTime.toDateString());
    TimeStamp destNtpTime = TimeStamp.getNtpTime(destTime);
    System.out.println(" Destination Timestamp:\t" + destNtpTime + "  " + destNtpTime.toDateString());
    info.computeDetails();
    Long offsetValue = info.getOffset();
    Long delayValue = info.getDelay();
    String delay = (delayValue == null) ? "N/A" : delayValue.toString();
    String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();
    System.out.println(" Roundtrip delay(ms)=" + delay + 
        ", clock offset(ms)=" + offset);
  }
  
  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: NTPClient <hostname-or-address-list>");
      System.exit(1);
    } 
    NTPUDPClient client = new NTPUDPClient();
    client.setDefaultTimeout(10000);
    try {
      client.open();
      byte b;
      int i;
      String[] arrayOfString;
      for (i = (arrayOfString = args).length, b = 0; b < i; ) {
        String arg = arrayOfString[b];
        System.out.println();
        try {
          InetAddress hostAddr = InetAddress.getByName(arg);
          System.out.println("> " + hostAddr.getHostName() + "/" + hostAddr.getHostAddress());
          TimeInfo info = client.getTime(hostAddr);
          processResponse(info);
        } catch (IOException ioe) {
          ioe.printStackTrace();
        } 
        b++;
      } 
    } catch (SocketException e) {
      e.printStackTrace();
    } 
    client.close();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\examples\ntp\NTPClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */