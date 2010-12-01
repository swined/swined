package net.swined.prime.binary;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Var implements IExpression {

  private final int name;
  private final boolean negative;
  private final Var neg;
  
  private Var(Var neg) {
    this.name = neg.name;
    this.negative = !neg.negative;
    this.neg = neg;
  }
  
  public Var(int name, boolean negative) {
    this.name = name;
    this.negative = negative;
    this.neg = new Var(this);
  }

  @Override
  public IExpression and(IExpression e) {
	e = e.sub(this, Const.ONE, new HashMap<IExpression, IExpression>());
	if (e instanceof Const)
		return e.and(this);
    return new And(this, e, null);
  }

  @Override
  public IExpression or(IExpression e) {
	if (e instanceof Const)
		return e.or(this);
	if (neg.equals(e))
		return Const.ONE;
    return new Or(this, e, null);
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
	public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
		if (v.name == name) {
			return v.negative == negative ? Const.ONE : Const.ZERO;
		} else {
			return this;			
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Var))
			return false;
		Var v = (Var)o;
		return (v.negative == negative) && (v.name == name);
	}

  @Override
  public void getVars(Set<Var> vars) {
    vars.add(this);
  }
	
}
