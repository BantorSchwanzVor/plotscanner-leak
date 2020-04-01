package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

public class GuiCreateFlatWorld extends GuiScreen {
  private final GuiCreateWorld createWorldGui;
  
  private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
  
  private String flatWorldTitle;
  
  private String materialText;
  
  private String heightText;
  
  private Details createFlatWorldListSlotGui;
  
  private GuiButton addLayerButton;
  
  private GuiButton editLayerButton;
  
  private GuiButton removeLayerButton;
  
  public GuiCreateFlatWorld(GuiCreateWorld createWorldGuiIn, String preset) {
    this.createWorldGui = createWorldGuiIn;
    setPreset(preset);
  }
  
  public String getPreset() {
    return this.theFlatGeneratorInfo.toString();
  }
  
  public void setPreset(String preset) {
    this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(preset);
  }
  
  public void initGui() {
    this.buttonList.clear();
    this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
    this.materialText = I18n.format("createWorld.customize.flat.tile", new Object[0]);
    this.heightText = I18n.format("createWorld.customize.flat.height", new Object[0]);
    this.createFlatWorldListSlotGui = new Details();
    this.addLayerButton = addButton(new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.addLayer", new Object[0])) + " (NYI)"));
    this.editLayerButton = addButton(new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.editLayer", new Object[0])) + " (NYI)"));
    this.removeLayerButton = addButton(new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
    this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
    this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
    this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
    this.addLayerButton.visible = false;
    this.editLayerButton.visible = false;
    this.theFlatGeneratorInfo.updateLayers();
    onLayersChanged();
  }
  
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.createFlatWorldListSlotGui.handleMouseInput();
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.selectedLayer - 1;
    if (button.id == 1) {
      this.mc.displayGuiScreen(this.createWorldGui);
    } else if (button.id == 0) {
      this.createWorldGui.chunkProviderSettingsJson = getPreset();
      this.mc.displayGuiScreen(this.createWorldGui);
    } else if (button.id == 5) {
      this.mc.displayGuiScreen(new GuiFlatPresets(this));
    } else if (button.id == 4 && hasSelectedLayer()) {
      this.theFlatGeneratorInfo.getFlatLayers().remove(i);
      this.createFlatWorldListSlotGui.selectedLayer = Math.min(this.createFlatWorldListSlotGui.selectedLayer, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
    } 
    this.theFlatGeneratorInfo.updateLayers();
    onLayersChanged();
  }
  
  public void onLayersChanged() {
    boolean flag = hasSelectedLayer();
    this.removeLayerButton.enabled = flag;
    this.editLayerButton.enabled = flag;
    this.editLayerButton.enabled = false;
    this.addLayerButton.enabled = false;
  }
  
  private boolean hasSelectedLayer() {
    return (this.createFlatWorldListSlotGui.selectedLayer > -1 && this.createFlatWorldListSlotGui.selectedLayer < this.theFlatGeneratorInfo.getFlatLayers().size());
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
    drawCenteredString(this.fontRendererObj, this.flatWorldTitle, this.width / 2, 8, 16777215);
    int i = this.width / 2 - 92 - 16;
    drawString(this.fontRendererObj, this.materialText, i, 32, 16777215);
    drawString(this.fontRendererObj, this.heightText, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.heightText), 32, 16777215);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  class Details extends GuiSlot {
    public int selectedLayer = -1;
    
    public Details() {
      super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
    }
    
    private void drawItem(int x, int z, ItemStack itemToDraw) {
      drawItemBackground(x + 1, z + 1);
      GlStateManager.enableRescaleNormal();
      if (!itemToDraw.func_190926_b()) {
        RenderHelper.enableGUIStandardItemLighting();
        GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(itemToDraw, x + 2, z + 2);
        RenderHelper.disableStandardItemLighting();
      } 
      GlStateManager.disableRescaleNormal();
    }
    
    private void drawItemBackground(int x, int y) {
      drawItemBackground(x, y, 0, 0);
    }
    
    private void drawItemBackground(int x, int z, int textureX, int textureY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
      float f = 0.0078125F;
      float f1 = 0.0078125F;
      int i = 18;
      int j = 18;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos((x + 0), (z + 18), GuiCreateFlatWorld.this.zLevel).tex(((textureX + 0) * 0.0078125F), ((textureY + 18) * 0.0078125F)).endVertex();
      bufferbuilder.pos((x + 18), (z + 18), GuiCreateFlatWorld.this.zLevel).tex(((textureX + 18) * 0.0078125F), ((textureY + 18) * 0.0078125F)).endVertex();
      bufferbuilder.pos((x + 18), (z + 0), GuiCreateFlatWorld.this.zLevel).tex(((textureX + 18) * 0.0078125F), ((textureY + 0) * 0.0078125F)).endVertex();
      bufferbuilder.pos((x + 0), (z + 0), GuiCreateFlatWorld.this.zLevel).tex(((textureX + 0) * 0.0078125F), ((textureY + 0) * 0.0078125F)).endVertex();
      tessellator.draw();
    }
    
    protected int getSize() {
      return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
    }
    
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
      this.selectedLayer = slotIndex;
      GuiCreateFlatWorld.this.onLayersChanged();
    }
    
    protected boolean isSelected(int slotIndex) {
      return (slotIndex == this.selectedLayer);
    }
    
    protected void drawBackground() {}
    
    protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
      String s1;
      FlatLayerInfo flatlayerinfo = GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - p_192637_1_ - 1);
      IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
      Block block = iblockstate.getBlock();
      Item item = Item.getItemFromBlock(block);
      if (item == Items.field_190931_a)
        if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
          if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
            item = Items.LAVA_BUCKET; 
        } else {
          item = Items.WATER_BUCKET;
        }  
      ItemStack itemstack = new ItemStack(item, 1, item.getHasSubtypes() ? block.getMetaFromState(iblockstate) : 0);
      String s = item.getItemStackDisplayName(itemstack);
      drawItem(p_192637_2_, p_192637_3_, itemstack);
      GuiCreateFlatWorld.this.fontRendererObj.drawString(s, p_192637_2_ + 18 + 5, p_192637_3_ + 3, 16777215);
      if (p_192637_1_ == 0) {
        s1 = I18n.format("createWorld.customize.flat.layer.top", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
      } else if (p_192637_1_ == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1) {
        s1 = I18n.format("createWorld.customize.flat.layer.bottom", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
      } else {
        s1 = I18n.format("createWorld.customize.flat.layer", new Object[] { Integer.valueOf(flatlayerinfo.getLayerCount()) });
      } 
      GuiCreateFlatWorld.this.fontRendererObj.drawString(s1, p_192637_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(s1), p_192637_3_ + 3, 16777215);
    }
    
    protected int getScrollBarX() {
      return this.width - 70;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiCreateFlatWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */