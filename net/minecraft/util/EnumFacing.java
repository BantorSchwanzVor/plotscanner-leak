package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public enum EnumFacing implements IStringSerializable {
  DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)),
  UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)),
  NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)),
  SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)),
  WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)),
  EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
  
  private final int index;
  
  private final int opposite;
  
  private final int horizontalIndex;
  
  private final String name;
  
  private final Axis axis;
  
  private final AxisDirection axisDirection;
  
  private final Vec3i directionVec;
  
  public static final EnumFacing[] VALUES;
  
  private static final EnumFacing[] HORIZONTALS;
  
  private static final Map<String, EnumFacing> NAME_LOOKUP;
  
  static {
    VALUES = new EnumFacing[6];
    HORIZONTALS = new EnumFacing[4];
    NAME_LOOKUP = Maps.newHashMap();
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      VALUES[enumfacing.index] = enumfacing;
      if (enumfacing.getAxis().isHorizontal())
        HORIZONTALS[enumfacing.horizontalIndex] = enumfacing; 
      NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(Locale.ROOT), enumfacing);
      b++;
    } 
  }
  
  EnumFacing(int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, AxisDirection axisDirectionIn, Axis axisIn, Vec3i directionVecIn) {
    this.index = indexIn;
    this.horizontalIndex = horizontalIndexIn;
    this.opposite = oppositeIn;
    this.name = nameIn;
    this.axis = axisIn;
    this.axisDirection = axisDirectionIn;
    this.directionVec = directionVecIn;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public int getHorizontalIndex() {
    return this.horizontalIndex;
  }
  
  public AxisDirection getAxisDirection() {
    return this.axisDirection;
  }
  
  public EnumFacing getOpposite() {
    return VALUES[this.opposite];
  }
  
  public EnumFacing rotateAround(Axis axis) {
    switch (axis) {
      case null:
        if (this != WEST && this != EAST)
          return rotateX(); 
        return this;
      case Y:
        if (this != UP && this != DOWN)
          return rotateY(); 
        return this;
      case Z:
        if (this != NORTH && this != SOUTH)
          return rotateZ(); 
        return this;
    } 
    throw new IllegalStateException("Unable to get CW facing for axis " + axis);
  }
  
  public EnumFacing rotateY() {
    switch (this) {
      case NORTH:
        return EAST;
      case EAST:
        return SOUTH;
      case SOUTH:
        return WEST;
      case WEST:
        return NORTH;
    } 
    throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
  }
  
  private EnumFacing rotateX() {
    switch (this) {
      case NORTH:
        return DOWN;
      default:
        throw new IllegalStateException("Unable to get X-rotated facing of " + this);
      case SOUTH:
        return UP;
      case UP:
        return NORTH;
      case null:
        break;
    } 
    return SOUTH;
  }
  
  private EnumFacing rotateZ() {
    switch (this) {
      case EAST:
        return DOWN;
      default:
        throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
      case WEST:
        return UP;
      case UP:
        return EAST;
      case null:
        break;
    } 
    return WEST;
  }
  
  public EnumFacing rotateYCCW() {
    switch (this) {
      case NORTH:
        return WEST;
      case EAST:
        return NORTH;
      case SOUTH:
        return EAST;
      case WEST:
        return SOUTH;
    } 
    throw new IllegalStateException("Unable to get CCW facing of " + this);
  }
  
  public int getFrontOffsetX() {
    return (this.axis == Axis.X) ? this.axisDirection.getOffset() : 0;
  }
  
  public int getFrontOffsetY() {
    return (this.axis == Axis.Y) ? this.axisDirection.getOffset() : 0;
  }
  
  public int getFrontOffsetZ() {
    return (this.axis == Axis.Z) ? this.axisDirection.getOffset() : 0;
  }
  
  public String getName2() {
    return this.name;
  }
  
  public Axis getAxis() {
    return this.axis;
  }
  
  @Nullable
  public static EnumFacing byName(String name) {
    return (name == null) ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
  }
  
  public static EnumFacing getFront(int index) {
    return VALUES[MathHelper.abs(index % VALUES.length)];
  }
  
  public static EnumFacing getHorizontal(int horizontalIndexIn) {
    return HORIZONTALS[MathHelper.abs(horizontalIndexIn % HORIZONTALS.length)];
  }
  
  public static EnumFacing fromAngle(double angle) {
    return getHorizontal(MathHelper.floor(angle / 90.0D + 0.5D) & 0x3);
  }
  
  public float getHorizontalAngle() {
    return ((this.horizontalIndex & 0x3) * 90);
  }
  
  public static EnumFacing random(Random rand) {
    return values()[rand.nextInt((values()).length)];
  }
  
  public static EnumFacing getFacingFromVector(float x, float y, float z) {
    EnumFacing enumfacing = NORTH;
    float f = Float.MIN_VALUE;
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing1 = arrayOfEnumFacing[b];
      float f1 = x * enumfacing1.directionVec.getX() + y * enumfacing1.directionVec.getY() + z * enumfacing1.directionVec.getZ();
      if (f1 > f) {
        f = f1;
        enumfacing = enumfacing1;
      } 
      b++;
    } 
    return enumfacing;
  }
  
  public String toString() {
    return this.name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public static EnumFacing getFacingFromAxis(AxisDirection axisDirectionIn, Axis axisIn) {
    byte b;
    int i;
    EnumFacing[] arrayOfEnumFacing;
    for (i = (arrayOfEnumFacing = values()).length, b = 0; b < i; ) {
      EnumFacing enumfacing = arrayOfEnumFacing[b];
      if (enumfacing.getAxisDirection() == axisDirectionIn && enumfacing.getAxis() == axisIn)
        return enumfacing; 
      b++;
    } 
    throw new IllegalArgumentException("No such direction: " + axisDirectionIn + " " + axisIn);
  }
  
  public static EnumFacing func_190914_a(BlockPos p_190914_0_, EntityLivingBase p_190914_1_) {
    if (Math.abs(p_190914_1_.posX - (p_190914_0_.getX() + 0.5F)) < 2.0D && Math.abs(p_190914_1_.posZ - (p_190914_0_.getZ() + 0.5F)) < 2.0D) {
      double d0 = p_190914_1_.posY + p_190914_1_.getEyeHeight();
      if (d0 - p_190914_0_.getY() > 2.0D)
        return UP; 
      if (p_190914_0_.getY() - d0 > 0.0D)
        return DOWN; 
    } 
    return p_190914_1_.getHorizontalFacing().getOpposite();
  }
  
  public Vec3i getDirectionVec() {
    return this.directionVec;
  }
  
  public enum Axis implements Predicate<EnumFacing>, IStringSerializable {
    X("x", EnumFacing.Plane.HORIZONTAL),
    Y("y", EnumFacing.Plane.VERTICAL),
    Z("z", EnumFacing.Plane.HORIZONTAL);
    
    private static final Map<String, Axis> NAME_LOOKUP = Maps.newHashMap();
    
    private final String name;
    
    private final EnumFacing.Plane plane;
    
    static {
      byte b;
      int i;
      Axis[] arrayOfAxis;
      for (i = (arrayOfAxis = values()).length, b = 0; b < i; ) {
        Axis enumfacing$axis = arrayOfAxis[b];
        NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(Locale.ROOT), enumfacing$axis);
        b++;
      } 
    }
    
    Axis(String name, EnumFacing.Plane plane) {
      this.name = name;
      this.plane = plane;
    }
    
    @Nullable
    public static Axis byName(String name) {
      return (name == null) ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }
    
    public String getName2() {
      return this.name;
    }
    
    public boolean isVertical() {
      return (this.plane == EnumFacing.Plane.VERTICAL);
    }
    
    public boolean isHorizontal() {
      return (this.plane == EnumFacing.Plane.HORIZONTAL);
    }
    
    public String toString() {
      return this.name;
    }
    
    public boolean apply(@Nullable EnumFacing p_apply_1_) {
      return (p_apply_1_ != null && p_apply_1_.getAxis() == this);
    }
    
    public EnumFacing.Plane getPlane() {
      return this.plane;
    }
    
    public String getName() {
      return this.name;
    }
  }
  
  public enum AxisDirection {
    POSITIVE(1, "Towards positive"),
    NEGATIVE(-1, "Towards negative");
    
    private final int offset;
    
    private final String description;
    
    AxisDirection(int offset, String description) {
      this.offset = offset;
      this.description = description;
    }
    
    public int getOffset() {
      return this.offset;
    }
    
    public String toString() {
      return this.description;
    }
  }
  
  public enum Plane implements Predicate<EnumFacing>, Iterable<EnumFacing> {
    HORIZONTAL, VERTICAL;
    
    public EnumFacing[] facings() {
      switch (this) {
        case null:
          return new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
        case VERTICAL:
          return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
      } 
      throw new Error("Someone's been tampering with the universe!");
    }
    
    public EnumFacing random(Random rand) {
      EnumFacing[] aenumfacing = facings();
      return aenumfacing[rand.nextInt(aenumfacing.length)];
    }
    
    public boolean apply(@Nullable EnumFacing p_apply_1_) {
      return (p_apply_1_ != null && p_apply_1_.getAxis().getPlane() == this);
    }
    
    public Iterator<EnumFacing> iterator() {
      return (Iterator<EnumFacing>)Iterators.forArray((Object[])facings());
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraf\\util\EnumFacing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */