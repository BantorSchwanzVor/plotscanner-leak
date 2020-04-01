package org.seltak.anubis.commandmanager;

public abstract class Command {
  private String name;
  
  private String description;
  
  private String usage;
  
  public String getUsage() {
    return this.usage;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public Command(String name, String description, String usage) {
    this.name = name;
    this.description = description;
    this.usage = usage;
  }
  
  public abstract void onCommand(String[] paramArrayOfString);
  
  public String getName() {
    return this.name;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */