package net.swined.prime.binary;

import java.util.Map;
import java.util.Set;

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
    return a.sub(v, c, map).and(b.sub(v, c, map));
  }

  @Override
  public void getVars(Set<Var> vars) {
    a.getVars(vars);
    b.getVars(vars);
  }

}
