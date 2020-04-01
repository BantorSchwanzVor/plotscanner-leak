package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class Sound implements ISoundEventAccessor<Sound> {
  private final ResourceLocation name;
  
  private final float volume;
  
  private final float pitch;
  
  private final int weight;
  
  private final Type type;
  
  private final boolean streaming;
  
  public Sound(String nameIn, float volumeIn, float pitchIn, int weightIn, Type typeIn, boolean p_i46526_6_) {
    this.name = new ResourceLocation(nameIn);
    this.volume = volumeIn;
    this.pitch = pitchIn;
    this.weight = weightIn;
    this.type = typeIn;
    this.streaming = p_i46526_6_;
  }
  
  public ResourceLocation getSoundLocation() {
    return this.name;
  }
  
  public ResourceLocation getSoundAsOggLocation() {
    return new ResourceLocation(this.name.getResourceDomain(), "sounds/" + this.name.getResourcePath() + ".ogg");
  }
  
  public float getVolume() {
    return this.volume;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public int getWeight() {
    return this.weight;
  }
  
  public Sound cloneEntry() {
    return this;
  }
  
  public Type getType() {
    return this.type;
  }
  
  public boolean isStreaming() {
    return this.streaming;
  }
  
  public enum Type {
    FILE("file"),
    SOUND_EVENT("event");
    
    private final String name;
    
    Type(String nameIn) {
      this.name = nameIn;
    }
    
    public static Type getByName(String nameIn) {
      byte b;
      int i;
      Type[] arrayOfType;
      for (i = (arrayOfType = values()).length, b = 0; b < i; ) {
        Type sound$type = arrayOfType[b];
        if (sound$type.name.equals(nameIn))
          return sound$type; 
        b++;
      } 
      return null;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\audio\Sound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */