package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoe extends Item {
  private final float speed;
  
  protected Item.ToolMaterial theToolMaterial;
  
  public ItemHoe(Item.ToolMaterial material) {
    this.theToolMaterial = material;
    this.maxStackSize = 1;
    setMaxDamage(material.getMaxUses());
    setCreativeTab(CreativeTabs.TOOLS);
    this.speed = material.getDamageVsEntity() + 1.0F;
  }
  
  public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
    ItemStack itemstack = stack.getHeldItem(pos);
    if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack))
      return EnumActionResult.FAIL; 
    IBlockState iblockstate = playerIn.getBlockState(worldIn);
    Block block = iblockstate.getBlock();
    if (hand != EnumFacing.DOWN && playerIn.getBlockState(worldIn.up()).getMaterial() == Material.AIR) {
      if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
        setBlock(itemstack, stack, playerIn, worldIn, Blocks.FARMLAND.getDefaultState());
        return EnumActionResult.SUCCESS;
      } 
      if (block == Blocks.DIRT)
        switch ((BlockDirt.DirtType)iblockstate.getValue((IProperty)BlockDirt.VARIANT)) {
          case DIRT:
            setBlock(itemstack, stack, playerIn, worldIn, Blocks.FARMLAND.getDefaultState());
            return EnumActionResult.SUCCESS;
          case null:
            setBlock(itemstack, stack, playerIn, worldIn, Blocks.DIRT.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.DIRT));
            return EnumActionResult.SUCCESS;
        }  
    } 
    return EnumActionResult.PASS;
  }
  
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    stack.damageItem(1, attacker);
    return true;
  }
  
  protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
    worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
    if (!worldIn.isRemote) {
      worldIn.setBlockState(pos, state, 11);
      stack.damageItem(1, (EntityLivingBase)player);
    } 
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public String getMaterialName() {
    return this.theToolMaterial.toString();
  }
  
  public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
    if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
      multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0D, 0));
      multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (this.speed - 4.0F), 0));
    } 
    return multimap;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemHoe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */