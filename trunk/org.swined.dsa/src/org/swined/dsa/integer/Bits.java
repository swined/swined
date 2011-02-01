package org.swined.dsa.integer;

import java.util.Arrays;

import org.swined.dsa.IExpression;

public class Bits implements IInteger {

	private final IExpression[] bits;
	
	public Bits(IExpression[] bits) {
		this.bits = bits;
	}

	@Override
	public IExpression[] getBits() {
		return bits;
	}

	@Override
	public String toString() {
		return Arrays.toString(bits);
	}
	
}
