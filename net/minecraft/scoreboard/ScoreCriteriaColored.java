package net.minecraft.scoreboard;

import net.minecraft.util.text.TextFormatting;

public class ScoreCriteriaColored implements IScoreCriteria {
  private final String goalName;
  
  public ScoreCriteriaColored(String name, TextFormatting format) {
    this.goalName = String.valueOf(name) + format.getFriendlyName();
    IScoreCriteria.INSTANCES.put(this.goalName, this);
  }
  
  public String getName() {
    return this.goalName;
  }
  
  public boolean isReadOnly() {
    return false;
  }
  
  public IScoreCriteria.EnumRenderType getRenderType() {
    return IScoreCriteria.EnumRenderType.INTEGER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ScoreCriteriaColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */