package net.swined.prime.binary;

import java.util.Map;

public class Var implements IExpression {

  public final String name;
  
  public Var(String name) {
    this.name = name;
  }
  
  @Override
  public IExpression and(IExpression e) {
    if (e instanceof Const)
      return e.and(this);
    return new And(this, e);
  }

  @Override
  public IExpression or(IExpression e) {
    if (e instanceof Const)
      return e.or(this);
    return new Or(this, e);
  }

  @Override
  public IExpression not() {
    return new Not(this);
  }

  @Override
  public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
    if (equals(v))
      return c;
    else
      return this;
  }

  @Override
  public boolean equals(Object e) {
    if (this == e)
      return true;
    if (!(e instanceof Var))
      return false;
    return name.equals(((Var)e).name);
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
