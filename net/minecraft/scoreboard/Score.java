package net.minecraft.scoreboard;

import java.util.Comparator;

public class Score {
  public static final Comparator<Score> SCORE_COMPARATOR = new Comparator<Score>() {
      public int compare(Score p_compare_1_, Score p_compare_2_) {
        if (p_compare_1_.getScorePoints() > p_compare_2_.getScorePoints())
          return 1; 
        return (p_compare_1_.getScorePoints() < p_compare_2_.getScorePoints()) ? -1 : p_compare_2_.getPlayerName().compareToIgnoreCase(p_compare_1_.getPlayerName());
      }
    };
  
  private final Scoreboard theScoreboard;
  
  private final ScoreObjective theScoreObjective;
  
  private final String scorePlayerName;
  
  private int scorePoints;
  
  private boolean locked;
  
  private boolean forceUpdate;
  
  public Score(Scoreboard theScoreboardIn, ScoreObjective theScoreObjectiveIn, String scorePlayerNameIn) {
    this.theScoreboard = theScoreboardIn;
    this.theScoreObjective = theScoreObjectiveIn;
    this.scorePlayerName = scorePlayerNameIn;
    this.forceUpdate = true;
  }
  
  public void increaseScore(int amount) {
    if (this.theScoreObjective.getCriteria().isReadOnly())
      throw new IllegalStateException("Cannot modify read-only score"); 
    setScorePoints(getScorePoints() + amount);
  }
  
  public void decreaseScore(int amount) {
    if (this.theScoreObjective.getCriteria().isReadOnly())
      throw new IllegalStateException("Cannot modify read-only score"); 
    setScorePoints(getScorePoints() - amount);
  }
  
  public void incrementScore() {
    if (this.theScoreObjective.getCriteria().isReadOnly())
      throw new IllegalStateException("Cannot modify read-only score"); 
    increaseScore(1);
  }
  
  public int getScorePoints() {
    return this.scorePoints;
  }
  
  public void setScorePoints(int points) {
    int i = this.scorePoints;
    this.scorePoints = points;
    if (i != points || this.forceUpdate) {
      this.forceUpdate = false;
      getScoreScoreboard().onScoreUpdated(this);
    } 
  }
  
  public ScoreObjective getObjective() {
    return this.theScoreObjective;
  }
  
  public String getPlayerName() {
    return this.scorePlayerName;
  }
  
  public Scoreboard getScoreScoreboard() {
    return this.theScoreboard;
  }
  
  public boolean isLocked() {
    return this.locked;
  }
  
  public void setLocked(boolean locked) {
    this.locked = locked;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\scoreboard\Score.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */