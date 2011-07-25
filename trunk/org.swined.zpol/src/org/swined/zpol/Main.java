package org.swined.zpol;

import java.math.BigInteger;


public class Main {

	
	
	public static void main(String... args) {
		BigInteger n = BigInteger.valueOf(27);
		Z[] a = I.vars(0, 8);
		Z[] b = I.vars(a.length, a.length);
		Z[] e = I.mul(a, b);
//		for (Z z : e)
//			System.out.println(z);
		System.out.println(I.eq(e, n));
	}
	
}

