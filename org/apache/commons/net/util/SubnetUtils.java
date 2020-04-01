package org.apache.commons.net.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubnetUtils {
  private static final String IP_ADDRESS = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
  
  private static final String SLASH_FORMAT = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,2})";
  
  private static final Pattern addressPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
  
  private static final Pattern cidrPattern = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,2})");
  
  private static final int NBITS = 32;
  
  private final int netmask;
  
  private final int address;
  
  private final int network;
  
  private final int broadcast;
  
  private boolean inclusiveHostCount = false;
  
  public SubnetUtils(String cidrNotation) {
    Matcher matcher = cidrPattern.matcher(cidrNotation);
    if (matcher.matches()) {
      this.address = matchAddress(matcher);
      int trailingZeroes = 32 - rangeCheck(Integer.parseInt(matcher.group(5)), 0, 32);
      this.netmask = (int)(4294967295L << trailingZeroes);
      this.network = this.address & this.netmask;
      this.broadcast = this.network | this.netmask ^ 0xFFFFFFFF;
    } else {
      throw new IllegalArgumentException("Could not parse [" + cidrNotation + "]");
    } 
  }
  
  public SubnetUtils(String address, String mask) {
    this.address = toInteger(address);
    this.netmask = toInteger(mask);
    if ((this.netmask & -this.netmask) - 1 != (this.netmask ^ 0xFFFFFFFF))
      throw new IllegalArgumentException("Could not parse [" + mask + "]"); 
    this.network = this.address & this.netmask;
    this.broadcast = this.network | this.netmask ^ 0xFFFFFFFF;
  }
  
  public boolean isInclusiveHostCount() {
    return this.inclusiveHostCount;
  }
  
  public void setInclusiveHostCount(boolean inclusiveHostCount) {
    this.inclusiveHostCount = inclusiveHostCount;
  }
  
  public final class SubnetInfo {
    private static final long UNSIGNED_INT_MASK = 4294967295L;
    
    private SubnetInfo() {}
    
    private long networkLong() {
      return SubnetUtils.this.network & 0xFFFFFFFFL;
    }
    
    private long broadcastLong() {
      return SubnetUtils.this.broadcast & 0xFFFFFFFFL;
    }
    
    private int low() {
      return SubnetUtils.this.isInclusiveHostCount() ? SubnetUtils.this.network : (
        (broadcastLong() - networkLong() > 1L) ? (SubnetUtils.this.network + 1) : 0);
    }
    
    private int high() {
      return SubnetUtils.this.isInclusiveHostCount() ? SubnetUtils.this.broadcast : (
        (broadcastLong() - networkLong() > 1L) ? (SubnetUtils.this.broadcast - 1) : 0);
    }
    
    public boolean isInRange(String address) {
      return isInRange(SubnetUtils.toInteger(address));
    }
    
    public boolean isInRange(int address) {
      if (address == 0)
        return false; 
      long addLong = address & 0xFFFFFFFFL;
      long lowLong = low() & 0xFFFFFFFFL;
      long highLong = high() & 0xFFFFFFFFL;
      return (addLong >= lowLong && addLong <= highLong);
    }
    
    public String getBroadcastAddress() {
      return SubnetUtils.this.format(SubnetUtils.this.toArray(SubnetUtils.this.broadcast));
    }
    
    public String getNetworkAddress() {
      return SubnetUtils.this.format(SubnetUtils.this.toArray(SubnetUtils.this.network));
    }
    
    public String getNetmask() {
      return SubnetUtils.this.format(SubnetUtils.this.toArray(SubnetUtils.this.netmask));
    }
    
    public String getAddress() {
      return SubnetUtils.this.format(SubnetUtils.this.toArray(SubnetUtils.this.address));
    }
    
    public String getLowAddress() {
      return SubnetUtils.this.format(SubnetUtils.this.toArray(low()));
    }
    
    public String getHighAddress() {
      return SubnetUtils.this.format(SubnetUtils.this.toArray(high()));
    }
    
    @Deprecated
    public int getAddressCount() {
      long countLong = getAddressCountLong();
      if (countLong > 2147483647L)
        throw new RuntimeException("Count is larger than an integer: " + countLong); 
      return (int)countLong;
    }
    
    public long getAddressCountLong() {
      long b = broadcastLong();
      long n = networkLong();
      long count = b - n + (SubnetUtils.this.isInclusiveHostCount() ? 1L : -1L);
      return (count < 0L) ? 0L : count;
    }
    
    public int asInteger(String address) {
      return SubnetUtils.toInteger(address);
    }
    
    public String getCidrSignature() {
      return String.valueOf(SubnetUtils.this.format(SubnetUtils.this.toArray(SubnetUtils.this.address))) + "/" + SubnetUtils.this.pop(SubnetUtils.this.netmask);
    }
    
    public String[] getAllAddresses() {
      int ct = getAddressCount();
      String[] addresses = new String[ct];
      if (ct == 0)
        return addresses; 
      for (int add = low(), j = 0; add <= high(); add++, j++)
        addresses[j] = SubnetUtils.this.format(SubnetUtils.this.toArray(add)); 
      return addresses;
    }
    
    public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append("CIDR Signature:\t[").append(getCidrSignature()).append("]")
        .append(" Netmask: [").append(getNetmask()).append("]\n")
        .append("Network:\t[").append(getNetworkAddress()).append("]\n")
        .append("Broadcast:\t[").append(getBroadcastAddress()).append("]\n")
        .append("First Address:\t[").append(getLowAddress()).append("]\n")
        .append("Last Address:\t[").append(getHighAddress()).append("]\n")
        .append("# Addresses:\t[").append(getAddressCount()).append("]\n");
      return buf.toString();
    }
  }
  
  public final SubnetInfo getInfo() {
    return new SubnetInfo(null);
  }
  
  private static int toInteger(String address) {
    Matcher matcher = addressPattern.matcher(address);
    if (matcher.matches())
      return matchAddress(matcher); 
    throw new IllegalArgumentException("Could not parse [" + address + "]");
  }
  
  private static int matchAddress(Matcher matcher) {
    int addr = 0;
    for (int i = 1; i <= 4; i++) {
      int n = rangeCheck(Integer.parseInt(matcher.group(i)), 0, 255);
      addr |= (n & 0xFF) << 8 * (4 - i);
    } 
    return addr;
  }
  
  private int[] toArray(int val) {
    int[] ret = new int[4];
    for (int j = 3; j >= 0; j--)
      ret[j] = ret[j] | val >>> 8 * (3 - j) & 0xFF; 
    return ret;
  }
  
  private String format(int[] octets) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < octets.length; i++) {
      str.append(octets[i]);
      if (i != octets.length - 1)
        str.append("."); 
    } 
    return str.toString();
  }
  
  private static int rangeCheck(int value, int begin, int end) {
    if (value >= begin && value <= end)
      return value; 
    throw new IllegalArgumentException("Value [" + value + "] not in range [" + begin + "," + end + "]");
  }
  
  int pop(int x) {
    x -= x >>> 1 & 0x55555555;
    x = (x & 0x33333333) + (x >>> 2 & 0x33333333);
    x = x + (x >>> 4) & 0xF0F0F0F;
    x += x >>> 8;
    x += x >>> 16;
    return x & 0x3F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\ne\\util\SubnetUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */