package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
  public TileEntity() {
    this.pos = BlockPos.ORIGIN;
    this.blockMetadata = -1;
  }
  
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> field_190562_f = new RegistryNamespaced();
  
  protected World world;
  
  protected BlockPos pos;
  
  protected boolean tileEntityInvalid;
  
  private int blockMetadata;
  
  protected Block blockType;
  
  private static void func_190560_a(String p_190560_0_, Class<? extends TileEntity> p_190560_1_) {
    field_190562_f.putObject(new ResourceLocation(p_190560_0_), p_190560_1_);
  }
  
  @Nullable
  public static ResourceLocation func_190559_a(Class<? extends TileEntity> p_190559_0_) {
    return (ResourceLocation)field_190562_f.getNameForObject(p_190559_0_);
  }
  
  public World getWorld() {
    return this.world;
  }
  
  public void setWorldObj(World worldIn) {
    this.world = worldIn;
  }
  
  public boolean hasWorldObj() {
    return (this.world != null);
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    return writeInternal(compound);
  }
  
  private NBTTagCompound writeInternal(NBTTagCompound compound) {
    ResourceLocation resourcelocation = (ResourceLocation)field_190562_f.getNameForObject(getClass());
    if (resourcelocation == null)
      throw new RuntimeException(getClass() + " is missing a mapping! This is a bug!"); 
    compound.setString("id", resourcelocation.toString());
    compound.setInteger("x", this.pos.getX());
    compound.setInteger("y", this.pos.getY());
    compound.setInteger("z", this.pos.getZ());
    return compound;
  }
  
  @Nullable
  public static TileEntity create(World worldIn, NBTTagCompound compound) {
    TileEntity tileentity = null;
    String s = compound.getString("id");
    try {
      Class<? extends TileEntity> oclass = (Class<? extends TileEntity>)field_190562_f.getObject(new ResourceLocation(s));
      if (oclass != null)
        tileentity = oclass.newInstance(); 
    } catch (Throwable throwable1) {
      LOGGER.error("Failed to create block entity {}", s, throwable1);
    } 
    if (tileentity != null) {
      try {
        tileentity.setWorldCreate(worldIn);
        tileentity.readFromNBT(compound);
      } catch (Throwable throwable) {
        LOGGER.error("Failed to load data for block entity {}", s, throwable);
        tileentity = null;
      } 
    } else {
      LOGGER.warn("Skipping BlockEntity with id {}", s);
    } 
    return tileentity;
  }
  
  protected void setWorldCreate(World worldIn) {}
  
  public int getBlockMetadata() {
    if (this.blockMetadata == -1) {
      IBlockState iblockstate = this.world.getBlockState(this.pos);
      this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
    } 
    return this.blockMetadata;
  }
  
  public void markDirty() {
    if (this.world != null) {
      IBlockState iblockstate = this.world.getBlockState(this.pos);
      this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
      this.world.markChunkDirty(this.pos, this);
      if (getBlockType() != Blocks.AIR)
        this.world.updateComparatorOutputLevel(this.pos, getBlockType()); 
    } 
  }
  
  public double getDistanceSq(double x, double y, double z) {
    double d0 = this.pos.getX() + 0.5D - x;
    double d1 = this.pos.getY() + 0.5D - y;
    double d2 = this.pos.getZ() + 0.5D - z;
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  public double getMaxRenderDistanceSquared() {
    return 4096.0D;
  }
  
  public BlockPos getPos() {
    return this.pos;
  }
  
  public Block getBlockType() {
    if (this.blockType == null && this.world != null)
      this.blockType = this.world.getBlockState(this.pos).getBlock(); 
    return this.blockType;
  }
  
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return null;
  }
  
  public NBTTagCompound getUpdateTag() {
    return writeInternal(new NBTTagCompound());
  }
  
  public boolean isInvalid() {
    return this.tileEntityInvalid;
  }
  
  public void invalidate() {
    this.tileEntityInvalid = true;
  }
  
  public void validate() {
    this.tileEntityInvalid = false;
  }
  
  public boolean receiveClientEvent(int id, int type) {
    return false;
  }
  
  public void updateContainingBlockInfo() {
    this.blockType = null;
    this.blockMetadata = -1;
  }
  
  public void addInfoToCrashReport(CrashReportCategory reportCategory) {
    reportCategory.setDetail("Name", new ICrashReportDetail<String>() {
          public String call() throws Exception {
            return TileEntity.field_190562_f.getNameForObject(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
          }
        });
    if (this.world != null) {
      CrashReportCategory.addBlockInfo(reportCategory, this.pos, getBlockType(), getBlockMetadata());
      reportCategory.setDetail("Actual block type", new ICrashReportDetail<String>() {
            public String call() throws Exception {
              int i = Block.getIdFromBlock(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());
              try {
                return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName() });
              } catch (Throwable var3) {
                return "ID #" + i;
              } 
            }
          });
      reportCategory.setDetail("Actual block data value", new ICrashReportDetail<String>() {
            public String call() throws Exception {
              IBlockState iblockstate = TileEntity.this.world.getBlockState(TileEntity.this.pos);
              int i = iblockstate.getBlock().getMetaFromState(iblockstate);
              if (i < 0)
                return "Unknown? (Got " + i + ")"; 
              String s = String.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
              return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });
            }
          });
    } 
  }
  
  public void setPos(BlockPos posIn) {
    this.pos = posIn.toImmutable();
  }
  
  public boolean onlyOpsCanSetNbt() {
    return false;
  }
  
  @Nullable
  public ITextComponent getDisplayName() {
    return null;
  }
  
  public void rotate(Rotation p_189667_1_) {}
  
  public void mirror(Mirror p_189668_1_) {}
  
  static {
    func_190560_a("furnace", (Class)TileEntityFurnace.class);
    func_190560_a("chest", (Class)TileEntityChest.class);
    func_190560_a("ender_chest", (Class)TileEntityEnderChest.class);
    func_190560_a("jukebox", (Class)BlockJukebox.TileEntityJukebox.class);
    func_190560_a("dispenser", (Class)TileEntityDispenser.class);
    func_190560_a("dropper", (Class)TileEntityDropper.class);
    func_190560_a("sign", (Class)TileEntitySign.class);
    func_190560_a("mob_spawner", (Class)TileEntityMobSpawner.class);
    func_190560_a("noteblock", (Class)TileEntityNote.class);
    func_190560_a("piston", (Class)TileEntityPiston.class);
    func_190560_a("brewing_stand", (Class)TileEntityBrewingStand.class);
    func_190560_a("enchanting_table", (Class)TileEntityEnchantmentTable.class);
    func_190560_a("end_portal", (Class)TileEntityEndPortal.class);
    func_190560_a("beacon", (Class)TileEntityBeacon.class);
    func_190560_a("skull", (Class)TileEntitySkull.class);
    func_190560_a("daylight_detector", (Class)TileEntityDaylightDetector.class);
    func_190560_a("hopper", (Class)TileEntityHopper.class);
    func_190560_a("comparator", (Class)TileEntityComparator.class);
    func_190560_a("flower_pot", (Class)TileEntityFlowerPot.class);
    func_190560_a("banner", (Class)TileEntityBanner.class);
    func_190560_a("structure_block", (Class)TileEntityStructure.class);
    func_190560_a("end_gateway", (Class)TileEntityEndGateway.class);
    func_190560_a("command_block", (Class)TileEntityCommandBlock.class);
    func_190560_a("shulker_box", (Class)TileEntityShulkerBox.class);
    func_190560_a("bed", (Class)TileEntityBed.class);
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */