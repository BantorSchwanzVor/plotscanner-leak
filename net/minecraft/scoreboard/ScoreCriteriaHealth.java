package net.minecraft.scoreboard;

public class ScoreCriteriaHealth extends ScoreCriteria {
  public ScoreCriteriaHealth(String name) {
    super(name);
  }
  
  public boolean isReadOnly() {
    return true;
  }
  
  public IScoreCriteria.EnumRenderType getRenderType() {
    return IScoreCriteria.EnumRenderType.HEARTS;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ScoreCriteriaHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */