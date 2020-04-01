package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.util.text.TextFormatting;

public class ScorePlayerTeam extends Team {
  private final Scoreboard theScoreboard;
  
  private final String registeredName;
  
  private final Set<String> membershipSet = Sets.newHashSet();
  
  private String teamNameSPT;
  
  private String namePrefixSPT = "";
  
  private String colorSuffix = "";
  
  private boolean allowFriendlyFire = true;
  
  private boolean canSeeFriendlyInvisibles = true;
  
  private Team.EnumVisible nameTagVisibility = Team.EnumVisible.ALWAYS;
  
  private Team.EnumVisible deathMessageVisibility = Team.EnumVisible.ALWAYS;
  
  private TextFormatting chatFormat = TextFormatting.RESET;
  
  private Team.CollisionRule collisionRule = Team.CollisionRule.ALWAYS;
  
  public ScorePlayerTeam(Scoreboard theScoreboardIn, String name) {
    this.theScoreboard = theScoreboardIn;
    this.registeredName = name;
    this.teamNameSPT = name;
  }
  
  public String getRegisteredName() {
    return this.registeredName;
  }
  
  public String getTeamName() {
    return this.teamNameSPT;
  }
  
  public void setTeamName(String name) {
    if (name == null)
      throw new IllegalArgumentException("Name cannot be null"); 
    this.teamNameSPT = name;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public Collection<String> getMembershipCollection() {
    return this.membershipSet;
  }
  
  public String getColorPrefix() {
    return this.namePrefixSPT;
  }
  
  public void setNamePrefix(String prefix) {
    if (prefix == null)
      throw new IllegalArgumentException("Prefix cannot be null"); 
    this.namePrefixSPT = prefix;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public String getColorSuffix() {
    return this.colorSuffix;
  }
  
  public void setNameSuffix(String suffix) {
    this.colorSuffix = suffix;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public String formatString(String input) {
    return String.valueOf(getColorPrefix()) + input + getColorSuffix();
  }
  
  public static String formatPlayerName(@Nullable Team teamIn, String string) {
    return (teamIn == null) ? string : teamIn.formatString(string);
  }
  
  public boolean getAllowFriendlyFire() {
    return this.allowFriendlyFire;
  }
  
  public void setAllowFriendlyFire(boolean friendlyFire) {
    this.allowFriendlyFire = friendlyFire;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public boolean getSeeFriendlyInvisiblesEnabled() {
    return this.canSeeFriendlyInvisibles;
  }
  
  public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles) {
    this.canSeeFriendlyInvisibles = friendlyInvisibles;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public Team.EnumVisible getNameTagVisibility() {
    return this.nameTagVisibility;
  }
  
  public Team.EnumVisible getDeathMessageVisibility() {
    return this.deathMessageVisibility;
  }
  
  public void setNameTagVisibility(Team.EnumVisible visibility) {
    this.nameTagVisibility = visibility;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public void setDeathMessageVisibility(Team.EnumVisible visibility) {
    this.deathMessageVisibility = visibility;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public Team.CollisionRule getCollisionRule() {
    return this.collisionRule;
  }
  
  public void setCollisionRule(Team.CollisionRule rule) {
    this.collisionRule = rule;
    this.theScoreboard.broadcastTeamInfoUpdate(this);
  }
  
  public int getFriendlyFlags() {
    int i = 0;
    if (getAllowFriendlyFire())
      i |= 0x1; 
    if (getSeeFriendlyInvisiblesEnabled())
      i |= 0x2; 
    return i;
  }
  
  public void setFriendlyFlags(int flags) {
    setAllowFriendlyFire(((flags & 0x1) > 0));
    setSeeFriendlyInvisiblesEnabled(((flags & 0x2) > 0));
  }
  
  public void setChatFormat(TextFormatting format) {
    this.chatFormat = format;
  }
  
  public TextFormatting getChatFormat() {
    return this.chatFormat;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ScorePlayerTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */