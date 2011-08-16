package org.swined.zpol;

import java.math.BigInteger;

import org.swined.zpol.v1.IB;
import org.swined.zpol.v1.Int;



public class Main {

    private static final BigInteger WTF = new BigInteger("14");
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");

    public static void main(String[] args) {
    	IB e1 = Int.meq(WTF);
    	System.out.println(e1);
    	IB e2 = Int.meq(RSA100);
    	System.out.println(e2.getClass());
    }
	
}

