package org.newdawn.slick.command;

public class ControllerDirectionControl extends ControllerControl {
  public static final Direction LEFT = new Direction(1);
  
  public static final Direction UP = new Direction(3);
  
  public static final Direction DOWN = new Direction(4);
  
  public static final Direction RIGHT = new Direction(2);
  
  public ControllerDirectionControl(int controllerIndex, Direction dir) {
    super(controllerIndex, dir.event, 0);
  }
  
  private static class Direction {
    private int event;
    
    public Direction(int event) {
      this.event = event;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\command\ControllerDirectionControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */