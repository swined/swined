package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Poly implements IB {

	private final Set<BigInteger> poly = new HashSet<BigInteger>();

	private Poly() {
		
	}

	public static Poly get(int a, int b) {
		BigInteger p = BigInteger.ZERO;
		p = p.setBit(a).setBit(b);
		Poly z = new Poly();
		z.poly.add(p);
		return z;
	}
	
	private void push(BigInteger m) {
		if (poly.contains(m))
			poly.remove(m);
		else
			poly.add(m);
	}
	
	public static Poly and(Poly a, Poly b) {
		Poly r = new Poly();
		for (BigInteger ma : a.poly)
			for (BigInteger mb : b.poly)
				r.push(ma.or(mb));
		return r;
	}

	public static Poly xor(Poly a, Poly b) {
		Poly r = new Poly();
		for (BigInteger m : a.poly)
			r.push(m);
		for (BigInteger m : b.poly)
			r.push(m);
		return r;
	}
	
	@Override
	public Poly sub(int v, boolean c, Map<IB, IB> ctx) {
		Poly r = (Poly)ctx.get(this);
		if (r == null) {
			ctx.put(this, r = new Poly());
			for (BigInteger m : poly)
				if (m.testBit(v)) {
					if (c)
						r.push(m.clearBit(v));
				} else
					r.push(m);
		}
		return r;
	}
	
	public static List<Poly> bitCount(List<Poly> a) {
		List<Poly> overflow = new ArrayList<Poly>();
		List<Poly> current = new ArrayList<Poly>(a);
		if (current.size() == 0)
			return current;
		while (current.size() > 1) {
			List<Poly> next = new ArrayList<Poly>();
			while (current.size() > 2) {
				Poly x = current.remove(0);
				Poly y = current.remove(0);
				overflow.add(Poly.and(x, y));
				next.add(Poly.xor(x, y));
			}
			next.addAll(current);
			current = next;
		}
		overflow = bitCount(overflow);
		overflow.add(0, current.get(0));
		return overflow;
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
