package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
  public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
    World world = source.getWorld();
    IPosition iposition = BlockDispenser.getDispensePosition(source);
    EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue((IProperty)BlockDispenser.FACING);
    IProjectile iprojectile = getProjectileEntity(world, iposition, stack);
    iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), (enumfacing.getFrontOffsetY() + 0.1F), enumfacing.getFrontOffsetZ(), getProjectileVelocity(), getProjectileInaccuracy());
    world.spawnEntityInWorld((Entity)iprojectile);
    stack.func_190918_g(1);
    return stack;
  }
  
  protected void playDispenseSound(IBlockSource source) {
    source.getWorld().playEvent(1002, source.getBlockPos(), 0);
  }
  
  protected abstract IProjectile getProjectileEntity(World paramWorld, IPosition paramIPosition, ItemStack paramItemStack);
  
  protected float getProjectileInaccuracy() {
    return 6.0F;
  }
  
  protected float getProjectileVelocity() {
    return 1.1F;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\dispenser\BehaviorProjectileDispense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */