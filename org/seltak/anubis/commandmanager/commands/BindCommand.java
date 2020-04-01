package org.seltak.anubis.commandmanager.commands;

import org.lwjgl.input.Keyboard;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;
import org.seltak.anubis.module.Module;

public class BindCommand extends Command {
  public BindCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(String[] args) {
    try {
      for (Module module : Anubis.moduleManager.moduleList) {
        if (args.length == 3 && module.getName().equalsIgnoreCase(args[1])) {
          module.setKeyCode(Keyboard.getKeyIndex(args[2].toUpperCase()));
          Anubis.sendMessage(String.valueOf(module.getName()) + " set to " + Keyboard.getKeyName(Keyboard.getKeyIndex(args[2].toUpperCase())));
          break;
        } 
        if (args.length != 3) {
          Anubis.sendMessage(getUsage());
          break;
        } 
      } 
    } catch (Exception exception) {}
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\BindCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */