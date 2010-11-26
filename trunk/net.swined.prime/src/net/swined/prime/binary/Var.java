package net.swined.prime.binary;

public class Var implements IExpression {

  private final String name;
  private final boolean negative;
  private final Var neg;
  
  private Var(Var neg) {
    this.name = neg.name;
    this.negative = !neg.negative;
    this.neg = neg;
  }
  
  public Var(String name, boolean negative) {
    this.name = name;
    this.negative = negative;
    this.neg = new Var(this);
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
  public Var not() {
    return neg;
  }

  @Override
  public String toString() {
    return (negative ? "!" : "") + name;
  }

	@Override
	public IExpression sub(Var v, Const c) {
		if (v.name.equals(name)) {
			return v.negative == negative ? Const.ONE : Const.ZERO;
		} else {
			return this;			
		}
	}
  
}
