package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;

public class RealmsSliderButton extends RealmsButton {
  public float value;
  
  public boolean sliding;
  
  private final float minValue;
  
  private final float maxValue;
  
  private int steps;
  
  public RealmsSliderButton(int buttonId, int x, int y, int width, int maxValueIn, int p_i1056_6_) {
    this(buttonId, x, y, width, p_i1056_6_, 0, 1.0F, maxValueIn);
  }
  
  public RealmsSliderButton(int buttonId, int x, int y, int width, int p_i1057_5_, int valueIn, float minValueIn, float maxValueIn) {
    super(buttonId, x, y, width, 20, "");
    this.value = 1.0F;
    this.minValue = minValueIn;
    this.maxValue = maxValueIn;
    this.value = toPct(valueIn);
    (getProxy()).displayString = getMessage();
  }
  
  public String getMessage() {
    return "";
  }
  
  public float toPct(float p_toPct_1_) {
    return MathHelper.clamp((clamp(p_toPct_1_) - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F);
  }
  
  public float toValue(float p_toValue_1_) {
    return clamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp(p_toValue_1_, 0.0F, 1.0F));
  }
  
  public float clamp(float p_clamp_1_) {
    p_clamp_1_ = clampSteps(p_clamp_1_);
    return MathHelper.clamp(p_clamp_1_, this.minValue, this.maxValue);
  }
  
  protected float clampSteps(float p_clampSteps_1_) {
    if (this.steps > 0)
      p_clampSteps_1_ = (this.steps * Math.round(p_clampSteps_1_ / this.steps)); 
    return p_clampSteps_1_;
  }
  
  public int getYImage(boolean p_getYImage_1_) {
    return 0;
  }
  
  public void renderBg(int p_renderBg_1_, int p_renderBg_2_) {
    if ((getProxy()).visible) {
      if (this.sliding) {
        this.value = (p_renderBg_1_ - (getProxy()).xPosition + 4) / (getProxy().getButtonWidth() - 8);
        this.value = MathHelper.clamp(this.value, 0.0F, 1.0F);
        float f = toValue(this.value);
        clicked(f);
        this.value = toPct(f);
        (getProxy()).displayString = getMessage();
      } 
      Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS_LOCATION);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      blit((getProxy()).xPosition + (int)(this.value * (getProxy().getButtonWidth() - 8)), (getProxy()).yPosition, 0, 66, 4, 20);
      blit((getProxy()).xPosition + (int)(this.value * (getProxy().getButtonWidth() - 8)) + 4, (getProxy()).yPosition, 196, 66, 4, 20);
    } 
  }
  
  public void clicked(int p_clicked_1_, int p_clicked_2_) {
    this.value = (p_clicked_1_ - (getProxy()).xPosition + 4) / (getProxy().getButtonWidth() - 8);
    this.value = MathHelper.clamp(this.value, 0.0F, 1.0F);
    clicked(toValue(this.value));
    (getProxy()).displayString = getMessage();
    this.sliding = true;
  }
  
  public void clicked(float p_clicked_1_) {}
  
  public void released(int p_released_1_, int p_released_2_) {
    this.sliding = false;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\realms\RealmsSliderButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */