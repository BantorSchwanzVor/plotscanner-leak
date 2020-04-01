package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject {
  private static final Ordering<NetworkPlayerInfo> PROFILE_ORDER = Ordering.from(new Comparator<NetworkPlayerInfo>() {
        public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
          return ComparisonChain.start().compare(p_compare_1_.getGameProfile().getId(), p_compare_2_.getGameProfile().getId()).result();
        }
      });
  
  private final List<ISpectatorMenuObject> items;
  
  public TeleportToPlayer() {
    this(PROFILE_ORDER.sortedCopy(Minecraft.getMinecraft().getConnection().getPlayerInfoMap()));
  }
  
  public TeleportToPlayer(Collection<NetworkPlayerInfo> p_i45493_1_) {
    this.items = Lists.newArrayList();
    for (NetworkPlayerInfo networkplayerinfo : PROFILE_ORDER.sortedCopy(p_i45493_1_)) {
      if (networkplayerinfo.getGameType() != GameType.SPECTATOR)
        this.items.add(new PlayerMenuObject(networkplayerinfo.getGameProfile())); 
    } 
  }
  
  public List<ISpectatorMenuObject> getItems() {
    return this.items;
  }
  
  public ITextComponent getPrompt() {
    return (ITextComponent)new TextComponentTranslation("spectatorMenu.teleport.prompt", new Object[0]);
  }
  
  public void selectItem(SpectatorMenu menu) {
    menu.selectCategory(this);
  }
  
  public ITextComponent getSpectatorName() {
    return (ITextComponent)new TextComponentTranslation("spectatorMenu.teleport", new Object[0]);
  }
  
  public void renderIcon(float p_178663_1_, int alpha) {
    Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, 16, 16, 256.0F, 256.0F);
  }
  
  public boolean isEnabled() {
    return !this.items.isEmpty();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\spectator\categories\TeleportToPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */