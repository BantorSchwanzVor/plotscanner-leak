package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSilverfish extends Block {
  public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
  
  public BlockSilverfish() {
    super(Material.CLAY);
    setDefaultState(this.blockState.getBaseState().withProperty((IProperty)VARIANT, EnumType.STONE));
    setHardness(0.0F);
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public int quantityDropped(Random random) {
    return 0;
  }
  
  public static boolean canContainSilverfish(IBlockState blockState) {
    Block block = blockState.getBlock();
    return !(blockState != Blocks.STONE.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, BlockStone.EnumType.STONE) && block != Blocks.COBBLESTONE && block != Blocks.STONEBRICK);
  }
  
  protected ItemStack getSilkTouchDrop(IBlockState state) {
    switch ((EnumType)state.getValue((IProperty)VARIANT)) {
      case COBBLESTONE:
        return new ItemStack(Blocks.COBBLESTONE);
      case STONEBRICK:
        return new ItemStack(Blocks.STONEBRICK);
      case MOSSY_STONEBRICK:
        return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
      case CRACKED_STONEBRICK:
        return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
      case null:
        return new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
    } 
    return new ItemStack(Blocks.STONE);
  }
  
  public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
      EntitySilverfish entitysilverfish = new EntitySilverfish(worldIn);
      entitysilverfish.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
      worldIn.spawnEntityInWorld((Entity)entitysilverfish);
      entitysilverfish.spawnExplosionParticle();
    } 
  }
  
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
  }
  
  public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
    byte b;
    int i;
    EnumType[] arrayOfEnumType;
    for (i = (arrayOfEnumType = EnumType.values()).length, b = 0; b < i; ) {
      EnumType blocksilverfish$enumtype = arrayOfEnumType[b];
      tab.add(new ItemStack(this, 1, blocksilverfish$enumtype.getMetadata()));
      b++;
    } 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty((IProperty)VARIANT, EnumType.byMetadata(meta));
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumType)state.getValue((IProperty)VARIANT)).getMetadata();
  }
  
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { (IProperty)VARIANT });
  }
  
  public enum EnumType implements IStringSerializable {
    STONE(0, "stone") {
      public IBlockState getModelBlock() {
        return Blocks.STONE.getDefaultState().withProperty((IProperty)BlockStone.VARIANT, BlockStone.EnumType.STONE);
      }
    },
    COBBLESTONE(1, "cobblestone", "cobble") {
      public IBlockState getModelBlock() {
        return Blocks.COBBLESTONE.getDefaultState();
      }
    },
    STONEBRICK(2, "stone_brick", "brick") {
      public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
      }
    },
    MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {
      public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
      }
    },
    CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {
      public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
      }
    },
    CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {
      public IBlockState getModelBlock() {
        return Blocks.STONEBRICK.getDefaultState().withProperty((IProperty)BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
      }
    };
    
    private static final EnumType[] META_LOOKUP = new EnumType[(values()).length];
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blocksilverfish$enumtype = arrayOfEnumType[b];
        META_LOOKUP[blocksilverfish$enumtype.getMetadata()] = blocksilverfish$enumtype;
        b++;
      } 
    }
    
    EnumType(int meta, String name, String unlocalizedName) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public int getMetadata() {
      return this.meta;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length)
        meta = 0; 
      return META_LOOKUP[meta];
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getUnlocalizedName() {
      return this.unlocalizedName;
    }
    
    public static EnumType forModelBlock(IBlockState model) {
      byte b;
      int i;
      EnumType[] arrayOfEnumType;
      for (i = (arrayOfEnumType = values()).length, b = 0; b < i; ) {
        EnumType blocksilverfish$enumtype = arrayOfEnumType[b];
        if (model == blocksilverfish$enumtype.getModelBlock())
          return blocksilverfish$enumtype; 
        b++;
      } 
      return STONE;
    }
    
    public abstract IBlockState getModelBlock();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockSilverfish.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */