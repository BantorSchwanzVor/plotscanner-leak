package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemFireworkCharge extends Item {
  public static NBTBase getExplosionTag(ItemStack stack, String key) {
    if (stack.hasTagCompound()) {
      NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
      if (nbttagcompound != null)
        return nbttagcompound.getTag(key); 
    } 
    return null;
  }
  
  public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
    if (stack.hasTagCompound()) {
      NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
      if (nbttagcompound != null)
        addExplosionInfo(nbttagcompound, tooltip); 
    } 
  }
  
  public static void addExplosionInfo(NBTTagCompound nbt, List<String> tooltip) {
    byte b0 = nbt.getByte("Type");
    if (b0 >= 0 && b0 <= 4) {
      tooltip.add(I18n.translateToLocal("item.fireworksCharge.type." + b0).trim());
    } else {
      tooltip.add(I18n.translateToLocal("item.fireworksCharge.type").trim());
    } 
    int[] aint = nbt.getIntArray("Colors");
    if (aint.length > 0) {
      boolean flag = true;
      String s = "";
      byte b;
      int i, arrayOfInt[];
      for (i = (arrayOfInt = aint).length, b = 0; b < i; ) {
        int k = arrayOfInt[b];
        if (!flag)
          s = String.valueOf(s) + ", "; 
        flag = false;
        boolean flag1 = false;
        for (int j = 0; j < ItemDye.DYE_COLORS.length; j++) {
          if (k == ItemDye.DYE_COLORS[j]) {
            flag1 = true;
            s = String.valueOf(s) + I18n.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(j).getUnlocalizedName());
            break;
          } 
        } 
        if (!flag1)
          s = String.valueOf(s) + I18n.translateToLocal("item.fireworksCharge.customColor"); 
        b++;
      } 
      tooltip.add(s);
    } 
    int[] aint1 = nbt.getIntArray("FadeColors");
    if (aint1.length > 0) {
      boolean flag2 = true;
      String s1 = String.valueOf(I18n.translateToLocal("item.fireworksCharge.fadeTo")) + " ";
      byte b;
      int i, arrayOfInt[];
      for (i = (arrayOfInt = aint1).length, b = 0; b < i; ) {
        int l = arrayOfInt[b];
        if (!flag2)
          s1 = String.valueOf(s1) + ", "; 
        flag2 = false;
        boolean flag5 = false;
        for (int k = 0; k < 16; k++) {
          if (l == ItemDye.DYE_COLORS[k]) {
            flag5 = true;
            s1 = String.valueOf(s1) + I18n.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(k).getUnlocalizedName());
            break;
          } 
        } 
        if (!flag5)
          s1 = String.valueOf(s1) + I18n.translateToLocal("item.fireworksCharge.customColor"); 
        b++;
      } 
      tooltip.add(s1);
    } 
    boolean flag3 = nbt.getBoolean("Trail");
    if (flag3)
      tooltip.add(I18n.translateToLocal("item.fireworksCharge.trail")); 
    boolean flag4 = nbt.getBoolean("Flicker");
    if (flag4)
      tooltip.add(I18n.translateToLocal("item.fireworksCharge.flicker")); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\item\ItemFireworkCharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */