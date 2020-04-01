package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMooshroom extends EntityCow {
  public EntityMooshroom(World worldIn) {
    super(worldIn);
    setSize(0.9F, 1.4F);
    this.spawnableBlock = (Block)Blocks.MYCELIUM;
  }
  
  public static void registerFixesMooshroom(DataFixer fixer) {
    EntityLiving.registerFixesMob(fixer, EntityMooshroom.class);
  }
  
  public boolean processInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (itemstack.getItem() == Items.BOWL && getGrowingAge() >= 0 && !player.capabilities.isCreativeMode) {
      itemstack.func_190918_g(1);
      if (itemstack.func_190926_b()) {
        player.setHeldItem(hand, new ItemStack(Items.MUSHROOM_STEW));
      } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW))) {
        player.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
      } 
      return true;
    } 
    if (itemstack.getItem() == Items.SHEARS && getGrowingAge() >= 0) {
      setDead();
      this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (this.height / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
      if (!this.world.isRemote) {
        EntityCow entitycow = new EntityCow(this.world);
        entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        entitycow.setHealth(getHealth());
        entitycow.renderYawOffset = this.renderYawOffset;
        if (hasCustomName())
          entitycow.setCustomNameTag(getCustomNameTag()); 
        this.world.spawnEntityInWorld((Entity)entitycow);
        for (int i = 0; i < 5; i++)
          this.world.spawnEntityInWorld((Entity)new EntityItem(this.world, this.posX, this.posY + this.height, this.posZ, new ItemStack((Block)Blocks.RED_MUSHROOM))); 
        itemstack.damageItem(1, (EntityLivingBase)player);
        playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);
      } 
      return true;
    } 
    return super.processInteract(player, hand);
  }
  
  public EntityMooshroom createChild(EntityAgeable ageable) {
    return new EntityMooshroom(this.world);
  }
  
  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTableList.ENTITIES_MUSHROOM_COW;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\passive\EntityMooshroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */