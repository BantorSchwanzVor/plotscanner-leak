package net.minecraft.stats;

import net.minecraft.util.text.ITextComponent;

public class StatBasic extends StatBase {
  public StatBasic(String statIdIn, ITextComponent statNameIn, IStatType typeIn) {
    super(statIdIn, statNameIn, typeIn);
  }
  
  public StatBasic(String statIdIn, ITextComponent statNameIn) {
    super(statIdIn, statNameIn);
  }
  
  public StatBase registerStat() {
    super.registerStat();
    StatList.BASIC_STATS.add(this);
    return this;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\stats\StatBasic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */