package net.minecraft.world;

import java.util.Set;
import java.util.TreeMap;
import net.minecraft.nbt.NBTTagCompound;

public class GameRules {
  private final TreeMap<String, Value> theGameRules = new TreeMap<>();
  
  public GameRules() {
    addGameRule("doFireTick", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("mobGriefing", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("keepInventory", "false", ValueType.BOOLEAN_VALUE);
    addGameRule("doMobSpawning", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("doMobLoot", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("doTileDrops", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("doEntityDrops", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("commandBlockOutput", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("naturalRegeneration", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("doDaylightCycle", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("logAdminCommands", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("showDeathMessages", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("randomTickSpeed", "3", ValueType.NUMERICAL_VALUE);
    addGameRule("sendCommandFeedback", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("reducedDebugInfo", "false", ValueType.BOOLEAN_VALUE);
    addGameRule("spectatorsGenerateChunks", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("spawnRadius", "10", ValueType.NUMERICAL_VALUE);
    addGameRule("disableElytraMovementCheck", "false", ValueType.BOOLEAN_VALUE);
    addGameRule("maxEntityCramming", "24", ValueType.NUMERICAL_VALUE);
    addGameRule("doWeatherCycle", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("doLimitedCrafting", "false", ValueType.BOOLEAN_VALUE);
    addGameRule("maxCommandChainLength", "65536", ValueType.NUMERICAL_VALUE);
    addGameRule("announceAdvancements", "true", ValueType.BOOLEAN_VALUE);
    addGameRule("gameLoopFunction", "-", ValueType.FUNCTION);
  }
  
  public void addGameRule(String key, String value, ValueType type) {
    this.theGameRules.put(key, new Value(value, type));
  }
  
  public void setOrCreateGameRule(String key, String ruleValue) {
    Value gamerules$value = this.theGameRules.get(key);
    if (gamerules$value != null) {
      gamerules$value.setValue(ruleValue);
    } else {
      addGameRule(key, ruleValue, ValueType.ANY_VALUE);
    } 
  }
  
  public String getString(String name) {
    Value gamerules$value = this.theGameRules.get(name);
    return (gamerules$value != null) ? gamerules$value.getString() : "";
  }
  
  public boolean getBoolean(String name) {
    Value gamerules$value = this.theGameRules.get(name);
    return (gamerules$value != null) ? gamerules$value.getBoolean() : false;
  }
  
  public int getInt(String name) {
    Value gamerules$value = this.theGameRules.get(name);
    return (gamerules$value != null) ? gamerules$value.getInt() : 0;
  }
  
  public NBTTagCompound writeToNBT() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    for (String s : this.theGameRules.keySet()) {
      Value gamerules$value = this.theGameRules.get(s);
      nbttagcompound.setString(s, gamerules$value.getString());
    } 
    return nbttagcompound;
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    for (String s : nbt.getKeySet())
      setOrCreateGameRule(s, nbt.getString(s)); 
  }
  
  public String[] getRules() {
    Set<String> set = this.theGameRules.keySet();
    return set.<String>toArray(new String[set.size()]);
  }
  
  public boolean hasRule(String name) {
    return this.theGameRules.containsKey(name);
  }
  
  public boolean areSameType(String key, ValueType otherValue) {
    Value gamerules$value = this.theGameRules.get(key);
    return (gamerules$value != null && (gamerules$value.getType() == otherValue || otherValue == ValueType.ANY_VALUE));
  }
  
  static class Value {
    private String valueString;
    
    private boolean valueBoolean;
    
    private int valueInteger;
    
    private double valueDouble;
    
    private final GameRules.ValueType type;
    
    public Value(String value, GameRules.ValueType type) {
      this.type = type;
      setValue(value);
    }
    
    public void setValue(String value) {
      this.valueString = value;
      if (value != null) {
        if (value.equals("false")) {
          this.valueBoolean = false;
          return;
        } 
        if (value.equals("true")) {
          this.valueBoolean = true;
          return;
        } 
      } 
      this.valueBoolean = Boolean.parseBoolean(value);
      this.valueInteger = this.valueBoolean ? 1 : 0;
      try {
        this.valueInteger = Integer.parseInt(value);
      } catch (NumberFormatException numberFormatException) {}
      try {
        this.valueDouble = Double.parseDouble(value);
      } catch (NumberFormatException numberFormatException) {}
    }
    
    public String getString() {
      return this.valueString;
    }
    
    public boolean getBoolean() {
      return this.valueBoolean;
    }
    
    public int getInt() {
      return this.valueInteger;
    }
    
    public GameRules.ValueType getType() {
      return this.type;
    }
  }
  
  public enum ValueType {
    ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE, FUNCTION;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\GameRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */