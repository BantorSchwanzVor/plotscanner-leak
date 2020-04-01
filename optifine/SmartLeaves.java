package optifine;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class SmartLeaves {
  private static IBakedModel modelLeavesCullAcacia = null;
  
  private static IBakedModel modelLeavesCullBirch = null;
  
  private static IBakedModel modelLeavesCullDarkOak = null;
  
  private static IBakedModel modelLeavesCullJungle = null;
  
  private static IBakedModel modelLeavesCullOak = null;
  
  private static IBakedModel modelLeavesCullSpruce = null;
  
  private static List generalQuadsCullAcacia = null;
  
  private static List generalQuadsCullBirch = null;
  
  private static List generalQuadsCullDarkOak = null;
  
  private static List generalQuadsCullJungle = null;
  
  private static List generalQuadsCullOak = null;
  
  private static List generalQuadsCullSpruce = null;
  
  private static IBakedModel modelLeavesDoubleAcacia = null;
  
  private static IBakedModel modelLeavesDoubleBirch = null;
  
  private static IBakedModel modelLeavesDoubleDarkOak = null;
  
  private static IBakedModel modelLeavesDoubleJungle = null;
  
  private static IBakedModel modelLeavesDoubleOak = null;
  
  private static IBakedModel modelLeavesDoubleSpruce = null;
  
  public static IBakedModel getLeavesModel(IBakedModel p_getLeavesModel_0_, IBlockState p_getLeavesModel_1_) {
    if (!Config.isTreesSmart())
      return p_getLeavesModel_0_; 
    List list = p_getLeavesModel_0_.getQuads(p_getLeavesModel_1_, null, 0L);
    if (list == generalQuadsCullAcacia)
      return modelLeavesDoubleAcacia; 
    if (list == generalQuadsCullBirch)
      return modelLeavesDoubleBirch; 
    if (list == generalQuadsCullDarkOak)
      return modelLeavesDoubleDarkOak; 
    if (list == generalQuadsCullJungle)
      return modelLeavesDoubleJungle; 
    if (list == generalQuadsCullOak)
      return modelLeavesDoubleOak; 
    return (list == generalQuadsCullSpruce) ? modelLeavesDoubleSpruce : p_getLeavesModel_0_;
  }
  
  public static boolean isSameLeaves(IBlockState p_isSameLeaves_0_, IBlockState p_isSameLeaves_1_) {
    if (p_isSameLeaves_0_ == p_isSameLeaves_1_)
      return true; 
    Block block = p_isSameLeaves_0_.getBlock();
    Block block1 = p_isSameLeaves_1_.getBlock();
    if (block != block1)
      return false; 
    if (block instanceof BlockOldLeaf)
      return ((BlockPlanks.EnumType)p_isSameLeaves_0_.getValue((IProperty)BlockOldLeaf.VARIANT)).equals(p_isSameLeaves_1_.getValue((IProperty)BlockOldLeaf.VARIANT)); 
    return (block instanceof BlockNewLeaf) ? ((BlockPlanks.EnumType)p_isSameLeaves_0_.getValue((IProperty)BlockNewLeaf.VARIANT)).equals(p_isSameLeaves_1_.getValue((IProperty)BlockNewLeaf.VARIANT)) : false;
  }
  
  public static void updateLeavesModels() {
    List list = new ArrayList();
    modelLeavesCullAcacia = getModelCull("acacia", list);
    modelLeavesCullBirch = getModelCull("birch", list);
    modelLeavesCullDarkOak = getModelCull("dark_oak", list);
    modelLeavesCullJungle = getModelCull("jungle", list);
    modelLeavesCullOak = getModelCull("oak", list);
    modelLeavesCullSpruce = getModelCull("spruce", list);
    generalQuadsCullAcacia = getGeneralQuadsSafe(modelLeavesCullAcacia);
    generalQuadsCullBirch = getGeneralQuadsSafe(modelLeavesCullBirch);
    generalQuadsCullDarkOak = getGeneralQuadsSafe(modelLeavesCullDarkOak);
    generalQuadsCullJungle = getGeneralQuadsSafe(modelLeavesCullJungle);
    generalQuadsCullOak = getGeneralQuadsSafe(modelLeavesCullOak);
    generalQuadsCullSpruce = getGeneralQuadsSafe(modelLeavesCullSpruce);
    modelLeavesDoubleAcacia = getModelDoubleFace(modelLeavesCullAcacia);
    modelLeavesDoubleBirch = getModelDoubleFace(modelLeavesCullBirch);
    modelLeavesDoubleDarkOak = getModelDoubleFace(modelLeavesCullDarkOak);
    modelLeavesDoubleJungle = getModelDoubleFace(modelLeavesCullJungle);
    modelLeavesDoubleOak = getModelDoubleFace(modelLeavesCullOak);
    modelLeavesDoubleSpruce = getModelDoubleFace(modelLeavesCullSpruce);
    if (list.size() > 0)
      Config.dbg("Enable face culling: " + Config.arrayToString(list.toArray())); 
  }
  
  private static List getGeneralQuadsSafe(IBakedModel p_getGeneralQuadsSafe_0_) {
    return (p_getGeneralQuadsSafe_0_ == null) ? null : p_getGeneralQuadsSafe_0_.getQuads(null, null, 0L);
  }
  
  static IBakedModel getModelCull(String p_getModelCull_0_, List<String> p_getModelCull_1_) {
    ModelManager modelmanager = Config.getModelManager();
    if (modelmanager == null)
      return null; 
    ResourceLocation resourcelocation = new ResourceLocation("blockstates/" + p_getModelCull_0_ + "_leaves.json");
    if (Config.getDefiningResourcePack(resourcelocation) != Config.getDefaultResourcePack())
      return null; 
    ResourceLocation resourcelocation1 = new ResourceLocation("models/block/" + p_getModelCull_0_ + "_leaves.json");
    if (Config.getDefiningResourcePack(resourcelocation1) != Config.getDefaultResourcePack())
      return null; 
    ModelResourceLocation modelresourcelocation = new ModelResourceLocation(String.valueOf(p_getModelCull_0_) + "_leaves", "normal");
    IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);
    if (ibakedmodel != null && ibakedmodel != modelmanager.getMissingModel()) {
      List list = ibakedmodel.getQuads(null, null, 0L);
      if (list.size() == 0)
        return ibakedmodel; 
      if (list.size() != 6)
        return null; 
      for (Object bakedquad : list) {
        List<Object> list1 = ibakedmodel.getQuads(null, ((BakedQuad)bakedquad).getFace(), 0L);
        if (list1.size() > 0)
          return null; 
        list1.add(bakedquad);
      } 
      list.clear();
      p_getModelCull_1_.add(String.valueOf(p_getModelCull_0_) + "_leaves");
      return ibakedmodel;
    } 
    return null;
  }
  
  private static IBakedModel getModelDoubleFace(IBakedModel p_getModelDoubleFace_0_) {
    if (p_getModelDoubleFace_0_ == null)
      return null; 
    if (p_getModelDoubleFace_0_.getQuads(null, null, 0L).size() > 0) {
      Config.warn("SmartLeaves: Model is not cube, general quads: " + p_getModelDoubleFace_0_.getQuads(null, null, 0L).size() + ", model: " + p_getModelDoubleFace_0_);
      return p_getModelDoubleFace_0_;
    } 
    EnumFacing[] aenumfacing = EnumFacing.VALUES;
    for (int i = 0; i < aenumfacing.length; i++) {
      EnumFacing enumfacing = aenumfacing[i];
      List<BakedQuad> list = p_getModelDoubleFace_0_.getQuads(null, enumfacing, 0L);
      if (list.size() != 1) {
        Config.warn("SmartLeaves: Model is not cube, side: " + enumfacing + ", quads: " + list.size() + ", model: " + p_getModelDoubleFace_0_);
        return p_getModelDoubleFace_0_;
      } 
    } 
    IBakedModel ibakedmodel = ModelUtils.duplicateModel(p_getModelDoubleFace_0_);
    List[] alist = new List[aenumfacing.length];
    for (int k = 0; k < aenumfacing.length; k++) {
      EnumFacing enumfacing1 = aenumfacing[k];
      List<BakedQuad> list1 = ibakedmodel.getQuads(null, enumfacing1, 0L);
      BakedQuad bakedquad = list1.get(0);
      BakedQuad bakedquad1 = new BakedQuad((int[])bakedquad.getVertexData().clone(), bakedquad.getTintIndex(), bakedquad.getFace(), bakedquad.getSprite());
      int[] aint = bakedquad1.getVertexData();
      int[] aint1 = (int[])aint.clone();
      int j = aint.length / 4;
      System.arraycopy(aint, 0 * j, aint1, 3 * j, j);
      System.arraycopy(aint, 1 * j, aint1, 2 * j, j);
      System.arraycopy(aint, 2 * j, aint1, 1 * j, j);
      System.arraycopy(aint, 3 * j, aint1, 0 * j, j);
      System.arraycopy(aint1, 0, aint, 0, aint1.length);
      list1.add(bakedquad1);
    } 
    return ibakedmodel;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\optifine\SmartLeaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */