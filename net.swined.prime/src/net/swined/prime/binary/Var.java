package net.swined.prime.binary;

import java.util.Map;

public class Var extends Expression {

  public final String name;
  
  public Var(String name) {
    this.name = name;
    vars.add(this);
  }
  
  @Override
  public IExpression not() {
    return new Not(this);
  }

  @Override
  protected IExpression subImpl(Var v, Const c, Map<IExpression, IExpression> ctx) {
    if (name.equals(v.name))
      return c;
    else
      return this;
  }
  
  @Override
  public String toString() {
    return name;
  }

}
