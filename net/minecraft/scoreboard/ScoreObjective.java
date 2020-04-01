package net.minecraft.scoreboard;

public class ScoreObjective {
  private final Scoreboard theScoreboard;
  
  private final String name;
  
  private final IScoreCriteria objectiveCriteria;
  
  private IScoreCriteria.EnumRenderType renderType;
  
  private String displayName;
  
  public ScoreObjective(Scoreboard theScoreboardIn, String nameIn, IScoreCriteria objectiveCriteriaIn) {
    this.theScoreboard = theScoreboardIn;
    this.name = nameIn;
    this.objectiveCriteria = objectiveCriteriaIn;
    this.displayName = nameIn;
    this.renderType = objectiveCriteriaIn.getRenderType();
  }
  
  public Scoreboard getScoreboard() {
    return this.theScoreboard;
  }
  
  public String getName() {
    return this.name;
  }
  
  public IScoreCriteria getCriteria() {
    return this.objectiveCriteria;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
  
  public void setDisplayName(String nameIn) {
    this.displayName = nameIn;
    this.theScoreboard.onObjectiveDisplayNameChanged(this);
  }
  
  public IScoreCriteria.EnumRenderType getRenderType() {
    return this.renderType;
  }
  
  public void setRenderType(IScoreCriteria.EnumRenderType type) {
    this.renderType = type;
    this.theScoreboard.onObjectiveDisplayNameChanged(this);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ScoreObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */