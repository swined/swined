package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;

public class Var implements IExpression {

	public final int name;
	public final boolean sign;
	
	public Var(int name, boolean sign) {
		this.name = name;
		this.sign = sign;
	}

	@Override
	public String toString() {
		return (sign ? "!" : "") + "x" + name;
	}

	@Override
	public BigInteger getVars() {
	  return BigInteger.ZERO.setBit(name);
	}
	
	@Override
	public IExpression sub(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		if (v == name) {
			return sign ? c.not() : c;
		} else {
			return this;
		}
	}
	
}
