package org.seltak.anubis.commandmanager;

import java.util.ArrayList;
import org.seltak.anubis.commandmanager.commands.BindCommand;
import org.seltak.anubis.commandmanager.commands.FindPlotsCommand;
import org.seltak.anubis.commandmanager.commands.FriendCommand;
import org.seltak.anubis.commandmanager.commands.PlotCheckerDatabaseCommand;
import org.seltak.anubis.commandmanager.commands.PlotPriceCommand;
import org.seltak.anubis.commandmanager.commands.ToggleCommand;

public class CommandManager {
  public ArrayList<Command> commandList = new ArrayList<>();
  
  public void init() {
    this.commandList.add(new BindCommand("bind", "Binds a module to a keybind", ".bind <Module> <Keybind>"));
    this.commandList.add(new FriendCommand("friend", "Add a friend that will not be attacked", ".friend add <Name>"));
    this.commandList.add(new ToggleCommand("toggle", "Toggle Module", ".toggle <Module>"));
    this.commandList.add(new PlotPriceCommand("plotprice", "Check the plot price", ".plotprice"));
    this.commandList.add(new FindPlotsCommand("findplots", "Searches plots from a txt file", ".findplots start"));
    this.commandList.add(new PlotCheckerDatabaseCommand("scanplots", "Scanne Grundstücke und speichert sie in einer Datenbank.", ".scanplots"));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */