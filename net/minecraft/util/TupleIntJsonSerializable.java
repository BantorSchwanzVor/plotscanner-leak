package net.minecraft.util;

public class TupleIntJsonSerializable {
  private int integerValue;
  
  private IJsonSerializable jsonSerializableValue;
  
  public int getIntegerValue() {
    return this.integerValue;
  }
  
  public void setIntegerValue(int integerValueIn) {
    this.integerValue = integerValueIn;
  }
  
  public <T extends IJsonSerializable> T getJsonSerializableValue() {
    return (T)this.jsonSerializableValue;
  }
  
  public void setJsonSerializableValue(IJsonSerializable jsonSerializableValueIn) {
    this.jsonSerializableValue = jsonSerializableValueIn;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\TupleIntJsonSerializable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */