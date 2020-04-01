package org.newdawn.slick.font.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Iterator;
import java.util.List;

public class OutlineWobbleEffect extends OutlineEffect {
  private float detail = 1.0F;
  
  private float amplitude = 1.0F;
  
  public OutlineWobbleEffect() {
    setStroke(new WobbleStroke(null));
  }
  
  public float getDetail() {
    return this.detail;
  }
  
  public void setDetail(float detail) {
    this.detail = detail;
  }
  
  public float getAmplitude() {
    return this.amplitude;
  }
  
  public void setAmplitude(float amplitude) {
    this.amplitude = amplitude;
  }
  
  public OutlineWobbleEffect(int width, Color color) {
    super(width, color);
  }
  
  public String toString() {
    return "Outline (Wobble)";
  }
  
  public List getValues() {
    List<ConfigurableEffect.Value> values = super.getValues();
    values.remove(2);
    values.add(EffectUtil.floatValue("Detail", this.detail, 1.0F, 50.0F, "This setting controls how detailed the outline will be. Smaller numbers cause the outline to have more detail."));
    values.add(EffectUtil.floatValue("Amplitude", this.amplitude, 0.5F, 50.0F, "This setting controls the amplitude of the outline."));
    return values;
  }
  
  public void setValues(List values) {
    super.setValues(values);
    for (Iterator<ConfigurableEffect.Value> iter = values.iterator(); iter.hasNext(); ) {
      ConfigurableEffect.Value value = iter.next();
      if (value.getName().equals("Detail")) {
        this.detail = ((Float)value.getObject()).floatValue();
        continue;
      } 
      if (value.getName().equals("Amplitude"))
        this.amplitude = ((Float)value.getObject()).floatValue(); 
    } 
  }
  
  private class WobbleStroke implements Stroke {
    private static final float FLATNESS = 1.0F;
    
    private WobbleStroke() {}
    
    public Shape createStrokedShape(Shape shape) {
      GeneralPath result = new GeneralPath();
      shape = (new BasicStroke(OutlineWobbleEffect.this.getWidth(), 2, OutlineWobbleEffect.this.getJoin())).createStrokedShape(shape);
      PathIterator it = new FlatteningPathIterator(shape.getPathIterator(null), 1.0D);
      float[] points = new float[6];
      float moveX = 0.0F, moveY = 0.0F;
      float lastX = 0.0F, lastY = 0.0F;
      float thisX = 0.0F, thisY = 0.0F;
      int type = 0;
      float next = 0.0F;
      while (!it.isDone()) {
        float dx, dy, distance;
        type = it.currentSegment(points);
        switch (type) {
          case 0:
            moveX = lastX = randomize(points[0]);
            moveY = lastY = randomize(points[1]);
            result.moveTo(moveX, moveY);
            next = 0.0F;
            break;
          case 4:
            points[0] = moveX;
            points[1] = moveY;
          case 1:
            thisX = randomize(points[0]);
            thisY = randomize(points[1]);
            dx = thisX - lastX;
            dy = thisY - lastY;
            distance = (float)Math.sqrt((dx * dx + dy * dy));
            if (distance >= next) {
              float r = 1.0F / distance;
              while (distance >= next) {
                float x = lastX + next * dx * r;
                float y = lastY + next * dy * r;
                result.lineTo(randomize(x), randomize(y));
                next += OutlineWobbleEffect.this.detail;
              } 
            } 
            next -= distance;
            lastX = thisX;
            lastY = thisY;
            break;
        } 
        it.next();
      } 
      return result;
    }
    
    private float randomize(float x) {
      return x + (float)Math.random() * OutlineWobbleEffect.this.amplitude * 2.0F - 1.0F;
    }
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\org\newdawn\slick\font\effects\OutlineWobbleEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */