package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListOps extends UserList<GameProfile, UserListOpsEntry> {
  public UserListOps(File saveFile) {
    super(saveFile);
  }
  
  protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
    return new UserListOpsEntry(entryData);
  }
  
  public String[] getKeys() {
    String[] astring = new String[getValues().size()];
    int i = 0;
    for (UserListOpsEntry userlistopsentry : getValues().values())
      astring[i++] = userlistopsentry.getValue().getName(); 
    return astring;
  }
  
  public int getPermissionLevel(GameProfile profile) {
    UserListOpsEntry userlistopsentry = getEntry(profile);
    return (userlistopsentry != null) ? userlistopsentry.getPermissionLevel() : 0;
  }
  
  public boolean bypassesPlayerLimit(GameProfile profile) {
    UserListOpsEntry userlistopsentry = getEntry(profile);
    return (userlistopsentry != null) ? userlistopsentry.bypassesPlayerLimit() : false;
  }
  
  protected String getObjectKey(GameProfile obj) {
    return obj.getId().toString();
  }
  
  public GameProfile getGameProfileFromName(String username) {
    for (UserListOpsEntry userlistopsentry : getValues().values()) {
      if (username.equalsIgnoreCase(userlistopsentry.getValue().getName()))
        return userlistopsentry.getValue(); 
    } 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\management\UserListOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */