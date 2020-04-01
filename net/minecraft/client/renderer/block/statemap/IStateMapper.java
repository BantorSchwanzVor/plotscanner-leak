package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public interface IStateMapper {
  Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block paramBlock);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\statemap\IStateMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */