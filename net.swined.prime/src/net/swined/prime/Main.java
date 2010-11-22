package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    final BigInteger mod = BigInteger.valueOf(100);
    E e = E.create(BigInteger.valueOf(377377), mod);
    System.out.println(e);
    for (Map<Integer, Integer> solution : e.solve(mod)) {
      System.out.println(solution);
    }
  }
  
}
