package org.swined.zpol;

import java.math.BigInteger;
import java.util.WeakHashMap;

import org.swined.zpol.v1.IB;
import org.swined.zpol.v1.Int;
import org.swined.zpol.v1.Poly;



public class Main {

    private static final BigInteger WTF = new BigInteger("145");
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");
//    private static final BigInteger RSA120 = new BigInteger("227010481295437363334259960947493668895875336466084780038173258247009162675779735389791151574049166747880487470296548479");

    public static void main(String[] args) {
    	IB e1 = Int.meq(WTF), e2 = Int.meq(RSA100);
    	System.out.println(e1.getClass());
    	System.out.println(e2.getClass());
    	Poly p = e2.toPoly(2, new WeakHashMap<IB, Poly>());
    	System.out.println(p.getClass());
			System.out.println(p);
    }
	
}

