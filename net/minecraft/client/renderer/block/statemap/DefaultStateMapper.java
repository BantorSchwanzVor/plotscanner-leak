package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class DefaultStateMapper extends StateMapperBase {
  protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
    return new ModelResourceLocation((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock()), getPropertyString((Map<IProperty<?>, Comparable<?>>)state.getProperties()));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\statemap\DefaultStateMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */