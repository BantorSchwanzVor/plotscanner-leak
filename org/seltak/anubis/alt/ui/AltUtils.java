package org.seltak.anubis.alt.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class AltUtils {
  public static ArrayList<Alt> getAlts() {
    BufferedReader br;
    File alts = getFile();
    try {
      FileReader reader = new FileReader(alts);
      br = new BufferedReader(reader);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } 
    ArrayList<String> altlist = new ArrayList<>();
    try {
      String current;
      while ((current = br.readLine()) != null)
        altlist.add(current); 
    } catch (IOException e) {
      e.printStackTrace();
    } 
    ArrayList<Alt> altlist1 = new ArrayList<>();
    for (String str : altlist) {
      str.replace("\n", "");
      String[] splitalt = str.split(":");
      altlist1.add(new Alt(splitalt[0], splitalt[1]));
    } 
    return altlist1;
  }
  
  public static boolean saveAlts(ArrayList<Alt> altList) {
    FileWriter writer;
    System.out.println("Dev: Alts file are now getting saved!");
    File alts = getFile();
    alts.delete();
    alts = getFile();
    try {
      writer = new FileWriter(alts);
    } catch (IOException e1) {
      e1.printStackTrace();
      return false;
    } 
    for (Alt str : altList) {
      try {
        writer.write(String.valueOf(str.getUsername()) + ":" + str.getPassword() + "\n");
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      } 
    } 
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    } 
    return true;
  }
  
  private static File getFile() {
    File file = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/");
    if (!file.exists())
      file.mkdirs(); 
    File alts = new File((Minecraft.getMinecraft()).mcDataDir + "/Anubis/alts.cfg");
    if (!alts.exists())
      try {
        alts.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }  
    return alts;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\seltak\anubis\al\\ui\AltUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */