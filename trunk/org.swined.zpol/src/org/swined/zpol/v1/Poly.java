package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.HashSet;
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
	
	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
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
	
}
