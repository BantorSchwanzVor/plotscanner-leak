package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Config;
import optifine.Lagometer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private final List<String> sectionList = Lists.newArrayList();
  
  private final List<Long> timestampList = Lists.newArrayList();
  
  public boolean profilingEnabled;
  
  private String profilingSection = "";
  
  private final Map<String, Long> profilingMap = Maps.newHashMap();
  
  public boolean profilerGlobalEnabled = true;
  
  private boolean profilerLocalEnabled;
  
  private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
  
  private static final String TICK = "tick";
  
  private static final String PRE_RENDER_ERRORS = "preRenderErrors";
  
  private static final String RENDER = "render";
  
  private static final String DISPLAY = "display";
  
  private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
  
  private static final int HASH_TICK = "tick".hashCode();
  
  private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
  
  private static final int HASH_RENDER = "render".hashCode();
  
  private static final int HASH_DISPLAY = "display".hashCode();
  
  public Profiler() {
    this.profilerLocalEnabled = this.profilerGlobalEnabled;
  }
  
  public void clearProfiling() {
    this.profilingMap.clear();
    this.profilingSection = "";
    this.sectionList.clear();
    this.profilerLocalEnabled = this.profilerGlobalEnabled;
  }
  
  public void startSection(String name) {
    if (Lagometer.isActive()) {
      int i = name.hashCode();
      if (i == HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
        Lagometer.timerScheduledExecutables.start();
      } else if (i == HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
        Lagometer.timerScheduledExecutables.end();
        Lagometer.timerTick.start();
      } else if (i == HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
        Lagometer.timerTick.end();
      } 
    } 
    if (Config.isFastRender()) {
      int j = name.hashCode();
      if (j == HASH_RENDER && name.equals("render")) {
        GlStateManager.clearEnabled = false;
      } else if (j == HASH_DISPLAY && name.equals("display")) {
        GlStateManager.clearEnabled = true;
      } 
    } 
    if (this.profilerLocalEnabled)
      if (this.profilingEnabled) {
        if (!this.profilingSection.isEmpty())
          this.profilingSection = String.valueOf(this.profilingSection) + "."; 
        this.profilingSection = String.valueOf(this.profilingSection) + name;
        this.sectionList.add(this.profilingSection);
        this.timestampList.add(Long.valueOf(System.nanoTime()));
      }  
  }
  
  public void func_194340_a(Supplier<String> p_194340_1_) {
    if (this.profilerLocalEnabled)
      if (this.profilingEnabled)
        startSection(p_194340_1_.get());  
  }
  
  public void endSection() {
    if (this.profilerLocalEnabled)
      if (this.profilingEnabled) {
        long i = System.nanoTime();
        long j = ((Long)this.timestampList.remove(this.timestampList.size() - 1)).longValue();
        this.sectionList.remove(this.sectionList.size() - 1);
        long k = i - j;
        if (this.profilingMap.containsKey(this.profilingSection)) {
          this.profilingMap.put(this.profilingSection, Long.valueOf(((Long)this.profilingMap.get(this.profilingSection)).longValue() + k));
        } else {
          this.profilingMap.put(this.profilingSection, Long.valueOf(k));
        } 
        if (k > 100000000L)
          LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", this.profilingSection, Double.valueOf(k / 1000000.0D)); 
        this.profilingSection = this.sectionList.isEmpty() ? "" : this.sectionList.get(this.sectionList.size() - 1);
      }  
  }
  
  public List<Result> getProfilingData(String profilerName) {
    if (!this.profilingEnabled)
      return Collections.emptyList(); 
    long i = this.profilingMap.containsKey("root") ? ((Long)this.profilingMap.get("root")).longValue() : 0L;
    long j = this.profilingMap.containsKey(profilerName) ? ((Long)this.profilingMap.get(profilerName)).longValue() : -1L;
    List<Result> list = Lists.newArrayList();
    if (!profilerName.isEmpty())
      profilerName = String.valueOf(profilerName) + "."; 
    long k = 0L;
    for (String s : this.profilingMap.keySet()) {
      if (s.length() > profilerName.length() && s.startsWith(profilerName) && s.indexOf(".", profilerName.length() + 1) < 0)
        k += ((Long)this.profilingMap.get(s)).longValue(); 
    } 
    float f = (float)k;
    if (k < j)
      k = j; 
    if (i < k)
      i = k; 
    for (String s1 : this.profilingMap.keySet()) {
      if (s1.length() > profilerName.length() && s1.startsWith(profilerName) && s1.indexOf(".", profilerName.length() + 1) < 0) {
        long l = ((Long)this.profilingMap.get(s1)).longValue();
        double d0 = l * 100.0D / k;
        double d1 = l * 100.0D / i;
        String s2 = s1.substring(profilerName.length());
        list.add(new Result(s2, d0, d1));
      } 
    } 
    for (String s3 : this.profilingMap.keySet())
      this.profilingMap.put(s3, Long.valueOf(((Long)this.profilingMap.get(s3)).longValue() * 950L / 1000L)); 
    if ((float)k > f)
      list.add(new Result("unspecified", ((float)k - f) * 100.0D / k, ((float)k - f) * 100.0D / i)); 
    Collections.sort(list);
    list.add(0, new Result(profilerName, 100.0D, k * 100.0D / i));
    return list;
  }
  
  public void endStartSection(String name) {
    if (this.profilerLocalEnabled) {
      endSection();
      startSection(name);
    } 
  }
  
  public void func_194339_b(Supplier<String> p_194339_1_) {
    if (this.profilerLocalEnabled) {
      endSection();
      func_194340_a(p_194339_1_);
    } 
  }
  
  public String getNameOfLastSection() {
    return this.sectionList.isEmpty() ? "[UNKNOWN]" : this.sectionList.get(this.sectionList.size() - 1);
  }
  
  public void startSection(Class<?> p_startSection_1_) {
    if (this.profilingEnabled)
      startSection(p_startSection_1_.getSimpleName()); 
  }
  
  public static final class Result implements Comparable<Result> {
    public double usePercentage;
    
    public double totalUsePercentage;
    
    public String profilerName;
    
    public Result(String profilerName, double usePercentage, double totalUsePercentage) {
      this.profilerName = profilerName;
      this.usePercentage = usePercentage;
      this.totalUsePercentage = totalUsePercentage;
    }
    
    public int compareTo(Result p_compareTo_1_) {
      if (p_compareTo_1_.usePercentage < this.usePercentage)
        return -1; 
      return (p_compareTo_1_.usePercentage > this.usePercentage) ? 1 : p_compareTo_1_.profilerName.compareTo(this.profilerName);
    }
    
    public int getColor() {
      return (this.profilerName.hashCode() & 0xAAAAAA) + 4473924;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\profiler\Profiler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */