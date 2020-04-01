package net.minecraft.inventory;

public enum EntityEquipmentSlot {
  MAINHAND(Type.HAND, 0, 0, "mainhand"),
  OFFHAND(Type.HAND, 1, 5, "offhand"),
  FEET(Type.ARMOR, 0, 1, "feet"),
  LEGS(Type.ARMOR, 1, 2, "legs"),
  CHEST(Type.ARMOR, 2, 3, "chest"),
  HEAD(Type.ARMOR, 3, 4, "head");
  
  private final Type slotType;
  
  private final int index;
  
  private final int slotIndex;
  
  private final String name;
  
  EntityEquipmentSlot(Type slotTypeIn, int indexIn, int slotIndexIn, String nameIn) {
    this.slotType = slotTypeIn;
    this.index = indexIn;
    this.slotIndex = slotIndexIn;
    this.name = nameIn;
  }
  
  public Type getSlotType() {
    return this.slotType;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public int getSlotIndex() {
    return this.slotIndex;
  }
  
  public String getName() {
    return this.name;
  }
  
  public static EntityEquipmentSlot fromString(String targetName) {
    byte b;
    int i;
    EntityEquipmentSlot[] arrayOfEntityEquipmentSlot;
    for (i = (arrayOfEntityEquipmentSlot = values()).length, b = 0; b < i; ) {
      EntityEquipmentSlot entityequipmentslot = arrayOfEntityEquipmentSlot[b];
      if (entityequipmentslot.getName().equals(targetName))
        return entityequipmentslot; 
      b++;
    } 
    throw new IllegalArgumentException("Invalid slot '" + targetName + "'");
  }
  
  public enum Type {
    HAND, ARMOR;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\inventory\EntityEquipmentSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */