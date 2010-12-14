package net.swined.prime.binary;

public class Var extends Expression {

  public final String name;
  
  public Var(String name) {
    this.name = name;
  }
  
  @Override
  public IExpression not() {
    return new Not(this);
  }

  @Override
  public IExpression sub(Var v, Const c, SubContext ctx) {
    if (name.equals(v.name))
      return c;
    else
      return this;
  }
  
  @Override
  public String toString() {
    return name;
  }

  @Override
  public Var getVar() {
    return this;
  }
  
}
