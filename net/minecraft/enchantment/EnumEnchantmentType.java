package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;

public enum EnumEnchantmentType {
  ALL {
    public boolean canEnchantItem(Item itemIn) {
      byte b;
      int i;
      EnumEnchantmentType[] arrayOfEnumEnchantmentType;
      for (i = (arrayOfEnumEnchantmentType = values()).length, b = 0; b < i; ) {
        EnumEnchantmentType enumenchantmenttype = arrayOfEnumEnchantmentType[b];
        if (enumenchantmenttype != EnumEnchantmentType.ALL && enumenchantmenttype.canEnchantItem(itemIn))
          return true; 
        b++;
      } 
      return false;
    }
  },
  ARMOR {
    public boolean canEnchantItem(Item itemIn) {
      return itemIn instanceof ItemArmor;
    }
  },
  ARMOR_FEET {
    public boolean canEnchantItem(Item itemIn) {
      return (itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.FEET);
    }
  },
  ARMOR_LEGS {
    public boolean canEnchantItem(Item itemIn) {
      return (itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.LEGS);
    }
  },
  ARMOR_CHEST {
    public boolean canEnchantItem(Item itemIn) {
      return (itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.CHEST);
    }
  },
  ARMOR_HEAD {
    public boolean canEnchantItem(Item itemIn) {
      return (itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.HEAD);
    }
  },
  WEAPON {
    public boolean canEnchantItem(Item itemIn) {
      return itemIn instanceof net.minecraft.item.ItemSword;
    }
  },
  DIGGER {
    public boolean canEnchantItem(Item itemIn) {
      return itemIn instanceof net.minecraft.item.ItemTool;
    }
  },
  FISHING_ROD {
    public boolean canEnchantItem(Item itemIn) {
      return itemIn instanceof net.minecraft.item.ItemFishingRod;
    }
  },
  BREAKABLE {
    public boolean canEnchantItem(Item itemIn) {
      return itemIn.isDamageable();
    }
  },
  BOW {
    public boolean canEnchantItem(Item itemIn) {
      return itemIn instanceof net.minecraft.item.ItemBow;
    }
  },
  WEARABLE {
    public boolean canEnchantItem(Item itemIn) {
      boolean flag = (itemIn instanceof ItemBlock && ((ItemBlock)itemIn).getBlock() instanceof net.minecraft.block.BlockPumpkin);
      return !(!(itemIn instanceof ItemArmor) && !(itemIn instanceof net.minecraft.item.ItemElytra) && !(itemIn instanceof net.minecraft.item.ItemSkull) && !flag);
    }
  };
  
  public abstract boolean canEnchantItem(Item paramItem);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\enchantment\EnumEnchantmentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */