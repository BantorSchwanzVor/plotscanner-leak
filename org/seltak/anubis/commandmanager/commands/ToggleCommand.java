package org.seltak.anubis.commandmanager.commands;

import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;
import org.seltak.anubis.module.Module;

public class ToggleCommand extends Command {
  public ToggleCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(String[] args) {
    if (args.length == 2 && args[0].equalsIgnoreCase(".t"))
      for (Module module : Anubis.moduleManager.moduleList) {
        if (module.getName().equalsIgnoreCase(args[1])) {
          module.toggle();
          Anubis.sendMessage(String.valueOf(module.getName()) + " is now " + (module.isEnabled() ? "enabled." : "disabled"));
        } 
      }  
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\ToggleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */