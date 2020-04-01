package net.minecraft.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGlazedTerracotta extends BlockHorizontal {
  public BlockGlazedTerracotta(EnumDyeColor p_i47400_1_) {
    super(Material.ROCK, MapColor.func_193558_a(p_i47400_1_));
    setHardness(1.4F);
    setSoundType(SoundType.STONE);
    String s = p_i47400_1_.getUnlocalizedName();
    if (s.length() > 1) {
      String s1 = String.valueOf(s.substring(0, 1).toUpperCase()) + s.substring(1, s.length());
      setUnlocalizedName("glazedTerracotta" + s1);
    } 
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)FACING });
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty((IProperty)FACING, (Comparable)rot.rotate((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue((IProperty)FACING)));
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)placer.getHorizontalFacing().getOpposite());
  }
  
  public int getMetaFromState(IBlockState state) {
    int i = 0;
    i |= ((EnumFacing)state.getValue((IProperty)FACING)).getHorizontalIndex();
    return i;
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)FACING, (Comparable)EnumFacing.getHorizontal(meta));
  }
  
  public EnumPushReaction getMobilityFlag(IBlockState state) {
    return EnumPushReaction.PUSH_ONLY;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockGlazedTerracotta.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */