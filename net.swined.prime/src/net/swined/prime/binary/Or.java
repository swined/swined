package net.swined.prime.binary;


public class Or implements IExpression {

  private final IExpression a;
  private final IExpression b;
  private final IExpression not;
  
  public Or(IExpression a, IExpression b, IExpression not) {
	if (a instanceof Const || b instanceof Const)
		throw new IllegalArgumentException();
    this.a = a;
    this.b = b;
    this.not = not == null ? new And(a.not(), b.not(), this) : not;
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
    return "(" + a + " | " + b + ")";
  }

	@Override
	public IExpression sub(Var v, Const c) {
		IExpression sa = a.sub(v, c);
		IExpression sb = b.sub(v, c);
		if (sa != a || sb != b)
			return sa.or(sb);
		else
			return this;
	}
  
}
