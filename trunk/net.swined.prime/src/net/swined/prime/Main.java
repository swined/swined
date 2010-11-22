package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    final BigInteger mod = BigInteger.TEN;
    E e = E.create(BigInteger.valueOf(12), mod);
    System.out.println(e);
    for (Map<Integer, Integer> solution : e.solve(mod)) {
      System.out.println(solution);
    }
  }
  
}
