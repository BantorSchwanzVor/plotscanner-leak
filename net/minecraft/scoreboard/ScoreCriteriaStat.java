package net.minecraft.scoreboard;

import net.minecraft.stats.StatBase;

public class ScoreCriteriaStat extends ScoreCriteria {
  private final StatBase stat;
  
  public ScoreCriteriaStat(StatBase statIn) {
    super(statIn.statId);
    this.stat = statIn;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\ScoreCriteriaStat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */