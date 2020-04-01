package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity {
  private IBlockState fallTile;
  
  public int fallTime;
  
  public boolean shouldDropItem = true;
  
  private boolean canSetAsBlock;
  
  private boolean hurtEntities;
  
  private int fallHurtMax = 40;
  
  private float fallHurtAmount = 2.0F;
  
  public NBTTagCompound tileEntityData;
  
  protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);
  
  public EntityFallingBlock(World worldIn) {
    super(worldIn);
  }
  
  public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
    super(worldIn);
    this.fallTile = fallingBlockState;
    this.preventEntitySpawning = true;
    setSize(0.98F, 0.98F);
    setPosition(x, y + ((1.0F - this.height) / 2.0F), z);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
    setOrigin(new BlockPos(this));
  }
  
  public boolean canBeAttackedWithItem() {
    return false;
  }
  
  public void setOrigin(BlockPos p_184530_1_) {
    this.dataManager.set(ORIGIN, p_184530_1_);
  }
  
  public BlockPos getOrigin() {
    return (BlockPos)this.dataManager.get(ORIGIN);
  }
  
  protected boolean canTriggerWalking() {
    return false;
  }
  
  protected void entityInit() {
    this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
  }
  
  public boolean canBeCollidedWith() {
    return !this.isDead;
  }
  
  public void onUpdate() {
    Block block = this.fallTile.getBlock();
    if (this.fallTile.getMaterial() == Material.AIR) {
      setDead();
    } else {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.fallTime++ == 0) {
        BlockPos blockpos = new BlockPos(this);
        if (this.world.getBlockState(blockpos).getBlock() == block) {
          this.world.setBlockToAir(blockpos);
        } else if (!this.world.isRemote) {
          setDead();
          return;
        } 
      } 
      if (!hasNoGravity())
        this.motionY -= 0.03999999910593033D; 
      moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
      if (!this.world.isRemote) {
        BlockPos blockpos1 = new BlockPos(this);
        boolean flag = (this.fallTile.getBlock() == Blocks.field_192444_dS);
        boolean flag1 = (flag && this.world.getBlockState(blockpos1).getMaterial() == Material.WATER);
        double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
        if (flag && d0 > 1.0D) {
          RayTraceResult raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);
          if (raytraceresult != null && this.world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER) {
            blockpos1 = raytraceresult.getBlockPos();
            flag1 = true;
          } 
        } 
        if (!this.onGround && !flag1) {
          if ((this.fallTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256)) || this.fallTime > 600) {
            if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops"))
              entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F); 
            setDead();
          } 
        } else {
          IBlockState iblockstate = this.world.getBlockState(blockpos1);
          if (!flag1 && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)))) {
            this.onGround = false;
            return;
          } 
          this.motionX *= 0.699999988079071D;
          this.motionZ *= 0.699999988079071D;
          this.motionY *= -0.5D;
          if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {
            setDead();
            if (!this.canSetAsBlock) {
              if (this.world.func_190527_a(block, blockpos1, true, EnumFacing.UP, null) && (flag1 || !BlockFalling.canFallThrough(this.world.getBlockState(blockpos1.down()))) && this.world.setBlockState(blockpos1, this.fallTile, 3)) {
                if (block instanceof BlockFalling)
                  ((BlockFalling)block).onEndFalling(this.world, blockpos1, this.fallTile, iblockstate); 
                if (this.tileEntityData != null && block instanceof net.minecraft.block.ITileEntityProvider) {
                  TileEntity tileentity = this.world.getTileEntity(blockpos1);
                  if (tileentity != null) {
                    NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                    for (String s : this.tileEntityData.getKeySet()) {
                      NBTBase nbtbase = this.tileEntityData.getTag(s);
                      if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s))
                        nbttagcompound.setTag(s, nbtbase.copy()); 
                    } 
                    tileentity.readFromNBT(nbttagcompound);
                    tileentity.markDirty();
                  } 
                } 
              } else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0F);
              } 
            } else if (block instanceof BlockFalling) {
              ((BlockFalling)block).func_190974_b(this.world, blockpos1);
            } 
          } 
        } 
      } 
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.9800000190734863D;
      this.motionZ *= 0.9800000190734863D;
    } 
  }
  
  public void fall(float distance, float damageMultiplier) {
    Block block = this.fallTile.getBlock();
    if (this.hurtEntities) {
      int i = MathHelper.ceil(distance - 1.0F);
      if (i > 0) {
        List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox()));
        boolean flag = (block == Blocks.ANVIL);
        DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
        for (Entity entity : list)
          entity.attackEntityFrom(damagesource, Math.min(MathHelper.floor(i * this.fallHurtAmount), this.fallHurtMax)); 
        if (flag && this.rand.nextFloat() < 0.05000000074505806D + i * 0.05D) {
          int j = ((Integer)this.fallTile.getValue((IProperty)BlockAnvil.DAMAGE)).intValue();
          j++;
          if (j > 2) {
            this.canSetAsBlock = true;
          } else {
            this.fallTile = this.fallTile.withProperty((IProperty)BlockAnvil.DAMAGE, Integer.valueOf(j));
          } 
        } 
      } 
    } 
  }
  
  public static void registerFixesFallingBlock(DataFixer fixer) {}
  
  protected void writeEntityToNBT(NBTTagCompound compound) {
    Block block = (this.fallTile != null) ? this.fallTile.getBlock() : Blocks.AIR;
    ResourceLocation resourcelocation = (ResourceLocation)Block.REGISTRY.getNameForObject(block);
    compound.setString("Block", (resourcelocation == null) ? "" : resourcelocation.toString());
    compound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
    compound.setInteger("Time", this.fallTime);
    compound.setBoolean("DropItem", this.shouldDropItem);
    compound.setBoolean("HurtEntities", this.hurtEntities);
    compound.setFloat("FallHurtAmount", this.fallHurtAmount);
    compound.setInteger("FallHurtMax", this.fallHurtMax);
    if (this.tileEntityData != null)
      compound.setTag("TileEntityData", (NBTBase)this.tileEntityData); 
  }
  
  protected void readEntityFromNBT(NBTTagCompound compound) {
    int i = compound.getByte("Data") & 0xFF;
    if (compound.hasKey("Block", 8)) {
      this.fallTile = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
    } else if (compound.hasKey("TileID", 99)) {
      this.fallTile = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
    } else {
      this.fallTile = Block.getBlockById(compound.getByte("Tile") & 0xFF).getStateFromMeta(i);
    } 
    this.fallTime = compound.getInteger("Time");
    Block block = this.fallTile.getBlock();
    if (compound.hasKey("HurtEntities", 99)) {
      this.hurtEntities = compound.getBoolean("HurtEntities");
      this.fallHurtAmount = compound.getFloat("FallHurtAmount");
      this.fallHurtMax = compound.getInteger("FallHurtMax");
    } else if (block == Blocks.ANVIL) {
      this.hurtEntities = true;
    } 
    if (compound.hasKey("DropItem", 99))
      this.shouldDropItem = compound.getBoolean("DropItem"); 
    if (compound.hasKey("TileEntityData", 10))
      this.tileEntityData = compound.getCompoundTag("TileEntityData"); 
    if (block == null || block.getDefaultState().getMaterial() == Material.AIR)
      this.fallTile = Blocks.SAND.getDefaultState(); 
  }
  
  public World getWorldObj() {
    return this.world;
  }
  
  public void setHurtEntities(boolean p_145806_1_) {
    this.hurtEntities = p_145806_1_;
  }
  
  public boolean canRenderOnFire() {
    return false;
  }
  
  public void addEntityCrashInfo(CrashReportCategory category) {
    super.addEntityCrashInfo(category);
    if (this.fallTile != null) {
      Block block = this.fallTile.getBlock();
      category.addCrashSection("Immitating block ID", Integer.valueOf(Block.getIdFromBlock(block)));
      category.addCrashSection("Immitating block data", Integer.valueOf(block.getMetaFromState(this.fallTile)));
    } 
  }
  
  @Nullable
  public IBlockState getBlock() {
    return this.fallTile;
  }
  
  public boolean ignoreItemEntityData() {
    return true;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\item\EntityFallingBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */