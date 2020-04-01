package net.minecraft.util.datafix.fixes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.StringUtils;
import net.minecraft.util.datafix.IFixableData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SignStrictJSON implements IFixableData {
  public static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(ITextComponent.class, new JsonDeserializer<ITextComponent>() {
        public ITextComponent deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
          if (p_deserialize_1_.isJsonPrimitive())
            return (ITextComponent)new TextComponentString(p_deserialize_1_.getAsString()); 
          if (p_deserialize_1_.isJsonArray()) {
            JsonArray jsonarray = p_deserialize_1_.getAsJsonArray();
            ITextComponent itextcomponent = null;
            for (JsonElement jsonelement : jsonarray) {
              ITextComponent itextcomponent1 = deserialize(jsonelement, jsonelement.getClass(), p_deserialize_3_);
              if (itextcomponent == null) {
                itextcomponent = itextcomponent1;
                continue;
              } 
              itextcomponent.appendSibling(itextcomponent1);
            } 
            return itextcomponent;
          } 
          throw new JsonParseException("Don't know how to turn " + p_deserialize_1_ + " into a Component");
        }
      }).create();
  
  public int getFixVersion() {
    return 101;
  }
  
  public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
    if ("Sign".equals(compound.getString("id"))) {
      updateLine(compound, "Text1");
      updateLine(compound, "Text2");
      updateLine(compound, "Text3");
      updateLine(compound, "Text4");
    } 
    return compound;
  }
  
  private void updateLine(NBTTagCompound compound, String key) {
    TextComponentString textComponentString;
    String s = compound.getString(key);
    ITextComponent itextcomponent = null;
    if (!"null".equals(s) && !StringUtils.isNullOrEmpty(s)) {
      if ((s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') || (s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')) {
        TextComponentString textComponentString1;
        ITextComponent iTextComponent;
        try {
          itextcomponent = (ITextComponent)JsonUtils.gsonDeserialize(GSON_INSTANCE, s, ITextComponent.class, true);
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
    compound.setString(key, ITextComponent.Serializer.componentToJson((ITextComponent)textComponentString));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\datafix\fixes\SignStrictJSON.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */