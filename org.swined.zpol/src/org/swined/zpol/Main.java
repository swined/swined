package org.swined.zpol;

import java.math.BigInteger;


public class Main {

	public static void main(String... args) {
		BigInteger n = BigInteger.valueOf(27);
		for (int i = 1; i < 9; i++) {
			Z[] a = I.vars(0, i);
			Z[] b = I.vars(a.length, a.length);
			Z z = I.eq(I.mul(a, b), n);
			System.out.println(z);
		}
	}
	
}

