package net.minecraft.world;

public enum EnumDifficulty {
  PEACEFUL(0, "options.difficulty.peaceful"),
  EASY(1, "options.difficulty.easy"),
  NORMAL(2, "options.difficulty.normal"),
  HARD(3, "options.difficulty.hard");
  
  private static final EnumDifficulty[] ID_MAPPING;
  
  private final int difficultyId;
  
  private final String difficultyResourceKey;
  
  static {
    ID_MAPPING = new EnumDifficulty[(values()).length];
    byte b;
    int i;
    EnumDifficulty[] arrayOfEnumDifficulty;
    for (i = (arrayOfEnumDifficulty = values()).length, b = 0; b < i; ) {
      EnumDifficulty enumdifficulty = arrayOfEnumDifficulty[b];
      ID_MAPPING[enumdifficulty.difficultyId] = enumdifficulty;
      b++;
    } 
  }
  
  EnumDifficulty(int difficultyIdIn, String difficultyResourceKeyIn) {
    this.difficultyId = difficultyIdIn;
    this.difficultyResourceKey = difficultyResourceKeyIn;
  }
  
  public int getDifficultyId() {
    return this.difficultyId;
  }
  
  public static EnumDifficulty getDifficultyEnum(int p_151523_0_) {
    return ID_MAPPING[p_151523_0_ % ID_MAPPING.length];
  }
  
  public String getDifficultyResourceKey() {
    return this.difficultyResourceKey;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\EnumDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */