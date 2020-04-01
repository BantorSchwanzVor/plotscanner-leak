package net.minecraft.tileentity;

import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityDropper extends TileEntityDispenser {
  public static void registerFixesDropper(DataFixer fixer) {
    fixer.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker)new ItemStackDataLists(TileEntityDropper.class, new String[] { "Items" }));
  }
  
  public String getName() {
    return hasCustomName() ? this.field_190577_o : "container.dropper";
  }
  
  public String getGuiID() {
    return "minecraft:dropper";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */