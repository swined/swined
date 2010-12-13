package net.swined.prime.binary;

import java.util.Map;
import java.util.Set;

public class Not implements IExpression {

  private final IExpression x;
  
  public Not(IExpression x) {
    this.x = x;
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
    return x;
  }

  @Override
  public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
    return x.sub(v, c, map).not();
  }

  @Override
  public void getVars(Set<Var> vars) {
    x.getVars(vars);
  }

}
