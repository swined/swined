package net.swined.prime;

import java.math.BigInteger;

public class M {

  public final BigInteger k;
  public final int[] v;
  
  public M(BigInteger k, int[] v) {
    this.k = k;
    this.v = v;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(k);
    for (int v : this.v) {
      sb.append(" * x");
      sb.append(v);
    }
    return sb.toString();
  }
  
}
