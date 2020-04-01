package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.border.WorldBorder;

public class CommandWorldBorder extends CommandBase {
  public String getCommandName() {
    return "worldborder";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.worldborder.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length < 1)
      throw new WrongUsageException("commands.worldborder.usage", new Object[0]); 
    WorldBorder worldborder = getWorldBorder(server);
    if ("set".equals(args[0])) {
      if (args.length != 2 && args.length != 3)
        throw new WrongUsageException("commands.worldborder.set.usage", new Object[0]); 
      double d0 = worldborder.getTargetSize();
      double d2 = parseDouble(args[1], 1.0D, 6.0E7D);
      long i = (args.length > 2) ? (parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L;
      if (i > 0L) {
        worldborder.setTransition(d0, d2, i);
        if (d0 > d2) {
          notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }), Long.toString(i / 1000L) });
        } else {
          notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }), Long.toString(i / 1000L) });
        } 
      } else {
        worldborder.setTransition(d2);
        notifyCommandListener(sender, this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d2) }), String.format("%.1f", new Object[] { Double.valueOf(d0) }) });
      } 
    } else if ("add".equals(args[0])) {
      if (args.length != 2 && args.length != 3)
        throw new WrongUsageException("commands.worldborder.add.usage", new Object[0]); 
      double d4 = worldborder.getDiameter();
      double d8 = d4 + parseDouble(args[1], -d4, 6.0E7D - d4);
      long j1 = worldborder.getTimeUntilTarget() + ((args.length > 2) ? (parseLong(args[2], 0L, 9223372036854775L) * 1000L) : 0L);
      if (j1 > 0L) {
        worldborder.setTransition(d4, d8, j1);
        if (d4 > d8) {
          notifyCommandListener(sender, this, "commands.worldborder.setSlowly.shrink.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }), Long.toString(j1 / 1000L) });
        } else {
          notifyCommandListener(sender, this, "commands.worldborder.setSlowly.grow.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }), Long.toString(j1 / 1000L) });
        } 
      } else {
        worldborder.setTransition(d8);
        notifyCommandListener(sender, this, "commands.worldborder.set.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d8) }), String.format("%.1f", new Object[] { Double.valueOf(d4) }) });
      } 
    } else if ("center".equals(args[0])) {
      if (args.length != 3)
        throw new WrongUsageException("commands.worldborder.center.usage", new Object[0]); 
      BlockPos blockpos = sender.getPosition();
      double d1 = parseDouble(blockpos.getX() + 0.5D, args[1], true);
      double d3 = parseDouble(blockpos.getZ() + 0.5D, args[2], true);
      worldborder.setCenter(d1, d3);
      notifyCommandListener(sender, this, "commands.worldborder.center.success", new Object[] { Double.valueOf(d1), Double.valueOf(d3) });
    } else if ("damage".equals(args[0])) {
      if (args.length < 2)
        throw new WrongUsageException("commands.worldborder.damage.usage", new Object[0]); 
      if ("buffer".equals(args[1])) {
        if (args.length != 3)
          throw new WrongUsageException("commands.worldborder.damage.buffer.usage", new Object[0]); 
        double d5 = parseDouble(args[2], 0.0D);
        double d9 = worldborder.getDamageBuffer();
        worldborder.setDamageBuffer(d5);
        notifyCommandListener(sender, this, "commands.worldborder.damage.buffer.success", new Object[] { String.format("%.1f", new Object[] { Double.valueOf(d5) }), String.format("%.1f", new Object[] { Double.valueOf(d9) }) });
      } else if ("amount".equals(args[1])) {
        if (args.length != 3)
          throw new WrongUsageException("commands.worldborder.damage.amount.usage", new Object[0]); 
        double d6 = parseDouble(args[2], 0.0D);
        double d10 = worldborder.getDamageAmount();
        worldborder.setDamageAmount(d6);
        notifyCommandListener(sender, this, "commands.worldborder.damage.amount.success", new Object[] { String.format("%.2f", new Object[] { Double.valueOf(d6) }), String.format("%.2f", new Object[] { Double.valueOf(d10) }) });
      } 
    } else if ("warning".equals(args[0])) {
      if (args.length < 2)
        throw new WrongUsageException("commands.worldborder.warning.usage", new Object[0]); 
      if ("time".equals(args[1])) {
        if (args.length != 3)
          throw new WrongUsageException("commands.worldborder.warning.time.usage", new Object[0]); 
        int j = parseInt(args[2], 0);
        int l = worldborder.getWarningTime();
        worldborder.setWarningTime(j);
        notifyCommandListener(sender, this, "commands.worldborder.warning.time.success", new Object[] { Integer.valueOf(j), Integer.valueOf(l) });
      } else if ("distance".equals(args[1])) {
        if (args.length != 3)
          throw new WrongUsageException("commands.worldborder.warning.distance.usage", new Object[0]); 
        int k = parseInt(args[2], 0);
        int i1 = worldborder.getWarningDistance();
        worldborder.setWarningDistance(k);
        notifyCommandListener(sender, this, "commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(k), Integer.valueOf(i1) });
      } 
    } else {
      if (!"get".equals(args[0]))
        throw new WrongUsageException("commands.worldborder.usage", new Object[0]); 
      double d7 = worldborder.getDiameter();
      sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, MathHelper.floor(d7 + 0.5D));
      sender.addChatMessage((ITextComponent)new TextComponentTranslation("commands.worldborder.get.success", new Object[] { String.format("%.0f", new Object[] { Double.valueOf(d7) }) }));
    } 
  }
  
  protected WorldBorder getWorldBorder(MinecraftServer server) {
    return server.worldServers[0].getWorldBorder();
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, new String[] { "set", "center", "damage", "warning", "add", "get" }); 
    if (args.length == 2 && "damage".equals(args[0]))
      return getListOfStringsMatchingLastWord(args, new String[] { "buffer", "amount" }); 
    if (args.length >= 2 && args.length <= 3 && "center".equals(args[0]))
      return getTabCompletionCoordinateXZ(args, 1, pos); 
    return (args.length == 2 && "warning".equals(args[0])) ? getListOfStringsMatchingLastWord(args, new String[] { "time", "distance" }) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandWorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */