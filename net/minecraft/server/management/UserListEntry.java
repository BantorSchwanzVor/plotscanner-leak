package net.minecraft.server.management;

import com.google.gson.JsonObject;

public class UserListEntry<T> {
  private final T value;
  
  public UserListEntry(T valueIn) {
    this.value = valueIn;
  }
  
  protected UserListEntry(T valueIn, JsonObject json) {
    this.value = valueIn;
  }
  
  T getValue() {
    return this.value;
  }
  
  boolean hasBanExpired() {
    return false;
  }
  
  protected void onSerialization(JsonObject data) {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\management\UserListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */