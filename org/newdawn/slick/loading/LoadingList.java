package org.newdawn.slick.loading;

import java.util.ArrayList;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.util.Log;

public class LoadingList {
  private static LoadingList single = new LoadingList();
  
  public static LoadingList get() {
    return single;
  }
  
  public static void setDeferredLoading(boolean loading) {
    single = new LoadingList();
    InternalTextureLoader.get().setDeferredLoading(loading);
    SoundStore.get().setDeferredLoading(loading);
  }
  
  public static boolean isDeferredLoading() {
    return InternalTextureLoader.get().isDeferredLoading();
  }
  
  private ArrayList deferred = new ArrayList();
  
  private int total;
  
  public void add(DeferredResource resource) {
    this.total++;
    this.deferred.add(resource);
  }
  
  public void remove(DeferredResource resource) {
    Log.info("Early loading of deferred resource due to req: " + resource.getDescription());
    this.total--;
    this.deferred.remove(resource);
  }
  
  public int getTotalResources() {
    return this.total;
  }
  
  public int getRemainingResources() {
    return this.deferred.size();
  }
  
  public DeferredResource getNext() {
    if (this.deferred.size() == 0)
      return null; 
    return this.deferred.remove(0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\loading\LoadingList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */