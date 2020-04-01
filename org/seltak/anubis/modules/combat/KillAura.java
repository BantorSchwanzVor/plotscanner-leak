package org.seltak.anubis.modules.combat;

import de.Hero.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;

public class KillAura extends Module {
  private EntityLivingBase target;
  
  private long current;
  
  private long last;
  
  private int delay = 3;
  
  private float yaw;
  
  private float pitch;
  
  private boolean others;
  
  public KillAura() {
    super("KillAura", Category.COMBAT, 19);
  }
  
  public void setup() {
    Anubis.setmgr.rSetting(new Setting("Criticals", this, false));
    Anubis.setmgr.rSetting(new Setting("Reach", this, false));
    Anubis.setmgr.rSetting(new Setting("Attack Invisibles", this, false));
  }
  
  public void onPreUpdate() {
    super.onPreUpdate();
    this.target = getClosest(1000.0D);
    if (this.target == null || Anubis.friends.contains(this.target.getName().toLowerCase()) || (this.target != null && !Anubis.setmgr.getSettingByName("Attack Invisibles").getValBoolean() && this.target.isInvisible()))
      return; 
    updateTime();
    this.pitch = this.mc.player.rotationPitch;
    this.yaw = this.mc.player.rotationYaw;
    for (int i = 0; i < 5; i++)
      LookAt(this.target.getPosition().getX(), this.target.getPosition().getY() - 0.4D, this.target.getPosition().getZ(), (EntityPlayer)this.mc.player); 
    if (this.current - this.last > (1000 / this.delay)) {
      if (this.mc.player.onGround && Anubis.setmgr.getSettingByName("Criticals").getValBoolean())
        this.mc.player.jump(); 
      LookAt(this.target.getPosition().getX(), this.target.getPosition().getY() - 0.4D, this.target.getPosition().getZ(), (EntityPlayer)this.mc.player);
      attack((Entity)this.target);
      resetTime();
    } 
  }
  
  public void onPostUpdate() {
    super.onPostUpdate();
    if (this.target == null || Anubis.friends.contains(this.target.getName().toLowerCase()))
      return; 
    this.mc.player.rotationYaw = this.yaw;
    this.mc.player.rotationPitch = this.pitch;
  }
  
  private void attack(Entity entity) {
    this.mc.player.swingArm(EnumHand.MAIN_HAND);
    this.mc.playerController.attackEntity((EntityPlayer)this.mc.player, entity);
  }
  
  private void updateTime() {
    this.current = System.nanoTime() / 1000000L;
  }
  
  private void resetTime() {
    this.last = System.nanoTime() / 1000000L;
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  private EntityLivingBase getClosest(double range) {
    double dist = range;
    EntityLivingBase target = null;
    for (Object object : this.mc.world.loadedEntityList) {
      Entity entity = (Entity)object;
      if (entity instanceof EntityLivingBase) {
        EntityLivingBase player = (EntityLivingBase)entity;
        if (canAttack(player))
          return player; 
      } 
    } 
    return target;
  }
  
  private boolean canAttack(EntityLivingBase player) {
    return (player != this.mc.player && player.isEntityAlive() && this.mc.player.getDistanceToEntity((Entity)player) <= this.mc.playerController.getBlockReachDistance() && player.ticksExisted > 30);
  }
  
  public static void LookAt(double px, double py, double pz, EntityPlayer me) {
    double dirx = me.getPosition().getX() - px;
    double diry = me.getPosition().getY() - py;
    double dirz = me.getPosition().getZ() - pz;
    double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
    dirx /= len;
    diry /= len;
    dirz /= len;
    double pitch = Math.asin(diry);
    double yaw = Math.atan2(dirz, dirx);
    pitch = pitch * 180.0D / Math.PI;
    yaw = yaw * 180.0D / Math.PI;
    yaw += 90.0D;
    me.rotationPitch = (float)pitch;
    me.rotationYaw = (float)yaw;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\combat\KillAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */