package net.swined.prime.digital;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class M {

  public final BigInteger k;
  public final Integer[] v;
  
  public M(BigInteger k, Integer[] v) {
    this.k = k;
    this.v = v;
  }
  
  public M sub(int v, int s) {
    BigInteger nk = k;
    List<Integer> nv = new ArrayList<Integer>();
    for (Integer c : this.v) {
      if (c == v)
        nk = nk.multiply(BigInteger.valueOf(s));
      else
        nv.add(c);
    }
    return new M(nk, nv.toArray(new Integer[0]));
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
