package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class FlatLayerInfo {
  private final int version;
  
  private IBlockState layerMaterial;
  
  private int layerCount;
  
  private int layerMinimumY;
  
  public FlatLayerInfo(int p_i45467_1_, Block layerMaterialIn) {
    this(3, p_i45467_1_, layerMaterialIn);
  }
  
  public FlatLayerInfo(int p_i45627_1_, int height, Block layerMaterialIn) {
    this.layerCount = 1;
    this.version = p_i45627_1_;
    this.layerCount = height;
    this.layerMaterial = layerMaterialIn.getDefaultState();
  }
  
  public FlatLayerInfo(int p_i45628_1_, int p_i45628_2_, Block layerMaterialIn, int p_i45628_4_) {
    this(p_i45628_1_, p_i45628_2_, layerMaterialIn);
    this.layerMaterial = layerMaterialIn.getStateFromMeta(p_i45628_4_);
  }
  
  public int getLayerCount() {
    return this.layerCount;
  }
  
  public IBlockState getLayerMaterial() {
    return this.layerMaterial;
  }
  
  private Block getLayerMaterialBlock() {
    return this.layerMaterial.getBlock();
  }
  
  private int getFillBlockMeta() {
    return this.layerMaterial.getBlock().getMetaFromState(this.layerMaterial);
  }
  
  public int getMinY() {
    return this.layerMinimumY;
  }
  
  public void setMinY(int minY) {
    this.layerMinimumY = minY;
  }
  
  public String toString() {
    String s;
    if (this.version >= 3) {
      ResourceLocation resourcelocation = (ResourceLocation)Block.REGISTRY.getNameForObject(getLayerMaterialBlock());
      s = (resourcelocation == null) ? "null" : resourcelocation.toString();
      if (this.layerCount > 1)
        s = String.valueOf(this.layerCount) + "*" + s; 
    } else {
      s = Integer.toString(Block.getIdFromBlock(getLayerMaterialBlock()));
      if (this.layerCount > 1)
        s = String.valueOf(this.layerCount) + "x" + s; 
    } 
    int i = getFillBlockMeta();
    if (i > 0)
      s = String.valueOf(s) + ":" + i; 
    return s;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\FlatLayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */