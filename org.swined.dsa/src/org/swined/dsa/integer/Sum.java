package org.swined.dsa.integer;

import org.swined.dsa.IExpression;
import org.swined.dsa.Int;

public class Sum implements IInteger {

	private final IInteger a;
	private final IInteger b;
	
	public Sum(IInteger a, IInteger b) {
		this.a = a;
		this.b = b;
	}
	
	public String toString() {
		return "(" + a + " + " + b + ")";
	}

	@Override
	public IExpression[] getBits() {
		IExpression[] ab = a.getBits();
		IExpression[] bb = b.getBits();
		if (ab.length < bb.length)
			ab = Int.pad(ab, bb.length);
		if (ab.length > bb.length)
			bb = Int.pad(bb, ab.length);
		return Int.sum(ab, bb);
	}
}
