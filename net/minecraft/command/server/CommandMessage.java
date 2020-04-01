package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends CommandBase {
  public List<String> getCommandAliases() {
    return Arrays.asList(new String[] { "w", "msg" });
  }
  
  public String getCommandName() {
    return "tell";
  }
  
  public int getRequiredPermissionLevel() {
    return 0;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.message.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length < 2)
      throw new WrongUsageException("commands.message.usage", new Object[0]); 
    EntityPlayerMP entityPlayerMP = getPlayer(server, sender, args[0]);
    if (entityPlayerMP == sender)
      throw new PlayerNotFoundException("commands.message.sameTarget"); 
    ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 1, !(sender instanceof net.minecraft.entity.player.EntityPlayer));
    TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.message.display.incoming", new Object[] { sender.getDisplayName(), itextcomponent.createCopy() });
    TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.message.display.outgoing", new Object[] { entityPlayerMP.getDisplayName(), itextcomponent.createCopy() });
    textcomponenttranslation.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
    textcomponenttranslation1.getStyle().setColor(TextFormatting.GRAY).setItalic(Boolean.valueOf(true));
    entityPlayerMP.addChatMessage((ITextComponent)textcomponenttranslation);
    sender.addChatMessage((ITextComponent)textcomponenttranslation1);
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (index == 0);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\server\CommandMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */