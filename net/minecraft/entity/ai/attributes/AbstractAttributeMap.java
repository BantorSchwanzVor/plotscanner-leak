package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.LowerStringMap;

public abstract class AbstractAttributeMap {
  protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
  
  protected final Map<String, IAttributeInstance> attributesByName = (Map<String, IAttributeInstance>)new LowerStringMap();
  
  protected final Multimap<IAttribute, IAttribute> descendantsByParent = (Multimap<IAttribute, IAttribute>)HashMultimap.create();
  
  public IAttributeInstance getAttributeInstance(IAttribute attribute) {
    return this.attributes.get(attribute);
  }
  
  @Nullable
  public IAttributeInstance getAttributeInstanceByName(String attributeName) {
    return this.attributesByName.get(attributeName);
  }
  
  public IAttributeInstance registerAttribute(IAttribute attribute) {
    if (this.attributesByName.containsKey(attribute.getAttributeUnlocalizedName()))
      throw new IllegalArgumentException("Attribute is already registered!"); 
    IAttributeInstance iattributeinstance = createInstance(attribute);
    this.attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
    this.attributes.put(attribute, iattributeinstance);
    for (IAttribute iattribute = attribute.getParent(); iattribute != null; iattribute = iattribute.getParent())
      this.descendantsByParent.put(iattribute, attribute); 
    return iattributeinstance;
  }
  
  protected abstract IAttributeInstance createInstance(IAttribute paramIAttribute);
  
  public Collection<IAttributeInstance> getAllAttributes() {
    return this.attributesByName.values();
  }
  
  public void onAttributeModified(IAttributeInstance instance) {}
  
  public void removeAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
    for (Map.Entry<String, AttributeModifier> entry : (Iterable<Map.Entry<String, AttributeModifier>>)modifiers.entries()) {
      IAttributeInstance iattributeinstance = getAttributeInstanceByName(entry.getKey());
      if (iattributeinstance != null)
        iattributeinstance.removeModifier(entry.getValue()); 
    } 
  }
  
  public void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
    for (Map.Entry<String, AttributeModifier> entry : (Iterable<Map.Entry<String, AttributeModifier>>)modifiers.entries()) {
      IAttributeInstance iattributeinstance = getAttributeInstanceByName(entry.getKey());
      if (iattributeinstance != null) {
        iattributeinstance.removeModifier(entry.getValue());
        iattributeinstance.applyModifier(entry.getValue());
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\entity\ai\attributes\AbstractAttributeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */