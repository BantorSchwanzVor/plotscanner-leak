package org.newdawn.slick.util.pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.newdawn.slick.util.pathfinding.heuristics.ClosestHeuristic;

public class AStarPathFinder implements PathFinder, PathFindingContext {
  private ArrayList closed = new ArrayList();
  
  private PriorityList open = new PriorityList(null);
  
  private TileBasedMap map;
  
  private int maxSearchDistance;
  
  private Node[][] nodes;
  
  private boolean allowDiagMovement;
  
  private AStarHeuristic heuristic;
  
  private Node current;
  
  private Mover mover;
  
  private int sourceX;
  
  private int sourceY;
  
  private int distance;
  
  public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
    this(map, maxSearchDistance, allowDiagMovement, (AStarHeuristic)new ClosestHeuristic());
  }
  
  public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement, AStarHeuristic heuristic) {
    this.heuristic = heuristic;
    this.map = map;
    this.maxSearchDistance = maxSearchDistance;
    this.allowDiagMovement = allowDiagMovement;
    this.nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
    for (int x = 0; x < map.getWidthInTiles(); x++) {
      for (int y = 0; y < map.getHeightInTiles(); y++)
        this.nodes[x][y] = new Node(x, y); 
    } 
  }
  
  public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
    this.current = null;
    this.mover = mover;
    this.sourceX = tx;
    this.sourceY = ty;
    this.distance = 0;
    if (this.map.blocked(this, tx, ty))
      return null; 
    for (int x = 0; x < this.map.getWidthInTiles(); x++) {
      for (int y = 0; y < this.map.getHeightInTiles(); y++)
        this.nodes[x][y].reset(); 
    } 
    (this.nodes[sx][sy]).cost = 0.0F;
    (this.nodes[sx][sy]).depth = 0;
    this.closed.clear();
    this.open.clear();
    addToOpen(this.nodes[sx][sy]);
    (this.nodes[tx][ty]).parent = null;
    int maxDepth = 0;
    while (maxDepth < this.maxSearchDistance && this.open.size() != 0) {
      int lx = sx;
      int ly = sy;
      if (this.current != null) {
        lx = this.current.x;
        ly = this.current.y;
      } 
      this.current = getFirstInOpen();
      this.distance = this.current.depth;
      if (this.current == this.nodes[tx][ty] && 
        isValidLocation(mover, lx, ly, tx, ty))
        break; 
      removeFromOpen(this.current);
      addToClosed(this.current);
      for (int i = -1; i < 2; i++) {
        for (int y = -1; y < 2; y++) {
          if (i != 0 || y != 0)
            if (this.allowDiagMovement || 
              i == 0 || y == 0) {
              int xp = i + this.current.x;
              int yp = y + this.current.y;
              if (isValidLocation(mover, this.current.x, this.current.y, xp, yp)) {
                float nextStepCost = this.current.cost + getMovementCost(mover, this.current.x, this.current.y, xp, yp);
                Node neighbour = this.nodes[xp][yp];
                this.map.pathFinderVisited(xp, yp);
                if (nextStepCost < neighbour.cost) {
                  if (inOpenList(neighbour))
                    removeFromOpen(neighbour); 
                  if (inClosedList(neighbour))
                    removeFromClosed(neighbour); 
                } 
                if (!inOpenList(neighbour) && !inClosedList(neighbour)) {
                  neighbour.cost = nextStepCost;
                  neighbour.heuristic = getHeuristicCost(mover, xp, yp, tx, ty);
                  maxDepth = Math.max(maxDepth, neighbour.setParent(this.current));
                  addToOpen(neighbour);
                } 
              } 
            }  
        } 
      } 
    } 
    if ((this.nodes[tx][ty]).parent == null)
      return null; 
    Path path = new Path();
    Node target = this.nodes[tx][ty];
    while (target != this.nodes[sx][sy]) {
      path.prependStep(target.x, target.y);
      target = target.parent;
    } 
    path.prependStep(sx, sy);
    return path;
  }
  
  public int getCurrentX() {
    if (this.current == null)
      return -1; 
    return this.current.x;
  }
  
  public int getCurrentY() {
    if (this.current == null)
      return -1; 
    return this.current.y;
  }
  
  protected Node getFirstInOpen() {
    return (Node)this.open.first();
  }
  
  protected void addToOpen(Node node) {
    node.setOpen(true);
    this.open.add(node);
  }
  
  protected boolean inOpenList(Node node) {
    return node.isOpen();
  }
  
  protected void removeFromOpen(Node node) {
    node.setOpen(false);
    this.open.remove(node);
  }
  
  protected void addToClosed(Node node) {
    node.setClosed(true);
    this.closed.add(node);
  }
  
  protected boolean inClosedList(Node node) {
    return node.isClosed();
  }
  
  protected void removeFromClosed(Node node) {
    node.setClosed(false);
    this.closed.remove(node);
  }
  
  protected boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
    boolean invalid = !(x >= 0 && y >= 0 && x < this.map.getWidthInTiles() && y < this.map.getHeightInTiles());
    if (!invalid && (sx != x || sy != y)) {
      this.mover = mover;
      this.sourceX = sx;
      this.sourceY = sy;
      invalid = this.map.blocked(this, x, y);
    } 
    return !invalid;
  }
  
  public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
    this.mover = mover;
    this.sourceX = sx;
    this.sourceY = sy;
    return this.map.getCost(this, tx, ty);
  }
  
  public float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
    return this.heuristic.getCost(this.map, mover, x, y, tx, ty);
  }
  
  private class PriorityList {
    private List list = new LinkedList();
    
    public Object first() {
      return this.list.get(0);
    }
    
    public void clear() {
      this.list.clear();
    }
    
    public void add(Object o) {
      for (int i = 0; i < this.list.size(); i++) {
        if (((Comparable<Object>)this.list.get(i)).compareTo(o) > 0) {
          this.list.add(i, o);
          break;
        } 
      } 
      if (!this.list.contains(o))
        this.list.add(o); 
    }
    
    public void remove(Object o) {
      this.list.remove(o);
    }
    
    public int size() {
      return this.list.size();
    }
    
    public boolean contains(Object o) {
      return this.list.contains(o);
    }
    
    public String toString() {
      String temp = "{";
      for (int i = 0; i < size(); i++)
        temp = String.valueOf(temp) + this.list.get(i).toString() + ","; 
      temp = String.valueOf(temp) + "}";
      return temp;
    }
    
    private PriorityList() {}
  }
  
  private class Node implements Comparable {
    private int x;
    
    private int y;
    
    private float cost;
    
    private Node parent;
    
    private float heuristic;
    
    private int depth;
    
    private boolean open;
    
    private boolean closed;
    
    public Node(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    public int setParent(Node parent) {
      parent.depth++;
      this.parent = parent;
      return this.depth;
    }
    
    public int compareTo(Object other) {
      Node o = (Node)other;
      float f = this.heuristic + this.cost;
      float of = o.heuristic + o.cost;
      if (f < of)
        return -1; 
      if (f > of)
        return 1; 
      return 0;
    }
    
    public void setOpen(boolean open) {
      this.open = open;
    }
    
    public boolean isOpen() {
      return this.open;
    }
    
    public void setClosed(boolean closed) {
      this.closed = closed;
    }
    
    public boolean isClosed() {
      return this.closed;
    }
    
    public void reset() {
      this.closed = false;
      this.open = false;
      this.cost = 0.0F;
      this.depth = 0;
    }
    
    public String toString() {
      return "[Node " + this.x + "," + this.y + "]";
    }
  }
  
  public Mover getMover() {
    return this.mover;
  }
  
  public int getSearchDistance() {
    return this.distance;
  }
  
  public int getSourceX() {
    return this.sourceX;
  }
  
  public int getSourceY() {
    return this.sourceY;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\pathfinding\AStarPathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */