package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AdvancementCommand extends CommandBase {
  public String getCommandName() {
    return "advancement";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.advancement.usage";
  }
  
  public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
    if (args.length < 1)
      throw new WrongUsageException("commands.advancement.usage", new Object[0]); 
    ActionType advancementcommand$actiontype = ActionType.func_193536_a(args[0]);
    if (advancementcommand$actiontype != null) {
      if (args.length < 3)
        throw advancementcommand$actiontype.func_193534_a(); 
      EntityPlayerMP entityplayermp = getPlayer(server, sender, args[1]);
      Mode advancementcommand$mode = Mode.func_193547_a(args[2]);
      if (advancementcommand$mode == null)
        throw advancementcommand$actiontype.func_193534_a(); 
      func_193516_a(server, sender, args, entityplayermp, advancementcommand$actiontype, advancementcommand$mode);
    } else {
      if (!"test".equals(args[0]))
        throw new WrongUsageException("commands.advancement.usage", new Object[0]); 
      if (args.length == 3) {
        func_192552_c(sender, getPlayer(server, sender, args[1]), func_192551_a(server, args[2]));
      } else {
        if (args.length != 4)
          throw new WrongUsageException("commands.advancement.test.usage", new Object[0]); 
        func_192554_c(sender, getPlayer(server, sender, args[1]), func_192551_a(server, args[2]), args[3]);
      } 
    } 
  }
  
  private void func_193516_a(MinecraftServer p_193516_1_, ICommandSender p_193516_2_, String[] p_193516_3_, EntityPlayerMP p_193516_4_, ActionType p_193516_5_, Mode p_193516_6_) throws CommandException {
    if (p_193516_6_ == Mode.EVERYTHING) {
      if (p_193516_3_.length == 3) {
        int j = p_193516_5_.func_193532_a(p_193516_4_, p_193516_1_.func_191949_aK().func_192780_b());
        if (j == 0)
          throw p_193516_6_.func_193543_a(p_193516_5_, new Object[] { p_193516_4_.getName() }); 
        p_193516_6_.func_193546_a(p_193516_2_, this, p_193516_5_, new Object[] { p_193516_4_.getName(), Integer.valueOf(j) });
      } else {
        throw p_193516_6_.func_193544_a(p_193516_5_);
      } 
    } else {
      if (p_193516_3_.length < 4)
        throw p_193516_6_.func_193544_a(p_193516_5_); 
      Advancement advancement = func_192551_a(p_193516_1_, p_193516_3_[3]);
      if (p_193516_6_ == Mode.ONLY && p_193516_3_.length == 5) {
        String s = p_193516_3_[4];
        if (!advancement.func_192073_f().keySet().contains(s))
          throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.func_192067_g(), p_193516_3_[4] }); 
        if (!p_193516_5_.func_193535_a(p_193516_4_, advancement, s))
          throw new CommandException(String.valueOf(p_193516_5_.field_193541_d) + ".criterion.failed", new Object[] { advancement.func_192067_g(), p_193516_4_.getName(), s }); 
        notifyCommandListener(p_193516_2_, this, String.valueOf(p_193516_5_.field_193541_d) + ".criterion.success", new Object[] { advancement.func_192067_g(), p_193516_4_.getName(), s });
      } else {
        if (p_193516_3_.length != 4)
          throw p_193516_6_.func_193544_a(p_193516_5_); 
        List<Advancement> list = func_193514_a(advancement, p_193516_6_);
        int i = p_193516_5_.func_193532_a(p_193516_4_, list);
        if (i == 0)
          throw p_193516_6_.func_193543_a(p_193516_5_, new Object[] { advancement.func_192067_g(), p_193516_4_.getName() }); 
        p_193516_6_.func_193546_a(p_193516_2_, this, p_193516_5_, new Object[] { advancement.func_192067_g(), p_193516_4_.getName(), Integer.valueOf(i) });
      } 
    } 
  }
  
  private void func_193515_a(Advancement p_193515_1_, List<Advancement> p_193515_2_) {
    for (Advancement advancement : p_193515_1_.func_192069_e()) {
      p_193515_2_.add(advancement);
      func_193515_a(advancement, p_193515_2_);
    } 
  }
  
  private List<Advancement> func_193514_a(Advancement p_193514_1_, Mode p_193514_2_) {
    List<Advancement> list = Lists.newArrayList();
    if (p_193514_2_.field_193555_h)
      for (Advancement advancement = p_193514_1_.func_192070_b(); advancement != null; advancement = advancement.func_192070_b())
        list.add(advancement);  
    list.add(p_193514_1_);
    if (p_193514_2_.field_193556_i)
      func_193515_a(p_193514_1_, list); 
    return list;
  }
  
  private void func_192554_c(ICommandSender p_192554_1_, EntityPlayerMP p_192554_2_, Advancement p_192554_3_, String p_192554_4_) throws CommandException {
    PlayerAdvancements playeradvancements = p_192554_2_.func_192039_O();
    CriterionProgress criterionprogress = playeradvancements.func_192747_a(p_192554_3_).func_192106_c(p_192554_4_);
    if (criterionprogress == null)
      throw new CommandException("commands.advancement.criterionNotFound", new Object[] { p_192554_3_.func_192067_g(), p_192554_4_ }); 
    if (!criterionprogress.func_192151_a())
      throw new CommandException("commands.advancement.test.criterion.notDone", new Object[] { p_192554_2_.getName(), p_192554_3_.func_192067_g(), p_192554_4_ }); 
    notifyCommandListener(p_192554_1_, this, "commands.advancement.test.criterion.success", new Object[] { p_192554_2_.getName(), p_192554_3_.func_192067_g(), p_192554_4_ });
  }
  
  private void func_192552_c(ICommandSender p_192552_1_, EntityPlayerMP p_192552_2_, Advancement p_192552_3_) throws CommandException {
    AdvancementProgress advancementprogress = p_192552_2_.func_192039_O().func_192747_a(p_192552_3_);
    if (!advancementprogress.func_192105_a())
      throw new CommandException("commands.advancement.test.advancement.notDone", new Object[] { p_192552_2_.getName(), p_192552_3_.func_192067_g() }); 
    notifyCommandListener(p_192552_1_, this, "commands.advancement.test.advancement.success", new Object[] { p_192552_2_.getName(), p_192552_3_.func_192067_g() });
  }
  
  public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    if (args.length == 1)
      return getListOfStringsMatchingLastWord(args, new String[] { "grant", "revoke", "test" }); 
    ActionType advancementcommand$actiontype = ActionType.func_193536_a(args[0]);
    if (advancementcommand$actiontype != null) {
      if (args.length == 2)
        return getListOfStringsMatchingLastWord(args, server.getAllUsernames()); 
      if (args.length == 3)
        return getListOfStringsMatchingLastWord(args, Mode.field_193553_f); 
      Mode advancementcommand$mode = Mode.func_193547_a(args[2]);
      if (advancementcommand$mode != null && advancementcommand$mode != Mode.EVERYTHING) {
        if (args.length == 4)
          return getListOfStringsMatchingLastWord(args, func_193517_a(server)); 
        if (args.length == 5 && advancementcommand$mode == Mode.ONLY) {
          Advancement advancement = server.func_191949_aK().func_192778_a(new ResourceLocation(args[3]));
          if (advancement != null)
            return getListOfStringsMatchingLastWord(args, advancement.func_192073_f().keySet()); 
        } 
      } 
    } 
    if ("test".equals(args[0])) {
      if (args.length == 2)
        return getListOfStringsMatchingLastWord(args, server.getAllUsernames()); 
      if (args.length == 3)
        return getListOfStringsMatchingLastWord(args, func_193517_a(server)); 
      if (args.length == 4) {
        Advancement advancement1 = server.func_191949_aK().func_192778_a(new ResourceLocation(args[2]));
        if (advancement1 != null)
          return getListOfStringsMatchingLastWord(args, advancement1.func_192073_f().keySet()); 
      } 
    } 
    return Collections.emptyList();
  }
  
  private List<ResourceLocation> func_193517_a(MinecraftServer p_193517_1_) {
    List<ResourceLocation> list = Lists.newArrayList();
    for (Advancement advancement : p_193517_1_.func_191949_aK().func_192780_b())
      list.add(advancement.func_192067_g()); 
    return list;
  }
  
  public boolean isUsernameIndex(String[] args, int index) {
    return (args.length > 1 && ("grant".equals(args[0]) || "revoke".equals(args[0]) || "test".equals(args[0])) && index == 1);
  }
  
  public static Advancement func_192551_a(MinecraftServer p_192551_0_, String p_192551_1_) throws CommandException {
    Advancement advancement = p_192551_0_.func_191949_aK().func_192778_a(new ResourceLocation(p_192551_1_));
    if (advancement == null)
      throw new CommandException("commands.advancement.advancementNotFound", new Object[] { p_192551_1_ }); 
    return advancement;
  }
  
  enum ActionType {
    GRANT("grant") {
      protected boolean func_193537_a(EntityPlayerMP p_193537_1_, Advancement p_193537_2_) {
        AdvancementProgress advancementprogress = p_193537_1_.func_192039_O().func_192747_a(p_193537_2_);
        if (advancementprogress.func_192105_a())
          return false; 
        for (String s : advancementprogress.func_192107_d())
          p_193537_1_.func_192039_O().func_192750_a(p_193537_2_, s); 
        return true;
      }
      
      protected boolean func_193535_a(EntityPlayerMP p_193535_1_, Advancement p_193535_2_, String p_193535_3_) {
        return p_193535_1_.func_192039_O().func_192750_a(p_193535_2_, p_193535_3_);
      }
    },
    REVOKE("revoke") {
      protected boolean func_193537_a(EntityPlayerMP p_193537_1_, Advancement p_193537_2_) {
        AdvancementProgress advancementprogress = p_193537_1_.func_192039_O().func_192747_a(p_193537_2_);
        if (!advancementprogress.func_192108_b())
          return false; 
        for (String s : advancementprogress.func_192102_e())
          p_193537_1_.func_192039_O().func_192744_b(p_193537_2_, s); 
        return true;
      }
      
      protected boolean func_193535_a(EntityPlayerMP p_193535_1_, Advancement p_193535_2_, String p_193535_3_) {
        return p_193535_1_.func_192039_O().func_192744_b(p_193535_2_, p_193535_3_);
      }
    };
    
    final String field_193540_c;
    
    final String field_193541_d;
    
    ActionType(String p_i47557_3_) {
      this.field_193540_c = p_i47557_3_;
      this.field_193541_d = "commands.advancement." + p_i47557_3_;
    }
    
    @Nullable
    static ActionType func_193536_a(String p_193536_0_) {
      byte b;
      int i;
      ActionType[] arrayOfActionType;
      for (i = (arrayOfActionType = values()).length, b = 0; b < i; ) {
        ActionType advancementcommand$actiontype = arrayOfActionType[b];
        if (advancementcommand$actiontype.field_193540_c.equals(p_193536_0_))
          return advancementcommand$actiontype; 
        b++;
      } 
      return null;
    }
    
    CommandException func_193534_a() {
      return new CommandException(String.valueOf(this.field_193541_d) + ".usage", new Object[0]);
    }
    
    public int func_193532_a(EntityPlayerMP p_193532_1_, Iterable<Advancement> p_193532_2_) {
      int i = 0;
      for (Advancement advancement : p_193532_2_) {
        if (func_193537_a(p_193532_1_, advancement))
          i++; 
      } 
      return i;
    }
    
    protected abstract boolean func_193537_a(EntityPlayerMP param1EntityPlayerMP, Advancement param1Advancement);
    
    protected abstract boolean func_193535_a(EntityPlayerMP param1EntityPlayerMP, Advancement param1Advancement, String param1String);
  }
  
  enum Mode {
    ONLY("only", false, false),
    THROUGH("through", true, true),
    FROM("from", false, true),
    UNTIL("until", true, false),
    EVERYTHING("everything", true, true);
    
    static final String[] field_193553_f = new String[(values()).length];
    
    final String field_193554_g;
    
    final boolean field_193555_h;
    
    final boolean field_193556_i;
    
    static {
      for (int i = 0; i < (values()).length; i++)
        field_193553_f[i] = (values()[i]).field_193554_g; 
    }
    
    Mode(String p_i47556_3_, boolean p_i47556_4_, boolean p_i47556_5_) {
      this.field_193554_g = p_i47556_3_;
      this.field_193555_h = p_i47556_4_;
      this.field_193556_i = p_i47556_5_;
    }
    
    CommandException func_193543_a(AdvancementCommand.ActionType p_193543_1_, Object... p_193543_2_) {
      return new CommandException(String.valueOf(p_193543_1_.field_193541_d) + "." + this.field_193554_g + ".failed", p_193543_2_);
    }
    
    CommandException func_193544_a(AdvancementCommand.ActionType p_193544_1_) {
      return new CommandException(String.valueOf(p_193544_1_.field_193541_d) + "." + this.field_193554_g + ".usage", new Object[0]);
    }
    
    void func_193546_a(ICommandSender p_193546_1_, AdvancementCommand p_193546_2_, AdvancementCommand.ActionType p_193546_3_, Object... p_193546_4_) {
      CommandBase.notifyCommandListener(p_193546_1_, p_193546_2_, String.valueOf(p_193546_3_.field_193541_d) + "." + this.field_193554_g + ".success", p_193546_4_);
    }
    
    @Nullable
    static Mode func_193547_a(String p_193547_0_) {
      byte b;
      int i;
      Mode[] arrayOfMode;
      for (i = (arrayOfMode = values()).length, b = 0; b < i; ) {
        Mode advancementcommand$mode = arrayOfMode[b];
        if (advancementcommand$mode.field_193554_g.equals(p_193547_0_))
          return advancementcommand$mode; 
        b++;
      } 
      return null;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\command\AdvancementCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */