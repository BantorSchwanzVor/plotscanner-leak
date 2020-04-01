package net.minecraft.util.datafix.fixes;

import com.google.gson.JsonParseException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class BookPagesStrictJSON implements IFixableData {
  public int getFixVersion() {
    return 165;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("minecraft:written_book".equals(compound.getString("id"))) {
      NBTTagCompound nbttagcompound = compound.getCompoundTag("tag");
      if (nbttagcompound.hasKey("pages", 9)) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
        for (int i = 0; i < nbttaglist.tagCount(); i++) {
          TextComponentString textComponentString;
          String s = nbttaglist.getStringTagAt(i);
          ITextComponent itextcomponent = null;
          if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s)) {
            if ((s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') || (s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')) {
              TextComponentString textComponentString1;
              ITextComponent iTextComponent;
              try {
                itextcomponent = (ITextComponent)JsonUtils.gsonDeserialize(SignStrictJSON.GSON_INSTANCE, s, ITextComponent.class, true);
                if (itextcomponent == null)
                  textComponentString1 = new TextComponentString(""); 
              } catch (JsonParseException jsonParseException) {}
              if (textComponentString1 == null)
                try {
                  iTextComponent = ITextComponent.Serializer.jsonToComponent(s);
                } catch (JsonParseException jsonParseException) {} 
              if (iTextComponent == null)
                try {
                  iTextComponent = ITextComponent.Serializer.fromJsonLenient(s);
                } catch (JsonParseException jsonParseException) {} 
              if (iTextComponent == null)
                textComponentString = new TextComponentString(s); 
            } else {
              textComponentString = new TextComponentString(s);
            } 
          } else {
            textComponentString = new TextComponentString("");
          } 
          nbttaglist.set(i, (NBTBase)new NBTTagString(ITextComponent.Serializer.componentToJson((ITextComponent)textComponentString)));
        } 
        nbttagcompound.setTag("pages", (NBTBase)nbttaglist);
      } 
    } 
    return compound;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\BookPagesStrictJSON.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */