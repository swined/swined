package org.swined.zpol;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Z {

	private final Set<BigInteger> poly = new HashSet<BigInteger>();
	
	private Z() {
	}

	private static void push(Z z, BigInteger m) {
		if (z.poly.contains(m))
			z.poly.remove(m);
		else
			z.poly.add(m);
	}
	
	public static Z c(boolean value) {
		Z z = new Z();
		if (value)
			z.poly.add(BigInteger.ZERO);
		return z;
	}
	
	public static Z v(int name) {
		Z z = new Z();
		z.poly.add(BigInteger.ZERO.setBit(name));
		return z;
	}
	
	public Z and(Z x) {
		Z z = new Z();
		for (BigInteger a : poly)
			for (BigInteger b : x.poly)
				push(z, a.or(b));
		return z;
	}

	public Z xor(Z x) {
		Z z = new Z();
		z.poly.addAll(poly);
		for (BigInteger a : x.poly)
			push(z, a);
		return z;
	}
	
	public Z xor(Z x, Z y) {
		Z z = new Z();
		z.poly.addAll(poly);
		for (BigInteger a : x.poly)
			push(z, a);
		for (BigInteger a : y.poly)
			push(z, a);
		return z;
	}
	
	public Z m2(Z x, Z y) {
		return x.and(y).xor(and(x), and(y));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (BigInteger n : poly) {
			if (sb.length() > 0)
				sb.append(" + ");
			sb.append(toString(n));
		}
		if (sb.length() == 0)
			sb.append("0");
		return sb.toString();
	}
	
	private static String toString(BigInteger n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n.bitLength(); i++)
			if (n.testBit(i)) {
				if (sb.length() > 0)
					sb.append(" & ");
				sb.append("x");
				sb.append(i);
			}
		if (sb.length() == 0)
			sb.append("1");
		return sb.toString();
	}
	
	public int complexity() {
		return poly.size();
	}
	
}
