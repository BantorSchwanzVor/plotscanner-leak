package net.minecraft.client.renderer.chunk;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IntegerCache;
import net.minecraft.util.math.BlockPos;

public class VisGraph {
  private static final int DX = (int)Math.pow(16.0D, 0.0D);
  
  private static final int DZ = (int)Math.pow(16.0D, 1.0D);
  
  private static final int DY = (int)Math.pow(16.0D, 2.0D);
  
  private final BitSet bitSet = new BitSet(4096);
  
  private static final int[] INDEX_OF_EDGES = new int[1352];
  
  private int empty = 4096;
  
  public void setOpaqueCube(BlockPos pos) {
    this.bitSet.set(getIndex(pos), true);
    this.empty--;
  }
  
  private static int getIndex(BlockPos pos) {
    return getIndex(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF);
  }
  
  private static int getIndex(int x, int y, int z) {
    return x << 0 | y << 8 | z << 4;
  }
  
  public SetVisibility computeVisibility() {
    SetVisibility setvisibility = new SetVisibility();
    if (4096 - this.empty < 256) {
      setvisibility.setAllVisible(true);
    } else if (this.empty == 0) {
      setvisibility.setAllVisible(false);
    } else {
      byte b;
      int i;
      int[] arrayOfInt;
      for (i = (arrayOfInt = INDEX_OF_EDGES).length, b = 0; b < i; ) {
        int j = arrayOfInt[b];
        if (!this.bitSet.get(j))
          setvisibility.setManyVisible(floodFill(j)); 
        b++;
      } 
    } 
    return setvisibility;
  }
  
  public Set<EnumFacing> getVisibleFacings(BlockPos pos) {
    return floodFill(getIndex(pos));
  }
  
  private Set<EnumFacing> floodFill(int p_178604_1_) {
    Set<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);
    ArrayDeque<Integer> arraydeque = new ArrayDeque(384);
    arraydeque.add(IntegerCache.getInteger(p_178604_1_));
    this.bitSet.set(p_178604_1_, true);
    while (!arraydeque.isEmpty()) {
      int i = ((Integer)arraydeque.poll()).intValue();
      addEdges(i, set);
      byte b;
      int j;
      EnumFacing[] arrayOfEnumFacing;
      for (j = (arrayOfEnumFacing = EnumFacing.VALUES).length, b = 0; b < j; ) {
        EnumFacing enumfacing = arrayOfEnumFacing[b];
        int k = getNeighborIndexAtFace(i, enumfacing);
        if (k >= 0 && !this.bitSet.get(k)) {
          this.bitSet.set(k, true);
          arraydeque.add(IntegerCache.getInteger(k));
        } 
        b++;
      } 
    } 
    return set;
  }
  
  private void addEdges(int p_178610_1_, Set<EnumFacing> p_178610_2_) {
    int i = p_178610_1_ >> 0 & 0xF;
    if (i == 0) {
      p_178610_2_.add(EnumFacing.WEST);
    } else if (i == 15) {
      p_178610_2_.add(EnumFacing.EAST);
    } 
    int j = p_178610_1_ >> 8 & 0xF;
    if (j == 0) {
      p_178610_2_.add(EnumFacing.DOWN);
    } else if (j == 15) {
      p_178610_2_.add(EnumFacing.UP);
    } 
    int k = p_178610_1_ >> 4 & 0xF;
    if (k == 0) {
      p_178610_2_.add(EnumFacing.NORTH);
    } else if (k == 15) {
      p_178610_2_.add(EnumFacing.SOUTH);
    } 
  }
  
  private int getNeighborIndexAtFace(int p_178603_1_, EnumFacing p_178603_2_) {
    switch (p_178603_2_) {
      case null:
        if ((p_178603_1_ >> 8 & 0xF) == 0)
          return -1; 
        return p_178603_1_ - DY;
      case UP:
        if ((p_178603_1_ >> 8 & 0xF) == 15)
          return -1; 
        return p_178603_1_ + DY;
      case NORTH:
        if ((p_178603_1_ >> 4 & 0xF) == 0)
          return -1; 
        return p_178603_1_ - DZ;
      case SOUTH:
        if ((p_178603_1_ >> 4 & 0xF) == 15)
          return -1; 
        return p_178603_1_ + DZ;
      case WEST:
        if ((p_178603_1_ >> 0 & 0xF) == 0)
          return -1; 
        return p_178603_1_ - DX;
      case EAST:
        if ((p_178603_1_ >> 0 & 0xF) == 15)
          return -1; 
        return p_178603_1_ + DX;
    } 
    return -1;
  }
  
  static {
    int i = 0;
    int j = 15;
    int k = 0;
    for (int l = 0; l < 16; l++) {
      for (int i1 = 0; i1 < 16; i1++) {
        for (int j1 = 0; j1 < 16; j1++) {
          if (l == 0 || l == 15 || i1 == 0 || i1 == 15 || j1 == 0 || j1 == 15)
            INDEX_OF_EDGES[k++] = getIndex(l, i1, j1); 
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\chunk\VisGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */