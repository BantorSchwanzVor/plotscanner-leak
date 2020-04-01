package net.optifine.entity.model.anim;

public class ModelUpdater {
  private ModelVariableUpdater[] modelVariableUpdaters;
  
  public ModelUpdater(ModelVariableUpdater[] modelVariableUpdaters) {
    this.modelVariableUpdaters = modelVariableUpdaters;
  }
  
  public void update() {
    for (int i = 0; i < this.modelVariableUpdaters.length; i++) {
      ModelVariableUpdater modelvariableupdater = this.modelVariableUpdaters[i];
      modelvariableupdater.update();
    } 
  }
  
  public boolean initialize(IModelResolver mr) {
    for (int i = 0; i < this.modelVariableUpdaters.length; i++) {
      ModelVariableUpdater modelvariableupdater = this.modelVariableUpdaters[i];
      if (!modelvariableupdater.initialize(mr))
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\ModelUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */