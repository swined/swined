package org.swined.euler;

import java.math.BigInteger;

public class Pair {

  public final BigInteger a;
  public final BigInteger b;
  
  public Pair(BigInteger a, BigInteger b) {
    this.a = a;
    this.b = b;
  }
  
  @Override
  public String toString() {
    return "<" + a + ", " + b + ">";
  }
  
}
