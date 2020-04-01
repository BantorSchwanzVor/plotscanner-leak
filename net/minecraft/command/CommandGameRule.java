package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameRules;

public class CommandGameRule extends CommandBase {
  public String getCommandName() {
    return "gamerule";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.gamerule.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    String s2;
    GameRules gamerules = getOverWorldGameRules(server);
    String s = (args.length > 0) ? args[0] : "";
    String s1 = (args.length > 1) ? buildString(args, 1) : "";
    switch (args.length) {
      case 0:
        sender.addChatMessage((ITextComponent)new TextComponentString(joinNiceString((Object[])gamerules.getRules())));
        return;
      case 1:
        if (!gamerules.hasRule(s))
          throw new CommandException("commands.gamerule.norule", new Object[] { s }); 
        s2 = gamerules.getString(s);
        sender.addChatMessage((new TextComponentString(s)).appendText(" = ").appendText(s2));
        sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, gamerules.getInt(s));
        return;
    } 
    if (gamerules.areSameType(s, GameRules.ValueType.BOOLEAN_VALUE) && !"true".equals(s1) && !"false".equals(s1))
      throw new CommandException("commands.generic.boolean.invalid", new Object[] { s1 }); 
    gamerules.setOrCreateGameRule(s, s1);
    notifyGameRuleChange(gamerules, s, server);
    notifyCommandListener(sender, this, "commands.gamerule.success", new Object[] { s, s1 });
  }
  
  public static void notifyGameRuleChange(GameRules rules, String p_184898_1_, MinecraftServer server) {
    if ("reducedDebugInfo".equals(p_184898_1_)) {
      byte b0 = (byte)(rules.getBoolean(p_184898_1_) ? 22 : 23);
      for (EntityPlayerMP entityplayermp : server.getPlayerList().getPlayerList())
        entityplayermp.connection.sendPacket((Packet)new SPacketEntityStatus((Entity)entityplayermp, b0)); 
    } 
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, getOverWorldGameRules(server).getRules()); 
    if (args.length == 2) {
      GameRules gamerules = getOverWorldGameRules(server);
      if (gamerules.areSameType(args[0], GameRules.ValueType.BOOLEAN_VALUE))
        return getListOfStringsMatchingLastWord(args, new String[] { "true", "false" }); 
      if (gamerules.areSameType(args[0], GameRules.ValueType.FUNCTION))
        return getListOfStringsMatchingLastWord(args, server.func_193030_aL().func_193066_d().keySet()); 
    } 
    return Collections.emptyList();
  }
  
  private GameRules getOverWorldGameRules(MinecraftServer server) {
    return server.worldServerForDimension(0).getGameRules();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandGameRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */