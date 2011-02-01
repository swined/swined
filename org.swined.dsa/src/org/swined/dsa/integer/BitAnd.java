package org.swined.dsa.integer;

import org.swined.dsa.Bin;
import org.swined.dsa.IExpression;

public class BitAnd implements IInteger {

	private final IInteger a;
	private final IExpression b;
	
	public BitAnd(IInteger a, IExpression b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public String toString() {
		return "bm(" + a + ", " + b + ")";
	}

	@Override
	public IExpression[] getBits() {
		IExpression[] ab = a.getBits();
		IExpression[] r = new IExpression[ab.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Bin.and(ab[i], b);
		return r;
	}
	
}
