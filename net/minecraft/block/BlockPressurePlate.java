package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  
  private final Sensitivity sensitivity;
  
  protected BlockPressurePlate(Material materialIn, Sensitivity sensitivityIn) {
    super(materialIn);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)));
    this.sensitivity = sensitivityIn;
  }
  
  protected int getRedstoneStrength(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
  }
  
  protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
    return state.withProperty((IProperty)POWERED, Boolean.valueOf((strength > 0)));
  }
  
  protected void playClickOnSound(World worldIn, BlockPos color) {
    if (this.blockMaterial == Material.WOOD) {
      worldIn.playSound(null, color, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
    } else {
      worldIn.playSound(null, color, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
    } 
  }
  
  protected void playClickOffSound(World worldIn, BlockPos pos) {
    if (this.blockMaterial == Material.WOOD) {
      worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.7F);
    } else {
      worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
    } 
  }
  
  protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
    List<? extends Entity> list;
    AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
    switch (this.sensitivity) {
      case null:
        list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
        break;
      case MOBS:
        list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        break;
      default:
        return 0;
    } 
    if (!list.isEmpty())
      for (Entity entity : list) {
        if (!entity.doesEntityNotTriggerPressurePlate())
          return 15; 
      }  
    return 0;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf((meta == 1)));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 1 : 0;
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)POWERED });
  }
  
  public enum Sensitivity {
    EVERYTHING, MOBS;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockPressurePlate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */