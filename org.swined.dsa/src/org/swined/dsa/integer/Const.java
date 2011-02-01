package org.swined.dsa.integer;

import java.math.BigInteger;

import org.swined.dsa.IExpression;
import org.swined.dsa.Int;

public class Const implements IInteger {

	public final BigInteger value;
	
	public Const(BigInteger value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "C";//value.toString();
	}

	@Override
	public IExpression[] getBits() {
		return Int.toExp(value);
	}
	
}
