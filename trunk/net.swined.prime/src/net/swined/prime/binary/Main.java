package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

  private static Var[] var(String n, int l) {
    Var[] e = new Var[l];
    for (int i = 0; i < l; i++)
      e[i] = new Var(n + i);
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
  
  private static IExpression split(IExpression e) {
    Set<Var> vars = vars(e);
    if (vars.isEmpty())
      return e;
    Var var = vars.iterator().next();
    IExpression p = e.sub(var, Const.ONE, new HashMap<IExpression, IExpression>());
    IExpression n = e.sub(var, Const.ZERO, new HashMap<IExpression, IExpression>());
    return var.and(split(p)).or(var.not().and(split(n)));
  }
  
  private static IExpression eq(IExpression[] e, BigInteger n) {
	  IExpression r = Const.ONE;
	  for (int i = 0; i < e.length; i++) {
	    System.out.println(i + "/" + e.length);
      IExpression x = n.testBit(i) ? e[i] : e[i].not();
      r = split(r.and(x));
      List<Map<Var, Const>> solution = solve(r);
      if (solution.size() == 1) {
        Map<Var, Const> s = solution.get(0);
        System.out.println(s);
        for (Var v : s.keySet()) {
          r = r.sub(v, s.get(v), new HashMap<IExpression, IExpression>());
          for (int j = 0; j < e.length; j++)
            e[j] = e[j].sub(v, s.get(v), new HashMap<IExpression, IExpression>());
        }
      } else {
        System.out.println(solution.size());
      }
    }
	  return r;
  }
  
  private static Set<Var> vars(IExpression e) {
    Set<Var> vars = new HashSet<Var>();
    e.getVars(vars);
    return vars;
  }
  
  private static IExpression eq(BigInteger n) {
    int l = n.bitLength() / 2 + n.bitLength() % 2;
    return eq(mul(var("x", l), var("y", l)), n);
  }

  private static String toBinary(BigInteger n) {
    StringBuilder sb = new StringBuilder();
    for (int i = n.bitLength(); i > 0; i--)
      sb.append(n.testBit(i - 1) ? "1" : "0");
    sb.append("b");
    return sb.toString();
  }
  
  private static List<Map<Var, Const>> solve(IExpression eq) {
    List<Map<Var, Const>> solutions = new ArrayList<Map<Var, Const>>();
    if (eq instanceof Const) {
      if (eq == Const.ONE)
        solutions.add(new HashMap<Var, Const>());
      return solutions;
    }
    Set<Var> vars = vars(eq);
    Var var = vars.iterator().next();
    for (Const c : Const.values()) {
      IExpression x = eq.sub(var, c, new HashMap<IExpression, IExpression>());
      List<Map<Var, Const>> s = solve(x);
      for (Map<Var, Const> t : s)
        t.put(var, c);
      solutions.addAll(s);
    }
    return solutions;
  }
  
  public static void main(String[] args) {
    BigInteger n = new BigInteger("9173503109612304981237094773");//9173503");
    System.out.println(toBinary(n));
    solve(eq(n));
  }
}
