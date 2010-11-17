package net.swined.prime;

import java.math.BigInteger;

public class Main {

  public static void main(String[] args) {
    final BigInteger mod = BigInteger.TEN;
    E e = new E(BigInteger.valueOf(421234355), mod);
    System.out.println(e);
    System.out.println(e.mod(mod));
  }
  
}
