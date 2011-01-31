package net.swined.prime.nodes;

import java.math.BigInteger;
import java.util.Map;

public class Var extends Expression {

	public final int name;
	
	public Var(int name) {
		super(BigInteger.ZERO.setBit(name));
		this.name = name;
	}

	@Override
	public String toString() {
		return "x" + name;
	}

	@Override
	protected IExpression subImpl(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		if (v == name) {
			return c;
		} else {
			return this;
		}
	}
	
}
