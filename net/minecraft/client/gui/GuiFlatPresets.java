package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends GuiScreen {
  private static final List<LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();
  
  private final GuiCreateFlatWorld parentScreen;
  
  private String presetsTitle;
  
  private String presetsShare;
  
  private String listText;
  
  private ListSlot list;
  
  private GuiButton btnSelect;
  
  private GuiTextField export;
  
  public GuiFlatPresets(GuiCreateFlatWorld p_i46318_1_) {
    this.parentScreen = p_i46318_1_;
  }
  
  public void initGui() {
    this.buttonList.clear();
    Keyboard.enableRepeatEvents(true);
    this.presetsTitle = I18n.format("createWorld.customize.presets.title", new Object[0]);
    this.presetsShare = I18n.format("createWorld.customize.presets.share", new Object[0]);
    this.listText = I18n.format("createWorld.customize.presets.list", new Object[0]);
    this.export = new GuiTextField(2, this.fontRendererObj, 50, 40, this.width - 100, 20);
    this.list = new ListSlot();
    this.export.setMaxStringLength(1230);
    this.export.setText(this.parentScreen.getPreset());
    this.btnSelect = addButton(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
    this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
    updateButtonValidity();
  }
  
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    this.list.handleMouseInput();
  }
  
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    this.export.mouseClicked(mouseX, mouseY, mouseButton);
    super.mouseClicked(mouseX, mouseY, mouseButton);
  }
  
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (!this.export.textboxKeyTyped(typedChar, keyCode))
      super.keyTyped(typedChar, keyCode); 
  }
  
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 0 && hasValidSelection()) {
      this.parentScreen.setPreset(this.export.getText());
      this.mc.displayGuiScreen(this.parentScreen);
    } else if (button.id == 1) {
      this.mc.displayGuiScreen(this.parentScreen);
    } 
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    drawDefaultBackground();
    this.list.drawScreen(mouseX, mouseY, partialTicks);
    drawCenteredString(this.fontRendererObj, this.presetsTitle, this.width / 2, 8, 16777215);
    drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
    drawString(this.fontRendererObj, this.listText, 50, 70, 10526880);
    this.export.drawTextBox();
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  
  public void updateScreen() {
    this.export.updateCursorCounter();
    super.updateScreen();
  }
  
  public void updateButtonValidity() {
    this.btnSelect.enabled = hasValidSelection();
  }
  
  private boolean hasValidSelection() {
    return !((this.list.selected <= -1 || this.list.selected >= FLAT_WORLD_PRESETS.size()) && this.export.getText().length() <= 1);
  }
  
  private static void registerPreset(String name, Item icon, Biome biome, List<String> features, FlatLayerInfo... layers) {
    registerPreset(name, icon, 0, biome, features, layers);
  }
  
  private static void registerPreset(String name, Item icon, int iconMetadata, Biome biome, List<String> features, FlatLayerInfo... layers) {
    FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
    for (int i = layers.length - 1; i >= 0; i--)
      flatgeneratorinfo.getFlatLayers().add(layers[i]); 
    flatgeneratorinfo.setBiome(Biome.getIdForBiome(biome));
    flatgeneratorinfo.updateLayers();
    for (String s : features)
      flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap()); 
    FLAT_WORLD_PRESETS.add(new LayerItem(icon, iconMetadata, name, flatgeneratorinfo.toString()));
  }
  
  static {
    registerPreset(I18n.format("createWorld.customize.preset.classic_flat", new Object[0]), Item.getItemFromBlock((Block)Blocks.GRASS), Biomes.PLAINS, Arrays.asList(new String[] { "village" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.GRASS), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.tunnelers_dream", new Object[0]), Item.getItemFromBlock(Blocks.STONE), Biomes.EXTREME_HILLS, Arrays.asList(new String[] { "biome_1", "dungeon", "decoration", "stronghold", "mineshaft" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.GRASS), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.water_world", new Object[0]), Items.WATER_BUCKET, Biomes.DEEP_OCEAN, Arrays.asList(new String[] { "biome_1", "oceanmonument" }, ), new FlatLayerInfo[] { new FlatLayerInfo(90, (Block)Blocks.WATER), new FlatLayerInfo(5, (Block)Blocks.SAND), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.overworld", new Object[0]), Item.getItemFromBlock((Block)Blocks.TALLGRASS), BlockTallGrass.EnumType.GRASS.getMeta(), Biomes.PLAINS, Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.snowy_kingdom", new Object[0]), Item.getItemFromBlock(Blocks.SNOW_LAYER), Biomes.ICE_PLAINS, Arrays.asList(new String[] { "village", "biome_1" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.SNOW_LAYER), new FlatLayerInfo(1, (Block)Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.bottomless_pit", new Object[0]), Items.FEATHER, Biomes.PLAINS, Arrays.asList(new String[] { "village", "biome_1" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, (Block)Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE) });
    registerPreset(I18n.format("createWorld.customize.preset.desert", new Object[0]), Item.getItemFromBlock((Block)Blocks.SAND), Biomes.DESERT, Arrays.asList(new String[] { "village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon" }, ), new FlatLayerInfo[] { new FlatLayerInfo(8, (Block)Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.redstone_ready", new Object[0]), Items.REDSTONE, Biomes.DESERT, Collections.emptyList(), new FlatLayerInfo[] { new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK) });
    registerPreset(I18n.format("createWorld.customize.preset.the_void", new Object[0]), Item.getItemFromBlock(Blocks.BARRIER), Biomes.VOID, Arrays.asList(new String[] { "decoration" }, ), new FlatLayerInfo[] { new FlatLayerInfo(1, Blocks.AIR) });
  }
  
  static class LayerItem {
    public Item icon;
    
    public int iconMetadata;
    
    public String name;
    
    public String generatorInfo;
    
    public LayerItem(Item iconIn, int iconMetadataIn, String nameIn, String generatorInfoIn) {
      this.icon = iconIn;
      this.iconMetadata = iconMetadataIn;
      this.name = nameIn;
      this.generatorInfo = generatorInfoIn;
    }
  }
  
  class ListSlot extends GuiSlot {
    public int selected = -1;
    
    public ListSlot() {
      super(GuiFlatPresets.this.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80, GuiFlatPresets.this.height - 37, 24);
    }
    
    private void renderIcon(int p_178054_1_, int p_178054_2_, Item icon, int iconMetadata) {
      blitSlotBg(p_178054_1_ + 1, p_178054_2_ + 1);
      GlStateManager.enableRescaleNormal();
      RenderHelper.enableGUIStandardItemLighting();
      GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(icon, 1, icon.getHasSubtypes() ? iconMetadata : 0), p_178054_1_ + 2, p_178054_2_ + 2);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
    }
    
    private void blitSlotBg(int p_148173_1_, int p_148173_2_) {
      blitSlotIcon(p_148173_1_, p_148173_2_, 0, 0);
    }
    
    private void blitSlotIcon(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
      float f = 0.0078125F;
      float f1 = 0.0078125F;
      int i = 18;
      int j = 18;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos((p_148171_1_ + 0), (p_148171_2_ + 18), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 0) * 0.0078125F), ((p_148171_4_ + 18) * 0.0078125F)).endVertex();
      bufferbuilder.pos((p_148171_1_ + 18), (p_148171_2_ + 18), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 18) * 0.0078125F), ((p_148171_4_ + 18) * 0.0078125F)).endVertex();
      bufferbuilder.pos((p_148171_1_ + 18), (p_148171_2_ + 0), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 18) * 0.0078125F), ((p_148171_4_ + 0) * 0.0078125F)).endVertex();
      bufferbuilder.pos((p_148171_1_ + 0), (p_148171_2_ + 0), GuiFlatPresets.this.zLevel).tex(((p_148171_3_ + 0) * 0.0078125F), ((p_148171_4_ + 0) * 0.0078125F)).endVertex();
      tessellator.draw();
    }
    
    protected int getSize() {
      return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
    }
    
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
      this.selected = slotIndex;
      GuiFlatPresets.this.updateButtonValidity();
      GuiFlatPresets.this.export.setText((GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.list.selected)).generatorInfo);
    }
    
    protected boolean isSelected(int slotIndex) {
      return (slotIndex == this.selected);
    }
    
    protected void drawBackground() {}
    
    protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
      GuiFlatPresets.LayerItem guiflatpresets$layeritem = GuiFlatPresets.FLAT_WORLD_PRESETS.get(p_192637_1_);
      renderIcon(p_192637_2_, p_192637_3_, guiflatpresets$layeritem.icon, guiflatpresets$layeritem.iconMetadata);
      GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.name, p_192637_2_ + 18 + 5, p_192637_3_ + 6, 16777215);
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\GuiFlatPresets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */