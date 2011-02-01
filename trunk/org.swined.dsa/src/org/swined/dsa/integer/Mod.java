package org.swined.dsa.integer;

import java.math.BigInteger;

import org.swined.dsa.IExpression;
import org.swined.dsa.Int;

public class Mod implements IInteger {

	private final IInteger a;
	private final BigInteger m;
	
	public Mod(IInteger a, BigInteger m) {
		this.a = a;
		this.m = m;
	}
	
	@Override
	public String toString() {
		//return "" + a + " mod " + m;
		return "" + a + " mod C";
	}

	@Override
	public IExpression[] getBits() {
		return Int.mod(a.getBits(), m);
	}
	
}
