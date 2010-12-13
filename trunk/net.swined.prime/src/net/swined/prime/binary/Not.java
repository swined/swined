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
    if (e instanceof Const)
      return e.or(this);
    return new Or(this, e);
  }

  @Override
  public IExpression not() {
    return x;
  }

  @Override
  public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
    IExpression sub = map.get(this);
    if (sub == null) {
      IExpression sx = x.sub(v, c, map);
      sub = sx.not();
      map.put(this, sub);
    }
    return sub;    
  }

  @Override
  public void getVars(Set<Var> vars) {
    x.getVars(vars);
  }

}
