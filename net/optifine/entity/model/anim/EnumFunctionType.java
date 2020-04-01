package net.optifine.entity.model.anim;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.MathHelper;
import optifine.Config;
import optifine.MathUtils;

public enum EnumFunctionType {
  PLUS("+", 2, 0),
  MINUS("-", 2, 0),
  MUL("*", 2, 1),
  DIV("/", 2, 1),
  MOD("%", 2, 1),
  NEG("neg", 1),
  PI("pi", 0),
  SIN("sin", 1),
  COS("cos", 1),
  TAN("tan", 1),
  ATAN("atan", 1),
  ATAN2("atan2", 2),
  TORAD("torad", 1),
  TODEG("todeg", 1),
  MIN("min", 2),
  MAX("max", 2),
  CLAMP("clamp", 3),
  ABS("abs", 1),
  FLOOR("floor", 1),
  CEIL("ceil", 1),
  FRAC("frac", 1),
  ROUND("round", 1),
  SQRT("sqrt", 1),
  FMOD("fmod", 2),
  TIME("time", 0);
  
  private String name;
  
  private int countArguments;
  
  private int precedence;
  
  public static EnumFunctionType[] VALUES;
  
  static {
    VALUES = values();
  }
  
  EnumFunctionType(String name, int countArguments) {
    this.name = name;
    this.countArguments = countArguments;
  }
  
  EnumFunctionType(String name, int countArguments, int precedence) {
    this.name = name;
    this.countArguments = countArguments;
    this.precedence = precedence;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getCountArguments() {
    return this.countArguments;
  }
  
  public int getPrecedence() {
    return this.precedence;
  }
  
  public float eval(IExpression[] arguments) {
    float f, f1, f2, f3;
    Minecraft minecraft;
    WorldClient worldClient;
    if (arguments.length != this.countArguments) {
      Config.warn("Invalid number of arguments, function: " + this + ", arguments: " + arguments.length + ", should be: " + this.countArguments);
      return 0.0F;
    } 
    switch (this) {
      case PLUS:
        return arguments[0].eval() + arguments[1].eval();
      case MINUS:
        return arguments[0].eval() - arguments[1].eval();
      case MUL:
        return arguments[0].eval() * arguments[1].eval();
      case DIV:
        return arguments[0].eval() / arguments[1].eval();
      case MOD:
        f = arguments[0].eval();
        f1 = arguments[1].eval();
        return f - f1 * (int)(f / f1);
      case NEG:
        return -arguments[0].eval();
      case PI:
        return 3.1415927F;
      case SIN:
        return MathHelper.sin(arguments[0].eval());
      case COS:
        return MathHelper.cos(arguments[0].eval());
      case TAN:
        return (float)Math.tan(arguments[0].eval());
      case ATAN:
        return (float)Math.atan(arguments[0].eval());
      case ATAN2:
        return (float)MathHelper.atan2(arguments[0].eval(), arguments[1].eval());
      case TORAD:
        return MathUtils.toRad(arguments[0].eval());
      case TODEG:
        return MathUtils.toDeg(arguments[0].eval());
      case MIN:
        return Math.min(arguments[0].eval(), arguments[1].eval());
      case MAX:
        return Math.max(arguments[0].eval(), arguments[1].eval());
      case CLAMP:
        return MathHelper.clamp(arguments[0].eval(), arguments[1].eval(), arguments[2].eval());
      case null:
        return MathHelper.abs(arguments[0].eval());
      case FLOOR:
        return MathHelper.floor(arguments[0].eval());
      case CEIL:
        return MathHelper.ceil(arguments[0].eval());
      case FRAC:
        return (float)MathHelper.frac(arguments[0].eval());
      case ROUND:
        return Math.round(arguments[0].eval());
      case SQRT:
        return MathHelper.sqrt(arguments[0].eval());
      case FMOD:
        f2 = arguments[0].eval();
        f3 = arguments[1].eval();
        return f2 - f3 * MathHelper.floor(f2 / f3);
      case TIME:
        minecraft = Minecraft.getMinecraft();
        worldClient = minecraft.world;
        if (worldClient == null)
          return 0.0F; 
        return (float)(worldClient.getTotalWorldTime() % 24000L) + minecraft.getRenderPartialTicks();
    } 
    Config.warn("Unknown function type: " + this);
    return 0.0F;
  }
  
  public static EnumFunctionType parse(String str) {
    for (int i = 0; i < VALUES.length; i++) {
      EnumFunctionType enumfunctiontype = VALUES[i];
      if (enumfunctiontype.getName().equals(str))
        return enumfunctiontype; 
    } 
    return null;
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\EnumFunctionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */