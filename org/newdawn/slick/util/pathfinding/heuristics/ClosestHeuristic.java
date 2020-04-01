package org.newdawn.slick.util.pathfinding.heuristics;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ClosestHeuristic implements AStarHeuristic {
  public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {
    float dx = (tx - x);
    float dy = (ty - y);
    float result = (float)Math.sqrt((dx * dx + dy * dy));
    return result;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slic\\util\pathfinding\heuristics\ClosestHeuristic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */