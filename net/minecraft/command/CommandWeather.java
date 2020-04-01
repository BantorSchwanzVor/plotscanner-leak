package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase {
  public String getCommandName() {
    return "weather";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.weather.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length >= 1 && args.length <= 2) {
      int i = (300 + (new Random()).nextInt(600)) * 20;
      if (args.length >= 2)
        i = parseInt(args[1], 1, 1000000) * 20; 
      WorldServer worldServer = server.worldServers[0];
      WorldInfo worldinfo = worldServer.getWorldInfo();
      if ("clear".equalsIgnoreCase(args[0])) {
        worldinfo.setCleanWeatherTime(i);
        worldinfo.setRainTime(0);
        worldinfo.setThunderTime(0);
        worldinfo.setRaining(false);
        worldinfo.setThundering(false);
        notifyCommandListener(sender, this, "commands.weather.clear", new Object[0]);
      } else if ("rain".equalsIgnoreCase(args[0])) {
        worldinfo.setCleanWeatherTime(0);
        worldinfo.setRainTime(i);
        worldinfo.setThunderTime(i);
        worldinfo.setRaining(true);
        worldinfo.setThundering(false);
        notifyCommandListener(sender, this, "commands.weather.rain", new Object[0]);
      } else {
        if (!"thunder".equalsIgnoreCase(args[0]))
          throw new WrongUsageException("commands.weather.usage", new Object[0]); 
        worldinfo.setCleanWeatherTime(0);
        worldinfo.setRainTime(i);
        worldinfo.setThunderTime(i);
        worldinfo.setRaining(true);
        worldinfo.setThundering(true);
        notifyCommandListener(sender, this, "commands.weather.thunder", new Object[0]);
      } 
    } else {
      throw new WrongUsageException("commands.weather.usage", new Object[0]);
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "clear", "rain", "thunder" }) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandWeather.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */