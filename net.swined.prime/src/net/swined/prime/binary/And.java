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
    return new And(this, e);
  }

  @Override
  public IExpression or(IExpression e) {
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

