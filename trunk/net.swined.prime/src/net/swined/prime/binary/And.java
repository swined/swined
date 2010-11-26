package net.swined.prime.binary;

public class And implements IExpression {

  private final IExpression a;
  private final IExpression b;
  
  public And(IExpression a, IExpression b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public IExpression and(IExpression e) {
    if (e.equals(Const.ZERO))
      return Const.ZERO;
    if (e.equals(Const.ONE))
      return this;
    return new And(this, e);
  }

  @Override
  public IExpression or(IExpression e) {
    if (e.equals(Const.ZERO))
      return this;
    if (e.equals(Const.ONE))
      return Const.ONE;
    return new Or(this, e);
  }

  @Override
  public IExpression not() {
    return new Or(a.not(), b.not());
  }

  @Override
  public String toString() {
    return "(" + a + " & " + b + ")";
  }
  
}

