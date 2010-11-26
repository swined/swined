package net.swined.prime.binary;

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
	public IExpression sub(Var v, Const c) {
		return a.sub(v, c).and(b.sub(v, c));
	}
  
}

