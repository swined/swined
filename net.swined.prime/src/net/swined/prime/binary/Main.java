package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

  private static IExpression[] var(int n, int l) {
    IExpression[] e = new IExpression[l];
    for (int i = 0; i < l; i++)
      e[i] = Conjunction.var(n + i, false);
    return e;
  }

  private static IExpression[] zero(int l) {
    IExpression[] e = new IExpression[l];
    for (int i = 0; i < l; i++)
      e[i] = Const.ZERO;
    return e;
  }
  
  private static IExpression xor(IExpression a, IExpression b) {
    return a.and(b.not()).or(a.not().and(b));
  }
  
  private static IExpression[] sum(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] q = new IExpression[a.length];
    IExpression f = Const.ZERO;
    for (int i = 0; i < q.length; i++) {
      q[i] = xor(xor(a[i], b[i]), f);
      f = a[i].and(b[i]).or(a[i].and(f)).or(b[i].and(f));
    }
    return q;
  }
  
  private static IExpression[] mul(IExpression[] a, IExpression[] b) {
    IExpression[] r = zero(a.length + b.length);
    for (int i = 0; i < a.length; i++) {
      IExpression[] t = zero(r.length);
      for (int j = 0; j < b.length; j++)
        t[i + j] = a[i].and(b[j]);
      r = sum(r, t);
    }
    return r;
  }
  
  private static IExpression eq(IExpression[] e, BigInteger n) {
	  IExpression r = Const.ONE;
	  for (int i = 0; i <  e.length; i++) {
      IExpression x = n.testBit(i) ? e[i] : e[i].not();
      System.out.println(solve(x));
      r = r.and(x);
    }
	  return r;
  }
  
  private static Set<Integer> vars(IExpression e) {
    Set<Integer> vars = new HashSet<Integer>();
    e.getVars(vars);
    return vars;
  }
  
  private static IExpression split(IExpression e) {
	  for (Integer x : vars(e)) {
	    IExpression px = e.sub(x, Const.ONE, new HashMap<IExpression, IExpression>());
	    IExpression nx = e.sub(x, Const.ZERO, new HashMap<IExpression, IExpression>());
	    e = Conjunction.var(x, false).and(px).or(Conjunction.var(x, true).and(nx));
	  }
	  return e;
  }

  private static IExpression eq(BigInteger n) {
    int l = n.bitLength() / 2 + n.bitLength() % 2;
    return eq(mul(var(0, l), var(l, l)), n);
  }

  private static String toBinary(BigInteger n) {
    StringBuilder sb = new StringBuilder();
    for (int i = n.bitLength(); i > 0; i--)
      sb.append(n.testBit(i - 1) ? "1" : "0");
    sb.append("b");
    return sb.toString();
  }
  
  private static List<Map<Integer, Const>> solve(IExpression eq) {
    List<Map<Integer, Const>> solutions = new ArrayList<Map<Integer, Const>>();
    if (eq instanceof Const) {
      if (eq == Const.ONE)
        solutions.add(new HashMap<Integer, Const>());
      return solutions;
    }
    Set<Integer> vars = vars(eq);
    int var = vars.iterator().next();
    for (Const c : Const.values()) {
      IExpression x = eq.sub(var, c, new HashMap<IExpression, IExpression>());
      List<Map<Integer, Const>> s = solve(x);
      for (Map<Integer, Const> t : s)
        t.put(var, c);
      solutions.addAll(s);
    }
    return solutions;
  }
  
  public static void main(String[] args) {
    BigInteger n = new BigInteger("9");//9173503");
    System.out.println(toBinary(n));
    System.out.println(solve(eq(n)));
//    System.out.println(n.bitLength() + " bit");
//    System.out.println(eq(n));
//    System.out.println(split(eq(n)));
  }
}
