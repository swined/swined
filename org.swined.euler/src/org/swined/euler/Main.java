package org.swined.euler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  private static BigInteger[] p(BigInteger n) {
	  BigInteger d = BigInteger.valueOf(2);
	  BigInteger m = BigInteger.ONE;
	  List<BigInteger> r = new ArrayList<BigInteger>();
	  while (m.multiply(m).compareTo(n) < 0) {
		  r.add(d);
		  d = d.nextProbablePrime();
		  m = m.multiply(d);
	  }
	  return r.toArray(new BigInteger[r.size()]);
  }
	
  private static BigInteger m(BigInteger[] a) {
	  BigInteger r = BigInteger.ONE;
	  for (BigInteger x : a)
		  r = r.multiply(x);
	  return r;
  }
  
  private static BigInteger[] e(BigInteger[] n) {
	  BigInteger[] r = new BigInteger[n.length];
	  BigInteger m = m(n);
	  for (int i = 0; i < r.length; i++) {
		  BigInteger d = m.divide(n[i]);
		  r[i] = egcd(n[i], d).b.multiply(d);
	  }
	  return r;
  }
  
  private static Pair egcd(BigInteger a, BigInteger b) {
	  if (a.mod(b).equals(BigInteger.ZERO))
		  return new Pair(BigInteger.ZERO, BigInteger.ONE);
	  Pair x = egcd(b, a.mod(b));
	  return new Pair(x.b, x.a.subtract(x.b.multiply(a.divide(b))));
  }
  
  public static void main(String[] args) {
    BigInteger n = new BigInteger("10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    n = n.nextProbablePrime();
    n = n.multiply(n.nextProbablePrime().nextProbablePrime().nextProbablePrime().nextProbablePrime());
    System.out.println(Arrays.toString(p(n)));
    BigInteger m = BigInteger.ONE;
    BigInteger d = BigInteger.valueOf(2);
    int c = 0;
    while (d.compareTo(n) < 0 && m.multiply(m).compareTo(n) < 0) {
    	if (n.mod(d).isProbablePrime(100)) {
    		System.out.println(d);
    		m = m.multiply(d);
    		c++;
    	}
    	d = d.nextProbablePrime();
    }
    System.out.println();
    System.out.println(n.bitLength());
    System.out.println(c);
  }
  
}

