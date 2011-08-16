package org.swined.zpol.v1;

import java.math.BigInteger;


public class And implements IB {

	public final IB a;
	public final IB b;
	
	private And(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	private static boolean isOne(IB x) {
		return x instanceof Vars && ((Vars) x).vars.equals(BigInteger.ZERO);
	}
	
	public static IB get(IB a, IB b) {
		if (a == Const.ZERO || b == Const.ZERO)
			return Const.ZERO;
		if (isOne(a))
			return b;
		if (isOne(b))
			return a;
		if (a instanceof Vars && b instanceof Vars)
			return Vars.get(((Vars)a).vars.or(((Vars)b).vars));
		return new And(a, b);
	}

	@Override
	public String toString() {
		return String.format("(%s & %s)", a, b);
	}

}
