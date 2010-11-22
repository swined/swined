package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    final BigInteger mod = BigInteger.TEN;
    E e = E.create(BigInteger.valueOf(355), mod);
    System.out.println(e);
    System.out.println(e = e.mod(mod));
    for (Map<Integer, Integer> solution : e.brute(mod)) {
      System.out.println(solution);
    }
  }
  
}
