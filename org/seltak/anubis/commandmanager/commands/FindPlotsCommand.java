package org.seltak.anubis.commandmanager.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.io.FileUtils;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;

public class FindPlotsCommand extends Command {
  public static Thread plotChecker;
  
  public static List<String> names = new ArrayList<>();
  
  public static List<String> scannedPlots = new ArrayList<>();
  
  public static boolean pause = false;
  
  public FindPlotsCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(final String[] args) {
    if (args.length == 3 && args[1].equalsIgnoreCase("start")) {
      plotChecker = new Thread(new Runnable() {
            public void run() {
              File availableFile = new File("findplots.txt");
              if (!availableFile.exists())
                try {
                  FileWriter fileWriter = new FileWriter("findplots.txt");
                } catch (IOException e) {
                  e.printStackTrace();
                }  
              File scannedPlotsFile = new File("scannedPlots.txt");
              if (!scannedPlotsFile.exists())
                try {
                  FileWriter fileWriter = new FileWriter("scannedPlots.txt");
                } catch (IOException e) {
                  e.printStackTrace();
                }  
              try {
                FindPlotsCommand.names = FileUtils.readLines(new File("findplots.txt"), "utf-8");
              } catch (IOException e1) {
                e1.printStackTrace();
              } 
              Anubis.sendMessage("§8[§6Preview§8] §3Checke 4 Plots des Spielers!");
              for (String name : FindPlotsCommand.names) {
                for (int k1 = 1; k1 <= 4; k1++) {
                  if (!FindPlotsCommand.pause) {
                    (Minecraft.getMinecraft()).player.sendChatMessage("/plot home " + name + " " + k1);
                    try {
                      Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                    } 
                    int price = 0;
                    List<String> value = new ArrayList<>();
                    try {
                      Anubis.sendMessage("/plot home " + name + " " + k1);
                      value = FindPlotsCommand.this.berechneWert(name);
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
                    try {
                      Anubis.sendMessage("Das " + k1 + ". Plot von " + name + " ist " + price + "$ wert.");
                      FindPlotsCommand.scannedPlots = FileUtils.readLines(new File("scannedPlots.txt"), "utf-8");
                      FindPlotsCommand.scannedPlots.add(String.valueOf(name) + "," + price + "," + args[2].toUpperCase());
                      BufferedWriter writer = new BufferedWriter(new FileWriter("scannedPlots.txt"));
                      for (String plot : FindPlotsCommand.scannedPlots)
                        writer.write(String.valueOf(plot) + System.lineSeparator()); 
                      writer.close();
                    } catch (Exception e) {
                      e.printStackTrace();
                    } 
                    Anubis.sendMessage("Name: " + name);
                    try {
                      Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                      e.printStackTrace();
                    } 
                  } else {
                    while (FindPlotsCommand.pause) {
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
          names = FileUtils.readLines(new File("findplots.txt"), "utf-8");
          names.sort(new Comparator<String>() {
                public int compare(String s1, String s2) {
                  int price1 = Integer.parseInt(s1.split(",")[1]);
                  int price2 = Integer.parseInt(s2.split(",")[1]);
                  return price1 - price2;
                }
              });
          Collections.reverse(names);
          names.forEach(System.out::println);
          BufferedWriter writer3 = new BufferedWriter(new FileWriter("findplots.txt"));
          for (String player : names)
            writer3.write(String.valueOf(player) + System.lineSeparator()); 
          writer3.close();
        } catch (Exception e) {
          e.printStackTrace();
        } 
      } 
    } 
  }
  
  private List<String> berechneWert(String name) throws Exception {
    List<String> worth = new ArrayList<>();
    (Minecraft.getMinecraft()).gameSettings.keyBindSprint.pressed = true;
    (Minecraft.getMinecraft()).gameSettings.keyBindForward.pressed = true;
    (Minecraft.getMinecraft()).gameSettings.keyBindJump.pressed = true;
    try {
      Thread.sleep(1500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } 
    (Minecraft.getMinecraft()).gameSettings.keyBindSprint.pressed = false;
    (Minecraft.getMinecraft()).gameSettings.keyBindForward.pressed = false;
    (Minecraft.getMinecraft()).gameSettings.keyBindJump.pressed = false;
    (Minecraft.getMinecraft()).player.sendChatMessage("/plot middle");
    try {
      Thread.sleep(700L);
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


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\FindPlotsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */