package net.swined.prime;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class E {

  public final M[] m;
  public final BigInteger c;
  
  public E(BigInteger c, BigInteger mod) {
    List<M> m = new ArrayList<M>();
    int l = l(c, mod);
    for (int i = 0; i < l; i++)
      for (int j = 0; j < l; j++)
        m.add(new M(mod.pow(i + j), new Integer[] { i, i + l } ));
    this.m = m.toArray(new M[0]);
    this.c = c;
  }

  public E(M[] m, BigInteger c) {
    this.m = m;
    this.c = c;
  }

  public E sub(int v, int s) {
    List<M> nm = new ArrayList<M>();
    BigInteger nc = c;
    for (M x : m) {
      x = x.sub(v, s);
      if (x.v.length == 0)
        nc = nc.subtract(x.k);
      else
        nm.add(x);
    }
    return new E(nm.toArray(new M[0]), nc);
  }
  
  private static int l(BigInteger c, BigInteger mod) {
    for (int l = 0; ; l++)
      if (c.equals(BigInteger.ZERO))
        return l;
      else
        c = c.divide(mod);
  }
  
  public E mod(BigInteger mod) {
    List<M> m = new ArrayList<M>();
    for (M x : this.m) {
      BigInteger r = x.k.mod(mod);
      if (!r.equals(BigInteger.ZERO))
        m.add(new M(r, x.v));
    }
    return new E(m.toArray(new M[0]), c.mod(mod));
  }
  
  public E div(BigInteger mod) {
    List<M> m = new ArrayList<M>();
    for (M x : this.m) {
      BigInteger r = x.k.divide(mod);
      if (!r.equals(BigInteger.ZERO))
        m.add(new M(r, x.v));
    }
    return new E(m.toArray(new M[0]), c.divide(mod));
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("0");
    for (M m : this.m) {
      sb.append(" + ");
      sb.append(m);
    }
    sb.append(" = ");
    sb.append(c);
    return sb.toString();
  }
}
