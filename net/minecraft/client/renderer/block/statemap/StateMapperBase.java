package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public abstract class StateMapperBase implements IStateMapper {
  protected Map<IBlockState, ModelResourceLocation> mapStateModelLocations = Maps.newLinkedHashMap();
  
  public String getPropertyString(Map<IProperty<?>, Comparable<?>> values) {
    StringBuilder stringbuilder = new StringBuilder();
    for (Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
      if (stringbuilder.length() != 0)
        stringbuilder.append(","); 
      IProperty<?> iproperty = entry.getKey();
      stringbuilder.append(iproperty.getName());
      stringbuilder.append("=");
      stringbuilder.append(getPropertyName(iproperty, entry.getValue()));
    } 
    if (stringbuilder.length() == 0)
      stringbuilder.append("normal"); 
    return stringbuilder.toString();
  }
  
  private <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> value) {
    return property.getName(value);
  }
  
  public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
    UnmodifiableIterator unmodifiableiterator = blockIn.getBlockState().getValidStates().iterator();
    while (unmodifiableiterator.hasNext()) {
      IBlockState iblockstate = (IBlockState)unmodifiableiterator.next();
      this.mapStateModelLocations.put(iblockstate, getModelResourceLocation(iblockstate));
    } 
    return this.mapStateModelLocations;
  }
  
  protected abstract ModelResourceLocation getModelResourceLocation(IBlockState paramIBlockState);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\renderer\block\statemap\StateMapperBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */