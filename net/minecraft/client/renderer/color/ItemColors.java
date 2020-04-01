package net.minecraft.client.renderer.color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.world.ColorizerGrass;

public class ItemColors {
  private final ObjectIntIdentityMap<IItemColor> mapItemColors = new ObjectIntIdentityMap(32);
  
  public static ItemColors init(final BlockColors p_186729_0_) {
    ItemColors itemcolors = new ItemColors();
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return (tintIndex > 0) ? -1 : ((ItemArmor)stack.getItem()).getColor(stack);
          }
        },  new Item[] { (Item)Items.LEATHER_HELMET, (Item)Items.LEATHER_CHESTPLATE, (Item)Items.LEATHER_LEGGINGS, (Item)Items.LEATHER_BOOTS });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.byMetadata(stack.getMetadata());
            return (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN) ? -1 : ColorizerGrass.getGrassColor(0.5D, 1.0D);
          }
        }new Block[] { (Block)Blocks.DOUBLE_PLANT });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            if (tintIndex != 1)
              return -1; 
            NBTBase nbtbase = ItemFireworkCharge.getExplosionTag(stack, "Colors");
            if (!(nbtbase instanceof NBTTagIntArray))
              return 9079434; 
            int[] aint = ((NBTTagIntArray)nbtbase).getIntArray();
            if (aint.length == 1)
              return aint[0]; 
            int i = 0;
            int j = 0;
            int k = 0;
            byte b;
            int m, arrayOfInt1[];
            for (m = (arrayOfInt1 = aint).length, b = 0; b < m; ) {
              int l = arrayOfInt1[b];
              i += (l & 0xFF0000) >> 16;
              j += (l & 0xFF00) >> 8;
              k += (l & 0xFF) >> 0;
              b++;
            } 
            i /= aint.length;
            j /= aint.length;
            k /= aint.length;
            return i << 16 | j << 8 | k;
          }
        }new Item[] { Items.FIREWORK_CHARGE });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return (tintIndex > 0) ? -1 : PotionUtils.func_190932_c(stack);
          }
        },  new Item[] { (Item)Items.POTIONITEM, (Item)Items.SPLASH_POTION, (Item)Items.LINGERING_POTION });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.ENTITY_EGGS.get(ItemMonsterPlacer.func_190908_h(stack));
            if (entitylist$entityegginfo == null)
              return -1; 
            return (tintIndex == 0) ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor;
          }
        },  new Item[] { Items.SPAWN_EGG });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            IBlockState iblockstate = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            return p_186729_0_.colorMultiplier(iblockstate, null, null, tintIndex);
          }
        }new Block[] { (Block)Blocks.GRASS, (Block)Blocks.TALLGRASS, Blocks.VINE, (Block)Blocks.LEAVES, (Block)Blocks.LEAVES2, Blocks.WATERLILY });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return (tintIndex == 0) ? PotionUtils.func_190932_c(stack) : -1;
          }
        },  new Item[] { Items.TIPPED_ARROW });
    itemcolors.registerItemColorHandler(new IItemColor() {
          public int getColorFromItemstack(ItemStack stack, int tintIndex) {
            return (tintIndex == 0) ? -1 : ItemMap.func_190907_h(stack);
          }
        },  new Item[] { (Item)Items.FILLED_MAP });
    return itemcolors;
  }
  
  public int getColorFromItemstack(ItemStack stack, int tintIndex) {
    IItemColor iitemcolor = (IItemColor)this.mapItemColors.getByValue(Item.REGISTRY.getIDForObject(stack.getItem()));
    return (iitemcolor == null) ? -1 : iitemcolor.getColorFromItemstack(stack, tintIndex);
  }
  
  public void registerItemColorHandler(IItemColor itemColor, Block... blocksIn) {
    byte b;
    int i;
    Block[] arrayOfBlock;
    for (i = (arrayOfBlock = blocksIn).length, b = 0; b < i; ) {
      Block block = arrayOfBlock[b];
      this.mapItemColors.put(itemColor, Item.getIdFromItem(Item.getItemFromBlock(block)));
      b++;
    } 
  }
  
  public void registerItemColorHandler(IItemColor itemColor, Item... itemsIn) {
    byte b;
    int i;
    Item[] arrayOfItem;
    for (i = (arrayOfItem = itemsIn).length, b = 0; b < i; ) {
      Item item = arrayOfItem[b];
      this.mapItemColors.put(itemColor, Item.getIdFromItem(item));
      b++;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\color\ItemColors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */