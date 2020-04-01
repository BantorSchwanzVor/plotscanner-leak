package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
  public static final String[] NBT_TYPES = new String[] { 
      "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", 
      "COMPOUND", "INT[]", "LONG[]" };
  
  abstract void write(DataOutput paramDataOutput) throws IOException;
  
  abstract void read(DataInput paramDataInput, int paramInt, NBTSizeTracker paramNBTSizeTracker) throws IOException;
  
  public abstract String toString();
  
  public abstract byte getId();
  
  protected static NBTBase createNewByType(byte id) {
    switch (id) {
      case 0:
        return new NBTTagEnd();
      case 1:
        return new NBTTagByte();
      case 2:
        return new NBTTagShort();
      case 3:
        return new NBTTagInt();
      case 4:
        return new NBTTagLong();
      case 5:
        return new NBTTagFloat();
      case 6:
        return new NBTTagDouble();
      case 7:
        return new NBTTagByteArray();
      case 8:
        return new NBTTagString();
      case 9:
        return new NBTTagList();
      case 10:
        return new NBTTagCompound();
      case 11:
        return new NBTTagIntArray();
      case 12:
        return new NBTTagLongArray();
    } 
    return null;
  }
  
  public static String func_193581_j(int p_193581_0_) {
    switch (p_193581_0_) {
      case 0:
        return "TAG_End";
      case 1:
        return "TAG_Byte";
      case 2:
        return "TAG_Short";
      case 3:
        return "TAG_Int";
      case 4:
        return "TAG_Long";
      case 5:
        return "TAG_Float";
      case 6:
        return "TAG_Double";
      case 7:
        return "TAG_Byte_Array";
      case 8:
        return "TAG_String";
      case 9:
        return "TAG_List";
      case 10:
        return "TAG_Compound";
      case 11:
        return "TAG_Int_Array";
      case 12:
        return "TAG_Long_Array";
      case 99:
        return "Any Numeric Tag";
    } 
    return "UNKNOWN";
  }
  
  public abstract NBTBase copy();
  
  public boolean hasNoTags() {
    return false;
  }
  
  public boolean equals(Object p_equals_1_) {
    return (p_equals_1_ instanceof NBTBase && getId() == ((NBTBase)p_equals_1_).getId());
  }
  
  public int hashCode() {
    return getId();
  }
  
  protected String getString() {
    return toString();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\nbt\NBTBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */