package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart {
  public EntityMinecartEmpty(World worldIn) {
    super(worldIn);
  }
  
  public EntityMinecartEmpty(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  
  public static void registerFixesMinecartEmpty(DataFixer fixer) {
    EntityMinecart.registerFixesMinecart(fixer, EntityMinecartEmpty.class);
  }
  
  public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
    if (player.isSneaking())
      return false; 
    if (isBeingRidden())
      return true; 
    if (!this.world.isRemote)
      player.startRiding(this); 
    return true;
  }
  
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      if (isBeingRidden())
        removePassengers(); 
      if (getRollingAmplitude() == 0) {
        setRollingDirection(-getRollingDirection());
        setRollingAmplitude(10);
        setDamage(50.0F);
        setBeenAttacked();
      } 
    } 
  }
  
  public EntityMinecart.Type getType() {
    return EntityMinecart.Type.RIDEABLE;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityMinecartEmpty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */