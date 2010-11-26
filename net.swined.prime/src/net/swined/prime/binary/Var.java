package net.swined.prime.binary;

public class Var implements IExpression {

  private final String name;
  private final boolean negative;
  
  public Var(String name, boolean negative) {
    this.name = name;
    this.negative = negative;
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
    return new Var(name, !negative);
  }

  @Override
  public String toString() {
    return (negative ? "!" : "") + name;
  }
  
}
