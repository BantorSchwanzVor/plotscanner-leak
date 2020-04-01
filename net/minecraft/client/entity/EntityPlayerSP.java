package net.minecraft.client.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ElytraSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditCommandBlockMinecart;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiEditStructure;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.seltak.anubis.Anubis;

public class EntityPlayerSP extends AbstractClientPlayer {
  public final NetHandlerPlayClient connection;
  
  private final StatisticsManager statWriter;
  
  private final RecipeBook field_192036_cb;
  
  private int permissionLevel = 0;
  
  private double lastReportedPosX;
  
  private double lastReportedPosY;
  
  private double lastReportedPosZ;
  
  private float lastReportedYaw;
  
  private float lastReportedPitch;
  
  private boolean prevOnGround;
  
  private boolean serverSneakState;
  
  private boolean serverSprintState;
  
  private int positionUpdateTicks;
  
  private boolean hasValidHealth;
  
  private String serverBrand;
  
  public MovementInput movementInput;
  
  protected Minecraft mc;
  
  protected int sprintToggleTimer;
  
  public int sprintingTicksLeft;
  
  public float renderArmYaw;
  
  public float renderArmPitch;
  
  public float prevRenderArmYaw;
  
  public float prevRenderArmPitch;
  
  private int horseJumpPowerCounter;
  
  private float horseJumpPower;
  
  public float timeInPortal;
  
  public float prevTimeInPortal;
  
  private boolean handActive;
  
  private EnumHand activeHand;
  
  private boolean rowingBoat;
  
  private boolean autoJumpEnabled = true;
  
  private int autoJumpTime;
  
  private boolean wasFallFlying;
  
  public EntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
    super(p_i47378_2_, p_i47378_3_.getGameProfile());
    this.connection = p_i47378_3_;
    this.statWriter = p_i47378_4_;
    this.field_192036_cb = p_i47378_5_;
    this.mc = p_i47378_1_;
    this.dimension = 0;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    return false;
  }
  
  public void heal(float healAmount) {}
  
  public boolean startRiding(Entity entityIn, boolean force) {
    if (!super.startRiding(entityIn, force))
      return false; 
    if (entityIn instanceof EntityMinecart)
      this.mc.getSoundHandler().playSound((ISound)new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn)); 
    if (entityIn instanceof EntityBoat) {
      this.prevRotationYaw = entityIn.rotationYaw;
      this.rotationYaw = entityIn.rotationYaw;
      setRotationYawHead(entityIn.rotationYaw);
    } 
    return true;
  }
  
  public void dismountRidingEntity() {
    super.dismountRidingEntity();
    this.rowingBoat = false;
  }
  
  public Vec3d getLook(float partialTicks) {
    return getVectorForRotation(this.rotationPitch, this.rotationYaw);
  }
  
  public void onUpdate() {
    if (this.world.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
      super.onUpdate();
      if (isRiding()) {
        this.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
        this.connection.sendPacket((Packet)new CPacketInput(this.moveStrafing, this.field_191988_bg, this.movementInput.jump, this.movementInput.sneak));
        Entity entity = getLowestRidingEntity();
        if (entity != this && entity.canPassengerSteer())
          this.connection.sendPacket((Packet)new CPacketVehicleMove(entity)); 
      } else {
        onUpdateWalkingPlayer();
      } 
      Anubis.onPostUpdate();
    } 
  }
  
  private void onUpdateWalkingPlayer() {
    Anubis.onPreUpdate();
    boolean flag = isSprinting();
    if (flag != this.serverSprintState) {
      if (flag) {
        this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_SPRINTING));
      } else {
        this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.STOP_SPRINTING));
      } 
      this.serverSprintState = flag;
    } 
    boolean flag1 = isSneaking();
    if (flag1 != this.serverSneakState) {
      if (flag1) {
        this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_SNEAKING));
      } else {
        this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.STOP_SNEAKING));
      } 
      this.serverSneakState = flag1;
    } 
    if (isCurrentViewEntity()) {
      AxisAlignedBB axisalignedbb = getEntityBoundingBox();
      double d0 = this.posX - this.lastReportedPosX;
      double d1 = axisalignedbb.minY - this.lastReportedPosY;
      double d2 = this.posZ - this.lastReportedPosZ;
      double d3 = (this.rotationYaw - this.lastReportedYaw);
      double d4 = (this.rotationPitch - this.lastReportedPitch);
      this.positionUpdateTicks++;
      boolean flag2 = !(d0 * d0 + d1 * d1 + d2 * d2 <= 9.0E-4D && this.positionUpdateTicks < 20);
      boolean flag3 = !(d3 == 0.0D && d4 == 0.0D);
      if (isRiding()) {
        this.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
        flag2 = false;
      } else if (flag2 && flag3) {
        this.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(this.posX, axisalignedbb.minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
      } else if (flag2) {
        this.connection.sendPacket((Packet)new CPacketPlayer.Position(this.posX, axisalignedbb.minY, this.posZ, this.onGround));
      } else if (flag3) {
        this.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
      } else if (this.prevOnGround != this.onGround) {
        this.connection.sendPacket((Packet)new CPacketPlayer(this.onGround));
      } 
      if (flag2) {
        this.lastReportedPosX = this.posX;
        this.lastReportedPosY = axisalignedbb.minY;
        this.lastReportedPosZ = this.posZ;
        this.positionUpdateTicks = 0;
      } 
      if (flag3) {
        this.lastReportedYaw = this.rotationYaw;
        this.lastReportedPitch = this.rotationPitch;
      } 
      this.prevOnGround = this.onGround;
      this.autoJumpEnabled = this.mc.gameSettings.autoJump;
    } 
  }
  
  @Nullable
  public EntityItem dropItem(boolean dropAll) {
    CPacketPlayerDigging.Action cpacketplayerdigging$action = dropAll ? CPacketPlayerDigging.Action.DROP_ALL_ITEMS : CPacketPlayerDigging.Action.DROP_ITEM;
    this.connection.sendPacket((Packet)new CPacketPlayerDigging(cpacketplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
    return null;
  }
  
  protected ItemStack dropItemAndGetStack(EntityItem p_184816_1_) {
    return ItemStack.field_190927_a;
  }
  
  public void sendChatMessage(String message) {
    this.connection.sendPacket((Packet)new CPacketChatMessage(message));
  }
  
  public void swingArm(EnumHand hand) {
    super.swingArm(hand);
    this.connection.sendPacket((Packet)new CPacketAnimation(hand));
  }
  
  public void respawnPlayer() {
    this.connection.sendPacket((Packet)new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
  }
  
  protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    if (!isEntityInvulnerable(damageSrc))
      setHealth(getHealth() - damageAmount); 
  }
  
  public void closeScreen() {
    this.connection.sendPacket((Packet)new CPacketCloseWindow(this.openContainer.windowId));
    closeScreenAndDropStack();
  }
  
  public void closeScreenAndDropStack() {
    this.inventory.setItemStack(ItemStack.field_190927_a);
    super.closeScreen();
    this.mc.displayGuiScreen(null);
  }
  
  public void setPlayerSPHealth(float health) {
    if (this.hasValidHealth) {
      float f = getHealth() - health;
      if (f <= 0.0F) {
        setHealth(health);
        if (f < 0.0F)
          this.hurtResistantTime = this.maxHurtResistantTime / 2; 
      } else {
        this.lastDamage = f;
        setHealth(getHealth());
        this.hurtResistantTime = this.maxHurtResistantTime;
        damageEntity(DamageSource.generic, f);
        this.maxHurtTime = 10;
        this.hurtTime = this.maxHurtTime;
      } 
    } else {
      setHealth(health);
      this.hasValidHealth = true;
    } 
  }
  
  public void addStat(StatBase stat, int amount) {
    if (stat != null)
      if (stat.isIndependent)
        super.addStat(stat, amount);  
  }
  
  public void sendPlayerAbilities() {
    this.connection.sendPacket((Packet)new CPacketPlayerAbilities(this.capabilities));
  }
  
  public boolean isUser() {
    return true;
  }
  
  protected void sendHorseJump() {
    this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_RIDING_JUMP, MathHelper.floor(getHorseJumpPower() * 100.0F)));
  }
  
  public void sendHorseInventory() {
    this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.OPEN_INVENTORY));
  }
  
  public void setServerBrand(String brand) {
    this.serverBrand = brand;
  }
  
  public String getServerBrand() {
    return this.serverBrand;
  }
  
  public StatisticsManager getStatFileWriter() {
    return this.statWriter;
  }
  
  public RecipeBook func_192035_E() {
    return this.field_192036_cb;
  }
  
  public void func_193103_a(IRecipe p_193103_1_) {
    if (this.field_192036_cb.func_194076_e(p_193103_1_)) {
      this.field_192036_cb.func_194074_f(p_193103_1_);
      this.connection.sendPacket((Packet)new CPacketRecipeInfo(p_193103_1_));
    } 
  }
  
  public int getPermissionLevel() {
    return this.permissionLevel;
  }
  
  public void setPermissionLevel(int p_184839_1_) {
    this.permissionLevel = p_184839_1_;
  }
  
  public void addChatComponentMessage(ITextComponent chatComponent, boolean p_146105_2_) {
    if (p_146105_2_) {
      this.mc.ingameGUI.setRecordPlaying(chatComponent, false);
    } else {
      this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
    } 
  }
  
  protected boolean pushOutOfBlocks(double x, double y, double z) {
    if (this.noClip)
      return false; 
    BlockPos blockpos = new BlockPos(x, y, z);
    double d0 = x - blockpos.getX();
    double d1 = z - blockpos.getZ();
    if (!isOpenBlockSpace(blockpos)) {
      int i = -1;
      double d2 = 9999.0D;
      if (isOpenBlockSpace(blockpos.west()) && d0 < d2) {
        d2 = d0;
        i = 0;
      } 
      if (isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
        d2 = 1.0D - d0;
        i = 1;
      } 
      if (isOpenBlockSpace(blockpos.north()) && d1 < d2) {
        d2 = d1;
        i = 4;
      } 
      if (isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
        d2 = 1.0D - d1;
        i = 5;
      } 
      float f = 0.1F;
      if (i == 0)
        this.motionX = -0.10000000149011612D; 
      if (i == 1)
        this.motionX = 0.10000000149011612D; 
      if (i == 4)
        this.motionZ = -0.10000000149011612D; 
      if (i == 5)
        this.motionZ = 0.10000000149011612D; 
    } 
    return false;
  }
  
  private boolean isOpenBlockSpace(BlockPos pos) {
    return (!this.world.getBlockState(pos).isNormalCube() && !this.world.getBlockState(pos.up()).isNormalCube());
  }
  
  public void setSprinting(boolean sprinting) {
    super.setSprinting(sprinting);
    this.sprintingTicksLeft = 0;
  }
  
  public void setXPStats(float currentXP, int maxXP, int level) {
    this.experience = currentXP;
    this.experienceTotal = maxXP;
    this.experienceLevel = level;
  }
  
  public void addChatMessage(ITextComponent component) {
    this.mc.ingameGUI.getChatGUI().printChatMessage(component);
  }
  
  public void sendMessage(ITextComponent component) {
    this.mc.ingameGUI.getChatGUI().printChatMessage(component);
  }
  
  public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
    return (permLevel <= getPermissionLevel());
  }
  
  public void handleStatusUpdate(byte id) {
    if (id >= 24 && id <= 28) {
      setPermissionLevel(id - 24);
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  public BlockPos getPosition() {
    return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
  }
  
  public void playSound(SoundEvent soundIn, float volume, float pitch) {
    this.world.playSound(this.posX, this.posY, this.posZ, soundIn, getSoundCategory(), volume, pitch, false);
  }
  
  public boolean isServerWorld() {
    return true;
  }
  
  public void setActiveHand(EnumHand hand) {
    ItemStack itemstack = getHeldItem(hand);
    if (!itemstack.func_190926_b() && !isHandActive()) {
      super.setActiveHand(hand);
      this.handActive = true;
      this.activeHand = hand;
    } 
  }
  
  public boolean isHandActive() {
    return this.handActive;
  }
  
  public void resetActiveHand() {
    super.resetActiveHand();
    this.handActive = false;
  }
  
  public EnumHand getActiveHand() {
    return this.activeHand;
  }
  
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (HAND_STATES.equals(key)) {
      boolean flag = ((((Byte)this.dataManager.get(HAND_STATES)).byteValue() & 0x1) > 0);
      EnumHand enumhand = ((((Byte)this.dataManager.get(HAND_STATES)).byteValue() & 0x2) > 0) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
      if (flag && !this.handActive) {
        setActiveHand(enumhand);
      } else if (!flag && this.handActive) {
        resetActiveHand();
      } 
    } 
    if (FLAGS.equals(key) && isElytraFlying() && !this.wasFallFlying)
      this.mc.getSoundHandler().playSound((ISound)new ElytraSound(this)); 
  }
  
  public boolean isRidingHorse() {
    Entity entity = getRidingEntity();
    return (isRiding() && entity instanceof IJumpingMount && ((IJumpingMount)entity).canJump());
  }
  
  public float getHorseJumpPower() {
    return this.horseJumpPower;
  }
  
  public void openEditSign(TileEntitySign signTile) {
    this.mc.displayGuiScreen((GuiScreen)new GuiEditSign(signTile));
  }
  
  public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlock) {
    this.mc.displayGuiScreen((GuiScreen)new GuiEditCommandBlockMinecart(commandBlock));
  }
  
  public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {
    this.mc.displayGuiScreen((GuiScreen)new GuiCommandBlock(commandBlock));
  }
  
  public void openEditStructure(TileEntityStructure structure) {
    this.mc.displayGuiScreen((GuiScreen)new GuiEditStructure(structure));
  }
  
  public void openBook(ItemStack stack, EnumHand hand) {
    Item item = stack.getItem();
    if (item == Items.WRITABLE_BOOK)
      this.mc.displayGuiScreen((GuiScreen)new GuiScreenBook(this, stack, true)); 
  }
  
  public void displayGUIChest(IInventory chestInventory) {
    String s = (chestInventory instanceof IInteractionObject) ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
    if ("minecraft:chest".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiChest((IInventory)this.inventory, chestInventory));
    } else if ("minecraft:hopper".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiHopper(this.inventory, chestInventory));
    } else if ("minecraft:furnace".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiFurnace(this.inventory, chestInventory));
    } else if ("minecraft:brewing_stand".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiBrewingStand(this.inventory, chestInventory));
    } else if ("minecraft:beacon".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiBeacon(this.inventory, chestInventory));
    } else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
      if ("minecraft:shulker_box".equals(s)) {
        this.mc.displayGuiScreen((GuiScreen)new GuiShulkerBox(this.inventory, chestInventory));
      } else {
        this.mc.displayGuiScreen((GuiScreen)new GuiChest((IInventory)this.inventory, chestInventory));
      } 
    } else {
      this.mc.displayGuiScreen((GuiScreen)new GuiDispenser(this.inventory, chestInventory));
    } 
  }
  
  public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {
    this.mc.displayGuiScreen((GuiScreen)new GuiScreenHorseInventory((IInventory)this.inventory, inventoryIn, horse));
  }
  
  public void displayGui(IInteractionObject guiOwner) {
    String s = guiOwner.getGuiID();
    if ("minecraft:crafting_table".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiCrafting(this.inventory, this.world));
    } else if ("minecraft:enchanting_table".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiEnchantment(this.inventory, this.world, (IWorldNameable)guiOwner));
    } else if ("minecraft:anvil".equals(s)) {
      this.mc.displayGuiScreen((GuiScreen)new GuiRepair(this.inventory, this.world));
    } 
  }
  
  public void displayVillagerTradeGui(IMerchant villager) {
    this.mc.displayGuiScreen((GuiScreen)new GuiMerchant(this.inventory, villager, this.world));
  }
  
  public void onCriticalHit(Entity entityHit) {
    this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
  }
  
  public void onEnchantmentCritical(Entity entityHit) {
    this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
  }
  
  public boolean isSneaking() {
    boolean flag = (this.movementInput != null && this.movementInput.sneak);
    return (flag && !this.sleeping);
  }
  
  public void updateEntityActionState() {
    super.updateEntityActionState();
    if (isCurrentViewEntity()) {
      this.moveStrafing = this.movementInput.moveStrafe;
      this.field_191988_bg = this.movementInput.field_192832_b;
      this.isJumping = this.movementInput.jump;
      this.prevRenderArmYaw = this.renderArmYaw;
      this.prevRenderArmPitch = this.renderArmPitch;
      this.renderArmPitch = (float)(this.renderArmPitch + (this.rotationPitch - this.renderArmPitch) * 0.5D);
      this.renderArmYaw = (float)(this.renderArmYaw + (this.rotationYaw - this.renderArmYaw) * 0.5D);
    } 
  }
  
  protected boolean isCurrentViewEntity() {
    return (this.mc.getRenderViewEntity() == this);
  }
  
  public void onLivingUpdate() {
    this.sprintingTicksLeft++;
    if (this.sprintToggleTimer > 0)
      this.sprintToggleTimer--; 
    this.prevTimeInPortal = this.timeInPortal;
    if (this.inPortal) {
      if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
        if (this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer)
          closeScreen(); 
        this.mc.displayGuiScreen(null);
      } 
      if (this.timeInPortal == 0.0F)
        this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4F + 0.8F)); 
      this.timeInPortal += 0.0125F;
      if (this.timeInPortal >= 1.0F)
        this.timeInPortal = 1.0F; 
      this.inPortal = false;
    } else if (isPotionActive(MobEffects.NAUSEA) && getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60) {
      this.timeInPortal += 0.006666667F;
      if (this.timeInPortal > 1.0F)
        this.timeInPortal = 1.0F; 
    } else {
      if (this.timeInPortal > 0.0F)
        this.timeInPortal -= 0.05F; 
      if (this.timeInPortal < 0.0F)
        this.timeInPortal = 0.0F; 
    } 
    if (this.timeUntilPortal > 0)
      this.timeUntilPortal--; 
    boolean flag = this.movementInput.jump;
    boolean flag1 = this.movementInput.sneak;
    float f = 0.8F;
    boolean flag2 = (this.movementInput.field_192832_b >= 0.8F);
    this.movementInput.updatePlayerMoveState();
    this.mc.func_193032_ao().func_193293_a(this.movementInput);
    if (isHandActive() && !isRiding()) {
      this.movementInput.moveStrafe *= 0.2F;
      this.movementInput.field_192832_b *= 0.2F;
      this.sprintToggleTimer = 0;
    } 
    boolean flag3 = false;
    if (this.autoJumpTime > 0) {
      this.autoJumpTime--;
      flag3 = true;
      this.movementInput.jump = true;
    } 
    AxisAlignedBB axisalignedbb = getEntityBoundingBox();
    pushOutOfBlocks(this.posX - this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ + this.width * 0.35D);
    pushOutOfBlocks(this.posX - this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ - this.width * 0.35D);
    pushOutOfBlocks(this.posX + this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ - this.width * 0.35D);
    pushOutOfBlocks(this.posX + this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ + this.width * 0.35D);
    boolean flag4 = !(getFoodStats().getFoodLevel() <= 6.0F && !this.capabilities.allowFlying);
    if (this.onGround && !flag1 && !flag2 && this.movementInput.field_192832_b >= 0.8F && !isSprinting() && flag4 && !isHandActive() && !isPotionActive(MobEffects.BLINDNESS))
      if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
        this.sprintToggleTimer = 7;
      } else {
        setSprinting(true);
      }  
    if (!isSprinting() && this.movementInput.field_192832_b >= 0.8F && flag4 && !isHandActive() && !isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown())
      setSprinting(true); 
    if (isSprinting() && (this.movementInput.field_192832_b < 0.8F || this.isCollidedHorizontally || !flag4))
      setSprinting(false); 
    if (this.capabilities.allowFlying)
      if (this.mc.playerController.isSpectatorMode()) {
        if (!this.capabilities.isFlying) {
          this.capabilities.isFlying = true;
          sendPlayerAbilities();
        } 
      } else if (!flag && this.movementInput.jump && !flag3) {
        if (this.flyToggleTimer == 0) {
          this.flyToggleTimer = 7;
        } else {
          this.capabilities.isFlying = !this.capabilities.isFlying;
          sendPlayerAbilities();
          this.flyToggleTimer = 0;
        } 
      }  
    if (this.movementInput.jump && !flag && !this.onGround && this.motionY < 0.0D && !isElytraFlying() && !this.capabilities.isFlying) {
      ItemStack itemstack = getItemStackFromSlot(EntityEquipmentSlot.CHEST);
      if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack))
        this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)this, CPacketEntityAction.Action.START_FALL_FLYING)); 
    } 
    this.wasFallFlying = isElytraFlying();
    if (this.capabilities.isFlying && isCurrentViewEntity()) {
      if (this.movementInput.sneak) {
        this.movementInput.moveStrafe = (float)(this.movementInput.moveStrafe / 0.3D);
        this.movementInput.field_192832_b = (float)(this.movementInput.field_192832_b / 0.3D);
        this.motionY -= (this.capabilities.getFlySpeed() * 3.0F);
      } 
      if (this.movementInput.jump)
        this.motionY += (this.capabilities.getFlySpeed() * 3.0F); 
    } 
    if (isRidingHorse()) {
      IJumpingMount ijumpingmount = (IJumpingMount)getRidingEntity();
      if (this.horseJumpPowerCounter < 0) {
        this.horseJumpPowerCounter++;
        if (this.horseJumpPowerCounter == 0)
          this.horseJumpPower = 0.0F; 
      } 
      if (flag && !this.movementInput.jump) {
        this.horseJumpPowerCounter = -10;
        ijumpingmount.setJumpPower(MathHelper.floor(getHorseJumpPower() * 100.0F));
        sendHorseJump();
      } else if (!flag && this.movementInput.jump) {
        this.horseJumpPowerCounter = 0;
        this.horseJumpPower = 0.0F;
      } else if (flag) {
        this.horseJumpPowerCounter++;
        if (this.horseJumpPowerCounter < 10) {
          this.horseJumpPower = this.horseJumpPowerCounter * 0.1F;
        } else {
          this.horseJumpPower = 0.8F + 2.0F / (this.horseJumpPowerCounter - 9) * 0.1F;
        } 
      } 
    } else {
      this.horseJumpPower = 0.0F;
    } 
    super.onLivingUpdate();
    if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
      this.capabilities.isFlying = false;
      sendPlayerAbilities();
    } 
  }
  
  public void updateRidden() {
    super.updateRidden();
    this.rowingBoat = false;
    if (getRidingEntity() instanceof EntityBoat) {
      EntityBoat entityboat = (EntityBoat)getRidingEntity();
      entityboat.updateInputs(this.movementInput.leftKeyDown, this.movementInput.rightKeyDown, this.movementInput.forwardKeyDown, this.movementInput.backKeyDown);
      this.rowingBoat |= (!this.movementInput.leftKeyDown && !this.movementInput.rightKeyDown && !this.movementInput.forwardKeyDown && !this.movementInput.backKeyDown) ? 0 : 1;
    } 
  }
  
  public boolean isRowingBoat() {
    return this.rowingBoat;
  }
  
  @Nullable
  public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin) {
    if (potioneffectin == MobEffects.NAUSEA) {
      this.prevTimeInPortal = 0.0F;
      this.timeInPortal = 0.0F;
    } 
    return super.removeActivePotionEffect(potioneffectin);
  }
  
  public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
    double d0 = this.posX;
    double d1 = this.posZ;
    super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
    updateAutoJump((float)(this.posX - d0), (float)(this.posZ - d1));
  }
  
  public boolean isAutoJumpEnabled() {
    return this.autoJumpEnabled;
  }
  
  protected void updateAutoJump(float p_189810_1_, float p_189810_2_) {
    if (isAutoJumpEnabled())
      if (this.autoJumpTime <= 0 && this.onGround && !isSneaking() && !isRiding()) {
        Vec2f vec2f = this.movementInput.getMoveVector();
        if (vec2f.x != 0.0F || vec2f.y != 0.0F) {
          Vec3d vec3d = new Vec3d(this.posX, (getEntityBoundingBox()).minY, this.posZ);
          double d0 = this.posX + p_189810_1_;
          double d1 = this.posZ + p_189810_2_;
          Vec3d vec3d1 = new Vec3d(d0, (getEntityBoundingBox()).minY, d1);
          Vec3d vec3d2 = new Vec3d(p_189810_1_, 0.0D, p_189810_2_);
          float f = getAIMoveSpeed();
          float f1 = (float)vec3d2.lengthSquared();
          if (f1 <= 0.001F) {
            float f2 = f * vec2f.x;
            float f3 = f * vec2f.y;
            float f4 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f5 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            vec3d2 = new Vec3d((f2 * f5 - f3 * f4), vec3d2.yCoord, (f3 * f5 + f2 * f4));
            f1 = (float)vec3d2.lengthSquared();
            if (f1 <= 0.001F)
              return; 
          } 
          float f12 = (float)MathHelper.fastInvSqrt(f1);
          Vec3d vec3d12 = vec3d2.scale(f12);
          Vec3d vec3d13 = getForward();
          float f13 = (float)(vec3d13.xCoord * vec3d12.xCoord + vec3d13.zCoord * vec3d12.zCoord);
          if (f13 >= -0.15F) {
            BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).maxY, this.posZ);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getCollisionBoundingBox((IBlockAccess)this.world, blockpos) == null) {
              blockpos = blockpos.up();
              IBlockState iblockstate1 = this.world.getBlockState(blockpos);
              if (iblockstate1.getCollisionBoundingBox((IBlockAccess)this.world, blockpos) == null) {
                float f6 = 7.0F;
                float f7 = 1.2F;
                if (isPotionActive(MobEffects.JUMP_BOOST))
                  f7 += (getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75F; 
                float f8 = Math.max(f * 7.0F, 1.0F / f12);
                Vec3d vec3d4 = vec3d1.add(vec3d12.scale(f8));
                float f9 = this.width;
                float f10 = this.height;
                AxisAlignedBB axisalignedbb = (new AxisAlignedBB(vec3d, vec3d4.addVector(0.0D, f10, 0.0D))).expand(f9, 0.0D, f9);
                Vec3d lvt_19_1_ = vec3d.addVector(0.0D, 0.5099999904632568D, 0.0D);
                vec3d4 = vec3d4.addVector(0.0D, 0.5099999904632568D, 0.0D);
                Vec3d vec3d5 = vec3d12.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
                Vec3d vec3d6 = vec3d5.scale((f9 * 0.5F));
                Vec3d vec3d7 = lvt_19_1_.subtract(vec3d6);
                Vec3d vec3d8 = vec3d4.subtract(vec3d6);
                Vec3d vec3d9 = lvt_19_1_.add(vec3d6);
                Vec3d vec3d10 = vec3d4.add(vec3d6);
                List<AxisAlignedBB> list = this.world.getCollisionBoxes((Entity)this, axisalignedbb);
                if (!list.isEmpty());
                float f11 = Float.MIN_VALUE;
                for (AxisAlignedBB axisalignedbb2 : list) {
                  if (axisalignedbb2.intersects(vec3d7, vec3d8) || axisalignedbb2.intersects(vec3d9, vec3d10)) {
                    f11 = (float)axisalignedbb2.maxY;
                    Vec3d vec3d11 = axisalignedbb2.getCenter();
                    BlockPos blockpos1 = new BlockPos(vec3d11);
                    int i = 1;
                    while (i < f7) {
                      BlockPos blockpos2 = blockpos1.up(i);
                      IBlockState iblockstate2 = this.world.getBlockState(blockpos2);
                      AxisAlignedBB axisalignedbb1;
                      if ((axisalignedbb1 = iblockstate2.getCollisionBoundingBox((IBlockAccess)this.world, blockpos2)) != null) {
                        f11 = (float)axisalignedbb1.maxY + blockpos2.getY();
                        if (f11 - (getEntityBoundingBox()).minY > f7)
                          return; 
                      } 
                      if (i > 1) {
                        blockpos = blockpos.up();
                        IBlockState iblockstate3 = this.world.getBlockState(blockpos);
                        if (iblockstate3.getCollisionBoundingBox((IBlockAccess)this.world, blockpos) != null)
                          return; 
                      } 
                      i++;
                    } 
                    break;
                  } 
                } 
                if (f11 != Float.MIN_VALUE) {
                  float f14 = (float)(f11 - (getEntityBoundingBox()).minY);
                  if (f14 > 0.5F && f14 <= f7)
                    this.autoJumpTime = 1; 
                } 
              } 
            } 
          } 
        } 
      }  
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\entity\EntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */