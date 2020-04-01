package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StatisticsManagerServer extends StatisticsManager {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final MinecraftServer mcServer;
  
  private final File statsFile;
  
  private final Set<StatBase> dirty = Sets.newHashSet();
  
  private int lastStatRequest = -300;
  
  public StatisticsManagerServer(MinecraftServer serverIn, File statsFileIn) {
    this.mcServer = serverIn;
    this.statsFile = statsFileIn;
  }
  
  public void readStatFile() {
    if (this.statsFile.isFile())
      try {
        this.statsData.clear();
        this.statsData.putAll(parseJson(FileUtils.readFileToString(this.statsFile)));
      } catch (IOException ioexception) {
        LOGGER.error("Couldn't read statistics file {}", this.statsFile, ioexception);
      } catch (JsonParseException jsonparseexception) {
        LOGGER.error("Couldn't parse statistics file {}", this.statsFile, jsonparseexception);
      }  
  }
  
  public void saveStatFile() {
    try {
      FileUtils.writeStringToFile(this.statsFile, dumpJson(this.statsData));
    } catch (IOException ioexception) {
      LOGGER.error("Couldn't save stats", ioexception);
    } 
  }
  
  public void unlockAchievement(EntityPlayer playerIn, StatBase statIn, int p_150873_3_) {
    super.unlockAchievement(playerIn, statIn, p_150873_3_);
    this.dirty.add(statIn);
  }
  
  private Set<StatBase> getDirty() {
    Set<StatBase> set = Sets.newHashSet(this.dirty);
    this.dirty.clear();
    return set;
  }
  
  public Map<StatBase, TupleIntJsonSerializable> parseJson(String p_150881_1_) {
    JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);
    if (!jsonelement.isJsonObject())
      return Maps.newHashMap(); 
    JsonObject jsonobject = jsonelement.getAsJsonObject();
    Map<StatBase, TupleIntJsonSerializable> map = Maps.newHashMap();
    for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)jsonobject.entrySet()) {
      StatBase statbase = StatList.getOneShotStat(entry.getKey());
      if (statbase != null) {
        TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();
        if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber()) {
          tupleintjsonserializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
        } else if (((JsonElement)entry.getValue()).isJsonObject()) {
          JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();
          if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber())
            tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt()); 
          if (jsonobject1.has("progress") && statbase.getSerializableClazz() != null)
            try {
              Constructor<? extends IJsonSerializable> constructor = statbase.getSerializableClazz().getConstructor(new Class[0]);
              IJsonSerializable ijsonserializable = constructor.newInstance(new Object[0]);
              ijsonserializable.fromJson(jsonobject1.get("progress"));
              tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
            } catch (Throwable throwable) {
              LOGGER.warn("Invalid statistic progress in {}", this.statsFile, throwable);
            }  
        } 
        map.put(statbase, tupleintjsonserializable);
        continue;
      } 
      LOGGER.warn("Invalid statistic in {}: Don't know what {} is", this.statsFile, entry.getKey());
    } 
    return map;
  }
  
  public static String dumpJson(Map<StatBase, TupleIntJsonSerializable> p_150880_0_) {
    JsonObject jsonobject = new JsonObject();
    for (Map.Entry<StatBase, TupleIntJsonSerializable> entry : p_150880_0_.entrySet()) {
      if (((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue() != null) {
        JsonObject jsonobject1 = new JsonObject();
        jsonobject1.addProperty("value", Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).getIntegerValue()));
        try {
          jsonobject1.add("progress", ((TupleIntJsonSerializable)entry.getValue()).getJsonSerializableValue().getSerializableElement());
        } catch (Throwable throwable) {
          LOGGER.warn("Couldn't save statistic {}: error serializing progress", ((StatBase)entry.getKey()).getStatName(), throwable);
        } 
        jsonobject.add(((StatBase)entry.getKey()).statId, (JsonElement)jsonobject1);
        continue;
      } 
      jsonobject.addProperty(((StatBase)entry.getKey()).statId, Integer.valueOf(((TupleIntJsonSerializable)entry.getValue()).getIntegerValue()));
    } 
    return jsonobject.toString();
  }
  
  public void markAllDirty() {
    this.dirty.addAll(this.statsData.keySet());
  }
  
  public void sendStats(EntityPlayerMP player) {
    int i = this.mcServer.getTickCounter();
    Map<StatBase, Integer> map = Maps.newHashMap();
    if (i - this.lastStatRequest > 300) {
      this.lastStatRequest = i;
      for (StatBase statbase : getDirty())
        map.put(statbase, Integer.valueOf(readStat(statbase))); 
    } 
    player.connection.sendPacket((Packet)new SPacketStatistics(map));
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\stats\StatisticsManagerServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */