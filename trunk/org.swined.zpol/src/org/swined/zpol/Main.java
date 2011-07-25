package org.swined.zpol;

import java.math.BigInteger;


public class Main {

    private static final BigInteger WTF = new BigInteger("156");
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");
    private static final BigInteger RSA120 = new BigInteger("227010481295437363334259960947493668895875336466084780038173258247009162675779735389791151574049166747880487470296548479");
	
	public static void main(String... args) {
		System.out.println(N.div(RSA100));
	}
	
}

