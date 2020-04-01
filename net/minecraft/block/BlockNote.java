package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer {
  private static final List<SoundEvent> INSTRUMENTS = Lists.newArrayList((Object[])new SoundEvent[] { SoundEvents.BLOCK_NOTE_HARP, SoundEvents.BLOCK_NOTE_BASEDRUM, SoundEvents.BLOCK_NOTE_SNARE, SoundEvents.BLOCK_NOTE_HAT, SoundEvents.BLOCK_NOTE_BASS, SoundEvents.field_193809_ey, SoundEvents.field_193807_ew, SoundEvents.field_193810_ez, SoundEvents.field_193808_ex, SoundEvents.field_193785_eE });
  
  public BlockNote() {
    super(Material.WOOD);
    setCreativeTab(CreativeTabs.REDSTONE);
  }
  
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
    boolean flag = worldIn.isBlockPowered(pos);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityNote) {
      TileEntityNote tileentitynote = (TileEntityNote)tileentity;
      if (tileentitynote.previousRedstoneState != flag) {
        if (flag)
          tileentitynote.triggerNote(worldIn, pos); 
        tileentitynote.previousRedstoneState = flag;
      } 
    } 
  }
  
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
    if (worldIn.isRemote)
      return true; 
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof TileEntityNote) {
      TileEntityNote tileentitynote = (TileEntityNote)tileentity;
      tileentitynote.changePitch();
      tileentitynote.triggerNote(worldIn, pos);
      playerIn.addStat(StatList.NOTEBLOCK_TUNED);
    } 
    return true;
  }
  
  public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
    if (!worldIn.isRemote) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof TileEntityNote) {
        ((TileEntityNote)tileentity).triggerNote(worldIn, pos);
        playerIn.addStat(StatList.NOTEBLOCK_PLAYED);
      } 
    } 
  }
  
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return (TileEntity)new TileEntityNote();
  }
  
  private SoundEvent getInstrument(int p_185576_1_) {
    if (p_185576_1_ < 0 || p_185576_1_ >= INSTRUMENTS.size())
      p_185576_1_ = 0; 
    return INSTRUMENTS.get(p_185576_1_);
  }
  
  public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
    float f = (float)Math.pow(2.0D, (param - 12) / 12.0D);
    worldIn.playSound(null, pos, getInstrument(id), SoundCategory.RECORDS, 3.0F, f);
    worldIn.spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, param / 24.0D, 0.0D, 0.0D, new int[0]);
    return true;
  }
  
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\block\BlockNote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */