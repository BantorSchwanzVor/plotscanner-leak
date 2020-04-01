package net.minecraft.stats;

import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;

public class StatCrafting extends StatBase {
  private final Item item;
  
  public StatCrafting(String p_i45910_1_, String p_i45910_2_, ITextComponent statNameIn, Item p_i45910_4_) {
    super(String.valueOf(p_i45910_1_) + p_i45910_2_, statNameIn);
    this.item = p_i45910_4_;
  }
  
  public Item getItem() {
    return this.item;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\stats\StatCrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */