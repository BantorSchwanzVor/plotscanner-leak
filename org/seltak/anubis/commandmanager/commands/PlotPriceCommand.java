package org.seltak.anubis.commandmanager.commands;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;

public class PlotPriceCommand extends Command {
  public static Thread plotChecker;
  
  public PlotPriceCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(final String[] args) {
    if (args.length == 3) {
      plotChecker = new Thread(new Runnable() {
            public void run() {
              List<String> value = new ArrayList<>();
              try {
                value = PlotPriceCommand.this.berechneWert(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
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
                int seaLantern = 0;
                int hopper = 0;
                int blockLapis = 0;
                int chest = 0;
                int lightgem = 0;
                int brick = 0;
                int bookshelf = 0;
                int cloth = 0;
                int log = 0;
                int furnance = 0;
                int enchantmentTable = 0;
                int enderChest = 0;
                int wood = 0;
                int dispenser = 0;
                int glass = 0;
                int dropper = 0;
                int sign = 0;
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
                  } else if (((String)value.get(k)).contains("seaLantern")) {
                    seaLantern++;
                  } else if (((String)value.get(k)).contains("hopper")) {
                    hopper++;
                  } else if (((String)value.get(k)).contains("blockLapis")) {
                    blockLapis++;
                  } else if (((String)value.get(k)).contains("chest")) {
                    chest++;
                  } else if (((String)value.get(k)).contains("lightgem")) {
                    lightgem++;
                  } else if (((String)value.get(k)).contains("brick")) {
                    brick++;
                  } else if (((String)value.get(k)).contains("bookshelf")) {
                    bookshelf++;
                  } else if (((String)value.get(k)).contains("cloth")) {
                    cloth++;
                  } else if (((String)value.get(k)).contains("log")) {
                    log++;
                  } else if (((String)value.get(k)).contains("furnance")) {
                    furnance++;
                  } else if (((String)value.get(k)).contains("enchantmentTable")) {
                    enchantmentTable++;
                  } else if (((String)value.get(k)).contains("enderChest")) {
                    enderChest++;
                  } else if (((String)value.get(k)).contains("wood")) {
                    wood++;
                  } else if (((String)value.get(k)).contains("dispenser")) {
                    dispenser++;
                  } else if (((String)value.get(k)).contains("glass")) {
                    glass++;
                  } else if (((String)value.get(k)).contains("dropper")) {
                    dropper++;
                  } else if (((String)value.get(k)).contains("sign")) {
                    sign++;
                  } 
                } 
                double price = (beacon * 1150 + dragonegg * 10000 + endPortalFrame * 13500 + 
                  whiteStone * 1400 + blockIron * 10 + blockGold * 6 + blockDiamond * 25 + 
                  blockEmerald * 56 + quartzBlock * 4 + skull * 2000 + mobSpawner * 5000 + 
                  seaLantern * 6 + hopper * 25 + blockLapis * 4 + chest * 12 + lightgem * 4 + brick * 6 + bookshelf * 7) + cloth * 1.5D + (log * 2) + (furnance * 1) + (
                  enchantmentTable * 50) + (enderChest * 100) + wood * 0.5D + (dispenser * 1) + glass * 0.85D + (dropper * 1) + (sign * 1);
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
              } catch (Exception e) {
                e.printStackTrace();
              } 
            }
          });
      plotChecker.start();
    } else if (args.length == 2 && 
      args[1].equalsIgnoreCase("stop")) {
      plotChecker.stop();
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
          if (!block.contains("air") && !block.contains("dirt") && !block.contains("stone") && !block.contains("sand") && !block.contains("water") && !block.contains("bedrock") && !block.contains("oreIron") && !block.contains("oreCoal") && !block.contains("lava") && !block.contains("grass") && !block.contains("gravel") && !block.contains("oreLapis") && !block.contains("oreGold") && !block.contains("deadbush") && !block.contains("oreDiamond"))
            if (block.contains("sign") || block.contains("dropper") || block.contains("glass") || block.contains("dispenser") || block.contains("wood") || block.contains("enderChest") || block.contains("enchantmentTable") || block.contains("furnance") || block.contains("log") || block.contains("cloth") || block.contains("bookshelf") || block.contains("brick") || block.contains("obsidian") || block.contains("lightgem") || block.contains("chest") || block.contains("quartzBlock") || block.contains("blockLapis") || block.contains("hopper") || block.contains("seaLantern") || block.contains("beacon") || block.contains("dragonEgg") || block.contains("blockEmerald") || block.contains("endPortalFrame") || block.contains("skull") || block.contains("quartzBlock") || block.contains("whiteStone") || block.contains("blockGold") || block.contains("blockDiamond") || block.contains("blockIron") || block.contains("mobSpawner"))
              worth.add(String.valueOf(block.substring(5).toUpperCase()) + "," + pos.getX() + ";" + pos.getY() + ";" + pos.getZ());  
        } 
      } 
    } 
    return worth;
  }
  
  private double calculatePrice(int x, int y) {
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
      int seaLantern = 0;
      int hopper = 0;
      int blockLapis = 0;
      int chest = 0;
      int lightgem = 0;
      int brick = 0;
      int bookshelf = 0;
      int cloth = 0;
      int log = 0;
      int furnance = 0;
      int enchantmentTable = 0;
      int enderChest = 0;
      int wood = 0;
      int dispenser = 0;
      int glass = 0;
      int dropper = 0;
      int sign = 0;
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
        } else if (((String)value.get(k)).contains("SEALANTERN")) {
          seaLantern++;
        } else if (((String)value.get(k)).contains("HOPPER")) {
          hopper++;
        } else if (((String)value.get(k)).contains("BLOCKLAPIS")) {
          blockLapis++;
        } else if (((String)value.get(k)).contains("CHEST")) {
          chest++;
        } else if (((String)value.get(k)).contains("LIGHTGEM")) {
          lightgem++;
        } else if (((String)value.get(k)).contains("BRICK")) {
          brick++;
        } else if (((String)value.get(k)).contains("BOOKSHELF")) {
          bookshelf++;
        } else if (((String)value.get(k)).contains("CLOTH")) {
          cloth++;
        } else if (((String)value.get(k)).contains("LOG")) {
          log++;
        } else if (((String)value.get(k)).contains("FURNANCE")) {
          furnance++;
        } else if (((String)value.get(k)).contains("ENCHANTMENTTABLE")) {
          enchantmentTable++;
        } else if (((String)value.get(k)).contains("ENDERCHEST")) {
          enderChest++;
        } else if (((String)value.get(k)).contains("WOOD")) {
          wood++;
        } else if (((String)value.get(k)).contains("DISPENSER")) {
          dispenser++;
        } else if (((String)value.get(k)).contains("GLASS")) {
          glass++;
        } else if (((String)value.get(k)).contains("DROPPER")) {
          dropper++;
        } else if (((String)value.get(k)).contains("SIGN")) {
          sign++;
        } 
      } 
      double price = (beacon * 1150 + dragonegg * 10000 + endPortalFrame * 13500 + 
        whiteStone * 1400 + blockIron * 10 + blockGold * 6 + blockDiamond * 25 + 
        blockEmerald * 56 + quartzBlock * 4 + skull * 2000 + mobSpawner * 5000 + 
        seaLantern * 6 + hopper * 25 + blockLapis * 4 + chest * 12 + lightgem * 4 + brick * 6 + bookshelf * 7) + cloth * 1.5D + (log * 2) + (furnance * 1) + (
        enchantmentTable * 50) + (enderChest * 100) + wood * 0.5D + (dispenser * 1) + glass * 0.85D + (dropper * 1) + (sign * 1);
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
      return price;
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0D;
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\PlotPriceCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */