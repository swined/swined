package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class Var implements IExpression {

	public final int name;
	public final boolean sign;
	public final Var not;
	
	public Var(int name, boolean sign) {
		this.name = name;
		this.sign = sign;
		this.not = new Var(this);
	}

	private Var(Var not) {
    this.name = not.name;
    this.sign = !not.sign;
    this.not = not;
	}
	
	@Override
	public String toString() {
		return (sign ? "X" : "x") + name;
	}

	@Override
	public BigInteger getVars() {
	  return BigInteger.ZERO.setBit(name);
	}
	
	@Override
	public IExpression sub(int v, IExpression c,
			Map<IExpression, IExpression> ctx) {
		if (v == name) {
			return sign ? c.not() : c;
		} else {
			return this;
		}
	}

  @Override
  public IExpression not() {
    return not;
  }
	
}
