package net.minecraft.util;

public enum Mirror {
  NONE("no_mirror"),
  LEFT_RIGHT("mirror_left_right"),
  FRONT_BACK("mirror_front_back");
  
  private final String name;
  
  private static final String[] mirrorNames;
  
  static {
    mirrorNames = new String[(values()).length];
    int i = 0;
    byte b;
    int j;
    Mirror[] arrayOfMirror;
    for (j = (arrayOfMirror = values()).length, b = 0; b < j; ) {
      Mirror mirror = arrayOfMirror[b];
      mirrorNames[i++] = mirror.name;
      b++;
    } 
  }
  
  Mirror(String nameIn) {
    this.name = nameIn;
  }
  
  public int mirrorRotation(int rotationIn, int rotationCount) {
    int i = rotationCount / 2;
    int j = (rotationIn > i) ? (rotationIn - rotationCount) : rotationIn;
    switch (this) {
      case null:
        return (rotationCount - j) % rotationCount;
      case LEFT_RIGHT:
        return (i - j + rotationCount) % rotationCount;
    } 
    return rotationIn;
  }
  
  public Rotation toRotation(EnumFacing facing) {
    EnumFacing.Axis enumfacing$axis = facing.getAxis();
    return ((this != LEFT_RIGHT || enumfacing$axis != EnumFacing.Axis.Z) && (this != FRONT_BACK || enumfacing$axis != EnumFacing.Axis.X)) ? Rotation.NONE : Rotation.CLOCKWISE_180;
  }
  
  public EnumFacing mirror(EnumFacing facing) {
    switch (this) {
      case null:
        if (facing == EnumFacing.WEST)
          return EnumFacing.EAST; 
        if (facing == EnumFacing.EAST)
          return EnumFacing.WEST; 
        return facing;
      case LEFT_RIGHT:
        if (facing == EnumFacing.NORTH)
          return EnumFacing.SOUTH; 
        if (facing == EnumFacing.SOUTH)
          return EnumFacing.NORTH; 
        return facing;
    } 
    return facing;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\Mirror.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */