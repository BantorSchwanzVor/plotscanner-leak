package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty extends CommandBase {
  public String getCommandName() {
    return "difficulty";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.difficulty.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length <= 0)
      throw new WrongUsageException("commands.difficulty.usage", new Object[0]); 
    EnumDifficulty enumdifficulty = getDifficultyFromCommand(args[0]);
    server.setDifficultyForAllWorlds(enumdifficulty);
    notifyCommandListener(sender, this, "commands.difficulty.success", new Object[] { new TextComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0]) });
  }
  
  protected EnumDifficulty getDifficultyFromCommand(String difficultyString) throws CommandException, NumberInvalidException {
    if (!"peaceful".equalsIgnoreCase(difficultyString) && !"p".equalsIgnoreCase(difficultyString)) {
      if (!"easy".equalsIgnoreCase(difficultyString) && !"e".equalsIgnoreCase(difficultyString)) {
        if (!"normal".equalsIgnoreCase(difficultyString) && !"n".equalsIgnoreCase(difficultyString))
          return (!"hard".equalsIgnoreCase(difficultyString) && !"h".equalsIgnoreCase(difficultyString)) ? EnumDifficulty.getDifficultyEnum(parseInt(difficultyString, 0, 3)) : EnumDifficulty.HARD; 
        return EnumDifficulty.NORMAL;
      } 
      return EnumDifficulty.EASY;
    } 
    return EnumDifficulty.PEACEFUL;
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "peaceful", "easy", "normal", "hard" }) : Collections.<String>emptyList();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\CommandDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */