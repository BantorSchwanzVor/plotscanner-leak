package net.minecraft.scoreboard;

public class ScoreCriteria implements IScoreCriteria {
  private final String dummyName;
  
  public ScoreCriteria(String name) {
    this.dummyName = name;
    IScoreCriteria.INSTANCES.put(name, this);
  }
  
  public String getName() {
    return this.dummyName;
  }
  
  public boolean isReadOnly() {
    return false;
  }
  
  public IScoreCriteria.EnumRenderType getRenderType() {
    return IScoreCriteria.EnumRenderType.INTEGER;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ScoreCriteria.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */