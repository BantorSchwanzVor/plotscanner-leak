package org.seltak.anubis.alt.ui;

public final class Alt {
  private String mask = "";
  
  private final String username;
  
  private String password;
  
  public Alt(String username, String password) {
    this(username, password, "");
  }
  
  public Alt(String username, String password, String mask) {
    this.username = username;
    this.password = password;
    this.mask = mask;
  }
  
  public String getMask() {
    return this.mask;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void setMask(String mask) {
    this.mask = mask;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\al\\ui\Alt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */