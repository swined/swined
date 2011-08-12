package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Poly {

	private final int limit;
	private final Set<BigInteger> poly = new HashSet<BigInteger>();

	private Poly(int limit) {
		this.limit = limit;
	}

	public static Poly zero(int limit) {
		return new Poly(limit);
	}
	
	public static Poly get(int limit, BigInteger vars) {
		Poly z = new Poly(limit);
		z.push(vars);
		return z;
	}

	private void push(BigInteger m) {
		if (m.bitCount() > limit)
			return;
		if (poly.contains(m))
			poly.remove(m);
		else
			poly.add(m);
	}
	
	public static Poly and(Poly a, Poly b) {
		if (a.limit != b.limit)
			throw new UnsupportedOperationException();
		Poly r = new Poly(a.limit);
		for (BigInteger ma : a.poly)
			for (BigInteger mb : b.poly)
				r.push(ma.or(mb));
		return r;
	}

	public static Poly xor(Poly a, Poly b) {
		if (a.limit != b.limit)
			throw new UnsupportedOperationException();
		Poly r = new Poly(a.limit);
		for (BigInteger m : a.poly)
			r.push(m);
		for (BigInteger m : b.poly)
			r.push(m);
		return r;
	}
	
	@Override
	public String toString() {
		return toString("x");
	}
	
	public String toString(String v) {
		StringBuilder sb = new StringBuilder();
		for (BigInteger m : poly) {
			if (sb.length() > 0)
				sb.append(" + ");
			sb.append(toString(m, v));
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
	
	
}
