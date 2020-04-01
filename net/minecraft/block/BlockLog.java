package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {
  public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);
  
  public BlockLog() {
    super(Material.WOOD);
    setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    setHardness(2.0F);
    setSoundType(SoundType.WOOD);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    int i = 4;
    int j = 5;
    if (worldIn.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5)))
      for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-4, -4, -4), pos.add(4, 4, 4))) {
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getMaterial() == Material.LEAVES && !((Boolean)iblockstate.getValue((IProperty)BlockLeaves.CHECK_DECAY)).booleanValue())
          worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4); 
      }  
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return getStateFromMeta(meta).withProperty((IProperty)LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
  }
  
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_90:
      case COUNTERCLOCKWISE_90:
        switch ((EnumAxis)state.getValue((IProperty)LOG_AXIS)) {
          case X:
            return state.withProperty((IProperty)LOG_AXIS, EnumAxis.Z);
          case Z:
            return state.withProperty((IProperty)LOG_AXIS, EnumAxis.X);
        } 
        return state;
    } 
    return state;
  }
  
  public enum EnumAxis implements IStringSerializable {
    X("x"),
    Y("y"),
    Z("z"),
    NONE("none");
    
    private final String name;
    
    EnumAxis(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
      switch (axis) {
        case null:
          return X;
        case Y:
          return Y;
        case Z:
          return Z;
      } 
      return NONE;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */