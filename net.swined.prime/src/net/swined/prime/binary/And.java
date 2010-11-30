package net.swined.prime.binary;

import java.util.Map;


public class And implements IExpression {

  private final IExpression a;
  private final IExpression b;
  private final IExpression not;
  
  public And(IExpression a, IExpression b, IExpression not) {
	if (a instanceof Const || b instanceof Const)
		throw new IllegalArgumentException();
    this.a = a;
    this.b = b;
    this.not = not == null ? new Or(a.not(), b.not(), this) : not;
  }

  @Override
  public IExpression and(IExpression e) {
    if (e.equals(Const.ZERO))
      return Const.ZERO;
    if (e.equals(Const.ONE))
      return this;
    if (e instanceof Var)
    	return e.and(this);
    return new And(this, e, null);
  }

  @Override
  public IExpression or(IExpression e) {
    if (e.equals(Const.ZERO))
      return this;
    if (e.equals(Const.ONE))
      return Const.ONE;
    return new Or(this, e, null);
  }

  @Override
  public IExpression not() {
    return not;
  }

  @Override
  public String toString() {
    return "(" + a + " & " + b + ")";
  }

	@Override
	public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
	  IExpression sub = map.get(this);
	  if (sub == null) {
  		IExpression sa = a.sub(v, c, map);
  		IExpression sb = b.sub(v, c, map);
  		if (sa != a || sb != b)
  			sub = sa.and(sb);
  		else
  			sub = this;
  		map.put(this, sub);
	  }
	  return sub;
	}

}

