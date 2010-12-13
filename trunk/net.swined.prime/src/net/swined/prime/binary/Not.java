package net.swined.prime.binary;

import java.util.Map;

public class Not implements IExpression {

  private final IExpression x;
  
  public Not(IExpression x) {
    if (x instanceof Const)
      throw new IllegalArgumentException();
    this.x = x;
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
  public String toString() {
    return "!" + x;
  }
  
  @Override
  public Var getVar() {
    return x.getVar();
  }
  
}
