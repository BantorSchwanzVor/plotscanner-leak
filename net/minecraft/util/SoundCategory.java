package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;

public enum SoundCategory {
  MASTER("master"),
  MUSIC("music"),
  RECORDS("record"),
  WEATHER("weather"),
  BLOCKS("block"),
  HOSTILE("hostile"),
  NEUTRAL("neutral"),
  PLAYERS("player"),
  AMBIENT("ambient"),
  VOICE("voice");
  
  private static final Map<String, SoundCategory> SOUND_CATEGORIES;
  
  private final String name;
  
  static {
    SOUND_CATEGORIES = Maps.newHashMap();
    byte b;
    int i;
    SoundCategory[] arrayOfSoundCategory;
    for (i = (arrayOfSoundCategory = values()).length, b = 0; b < i; ) {
      SoundCategory soundcategory = arrayOfSoundCategory[b];
      if (SOUND_CATEGORIES.containsKey(soundcategory.getName()))
        throw new Error("Clash in Sound Category name pools! Cannot insert " + soundcategory); 
      SOUND_CATEGORIES.put(soundcategory.getName(), soundcategory);
      b++;
    } 
  }
  
  SoundCategory(String nameIn) {
    this.name = nameIn;
  }
  
  public String getName() {
    return this.name;
  }
  
  public static SoundCategory getByName(String categoryName) {
    return SOUND_CATEGORIES.get(categoryName);
  }
  
  public static Set<String> getSoundCategoryNames() {
    return SOUND_CATEGORIES.keySet();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\SoundCategory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */