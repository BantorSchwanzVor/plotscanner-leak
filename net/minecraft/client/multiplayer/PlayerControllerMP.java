package net.minecraft.client.multiplayer;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import org.seltak.anubis.Anubis;

public class PlayerControllerMP {
  private final Minecraft mc;
  
  private final NetHandlerPlayClient connection;
  
  private BlockPos currentBlock = new BlockPos(-1, -1, -1);
  
  private ItemStack currentItemHittingBlock = ItemStack.field_190927_a;
  
  private float curBlockDamageMP;
  
  private float stepSoundTickCounter;
  
  private int blockHitDelay;
  
  private boolean isHittingBlock;
  
  private GameType currentGameType = GameType.SURVIVAL;
  
  private int currentPlayerItem;
  
  public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient netHandler) {
    this.mc = mcIn;
    this.connection = netHandler;
  }
  
  public static void clickBlockCreative(Minecraft mcIn, PlayerControllerMP playerController, BlockPos pos, EnumFacing facing) {
    if (!mcIn.world.extinguishFire((EntityPlayer)mcIn.player, pos, facing))
      playerController.onPlayerDestroyBlock(pos); 
  }
  
  public void setPlayerCapabilities(EntityPlayer player) {
    this.currentGameType.configurePlayerCapabilities(player.capabilities);
  }
  
  public boolean isSpectator() {
    return (this.currentGameType == GameType.SPECTATOR);
  }
  
  public void setGameType(GameType type) {
    this.currentGameType = type;
    this.currentGameType.configurePlayerCapabilities(this.mc.player.capabilities);
  }
  
  public void flipPlayer(EntityPlayer playerIn) {
    playerIn.rotationYaw = -180.0F;
  }
  
  public boolean shouldDrawHUD() {
    return this.currentGameType.isSurvivalOrAdventure();
  }
  
  public boolean onPlayerDestroyBlock(BlockPos pos) {
    if (this.currentGameType.isAdventure()) {
      if (this.currentGameType == GameType.SPECTATOR)
        return false; 
      if (!this.mc.player.isAllowEdit()) {
        ItemStack itemstack = this.mc.player.getHeldItemMainhand();
        if (itemstack.func_190926_b())
          return false; 
        if (!itemstack.canDestroy(this.mc.world.getBlockState(pos).getBlock()))
          return false; 
      } 
    } 
    if (this.currentGameType.isCreative() && !this.mc.player.getHeldItemMainhand().func_190926_b() && this.mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword)
      return false; 
    World world = this.mc.world;
    IBlockState iblockstate = world.getBlockState(pos);
    Block block = iblockstate.getBlock();
    if ((block instanceof net.minecraft.block.BlockCommandBlock || block instanceof net.minecraft.block.BlockStructure) && !this.mc.player.canUseCommandBlock())
      return false; 
    if (iblockstate.getMaterial() == Material.AIR)
      return false; 
    world.playEvent(2001, pos, Block.getStateId(iblockstate));
    block.onBlockHarvested(world, pos, iblockstate, (EntityPlayer)this.mc.player);
    boolean flag = world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
    if (flag)
      block.onBlockDestroyedByPlayer(world, pos, iblockstate); 
    this.currentBlock = new BlockPos(this.currentBlock.getX(), -1, this.currentBlock.getZ());
    if (!this.currentGameType.isCreative()) {
      ItemStack itemstack1 = this.mc.player.getHeldItemMainhand();
      if (!itemstack1.func_190926_b()) {
        itemstack1.onBlockDestroyed(world, iblockstate, pos, (EntityPlayer)this.mc.player);
        if (itemstack1.func_190926_b())
          this.mc.player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.field_190927_a); 
      } 
    } 
    return flag;
  }
  
  public boolean clickBlock(BlockPos loc, EnumFacing face) {
    if (this.currentGameType.isAdventure()) {
      if (this.currentGameType == GameType.SPECTATOR)
        return false; 
      if (!this.mc.player.isAllowEdit()) {
        ItemStack itemstack = this.mc.player.getHeldItemMainhand();
        if (itemstack.func_190926_b())
          return false; 
        if (!itemstack.canDestroy(this.mc.world.getBlockState(loc).getBlock()))
          return false; 
      } 
    } 
    if (!this.mc.world.getWorldBorder().contains(loc))
      return false; 
    if (this.currentGameType.isCreative()) {
      this.mc.func_193032_ao().func_193294_a(this.mc.world, loc, this.mc.world.getBlockState(loc), 1.0F);
      this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
      clickBlockCreative(this.mc, this, loc, face);
      this.blockHitDelay = 5;
    } else if (!this.isHittingBlock || !isHittingPosition(loc)) {
      if (this.isHittingBlock)
        this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, face)); 
      IBlockState iblockstate = this.mc.world.getBlockState(loc);
      this.mc.func_193032_ao().func_193294_a(this.mc.world, loc, iblockstate, 0.0F);
      this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, loc, face));
      boolean flag = (iblockstate.getMaterial() != Material.AIR);
      if (flag && this.curBlockDamageMP == 0.0F)
        iblockstate.getBlock().onBlockClicked(this.mc.world, loc, (EntityPlayer)this.mc.player); 
      if (flag && iblockstate.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.player, this.mc.player.world, loc) >= 1.0F) {
        onPlayerDestroyBlock(loc);
      } else {
        this.isHittingBlock = true;
        this.currentBlock = loc;
        this.currentItemHittingBlock = this.mc.player.getHeldItemMainhand();
        this.curBlockDamageMP = 0.0F;
        this.stepSoundTickCounter = 0.0F;
        this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
      } 
    } 
    return true;
  }
  
  public void resetBlockRemoving() {
    if (this.isHittingBlock) {
      this.mc.func_193032_ao().func_193294_a(this.mc.world, this.currentBlock, this.mc.world.getBlockState(this.currentBlock), -1.0F);
      this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.currentBlock, EnumFacing.DOWN));
      this.isHittingBlock = false;
      this.curBlockDamageMP = 0.0F;
      this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityId(), this.currentBlock, -1);
      this.mc.player.resetCooldown();
    } 
  }
  
  public boolean onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing) {
    syncCurrentPlayItem();
    if (this.blockHitDelay > 0) {
      this.blockHitDelay--;
      return true;
    } 
    if (this.currentGameType.isCreative() && this.mc.world.getWorldBorder().contains(posBlock)) {
      this.blockHitDelay = 5;
      this.mc.func_193032_ao().func_193294_a(this.mc.world, posBlock, this.mc.world.getBlockState(posBlock), 1.0F);
      this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, posBlock, directionFacing));
      clickBlockCreative(this.mc, this, posBlock, directionFacing);
      return true;
    } 
    if (isHittingPosition(posBlock)) {
      IBlockState iblockstate = this.mc.world.getBlockState(posBlock);
      Block block = iblockstate.getBlock();
      if (iblockstate.getMaterial() == Material.AIR) {
        this.isHittingBlock = false;
        return false;
      } 
      this.curBlockDamageMP += iblockstate.getPlayerRelativeBlockHardness((EntityPlayer)this.mc.player, this.mc.player.world, posBlock);
      if (this.stepSoundTickCounter % 4.0F == 0.0F) {
        SoundType soundtype = block.getSoundType();
        this.mc.getSoundHandler().playSound((ISound)new PositionedSoundRecord(soundtype.getHitSound(), SoundCategory.NEUTRAL, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, posBlock));
      } 
      this.stepSoundTickCounter++;
      this.mc.func_193032_ao().func_193294_a(this.mc.world, posBlock, iblockstate, MathHelper.clamp(this.curBlockDamageMP, 0.0F, 1.0F));
      if (this.curBlockDamageMP >= 1.0F) {
        this.isHittingBlock = false;
        this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
        onPlayerDestroyBlock(posBlock);
        this.curBlockDamageMP = 0.0F;
        this.stepSoundTickCounter = 0.0F;
        this.blockHitDelay = 5;
      } 
      this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
      return true;
    } 
    return clickBlock(posBlock, directionFacing);
  }
  
  public float getBlockReachDistance() {
    return this.currentGameType.isCreative() ? 5.0F : 4.5F;
  }
  
  public void updateController() {
    syncCurrentPlayItem();
    if (this.connection.getNetworkManager().isChannelOpen()) {
      this.connection.getNetworkManager().processReceivedPackets();
    } else {
      this.connection.getNetworkManager().checkDisconnected();
    } 
  }
  
  private boolean isHittingPosition(BlockPos pos) {
    ItemStack itemstack = this.mc.player.getHeldItemMainhand();
    boolean flag = (this.currentItemHittingBlock.func_190926_b() && itemstack.func_190926_b());
    if (!this.currentItemHittingBlock.func_190926_b() && !itemstack.func_190926_b())
      flag = (itemstack.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.currentItemHittingBlock) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.currentItemHittingBlock.getMetadata())); 
    return (pos.equals(this.currentBlock) && flag);
  }
  
  private void syncCurrentPlayItem() {
    int i = this.mc.player.inventory.currentItem;
    if (i != this.currentPlayerItem) {
      this.currentPlayerItem = i;
      this.connection.sendPacket((Packet)new CPacketHeldItemChange(this.currentPlayerItem));
    } 
  }
  
  public EnumActionResult processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos stack, EnumFacing pos, Vec3d facing, EnumHand vec) {
    syncCurrentPlayItem();
    ItemStack itemstack = player.getHeldItem(vec);
    float f = (float)(facing.xCoord - stack.getX());
    float f1 = (float)(facing.yCoord - stack.getY());
    float f2 = (float)(facing.zCoord - stack.getZ());
    boolean flag = false;
    if (!this.mc.world.getWorldBorder().contains(stack))
      return EnumActionResult.FAIL; 
    if (this.currentGameType != GameType.SPECTATOR) {
      IBlockState iblockstate = worldIn.getBlockState(stack);
      if ((!player.isSneaking() || (player.getHeldItemMainhand().func_190926_b() && player.getHeldItemOffhand().func_190926_b())) && iblockstate.getBlock().onBlockActivated(worldIn, stack, iblockstate, (EntityPlayer)player, vec, pos, f, f1, f2))
        flag = true; 
      if (!flag && itemstack.getItem() instanceof ItemBlock) {
        ItemBlock itemblock = (ItemBlock)itemstack.getItem();
        if (!itemblock.canPlaceBlockOnSide(worldIn, stack, pos, (EntityPlayer)player, itemstack))
          return EnumActionResult.FAIL; 
      } 
    } 
    this.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(stack, pos, vec, f, f1, f2));
    if (!flag && this.currentGameType != GameType.SPECTATOR) {
      if (itemstack.func_190926_b())
        return EnumActionResult.PASS; 
      if (player.getCooldownTracker().hasCooldown(itemstack.getItem()))
        return EnumActionResult.PASS; 
      if (itemstack.getItem() instanceof ItemBlock && !player.canUseCommandBlock()) {
        Block block = ((ItemBlock)itemstack.getItem()).getBlock();
        if (block instanceof net.minecraft.block.BlockCommandBlock || block instanceof net.minecraft.block.BlockStructure)
          return EnumActionResult.FAIL; 
      } 
      if (this.currentGameType.isCreative()) {
        int i = itemstack.getMetadata();
        int j = itemstack.func_190916_E();
        EnumActionResult enumactionresult = itemstack.onItemUse((EntityPlayer)player, worldIn, stack, vec, pos, f, f1, f2);
        itemstack.setItemDamage(i);
        itemstack.func_190920_e(j);
        return enumactionresult;
      } 
      return itemstack.onItemUse((EntityPlayer)player, worldIn, stack, vec, pos, f, f1, f2);
    } 
    return EnumActionResult.SUCCESS;
  }
  
  public EnumActionResult processRightClick(EntityPlayer player, World worldIn, EnumHand stack) {
    if (this.currentGameType == GameType.SPECTATOR)
      return EnumActionResult.PASS; 
    syncCurrentPlayItem();
    this.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(stack));
    ItemStack itemstack = player.getHeldItem(stack);
    if (player.getCooldownTracker().hasCooldown(itemstack.getItem()))
      return EnumActionResult.PASS; 
    int i = itemstack.func_190916_E();
    ActionResult<ItemStack> actionresult = itemstack.useItemRightClick(worldIn, player, stack);
    ItemStack itemstack1 = (ItemStack)actionresult.getResult();
    if (itemstack1 != itemstack || itemstack1.func_190916_E() != i)
      player.setHeldItem(stack, itemstack1); 
    return actionresult.getType();
  }
  
  public EntityPlayerSP func_192830_a(World p_192830_1_, StatisticsManager p_192830_2_, RecipeBook p_192830_3_) {
    return new EntityPlayerSP(this.mc, p_192830_1_, this.connection, p_192830_2_, p_192830_3_);
  }
  
  public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
    syncCurrentPlayItem();
    this.connection.sendPacket((Packet)new CPacketUseEntity(targetEntity));
    if (this.currentGameType != GameType.SPECTATOR) {
      playerIn.attackTargetEntityWithCurrentItem(targetEntity);
      playerIn.resetCooldown();
    } 
  }
  
  public EnumActionResult interactWithEntity(EntityPlayer player, Entity target, EnumHand heldItem) {
    syncCurrentPlayItem();
    this.connection.sendPacket((Packet)new CPacketUseEntity(target, heldItem));
    return (this.currentGameType == GameType.SPECTATOR) ? EnumActionResult.PASS : player.func_190775_a(target, heldItem);
  }
  
  public EnumActionResult interactWithEntity(EntityPlayer player, Entity target, RayTraceResult raytrace, EnumHand heldItem) {
    syncCurrentPlayItem();
    Vec3d vec3d = new Vec3d(raytrace.hitVec.xCoord - target.posX, raytrace.hitVec.yCoord - target.posY, raytrace.hitVec.zCoord - target.posZ);
    this.connection.sendPacket((Packet)new CPacketUseEntity(target, heldItem, vec3d));
    return (this.currentGameType == GameType.SPECTATOR) ? EnumActionResult.PASS : target.applyPlayerInteraction(player, vec3d, heldItem);
  }
  
  public ItemStack windowClick(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player) {
    short short1 = player.openContainer.getNextTransactionID(player.inventory);
    ItemStack itemstack = player.openContainer.slotClick(slotId, mouseButton, type, player);
    this.connection.sendPacket((Packet)new CPacketClickWindow(windowId, slotId, mouseButton, type, itemstack, short1));
    return itemstack;
  }
  
  public void func_194338_a(int p_194338_1_, IRecipe p_194338_2_, boolean p_194338_3_, EntityPlayer p_194338_4_) {
    this.connection.sendPacket((Packet)new CPacketPlaceRecipe(p_194338_1_, p_194338_2_, p_194338_3_));
  }
  
  public void sendEnchantPacket(int windowID, int button) {
    this.connection.sendPacket((Packet)new CPacketEnchantItem(windowID, button));
  }
  
  public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
    if (this.currentGameType.isCreative())
      this.connection.sendPacket((Packet)new CPacketCreativeInventoryAction(slotId, itemStackIn)); 
  }
  
  public void sendPacketDropItem(ItemStack itemStackIn) {
    if (this.currentGameType.isCreative() && !itemStackIn.func_190926_b())
      this.connection.sendPacket((Packet)new CPacketCreativeInventoryAction(-1, itemStackIn)); 
  }
  
  public void onStoppedUsingItem(EntityPlayer playerIn) {
    syncCurrentPlayItem();
    this.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    playerIn.stopActiveHand();
  }
  
  public boolean gameIsSurvivalOrAdventure() {
    return this.currentGameType.isSurvivalOrAdventure();
  }
  
  public boolean isNotCreative() {
    return !this.currentGameType.isCreative();
  }
  
  public boolean isInCreativeMode() {
    return this.currentGameType.isCreative();
  }
  
  public boolean extendedReach() {
    return !(!this.currentGameType.isCreative() && (!Anubis.moduleManager.getModuleByName("KillAura") || !Anubis.setmgr.getSettingByName("Reach").getValBoolean()));
  }
  
  public boolean isRidingHorse() {
    return (this.mc.player.isRiding() && this.mc.player.getRidingEntity() instanceof net.minecraft.entity.passive.AbstractHorse);
  }
  
  public boolean isSpectatorMode() {
    return (this.currentGameType == GameType.SPECTATOR);
  }
  
  public GameType getCurrentGameType() {
    return this.currentGameType;
  }
  
  public boolean getIsHittingBlock() {
    return this.isHittingBlock;
  }
  
  public void pickItem(int index) {
    this.connection.sendPacket((Packet)new CPacketCustomPayload("MC|PickItem", (new PacketBuffer(Unpooled.buffer())).writeVarIntToBuffer(index)));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\multiplayer\PlayerControllerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */