package org.swined.zpol;

import java.math.BigInteger;

import org.swined.zpol.v1.IB;
import org.swined.zpol.v1.Int;



public class Main {

    private static final BigInteger WTF = new BigInteger("15");
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");
//    private static final BigInteger RSA120 = new BigInteger("227010481295437363334259960947493668895875336466084780038173258247009162675779735389791151574049166747880487470296548479");
//	
//    private static String toBinaryString(BigInteger n) {
//    	StringBuilder sb = new StringBuilder();
//    	for (int i = 0; i < n.bitLength(); i++)
//    		sb.append(n.testBit(n.bitLength() - 1 - i) ? "1" : "0");
//    	return sb.toString();
//    }
    
	public static void main(String... args) {
		test(WTF);
		test(RSA100);
	}

	private static void test(BigInteger n) {
		IB[] a = Int.vars(0, n.bitLength() - 1);
		IB[] b = Int.vars(a.length, a.length);
		IB[] m = Int.mul(a, b);
		IB e = Int.eq(m, n);
		System.out.println(e.getClass());
		if (n.bitLength() < 8)
			System.out.println(e);
	}
	
}

