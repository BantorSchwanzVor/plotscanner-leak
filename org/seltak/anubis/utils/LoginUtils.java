package org.seltak.anubis.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.net.Proxy;
import net.minecraft.util.Session;

public class LoginUtils {
  public static Session createSession(String username, String password, Proxy proxy) throws AuthenticationException {
    YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(proxy, "");
    YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service
      .createUserAuthentication(Agent.MINECRAFT);
    auth.setUsername(username);
    auth.setPassword(password);
    auth.logIn();
    return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), 
        auth.getAuthenticatedToken(), "mojang");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubi\\utils\LoginUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */