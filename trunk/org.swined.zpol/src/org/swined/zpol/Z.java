package org.swined.zpol;

import java.math.BigInteger;

public class Z {

	private final BigInteger p;
	
	private Z(BigInteger p) {
		this.p = p;
	}

	public static Z m(BigInteger m) {
		return new Z(BigInteger.ZERO.setBit(m.intValue()));
	}
	
	public static Z c(boolean value) {
		return new Z(value ? BigInteger.ONE : BigInteger.ZERO);
	}
	
	public static Z v(int name) {
		return new Z(BigInteger.ZERO.setBit(1 << name));
	}
	
	public Z and(Z x) {
		BigInteger r = BigInteger.ZERO;
		for (int i = 0; i < p.bitLength(); i++)
			if (p.testBit(i))
				for (int j = 0; j < x.p.bitLength(); j++)
					if (x.p.testBit(j))
						r = r.flipBit(i | j);
		return new Z(r);
	}

	public Z xor(Z x) {
		return new Z(p.xor(x.p));
	}
	
	public Z xor(Z x, Z y) {
		return new Z(p.xor(x.p).xor(y.p));
	}
	
	public Z m2(Z x, Z y) {
		return x.and(y).xor(and(x), and(y));
	}

	@Override
	public String toString() {
		return toString("x");
	}
	
	public String toString(String v) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < p.bitLength(); i++)
			if (p.testBit(i)) {
				if (sb.length() > 0)
					sb.append(" + ");
				sb.append(toString(BigInteger.valueOf(i), v));
			}
		if (sb.length() == 0)
			sb.append("0");
		return sb.toString();
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
	
	public BigInteger toInt() {
		return p;
	}
	
	public boolean isZero() {
		return p.equals(BigInteger.ZERO);
	}
	
}
