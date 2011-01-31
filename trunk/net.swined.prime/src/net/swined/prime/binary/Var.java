package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public class Var implements IExpression {

	public final int name;
	
	public Var(int name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "x" + name;
	}

	@Override
	public BigInteger getVars() {
	  return BigInteger.ZERO.setBit(name);
	}
	
	@Override
	public IExpression sub(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		if (v == name) {
			return c;
		} else {
			return this;
		}
	}
	
}
