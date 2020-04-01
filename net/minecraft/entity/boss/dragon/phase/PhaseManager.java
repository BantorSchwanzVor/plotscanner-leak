package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseManager {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final EntityDragon dragon;
  
  private final IPhase[] phases = new IPhase[PhaseList.getTotalPhases()];
  
  private IPhase phase;
  
  public PhaseManager(EntityDragon dragonIn) {
    this.dragon = dragonIn;
    setPhase(PhaseList.HOVER);
  }
  
  public void setPhase(PhaseList<?> phaseIn) {
    if (this.phase == null || phaseIn != this.phase.getPhaseList()) {
      if (this.phase != null)
        this.phase.removeAreaEffect(); 
      this.phase = getPhase(phaseIn);
      if (!this.dragon.world.isRemote)
        this.dragon.getDataManager().set(EntityDragon.PHASE, Integer.valueOf(phaseIn.getId())); 
      LOGGER.debug("Dragon is now in phase {} on the {}", phaseIn, this.dragon.world.isRemote ? "client" : "server");
      this.phase.initPhase();
    } 
  }
  
  public IPhase getCurrentPhase() {
    return this.phase;
  }
  
  public <T extends IPhase> T getPhase(PhaseList<T> phaseIn) {
    int i = phaseIn.getId();
    if (this.phases[i] == null)
      this.phases[i] = phaseIn.createPhase(this.dragon); 
    return (T)this.phases[i];
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\boss\dragon\phase\PhaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */