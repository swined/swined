package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Poly {

	private final BigInteger mask;
	private final List<BigInteger> poly = new ArrayList<BigInteger>();

	private Poly(BigInteger mask) {
		this.mask = mask;
	}

	public static Poly zero(BigInteger mask) {
		return new Poly(mask);
	}
	
	public static Poly get(BigInteger mask, BigInteger vars) {
		Poly z = new Poly(mask);
		z.push(vars);
		return z;
	}

	private void push(BigInteger m) {
		if (!m.and(mask).equals(m))
			return;
		if (poly.contains(m))
			poly.remove(m);
		else
			poly.add(m);
	}
	
	public static Poly and(Poly a, Poly b) {
		if (!a.mask.equals(b.mask))
			throw new UnsupportedOperationException();
		Poly r = new Poly(a.mask);
		for (BigInteger ma : a.poly)
			for (BigInteger mb : b.poly)
				r.push(ma.or(mb));
		return r;
	}

	public static Poly xor(Poly a, Poly b) {
		if (!a.mask.equals(b.mask))
			throw new UnsupportedOperationException();
		Poly r = new Poly(a.mask);
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
