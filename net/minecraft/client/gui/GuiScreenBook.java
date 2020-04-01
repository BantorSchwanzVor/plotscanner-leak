package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook extends GuiScreen {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
  
  private final EntityPlayer editingPlayer;
  
  private final ItemStack bookObj;
  
  private final boolean bookIsUnsigned;
  
  private boolean bookIsModified;
  
  private boolean bookGettingSigned;
  
  private int updateCount;
  
  private final int bookImageWidth = 192;
  
  private final int bookImageHeight = 192;
  
  private int bookTotalPages = 1;
  
  private int currPage;
  
  private NBTTagList bookPages;
  
  private String bookTitle = "";
  
  private List<ITextComponent> cachedComponents;
  
  private int cachedPage = -1;
  
  private NextPageButton buttonNextPage;
  
  private NextPageButton buttonPreviousPage;
  
  private GuiButton buttonDone;
  
  private GuiButton buttonSign;
  
  private GuiButton buttonFinalize;
  
  private GuiButton buttonCancel;
  
  public GuiScreenBook(EntityPlayer player, ItemStack book, boolean isUnsigned) {
    this.editingPlayer = player;
    this.bookObj = book;
    this.bookIsUnsigned = isUnsigned;
    if (book.hasTagCompound()) {
      NBTTagCompound nbttagcompound = book.getTagCompound();
      this.bookPages = nbttagcompound.getTagList("pages", 8).copy();
      this.bookTotalPages = this.bookPages.tagCount();
      if (this.bookTotalPages < 1)
        this.bookTotalPages = 1; 
    } 
    if (this.bookPages == null && isUnsigned) {
      this.bookPages = new NBTTagList();
      this.bookPages.appendTag((NBTBase)new NBTTagString(""));
      this.bookTotalPages = 1;
    } 
  }
  
  public void updateScreen() {
    super.updateScreen();
    this.updateCount++;
  }
  
  public void initGui() {
    this.buttonList.clear();
    Keyboard.enableRepeatEvents(true);
    if (this.bookIsUnsigned) {
      this.buttonSign = addButton(new GuiButton(3, this.width / 2 - 100, 196, 98, 20, I18n.format("book.signButton", new Object[0])));
      this.buttonDone = addButton(new GuiButton(0, this.width / 2 + 2, 196, 98, 20, I18n.format("gui.done", new Object[0])));
      this.buttonFinalize = addButton(new GuiButton(5, this.width / 2 - 100, 196, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
      this.buttonCancel = addButton(new GuiButton(4, this.width / 2 + 2, 196, 98, 20, I18n.format("gui.cancel", new Object[0])));
    } else {
      this.buttonDone = addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done", new Object[0])));
    } 
    int i = (this.width - 192) / 2;
    int j = 2;
    this.buttonNextPage = addButton(new NextPageButton(1, i + 120, 156, true));
    this.buttonPreviousPage = addButton(new NextPageButton(2, i + 38, 156, false));
    updateButtons();
  }
  
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  private void updateButtons() {
    this.buttonNextPage.visible = (!this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned));
    this.buttonPreviousPage.visible = (!this.bookGettingSigned && this.currPage > 0);
    this.buttonDone.visible = !(this.bookIsUnsigned && this.bookGettingSigned);
    if (this.bookIsUnsigned) {
      this.buttonSign.visible = !this.bookGettingSigned;
      this.buttonCancel.visible = this.bookGettingSigned;
      this.buttonFinalize.visible = this.bookGettingSigned;
      this.buttonFinalize.enabled = !this.bookTitle.trim().isEmpty();
    } 
  }
  
  private void sendBookToServer(boolean publish) throws IOException {
    if (this.bookIsUnsigned && this.bookIsModified)
      if (this.bookPages != null) {
        while (this.bookPages.tagCount() > 1) {
          String s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
          if (!s.isEmpty())
            break; 
          this.bookPages.removeTag(this.bookPages.tagCount() - 1);
        } 
        if (this.bookObj.hasTagCompound()) {
          NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
          nbttagcompound.setTag("pages", (NBTBase)this.bookPages);
        } else {
          this.bookObj.setTagInfo("pages", (NBTBase)this.bookPages);
        } 
        String s1 = "MC|BEdit";
        if (publish) {
          s1 = "MC|BSign";
          this.bookObj.setTagInfo("author", (NBTBase)new NBTTagString(this.editingPlayer.getName()));
          this.bookObj.setTagInfo("title", (NBTBase)new NBTTagString(this.bookTitle.trim()));
        } 
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        packetbuffer.writeItemStackToBuffer(this.bookObj);
        this.mc.getConnection().sendPacket((Packet)new CPacketCustomPayload(s1, packetbuffer));
      }  
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.enabled) {
      if (button.id == 0) {
        this.mc.displayGuiScreen(null);
        sendBookToServer(false);
      } else if (button.id == 3 && this.bookIsUnsigned) {
        this.bookGettingSigned = true;
      } else if (button.id == 1) {
        if (this.currPage < this.bookTotalPages - 1) {
          this.currPage++;
        } else if (this.bookIsUnsigned) {
          addNewPage();
          if (this.currPage < this.bookTotalPages - 1)
            this.currPage++; 
        } 
      } else if (button.id == 2) {
        if (this.currPage > 0)
          this.currPage--; 
      } else if (button.id == 5 && this.bookGettingSigned) {
        sendBookToServer(true);
        this.mc.displayGuiScreen(null);
      } else if (button.id == 4 && this.bookGettingSigned) {
        this.bookGettingSigned = false;
      } 
      updateButtons();
    } 
  }
  
  private void addNewPage() {
    if (this.bookPages != null && this.bookPages.tagCount() < 50) {
      this.bookPages.appendTag((NBTBase)new NBTTagString(""));
      this.bookTotalPages++;
      this.bookIsModified = true;
    } 
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    if (this.bookIsUnsigned)
      if (this.bookGettingSigned) {
        keyTypedInTitle(typedChar, keyCode);
      } else {
        keyTypedInBook(typedChar, keyCode);
      }  
  }
  
  private void keyTypedInBook(char typedChar, int keyCode) {
    if (GuiScreen.isKeyComboCtrlV(keyCode)) {
      pageInsertIntoCurrent(GuiScreen.getClipboardString());
    } else {
      String s;
      switch (keyCode) {
        case 14:
          s = pageGetCurrent();
          if (!s.isEmpty())
            pageSetCurrent(s.substring(0, s.length() - 1)); 
          return;
        case 28:
        case 156:
          pageInsertIntoCurrent("\n");
          return;
      } 
      if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
        pageInsertIntoCurrent(Character.toString(typedChar)); 
    } 
  }
  
  private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) throws IOException {
    switch (p_146460_2_) {
      case 14:
        if (!this.bookTitle.isEmpty()) {
          this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
          updateButtons();
        } 
        return;
      case 28:
      case 156:
        if (!this.bookTitle.isEmpty()) {
          sendBookToServer(true);
          this.mc.displayGuiScreen(null);
        } 
        return;
    } 
    if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
      this.bookTitle = String.valueOf(this.bookTitle) + Character.toString(p_146460_1_);
      updateButtons();
      this.bookIsModified = true;
    } 
  }
  
  private String pageGetCurrent() {
    return (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) ? this.bookPages.getStringTagAt(this.currPage) : "";
  }
  
  private void pageSetCurrent(String p_146457_1_) {
    if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
      this.bookPages.set(this.currPage, (NBTBase)new NBTTagString(p_146457_1_));
      this.bookIsModified = true;
    } 
  }
  
  private void pageInsertIntoCurrent(String p_146459_1_) {
    String s = pageGetCurrent();
    String s1 = String.valueOf(s) + p_146459_1_;
    int i = this.fontRendererObj.splitStringWidth(String.valueOf(s1) + TextFormatting.BLACK + "_", 118);
    if (i <= 128 && s1.length() < 256)
      pageSetCurrent(s1); 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
    int i = (this.width - 192) / 2;
    int j = 2;
    drawTexturedModalRect(i, 2, 0, 0, 192, 192);
    if (this.bookGettingSigned) {
      String s = this.bookTitle;
      if (this.bookIsUnsigned)
        if (this.updateCount / 6 % 2 == 0) {
          s = String.valueOf(s) + TextFormatting.BLACK + "_";
        } else {
          s = String.valueOf(s) + TextFormatting.GRAY + "_";
        }  
      String s1 = I18n.format("book.editTitle", new Object[0]);
      int k = this.fontRendererObj.getStringWidth(s1);
      this.fontRendererObj.drawString(s1, i + 36 + (116 - k) / 2, 34, 0);
      int l = this.fontRendererObj.getStringWidth(s);
      this.fontRendererObj.drawString(s, i + 36 + (116 - l) / 2, 50, 0);
      String s2 = I18n.format("book.byAuthor", new Object[] { this.editingPlayer.getName() });
      int i1 = this.fontRendererObj.getStringWidth(s2);
      this.fontRendererObj.drawString(TextFormatting.DARK_GRAY + s2, i + 36 + (116 - i1) / 2, 60, 0);
      String s3 = I18n.format("book.finalizeWarning", new Object[0]);
      this.fontRendererObj.drawSplitString(s3, i + 36, 82, 116, 0);
    } else {
      String s4 = I18n.format("book.pageIndicator", new Object[] { Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages) });
      String s5 = "";
      if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount())
        s5 = this.bookPages.getStringTagAt(this.currPage); 
      if (this.bookIsUnsigned) {
        if (this.fontRendererObj.getBidiFlag()) {
          s5 = String.valueOf(s5) + "_";
        } else if (this.updateCount / 6 % 2 == 0) {
          s5 = String.valueOf(s5) + TextFormatting.BLACK + "_";
        } else {
          s5 = String.valueOf(s5) + TextFormatting.GRAY + "_";
        } 
      } else if (this.cachedPage != this.currPage) {
        if (ItemWrittenBook.validBookTagContents(this.bookObj.getTagCompound())) {
          try {
            ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s5);
            this.cachedComponents = (itextcomponent != null) ? GuiUtilRenderComponents.splitText(itextcomponent, 116, this.fontRendererObj, true, true) : null;
          } catch (JsonParseException var13) {
            this.cachedComponents = null;
          } 
        } else {
          TextComponentString textcomponentstring = new TextComponentString(TextFormatting.DARK_RED + "* Invalid book tag *");
          this.cachedComponents = Lists.newArrayList((Iterable)textcomponentstring);
        } 
        this.cachedPage = this.currPage;
      } 
      int j1 = this.fontRendererObj.getStringWidth(s4);
      this.fontRendererObj.drawString(s4, i - j1 + 192 - 44, 18, 0);
      if (this.cachedComponents == null) {
        this.fontRendererObj.drawSplitString(s5, i + 36, 34, 116, 0);
      } else {
        int k1 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.cachedComponents.size());
        for (int l1 = 0; l1 < k1; l1++) {
          ITextComponent itextcomponent2 = this.cachedComponents.get(l1);
          this.fontRendererObj.drawString(itextcomponent2.getUnformattedText(), i + 36, 34 + l1 * this.fontRendererObj.FONT_HEIGHT, 0);
        } 
        ITextComponent itextcomponent1 = getClickedComponentAt(mouseX, mouseY);
        if (itextcomponent1 != null)
          handleComponentHover(itextcomponent1, mouseX, mouseY); 
      } 
    } 
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    if (mouseButton == 0) {
      ITextComponent itextcomponent = getClickedComponentAt(mouseX, mouseY);
      if (itextcomponent != null && handleComponentClick(itextcomponent))
        return; 
    } 
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  public boolean handleComponentClick(ITextComponent component) {
    ClickEvent clickevent = component.getStyle().getClickEvent();
    if (clickevent == null)
      return false; 
    if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
      String s = clickevent.getValue();
      try {
        int i = Integer.parseInt(s) - 1;
        if (i >= 0 && i < this.bookTotalPages && i != this.currPage) {
          this.currPage = i;
          updateButtons();
          return true;
        } 
      } catch (Throwable throwable) {}
      return false;
    } 
    boolean flag = super.handleComponentClick(component);
    if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
      this.mc.displayGuiScreen(null); 
    return flag;
  }
  
  @Nullable
  public ITextComponent getClickedComponentAt(int p_175385_1_, int p_175385_2_) {
    if (this.cachedComponents == null)
      return null; 
    int i = p_175385_1_ - (this.width - 192) / 2 - 36;
    int j = p_175385_2_ - 2 - 16 - 16;
    if (i >= 0 && j >= 0) {
      int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.cachedComponents.size());
      if (i <= 116 && j < this.mc.fontRendererObj.FONT_HEIGHT * k + k) {
        int l = j / this.mc.fontRendererObj.FONT_HEIGHT;
        if (l >= 0 && l < this.cachedComponents.size()) {
          ITextComponent itextcomponent = this.cachedComponents.get(l);
          int i1 = 0;
          for (ITextComponent itextcomponent1 : itextcomponent) {
            if (itextcomponent1 instanceof TextComponentString) {
              i1 += this.mc.fontRendererObj.getStringWidth(((TextComponentString)itextcomponent1).getText());
              if (i1 > i)
                return itextcomponent1; 
            } 
          } 
        } 
        return null;
      } 
      return null;
    } 
    return null;
  }
  
  static class NextPageButton extends GuiButton {
    private final boolean isForward;
    
    public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_) {
      super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
      this.isForward = p_i46316_4_;
    }
    
    public void func_191745_a(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float p_191745_4_) {
      if (this.visible) {
        boolean flag = (p_191745_2_ >= this.xPosition && p_191745_3_ >= this.yPosition && p_191745_2_ < this.xPosition + this.width && p_191745_3_ < this.yPosition + this.height);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        p_191745_1_.getTextureManager().bindTexture(GuiScreenBook.BOOK_GUI_TEXTURES);
        int i = 0;
        int j = 192;
        if (flag)
          i += 23; 
        if (!this.isForward)
          j += 13; 
        drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
      } 
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiScreenBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */