package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

public class Session {
  private String username;
  
  private final String playerID;
  
  private final String token;
  
  private final Type sessionType;
  
  public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn) {
    this.username = usernameIn;
    this.playerID = playerIDIn;
    this.token = tokenIn;
    this.sessionType = Type.setSessionType(sessionTypeIn);
  }
  
  public String getSessionID() {
    return "token:" + this.token + ":" + this.playerID;
  }
  
  public String getPlayerID() {
    return this.playerID;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getToken() {
    return this.token;
  }
  
  public GameProfile getProfile() {
    try {
      UUID uuid = UUIDTypeAdapter.fromString(getPlayerID());
      return new GameProfile(uuid, getUsername());
    } catch (IllegalArgumentException var2) {
      return new GameProfile(null, getUsername());
    } 
  }
  
  public enum Type {
    LEGACY("legacy"),
    MOJANG("mojang");
    
    private static final Map<String, Type> SESSION_TYPES = Maps.newHashMap();
    
    private final String sessionType;
    
    static {
      byte b;
      int i;
      Type[] arrayOfType;
      for (i = (arrayOfType = values()).length, b = 0; b < i; ) {
        Type session$type = arrayOfType[b];
        SESSION_TYPES.put(session$type.sessionType, session$type);
        b++;
      } 
    }
    
    Type(String sessionTypeIn) {
      this.sessionType = sessionTypeIn;
    }
    
    @Nullable
    public static Type setSessionType(String sessionTypeIn) {
      return SESSION_TYPES.get(sessionTypeIn.toLowerCase(Locale.ROOT));
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\Session.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */