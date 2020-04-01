package shadersmod.client;

import optifine.Lang;

public class PropertyDefaultTrueFalse extends Property {
  public static final String[] PROPERTY_VALUES = new String[] { "default", "true", "false" };
  
  public static final String[] USER_VALUES = new String[] { "Default", "ON", "OFF" };
  
  public PropertyDefaultTrueFalse(String propertyName, String userName, int defaultValue) {
    super(propertyName, PROPERTY_VALUES, userName, USER_VALUES, defaultValue);
  }
  
  public String getUserValue() {
    if (isDefault())
      return Lang.getDefault(); 
    if (isTrue())
      return Lang.getOn(); 
    return isFalse() ? Lang.getOff() : super.getUserValue();
  }
  
  public boolean isDefault() {
    return (getValue() == 0);
  }
  
  public boolean isTrue() {
    return (getValue() == 1);
  }
  
  public boolean isFalse() {
    return (getValue() == 2);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\PropertyDefaultTrueFalse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */