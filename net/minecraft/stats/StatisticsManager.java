package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManager {
  protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newConcurrentMap();
  
  public void increaseStat(EntityPlayer player, StatBase stat, int amount) {
    unlockAchievement(player, stat, readStat(stat) + amount);
  }
  
  public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
    TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);
    if (tupleintjsonserializable == null) {
      tupleintjsonserializable = new TupleIntJsonSerializable();
      this.statsData.put(statIn, tupleintjsonserializable);
    } 
    tupleintjsonserializable.setIntegerValue(p_150873_3_);
  }
  
  public int readStat(StatBase stat) {
    TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
    return (tupleintjsonserializable == null) ? 0 : tupleintjsonserializable.getIntegerValue();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\stats\StatisticsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */