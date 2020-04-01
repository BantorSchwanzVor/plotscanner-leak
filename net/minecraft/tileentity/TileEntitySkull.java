package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import javax.annotation.Nullable;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.properties.IProperty;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity implements ITickable {
  private int skullType;
  
  private int skullRotation;
  
  private GameProfile playerProfile;
  
  private int dragonAnimatedTicks;
  
  private boolean dragonAnimated;
  
  private static PlayerProfileCache profileCache;
  
  private static MinecraftSessionService sessionService;
  
  public static void setProfileCache(PlayerProfileCache profileCacheIn) {
    profileCache = profileCacheIn;
  }
  
  public static void setSessionService(MinecraftSessionService sessionServiceIn) {
    sessionService = sessionServiceIn;
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setByte("SkullType", (byte)(this.skullType & 0xFF));
    compound.setByte("Rot", (byte)(this.skullRotation & 0xFF));
    if (this.playerProfile != null) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
      compound.setTag("Owner", (NBTBase)nbttagcompound);
    } 
    return compound;
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.skullType = compound.getByte("SkullType");
    this.skullRotation = compound.getByte("Rot");
    if (this.skullType == 3)
      if (compound.hasKey("Owner", 10)) {
        this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
      } else if (compound.hasKey("ExtraType", 8)) {
        String s = compound.getString("ExtraType");
        if (!StringUtils.isNullOrEmpty(s)) {
          this.playerProfile = new GameProfile(null, s);
          updatePlayerProfile();
        } 
      }  
  }
  
  public void update() {
    if (this.skullType == 5)
      if (this.world.isBlockPowered(this.pos)) {
        this.dragonAnimated = true;
        this.dragonAnimatedTicks++;
      } else {
        this.dragonAnimated = false;
      }  
  }
  
  public float getAnimationProgress(float p_184295_1_) {
    return this.dragonAnimated ? (this.dragonAnimatedTicks + p_184295_1_) : this.dragonAnimatedTicks;
  }
  
  @Nullable
  public GameProfile getPlayerProfile() {
    return this.playerProfile;
  }
  
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 4, getUpdateTag());
  }
  
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }
  
  public void setType(int type) {
    this.skullType = type;
    this.playerProfile = null;
  }
  
  public void setPlayerProfile(@Nullable GameProfile playerProfile) {
    this.skullType = 3;
    this.playerProfile = playerProfile;
    updatePlayerProfile();
  }
  
  private void updatePlayerProfile() {
    this.playerProfile = updateGameprofile(this.playerProfile);
    markDirty();
  }
  
  public static GameProfile updateGameprofile(GameProfile input) {
    if (input != null && !StringUtils.isNullOrEmpty(input.getName())) {
      if (input.isComplete() && input.getProperties().containsKey("textures"))
        return input; 
      if (profileCache != null && sessionService != null) {
        GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());
        if (gameprofile == null)
          return input; 
        Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
        if (property == null)
          gameprofile = sessionService.fillProfileProperties(gameprofile, true); 
        return gameprofile;
      } 
      return input;
    } 
    return input;
  }
  
  public int getSkullType() {
    return this.skullType;
  }
  
  public int getSkullRotation() {
    return this.skullRotation;
  }
  
  public void setSkullRotation(int rotation) {
    this.skullRotation = rotation;
  }
  
  public void mirror(Mirror p_189668_1_) {
    if (this.world != null && this.world.getBlockState(getPos()).getValue((IProperty)BlockSkull.FACING) == EnumFacing.UP)
      this.skullRotation = p_189668_1_.mirrorRotation(this.skullRotation, 16); 
  }
  
  public void rotate(Rotation p_189667_1_) {
    if (this.world != null && this.world.getBlockState(getPos()).getValue((IProperty)BlockSkull.FACING) == EnumFacing.UP)
      this.skullRotation = p_189667_1_.rotate(this.skullRotation, 16); 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntitySkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */