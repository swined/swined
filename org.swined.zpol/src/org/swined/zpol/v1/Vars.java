package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.Map;

public class Vars implements IB {

	public final BigInteger vars;
	
	private Vars(BigInteger vars) {
		this.vars = vars;
	}

	public static IB get() {
		return get(BigInteger.ZERO);
	}
	
	public static IB get(int v) {
		return get(BigInteger.ZERO.setBit(v));
	}

	public static IB get(BigInteger v) {
		return new Vars(v);
	}
	
	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
		if (vars.testBit(v))
			return c ? get(vars.clearBit(v)) : Const.ZERO;
		else return this;
	}
	
	@Override
	public String toString() {
		return toString(vars, "x");
	}
	
	private static String toString(BigInteger n, String v) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n.bitLength(); i++)
			if (n.testBit(i)) {
				if (sb.length() > 0)
					sb.append(" & ");
				sb.append(v);
				sb.append(i);
			}
		if (sb.length() == 0)
			sb.append("1");
		return sb.toString();
	}

	@Override
	public Poly toPoly(int limit, Map<IB, Poly> ctx) {
		Poly r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = Poly.get(limit, vars));
		return r;
	}
	
	
}
