package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;

public class MerchantRecipeList extends ArrayList<MerchantRecipe> {
  public MerchantRecipeList() {}
  
  public MerchantRecipeList(NBTTagCompound compound) {
    readRecipiesFromTags(compound);
  }
  
  @Nullable
  public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_) {
    if (p_77203_3_ > 0 && p_77203_3_ < size()) {
      MerchantRecipe merchantrecipe1 = get(p_77203_3_);
      return (!areItemStacksExactlyEqual(p_77203_1_, merchantrecipe1.getItemToBuy()) || ((!p_77203_2_.func_190926_b() || merchantrecipe1.hasSecondItemToBuy()) && (!merchantrecipe1.hasSecondItemToBuy() || !areItemStacksExactlyEqual(p_77203_2_, merchantrecipe1.getSecondItemToBuy()))) || p_77203_1_.func_190916_E() < merchantrecipe1.getItemToBuy().func_190916_E() || (merchantrecipe1.hasSecondItemToBuy() && p_77203_2_.func_190916_E() < merchantrecipe1.getSecondItemToBuy().func_190916_E())) ? null : merchantrecipe1;
    } 
    for (int i = 0; i < size(); i++) {
      MerchantRecipe merchantrecipe = get(i);
      if (areItemStacksExactlyEqual(p_77203_1_, merchantrecipe.getItemToBuy()) && p_77203_1_.func_190916_E() >= merchantrecipe.getItemToBuy().func_190916_E() && ((!merchantrecipe.hasSecondItemToBuy() && p_77203_2_.func_190926_b()) || (merchantrecipe.hasSecondItemToBuy() && areItemStacksExactlyEqual(p_77203_2_, merchantrecipe.getSecondItemToBuy()) && p_77203_2_.func_190916_E() >= merchantrecipe.getSecondItemToBuy().func_190916_E())))
        return merchantrecipe; 
    } 
    return null;
  }
  
  private boolean areItemStacksExactlyEqual(ItemStack stack1, ItemStack stack2) {
    return (ItemStack.areItemsEqual(stack1, stack2) && (!stack2.hasTagCompound() || (stack1.hasTagCompound() && NBTUtil.areNBTEquals((NBTBase)stack2.getTagCompound(), (NBTBase)stack1.getTagCompound(), false))));
  }
  
  public void writeToBuf(PacketBuffer buffer) {
    buffer.writeByte((byte)(size() & 0xFF));
    for (int i = 0; i < size(); i++) {
      MerchantRecipe merchantrecipe = get(i);
      buffer.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
      buffer.writeItemStackToBuffer(merchantrecipe.getItemToSell());
      ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
      buffer.writeBoolean(!itemstack.func_190926_b());
      if (!itemstack.func_190926_b())
        buffer.writeItemStackToBuffer(itemstack); 
      buffer.writeBoolean(merchantrecipe.isRecipeDisabled());
      buffer.writeInt(merchantrecipe.getToolUses());
      buffer.writeInt(merchantrecipe.getMaxTradeUses());
    } 
  }
  
  public static MerchantRecipeList readFromBuf(PacketBuffer buffer) throws IOException {
    MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
    int i = buffer.readByte() & 0xFF;
    for (int j = 0; j < i; j++) {
      ItemStack itemstack = buffer.readItemStackFromBuffer();
      ItemStack itemstack1 = buffer.readItemStackFromBuffer();
      ItemStack itemstack2 = ItemStack.field_190927_a;
      if (buffer.readBoolean())
        itemstack2 = buffer.readItemStackFromBuffer(); 
      boolean flag = buffer.readBoolean();
      int k = buffer.readInt();
      int l = buffer.readInt();
      MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);
      if (flag)
        merchantrecipe.compensateToolUses(); 
      merchantrecipelist.add(merchantrecipe);
    } 
    return merchantrecipelist;
  }
  
  public void readRecipiesFromTags(NBTTagCompound compound) {
    NBTTagList nbttaglist = compound.getTagList("Recipes", 10);
    for (int i = 0; i < nbttaglist.tagCount(); i++) {
      NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
      add(new MerchantRecipe(nbttagcompound));
    } 
  }
  
  public NBTTagCompound getRecipiesAsTags() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < size(); i++) {
      MerchantRecipe merchantrecipe = get(i);
      nbttaglist.appendTag((NBTBase)merchantrecipe.writeToTags());
    } 
    nbttagcompound.setTag("Recipes", (NBTBase)nbttaglist);
    return nbttagcompound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\village\MerchantRecipeList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */