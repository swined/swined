package net.swined.prime.binary;

import java.math.BigInteger;

public class Var extends Expression {

	public final boolean sign;
	public final int name;
	
	public Var(int name, boolean sign) {
		super(BigInteger.ONE, BigInteger.ZERO.setBit(name));
		this.name = name;
		this.sign = sign;
	}

	@Override
	protected IExpression notImpl() {
		return new Var(name, !sign);
	}

	@Override
	public String toString() {
		return (sign ? "!" : "") + "x" + name;
	}
	
}
