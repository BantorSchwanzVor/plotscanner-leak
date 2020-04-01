package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;

public class CooldownTracker {
  private final Map<Item, Cooldown> cooldowns = Maps.newHashMap();
  
  private int ticks;
  
  public boolean hasCooldown(Item itemIn) {
    return (getCooldown(itemIn, 0.0F) > 0.0F);
  }
  
  public float getCooldown(Item itemIn, float partialTicks) {
    Cooldown cooldowntracker$cooldown = this.cooldowns.get(itemIn);
    if (cooldowntracker$cooldown != null) {
      float f = (cooldowntracker$cooldown.expireTicks - cooldowntracker$cooldown.createTicks);
      float f1 = cooldowntracker$cooldown.expireTicks - this.ticks + partialTicks;
      return MathHelper.clamp(f1 / f, 0.0F, 1.0F);
    } 
    return 0.0F;
  }
  
  public void tick() {
    this.ticks++;
    if (!this.cooldowns.isEmpty()) {
      Iterator<Map.Entry<Item, Cooldown>> iterator = this.cooldowns.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<Item, Cooldown> entry = iterator.next();
        if (((Cooldown)entry.getValue()).expireTicks <= this.ticks) {
          iterator.remove();
          notifyOnRemove(entry.getKey());
        } 
      } 
    } 
  }
  
  public void setCooldown(Item itemIn, int ticksIn) {
    this.cooldowns.put(itemIn, new Cooldown(this.ticks, this.ticks + ticksIn, null));
    notifyOnSet(itemIn, ticksIn);
  }
  
  public void removeCooldown(Item itemIn) {
    this.cooldowns.remove(itemIn);
    notifyOnRemove(itemIn);
  }
  
  protected void notifyOnSet(Item itemIn, int ticksIn) {}
  
  protected void notifyOnRemove(Item itemIn) {}
  
  class Cooldown {
    final int createTicks;
    
    final int expireTicks;
    
    private Cooldown(int createTicksIn, int expireTicksIn) {
      this.createTicks = createTicksIn;
      this.expireTicks = expireTicksIn;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\CooldownTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */