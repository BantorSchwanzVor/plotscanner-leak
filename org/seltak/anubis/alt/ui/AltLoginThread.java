package org.seltak.anubis.alt.ui;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.seltak.anubis.Anubis;

public final class AltLoginThread extends Thread {
  private final String password;
  
  private String status;
  
  private final String username;
  
  private Minecraft mc = Minecraft.getMinecraft();
  
  public AltLoginThread(String username, String password) {
    super("Alt Login Thread");
    this.username = username;
    this.password = password;
    this.status = "§7" + "Waiting...";
  }
  
  private Session createSession(String username, String password) {
    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
    auth.setUsername(username);
    auth.setPassword(password);
    try {
      auth.logIn();
      return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
    } catch (AuthenticationException localAuthenticationException) {
      localAuthenticationException.printStackTrace();
      return null;
    } 
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void run() {
    if (this.password.equals("")) {
      this.mc.session = new Session(this.username, "", "", "mojang");
      this.status = "§a" + "Logged in. (" + this.username + " - offline name)";
      return;
    } 
    this.status = "§e" + "Logging in...";
    Session auth = createSession(this.username, this.password);
    if (auth == null) {
      this.status = "§c" + "Login failed!";
    } else {
      AltManager altManager = Anubis.altManager;
      AltManager.lastAlt = new Alt(this.username, this.password);
      this.status = "§a" + "Logged in. (" + auth.getUsername() + ")";
      this.mc.session = auth;
    } 
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\al\\ui\AltLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */