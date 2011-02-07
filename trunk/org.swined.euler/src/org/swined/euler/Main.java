package org.swined.euler;

import java.math.BigInteger;

public class Main {

  private static BigInteger bruteTotient(BigInteger n) {
    BigInteger r = BigInteger.ZERO;
    BigInteger d = BigInteger.ONE;
    while (n.compareTo(d) > 0) {
      if (n.gcd(d).equals(BigInteger.ONE))
        r = r.add(BigInteger.ONE);
      d = d.add(BigInteger.ONE);
    }
    return r;
  }
  
  public static void main(String[] args) {
    for (int i = 2; i < 100; i++) {
      BigInteger a = BigInteger.valueOf(i);
      BigInteger pa = bruteTotient(a);
      System.out.println(pa);
    }
  }
  
}
