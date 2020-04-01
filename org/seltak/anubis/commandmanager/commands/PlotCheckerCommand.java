package org.seltak.anubis.commandmanager.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.FileUtils;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;
import org.seltak.anubis.utils.ChatHelper;

public class PlotCheckerCommand extends Command {
  public static Thread plotChecker;
  
  public static boolean pausePlotChecker = false;
  
  public static List<String> availableUsernames = new ArrayList<>();
  
  public static List<String> allAvailableUsernames = new ArrayList<>();
  
  public static List<String> availableTrustedUsernames = new ArrayList<>();
  
  public static List<String> allAvailableTrustedUsernames = new ArrayList<>();
  
  public static List<String> temp = new ArrayList<>();
  
  public PlotCheckerCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(String[] args) {
    if (args.length == 6) {
      startPlotChecker(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), args[5]);
    } else if (args.length == 4 && args[1].equalsIgnoreCase("area") && args[2].equalsIgnoreCase("1")) {
      startAreaPlotChecker(-200, 0, -200, 0, args[3], 1);
    } else if (args.length == 4 && args[1].equalsIgnoreCase("area") && args[2].equalsIgnoreCase("2")) {
      startAreaPlotChecker(-200, 0, 0, 200, args[3], 2);
    } else if (args.length == 4 && args[1].equalsIgnoreCase("area") && args[2].equalsIgnoreCase("3")) {
      startAreaPlotChecker(0, 200, -200, 0, args[3], 3);
    } else if (args.length == 4 && args[1].equalsIgnoreCase("area") && args[2].equalsIgnoreCase("4")) {
      startAreaPlotChecker(0, 200, 0, 200, args[3], 4);
    } else if (args.length == 2) {
      if (args[1].equalsIgnoreCase("stop")) {
        plotChecker.stop();
        Anubis.sendMessage("§3Plot-Checker gestoppt!");
      } else if (args[1].equalsIgnoreCase("pause")) {
        pausePlotChecker = true;
      } else if (args[1].equalsIgnoreCase("start")) {
        pausePlotChecker = false;
      } else if (args[1].equalsIgnoreCase("sort")) {
        Anubis.sendMessage("Sorting List...");
        try {
          allAvailableUsernames = FileUtils.readLines(new File("all_available_usernames.txt"), "utf-8");
          allAvailableUsernames.sort(new Comparator<String>() {
                public int compare(String s1, String s2) {
                  System.out.println(s1);
                  System.out.println(s2);
                  int price1 = Integer.parseInt(s1.split(",")[2]);
                  int price2 = Integer.parseInt(s2.split(",")[2]);
                  return price1 - price2;
                }
              });
          Collections.reverse(allAvailableUsernames);
          allAvailableUsernames.forEach(System.out::println);
          BufferedWriter writer3 = new BufferedWriter(new FileWriter("all_available_usernames.txt"));
          for (String usernameData : allAvailableUsernames)
            writer3.write(String.valueOf(usernameData) + System.lineSeparator()); 
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
  
  private void getScrt() {}
  
  private void createTextFiles(String citybuild, String date) {
    File availableUsernamesFile = new File("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt");
    File allAvailableUsernamesFile = new File("Preview/" + citybuild.toUpperCase() + "/" + "all_available_usernames_" + citybuild.toLowerCase() + ".txt");
    File availableTrustedUsernamesFile = new File("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt");
    File allAvailableTrustedUsernamesFile = new File("Preview/" + citybuild.toUpperCase() + "/" + "all_available_trusted_usernames_" + citybuild.toLowerCase() + ".txt");
    File allAvailableUsernamesSortFile = new File("all_available_usernames.txt");
    if (!(new File("Preview")).exists())
      (new File("Preview")).mkdir(); 
    if (!(new File("Preview/" + citybuild.toUpperCase())).exists())
      (new File("Preview/" + citybuild.toUpperCase())).mkdir(); 
    if (!availableUsernamesFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
    if (!allAvailableUsernamesFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + "all_available_usernames_" + citybuild.toLowerCase() + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
    if (!availableTrustedUsernamesFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
    if (!allAvailableTrustedUsernamesFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + "all_available_trusted_usernames_" + citybuild.toLowerCase() + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
    if (!allAvailableUsernamesSortFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("all_available_usernames.txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
  }
  
  private void createAreaTextFiles(String citybuild, String date, int area) {
    File availableUsernamesFile = new File("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt");
    File availableTrustedUsernamesFile = new File("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt");
    File scanProgressFile = new File("Preview/" + citybuild.toUpperCase() + "/Area/" + "progress_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt");
    if (!(new File("Preview")).exists())
      (new File("Preview")).mkdir(); 
    if (!(new File("Preview/" + citybuild.toUpperCase())).exists())
      (new File("Preview/" + citybuild.toUpperCase())).mkdir(); 
    if (!(new File("Preview/" + citybuild.toUpperCase() + "/Area")).exists())
      (new File("Preview/" + citybuild.toUpperCase() + "/Area")).mkdir(); 
    if (!availableUsernamesFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
    if (!availableTrustedUsernamesFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
    if (!scanProgressFile.exists())
      try {
        FileWriter fileWriter = new FileWriter("Preview/" + citybuild.toUpperCase() + "/Area/" + "progress_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt");
      } catch (IOException e) {
        e.printStackTrace();
      }  
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
  
  private void checkPlotOwner(int x, int y, String citybuild, String date, int area) {
    if (ChatHelper.getLastContaining("Besitzer") != null && 
      ChatHelper.getLastContaining("Besitzer").contains("§e §e") && (ChatHelper.getLastContaining("Besitzer").split("§e §e")).length >= 2) {
      String username = ChatHelper.getLastContaining("Besitzer").split("§e §e")[1].replaceAll("§7", "").replaceAll(" ", "");
      int price = 0;
      if (area != 0)
        try {
          BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/Area/" + "progress_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt"));
          writer.write(String.valueOf(x) + "," + y + System.lineSeparator());
          writer.close();
        } catch (Exception e) {
          e.printStackTrace();
        }  
      if (getResponseCode(username)) {
        List<String> value = new ArrayList<>();
        try {
          Anubis.sendMessage("/plot " + x + ";" + y + " middle");
          value = berechneWert(x, y);
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
        if (area == 0) {
          try {
            Anubis.sendMessage("Added " + username);
            allAvailableUsernames = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt"), "utf-8");
            allAvailableUsernames.add(String.valueOf(username) + ",[" + x + ";" + y + "]," + price + "," + citybuild.toUpperCase());
            BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt"));
            for (String usernameData : allAvailableUsernames)
              writer.write(String.valueOf(usernameData) + System.lineSeparator()); 
            writer.close();
          } catch (Exception e) {
            e.printStackTrace();
          } 
          try {
            Anubis.sendMessage("(All) " + username);
            allAvailableUsernames = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/" + "all_available_usernames_" + citybuild.toLowerCase() + ".txt"), "utf-8");
            allAvailableUsernames.add(String.valueOf(username) + ",[" + x + ";" + y + "]," + price + "," + citybuild.toUpperCase());
            BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + "all_available_usernames_" + citybuild.toLowerCase() + ".txt"));
            for (String usernameData : allAvailableUsernames)
              writer.write(String.valueOf(usernameData) + System.lineSeparator()); 
            writer.close();
          } catch (Exception e) {
            e.printStackTrace();
          } 
        } else {
          try {
            Anubis.sendMessage("Added " + username);
            allAvailableTrustedUsernames = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt"), "utf-8");
            allAvailableTrustedUsernames.add(String.valueOf(username) + ",[" + x + ";" + y + "]," + price + "," + citybuild.toUpperCase());
            BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt"));
            for (String usernameData : allAvailableTrustedUsernames)
              writer.write(String.valueOf(usernameData) + System.lineSeparator()); 
            writer.close();
          } catch (Exception e) {
            e.printStackTrace();
          } 
        } 
      } 
      Anubis.sendMessage("Besitzer: " + username);
    } 
  }
  
  private void checkPlotMembers(int x, int y, String citybuild, String date, int area) {
    if (ChatHelper.getLastContaining("Vertraut") != null) {
      System.out.println(ChatHelper.getLastContaining("Vertraut"));
      if (ChatHelper.getLastContaining("Vertraut").contains("§7Vertraut: §e §e") && !ChatHelper.getLastContaining("Vertraut").contains("None") && !ChatHelper.getLastContaining("Vertraut").contains("Keine")) {
        String[] trustedMembers = ChatHelper.getLastContaining("Vertraut").replace("§7Vertraut: §e §e", "").replaceAll("§7", "").replaceAll("§6", "").replaceAll("§e", "").replaceAll("§r", "").replaceAll(" ", "").split(",");
        for (int k2 = 0; k2 < trustedMembers.length; k2++);
        for (int k1 = 0; k1 < trustedMembers.length; k1++) {
          String username = trustedMembers[k1];
          Anubis.sendMessage(ChatHelper.getLastContaining(username));
          System.out.println(ChatHelper.getLastContaining(username));
          int price = 0;
          String result = "";
          if (getResponseCode(username)) {
            List<String> value = new ArrayList<>();
            try {
              value = berechneWert(x, y);
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
            if (area != 0) {
              try {
                Anubis.sendMessage("Added " + username);
                allAvailableTrustedUsernames = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt"), "utf-8");
                allAvailableTrustedUsernames.add(String.valueOf(username) + ",[" + x + ";" + y + "]," + price + "," + citybuild.toUpperCase());
                BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + Minecraft.getMinecraft().getSession().getUsername().toLowerCase() + ".txt"));
                for (String usernameData : allAvailableTrustedUsernames)
                  writer.write(String.valueOf(usernameData) + System.lineSeparator()); 
                writer.close();
              } catch (Exception e) {
                e.printStackTrace();
              } 
              try {
                Anubis.sendMessage("(All) Added " + username);
                allAvailableTrustedUsernames = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/" + "all_available_trusted_usernames_" + citybuild.toLowerCase() + ".txt"), "utf-8");
                allAvailableTrustedUsernames.add(String.valueOf(username) + ",[" + x + ";" + y + "]," + price + "," + citybuild.toUpperCase());
                BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/" + "all_available_trusted_usernames_" + citybuild.toLowerCase() + ".txt"));
                for (String usernameData : allAvailableTrustedUsernames)
                  writer.write(String.valueOf(usernameData) + System.lineSeparator()); 
                writer.close();
              } catch (Exception e) {
                e.printStackTrace();
              } 
            } else {
              try {
                Anubis.sendMessage("Added " + username);
                allAvailableTrustedUsernames = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt"), "utf-8");
                allAvailableTrustedUsernames.add(String.valueOf(username) + ",[" + x + ";" + y + "]," + price + "," + citybuild.toUpperCase());
                BufferedWriter writer = new BufferedWriter(new FileWriter("Preview/" + citybuild.toUpperCase() + "/Area/" + date + "_available_trusted_usernames_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt"));
                for (String usernameData : allAvailableTrustedUsernames)
                  writer.write(String.valueOf(usernameData) + System.lineSeparator()); 
                writer.close();
              } catch (Exception e) {
                e.printStackTrace();
              } 
            } 
          } 
          Anubis.sendMessage("Vertraut: " + username);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } 
        } 
      } 
    } 
  }
  
  private void startPlotChecker(final int xStart, final int xEnd, final int yStart, final int yEnd, final String citybuild) {
    plotChecker = new Thread(new Runnable() {
          public void run() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            PlotCheckerCommand.this.createTextFiles(citybuild, date);
            int plotAmount = Math.abs(xStart - xEnd) * Math.abs(yStart - yEnd);
            if (plotAmount == 0)
              plotAmount = 1; 
            Anubis.sendMessage("§3Überprüfe " + plotAmount + " Grundstücke.");
            for (int i = xStart; i <= xEnd; i++) {
              Anubis.sendMessage("i: " + i + " - xEnd: " + xEnd);
              PlotCheckerCommand.this.getScrt();
              for (int j = yStart; j <= yEnd; j++) {
                if (!PlotCheckerCommand.pausePlotChecker) {
                  Anubis.sendMessage("j: " + j + " - yEnd: " + yStart);
                  (Minecraft.getMinecraft()).player.sendChatMessage("/plot info " + i + ";" + j);
                  try {
                    Thread.sleep(1000L);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  } 
                  System.out.println(ChatHelper.getLastContaining("Besitzer"));
                  PlotCheckerCommand.this.checkPlotOwner(i, j, citybuild, date, 0);
                  PlotCheckerCommand.this.checkPlotMembers(i, j, citybuild, date, 0);
                  try {
                    Thread.sleep(2000L);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  } 
                } else {
                  while (PlotCheckerCommand.pausePlotChecker) {
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
  }
  
  private void startAreaPlotChecker(final int xBegin, final int xEnd, final int yBegin, final int yEnd, final String citybuild, final int area) {
    plotChecker = new Thread(new Runnable() {
          public void run() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            int xStart = xBegin;
            int yStart = yBegin;
            PlotCheckerCommand.this.createAreaTextFiles(citybuild, date, area);
            try {
              PlotCheckerCommand.temp = FileUtils.readLines(new File("Preview/" + citybuild.toUpperCase() + "/Area/" + "progress_" + citybuild.toLowerCase() + "_" + "area_" + area + ".txt"), "utf-8");
              for (String coords : PlotCheckerCommand.temp) {
                if ((coords.split(",")).length == 2) {
                  xStart = Integer.parseInt(coords.split(",")[0]);
                  yStart = Integer.parseInt(coords.split(",")[1]);
                } 
              } 
            } catch (IOException e1) {
              e1.printStackTrace();
            } 
            int plotAmount = Math.abs(xStart - xEnd) * Math.abs(yBegin - yEnd);
            if (plotAmount == 0)
              plotAmount = 1; 
            double scanProgress = (100 - plotAmount / 40000 * 100);
            Anubis.sendMessage("§3Überprüfe " + plotAmount + " Grundstücke.");
            Anubis.sendMessage("§3Fortschritt: " + scanProgress + "%");
            PlotCheckerCommand.this.plotRange(xStart, xEnd, yStart, yBegin, yEnd, plotAmount, scanProgress, citybuild, date, area);
          }
        });
    plotChecker.start();
  }
  
  private void plotRange(int xStart, int xEnd, int yStart, int yBegin, int yEnd, int plotAmount, double scanProgress, String citybuild, String date, int area) {
    boolean flag = false;
    for (int i = xStart; i <= xEnd; i++) {
      getScrt();
      for (int j = yBegin; j <= yEnd; j++) {
        if (!flag) {
          flag = true;
          j = yStart;
        } 
        if (!pausePlotChecker) {
          plotAmount = Math.abs(i - xEnd) * Math.abs(yBegin - yEnd) - j;
          if (plotAmount == 0)
            plotAmount = 1; 
          scanProgress = (100 - plotAmount / 40000 * 100);
          Anubis.sendMessage("§6Noch " + plotAmount + " Grundstücke.");
          Anubis.sendMessage("§6Fortschritt: " + scanProgress + "%");
          Anubis.sendMessage("Checking: " + i + "," + j);
          (Minecraft.getMinecraft()).player.sendChatMessage("/plot info " + i + ";" + j);
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } 
          System.out.println(ChatHelper.getLastContaining("Besitzer"));
          checkPlotOwner(i, j, citybuild, date, area);
          checkPlotMembers(i, j, citybuild, date, area);
          try {
            Thread.sleep(2000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          } 
        } else {
          while (pausePlotChecker) {
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
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\PlotCheckerCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */