package net.swined.prime.binary;

import java.util.Map;
import java.util.Set;


public class Or implements IExpression {

  private final IExpression a;
  private final IExpression b;
  
  public Or(IExpression a, IExpression b) {
    if (a instanceof Const || b instanceof Const)
      throw new IllegalArgumentException();
    this.a = a;
    this.b = b;
  }

  @Override
  public IExpression and(IExpression e) {
    if (e.equals(Const.ZERO))
      return Const.ZERO;
    if (e.equals(Const.ONE))
      return this;
    return a.and(e).or(b.and(e));
  }

  @Override
  public IExpression or(IExpression e) {
    if (e.equals(Const.ZERO))
      return this;
    if (e.equals(Const.ONE))
      return Const.ONE;
    return new Or(this, e);
  }

  @Override
  public IExpression not() {
    return a.not().and(b.not());
  }
  
  @Override
  public String toString() {
    return "(" + a + " | " + b + ")";
  }

	@Override
	public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
	  IExpression sub = map.get(this);
	  if (sub == null) {
  		IExpression sa = a.sub(v, c, map);
  		IExpression sb = b.sub(v, c, map);
			sub = sa.or(sb);
  		map.put(this, sub);
	  }
	  return sub;
	}

  @Override
  public void getVars(Set<Var> vars) {
    a.getVars(vars);
    b.getVars(vars);
  }
	
}
