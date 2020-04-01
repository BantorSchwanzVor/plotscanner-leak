package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;

public class UserListWhitelist extends UserList<GameProfile, UserListWhitelistEntry> {
  public UserListWhitelist(File p_i1132_1_) {
    super(p_i1132_1_);
  }
  
  protected UserListEntry<GameProfile> createEntry(JsonObject entryData) {
    return new UserListWhitelistEntry(entryData);
  }
  
  public String[] getKeys() {
    String[] astring = new String[getValues().size()];
    int i = 0;
    for (UserListWhitelistEntry userlistwhitelistentry : getValues().values())
      astring[i++] = userlistwhitelistentry.getValue().getName(); 
    return astring;
  }
  
  protected String getObjectKey(GameProfile obj) {
    return obj.getId().toString();
  }
  
  public GameProfile getByName(String profileName) {
    for (UserListWhitelistEntry userlistwhitelistentry : getValues().values()) {
      if (profileName.equalsIgnoreCase(userlistwhitelistentry.getValue().getName()))
        return userlistwhitelistentry.getValue(); 
    } 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\management\UserListWhitelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */