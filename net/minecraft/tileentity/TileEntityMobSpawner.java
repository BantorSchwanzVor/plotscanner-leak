package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity implements ITickable {
  private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic() {
      public void broadcastEvent(int id) {
        TileEntityMobSpawner.this.world.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.MOB_SPAWNER, id, 0);
      }
      
      public World getSpawnerWorld() {
        return TileEntityMobSpawner.this.world;
      }
      
      public BlockPos getSpawnerPosition() {
        return TileEntityMobSpawner.this.pos;
      }
      
      public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
        super.setNextSpawnData(p_184993_1_);
        if (getSpawnerWorld() != null) {
          IBlockState iblockstate = getSpawnerWorld().getBlockState(getSpawnerPosition());
          getSpawnerWorld().notifyBlockUpdate(TileEntityMobSpawner.this.pos, iblockstate, iblockstate, 4);
        } 
      }
    };
  
  public static void registerFixesMobSpawner(DataFixer fixer) {
    fixer.registerWalker(FixTypes.BLOCK_ENTITY, new IDataWalker() {
          public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
            if (TileEntity.func_190559_a((Class)TileEntityMobSpawner.class).equals(new ResourceLocation(compound.getString("id")))) {
              if (compound.hasKey("SpawnPotentials", 9)) {
                NBTTagList nbttaglist = compound.getTagList("SpawnPotentials", 10);
                for (int i = 0; i < nbttaglist.tagCount(); i++) {
                  NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                  nbttagcompound.setTag("Entity", (NBTBase)fixer.process((IFixType)FixTypes.ENTITY, nbttagcompound.getCompoundTag("Entity"), versionIn));
                } 
              } 
              compound.setTag("SpawnData", (NBTBase)fixer.process((IFixType)FixTypes.ENTITY, compound.getCompoundTag("SpawnData"), versionIn));
            } 
            return compound;
          }
        });
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.spawnerLogic.readFromNBT(compound);
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    this.spawnerLogic.writeToNBT(compound);
    return compound;
  }
  
  public void update() {
    this.spawnerLogic.updateSpawner();
  }
  
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 1, getUpdateTag());
  }
  
  public NBTTagCompound getUpdateTag() {
    NBTTagCompound nbttagcompound = writeToNBT(new NBTTagCompound());
    nbttagcompound.removeTag("SpawnPotentials");
    return nbttagcompound;
  }
  
  public boolean receiveClientEvent(int id, int type) {
    return this.spawnerLogic.setDelayToMin(id) ? true : super.receiveClientEvent(id, type);
  }
  
  public boolean onlyOpsCanSetNbt() {
    return true;
  }
  
  public MobSpawnerBaseLogic getSpawnerBaseLogic() {
    return this.spawnerLogic;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntityMobSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */