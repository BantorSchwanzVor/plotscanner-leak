package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ClosestSquaredHeuristic implements AStarHeuristic {
  public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
    float dx = (tx - x);
    float dy = (ty - y);
    return dx * dx + dy * dy;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\pathfinding\heuristics\ClosestSquaredHeuristic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */