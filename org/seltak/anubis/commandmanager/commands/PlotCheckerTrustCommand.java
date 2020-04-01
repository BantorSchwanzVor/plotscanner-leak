package org.seltak.anubis.commandmanager.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.FileUtils;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;
import org.seltak.anubis.utils.ChatHelper;

public class PlotCheckerTrustCommand extends Command {
  public static Thread plotChecker;
  
  public static List<String> availableTrust = new ArrayList<>();
  
  public static List<String> availableTrustLater = new ArrayList<>();
  
  public static List<String> players = new ArrayList<>();
  
  public static boolean pause = false;
  
  public PlotCheckerTrustCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(final String[] args) {
    if (args.length == 6) {
      plotChecker = new Thread(new Runnable() {
            public void run() {
              int x = Integer.parseInt(args[1]);
              int xx = Integer.parseInt(args[2]);
              int y = Integer.parseInt(args[3]);
              int yy = Integer.parseInt(args[4]);
              File availableTrustFile = new File("availableTrust.txt");
              File availableTrustLaterFile = new File("availableTrustLater.txt");
              File playersTrustFile = new File("playersTrust.txt");
              if (!availableTrustFile.exists())
                try {
                  FileWriter fileWriter = new FileWriter("availableTrust.txt");
                } catch (IOException e) {
                  e.printStackTrace();
                }  
              if (!availableTrustLaterFile.exists())
                try {
                  FileWriter fileWriter = new FileWriter("availableTrustLater.txt");
                } catch (IOException e) {
                  e.printStackTrace();
                }  
              if (!playersTrustFile.exists())
                try {
                  FileWriter fileWriter = new FileWriter("playersTrust.txt");
                } catch (IOException e) {
                  e.printStackTrace();
                }  
              int plotAmount = Math.abs(x - xx) * Math.abs(y - yy);
              if (plotAmount == 0)
                plotAmount = 1; 
              Anubis.sendMessage("§8[§6Preview§8] §3Checke " + plotAmount + " Plots! (Trust)");
              for (int i = x; i <= xx; i++) {
                Anubis.sendMessage("i: " + i + " - xx: " + xx);
                for (int j = y; j <= yy; j++) {
                  if (!PlotCheckerTrustCommand.pause) {
                    Anubis.sendMessage("j: " + j + " - yy: " + yy);
                    (Minecraft.getMinecraft()).player.sendChatMessage("/plot info " + i + ";" + j + " trusted");
                    try {
                      Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                    } 
                    if (ChatHelper.getLastContaining("Vertraut") != null) {
                      System.out.println(ChatHelper.getLastContaining("Vertraut"));
                      if (ChatHelper.getLastContaining("Vertraut").contains("§7Vertraut: §e §e") && !ChatHelper.getLastContaining("Vertraut").contains("None") && !ChatHelper.getLastContaining("Vertraut").contains("Keine")) {
                        String[] vertraute = ChatHelper.getLastContaining("Vertraut").replace("§7Vertraut: §e §e", "").replaceAll("§7", "").replaceAll("§6", "").replaceAll("§e", "").replaceAll("§r", "").replaceAll(" ", "").split(",");
                        for (int k2 = 0; k2 < vertraute.length; k2++)
                          Anubis.sendMessage("x: " + vertraute[k2]); 
                        for (int k1 = 0; k1 < vertraute.length; k1++) {
                          String last = vertraute[k1];
                          Anubis.sendMessage(ChatHelper.getLastContaining(last));
                          System.out.println(ChatHelper.getLastContaining(last));
                          int price = 0;
                          String result = "";
                          if (PlotCheckerTrustCommand.this.getResponseCode(last)) {
                            List<String> value = new ArrayList<>();
                            try {
                              Anubis.sendMessage("/plot " + i + ";" + j + " middle");
                              value = PlotCheckerTrustCommand.this.berechneWert(i, j);
                              int beacon = 0;
                              int dragonegg = 0;
                              int endPortalFrame = 0;
                              int whiteStone = 0;
                              int blockIron = 0;
                              int blockGold = 0;
                              int blockDiamond = 0;
                              int blockEmerald = 0;
                              int quartzBlock = 0;
                              int skull = 0;
                              int mobSpawner = 0;
                              for (int k = 0; k < value.size(); k++) {
                                if (((String)value.get(k)).contains("BEACON")) {
                                  beacon++;
                                } else if (((String)value.get(k)).contains("DRAGONEGG")) {
                                  dragonegg++;
                                } else if (((String)value.get(k)).contains("ENDPORTALFRAME")) {
                                  endPortalFrame++;
                                } else if (((String)value.get(k)).contains("WHITESTONE")) {
                                  whiteStone++;
                                } else if (((String)value.get(k)).contains("BLOCKIRON")) {
                                  blockIron++;
                                } else if (((String)value.get(k)).contains("BLOCKGOLD")) {
                                  blockGold++;
                                } else if (((String)value.get(k)).contains("BLOCKDIAMOND")) {
                                  blockDiamond++;
                                } else if (((String)value.get(k)).contains("BLOCKEMERALD")) {
                                  blockEmerald++;
                                } else if (((String)value.get(k)).contains("QUARTZBLOCK")) {
                                  quartzBlock++;
                                } else if (((String)value.get(k)).contains("SKULL")) {
                                  skull++;
                                } else if (((String)value.get(k)).contains("MOBSPAWNER")) {
                                  mobSpawner++;
                                } 
                              } 
                              price = beacon * 1150 + dragonegg * 10000 + endPortalFrame * 13500 + whiteStone * 1400 + blockIron * 10 + blockGold * 6 + blockDiamond * 25 + blockEmerald * 56 + quartzBlock * 4 + skull * 2000 + mobSpawner * 5000;
                              Anubis.sendMessage(String.valueOf(beacon) + "x Beacon");
                              Anubis.sendMessage(String.valueOf(dragonegg) + "x Dragon Egg");
                              Anubis.sendMessage(String.valueOf(endPortalFrame) + "x End Portal Frame");
                              Anubis.sendMessage(String.valueOf(whiteStone) + "x End Stone");
                              Anubis.sendMessage(String.valueOf(blockIron) + "x Iron Block");
                              Anubis.sendMessage(String.valueOf(blockGold) + "x Gold Block");
                              Anubis.sendMessage(String.valueOf(blockDiamond) + "x Diamond Block");
                              Anubis.sendMessage(String.valueOf(blockEmerald) + "x Emerald Block");
                              Anubis.sendMessage(String.valueOf(quartzBlock) + "x Quartz Block");
                              Anubis.sendMessage(String.valueOf(skull) + "x Skull");
                              Anubis.sendMessage(String.valueOf(mobSpawner) + "x Spawner");
                              Anubis.sendMessage("----------");
                              Anubis.sendMessage("Price: " + price + "$");
                            } catch (NumberFormatException e) {
                              e.printStackTrace();
                            } catch (Exception e) {
                              e.printStackTrace();
                            } 
                            int plotWert = 0;
                            try {
                              Anubis.sendMessage("Added " + last);
                              PlotCheckerTrustCommand.players = FileUtils.readLines(new File("playersTrust.txt"), "utf-8");
                              PlotCheckerTrustCommand.players.add(String.valueOf(last) + ",[" + i + ";" + j + "]," + price + "," + args[5].toUpperCase());
                              BufferedWriter writer = new BufferedWriter(new FileWriter("playersTrust.txt"));
                              for (String player : PlotCheckerTrustCommand.players)
                                writer.write(String.valueOf(player) + System.lineSeparator()); 
                              writer.close();
                              result = PlotCheckerTrustCommand.this.getLineTwelve(last);
                              if (result.contains("Noch nicht ver")) {
                                Anubis.sendMessage("Added " + last);
                                PlotCheckerTrustCommand.availableTrustLater = FileUtils.readLines(new File("availableTrustLater.txt"), "utf-8");
                                PlotCheckerTrustCommand.availableTrustLater.add(String.valueOf(last) + ",[" + i + ";" + j + "]," + price + "," + args[5].toUpperCase());
                                BufferedWriter writer2 = new BufferedWriter(new FileWriter("availableTrustLater.txt"));
                                for (String player : PlotCheckerTrustCommand.availableTrustLater)
                                  writer2.write(String.valueOf(player) + System.lineSeparator()); 
                                writer2.close();
                              } else if (!result.contains("Nicht ver")) {
                                Anubis.sendMessage("Added " + last);
                                PlotCheckerTrustCommand.availableTrust = FileUtils.readLines(new File("availableTrust.txt"), "utf-8");
                                PlotCheckerTrustCommand.availableTrust.add(String.valueOf(last) + ",[" + i + ";" + j + "]," + price + "," + args[5]);
                                BufferedWriter writer3 = new BufferedWriter(new FileWriter("availableTrust.txt"));
                                for (String player : PlotCheckerTrustCommand.availableTrust)
                                  writer3.write(String.valueOf(player) + System.lineSeparator()); 
                                writer3.close();
                              } 
                            } catch (Exception e) {
                              e.printStackTrace();
                            } 
                          } 
                          Anubis.sendMessage("Vertraute: " + last);
                          try {
                            Thread.sleep(2000L);
                          } catch (InterruptedException e) {
                            e.printStackTrace();
                          } 
                        } 
                      } 
                    } 
                    try {
                      Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                    } 
                  } else {
                    while (PlotCheckerTrustCommand.pause) {
                      try {
                        Thread.sleep(1000L);
                      } catch (InterruptedException e) {
                        e.printStackTrace();
                      } 
                    } 
                  } 
                } 
              } 
            }
          });
      plotChecker.start();
    } else if (args.length == 2) {
      if (args[1].equalsIgnoreCase("stop")) {
        plotChecker.stop();
        Anubis.sendMessage("§8[§6Preview§8] §3Plot-Checker gestoppt!");
      } else if (args[1].equalsIgnoreCase("pause")) {
        pause = true;
      } else if (args[1].equalsIgnoreCase("start")) {
        pause = false;
      } else if (args[1].equalsIgnoreCase("sort")) {
        Anubis.sendMessage("Sorting List...");
        try {
          availableTrust = FileUtils.readLines(new File("availableTrust.txt"), "utf-8");
          availableTrust.sort(new Comparator<String>() {
                public int compare(String s1, String s2) {
                  int price1 = Integer.parseInt(s1.split(",")[2]);
                  int price2 = Integer.parseInt(s2.split(",")[2]);
                  return price1 - price2;
                }
              });
          Collections.reverse(availableTrust);
          availableTrust.forEach(System.out::println);
          BufferedWriter writer3 = new BufferedWriter(new FileWriter("availableTrust.txt"));
          for (String player : availableTrust)
            writer3.write(String.valueOf(player) + System.lineSeparator()); 
          writer3.close();
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    } 
  }
  
  private boolean getResponseCode(String name) {
    try {
      URL url = new URL("https://playerdb.co/api/player/minecraft/" + name);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    } 
    return false;
  }
  
  private boolean getExisting(String name) throws IOException {
    URL url = new URL("https://mcuuid.net/?q=" + name);
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    boolean contains = false;
    for (Object s : in.lines().collect((Collector)Collectors.toList())) {
      if (((String)s).contains("No user found with provided UUID/username!")) {
        contains = true;
        break;
      } 
    } 
    return !contains;
  }
  
  private String getLineTwelve(String name) throws IOException {
    URL url = new URL("https://namemc.com/name/" + name);
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    return ((List<String>)in.lines().collect((Collector)Collectors.toList())).get(12);
  }
  
  private List<String> berechneWert(int i, int j) throws Exception {
    List<String> worth = new ArrayList<>();
    (Minecraft.getMinecraft()).player.sendChatMessage("/plot " + i + ";" + j + " middle");
    try {
      Thread.sleep(2500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } 
    int x = (int)((Minecraft.getMinecraft()).player.posX - 15.0D);
    int y = 65;
    int z = (int)((Minecraft.getMinecraft()).player.posZ - 15.0D);
    for (int i1 = 0; i1 < 31; i1++) {
      for (int j1 = 0; j1 < 31; j1++) {
        for (int k = 0; k < 255; k++) {
          BlockPos pos = new BlockPos(x + i1, k, z + j1);
          String block = (Minecraft.getMinecraft()).world.getBlockState(pos).getBlock().getUnlocalizedName();
          if (!block.contains("air") && !block.contains("dirt") && !block.contains("stone") && !block.contains("sand") && !block.contains("water") && !block.contains("bedrock") && !block.contains("oreIron") && !block.contains("oreCoal") && !block.contains("lava") && !block.contains("grass") && !block.contains("gravel") && !block.contains("obsidian") && !block.contains("oreLapis") && !block.contains("oreGold") && !block.contains("deadbush") && !block.contains("oreDiamond"))
            if (block.contains("beacon") || block.contains("dragonEgg") || block.contains("blockEmerald") || block.contains("endPortalFrame") || block.contains("skull") || block.contains("quartzBlock") || block.contains("whiteStone") || block.contains("blockGold") || block.contains("blockDiamond") || block.contains("blockIron") || block.contains("mobSpawner"))
              worth.add(String.valueOf(block.substring(5).toUpperCase()) + "," + pos.getX() + ";" + pos.getY() + ";" + pos.getZ());  
        } 
      } 
    } 
    return worth;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\PlotCheckerTrustCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */