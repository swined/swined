package org.swined.zpol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



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
    
    public static void guess(BigInteger n, List<Integer> g, int... b) {
    	if (n.equals(BigInteger.ZERO)) {
    		for (int i : g)
    			System.out.print(i);
    		System.out.println();
    		return;
    	}
    	if (b.length == 0)
    		return;
    	int ix = b.length - 1;
    	for (int i = 0; i <= b[ix]; i++) {
    		BigInteger t = n.subtract(BigInteger.valueOf(i).shiftLeft(ix));
    		if (t.compareTo(BigInteger.ZERO) >= 0) {
    			List<Integer> o = new ArrayList<Integer>(g);
    			o.add(i);
    			guess(t, o, Arrays.copyOf(b, ix));
    		}
    	}
    }
    
    
	public static void main(String[] args) {
		guess(BigInteger.valueOf(43), new ArrayList<Integer>(), 1, 2, 3, 4, 5, 4, 3, 2, 1);
//		int c = 7;
//		BigInteger[] b = new BigInteger[c];
//		for (int i = 0; i < c; i++)
//			b[i] = BigInteger.ZERO.setBit(i).setBit(c - i - 1);
//		List<Poly> p = new ArrayList<Poly>();
//		for (int i = 0; i < c; i++)
//			p.add(Poly.get(i, c - i - 1));
//		System.out.println(Poly.bitCount(p));
//		System.out.println(Arrays.toString(Poly.bitCount1(b)));
////		test(WTF);
////		test(RSA100);
	}

//	private static void test(BigInteger n) {
//		IB[] a = Int.vars(0, n.bitLength() - 1);
//		IB[] b = Int.vars(a.length, a.length);
//		IB[] m = Int.mul(a, b);
//		IB e = Int.eq(m, n);
//		System.out.println(e.getClass());
//		if (n.bitLength() < 8)
//			System.out.println(e);
//	}
	
}

