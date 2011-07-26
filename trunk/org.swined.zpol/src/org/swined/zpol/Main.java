package org.swined.zpol;

import java.math.BigInteger;


public class Main {

//    private static final BigInteger WTF = new BigInteger("156");
//    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");
//    private static final BigInteger RSA120 = new BigInteger("227010481295437363334259960947493668895875336466084780038173258247009162675779735389791151574049166747880487470296548479");
//	
//    private static String toBinaryString(BigInteger n) {
//    	StringBuilder sb = new StringBuilder();
//    	for (int i = 0; i < n.bitLength(); i++)
//    		sb.append(n.testBit(n.bitLength() - 1 - i) ? "1" : "0");
//    	return sb.toString();
//    }
    
	public static void main(String... args) {
		Z[] a = new Z[3];
		Z[] b = new Z[a.length];
		for (int i = 0; i < a.length; i++) {
			a[i] = Z.v(2 * i);
			b[i] = Z.v(2 * i + 1);
		}
		Z[] m = I.mul(a, b);
		BigInteger mask = BigInteger.ZERO.setBit(a.length * 2).subtract(BigInteger.ONE);
		for (BigInteger i = BigInteger.ZERO; i.compareTo(mask) <= 0; i = i.add(BigInteger.ONE)) {
			BigInteger co = mask;
			Z z = Z.c(true);
			for (int j = 0; j < mask.bitLength(); j++)
				if (i.testBit(j)) {
					z = z.and(m[j]);
					co = co.clearBit(j);
				}
//			Z cp = Z.m(co);
//			if (!z.isZero()) {
//				System.out.print(cp.toString("c"));
//				System.out.print(" * ");
				System.out.println(z.toInt());
//			}
				
				// x0 & (10)
				// 10 & 100
		}
	}
	
}

