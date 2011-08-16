package org.swined.zpol;

import java.math.BigInteger;

import org.swined.zpol.v1.IB;
import org.swined.zpol.v1.IB.Iterator;
import org.swined.zpol.v1.Int;
import org.swined.zpol.v1.Vars;



public class Main {

    private static final BigInteger WTF = new BigInteger("14");
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");

    private static void print(IB x) {
    	x.iterate(new Iterator() {
				
				@Override
				public void process(BigInteger n) {
					System.out.println(Vars.get(n));
				}
			});
    }
    
    private static BigInteger count(IB x) {
    	final BigInteger[] c = new BigInteger[] { BigInteger.ZERO };
    	x.iterate(new Iterator() {
				
				@Override
				public void process(BigInteger n) {
					c[0] = c[0].add(BigInteger.ONE);
				}
			});
    	return c[0];
    }
    
    public static void main(String[] args) {
    	IB e1 = Int.meq(WTF);
    	System.out.println(e1);
    	System.out.println(count(e1));
    	IB e2 = Int.meq(RSA100);
    	System.out.println(e2.getClass());
    	System.out.println(count(e2));
    }
	
}

