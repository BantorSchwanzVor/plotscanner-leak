package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockWorkbench extends Block {
  protected BlockWorkbench() {
    super(Material.WOOD);
    setCreativeTab(CreativeTabs.DECORATIONS);
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (worldIn.isRemote)
      return true; 
    playerIn.displayGui(new InterfaceCraftingTable(worldIn, pos));
    playerIn.addStat(StatList.CRAFTING_TABLE_INTERACTION);
    return true;
  }
  
  public static class InterfaceCraftingTable implements IInteractionObject {
    private final World world;
    
    private final BlockPos position;
    
    public InterfaceCraftingTable(World worldIn, BlockPos pos) {
      this.world = worldIn;
      this.position = pos;
    }
    
    public String getName() {
      return "crafting_table";
    }
    
    public boolean hasCustomName() {
      return false;
    }
    
    public ITextComponent getDisplayName() {
      return (ITextComponent)new TextComponentTranslation(String.valueOf(Blocks.CRAFTING_TABLE.getUnlocalizedName()) + ".name", new Object[0]);
    }
    
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
      return (Container)new ContainerWorkbench(playerInventory, this.world, this.position);
    }
    
    public String getGuiID() {
      return "minecraft:crafting_table";
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockWorkbench.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */