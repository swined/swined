package org.swined.dsa.integer;

import org.swined.dsa.IExpression;

public class Bit implements IInteger {

	private final IExpression bit;
	
	public Bit(IExpression bit) {
		this.bit = bit;
	}

	@Override
	public IExpression[] getBits() {
		return new IExpression[] { bit };
	}

	@Override
	public String toString() {
		return "[" + bit + "]";
	}
	
}
