package net.minecraftforge.common.model;

import java.util.Optional;

public interface IModelState {
  Optional<TRSRTransformation> apply(Optional<? extends IModelPart> paramOptional);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraftforge\common\model\IModelState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */