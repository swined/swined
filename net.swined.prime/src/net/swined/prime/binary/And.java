package net.swined.prime.binary;

import java.util.Map;
import java.util.Set;

public class And implements IExpression {

  private final IExpression a;
  private final IExpression b;
  
  public And(IExpression a, IExpression b) {
    if (a instanceof Const || b instanceof Const)
      throw new IllegalArgumentException();
    this.a = a;
    this.b = b;
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
    IExpression sub = map.get(this);
    if (sub == null) {
      IExpression sa = a.sub(v, c, map);
      IExpression sb = b.sub(v, c, map);
      sub = sa.and(sb);
      map.put(this, sub);
    }
    return sub;    
  }

  @Override
  public void getVars(Set<Var> vars) {
    a.getVars(vars);
    b.getVars(vars);
  }

  @Override
  public String toString() {
    return "(" + a + " & " + b + ")";
  }

  @Override
  public Var getVar() {
    Var va = a.getVar();
    if (va != null)
      return va;
    Var vb = b.getVar();
    if (vb != null)
      return vb;
    return null;
  }
  
}
