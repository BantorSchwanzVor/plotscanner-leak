package org.newdawn.slick.command;

public class BasicCommand implements Command {
  private String name;
  
  public BasicCommand(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int hashCode() {
    return this.name.hashCode();
  }
  
  public boolean equals(Object other) {
    if (other instanceof BasicCommand)
      return ((BasicCommand)other).name.equals(this.name); 
    return false;
  }
  
  public String toString() {
    return "[Command=" + this.name + "]";
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\command\BasicCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */