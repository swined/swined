package org.swined.euler;

import java.math.BigInteger;

public class Main {

  private static final Number[][] hints3 = new Number[][] {
	  {0, 0.4}, // d
	  {1, 0.6}, // u
	  {2, 0},
  };
	
  private static final int CERTAINTY = 100;
  private static final BigInteger TWO = BigInteger.valueOf(2);
  
  private static BigInteger phi(BigInteger n) {
    System.out.println("phi(" + n + ")");
    // phi(1) == 1
    if (n.equals(BigInteger.ONE))
      return BigInteger.ONE;
    // phi(prime) = prime - 1
    if (n.isProbablePrime(CERTAINTY))
      return n.subtract(BigInteger.ONE);
    // phi(2^a * n) = 2 ^ (a - 1) * phi(n)
    int lsb = n.getLowestSetBit();
    if (lsb > 0)
      return phi(n.shiftRight(lsb)).multiply(TWO.pow(lsb - 1));
    // hints && brute
    BigInteger d = phi(n.subtract(BigInteger.ONE));
    BigInteger u = n;
    for (BigInteger i = d; i.compareTo(u) < 0; i = i.add(TWO))
      if (TWO.modPow(i, n).equals(BigInteger.ONE))
        return i;
    // oops
    throw new RuntimeException(n.toString());
  }
  
  public static void main(String[] args) {
    BigInteger n = new BigInteger("777777777777666667777777");
    n = n.nextProbablePrime();
    n = n.multiply(n.nextProbablePrime());
    System.out.println(n);
    //System.out.println(phi(n));
  }
  
}

