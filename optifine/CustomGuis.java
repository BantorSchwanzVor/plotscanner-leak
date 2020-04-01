package optifine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CustomGuis {
  private static Minecraft mc = Config.getMinecraft();
  
  private static PlayerControllerOF playerControllerOF = null;
  
  private static CustomGuiProperties[][] guiProperties = null;
  
  public static boolean isChristmas = isChristmas();
  
  public static ResourceLocation getTextureLocation(ResourceLocation p_getTextureLocation_0_) {
    if (guiProperties == null)
      return p_getTextureLocation_0_; 
    GuiScreen guiscreen = mc.currentScreen;
    if (!(guiscreen instanceof net.minecraft.client.gui.inventory.GuiContainer))
      return p_getTextureLocation_0_; 
    if (p_getTextureLocation_0_.getResourceDomain().equals("minecraft") && p_getTextureLocation_0_.getResourcePath().startsWith("textures/gui/")) {
      if (playerControllerOF == null)
        return p_getTextureLocation_0_; 
      WorldClient worldClient = mc.world;
      if (worldClient == null)
        return p_getTextureLocation_0_; 
      if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiContainerCreative)
        return getTexturePos(CustomGuiProperties.EnumContainer.CREATIVE, mc.player.getPosition(), (IBlockAccess)worldClient, p_getTextureLocation_0_); 
      if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiInventory)
        return getTexturePos(CustomGuiProperties.EnumContainer.INVENTORY, mc.player.getPosition(), (IBlockAccess)worldClient, p_getTextureLocation_0_); 
      BlockPos blockpos = playerControllerOF.getLastClickBlockPos();
      if (blockpos != null) {
        if (guiscreen instanceof net.minecraft.client.gui.GuiRepair)
          return getTexturePos(CustomGuiProperties.EnumContainer.ANVIL, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiBeacon)
          return getTexturePos(CustomGuiProperties.EnumContainer.BEACON, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiBrewingStand)
          return getTexturePos(CustomGuiProperties.EnumContainer.BREWING_STAND, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiChest)
          return getTexturePos(CustomGuiProperties.EnumContainer.CHEST, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiCrafting)
          return getTexturePos(CustomGuiProperties.EnumContainer.CRAFTING, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiDispenser)
          return getTexturePos(CustomGuiProperties.EnumContainer.DISPENSER, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.GuiEnchantment)
          return getTexturePos(CustomGuiProperties.EnumContainer.ENCHANTMENT, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiFurnace)
          return getTexturePos(CustomGuiProperties.EnumContainer.FURNACE, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.GuiHopper)
          return getTexturePos(CustomGuiProperties.EnumContainer.HOPPER, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiShulkerBox)
          return getTexturePos(CustomGuiProperties.EnumContainer.SHULKER_BOX, blockpos, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
      } 
      Entity entity = playerControllerOF.getLastClickEntity();
      if (entity != null) {
        if (guiscreen instanceof net.minecraft.client.gui.inventory.GuiScreenHorseInventory)
          return getTextureEntity(CustomGuiProperties.EnumContainer.HORSE, entity, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
        if (guiscreen instanceof net.minecraft.client.gui.GuiMerchant)
          return getTextureEntity(CustomGuiProperties.EnumContainer.VILLAGER, entity, (IBlockAccess)worldClient, p_getTextureLocation_0_); 
      } 
      return p_getTextureLocation_0_;
    } 
    return p_getTextureLocation_0_;
  }
  
  private static ResourceLocation getTexturePos(CustomGuiProperties.EnumContainer p_getTexturePos_0_, BlockPos p_getTexturePos_1_, IBlockAccess p_getTexturePos_2_, ResourceLocation p_getTexturePos_3_) {
    CustomGuiProperties[] acustomguiproperties = guiProperties[p_getTexturePos_0_.ordinal()];
    if (acustomguiproperties == null)
      return p_getTexturePos_3_; 
    for (int i = 0; i < acustomguiproperties.length; i++) {
      CustomGuiProperties customguiproperties = acustomguiproperties[i];
      if (customguiproperties.matchesPos(p_getTexturePos_0_, p_getTexturePos_1_, p_getTexturePos_2_))
        return customguiproperties.getTextureLocation(p_getTexturePos_3_); 
    } 
    return p_getTexturePos_3_;
  }
  
  private static ResourceLocation getTextureEntity(CustomGuiProperties.EnumContainer p_getTextureEntity_0_, Entity p_getTextureEntity_1_, IBlockAccess p_getTextureEntity_2_, ResourceLocation p_getTextureEntity_3_) {
    CustomGuiProperties[] acustomguiproperties = guiProperties[p_getTextureEntity_0_.ordinal()];
    if (acustomguiproperties == null)
      return p_getTextureEntity_3_; 
    for (int i = 0; i < acustomguiproperties.length; i++) {
      CustomGuiProperties customguiproperties = acustomguiproperties[i];
      if (customguiproperties.matchesEntity(p_getTextureEntity_0_, p_getTextureEntity_1_, p_getTextureEntity_2_))
        return customguiproperties.getTextureLocation(p_getTextureEntity_3_); 
    } 
    return p_getTextureEntity_3_;
  }
  
  public static void update() {
    guiProperties = null;
    if (Config.isCustomGuis()) {
      List<List<CustomGuiProperties>> list = new ArrayList<>();
      IResourcePack[] airesourcepack = Config.getResourcePacks();
      for (int i = airesourcepack.length - 1; i >= 0; i--) {
        IResourcePack iresourcepack = airesourcepack[i];
        update(iresourcepack, list);
      } 
      guiProperties = propertyListToArray(list);
    } 
  }
  
  private static CustomGuiProperties[][] propertyListToArray(List<List<CustomGuiProperties>> p_propertyListToArray_0_) {
    if (p_propertyListToArray_0_.isEmpty())
      return null; 
    CustomGuiProperties[][] acustomguiproperties = new CustomGuiProperties[(CustomGuiProperties.EnumContainer.values()).length][];
    for (int i = 0; i < acustomguiproperties.length; i++) {
      if (p_propertyListToArray_0_.size() > i) {
        List<CustomGuiProperties> list = p_propertyListToArray_0_.get(i);
        if (list != null) {
          CustomGuiProperties[] acustomguiproperties1 = list.<CustomGuiProperties>toArray(new CustomGuiProperties[list.size()]);
          acustomguiproperties[i] = acustomguiproperties1;
        } 
      } 
    } 
    return acustomguiproperties;
  }
  
  private static void update(IResourcePack p_update_0_, List<List<CustomGuiProperties>> p_update_1_) {
    String[] astring = ResUtils.collectFiles(p_update_0_, "optifine/gui/container/", ".properties", (String[])null);
    Arrays.sort((Object[])astring);
    for (int i = 0; i < astring.length; i++) {
      String s = astring[i];
      Config.dbg("CustomGuis: " + s);
      try {
        ResourceLocation resourcelocation = new ResourceLocation(s);
        InputStream inputstream = p_update_0_.getInputStream(resourcelocation);
        if (inputstream == null) {
          Config.warn("CustomGuis file not found: " + s);
        } else {
          Properties properties = new Properties();
          properties.load(inputstream);
          inputstream.close();
          CustomGuiProperties customguiproperties = new CustomGuiProperties(properties, s);
          if (customguiproperties.isValid(s))
            addToList(customguiproperties, p_update_1_); 
        } 
      } catch (FileNotFoundException var9) {
        Config.warn("CustomGuis file not found: " + s);
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    } 
  }
  
  private static void addToList(CustomGuiProperties p_addToList_0_, List<List<CustomGuiProperties>> p_addToList_1_) {
    if (p_addToList_0_.getContainer() == null) {
      warn("Invalid container: " + p_addToList_0_.getContainer());
    } else {
      int i = p_addToList_0_.getContainer().ordinal();
      while (p_addToList_1_.size() <= i)
        p_addToList_1_.add(null); 
      List<CustomGuiProperties> list = p_addToList_1_.get(i);
      if (list == null) {
        list = new ArrayList<>();
        p_addToList_1_.set(i, list);
      } 
      list.add(p_addToList_0_);
    } 
  }
  
  public static PlayerControllerOF getPlayerControllerOF() {
    return playerControllerOF;
  }
  
  public static void setPlayerControllerOF(PlayerControllerOF p_setPlayerControllerOF_0_) {
    playerControllerOF = p_setPlayerControllerOF_0_;
  }
  
  private static boolean isChristmas() {
    Calendar calendar = Calendar.getInstance();
    return (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26);
  }
  
  private static void warn(String p_warn_0_) {
    Config.warn("[CustomGuis] " + p_warn_0_);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\CustomGuis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */