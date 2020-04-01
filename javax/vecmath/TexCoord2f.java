package javax.vecmath;

import java.io.Serializable;

public class TexCoord2f extends Tuple2f implements Serializable {
  static final long serialVersionUID = 7998248474800032487L;
  
  public TexCoord2f(float x, float y) {
    super(x, y);
  }
  
  public TexCoord2f(float[] v) {
    super(v);
  }
  
  public TexCoord2f(TexCoord2f v1) {
    super(v1);
  }
  
  public TexCoord2f(Tuple2f t1) {
    super(t1);
  }
  
  public TexCoord2f() {}
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\javax\vecmath\TexCoord2f.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */