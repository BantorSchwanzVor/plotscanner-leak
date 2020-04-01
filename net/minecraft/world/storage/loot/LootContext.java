package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class LootContext {
  private final float luck;
  
  private final WorldServer worldObj;
  
  private final LootTableManager lootTableManager;
  
  @Nullable
  private final Entity lootedEntity;
  
  @Nullable
  private final EntityPlayer player;
  
  @Nullable
  private final DamageSource damageSource;
  
  private final Set<LootTable> lootTables = Sets.newLinkedHashSet();
  
  public LootContext(float luckIn, WorldServer worldIn, LootTableManager lootTableManagerIn, @Nullable Entity lootedEntityIn, @Nullable EntityPlayer playerIn, @Nullable DamageSource damageSourceIn) {
    this.luck = luckIn;
    this.worldObj = worldIn;
    this.lootTableManager = lootTableManagerIn;
    this.lootedEntity = lootedEntityIn;
    this.player = playerIn;
    this.damageSource = damageSourceIn;
  }
  
  @Nullable
  public Entity getLootedEntity() {
    return this.lootedEntity;
  }
  
  @Nullable
  public Entity getKillerPlayer() {
    return (Entity)this.player;
  }
  
  @Nullable
  public Entity getKiller() {
    return (this.damageSource == null) ? null : this.damageSource.getEntity();
  }
  
  public boolean addLootTable(LootTable lootTableIn) {
    return this.lootTables.add(lootTableIn);
  }
  
  public void removeLootTable(LootTable lootTableIn) {
    this.lootTables.remove(lootTableIn);
  }
  
  public LootTableManager getLootTableManager() {
    return this.lootTableManager;
  }
  
  public float getLuck() {
    return this.luck;
  }
  
  @Nullable
  public Entity getEntity(EntityTarget target) {
    switch (target) {
      case THIS:
        return getLootedEntity();
      case null:
        return getKiller();
      case KILLER_PLAYER:
        return getKillerPlayer();
    } 
    return null;
  }
  
  public static class Builder {
    private final WorldServer worldObj;
    
    private float luck;
    
    private Entity lootedEntity;
    
    private EntityPlayer player;
    
    private DamageSource damageSource;
    
    public Builder(WorldServer worldIn) {
      this.worldObj = worldIn;
    }
    
    public Builder withLuck(float luckIn) {
      this.luck = luckIn;
      return this;
    }
    
    public Builder withLootedEntity(Entity entityIn) {
      this.lootedEntity = entityIn;
      return this;
    }
    
    public Builder withPlayer(EntityPlayer playerIn) {
      this.player = playerIn;
      return this;
    }
    
    public Builder withDamageSource(DamageSource dmgSource) {
      this.damageSource = dmgSource;
      return this;
    }
    
    public LootContext build() {
      return new LootContext(this.luck, this.worldObj, this.worldObj.getLootTableManager(), this.lootedEntity, this.player, this.damageSource);
    }
  }
  
  public enum EntityTarget {
    THIS("this"),
    KILLER("killer"),
    KILLER_PLAYER("killer_player");
    
    private final String targetType;
    
    EntityTarget(String type) {
      this.targetType = type;
    }
    
    public static EntityTarget fromString(String type) {
      byte b;
      int i;
      EntityTarget[] arrayOfEntityTarget;
      for (i = (arrayOfEntityTarget = values()).length, b = 0; b < i; ) {
        EntityTarget lootcontext$entitytarget = arrayOfEntityTarget[b];
        if (lootcontext$entitytarget.targetType.equals(type))
          return lootcontext$entitytarget; 
        b++;
      } 
      throw new IllegalArgumentException("Invalid entity target " + type);
    }
    
    public static class Serializer extends TypeAdapter<EntityTarget> {
      public void write(JsonWriter p_write_1_, LootContext.EntityTarget p_write_2_) throws IOException {
        p_write_1_.value(p_write_2_.targetType);
      }
      
      public LootContext.EntityTarget read(JsonReader p_read_1_) throws IOException {
        return LootContext.EntityTarget.fromString(p_read_1_.nextString());
      }
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\world\storage\loot\LootContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */