package net.minecraft.client.gui.spectator;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayer;
import net.minecraft.client.gui.spectator.categories.TeleportToTeam;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class BaseSpectatorGroup implements ISpectatorMenuView {
  private final List<ISpectatorMenuObject> items = Lists.newArrayList();
  
  public BaseSpectatorGroup() {
    this.items.add(new TeleportToPlayer());
    this.items.add(new TeleportToTeam());
  }
  
  public List<ISpectatorMenuObject> getItems() {
    return this.items;
  }
  
  public ITextComponent getPrompt() {
    return (ITextComponent)new TextComponentTranslation("spectatorMenu.root.prompt", new Object[0]);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\spectator\BaseSpectatorGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */