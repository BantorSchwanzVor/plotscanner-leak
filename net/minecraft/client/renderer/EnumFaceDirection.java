package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public enum EnumFaceDirection {
  DOWN(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null) }),
  UP(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) }),
  NORTH(new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) }),
  SOUTH(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null) }),
  WEST(new VertexInformation[] { new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.WEST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null) }),
  EAST(new VertexInformation[] { new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.SOUTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.DOWN_INDEX, Constants.NORTH_INDEX, null), new VertexInformation(Constants.EAST_INDEX, Constants.UP_INDEX, Constants.NORTH_INDEX, null) });
  
  private static final EnumFaceDirection[] FACINGS;
  
  private final VertexInformation[] vertexInfos;
  
  static {
    FACINGS = new EnumFaceDirection[6];
    FACINGS[Constants.DOWN_INDEX] = DOWN;
    FACINGS[Constants.UP_INDEX] = UP;
    FACINGS[Constants.NORTH_INDEX] = NORTH;
    FACINGS[Constants.SOUTH_INDEX] = SOUTH;
    FACINGS[Constants.WEST_INDEX] = WEST;
    FACINGS[Constants.EAST_INDEX] = EAST;
  }
  
  public static EnumFaceDirection getFacing(EnumFacing facing) {
    return FACINGS[facing.getIndex()];
  }
  
  EnumFaceDirection(VertexInformation[] vertexInfosIn) {
    this.vertexInfos = vertexInfosIn;
  }
  
  public VertexInformation getVertexInformation(int index) {
    return this.vertexInfos[index];
  }
  
  public static final class Constants {
    public static final int SOUTH_INDEX = EnumFacing.SOUTH.getIndex();
    
    public static final int UP_INDEX = EnumFacing.UP.getIndex();
    
    public static final int EAST_INDEX = EnumFacing.EAST.getIndex();
    
    public static final int NORTH_INDEX = EnumFacing.NORTH.getIndex();
    
    public static final int DOWN_INDEX = EnumFacing.DOWN.getIndex();
    
    public static final int WEST_INDEX = EnumFacing.WEST.getIndex();
  }
  
  public static class VertexInformation {
    public final int xIndex;
    
    public final int yIndex;
    
    public final int zIndex;
    
    private VertexInformation(int xIndexIn, int yIndexIn, int zIndexIn) {
      this.xIndex = xIndexIn;
      this.yIndex = yIndexIn;
      this.zIndex = zIndexIn;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\EnumFaceDirection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */