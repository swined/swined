package org.swined.zpol;

import java.math.BigInteger;
import java.util.WeakHashMap;

public class N {

	public static BigInteger div(BigInteger n) {
		IB[] a = I.var(0, n.bitLength() - 1);
		IB[] b = I.var(a.length, a.length);
		IB e = I.eq(I.mul(a, b), n);
		System.out.println(B.sat(e));
		return null;
//		while (true) {
//			int v = e.getVar();
//			if (v < 0)
//				break;
//			System.out.print(".");
//			BigInteger f = e.getNonFreeVars(new WeakHashMap<IB, BigInteger>());
//			if (f.equals(BigInteger.ZERO)) {
//				throw new RuntimeException();
//			} else
//				for (int j = 0; j < f.bitLength(); j++)
//					if (f.testBit(j)) {
//						C c = C.ONE;
//						e = B.sub(e, v, c);
//						for (int i = 0; i < a.length; i++)
//							a[i] = B.sub(a[i], v, c);
//						for (int i = 0; i < b.length; i++)
//							b[i] = B.sub(b[i], v, c);
//					}
//		}
//		System.out.println();
//		if (e != C.ONE)
//			throw new RuntimeException("no divisors found");
//		BigInteger na = I.toInt(a);
//		BigInteger nb = I.toInt(b);
//		BigInteger nn = na.multiply(nb);
//		if (!nn.equals(n))
//			throw new RuntimeException("wrong divisor found");
//		return na;
	}
	
}
