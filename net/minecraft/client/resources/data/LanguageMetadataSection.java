package net.minecraft.client.resources.data;

import java.util.Collection;
import net.minecraft.client.resources.Language;

public class LanguageMetadataSection implements IMetadataSection {
  private final Collection<Language> languages;
  
  public LanguageMetadataSection(Collection<Language> languagesIn) {
    this.languages = languagesIn;
  }
  
  public Collection<Language> getLanguages() {
    return this.languages;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\resources\data\LanguageMetadataSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */