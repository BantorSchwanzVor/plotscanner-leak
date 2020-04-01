package net.minecraft.client.gui.toasts;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public interface IToast {
  public static final ResourceLocation field_193654_a = new ResourceLocation("textures/gui/toasts.png");
  
  public static final Object field_193655_b = new Object();
  
  Visibility func_193653_a(GuiToast paramGuiToast, long paramLong);
  
  default Object func_193652_b() {
    return field_193655_b;
  }
  
  public enum Visibility {
    SHOW((String)SoundEvents.field_194226_id),
    HIDE((String)SoundEvents.field_194227_ie);
    
    private final SoundEvent field_194170_c;
    
    Visibility(SoundEvent p_i47607_3_) {
      this.field_194170_c = p_i47607_3_;
    }
    
    public void func_194169_a(SoundHandler p_194169_1_) {
      p_194169_1_.playSound((ISound)PositionedSoundRecord.func_194007_a(this.field_194170_c, 1.0F, 1.0F));
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\gui\toasts\IToast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */