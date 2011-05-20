package org.swined.euler;

import java.math.BigInteger;

public class Main {

  private static final int CERTAINTY = 100;
  private static final BigInteger TWO = BigInteger.valueOf(2);
  
  private static BigInteger phi(BigInteger n) {
    System.out.println("phi(" + n + ")");
    if (n.equals(BigInteger.ONE))
      return BigInteger.ONE;
    if (n.isProbablePrime(CERTAINTY))
      return n.subtract(BigInteger.ONE);
    int lsb = n.getLowestSetBit();
    if (lsb > 0)
      return phi(n.shiftRight(lsb)).multiply(TWO.pow(lsb - 1));
    BigInteger l = phi(n.subtract(BigInteger.ONE));
    for (BigInteger i = l; i.compareTo(n) < 0; i = i.add(TWO))
      if (TWO.modPow(i, n).equals(BigInteger.ONE))
        return i;
    throw new RuntimeException(n.toString());
  }
  
  public static void main(String[] args) {
    BigInteger n = new BigInteger("77777777777");
    System.out.println(phi(n));
  }
  
}

