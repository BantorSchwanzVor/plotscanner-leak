package net.minecraft.world.gen.structure.template;

import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITemplateProcessor {
  @Nullable
  Template.BlockInfo processBlock(World paramWorld, BlockPos paramBlockPos, Template.BlockInfo paramBlockInfo);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\gen\structure\template\ITemplateProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */