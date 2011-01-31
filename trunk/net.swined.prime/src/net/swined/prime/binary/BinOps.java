package net.swined.prime.binary;

public class BinOps {

  public static IExpression and(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).and(b);
    if (b instanceof Const)
      return ((Const) b).and(a);
    return new And(a, b);
  }

  public static IExpression or(IExpression a, IExpression b) {
    if (a instanceof Const)
      return ((Const) a).or(b);
    if (b instanceof Const)
      return ((Const) b).or(a);
    return new Or(a, b);
  }
  
  public static IExpression not(IExpression a) {
    if (a instanceof Const)
      return ((Const) a).not();
    return new Not(a);
  }
  
  public static IExpression xor(IExpression a, IExpression b) {
    return or(and(a, not(b)), and(not(a), b));
  }
  
}
