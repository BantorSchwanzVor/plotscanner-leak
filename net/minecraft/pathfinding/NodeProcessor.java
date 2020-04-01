package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor {
  protected IBlockAccess blockaccess;
  
  protected EntityLiving entity;
  
  protected final IntHashMap<PathPoint> pointMap = new IntHashMap();
  
  protected int entitySizeX;
  
  protected int entitySizeY;
  
  protected int entitySizeZ;
  
  protected boolean canEnterDoors;
  
  protected boolean canBreakDoors;
  
  protected boolean canSwim;
  
  public void initProcessor(IBlockAccess sourceIn, EntityLiving mob) {
    this.blockaccess = sourceIn;
    this.entity = mob;
    this.pointMap.clearMap();
    this.entitySizeX = MathHelper.floor(mob.width + 1.0F);
    this.entitySizeY = MathHelper.floor(mob.height + 1.0F);
    this.entitySizeZ = MathHelper.floor(mob.width + 1.0F);
  }
  
  public void postProcess() {
    this.blockaccess = null;
    this.entity = null;
  }
  
  protected PathPoint openPoint(int x, int y, int z) {
    int i = PathPoint.makeHash(x, y, z);
    PathPoint pathpoint = (PathPoint)this.pointMap.lookup(i);
    if (pathpoint == null) {
      pathpoint = new PathPoint(x, y, z);
      this.pointMap.addKey(i, pathpoint);
    } 
    return pathpoint;
  }
  
  public abstract PathPoint getStart();
  
  public abstract PathPoint getPathPointToCoords(double paramDouble1, double paramDouble2, double paramDouble3);
  
  public abstract int findPathOptions(PathPoint[] paramArrayOfPathPoint, PathPoint paramPathPoint1, PathPoint paramPathPoint2, float paramFloat);
  
  public abstract PathNodeType getPathNodeType(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, EntityLiving paramEntityLiving, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract PathNodeType getPathNodeType(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3);
  
  public void setCanEnterDoors(boolean canEnterDoorsIn) {
    this.canEnterDoors = canEnterDoorsIn;
  }
  
  public void setCanBreakDoors(boolean canBreakDoorsIn) {
    this.canBreakDoors = canBreakDoorsIn;
  }
  
  public void setCanSwim(boolean canSwimIn) {
    this.canSwim = canSwimIn;
  }
  
  public boolean getCanEnterDoors() {
    return this.canEnterDoors;
  }
  
  public boolean getCanBreakDoors() {
    return this.canBreakDoors;
  }
  
  public boolean getCanSwim() {
    return this.canSwim;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\pathfinding\NodeProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */