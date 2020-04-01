package shadersmod.client;

import org.lwjgl.opengl.ARBShaderObjects;

public class ShaderProgramData {
  public int programIDGL;
  
  public int uniform_texture;
  
  public int uniform_lightmap;
  
  public int uniform_normals;
  
  public int uniform_specular;
  
  public int uniform_shadow;
  
  public int uniform_watershadow;
  
  public int uniform_shadowtex0;
  
  public int uniform_shadowtex1;
  
  public int uniform_depthtex0;
  
  public int uniform_depthtex1;
  
  public int uniform_shadowcolor;
  
  public int uniform_shadowcolor0;
  
  public int uniform_shadowcolor1;
  
  public int uniform_noisetex;
  
  public int uniform_gcolor;
  
  public int uniform_gdepth;
  
  public int uniform_gnormal;
  
  public int uniform_composite;
  
  public int uniform_gaux1;
  
  public int uniform_gaux2;
  
  public int uniform_gaux3;
  
  public int uniform_gaux4;
  
  public int uniform_colortex0;
  
  public int uniform_colortex1;
  
  public int uniform_colortex2;
  
  public int uniform_colortex3;
  
  public int uniform_colortex4;
  
  public int uniform_colortex5;
  
  public int uniform_colortex6;
  
  public int uniform_colortex7;
  
  public int uniform_gdepthtex;
  
  public int uniform_depthtex2;
  
  public int uniform_tex;
  
  public int uniform_heldItemId;
  
  public int uniform_heldBlockLightValue;
  
  public int uniform_fogMode;
  
  public int uniform_fogColor;
  
  public int uniform_skyColor;
  
  public int uniform_worldTime;
  
  public int uniform_moonPhase;
  
  public int uniform_frameTimeCounter;
  
  public int uniform_sunAngle;
  
  public int uniform_shadowAngle;
  
  public int uniform_rainStrength;
  
  public int uniform_aspectRatio;
  
  public int uniform_viewWidth;
  
  public int uniform_viewHeight;
  
  public int uniform_near;
  
  public int uniform_far;
  
  public int uniform_sunPosition;
  
  public int uniform_moonPosition;
  
  public int uniform_upPosition;
  
  public int uniform_previousCameraPosition;
  
  public int uniform_cameraPosition;
  
  public int uniform_gbufferModelView;
  
  public int uniform_gbufferModelViewInverse;
  
  public int uniform_gbufferPreviousProjection;
  
  public int uniform_gbufferProjection;
  
  public int uniform_gbufferProjectionInverse;
  
  public int uniform_gbufferPreviousModelView;
  
  public int uniform_shadowProjection;
  
  public int uniform_shadowProjectionInverse;
  
  public int uniform_shadowModelView;
  
  public int uniform_shadowModelViewInverse;
  
  public int uniform_wetness;
  
  public int uniform_eyeAltitude;
  
  public int uniform_eyeBrightness;
  
  public int uniform_eyeBrightnessSmooth;
  
  public int uniform_terrainTextureSize;
  
  public int uniform_terrainIconSize;
  
  public int uniform_isEyeInWater;
  
  public int uniform_hideGUI;
  
  public int uniform_centerDepthSmooth;
  
  public int uniform_atlasSize;
  
  public ShaderProgramData(int programID) {
    this.programIDGL = programID;
    this.uniform_texture = ARBShaderObjects.glGetUniformLocationARB(programID, "texture");
    this.uniform_lightmap = ARBShaderObjects.glGetUniformLocationARB(programID, "lightmap");
    this.uniform_normals = ARBShaderObjects.glGetUniformLocationARB(programID, "normals");
    this.uniform_specular = ARBShaderObjects.glGetUniformLocationARB(programID, "specular");
    this.uniform_shadow = ARBShaderObjects.glGetUniformLocationARB(programID, "shadow");
    this.uniform_watershadow = ARBShaderObjects.glGetUniformLocationARB(programID, "watershadow");
    this.uniform_shadowtex0 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowtex0");
    this.uniform_shadowtex1 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowtex1");
    this.uniform_depthtex0 = ARBShaderObjects.glGetUniformLocationARB(programID, "depthtex0");
    this.uniform_depthtex1 = ARBShaderObjects.glGetUniformLocationARB(programID, "depthtex1");
    this.uniform_shadowcolor = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowcolor");
    this.uniform_shadowcolor0 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowcolor0");
    this.uniform_shadowcolor1 = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowcolor1");
    this.uniform_noisetex = ARBShaderObjects.glGetUniformLocationARB(programID, "noisetex");
    this.uniform_gcolor = ARBShaderObjects.glGetUniformLocationARB(programID, "gcolor");
    this.uniform_gdepth = ARBShaderObjects.glGetUniformLocationARB(programID, "gdepth");
    this.uniform_gnormal = ARBShaderObjects.glGetUniformLocationARB(programID, "gnormal");
    this.uniform_composite = ARBShaderObjects.glGetUniformLocationARB(programID, "composite");
    this.uniform_gaux1 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux1");
    this.uniform_gaux2 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux2");
    this.uniform_gaux3 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux3");
    this.uniform_gaux4 = ARBShaderObjects.glGetUniformLocationARB(programID, "gaux4");
    this.uniform_colortex0 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex0");
    this.uniform_colortex1 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex1");
    this.uniform_colortex2 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex2");
    this.uniform_colortex3 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex3");
    this.uniform_colortex4 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex4");
    this.uniform_colortex5 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex5");
    this.uniform_colortex6 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex6");
    this.uniform_colortex7 = ARBShaderObjects.glGetUniformLocationARB(programID, "colortex7");
    this.uniform_gdepthtex = ARBShaderObjects.glGetUniformLocationARB(programID, "gdepthtex");
    this.uniform_depthtex2 = ARBShaderObjects.glGetUniformLocationARB(programID, "depthtex2");
    this.uniform_tex = ARBShaderObjects.glGetUniformLocationARB(programID, "tex");
    this.uniform_heldItemId = ARBShaderObjects.glGetUniformLocationARB(programID, "heldItemId");
    this.uniform_heldBlockLightValue = ARBShaderObjects.glGetUniformLocationARB(programID, "heldBlockLightValue");
    this.uniform_fogMode = ARBShaderObjects.glGetUniformLocationARB(programID, "fogMode");
    this.uniform_fogColor = ARBShaderObjects.glGetUniformLocationARB(programID, "fogColor");
    this.uniform_skyColor = ARBShaderObjects.glGetUniformLocationARB(programID, "skyColor");
    this.uniform_worldTime = ARBShaderObjects.glGetUniformLocationARB(programID, "worldTime");
    this.uniform_moonPhase = ARBShaderObjects.glGetUniformLocationARB(programID, "moonPhase");
    this.uniform_frameTimeCounter = ARBShaderObjects.glGetUniformLocationARB(programID, "frameTimeCounter");
    this.uniform_sunAngle = ARBShaderObjects.glGetUniformLocationARB(programID, "sunAngle");
    this.uniform_shadowAngle = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowAngle");
    this.uniform_rainStrength = ARBShaderObjects.glGetUniformLocationARB(programID, "rainStrength");
    this.uniform_aspectRatio = ARBShaderObjects.glGetUniformLocationARB(programID, "aspectRatio");
    this.uniform_viewWidth = ARBShaderObjects.glGetUniformLocationARB(programID, "viewWidth");
    this.uniform_viewHeight = ARBShaderObjects.glGetUniformLocationARB(programID, "viewHeight");
    this.uniform_near = ARBShaderObjects.glGetUniformLocationARB(programID, "near");
    this.uniform_far = ARBShaderObjects.glGetUniformLocationARB(programID, "far");
    this.uniform_sunPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "sunPosition");
    this.uniform_moonPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "moonPosition");
    this.uniform_upPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "upPosition");
    this.uniform_previousCameraPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "previousCameraPosition");
    this.uniform_cameraPosition = ARBShaderObjects.glGetUniformLocationARB(programID, "cameraPosition");
    this.uniform_gbufferModelView = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferModelView");
    this.uniform_gbufferModelViewInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferModelViewInverse");
    this.uniform_gbufferPreviousProjection = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferPreviousProjection");
    this.uniform_gbufferProjection = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferProjection");
    this.uniform_gbufferProjectionInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferProjectionInverse");
    this.uniform_gbufferPreviousModelView = ARBShaderObjects.glGetUniformLocationARB(programID, "gbufferPreviousModelView");
    this.uniform_shadowProjection = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowProjection");
    this.uniform_shadowProjectionInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowProjectionInverse");
    this.uniform_shadowModelView = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowModelView");
    this.uniform_shadowModelViewInverse = ARBShaderObjects.glGetUniformLocationARB(programID, "shadowModelViewInverse");
    this.uniform_wetness = ARBShaderObjects.glGetUniformLocationARB(programID, "wetness");
    this.uniform_eyeAltitude = ARBShaderObjects.glGetUniformLocationARB(programID, "eyeAltitude");
    this.uniform_eyeBrightness = ARBShaderObjects.glGetUniformLocationARB(programID, "eyeBrightness");
    this.uniform_eyeBrightnessSmooth = ARBShaderObjects.glGetUniformLocationARB(programID, "eyeBrightnessSmooth");
    this.uniform_terrainTextureSize = ARBShaderObjects.glGetUniformLocationARB(programID, "terrainTextureSize");
    this.uniform_terrainIconSize = ARBShaderObjects.glGetUniformLocationARB(programID, "terrainIconSize");
    this.uniform_isEyeInWater = ARBShaderObjects.glGetUniformLocationARB(programID, "isEyeInWater");
    this.uniform_hideGUI = ARBShaderObjects.glGetUniformLocationARB(programID, "hideGUI");
    this.uniform_centerDepthSmooth = ARBShaderObjects.glGetUniformLocationARB(programID, "centerDepthSmooth");
    this.uniform_atlasSize = ARBShaderObjects.glGetUniformLocationARB(programID, "atlasSize");
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\shadersmod\client\ShaderProgramData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */