package org.swined.zpol;

import java.math.BigInteger;
import java.util.WeakHashMap;

public class B {

	public static IB sub(IB x, int v, C c) {
		return x.sub(v, c, new WeakHashMap<IB, IB>());
	}
	
	public static IB m2(IB a, IB b, IB c) {
		return X.get(A.get(a, b), A.get(X.get(a, b), c));
	}
	
	public static BigInteger getNonFreeVars(IB x) {
		return x.getNonFreeVars(new WeakHashMap<IB, BigInteger>());
	}
	
	public static boolean sat(IB x) {
		BigInteger f = getNonFreeVars(x);
		if (f.equals(BigInteger.ZERO)) {
			System.out.print(",");
			int v = x.getVar();
			if (v < 0)
				return x == C.ONE;
			IB p = sub(x, v, C.ONE);
			IB n = sub(x, v, C.ZERO);
			return sat(X.get(X.get(p, n), A.get(p, n)));
		} else {
			System.out.print(".");
			System.out.print(f.bitCount());
			for (int i = 0; i < f.bitLength(); i++)
				if (f.testBit(i))
					x = sub(x, i, C.ONE);
			return sat(x);
		}
	}
	
}
