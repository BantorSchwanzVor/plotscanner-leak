package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class TileEntitySign extends TileEntity {
  public final ITextComponent[] signText = new ITextComponent[] { (ITextComponent)new TextComponentString(""), (ITextComponent)new TextComponentString(""), (ITextComponent)new TextComponentString(""), (ITextComponent)new TextComponentString("") };
  
  public int lineBeingEdited = -1;
  
  private boolean isEditable = true;
  
  private EntityPlayer player;
  
  private final CommandResultStats stats = new CommandResultStats();
  
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    for (int i = 0; i < 4; i++) {
      String s = ITextComponent.Serializer.componentToJson(this.signText[i]);
      compound.setString("Text" + (i + 1), s);
    } 
    this.stats.writeStatsToNBT(compound);
    return compound;
  }
  
  protected void setWorldCreate(World worldIn) {
    setWorldObj(worldIn);
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    this.isEditable = false;
    super.readFromNBT(compound);
    ICommandSender icommandsender = new ICommandSender() {
        public String getName() {
          return "Sign";
        }
        
        public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
          return true;
        }
        
        public BlockPos getPosition() {
          return TileEntitySign.this.pos;
        }
        
        public Vec3d getPositionVector() {
          return new Vec3d(TileEntitySign.this.pos.getX() + 0.5D, TileEntitySign.this.pos.getY() + 0.5D, TileEntitySign.this.pos.getZ() + 0.5D);
        }
        
        public World getEntityWorld() {
          return TileEntitySign.this.world;
        }
        
        public MinecraftServer getServer() {
          return TileEntitySign.this.world.getMinecraftServer();
        }
      };
    for (int i = 0; i < 4; i++) {
      String s = compound.getString("Text" + (i + 1));
      ITextComponent itextcomponent = ITextComponent.Serializer.jsonToComponent(s);
      try {
        this.signText[i] = TextComponentUtils.processComponent(icommandsender, itextcomponent, null);
      } catch (CommandException var7) {
        this.signText[i] = itextcomponent;
      } 
    } 
    this.stats.readStatsFromNBT(compound);
  }
  
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 9, getUpdateTag());
  }
  
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }
  
  public boolean onlyOpsCanSetNbt() {
    return true;
  }
  
  public boolean getIsEditable() {
    return this.isEditable;
  }
  
  public void setEditable(boolean isEditableIn) {
    this.isEditable = isEditableIn;
    if (!isEditableIn)
      this.player = null; 
  }
  
  public void setPlayer(EntityPlayer playerIn) {
    this.player = playerIn;
  }
  
  public EntityPlayer getPlayer() {
    return this.player;
  }
  
  public boolean executeCommand(final EntityPlayer playerIn) {
    ICommandSender icommandsender = new ICommandSender() {
        public String getName() {
          return playerIn.getName();
        }
        
        public ITextComponent getDisplayName() {
          return playerIn.getDisplayName();
        }
        
        public void addChatMessage(ITextComponent component) {}
        
        public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
          return (permLevel <= 2);
        }
        
        public BlockPos getPosition() {
          return TileEntitySign.this.pos;
        }
        
        public Vec3d getPositionVector() {
          return new Vec3d(TileEntitySign.this.pos.getX() + 0.5D, TileEntitySign.this.pos.getY() + 0.5D, TileEntitySign.this.pos.getZ() + 0.5D);
        }
        
        public World getEntityWorld() {
          return playerIn.getEntityWorld();
        }
        
        public Entity getCommandSenderEntity() {
          return (Entity)playerIn;
        }
        
        public boolean sendCommandFeedback() {
          return false;
        }
        
        public void setCommandStat(CommandResultStats.Type type, int amount) {
          if (TileEntitySign.this.world != null && !TileEntitySign.this.world.isRemote)
            TileEntitySign.this.stats.setCommandStatForSender(TileEntitySign.this.world.getMinecraftServer(), this, type, amount); 
        }
        
        public MinecraftServer getServer() {
          return playerIn.getServer();
        }
      };
    byte b;
    int i;
    ITextComponent[] arrayOfITextComponent;
    for (i = (arrayOfITextComponent = this.signText).length, b = 0; b < i; ) {
      ITextComponent itextcomponent = arrayOfITextComponent[b];
      Style style = (itextcomponent == null) ? null : itextcomponent.getStyle();
      if (style != null && style.getClickEvent() != null) {
        ClickEvent clickevent = style.getClickEvent();
        if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND)
          playerIn.getServer().getCommandManager().executeCommand(icommandsender, clickevent.getValue()); 
      } 
      b++;
    } 
    return true;
  }
  
  public CommandResultStats getStats() {
    return this.stats;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\tileentity\TileEntitySign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */