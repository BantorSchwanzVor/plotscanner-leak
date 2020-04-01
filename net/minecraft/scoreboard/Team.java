package net.minecraft.scoreboard;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.text.TextFormatting;

public abstract class Team {
  public boolean isSameTeam(@Nullable Team other) {
    if (other == null)
      return false; 
    return (this == other);
  }
  
  public abstract String getRegisteredName();
  
  public abstract String formatString(String paramString);
  
  public abstract boolean getSeeFriendlyInvisiblesEnabled();
  
  public abstract boolean getAllowFriendlyFire();
  
  public abstract EnumVisible getNameTagVisibility();
  
  public abstract TextFormatting getChatFormat();
  
  public abstract Collection<String> getMembershipCollection();
  
  public abstract EnumVisible getDeathMessageVisibility();
  
  public abstract CollisionRule getCollisionRule();
  
  public enum CollisionRule {
    ALWAYS("always", 0),
    NEVER("never", 1),
    HIDE_FOR_OTHER_TEAMS("pushOtherTeams", 2),
    HIDE_FOR_OWN_TEAM("pushOwnTeam", 3);
    
    private static final Map<String, CollisionRule> nameMap = Maps.newHashMap();
    
    public final String name;
    
    public final int id;
    
    static {
      byte b;
      int i;
      CollisionRule[] arrayOfCollisionRule;
      for (i = (arrayOfCollisionRule = values()).length, b = 0; b < i; ) {
        CollisionRule team$collisionrule = arrayOfCollisionRule[b];
        nameMap.put(team$collisionrule.name, team$collisionrule);
        b++;
      } 
    }
    
    public static String[] getNames() {
      return (String[])nameMap.keySet().toArray((Object[])new String[nameMap.size()]);
    }
    
    @Nullable
    public static CollisionRule getByName(String nameIn) {
      return nameMap.get(nameIn);
    }
    
    CollisionRule(String nameIn, int idIn) {
      this.name = nameIn;
      this.id = idIn;
    }
  }
  
  public enum EnumVisible {
    ALWAYS("always", 0),
    NEVER("never", 1),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
    
    private static final Map<String, EnumVisible> nameMap = Maps.newHashMap();
    
    public final String internalName;
    
    public final int id;
    
    static {
      byte b;
      int i;
      EnumVisible[] arrayOfEnumVisible;
      for (i = (arrayOfEnumVisible = values()).length, b = 0; b < i; ) {
        EnumVisible team$enumvisible = arrayOfEnumVisible[b];
        nameMap.put(team$enumvisible.internalName, team$enumvisible);
        b++;
      } 
    }
    
    public static String[] getNames() {
      return (String[])nameMap.keySet().toArray((Object[])new String[nameMap.size()]);
    }
    
    @Nullable
    public static EnumVisible getByName(String nameIn) {
      return nameMap.get(nameIn);
    }
    
    EnumVisible(String nameIn, int idIn) {
      this.internalName = nameIn;
      this.id = idIn;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\Team.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */