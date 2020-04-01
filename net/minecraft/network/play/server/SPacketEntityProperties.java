package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketEntityProperties implements Packet<INetHandlerPlayClient> {
  private int entityId;
  
  private final List<Snapshot> snapshots = Lists.newArrayList();
  
  public SPacketEntityProperties(int entityIdIn, Collection<IAttributeInstance> instances) {
    this.entityId = entityIdIn;
    for (IAttributeInstance iattributeinstance : instances)
      this.snapshots.add(new Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.getModifiers())); 
  }
  
  public void readPacketData(PacketBuffer buf) throws IOException {
    this.entityId = buf.readVarIntFromBuffer();
    int i = buf.readInt();
    for (int j = 0; j < i; j++) {
      String s = buf.readStringFromBuffer(64);
      double d0 = buf.readDouble();
      List<AttributeModifier> list = Lists.newArrayList();
      int k = buf.readVarIntFromBuffer();
      for (int l = 0; l < k; l++) {
        UUID uuid = buf.readUuid();
        list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
      } 
      this.snapshots.add(new Snapshot(s, d0, list));
    } 
  }
  
  public void writePacketData(PacketBuffer buf) throws IOException {
    buf.writeVarIntToBuffer(this.entityId);
    buf.writeInt(this.snapshots.size());
    for (Snapshot spacketentityproperties$snapshot : this.snapshots) {
      buf.writeString(spacketentityproperties$snapshot.getName());
      buf.writeDouble(spacketentityproperties$snapshot.getBaseValue());
      buf.writeVarIntToBuffer(spacketentityproperties$snapshot.getModifiers().size());
      for (AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers()) {
        buf.writeUuid(attributemodifier.getID());
        buf.writeDouble(attributemodifier.getAmount());
        buf.writeByte(attributemodifier.getOperation());
      } 
    } 
  }
  
  public void processPacket(INetHandlerPlayClient handler) {
    handler.handleEntityProperties(this);
  }
  
  public int getEntityId() {
    return this.entityId;
  }
  
  public List<Snapshot> getSnapshots() {
    return this.snapshots;
  }
  
  public SPacketEntityProperties() {}
  
  public class Snapshot {
    private final String name;
    
    private final double baseValue;
    
    private final Collection<AttributeModifier> modifiers;
    
    public Snapshot(String nameIn, double baseValueIn, Collection<AttributeModifier> modifiersIn) {
      this.name = nameIn;
      this.baseValue = baseValueIn;
      this.modifiers = modifiersIn;
    }
    
    public String getName() {
      return this.name;
    }
    
    public double getBaseValue() {
      return this.baseValue;
    }
    
    public Collection<AttributeModifier> getModifiers() {
      return this.modifiers;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\network\play\server\SPacketEntityProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */