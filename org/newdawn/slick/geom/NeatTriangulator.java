package org.newdawn.slick.geom;

public class NeatTriangulator implements Triangulator {
  static final float EPSILON = 1.0E-6F;
  
  private float[] pointsX;
  
  private float[] pointsY;
  
  private int numPoints;
  
  private Edge[] edges;
  
  private int[] V;
  
  private int numEdges;
  
  private Triangle[] triangles;
  
  private int numTriangles;
  
  private float offset = 1.0E-6F;
  
  public NeatTriangulator() {
    this.pointsX = new float[100];
    this.pointsY = new float[100];
    this.numPoints = 0;
    this.edges = new Edge[100];
    this.numEdges = 0;
    this.triangles = new Triangle[100];
    this.numTriangles = 0;
  }
  
  public void clear() {
    this.numPoints = 0;
    this.numEdges = 0;
    this.numTriangles = 0;
  }
  
  private int findEdge(int i, int j) {
    int k;
    int l;
    if (i < j) {
      k = i;
      l = j;
    } else {
      k = j;
      l = i;
    } 
    for (int i1 = 0; i1 < this.numEdges; i1++) {
      if ((this.edges[i1]).v0 == k && (this.edges[i1]).v1 == l)
        return i1; 
    } 
    return -1;
  }
  
  private void addEdge(int i, int j, int k) {
    int m, n;
    Edge edge;
    int l, i1, l1 = findEdge(i, j);
    if (l1 < 0) {
      if (this.numEdges == this.edges.length) {
        Edge[] aedge = new Edge[this.edges.length * 2];
        System.arraycopy(this.edges, 0, aedge, 0, this.numEdges);
        this.edges = aedge;
      } 
      m = -1;
      n = -1;
      l1 = this.numEdges++;
      edge = this.edges[l1] = new Edge();
    } else {
      edge = this.edges[l1];
      m = edge.t0;
      n = edge.t1;
    } 
    if (i < j) {
      l = i;
      i1 = j;
      m = k;
    } else {
      l = j;
      i1 = i;
      n = k;
    } 
    edge.v0 = l;
    edge.v1 = i1;
    edge.t0 = m;
    edge.t1 = n;
    edge.suspect = true;
  }
  
  private void deleteEdge(int i, int j) throws InternalException {
    int k;
    if ((k = findEdge(i, j)) < 0)
      throw new InternalException("Attempt to delete unknown edge"); 
    this.edges[k] = this.edges[--this.numEdges];
  }
  
  void markSuspect(int i, int j, boolean flag) throws InternalException {
    int k;
    if ((k = findEdge(i, j)) < 0)
      throw new InternalException("Attempt to mark unknown edge"); 
    (this.edges[k]).suspect = flag;
  }
  
  private Edge chooseSuspect() {
    for (int i = 0; i < this.numEdges; i++) {
      Edge edge = this.edges[i];
      if (edge.suspect) {
        edge.suspect = false;
        if (edge.t0 >= 0 && edge.t1 >= 0)
          return edge; 
      } 
    } 
    return null;
  }
  
  private static float rho(float f, float f1, float f2, float f3, float f4, float f5) {
    float f6 = f4 - f2;
    float f7 = f5 - f3;
    float f8 = f - f4;
    float f9 = f1 - f5;
    float f18 = f6 * f9 - f7 * f8;
    if (f18 > 0.0F) {
      if (f18 < 1.0E-6F)
        f18 = 1.0E-6F; 
      float f12 = f6 * f6;
      float f13 = f7 * f7;
      float f14 = f8 * f8;
      float f15 = f9 * f9;
      float f10 = f2 - f;
      float f11 = f3 - f1;
      float f16 = f10 * f10;
      float f17 = f11 * f11;
      return (f12 + f13) * (f14 + f15) * (f16 + f17) / f18 * f18;
    } 
    return -1.0F;
  }
  
  private static boolean insideTriangle(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
    float f8 = f4 - f2;
    float f9 = f5 - f3;
    float f10 = f - f4;
    float f11 = f1 - f5;
    float f12 = f2 - f;
    float f13 = f3 - f1;
    float f14 = f6 - f;
    float f15 = f7 - f1;
    float f16 = f6 - f2;
    float f17 = f7 - f3;
    float f18 = f6 - f4;
    float f19 = f7 - f5;
    float f22 = f8 * f17 - f9 * f16;
    float f20 = f12 * f15 - f13 * f14;
    float f21 = f10 * f19 - f11 * f18;
    return (f22 >= 0.0D && f21 >= 0.0D && f20 >= 0.0D);
  }
  
  private boolean snip(int i, int j, int k, int l) {
    float f = this.pointsX[this.V[i]];
    float f1 = this.pointsY[this.V[i]];
    float f2 = this.pointsX[this.V[j]];
    float f3 = this.pointsY[this.V[j]];
    float f4 = this.pointsX[this.V[k]];
    float f5 = this.pointsY[this.V[k]];
    if (1.0E-6F > (f2 - f) * (f5 - f1) - (f3 - f1) * (f4 - f))
      return false; 
    for (int i1 = 0; i1 < l; i1++) {
      if (i1 != i && i1 != j && i1 != k) {
        float f6 = this.pointsX[this.V[i1]];
        float f7 = this.pointsY[this.V[i1]];
        if (insideTriangle(f, f1, f2, f3, f4, f5, f6, f7))
          return false; 
      } 
    } 
    return true;
  }
  
  private float area() {
    float f = 0.0F;
    int i = this.numPoints - 1;
    for (int j = 0; j < this.numPoints; ) {
      f += this.pointsX[i] * this.pointsY[j] - this.pointsY[i] * this.pointsX[j];
      i = j++;
    } 
    return f * 0.5F;
  }
  
  public void basicTriangulation() throws InternalException {
    int i = this.numPoints;
    if (i < 3)
      return; 
    this.numEdges = 0;
    this.numTriangles = 0;
    this.V = new int[i];
    if (0.0D < area()) {
      for (int k = 0; k < i; k++)
        this.V[k] = k; 
    } else {
      for (int l = 0; l < i; l++)
        this.V[l] = this.numPoints - 1 - l; 
    } 
    int k1 = 2 * i;
    int i1 = i - 1;
    while (i > 2) {
      if (k1-- <= 0)
        throw new InternalException("Bad polygon"); 
      int j = i1;
      if (i <= j)
        j = 0; 
      i1 = j + 1;
      if (i <= i1)
        i1 = 0; 
      int j1 = i1 + 1;
      if (i <= j1)
        j1 = 0; 
      if (snip(j, i1, j1, i)) {
        int l1 = this.V[j];
        int i2 = this.V[i1];
        int j2 = this.V[j1];
        if (this.numTriangles == this.triangles.length) {
          Triangle[] atriangle = new Triangle[this.triangles.length * 2];
          System.arraycopy(this.triangles, 0, atriangle, 0, this.numTriangles);
          this.triangles = atriangle;
        } 
        this.triangles[this.numTriangles] = new Triangle(l1, i2, j2);
        addEdge(l1, i2, this.numTriangles);
        addEdge(i2, j2, this.numTriangles);
        addEdge(j2, l1, this.numTriangles);
        this.numTriangles++;
        int k2 = i1;
        for (int l2 = i1 + 1; l2 < i; l2++) {
          this.V[k2] = this.V[l2];
          k2++;
        } 
        i--;
        k1 = 2 * i;
      } 
    } 
    this.V = null;
  }
  
  private void optimize() throws InternalException {
    Edge edge;
    while ((edge = chooseSuspect()) != null) {
      int i1 = edge.v0;
      int k1 = edge.v1;
      int i = edge.t0;
      int j = edge.t1;
      int j1 = -1;
      int l1 = -1;
      for (int k = 0; k < 3; ) {
        int i2 = (this.triangles[i]).v[k];
        if (i1 == i2 || k1 == i2) {
          k++;
          continue;
        } 
        l1 = i2;
        break;
      } 
      for (int l = 0; l < 3; ) {
        int j2 = (this.triangles[j]).v[l];
        if (i1 == j2 || k1 == j2) {
          l++;
          continue;
        } 
        j1 = j2;
        break;
      } 
      if (-1 == j1 || -1 == l1)
        throw new InternalException("can't find quad"); 
      float f = this.pointsX[i1];
      float f1 = this.pointsY[i1];
      float f2 = this.pointsX[j1];
      float f3 = this.pointsY[j1];
      float f4 = this.pointsX[k1];
      float f5 = this.pointsY[k1];
      float f6 = this.pointsX[l1];
      float f7 = this.pointsY[l1];
      float f8 = rho(f, f1, f2, f3, f4, f5);
      float f9 = rho(f, f1, f4, f5, f6, f7);
      float f10 = rho(f2, f3, f4, f5, f6, f7);
      float f11 = rho(f2, f3, f6, f7, f, f1);
      if (0.0F > f8 || 0.0F > f9)
        throw new InternalException("original triangles backwards"); 
      if (0.0F <= f10 && 0.0F <= f11) {
        if (f8 > f9)
          f8 = f9; 
        if (f10 > f11)
          f10 = f11; 
        if (f8 > f10) {
          deleteEdge(i1, k1);
          (this.triangles[i]).v[0] = j1;
          (this.triangles[i]).v[1] = k1;
          (this.triangles[i]).v[2] = l1;
          (this.triangles[j]).v[0] = j1;
          (this.triangles[j]).v[1] = l1;
          (this.triangles[j]).v[2] = i1;
          addEdge(j1, k1, i);
          addEdge(k1, l1, i);
          addEdge(l1, j1, i);
          addEdge(l1, i1, j);
          addEdge(i1, j1, j);
          addEdge(j1, l1, j);
          markSuspect(j1, l1, false);
        } 
      } 
    } 
  }
  
  public boolean triangulate() {
    try {
      basicTriangulation();
      return true;
    } catch (InternalException e) {
      this.numEdges = 0;
      return false;
    } 
  }
  
  public void addPolyPoint(float x, float y) {
    for (int i = 0; i < this.numPoints; i++) {
      if (this.pointsX[i] == x && this.pointsY[i] == y) {
        y += this.offset;
        this.offset += 1.0E-6F;
      } 
    } 
    if (this.numPoints == this.pointsX.length) {
      float[] af = new float[this.numPoints * 2];
      System.arraycopy(this.pointsX, 0, af, 0, this.numPoints);
      this.pointsX = af;
      af = new float[this.numPoints * 2];
      System.arraycopy(this.pointsY, 0, af, 0, this.numPoints);
      this.pointsY = af;
    } 
    this.pointsX[this.numPoints] = x;
    this.pointsY[this.numPoints] = y;
    this.numPoints++;
  }
  
  class Triangle {
    int[] v;
    
    Triangle(int i, int j, int k) {
      this.v = new int[3];
      this.v[0] = i;
      this.v[1] = j;
      this.v[2] = k;
    }
  }
  
  class Edge {
    int v0 = -1;
    
    int v1 = -1;
    
    int t0 = -1;
    
    int t1 = -1;
    
    boolean suspect;
  }
  
  class InternalException extends Exception {
    public InternalException(String msg) {
      super(msg);
    }
  }
  
  public int getTriangleCount() {
    return this.numTriangles;
  }
  
  public float[] getTrianglePoint(int tri, int i) {
    float xp = this.pointsX[(this.triangles[tri]).v[i]];
    float yp = this.pointsY[(this.triangles[tri]).v[i]];
    return new float[] { xp, yp };
  }
  
  public void startHole() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\geom\NeatTriangulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */