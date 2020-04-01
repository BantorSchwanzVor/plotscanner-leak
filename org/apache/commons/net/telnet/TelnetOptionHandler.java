package org.apache.commons.net.telnet;

public abstract class TelnetOptionHandler {
  private int optionCode = -1;
  
  private boolean initialLocal = false;
  
  private boolean initialRemote = false;
  
  private boolean acceptLocal = false;
  
  private boolean acceptRemote = false;
  
  private boolean doFlag = false;
  
  private boolean willFlag = false;
  
  public TelnetOptionHandler(int optcode, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
    this.optionCode = optcode;
    this.initialLocal = initlocal;
    this.initialRemote = initremote;
    this.acceptLocal = acceptlocal;
    this.acceptRemote = acceptremote;
  }
  
  public int getOptionCode() {
    return this.optionCode;
  }
  
  public boolean getAcceptLocal() {
    return this.acceptLocal;
  }
  
  public boolean getAcceptRemote() {
    return this.acceptRemote;
  }
  
  public void setAcceptLocal(boolean accept) {
    this.acceptLocal = accept;
  }
  
  public void setAcceptRemote(boolean accept) {
    this.acceptRemote = accept;
  }
  
  public boolean getInitLocal() {
    return this.initialLocal;
  }
  
  public boolean getInitRemote() {
    return this.initialRemote;
  }
  
  public void setInitLocal(boolean init) {
    this.initialLocal = init;
  }
  
  public void setInitRemote(boolean init) {
    this.initialRemote = init;
  }
  
  public int[] answerSubnegotiation(int[] suboptionData, int suboptionLength) {
    return null;
  }
  
  public int[] startSubnegotiationLocal() {
    return null;
  }
  
  public int[] startSubnegotiationRemote() {
    return null;
  }
  
  boolean getWill() {
    return this.willFlag;
  }
  
  void setWill(boolean state) {
    this.willFlag = state;
  }
  
  boolean getDo() {
    return this.doFlag;
  }
  
  void setDo(boolean state) {
    this.doFlag = state;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\apache\commons\net\telnet\TelnetOptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */