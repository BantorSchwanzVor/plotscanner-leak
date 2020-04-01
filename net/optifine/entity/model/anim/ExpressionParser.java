package net.optifine.entity.model.anim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import optifine.Config;

public class ExpressionParser {
  private IModelResolver modelResolver;
  
  public ExpressionParser(IModelResolver modelResolver) {
    this.modelResolver = modelResolver;
  }
  
  public IExpression parse(String str) throws ParseException {
    try {
      Token[] atoken = TokenParser.parse(str);
      if (atoken == null)
        return null; 
      Deque<Token> deque = new ArrayDeque<>(Arrays.asList(atoken));
      return parseInfix(deque);
    } catch (IOException ioexception) {
      throw new ParseException(ioexception.getMessage(), ioexception);
    } 
  }
  
  private IExpression parseInfix(Deque<Token> deque) throws ParseException {
    if (deque.isEmpty())
      return null; 
    List<IExpression> list = new LinkedList<>();
    List<Token> list1 = new LinkedList<>();
    IExpression iexpression = parseExpression(deque);
    checkNull(iexpression, "Missing expression");
    list.add(iexpression);
    while (true) {
      Token token = deque.poll();
      if (token == null)
        return makeInfix(list, list1); 
      if (token.getType() != EnumTokenType.OPERATOR)
        throw new ParseException("Invalid operator: " + token); 
      IExpression iexpression1 = parseExpression(deque);
      checkNull(iexpression1, "Missing expression");
      list1.add(token);
      list.add(iexpression1);
    } 
  }
  
  private IExpression makeInfix(List<IExpression> listExpr, List<Token> listOper) throws ParseException {
    List<EnumFunctionType> list = new LinkedList<>();
    for (Token token : listOper) {
      EnumFunctionType enumfunctiontype = EnumFunctionType.parse(token.getText());
      checkNull(enumfunctiontype, "Invalid operator: " + token);
      list.add(enumfunctiontype);
    } 
    return makeInfixFunc(listExpr, list);
  }
  
  private IExpression makeInfixFunc(List<IExpression> listExpr, List<EnumFunctionType> listFunc) throws ParseException {
    if (listExpr.size() != listFunc.size() + 1)
      throw new ParseException("Invalid infix expression, expressions: " + listExpr.size() + ", operators: " + listFunc.size()); 
    if (listExpr.size() == 1)
      return listExpr.get(0); 
    int i = Integer.MAX_VALUE;
    int j = Integer.MIN_VALUE;
    for (EnumFunctionType enumfunctiontype : listFunc) {
      i = Math.min(enumfunctiontype.getPrecedence(), i);
      j = Math.max(enumfunctiontype.getPrecedence(), j);
    } 
    if (j >= i && j - i <= 10) {
      for (int k = j; k >= i; k--)
        mergeOperators(listExpr, listFunc, k); 
      if (listExpr.size() == 1 && listFunc.size() == 0)
        return listExpr.get(0); 
      throw new ParseException("Error merging operators, expressions: " + listExpr.size() + ", operators: " + listFunc.size());
    } 
    throw new ParseException("Invalid infix precedence, min: " + i + ", max: " + j);
  }
  
  private void mergeOperators(List<IExpression> listExpr, List<EnumFunctionType> listFuncs, int precedence) {
    for (int i = 0; i < listFuncs.size(); i++) {
      EnumFunctionType enumfunctiontype = listFuncs.get(i);
      if (enumfunctiontype.getPrecedence() == precedence) {
        listFuncs.remove(i);
        IExpression iexpression = listExpr.remove(i);
        IExpression iexpression1 = listExpr.remove(i);
        IExpression iexpression2 = new Function(enumfunctiontype, new IExpression[] { iexpression, iexpression1 });
        listExpr.add(i, iexpression2);
        i--;
      } 
    } 
  }
  
  private IExpression parseExpression(Deque<Token> deque) throws ParseException {
    EnumFunctionType enumfunctiontype, enumfunctiontype1;
    Token token = deque.poll();
    checkNull(token, "Missing expression");
    switch (token.getType()) {
      case CONSTANT:
        return makeConstant(token);
      case IDENTIFIER:
        enumfunctiontype = getFunctionType(token, deque);
        if (enumfunctiontype != null)
          return makeFunction(enumfunctiontype, deque); 
        return makeVariable(token);
      case BRACKET_OPEN:
        return makeBracketed(token, deque);
      case OPERATOR:
        enumfunctiontype1 = EnumFunctionType.parse(token.getText());
        checkNull(enumfunctiontype1, "Invalid operator: " + token);
        if (enumfunctiontype1 == EnumFunctionType.PLUS)
          return parseExpression(deque); 
        if (enumfunctiontype1 == EnumFunctionType.MINUS) {
          IExpression iexpression = parseExpression(deque);
          return new Function(EnumFunctionType.NEG, new IExpression[] { iexpression });
        } 
        break;
    } 
    throw new ParseException("Invalid expression: " + token);
  }
  
  private static IExpression makeConstant(Token token) throws ParseException {
    float f = Config.parseFloat(token.getText(), Float.NaN);
    if (f == Float.NaN)
      throw new ParseException("Invalid float value: " + token); 
    return new Constant(f);
  }
  
  private EnumFunctionType getFunctionType(Token token, Deque<Token> deque) throws ParseException {
    Token token1 = deque.peek();
    if (token1 != null && token1.getType() == EnumTokenType.BRACKET_OPEN) {
      EnumFunctionType enumfunctiontype1 = EnumFunctionType.parse(token1.getText());
      checkNull(enumfunctiontype1, "Unknown function: " + token1);
      return enumfunctiontype1;
    } 
    EnumFunctionType enumfunctiontype = EnumFunctionType.parse(token1.getText());
    if (enumfunctiontype == null)
      return null; 
    if (enumfunctiontype.getCountArguments() > 0)
      throw new ParseException("Missing arguments: " + enumfunctiontype); 
    return enumfunctiontype;
  }
  
  private IExpression makeFunction(EnumFunctionType type, Deque<Token> deque) throws ParseException {
    if (type.getCountArguments() == 0)
      return makeFunction(type, new IExpression[0]); 
    Token token = deque.poll();
    Deque<Token> deque1 = getGroup(deque, EnumTokenType.BRACKET_CLOSE, true);
    IExpression[] aiexpression = parseExpressions(deque1);
    return makeFunction(type, aiexpression);
  }
  
  private IExpression[] parseExpressions(Deque<Token> deque) throws ParseException {
    List<IExpression> list = new ArrayList<>();
    while (true) {
      Deque<Token> deque1 = getGroup(deque, EnumTokenType.COMMA, false);
      IExpression iexpression = parseInfix(deque1);
      if (iexpression == null) {
        IExpression[] aiexpression = list.<IExpression>toArray(new IExpression[list.size()]);
        return aiexpression;
      } 
      list.add(iexpression);
    } 
  }
  
  private static IExpression makeFunction(EnumFunctionType type, IExpression[] exprs) throws ParseException {
    if (type.getCountArguments() != exprs.length)
      throw new ParseException("Invalid number of arguments: " + exprs.length + ", should be: " + type.getCountArguments() + ", function: " + type.getName()); 
    return new Function(type, exprs);
  }
  
  private IExpression makeVariable(Token token) throws ParseException {
    if (this.modelResolver == null)
      throw new ParseException("Model variable not found: " + token); 
    IExpression iexpression = this.modelResolver.getExpression(token.getText());
    if (iexpression == null)
      throw new ParseException("Model variable not found: " + token); 
    return iexpression;
  }
  
  private IExpression makeBracketed(Token token, Deque<Token> deque) throws ParseException {
    Deque<Token> deque1 = getGroup(deque, EnumTokenType.BRACKET_CLOSE, true);
    return parseInfix(deque1);
  }
  
  private static Deque<Token> getGroup(Deque<Token> deque, EnumTokenType tokenTypeEnd, boolean tokenEndRequired) throws ParseException {
    Deque<Token> deque1 = new ArrayDeque<>();
    int i = 0;
    Iterator<Token> iterator = deque1.iterator();
    while (iterator.hasNext()) {
      Token token = iterator.next();
      iterator.remove();
      if (i == 0 && token.getType() == tokenTypeEnd)
        return deque1; 
      deque1.add(token);
      if (token.getType() == EnumTokenType.BRACKET_OPEN)
        i++; 
      if (token.getType() == EnumTokenType.BRACKET_CLOSE)
        i--; 
    } 
    if (tokenEndRequired)
      throw new ParseException("Missing end token: " + tokenTypeEnd); 
    return deque1;
  }
  
  private static void checkNull(Object obj, String message) throws ParseException {
    if (obj == null)
      throw new ParseException(message); 
  }
  
  public static void main(String[] args) throws Exception {
    ExpressionParser expressionparser = new ExpressionParser(null);
    while (true) {
      try {
        InputStreamReader inputstreamreader = new InputStreamReader(System.in);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        String s = bufferedreader.readLine();
        if (s.length() <= 0)
          return; 
        IExpression iexpression = expressionparser.parse(s);
        float f = iexpression.eval();
        Config.dbg(s + " = " + f);
      } catch (Exception exception) {
        exception.printStackTrace();
      } 
    } 
  }
}


/* Location:              C:\Users\BSV\AppData\Local\Temp\Rar$DRa6216.20396\Preview\Preview.jar!\net\optifine\entity\model\anim\ExpressionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */