package net.swined.prime;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class E {

  public final M[] m;
  public final BigInteger c;
  
  public static E create(BigInteger c, BigInteger mod) {
    List<M> m = new ArrayList<M>();
    int l = l(c, mod);
    for (int i = 0; i < l; i++)
      for (int j = 0; i + j < l; j++)
        m.add(new M(mod.pow(i + j), new Integer[] { i, j + l } ));
    return new E(m.toArray(new M[0]), c);
  }

  public E(M[] m, BigInteger c) {
    this.m = m;
    this.c = c;
    for (M x : this.m)
      if (x.v.length == 0)
        throw new IllegalArgumentException();
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
  
  public E sub(Map<Integer, Integer> s) {
    E x = this;
    for (Integer k : s.keySet())
      x = x.sub(k, s.get(k));
    return x;
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
  
  public Integer getVar() {
    for (M m : this.m)
      for (int v : m.v)
        return v;
    return null;
  }
  
  public Map<Integer,Integer> brute(BigInteger mod) [] {
    if (m.length == 0) {
      if (c.mod(mod).equals(BigInteger.ZERO))
        return new Map [] { new HashMap() };
      else
        return new Map [] { };
    }
    Integer var = getVar();
    List<Map> solutions = new ArrayList();
    for (int i = 0; i < mod.intValue(); i++) {
      E sub = sub(var, i);
      for (Map<Integer, Integer> solution : sub.brute(mod)) {
        solution.put(var, i);
        solutions.add(solution);
      }
    }
    return solutions.toArray(new Map[0]);
  }
  
  public Map<Integer,Integer> solve(BigInteger mod) [] {
    if (m.length == 0) {
      if (c.equals(BigInteger.ZERO))
        return new Map [] { new HashMap() };
      else
        return new Map [] { };
    }
    E m = mod(mod);
    List<Map> solutions = new ArrayList();
    for (Map<Integer, Integer> solution : m.brute(mod)) {
      for (Map s : sub(solution).div(mod).solve(mod)) {
        Map<Integer, Integer> x = new HashMap();
        x.putAll(solution);
        x.putAll(s);
        solutions.add(x);
      }
    }    
    return solutions.toArray(new Map[0]);
  }

}
