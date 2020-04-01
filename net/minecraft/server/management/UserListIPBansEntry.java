package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;

public class UserListIPBansEntry extends UserListEntryBan<String> {
  public UserListIPBansEntry(String valueIn) {
    this(valueIn, null, null, null, null);
  }
  
  public UserListIPBansEntry(String valueIn, Date startDate, String banner, Date endDate, String banReason) {
    super(valueIn, startDate, banner, endDate, banReason);
  }
  
  public UserListIPBansEntry(JsonObject json) {
    super(getIPFromJson(json), json);
  }
  
  private static String getIPFromJson(JsonObject json) {
    return json.has("ip") ? json.get("ip").getAsString() : null;
  }
  
  protected void onSerialization(JsonObject data) {
    if (getValue() != null) {
      data.addProperty("ip", getValue());
      super.onSerialization(data);
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\management\UserListIPBansEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */