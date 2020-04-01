package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;

public enum GameType {
  NOT_SET(-1, "", ""),
  SURVIVAL(0, "survival", "s"),
  CREATIVE(1, "creative", "c"),
  ADVENTURE(2, "adventure", "a"),
  SPECTATOR(3, "spectator", "sp");
  
  int id;
  
  String name;
  
  String shortName;
  
  GameType(int idIn, String nameIn, String shortNameIn) {
    this.id = idIn;
    this.name = nameIn;
    this.shortName = shortNameIn;
  }
  
  public int getID() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void configurePlayerCapabilities(PlayerCapabilities capabilities) {
    if (this == CREATIVE) {
      capabilities.allowFlying = true;
      capabilities.isCreativeMode = true;
      capabilities.disableDamage = true;
    } else if (this == SPECTATOR) {
      capabilities.allowFlying = true;
      capabilities.isCreativeMode = false;
      capabilities.disableDamage = true;
      capabilities.isFlying = true;
    } else {
      capabilities.allowFlying = false;
      capabilities.isCreativeMode = false;
      capabilities.disableDamage = false;
      capabilities.isFlying = false;
    } 
    capabilities.allowEdit = !isAdventure();
  }
  
  public boolean isAdventure() {
    return !(this != ADVENTURE && this != SPECTATOR);
  }
  
  public boolean isCreative() {
    return (this == CREATIVE);
  }
  
  public boolean isSurvivalOrAdventure() {
    return !(this != SURVIVAL && this != ADVENTURE);
  }
  
  public static GameType getByID(int idIn) {
    return parseGameTypeWithDefault(idIn, SURVIVAL);
  }
  
  public static GameType parseGameTypeWithDefault(int targetId, GameType fallback) {
    byte b;
    int i;
    GameType[] arrayOfGameType;
    for (i = (arrayOfGameType = values()).length, b = 0; b < i; ) {
      GameType gametype = arrayOfGameType[b];
      if (gametype.id == targetId)
        return gametype; 
      b++;
    } 
    return fallback;
  }
  
  public static GameType getByName(String gamemodeName) {
    return parseGameTypeWithDefault(gamemodeName, SURVIVAL);
  }
  
  public static GameType parseGameTypeWithDefault(String targetName, GameType fallback) {
    byte b;
    int i;
    GameType[] arrayOfGameType;
    for (i = (arrayOfGameType = values()).length, b = 0; b < i; ) {
      GameType gametype = arrayOfGameType[b];
      if (gametype.name.equals(targetName) || gametype.shortName.equals(targetName))
        return gametype; 
      b++;
    } 
    return fallback;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\GameType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */