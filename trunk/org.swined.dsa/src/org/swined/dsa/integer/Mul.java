package org.swined.dsa.integer;

import org.swined.dsa.IExpression;
import org.swined.dsa.Int;

public class Mul implements IInteger {

	private final IInteger a;
	private final IInteger b;
	
	public Mul(IInteger a, IInteger b) {
		this.a = a;
		this.b = b;
	}
	
	public String toString() {
		return "(" + a + " * " + b + ")";
	}

	@Override
	public IExpression[] getBits() {
		return Int.mul(a.getBits(), b.getBits());
	}
}
