package org.seltak.anubis.modules.player;

import de.Hero.settings.Setting;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextFormatting;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.commands.PlotCheckerDatabaseCommand;
import org.seltak.anubis.module.Category;
import org.seltak.anubis.module.Module;
import org.seltak.anubis.utils.LoginUtils;
import org.seltak.anubis.utils.Timer;

public class AutoRejoin extends Module {
  private static GuiMultiplayer multiplayer = null;
  
  Timer t = new Timer();
  
  Timer t2 = new Timer();
  
  public static boolean executedCommand = false;
  
  public AutoRejoin() {
    super("AutoRejoin", Category.PLAYER, 0);
  }
  
  public void setup() {
    Anubis.setmgr.rSetting(new Setting("Citybuild", this, 1.0D, 0.0D, 10.0D, true));
    Anubis.setmgr.rSetting(new Setting("Account", this, 1.0D, 0.0D, 20.0D, true));
    Anubis.setmgr.rSetting(new Setting("Area", this, 1.0D, 1.0D, 4.0D, true));
  }
  
  public void onPreUpdate() {
    connectToServer();
    connectToPortal();
    connectToCitybuild((int)Anubis.setmgr.getSettingByName("Citybuild").getValDouble());
    startChecking();
    respawnWhenDead();
    super.onPreUpdate();
  }
  
  public void onTick() {
    PlotCheckerDatabaseCommand.plotScanner = null;
    connectToServer();
  }
  
  public void onDisable() {
    super.onDisable();
  }
  
  private void startChecking() {
    if (checkCurrentServer("CB" + (int)Anubis.setmgr.getSettingByName("Citybuild").getValDouble()) && 
      Timer.delayOver(15000L) && !executedCommand) {
      executedCommand = true;
      String s = ".scanplots area " + (int)Anubis.setmgr.getSettingByName("Area").getValDouble() + " CB" + (int)Anubis.setmgr.getSettingByName("Citybuild").getValDouble();
      Anubis.onCommand(s.split(" "));
      this.mc.ingameGUI.getChatGUI().addToSentMessages(s);
      Timer.reset();
    } 
  }
  
  private void respawnWhenDead() {
    if (this.mc.player.isDead) {
      this.mc.player.respawnPlayer();
      this.mc.displayGuiScreen(null);
    } 
  }
  
  private void connectToPortal() {
    if (checkCurrentServer("lobby") && 
      Timer.delayOver(6000L)) {
      this.mc.player.sendChatMessage("/portal");
      Timer.reset();
    } 
  }
  
  private void connectToCitybuild(int citybuild) {
    if (checkCurrentServer("portal")) {
      Timer.reset();
      if (citybuild == 1) {
        walkIntoPortal(308.0D, 117.0D, 316.0D);
      } else if (citybuild == 2) {
        walkIntoPortal(308.0D, 117.0D, 308.0D);
      } else if (citybuild == 3) {
        walkIntoPortal(308.0D, 117.0D, 300.0D);
      } else if (citybuild == 4) {
        walkIntoPortal(308.0D, 117.0D, 292.0D);
      } else if (citybuild == 5) {
        walkIntoPortal(308.0D, 117.0D, 284.0D);
      } else if (citybuild == 6) {
        walkIntoPortal(308.0D, 117.0D, 276.0D);
      } else if (citybuild == 7) {
        walkIntoPortal(308.0D, 117.0D, 268.0D);
      } else if (citybuild == 8) {
        walkIntoPortal(308.0D, 117.0D, 260.0D);
      } else if (citybuild == 9) {
        walkIntoPortal(308.0D, 117.0D, 252.0D);
      } else if (citybuild == 10) {
        walkIntoPortal(308.0D, 117.0D, 244.0D);
      } 
    } 
  }
  
  private void walkIntoPortal(double x, double y, double z) {
    this.mc.player.rotationYaw = 0.0F;
    this.mc.player.prevRotationPitch = 0.0F;
    this.mc.gameSettings.keyBindJump.setPressed(true);
    if (this.mc.player.posX > 314.0D) {
      this.mc.gameSettings.keyBindLeft.setPressed(false);
      this.mc.gameSettings.keyBindRight.setPressed(true);
    } else if (this.mc.player.posX < 312.0D) {
      this.mc.gameSettings.keyBindLeft.setPressed(true);
      this.mc.gameSettings.keyBindRight.setPressed(false);
    } 
    if (this.mc.player.posX < 314.0D && this.mc.player.posX > 312.0D && Math.abs(this.mc.player.posZ - z) > 0.5D)
      if (this.mc.player.posZ < z) {
        this.mc.gameSettings.keyBindLeft.setPressed(false);
        this.mc.gameSettings.keyBindRight.setPressed(false);
        this.mc.gameSettings.keyBindBack.setPressed(false);
        this.mc.gameSettings.keyBindForward.setPressed(true);
      } else if (this.mc.player.posZ > z) {
        this.mc.gameSettings.keyBindLeft.setPressed(false);
        this.mc.gameSettings.keyBindRight.setPressed(false);
        this.mc.gameSettings.keyBindBack.setPressed(true);
        this.mc.gameSettings.keyBindForward.setPressed(false);
      }  
    if (Math.abs(this.mc.player.posZ - z) < 0.5D) {
      this.mc.gameSettings.keyBindBack.setPressed(false);
      this.mc.gameSettings.keyBindForward.setPressed(false);
      if (this.mc.player.posX > x) {
        this.mc.gameSettings.keyBindLeft.setPressed(false);
        this.mc.gameSettings.keyBindRight.setPressed(true);
      } else if (this.mc.player.posX < x) {
        this.mc.gameSettings.keyBindLeft.setPressed(true);
        this.mc.gameSettings.keyBindRight.setPressed(false);
      } 
    } 
  }
  
  private boolean checkCurrentServer(String server) {
    Scoreboard scoreboard = this.mc.world.getScoreboard();
    ScaledResolution scaledresolution = new ScaledResolution(this.mc);
    ScoreObjective scoreobjective = null;
    ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.player.getName());
    if (scoreplayerteam != null) {
      int i1 = scoreplayerteam.getChatFormat().getColorIndex();
      if (i1 >= 0)
        scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1); 
    } 
    ScoreObjective scoreobjective1 = (scoreobjective != null) ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
    Collection<Score> collection = scoreboard.getSortedScores(scoreobjective1);
    for (Score score : collection) {
      ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score.getPlayerName());
      String s = String.valueOf(ScorePlayerTeam.formatPlayerName((Team)scoreplayerteam2, score.getPlayerName())) + ": " + TextFormatting.RED + score.getScorePoints();
      if (s.toUpperCase().contains(server.toUpperCase()))
        return true; 
    } 
    return false;
  }
  
  private void connectToServer() {
    if (this.mc.player == null && 
      Timer.delayOver(10000L)) {
      try {
        String[] account_data = account().split(";");
        Session session = LoginUtils.createSession(account_data[0], account_data[1], Proxy.NO_PROXY);
        Field field = Minecraft.class.getDeclaredField("session");
        field.setAccessible(true);
        field.set(this.mc, session);
        System.out.println("Erfolgreich in Account eingeloggt!");
      } catch (Exception e) {
        System.out.println("Fehler beim Account Login!");
        e.printStackTrace();
      } 
      executedCommand = false;
      ServerData server = new ServerData("Minecraft Server", "GrieferGames.net", false);
      if (multiplayer == null) {
        multiplayer = new GuiMultiplayer((GuiScreen)new GuiMainMenu());
      } else if (this.mc.currentScreen != multiplayer) {
        System.out.println("Set currentScreen");
        this.mc.displayGuiScreen((GuiScreen)new GuiConnecting((GuiScreen)multiplayer, this.mc, server));
      } 
      Timer.reset();
    } 
  }
  
  public static String account() {
    try {
      Connection con = getConnection();
      PreparedStatement select = con.prepareStatement("SELECT email,password FROM scan_accounts WHERE id=" + (int)Anubis.setmgr.getSettingByName("Account").getValDouble());
      ResultSet result = select.executeQuery();
      String coords = "";
      while (result.next())
        coords = String.valueOf(result.getString("email")) + ";" + result.getString("password"); 
      System.out.println("Received Account Data.");
      return coords;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error When Receiving Account Data.");
      return "";
    } 
  }
  
  public static Connection getConnection() throws Exception {
    try {
      String driver = "com.mysql.jdbc.Driver";
      String url = "jdbc:mysql://88.214.56.166:3306/plotscanner";
      String username = "plotscanner";
      String password = "Plotscanner234";
      Class.forName(driver);
      Connection conn = DriverManager.getConnection(url, username, password);
      System.out.println("Connected");
      return conn;
    } catch (Exception e) {
      System.out.println(e);
      return null;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\modules\player\AutoRejoin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */