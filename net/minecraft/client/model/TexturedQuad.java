package net.minecraft.client.model;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import optifine.Config;
import shadersmod.client.SVertexFormat;

public class TexturedQuad {
  public PositionTextureVertex[] vertexPositions;
  
  public int nVertices;
  
  private boolean invertNormal;
  
  public TexturedQuad(PositionTextureVertex[] vertices) {
    this.vertexPositions = vertices;
    this.nVertices = vertices.length;
  }
  
  public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight) {
    this(vertices);
    float f = 0.0F / textureWidth;
    float f1 = 0.0F / textureHeight;
    vertices[0] = vertices[0].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV1 / textureHeight + f1);
    vertices[1] = vertices[1].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV1 / textureHeight + f1);
    vertices[2] = vertices[2].setTexturePosition(texcoordU1 / textureWidth + f, texcoordV2 / textureHeight - f1);
    vertices[3] = vertices[3].setTexturePosition(texcoordU2 / textureWidth - f, texcoordV2 / textureHeight - f1);
  }
  
  public void flipFace() {
    PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
    for (int i = 0; i < this.vertexPositions.length; i++)
      apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1]; 
    this.vertexPositions = apositiontexturevertex;
  }
  
  public void draw(BufferBuilder renderer, float scale) {
    Vec3d vec3d = (this.vertexPositions[1]).vector3D.subtractReverse((this.vertexPositions[0]).vector3D);
    Vec3d vec3d1 = (this.vertexPositions[1]).vector3D.subtractReverse((this.vertexPositions[2]).vector3D);
    Vec3d vec3d2 = vec3d1.crossProduct(vec3d).normalize();
    float f = (float)vec3d2.xCoord;
    float f1 = (float)vec3d2.yCoord;
    float f2 = (float)vec3d2.zCoord;
    if (this.invertNormal) {
      f = -f;
      f1 = -f1;
      f2 = -f2;
    } 
    if (Config.isShaders()) {
      renderer.begin(7, SVertexFormat.defVertexFormatTextured);
    } else {
      renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
    } 
    for (int i = 0; i < 4; i++) {
      PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
      renderer.pos(positiontexturevertex.vector3D.xCoord * scale, positiontexturevertex.vector3D.yCoord * scale, positiontexturevertex.vector3D.zCoord * scale).tex(positiontexturevertex.texturePositionX, positiontexturevertex.texturePositionY).normal(f, f1, f2).endVertex();
    } 
    Tessellator.getInstance().draw();
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\minecraft\client\model\TexturedQuad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */