package net.minecraft.server.management;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PlayerInteractionManager {
  public World theWorld;
  
  public EntityPlayerMP thisPlayerMP;
  
  private GameType gameType = GameType.NOT_SET;
  
  private boolean isDestroyingBlock;
  
  private int initialDamage;
  
  private BlockPos destroyPos = BlockPos.ORIGIN;
  
  private int curblockDamage;
  
  private boolean receivedFinishDiggingPacket;
  
  private BlockPos delayedDestroyPos = BlockPos.ORIGIN;
  
  private int initialBlockDamage;
  
  private int durabilityRemainingOnBlock = -1;
  
  public PlayerInteractionManager(World worldIn) {
    this.theWorld = worldIn;
  }
  
  public void setGameType(GameType type) {
    this.gameType = type;
    type.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
    this.thisPlayerMP.sendPlayerAbilities();
    this.thisPlayerMP.mcServer.getPlayerList().sendPacketToAllPlayers((Packet<?>)new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.thisPlayerMP }));
    this.theWorld.updateAllPlayersSleepingFlag();
  }
  
  public GameType getGameType() {
    return this.gameType;
  }
  
  public boolean survivalOrAdventure() {
    return this.gameType.isSurvivalOrAdventure();
  }
  
  public boolean isCreative() {
    return this.gameType.isCreative();
  }
  
  public void initializeGameType(GameType type) {
    if (this.gameType == GameType.NOT_SET)
      this.gameType = type; 
    setGameType(this.gameType);
  }
  
  public void updateBlockRemoving() {
    this.curblockDamage++;
    if (this.receivedFinishDiggingPacket) {
      int i = this.curblockDamage - this.initialBlockDamage;
      IBlockState iblockstate = this.theWorld.getBlockState(this.delayedDestroyPos);
      if (iblockstate.getMaterial() == Material.AIR) {
        this.receivedFinishDiggingPacket = false;
      } else {
        float f = iblockstate.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.world, this.delayedDestroyPos) * (i + 1);
        int j = (int)(f * 10.0F);
        if (j != this.durabilityRemainingOnBlock) {
          this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.delayedDestroyPos, j);
          this.durabilityRemainingOnBlock = j;
        } 
        if (f >= 1.0F) {
          this.receivedFinishDiggingPacket = false;
          tryHarvestBlock(this.delayedDestroyPos);
        } 
      } 
    } else if (this.isDestroyingBlock) {
      IBlockState iblockstate1 = this.theWorld.getBlockState(this.destroyPos);
      if (iblockstate1.getMaterial() == Material.AIR) {
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.destroyPos, -1);
        this.durabilityRemainingOnBlock = -1;
        this.isDestroyingBlock = false;
      } else {
        int k = this.curblockDamage - this.initialDamage;
        float f1 = iblockstate1.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.world, this.delayedDestroyPos) * (k + 1);
        int l = (int)(f1 * 10.0F);
        if (l != this.durabilityRemainingOnBlock) {
          this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.destroyPos, l);
          this.durabilityRemainingOnBlock = l;
        } 
      } 
    } 
  }
  
  public void onBlockClicked(BlockPos pos, EnumFacing side) {
    if (isCreative()) {
      if (!this.theWorld.extinguishFire(null, pos, side))
        tryHarvestBlock(pos); 
    } else {
      IBlockState iblockstate = this.theWorld.getBlockState(pos);
      Block block = iblockstate.getBlock();
      if (this.gameType.isAdventure()) {
        if (this.gameType == GameType.SPECTATOR)
          return; 
        if (!this.thisPlayerMP.isAllowEdit()) {
          ItemStack itemstack = this.thisPlayerMP.getHeldItemMainhand();
          if (itemstack.func_190926_b())
            return; 
          if (!itemstack.canDestroy(block))
            return; 
        } 
      } 
      this.theWorld.extinguishFire(null, pos, side);
      this.initialDamage = this.curblockDamage;
      float f = 1.0F;
      if (iblockstate.getMaterial() != Material.AIR) {
        block.onBlockClicked(this.theWorld, pos, (EntityPlayer)this.thisPlayerMP);
        f = iblockstate.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.world, pos);
      } 
      if (iblockstate.getMaterial() != Material.AIR && f >= 1.0F) {
        tryHarvestBlock(pos);
      } else {
        this.isDestroyingBlock = true;
        this.destroyPos = pos;
        int i = (int)(f * 10.0F);
        this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, i);
        this.durabilityRemainingOnBlock = i;
      } 
    } 
  }
  
  public void blockRemoving(BlockPos pos) {
    if (pos.equals(this.destroyPos)) {
      int i = this.curblockDamage - this.initialDamage;
      IBlockState iblockstate = this.theWorld.getBlockState(pos);
      if (iblockstate.getMaterial() != Material.AIR) {
        float f = iblockstate.getPlayerRelativeBlockHardness((EntityPlayer)this.thisPlayerMP, this.thisPlayerMP.world, pos) * (i + 1);
        if (f >= 0.7F) {
          this.isDestroyingBlock = false;
          this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), pos, -1);
          tryHarvestBlock(pos);
        } else if (!this.receivedFinishDiggingPacket) {
          this.isDestroyingBlock = false;
          this.receivedFinishDiggingPacket = true;
          this.delayedDestroyPos = pos;
          this.initialBlockDamage = this.initialDamage;
        } 
      } 
    } 
  }
  
  public void cancelDestroyingBlock() {
    this.isDestroyingBlock = false;
    this.theWorld.sendBlockBreakProgress(this.thisPlayerMP.getEntityId(), this.destroyPos, -1);
  }
  
  private boolean removeBlock(BlockPos pos) {
    IBlockState iblockstate = this.theWorld.getBlockState(pos);
    iblockstate.getBlock().onBlockHarvested(this.theWorld, pos, iblockstate, (EntityPlayer)this.thisPlayerMP);
    boolean flag = this.theWorld.setBlockToAir(pos);
    if (flag)
      iblockstate.getBlock().onBlockDestroyedByPlayer(this.theWorld, pos, iblockstate); 
    return flag;
  }
  
  public boolean tryHarvestBlock(BlockPos pos) {
    if (this.gameType.isCreative() && !this.thisPlayerMP.getHeldItemMainhand().func_190926_b() && this.thisPlayerMP.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword)
      return false; 
    IBlockState iblockstate = this.theWorld.getBlockState(pos);
    TileEntity tileentity = this.theWorld.getTileEntity(pos);
    Block block = iblockstate.getBlock();
    if ((block instanceof net.minecraft.block.BlockCommandBlock || block instanceof net.minecraft.block.BlockStructure) && !this.thisPlayerMP.canUseCommandBlock()) {
      this.theWorld.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
      return false;
    } 
    if (this.gameType.isAdventure()) {
      if (this.gameType == GameType.SPECTATOR)
        return false; 
      if (!this.thisPlayerMP.isAllowEdit()) {
        ItemStack itemstack = this.thisPlayerMP.getHeldItemMainhand();
        if (itemstack.func_190926_b())
          return false; 
        if (!itemstack.canDestroy(block))
          return false; 
      } 
    } 
    this.theWorld.playEvent((EntityPlayer)this.thisPlayerMP, 2001, pos, Block.getStateId(iblockstate));
    boolean flag1 = removeBlock(pos);
    if (isCreative()) {
      this.thisPlayerMP.connection.sendPacket((Packet)new SPacketBlockChange(this.theWorld, pos));
    } else {
      ItemStack itemstack1 = this.thisPlayerMP.getHeldItemMainhand();
      ItemStack itemstack2 = itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1.copy();
      boolean flag = this.thisPlayerMP.canHarvestBlock(iblockstate);
      if (!itemstack1.func_190926_b())
        itemstack1.onBlockDestroyed(this.theWorld, iblockstate, pos, (EntityPlayer)this.thisPlayerMP); 
      if (flag1 && flag)
        iblockstate.getBlock().harvestBlock(this.theWorld, (EntityPlayer)this.thisPlayerMP, pos, iblockstate, tileentity, itemstack2); 
    } 
    return flag1;
  }
  
  public EnumActionResult processRightClick(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand) {
    if (this.gameType == GameType.SPECTATOR)
      return EnumActionResult.PASS; 
    if (player.getCooldownTracker().hasCooldown(stack.getItem()))
      return EnumActionResult.PASS; 
    int i = stack.func_190916_E();
    int j = stack.getMetadata();
    ActionResult<ItemStack> actionresult = stack.useItemRightClick(worldIn, player, hand);
    ItemStack itemstack = (ItemStack)actionresult.getResult();
    if (itemstack == stack && itemstack.func_190916_E() == i && itemstack.getMaxItemUseDuration() <= 0 && itemstack.getMetadata() == j)
      return actionresult.getType(); 
    if (actionresult.getType() == EnumActionResult.FAIL && itemstack.getMaxItemUseDuration() > 0 && !player.isHandActive())
      return actionresult.getType(); 
    player.setHeldItem(hand, itemstack);
    if (isCreative()) {
      itemstack.func_190920_e(i);
      if (itemstack.isItemStackDamageable())
        itemstack.setItemDamage(j); 
    } 
    if (itemstack.func_190926_b())
      player.setHeldItem(hand, ItemStack.field_190927_a); 
    if (!player.isHandActive())
      ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer); 
    return actionresult.getType();
  }
  
  public EnumActionResult processRightClickBlock(EntityPlayer player, World worldIn, ItemStack stack, EnumHand hand, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if (this.gameType == GameType.SPECTATOR) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof ILockableContainer) {
        Block block1 = worldIn.getBlockState(pos).getBlock();
        ILockableContainer ilockablecontainer = (ILockableContainer)tileentity;
        if (ilockablecontainer instanceof net.minecraft.tileentity.TileEntityChest && block1 instanceof BlockChest)
          ilockablecontainer = ((BlockChest)block1).getLockableContainer(worldIn, pos); 
        if (ilockablecontainer != null) {
          player.displayGUIChest((IInventory)ilockablecontainer);
          return EnumActionResult.SUCCESS;
        } 
      } else if (tileentity instanceof IInventory) {
        player.displayGUIChest((IInventory)tileentity);
        return EnumActionResult.SUCCESS;
      } 
      return EnumActionResult.PASS;
    } 
    if (!player.isSneaking() || (player.getHeldItemMainhand().func_190926_b() && player.getHeldItemOffhand().func_190926_b())) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if (iblockstate.getBlock().onBlockActivated(worldIn, pos, iblockstate, player, hand, facing, hitX, hitY, hitZ))
        return EnumActionResult.SUCCESS; 
    } 
    if (stack.func_190926_b())
      return EnumActionResult.PASS; 
    if (player.getCooldownTracker().hasCooldown(stack.getItem()))
      return EnumActionResult.PASS; 
    if (stack.getItem() instanceof ItemBlock && !player.canUseCommandBlock()) {
      Block block = ((ItemBlock)stack.getItem()).getBlock();
      if (block instanceof net.minecraft.block.BlockCommandBlock || block instanceof net.minecraft.block.BlockStructure)
        return EnumActionResult.FAIL; 
    } 
    if (isCreative()) {
      int j = stack.getMetadata();
      int i = stack.func_190916_E();
      EnumActionResult enumactionresult = stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
      stack.setItemDamage(j);
      stack.func_190920_e(i);
      return enumactionresult;
    } 
    return stack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }
  
  public void setWorld(WorldServer serverWorld) {
    this.theWorld = (World)serverWorld;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\server\management\PlayerInteractionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */