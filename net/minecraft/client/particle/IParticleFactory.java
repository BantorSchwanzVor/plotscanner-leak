package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.world.World;

public interface IParticleFactory {
  @Nullable
  Particle createParticle(int paramInt, World paramWorld, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, int... paramVarArgs);
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\particle\IParticleFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */